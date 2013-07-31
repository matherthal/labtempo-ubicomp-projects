package br.uff.tempo.apps.map.rule;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import br.uff.tempo.apps.map.utils.Transformations;
import br.uff.tempo.middleware.management.Formula;
import br.uff.tempo.middleware.management.IRuleComposeListener;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.Predicate;
import br.uff.tempo.middleware.management.RuleComposer;

public class RuleComposeBar extends HUD implements IRuleComposeListener {
	private static final String TAG = "RuleComposeBar";
	
	private static float MAX_ALPHA = 0.5f;
	private static float MIN_ALPHA = 0f;
	public static final int FONT_SIZE = 20;
	
	private Rectangle backgroundRect;
	private ExpandablePanel panelCondition;
	private ExpandablePanel panelAction;
	
	private float x = 0f;
	private float y = 0f;
	private float width;
	private float height;
	private Font mFont;
	private Font titleFont;
	private Text leftText;
	private Text titleText;
	private VertexBufferObjectManager vertexBuffObjMan;
	private	FontManager menuFontManager;
	private TextureManager menuTextureManager;
	private int bracketLevel = 0;

	private RuleComposer composer = new RuleComposer();
	
	public RuleComposeBar(final Camera camera, final VertexBufferObjectManager vertexBuffObjMan,
			FontManager menuFontManager, TextureManager menuTextureManager) {
		
		this(camera, vertexBuffObjMan, camera.getWidth() * 0.25f, camera.getHeight(), menuFontManager,
				menuTextureManager);
	}
	
	public RuleComposeBar(final Camera camera, final VertexBufferObjectManager vertexBuffObjMan, float width,
			float height, FontManager menuFontManager, TextureManager menuTextureManager) {
		super();
		
		setCamera(camera);
		
		// Defining the side bar dimensions
		this.width = width;
		this.height = height;
		
		this.vertexBuffObjMan = vertexBuffObjMan;
		this.menuFontManager = menuFontManager;
		this.menuTextureManager = menuTextureManager;

		// Default title font config
		int FONT_SIZE_TITLE = 30;
		final ITexture fontTexture = new BitmapTextureAtlas(menuTextureManager, 256, 256, TextureOptions.BILINEAR);
//		this.titleFont = new Font(menuFontManager, fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE_TITLE, true, Color.WHITE);
//		this.titleFont.load();
//		float xtitle = 0f;
//		float ytitle = 0f;
//		this.titleText = new Text(xtitle, ytitle, this.titleFont, "Regra de Contexto", new TextOptions(HorizontalAlign.LEFT), vertexBuffObjMan);
//		backgroundRect.attachChild(titleText);
		
		// Default side text config 
		//final ITexture fontTexture = new BitmapTextureAtlas(menuTextureManager, 256, 256, TextureOptions.BILINEAR);
		this.mFont = new Font(menuFontManager, fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), FONT_SIZE, true, Color.WHITE);
		this.mFont.load();
		
		// Initializing the background
		backgroundRect = new Rectangle(this.x, this.y, this.width, this.height, vertexBuffObjMan);
		backgroundRect.setColor(Color.BLACK);
		
		// Initializing the Panels to setup Conditions and Actions
		// panelcondition = new expandablepanel(vertexbuffobjman);
		// panelaction = new expandablepanel(vertexbuffobjman);

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
		
//	public void btnAddCondition(String rai, String cv, Object[] params, long timeout) throws Exception {
//		composer.addCondition(rai, cv, params, timeout);
//	}
//	
//	public void btnAddConditionComp(String rai1, String cv1, Object[] params1, Operator op, String rai2, String cv2, Object[] params2, long timeout) throws Exception {
//		composer.addConditionComp(rai1, cv1, params1, op, rai2, cv2, params2, timeout);
//	}
//	
//	public void btnSetActuatorName(String name) {
//		composer.setActuatorName(name);
//	}
//	
//	public void btnAddAction(String rai, String serv, Object[] params) {
//		composer.addAction(rai, serv, params);
//	}
	
	public void appendTextLine(String line) {
		for (int i=0; i<bracketLevel; i++)
			line = "      " + line;
		line += "\n";
		if (leftText == null)
			leftText = new Text(this.x, this.y, this.mFont, line, new TextOptions(HorizontalAlign.LEFT), vertexBuffObjMan);
		else
			leftText = new Text(this.x, this.y, this.mFont, leftText.getText() + line, new TextOptions(HorizontalAlign.LEFT), vertexBuffObjMan);
		backgroundRect.attachChild(leftText);
	}

	public void openBracket() {
		this.bracketLevel++;
	}

	public void closeBracket() {
		this.bracketLevel--;
	}

	@Override
	public void onRuleCompositionChanged(String changeName) {
		
		if (changeName.equals("(")) {
			openBracket();
		} else if (changeName.equals(")")) {
			closeBracket();
		}
		
		appendTextLine(changeName);
	}

	@Override
	public void onRuleCompositionFinished() {
		backgroundRect.detachChildren();
		leftText = null;
	}

	@Override
	public void onInterpreterFinished() {
		backgroundRect.detachChildren();
		leftText = null;
	}

	@Override
	public void onActionAdded(String name) {
		appendTextLine(name);
	}
}
