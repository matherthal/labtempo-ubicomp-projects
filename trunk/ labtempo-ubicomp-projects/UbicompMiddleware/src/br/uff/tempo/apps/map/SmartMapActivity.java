package br.uff.tempo.apps.map;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Dialog;
import android.widget.Toast;
import br.uff.tempo.apps.map.dialogs.IDialogFinishHandler;
import br.uff.tempo.apps.map.dialogs.YesNoDialog;
import br.uff.tempo.apps.map.dialogs.YesNoGetter;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.ResourceIcon;
import br.uff.tempo.apps.map.objects.notification.NotificationBox;
import br.uff.tempo.apps.map.objects.sprite.ISpriteController;
import br.uff.tempo.apps.map.objects.sprite.SmartSprite;
import br.uff.tempo.apps.map.objects.sprite.SmartSpriteFactory;
import br.uff.tempo.apps.map.rule.ContextMenu;
import br.uff.tempo.apps.map.rule.ContextMenuItem;
import br.uff.tempo.apps.map.rule.IContextMenuAction;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.utils.Position;

public class SmartMapActivity extends SmartAndroidMap implements ISpriteController, IContextMenuAction,
		IDialogFinishHandler, YesNoGetter {
	
	// Called when a resource is just created by the Menu
	@Override
	public void onResourceCreationFinished(ResourceWrapper wrapper) {
		
		if (wrapper != null) {
			//Get the appropriated texture
			ITextureRegion resTexture = getTextureFactory().getResourceTextureRegion(wrapper.getId());
			ResourceIcon icon = createSprite(resTexture, wrapper);
			
			InterfaceApplicationManager manager = getInterfaceManager();
			manager.addResource(wrapper.getStub().getRANS(), icon);
			
			// Register interest in all context variables
			wrapper.getStub().registerStakeholder("all", manager.getRANS());
		} else {
			toastOnUIThread("Error creating resource... try again", Toast.LENGTH_LONG);
		}
	}

	public ResourceIcon createSprite(ITextureRegion textureRegion, final ResourceWrapper wrapper) {
		
		// It's initially positioned at (x,y) position (pixels)
		Position pos = wrapper.getStub().getPosition();
		if (pos == null) {
			pos = new Position(0f, 0f); // If unknown, that is the starting position
			wrapper.getStub().identifyPosition(pos);
		}
		
		float x = getMap().metersToPixel(pos.getX());
		float y = getMap().metersToPixel(getMap().invertYcoordinate(pos.getY()));

		VertexBufferObjectManager vertexBufferObj = this.getVertexBufferObjectManager();
		Sprite sprite = SmartSpriteFactory.createSprite(wrapper.getId(), x, y, textureRegion, vertexBufferObj, this, wrapper, getMap(), this);

		// Creates a box that will show notification texts near the sprite
		NotificationBox box = new NotificationBox(sprite.getWidth(),
				sprite.getHeight(), vertexBufferObj, this.getFontManager(), this.getTextureManager());

		getScene().registerTouchArea(sprite);
		getScene().attachChild(sprite);
		
		return new ResourceIcon(sprite, box, wrapper);
	}
	
	@Override
	public void onSpriteStartLongPress(SmartSprite sprite,	TouchEvent pSceneTouchEvent) {
		// Vibrate the device for a while 
		this.mEngine.vibrate(VIBRATE_TIME);
	}

	@Override
	public void onSpriteLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent) {
		//Move the sprite
		sprite.setPosition(pSceneTouchEvent.getX() - sprite.getWidth() / 2, pSceneTouchEvent.getY() - sprite.getHeight() / 2);
	}

	@Override
	public void onSpriteEndLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent) {}

	@Override
	public void onSpriteTap(SmartSprite sprite, TouchEvent pSceneTouchEvent) {
		if (this.viewMode == INTERPRETER_MODE) {
			sprite.showContextMenu(getCamera());
		}
		else if (this.viewMode == ACTUATOR_MODE) {
			sprite.showOperationsMenu(getCamera());
		} else {
			getResourceCreator().callSimulator(sprite.getResourceWrapper().getStub(), sprite.getResourceWrapper().getSimulator());
		}
	}

	@Override
	public void onContextMenuAction(ContextMenu menu, ContextMenuItem itemSelected) {
		
		//toastOnUIThread(itemSelected.getLabel(), Toast.LENGTH_LONG);
		
		if (viewMode == INTERPRETER_MODE) {
			// Insert a context variable in the context rule expression
			ruleToolbar.setContextVariable(itemSelected.getRans(), itemSelected.getMethodName(), itemSelected.getLabel()); 
			
			//'show' operation must be executed in UI thread.
			//Remember that 'onContextMenuAction' method is a kind of event (callback) called by another thread
			//and you can't do UI operations from a non-ui thread.
			
			runOnUiThread(new Runnable() {			
				@Override
				public void run() {
					ruleToolbar.showDialog();
				}
			});
		} else if (viewMode == ACTUATOR_MODE) {
			//create action
			ruleComposer.addAction(itemSelected.getRans(), itemSelected.getMethodName(), new Object[0], itemSelected.getLabel());
			runOnUiThread(new Runnable() {			
				@Override
				public void run() {
					//call again dialog
					new YesNoDialog("Action", "Choose new Action?", "Yes", "No", SmartMapActivity.this, SmartMapActivity.this);
				}
			});
		}
	}

	@Override
	public void onDialogFinished(Dialog dialog) {
		// TODO:? Maybe get the string from RuleToobar here, instead of allow
		// RuleToolbar to write on RuleComposeBar
	}

	public void setMode(int actuatorMode) {
		this.viewMode = ACTUATOR_MODE;
		new YesNoDialog("Action", "Choose new Action?", "Yes", "No", this, this);
	}

	@Override
	public void onYesPressed() {}

	@Override
	public void onNoPressed() {
		try {
			this.ruleComposer.finish(this);
		} catch (Exception e) {
			throw new SmartAndroidRuntimeException("Error by finishing a context rule expression!", e);
		}
		viewMode = SIMULATOR_MODE;
	}
}
