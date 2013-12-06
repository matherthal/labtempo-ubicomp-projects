package br.uff.tempo.apps.map;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceGestureDetectorAdapter;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.RuleToolbar;
import br.uff.tempo.apps.map.log.LogActivity;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.ResourceTextureFactory;
import br.uff.tempo.apps.map.rule.RuleComposeBar;
import br.uff.tempo.apps.map.settings.MapSettings;
import br.uff.tempo.apps.simulators.utils.Creator;
import br.uff.tempo.apps.simulators.utils.ICreationFinisher;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.RuleComposer;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public abstract class SmartAndroidMap extends SimpleBaseGameActivity implements IOnSceneTouchListener,
		IScrollDetectorListener, IPinchZoomDetectorListener, IHoldDetectorListener,
		OnSharedPreferenceChangeListener, ICreationFinisher {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final String TAG = "SmartAndroid";
	public static final long VIBRATE_TIME = 100;
	
	public static final int SIMULATOR_MODE = 0;
	public static final int INTERPRETER_MODE = 1;
	public static final int ACTUATOR_MODE = 2;
	// ===========================================================
	// Fields
	// ===========================================================
	private Scene mScene;
	private ZoomCamera mCamera;

	// Detectors to manage events
	private HoldDetector mHoldDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private SurfaceScrollDetector mScrollDetector;
	private SurfaceGestureDetectorAdapter mSurfaceGestureDetector;
	private float mPinchZoomStartedCameraZoomFactor;

	// To create the resources
	private Creator resCreator;
	private ResourceTextureFactory textureFactory;

	// Camera size
	private int mCameraWidth;
	private int mCameraHeight;

	// To save the state of the map
	//private SceneState state;
	private SharedPreferences prefs;
	private Editor editor;

	// Map and Map Layers
	private TMXTiledMap tiledMap;
	private TMXLayer mapFloorLayer;
	private TMXLayer mapWallLayer;

	private Space houseMap;
	protected RuleComposeBar mBar;
	protected RuleToolbar ruleToolbar;
	private InterfaceApplicationManager mAppManager;
	protected int viewMode = SIMULATOR_MODE;
	protected RuleComposer ruleComposer;
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public void pushMap() {

		// Get the TXM Groups from map (actually there's only one, so using get(0) )
		final TMXObjectGroup group = this.tiledMap.getTMXObjectGroups().get(0);

		IResourceLocation resLocation = new ResourceLocationStub(IResourceLocation.rans);

		final int mapWidth = this.mapFloorLayer.getWidth();
		final int mapHeight = this.mapFloorLayer.getHeight();
		
		// Create a new Space (a set of places)
		if (houseMap == null) {
			houseMap = new Space(mapWidth, mapHeight);
		}
		
		for (TMXObject obj : group.getTMXObjects()) {

			String roomName = obj.getName();

			// [x0, y0] -> bottom-left corner
			float x0 = houseMap.pixelToMeters(obj.getX());

			// Y coordinate is transformed. System origin is in bottom-left
			// corner. The original one was in top-left corner
			float y0 = houseMap.invertYcoordinate(houseMap.pixelToMeters(obj
					.getY() + obj.getHeight()));

			// [x1, y1] -> top-right
			float x1 = x0 + houseMap.pixelToMeters(obj.getWidth());
			float y1 = houseMap.pixelToMeters(mapHeight - obj.getY());

			Place place = new Place(roomName, new Position(x0, y0), new Position(x1, y1));
			Log.i("SmartAndroid", "Created a new Place. lower = " + x0 + " "
					+ y0 + " and Upper = " + x1 + " " + y1);
			houseMap.addPlace(place);
		}

		resLocation.setMap(houseMap);
		//state.setMapInfo(houseMap);
	}
	
	// ===========================================================
	// Getters and Setters
	// ===========================================================
	
	public ResourceTextureFactory getTextureFactory() {
		return this.textureFactory;
	}
	
	public Scene getScene() {
		return this.mScene;
	}
	
	public Creator getResourceCreator() {
		return this.resCreator;
	}
	
	public Space getMap() {
		return this.houseMap;
	}
	
	public InterfaceApplicationManager getInterfaceManager() {
		return this.mAppManager;
	}
	
	public Camera getCamera() {
		return this.mCamera;
	}
	// ===========================================================
	// Overridden Methods
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		Log.i(TAG, "Creating engine options");

		// Get screen informations
		Display d = getWindowManager().getDefaultDisplay();
		d.getMetrics(new DisplayMetrics());

		this.mCameraWidth = d.getWidth();
		this.mCameraHeight = d.getHeight();

		this.mCamera = new ZoomCamera(0, 0, this.mCameraWidth, this.mCameraHeight);

		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						this.mCameraWidth, this.mCameraHeight), this.mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		Log.i(TAG, "Creating resources");

		// Setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		mAppManager = InterfaceApplicationManager.getInstance();

		// Add a default value for the preference "rdsAddress"
		editor = prefs.edit();
		String rdsIP = prefs.getString("rdsAddress", null);

		if (rdsIP == null) {
			editor.putString("rdsAddress", SmartAndroid.resourceDiscoveryIP);
			editor.commit();
		}

		// Enable device vibrator
		this.mEngine.enableVibrator(this);
		
		// Side bar to compose Context Rules
		this.mBar = new RuleComposeBar(this.mCamera, getVertexBufferObjectManager(), this.getFontManager(), this.getTextureManager());
		this.resCreator = new Creator(this, this);

		try {
			this.textureFactory = new ResourceTextureFactory(this);
		} catch (SmartAndroidException e) {
			Log.e("SmartAndroid", e.getMessage());
		}

		this.mCamera.setHUD(this.mBar);
	}

	@Override
	public Scene onCreateScene() {

		Log.i(TAG, "Creating scene");

		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		// load the TMX map (tiled map from house) see www.mapeditor.org
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
				this.mEngine.getTextureManager(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				this.getVertexBufferObjectManager(), null);

		// TODO Read a property file to load the map file.
		try {
			this.tiledMap = tmxLoader.loadFromAsset("tmx/casa_meiry.tmx");
		} catch (TMXLoadException e) {
			Log.e(TAG, "Error by loading the TMX Map");
			e.printStackTrace();
		}

		mapFloorLayer = this.tiledMap.getTMXLayers().get(0);
		mapWallLayer = this.tiledMap.getTMXLayers().get(1);

		// Attach the map layers to the scene (the order is important)
		// Background layer (floor)
		this.mScene.attachChild(this.mapFloorLayer);
		// Wall layer
		this.mScene.attachChild(this.mapWallLayer);

		// Set the maximum and minimum bounds from Camera
		this.mCamera.setBounds(0, 0, this.mapFloorLayer.getWidth(),
				this.mapFloorLayer.getHeight());
		this.mCamera.setBoundsEnabled(true);

		// Background is black...
		this.mScene.setBackground(new Background(0.0f, 0.0f, 0.0f));

		// Instantiating detectors (to perform Pan and Zoom)
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		this.mHoldDetector = new HoldDetector(this);
		setupGestureDetection();

		// Scene must listen to touch events!
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);
		

		return this.mScene;
	}

	@Override
	public synchronized void onGameDestroyed() {
		super.onGameDestroyed();
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		pushMap();

		//Loading sprites of all resource agents on screen
		resCreator.createAllResources();
	}

	// The main menu, accessed by Android menu button (in the Android device)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_context_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// when user select an item from menu...
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.createResource:
			resCreator.createResource(Creator.NOT_AUTOMATICALLY_OPEN);
			break;

		case R.id.conectToResource:
			resCreator.chooseResource(Creator.NOT_AUTOMATICALLY_OPEN);
			break;

		case R.id.rule:
			this.viewMode = INTERPRETER_MODE;
			this.mBar.show();
			this.ruleComposer = new RuleComposer();
			this.ruleComposer.addListener(this.mBar);
			this.ruleToolbar = new RuleToolbar(this, this.ruleComposer);
			
			break;

		case R.id.settings:
			startActivity(new Intent(this, MapSettings.class));
			break;

		case R.id.erase:
			editor.remove("state");
			editor.remove("rdsAddress");
			editor.commit();
			break;
			
		case R.id.log:
			startActivity(new Intent(this, LogActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// It is called when a preference is changed
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,	String key) {
		// Save the new preference to memory
		MapSettings.setAddress(sharedPreferences.getString("rdsAddress", null));
	}

	// Scroll Events
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY	/ zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector,	final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY	/ zoomFactor);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector,
			final int pPointerID, final float pDistanceX, final float pDistanceY) {

		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY
				/ zoomFactor);
	}

	// Pinch Zoom Events
	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	// Hold Events
	@Override
	public void onHold(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final int pPointerID, final float pHoldX, final float pHoldY) {
	}

	@Override
	public void onHoldFinished(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final int pPointerID, final float pHoldX, final float pHoldY) {
	}

	@Override
	public void onHoldStarted(final HoldDetector pHoldDetector, final int pPointerID, final float pHoldX, final float pHoldY) {
		this.mEngine.vibrate(VIBRATE_TIME);
	}

	// Touch Events
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

		this.mSurfaceGestureDetector.onTouchEvent(pSceneTouchEvent);

		return true;
	}

	// Gesture Events
	private void setupGestureDetection() {
		
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SmartAndroidMap.this.mSurfaceGestureDetector = new SurfaceGestureDetectorAdapter(SmartAndroidMap.this,	1f) {
					@Override
					protected boolean onDoubleTap() {
						// Reset the zoom to 100%
						Log.i("SmartAndroid", "MAP: Double tap");
						SmartAndroidMap.this.mCamera.setZoomFactor(1f);
						SmartAndroidMap.this.mBar.dismiss();
						SmartAndroidMap.this.ruleComposer = null;
						SmartAndroidMap.this.ruleToolbar = null;
						
						return true;
					}
				};
				SmartAndroidMap.this.mSurfaceGestureDetector.setEnabled(true);
			}
		});
	}
}