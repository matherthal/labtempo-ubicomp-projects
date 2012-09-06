package br.uff.tempo.apps.map.objects;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;

public class NotificationBox extends Entity {

	public static final int MAX_HEIGHT = 300;
	public static final int MAX_WIDTH = 300;
	public static final int FONT_SIZE = 12;
	public static final int DEFAULT_DURATION = 4000;

	// shape that contains the text
	private Rectangle box;

	// shape width
	private int width;

	// shape height
	private int height;

	// The shape color
	private Color boxColor = Color.BLACK;

	// The notification text
	private Text text;

	private Color textColor = Color.WHITE;

	private Font font;

	private Timer durationTimer;

	public NotificationBox(float parentWidth, float parentHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			FontManager fontManager, TextureManager textureManager) {

		this.font = FontFactory.create(fontManager, textureManager, 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), FONT_SIZE,
				this.textColor.getARGBPackedInt());
		this.font.load();

		box = new Rectangle(parentWidth, parentHeight, this.width, this.height,
				pVertexBufferObjectManager);
		
		box.setColor(this.boxColor);

		text = new Text(0, 0, font,
				new String(new char[256]),
				new TextOptions(HorizontalAlign.LEFT),
				pVertexBufferObjectManager);

		this.box.attachChild(text);
		this.attachChild(box);

		this.setVisible(false);
	}

	public void show(String message) {
		show(message, DEFAULT_DURATION);
	}

	public void show(String message, int duration) {

		this.setText(message);

		durationTimer = new Timer();
		durationTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				NotificationBox.this.setVisible(false);
			}
		}, duration);

		this.setVisible(true);
	}

	public String getText() {
		return (String) this.text.getText();
	}

	public void setText(String text) {

		this.text.setText(text);
		box.setWidth(this.text.getWidth());
		box.setHeight(this.text.getHeight());
	}

	public Color getBoxColor() {
		return boxColor;
	}

	public void setBoxColor(Color color) {
		this.box.setColor(color);
	}
}
