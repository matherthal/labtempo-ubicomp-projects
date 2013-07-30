package br.uff.tempo.apps.map.rule;

import java.util.LinkedList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.util.Log;
import br.uff.tempo.apps.map.utils.Transformations;

/**
 * Shows a context menu to be used with AndEngine
 * 
 * @see IContextMenuAction
 * @author dbarreto
 * 
 */
public class ContextMenu extends Entity {

	// Constants
	private static float MAX_ALPHA = 0.8f;
	private static float MIN_ALPHA = 0f;
	public static final float DX = 10f;
	public static final float DY = 10f;
	public static final int FONT_SIZE = 20;
	public static final float DEFAULT_X = 0f;
	public static final float DEFAULT_Y = 0f;
	public static final float DEFAULT_WIDTH = 100f;
	public static final float DEFAULT_HEIGHT = 100f;

	// Fields
	private List<ContextMenuItem> items;
	private List<IContextMenuAction> actionListeners;
	private Rectangle backgroundMenu;
	private Font font;
	private Color textColor = Color.WHITE;
	private Color selectedTextColor = new Color(0.69f, 0.77f, 0.77f);
	private VertexBufferObjectManager vertexObj;

	// Constructors
	public ContextMenu(float pX, float pY, final IContextMenuAction action,
			final VertexBufferObjectManager vertexObj,
			FontManager menuFontManager, TextureManager menuTextureManager) {

		super.setPosition(pX, pY);
		setVisible(false);

		this.actionListeners = new LinkedList<IContextMenuAction>();
		this.actionListeners.add(action);

		this.backgroundMenu = new Rectangle(DEFAULT_X, DEFAULT_Y,
				DEFAULT_WIDTH, DEFAULT_HEIGHT, vertexObj) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				return ContextMenu.this.onAreaTouched(pSceneTouchEvent,
						pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		this.backgroundMenu.setColor(Color.BLACK);
		this.backgroundMenu.setVisible(true);
		attachChild(backgroundMenu);

		this.font = FontFactory.create(menuFontManager, menuTextureManager,
				256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),
				FONT_SIZE, this.textColor.getARGBPackedInt());
		this.font.load();

		this.vertexObj = vertexObj;
		this.items = new LinkedList<ContextMenuItem>();

//		addItem("item1111111111111");
//		addItem("item2222222222");
//		addItem("item3333333333333");
//		update();
	}

	public ContextMenu(final IContextMenuAction action,
			final VertexBufferObjectManager vertexObj,
			FontManager menuFontManager, TextureManager menuTextureManager) {
		this(DEFAULT_X, DEFAULT_Y, action, vertexObj, menuFontManager,
				menuTextureManager);
	}

	public ContextMenu(float pX, float pY, VertexBufferObjectManager vertexObj,
			FontManager menuFontManager, TextureManager menuTextureManager) {
		this(pX, pY, null, vertexObj, menuFontManager, menuTextureManager);
	}

	public ContextMenu(VertexBufferObjectManager vertexObj,
			FontManager menuFontManager, TextureManager menuTextureManager) {
		this(DEFAULT_X, DEFAULT_Y, vertexObj, menuFontManager,
				menuTextureManager);
	}
	
	protected boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (!isVisible()) return false;
		
		if (pSceneTouchEvent.isActionUp()) {
			
			ContextMenuItem selected = search(pTouchAreaLocalX, pTouchAreaLocalY);
					
			if (selected != null) {
				
				hide();
				selected.getText().setColor(textColor);
				for (IContextMenuAction listener : actionListeners) {
					if (listener != null) {
						listener.onContextMenuAction(this, selected);
					} else {
						Log.w("SmartAndroid", "ContextMenu: There are no listeners available");
					}
				}
			} else {
				Log.w("SmartAndroid", "ContextMenu: No item was selected...");
			}
		} else if (pSceneTouchEvent.isActionDown()) {
			ContextMenuItem selectedItem = search(pTouchAreaLocalX, pTouchAreaLocalY);
			
			if (selectedItem != null) {
				selectedItem.getText().setColor(selectedTextColor);
			}
		}
		
		return false;
	}
	
	private ContextMenuItem search(float pTouchAreaLocalX, float pTouchAreaLocalY) {
		for (ContextMenuItem item : items) {
			Text t = item.getText();

			if (pTouchAreaLocalX >= t.getX()
					&& pTouchAreaLocalX <= t.getX() + t.getWidth()
					&& pTouchAreaLocalY >= t.getY()
					&& pTouchAreaLocalY <= t.getY() + t.getHeight()) {

				return item;
			}
		}
		return null;
	}

	// Methods
	public void update() {

		float maxWidth = 0f;
		float totalHeight = 0f;
		
		this.backgroundMenu.detachChildren();

		for (final ContextMenuItem item : this.items) {

			Text text = new Text(DX, totalHeight + DY, font, new String(
					new char[256]), new TextOptions(HorizontalAlign.LEFT),
					vertexObj);

			text.setText(item.getLabel());
			item.setText(text);
			this.backgroundMenu.attachChild(text);

			float width = text.getWidth();

			if (width > maxWidth) {
				maxWidth = width;
			}

			totalHeight += text.getHeight() + DY;
		}

		totalHeight += DY;

		if (!this.items.isEmpty()) {
			this.backgroundMenu.setWidth(maxWidth + 2*DX);
			this.backgroundMenu.setHeight(totalHeight);
		}
	}
	
	public void update(ContextMenuItem item) {
		
		Text text = new Text(DX, this.backgroundMenu.getHeight() + DY, font, new String(
				new char[256]), new TextOptions(HorizontalAlign.LEFT),
				vertexObj);

		text.setText(item.getLabel());
		item.setText(text);
		this.backgroundMenu.attachChild(text);
		
		float tW = text.getWidth();
		if (this.backgroundMenu.getWidth() < text.getWidth()) {
			this.backgroundMenu.setWidth(tW + 2*DX);
		}
		this.backgroundMenu.setHeight(this.backgroundMenu.getHeight() + text.getHeight() + DY);
	}
	
	public void show() {
		
		if (this.items.isEmpty()) return;
		
		if (isVisible()) {
			hide();
			return;
		}
		
		setVisible(true);
		Transformations.applyAlphaModifier(this, MIN_ALPHA, MAX_ALPHA);
		Transformations.applyAlphaModifierInFatherAndChindren(backgroundMenu, MIN_ALPHA, MAX_ALPHA);
	}
	
	public void hide() {
//		Transformations.applyAlphaModifier(this, MAX_ALPHA, MIN_ALPHA);
//		Transformations.applyAlphaModifierInFatherAndChindren(this.backgroundMenu, MAX_ALPHA, MIN_ALPHA);
		setVisible(false);
	}

	public void addItem(ContextMenuItem item) {
		this.items.add(item);
		update();
	}

	public void addItem(String itemLabel) {
		addItem(new ContextMenuItem(itemLabel));
	}

	public List<ContextMenuItem> getItems() {
		return this.items;
	}

	public Rectangle getBoxMenu() {
		return this.backgroundMenu;
	}
	
	public float getWidth() {
		return this.backgroundMenu.getWidth();
	}
	
	public float getHeight() {
		return this.backgroundMenu.getHeight();
	}

	public void registerActionListener(IContextMenuAction action) {
		this.actionListeners.add(action);
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getSelectedTextColor() {
		return selectedTextColor;
	}

	public void setSelectedTextColor(Color selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
	}
}
