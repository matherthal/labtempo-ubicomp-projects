package br.uff.tempo.apps.map.rule;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import br.uff.tempo.apps.map.utils.Transformations;

import android.view.animation.Transformation;

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
		
		this(camera, vertexBuffObjMan, camera.getWidth() * 0.25f, camera.getHeight());
	}
	
	public RuleComposeBar(final Camera camera, final VertexBufferObjectManager vertexBuffObjMan, float width, float height) {
		super();
		
		setCamera(camera);
		
		// Defining the side bar dimensions
		this.width = width;
		this.height = height;
		
		// Initializing the background
		backgroundRect = new Rectangle(this.x, this.y, this.width, this.height, vertexBuffObjMan);
		backgroundRect.setColor(Color.BLACK);
		
		// Initializing the Panels to setup Conditions and Actions
//		panelcondition = new expandablepanel(vertexbuffobjman);
//		panelaction    = new expandablepanel(vertexbuffobjman);

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
		
		Transformations.applyAlphaModifierInChindren(this, MIN_ALPHA, MAX_ALPHA);
	}
	
	public void dismiss() {
		Transformations.applyAlphaModifierInChindren(this, MAX_ALPHA, MIN_ALPHA);
		this.setVisible(false);
	}
}
