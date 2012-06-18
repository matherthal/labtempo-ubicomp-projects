package br.uff.tempo.apps.map;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.config.ResourceConfig;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.apps.map.objects.ResourceObject;
import br.uff.tempo.apps.map.quickaction.ActionItem;
import br.uff.tempo.apps.map.quickaction.QuickAction;
import br.uff.tempo.apps.stove.StoveData;

public class MapActivity extends SimpleLayoutGameActivity
/* SimpleBaseGameActivity */implements IOnSceneTouchListener,
		IScrollDetectorListener, IPinchZoomDetectorListener {

	// ===========================================================
	// Constants
	// ===========================================================

	// private static final int CAMERA_WIDTH = 720;
	// private static final int CAMERA_HEIGHT = 480;

	private static final String TAG = "Test";
	private static final long VIBRATE_TIME = 100;

	// TODO: Put these constants in a separate file
	private static final int GPR_RESOURCES = 10;
	private static final int STOVE = GPR_RESOURCES + 1;
	private static final int TV = STOVE + 1;
	private static final int AR_CONDITIONER = TV + 1;
	private static final int DVD = AR_CONDITIONER + 1;
	private static final int BED = DVD + 1;
	private static final int TEMPERATURE = BED + 1;
	private static final int LUMINOSITY = TEMPERATURE + 1;

	private static final int ID_UNREG = 1;
	private static final int ID_REMOVE = 2;
	private static final int ID_INFO = 3;
	private static final int ID_SETTINGS = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private ZoomCamera mCamera;

	private Scene mScene;

	// Detectors to manage zoom and pan
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;

	// The tiled (pieces of images) map
	private TMXTiledMap tiledMap;

	// The image atlas (an object that "contains" every image)
	private BitmapTextureAtlas mBitmapTextureAtlas;

	// Texture Regions
	private TextureRegion mStoveTextureRegion;
	private TextureRegion mTVTextureRegion;
	private TextureRegion mBedTextureRegion;

	// Camera size
	private int mCameraWidth;
	private int mCameraHeight;

	// Quick action menu
	private QuickAction mQuickAction;

	// Manages the Resources data
	InterfaceApplicationManager mAppManager;
	private MenuItem m;
	private ResourceConfig resConf;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		Log.d(TAG, "Creating engine options");

		// Get screen informations
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();

		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);

		this.mCameraWidth = d.getWidth();
		this.mCameraHeight = d.getHeight();

		this.mCamera = new ZoomCamera(0, 0, this.mCameraWidth,
				this.mCameraHeight);

		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						this.mCameraWidth, this.mCameraHeight), this.mCamera);

		// get an Interface Manager instance
		mAppManager = InterfaceApplicationManager.getInstance();

		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		Log.d(TAG, "Creating resources");

		//Create the dialogs
		resConf = new ResourceConfig(this);
		
		this.mEngine.enableVibrator(this);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// It's like a catalog of images. The TextureRegion's objects are placed
		// onto the BitmapAtlas
		// Be careful with these integers! They are the TerxtureRegion
		// coordinates (pixels) in the atlas

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 127, 253, TextureOptions.BILINEAR);

		// the stove image is in position (0,0) in the atlas
		this.mStoveTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"stove_small.png", 0, 0);

		// the tv image is at the position (0,52) in the atlas
		this.mTVTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"tv_small.png", 0, 52);

		// the bed image is at the position (0,75) in the atlas
		this.mBedTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"bed_small.png", 0, 75);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {

		Log.d(TAG, "Creating scene");

		setupQuickActions();
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		// load the TMX map (tiled map from house)
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
				this.mEngine.getTextureManager(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				this.getVertexBufferObjectManager(), null);

		try {
			// this.tiledMap = tmxLoader.loadFromAsset("tmx/desert.tmx");
			this.tiledMap = tmxLoader.loadFromAsset("tmx/house.tmx");
			// this.tiledMap = tmxLoader.loadFromAsset("tmx/testTiled.tmx");
		} catch (TMXLoadException e) {
			e.printStackTrace();
		}
		final TMXLayer tmxLayer = this.tiledMap.getTMXLayers().get(0);

		// Attach the map layers to the scene

		// wall layer
		this.mScene.attachChild(tmxLayer);
		// background layer (floor)
		this.mScene.attachChild(this.tiledMap.getTMXLayers().get(1));

		// Set the maximum and minimum bound from Camera
		this.mCamera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());
		this.mCamera.setBoundsEnabled(true);

		// Background is a kind of blue...
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		// Instanciating detectors (to perform Pan and Zoom)
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);

		// Scene must listen to touch events!
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		return this.mScene;
	}

	// TODO: Try to use Android layouts to implement a Quick action
	@Override
	protected int getLayoutID() {

		return R.layout.andengine;
	}

	@Override
	protected int getRenderSurfaceViewID() {

		return R.id.xmllayoutexample_rendersurfaceview;
	}

	// Detectors
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
				* pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
				* pZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if (this.mPinchZoomDetector.isZooming()) {
			this.mScrollDetector.setEnabled(false);
		} else {
			if (pSceneTouchEvent.isActionDown()) {
				this.mScrollDetector.setEnabled(true);
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

	// The main menu, accessed by menu button
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// sub menu: simulated resources
		SubMenu simulated = menu.addSubMenu("Add Simulated Resource").setIcon(
				R.drawable.add);

		// Simulated resources to add
		simulated.add(GPR_RESOURCES, TV, Menu.NONE, "TV");
		simulated.add(GPR_RESOURCES, DVD, Menu.NONE, "DVD");
		simulated.add(GPR_RESOURCES, STOVE, Menu.NONE, "Smart Stove");
		simulated.add(GPR_RESOURCES, BED, Menu.NONE, "Smart Bed");
		simulated.add(GPR_RESOURCES, AR_CONDITIONER, Menu.NONE,
				"Ar-conditioner");
		simulated.add(GPR_RESOURCES, TEMPERATURE, Menu.NONE,
				"Temperature Sensor");
		simulated
				.add(GPR_RESOURCES, LUMINOSITY, Menu.NONE, "Luminosity Sensor");

		menu.add("Connect to External Resource").setIcon(R.drawable.connect);
		menu.add("Settings").setIcon(R.drawable.settings);
		menu.add("Load Map").setIcon(R.drawable.map);
		menu.add("Create rule").setIcon(R.drawable.thunder);

		return super.onCreateOptionsMenu(menu);
	}

	// when user select an item from menu...
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i;
		RegistryData rd;

		switch (item.getItemId()) {

		// A simulated resource was selected
		// Call a configuration activity and get some information about the
		// resource.
		case GPR_RESOURCES:

			rd = callConfigActivity();

			// Smart Stove selected. Creates a new stove in the scene
		case STOVE:

			i = new Intent(getApplicationContext(),
					br.uff.tempo.apps.stove.StoveView.class);
			i.putExtra("stoveData", new StoveData(4));
			createSprite(this.mStoveTextureRegion, i,
					InterfaceApplicationManager.STOVE_DATA);
			break;

		// Smart TV selected. Creates a new TV in the scene
		case TV:
			i = new Intent(getApplicationContext(),
					br.uff.tempo.apps.stove.StoveView.class);
			createSprite(this.mTVTextureRegion, i,
					InterfaceApplicationManager.TV_DATA);
			break;

		// Smart Bed selected. Creates a new Bed in the scene
		case BED:
			i = new Intent(getApplicationContext(),
					br.uff.tempo.apps.bed.BedView.class);
			createSprite(this.mBedTextureRegion, i,
					InterfaceApplicationManager.BED_DATA);
			break;
			
		case DVD:
			
			callConfigActivity();
			break;

		default:
			Toast.makeText(this, "This resource is not supported yet",
					Toast.LENGTH_LONG).show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // TODO Auto-generated method stub
	 * super.onActivityResult(requestCode, resultCode, data);
	 * 
	 * 
	 * //mAppManager.addResourceData((ResourceData)
	 * data.getSerializableExtra("resourceData")); }
	 */
	// ===========================================================
	// Methods
	// ===========================================================

	private RegistryData callConfigActivity() {
		
		resConf.showPopup();
		
		return null;
	}
	
	private ResourceObject createSprite(final TextureRegion pTextureRegion,
			final Intent intent, final int dataType) {

		// Create a new Sprite that shows pTextureImage as graphical
		// representation
		// It's initially positioned at center screen
		ResourceObject sprite = new ResourceObject(this.mCameraWidth / 2,
				this.mCameraHeight / 2, pTextureRegion,
				this.getVertexBufferObjectManager()) {

			@Override
			public void onLongPress(TouchEvent pSceneTouchEvent) {

				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
						pSceneTouchEvent.getY() - this.getHeight() / 2);
			}

			@Override
			public void onTap(TouchEvent pSceneTouchEvent) {

				// Start the resource app (e.g. stove, tv)
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// TestMapActivity.this.startActivityForResult(intent,
				// dataType);
				MapActivity.this.startActivity(intent);
			}

			@Override
			public void onStartLongPress(TouchEvent pSceneTouchEvent) {

				// when start the long press event, vibrate the device for
				// VIBRATE_TIME ms
				MapActivity.this.mEngine.vibrate(VIBRATE_TIME);
			}
		};

		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);

		return sprite;
	}

	private void setupQuickActions() {

		ActionItem unregItem = new ActionItem(ID_UNREG, "Unregister",
				getResources().getDrawable(R.drawable.add));
		ActionItem removeItem = new ActionItem(ID_REMOVE, "Accept",
				getResources().getDrawable(R.drawable.cancel));
		ActionItem infoItem = new ActionItem(ID_INFO, "Upload", getResources()
				.getDrawable(R.drawable.info));
		ActionItem settingsItem = new ActionItem(ID_SETTINGS, "Upload",
				getResources().getDrawable(R.drawable.settings));

		mQuickAction = new QuickAction(getApplicationContext());

		mQuickAction.addActionItem(unregItem);
		mQuickAction.addActionItem(removeItem);
		mQuickAction.addActionItem(infoItem);
		mQuickAction.addActionItem(settingsItem);

		mQuickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction quickAction, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						if (actionId == ID_UNREG) {
							Toast.makeText(getApplicationContext(),
									"Unregister", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									actionItem.getTitle() + " selected",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

}