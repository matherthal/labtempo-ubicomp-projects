package br.uff.tempo.apps.map.rule;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class RuleComposeBar extends HUD {

	private static float MAX_ALPHA = 0.5f;
	private static float MIN_ALPHA = 0f;
	
	private Rectangle backgroundRect;
	private ExpandablePanel panelCondition;
	private ExpandablePanel panelAction;
	
	private float x = 0f;
	private float y = 0f;
	private float width;
	private float height;
	
	public RuleComposeBar(final Camera camera, final VertexBufferObjectManager vertexBuffObjMan) {
		
		super();
		
		setCamera(camera);
		
		// Defining the side bar dimensions
		this.width = camera.getWidth() * 0.25f; // 25% of the width
		this.height = camera.getHeight();       // 100% of the height
		
		// Initializing the background
		backgroundRect = new Rectangle(this.x, this.y, this.width, this.height, vertexBuffObjMan);
		backgroundRect.setColor(Color.BLACK);
		
		// Initializing the Panels to setup Conditions and Actions
		panelCondition = new ExpandablePanel(vertexBuffObjMan);
		panelAction    = new ExpandablePanel(vertexBuffObjMan);

		attachChild(backgroundRect);
		setVisible(false);
	}
	
	public void setBackgroundColor(Color color) {
		backgroundRect.setColor(color);
	}
	
	public Color getBackgroundColor() {
		return backgroundRect.getColor();
	}
	
	public void show() {
		
		this.setVisible(true);
		
		showEntities();
	}
	
	public void dismiss() {
		hideEntities();
	}
	
	private void hideEntities() {
		
		int childCount = this.getChildCount();
		
		for (int i = 0; i < childCount; i++) {
			
			IEntity entity = this.getChildByIndex(i);
			applyAlphaModifier(entity, MAX_ALPHA, MIN_ALPHA);
		}
	}
	
	private void showEntities() {
		
		int childCount = this.getChildCount();
		
		for (int i = 0; i < childCount; i++) {
			
			IEntity entity = this.getChildByIndex(i);
			applyAlphaModifier(entity, MIN_ALPHA, MAX_ALPHA);
		}
	}
	
	private void applyAlphaModifier(final IEntity entity, final float fromAlpha, final float toAlpha) {
		
		entity.registerEntityModifier(new AlphaModifier(2, fromAlpha, toAlpha));
	}
}
