package br.uff.tempo.apps.map.objects.notification;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
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
import org.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Typeface;

public class NotificationBox extends Rectangle {

	public static final int MAX_HEIGHT = 300;
	public static final int MAX_WIDTH = 300;
	public static final int FONT_SIZE = 12;
	public static final int DEFAULT_DURATION = 6000;
	// The shape color
	public static final Color DEFAULT_COLOR = Color.BLACK;

	// The notification text
	private Text text;

	private Color textColor = Color.WHITE;

	private Font font;

	private Timer durationTimer;

	public NotificationBox(float parentWidth, float parentHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			FontManager fontManager, TextureManager textureManager) {

		super(parentWidth, parentHeight, 0f, 0f, pVertexBufferObjectManager);
		
		this.font = FontFactory.create(fontManager, textureManager, 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), FONT_SIZE,
				this.textColor.getARGBPackedInt());
		this.font.load();
		
		this.setColor(DEFAULT_COLOR);

		text = new Text(0, 0, font,
				new String(new char[256]),
				new TextOptions(HorizontalAlign.LEFT),
				pVertexBufferObjectManager);

		this.attachChild(text);

		this.setAlpha(0f);
		//this.setVisible(true);
		
		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
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

				NotificationBox.this.registerEntityModifier(new FadeOutModifier(2));
				NotificationBox.this.text.registerEntityModifier(new FadeOutModifier(2));
			}
		}, duration);
		
		this.registerEntityModifier(new FadeInModifier(2, EaseLinear.getInstance()));
		this.text.registerEntityModifier(new FadeInModifier(2, EaseLinear.getInstance()));
	}

	public String getText() {
		return (String) this.text.getText();
	}

	public void setText(String text) {

		this.text.setText(text);
		this.setWidth(this.text.getWidth());
		this.setHeight(this.text.getHeight());
	}

}
