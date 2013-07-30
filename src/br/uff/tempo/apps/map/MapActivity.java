package br.uff.tempo.apps.map;

import java.util.List;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.ChosenData;
import br.uff.tempo.apps.map.dialogs.IChooser;
import br.uff.tempo.apps.map.dialogs.IDialogFinishHandler;
import br.uff.tempo.apps.map.dialogs.IListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.apps.map.log.LogActivity;
import br.uff.tempo.apps.map.objects.AnimatedResourceObject;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.ResourceObject;
import br.uff.tempo.apps.map.objects.notification.INotificationBoxReceiver;
import br.uff.tempo.apps.map.objects.persistence.PersistHelper;
import br.uff.tempo.apps.map.objects.persistence.RegistryData;
import br.uff.tempo.apps.map.objects.persistence.SceneState;
import br.uff.tempo.apps.map.quickaction.ActionItem;
import br.uff.tempo.apps.map.quickaction.QuickAction;
import br.uff.tempo.apps.map.rule.RuleComposeBar;
import br.uff.tempo.apps.map.settings.MapSettings;
import br.uff.tempo.apps.simulators.utils.Creator;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.PersonStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.stubs.BedStub;
import br.uff.tempo.middleware.resources.stubs.LampStub;
import br.uff.tempo.middleware.resources.stubs.StoveStub;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class MapActivity extends SimpleBaseGameActivity implements
		IOnSceneTouchListener, IScrollDetectorListener,
		IPinchZoomDetectorListener, IChooser, IListGetter,
		IDialogFinishHandler, OnSharedPreferenceChangeListener {

	// ===========================================================
	// Constants
	// ===========================================================

	// private static final int CAMERA_WIDTH = 720;
	// private static final int CAMERA_HEIGHT = 480;

	private static final String TAG = "Map";

	// Time to vibrate (ms)
	private static final long VIBRATE_TIME = 100;

	private static final int MIN_OBJECTS = 3;

	// TODO: Put these constants in a separate file
	public static final int GPR_RESOURCES = 10;
	public static final int STOVE = GPR_RESOURCES + 1;
	public static final int PERSON = STOVE + 1;
	public static final int TV = PERSON + 1;
	public static final int AIR_CONDITIONER = TV + 1;
	public static final int DVD = AIR_CONDITIONER + 1;
	public static final int BED = DVD + 1;
	public static final int LAMP = BED + 1;
	public static final int TEMPERATURE = LAMP + 1;
	public static final int LUMINOSITY = TEMPERATURE + 1;
	public static final int EXTERNAL = LUMINOSITY + 1;
	public static final int LOG = EXTERNAL + 1;
	public static final int SETTINGS = LOG + 1;
	public static final int ERASE = SETTINGS + 1;
	public static final int RULE = ERASE + 1;

	// constants to QuickAction (it's not been used yet)
	public static final int ID_UNREG = 1;
	public static final int ID_REMOVE = 2;
	public static final int ID_INFO = 3;
	public static final int ID_SETTINGS = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private ZoomCamera mCamera;
	private Scene mScene;

	// Detectors to manage zoom and pan
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	private SurfaceGestureDetector mSurfaceGestureDetector;

	// The tiled (pieces of images) map
	private TMXTiledMap tiledMap;

	// The image atlas (an object that "contains" every image)
	private BuildableBitmapTextureAtlas mTiledTextureAtlas;
	private BuildableBitmapTextureAtlas mBuildableTexture;

	// Texture Regions
	private TextureRegion mStoveTextureRegion;
	private TextureRegion mTVTextureRegion;
	private TextureRegion mBedTextureRegion;
	private TextureRegion mLampTextureRegion;
	private TextureRegion mAirTextureRegion;
	// People
	private TiledTextureRegion mPersonBaldMan;
	private TiledTextureRegion mPersonBlondMan;
	private TiledTextureRegion mPersonManInTie;
	private TiledTextureRegion mPersonBlondGirl;
	private TiledTextureRegion mPersonRedHeadedGirl;
	private TiledTextureRegion mPersonBlondGuy;
	private TiledTextureRegion mPersonGrayGuy;
	private TiledTextureRegion mPersonNurse;
	
	private Creator resCreator;

	// Shared preferences
	private SharedPreferences prefs;

	// Editor from Shared preferences
	Editor editor;

	// RDS Address from the preferences
	private String rdsAddress;

	// Camera size
	private int mCameraWidth;
	private int mCameraHeight;

	// Quick action menu
	private QuickAction mQuickAction;

	// Manages the Resources data
	private InterfaceApplicationManager mAppManager;

	private SceneState state;

	// Dialog wrappers
	private ResourceConfig resConf;
	private ChooseResource externalList;

	// Information about the resource that will be created
	private RegistryData regData;

	// flag to know if the user's already finished the Config Dialog
	private boolean resConfigured;

	// A helper variable, keeping the selected item from menu
	private MenuItem itemSelected;

	private FontManager fontManager;
	private TextureManager textureManager;

	private TMXLayer mapFloorLayer;
	private TMXLayer mapWallLayer;

	// House map
	private Space houseMap;

	private RuleComposeBar mBar;

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
				ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(
						this.mCameraWidth, this.mCameraHeight), this.mCamera);

		Log.i(TAG, "Create Engine Options");

		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		Log.d(TAG, "Creating resources");

		// Create the dialogs
		resConf = new ResourceConfig(this);
		externalList = new ChooseResource(this);

		// Setup preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		// Add a default value for the preference "rdsAddress"
		editor = prefs.edit();

		String rdsIP = prefs.getString("rdsAddress", null);

		if (rdsIP == null) {
			editor.putString("rdsAddress", SmartAndroid.resourceDiscoveryIP);
			editor.commit();
		} else {
			testRDSAddress(rdsIP);
		}

		// Save this value to memory
		MapSettings.setAddress(prefs.getString("rdsAddress", null));

		this.mEngine.enableVibrator(this);

		// Now using an algorithm to automatically place the TextureSources on a
		// Texture, so you don't have to care about the actual position anymore
		this.mBuildableTexture = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 512, TextureOptions.DEFAULT);

		this.mTiledTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 512, TextureOptions.NEAREST);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// the stove image
		this.mStoveTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBuildableTexture, this,
						"stove_small.png");

		// the tv image
		this.mTVTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBuildableTexture, this, "tv_small.png");

		// the bed image
		this.mBedTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBuildableTexture, this, "bed_small.png");

		// the lamp image
		this.mLampTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBuildableTexture, this,
						"lamp_inactive.png");
		
		// All People sprites are 96x128 pixels
		this.mAirTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBuildableTexture, this,
						"air_cond.png");

		// All People sprites are 96x128 pixels
		this.mPersonBaldMan = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBuildableTexture, this,
						"man_bald_big.png", 3, 4);

		try {
			this.mBuildableTexture
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBuildableTexture.load();

		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		// Side bar to compose Context Rules
		this.mBar = new RuleComposeBar(this.mCamera, getVertexBufferObjectManager(), this.fontManager,
				this.textureManager);
		this.mCamera.setHUD(this.mBar);

		Log.i(TAG, "Create Resources");
	}

	private void testRDSAddress(String address) {
		// TODO Test if the address is really appointing to a ResourceDiscovery
		// Service
	}

	@Override
	public Scene onCreateScene() {

		Log.d(TAG, "Creating scene");

		// setupQuickActions();
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		// load the TMX map (tiled map from house)
		// see www.mapeditor.org
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
				this.mEngine.getTextureManager(),
				TextureOptions.BILINEAR_PREMULTIPLYALPHA,
				this.getVertexBufferObjectManager(), null);

		// TODO Read a property file to load the map file. Create an interface
		// to choose the file
		try {
			this.tiledMap = tmxLoader.loadFromAsset("tmx/casa_meiry.tmx");
		} catch (TMXLoadException e) {
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

		// Scene must listen to touch events!
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		setupGestureDetection();

		Log.i(TAG, "Create Scene");

		return this.mScene;
	}

	@Override
	public synchronized void onGameDestroyed() {

		// Unregister all fake devices from Resource Register and
		// remove it from local containers

		if (state != null) {

			for (RegistryData regData : state.getData()) {
				if (regData.isFake()) {
					regData.getAgent().unregister();
				}
			}

			editor = prefs.edit();

			PersistHelper.saveToFile("state", this.state, editor);
			this.mScene.dispose();
		}

		Log.i(TAG, "Destroy Game");

		super.onGameDestroyed();
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();

		if (this.mScene.getChildCount() < MIN_OBJECTS) {
			// Create an Interface Manager instance and register it
			mAppManager = InterfaceApplicationManager.getInstance();

			state = (SceneState) PersistHelper.loadFromFile("state", prefs);

			// There is no state saved
			if (state == null) {

				state = new SceneState();
				// TODO Test if it is called again, when screen rotate...
				pushMap();

				// There is a previous state saved. Restoring it

			} else {

				houseMap = state.getMapInfo();
				pushMap();

				state.setLock(true);
				for (RegistryData data : state.getData()) {

					createResourceIcon(data);
				}
				state.setLock(false);
			}
		}
		Log.i(TAG, "Resume Game");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// Called when a new resource agent is created
	private void callConfigDialog() {
		// It is a configuration dialog to
		// get a name, location and
		// others configurations
		this.resConfigured = false;
		resConf.showDialog();
	}

	public void createResourceIcon(RegistryData regData) {

		// init local variables

		// It is used to call another activity and pass values to it!
		Intent i = null;

		// The class of the application (StoveView, BedView...)
		// it will be called when the icon is clicked
		Class c = null;

		// The resource icon (image)
		ITextureRegion tr = null;

		// Reference to resource to be created

		// This will be sent through intents to Simulator GUI
		IResourceAgent proxyAg = regData.getStub();

		// This one will be kept locally (this is the real one, not a proxy)
		IResourceAgent realAg = regData.getAgent();

		switch (regData.getIconType()) {

		// Smart Stove selected. Creates a new stove in the scene
		case STOVE:

			c = br.uff.tempo.apps.simulators.stove.StoveView.class;
			tr = this.mStoveTextureRegion;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Stove(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new StoveStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new StoveStub(regData.getResourceName());
				}
			}

			break;
			
		case AIR_CONDITIONER:

			c = br.uff.tempo.apps.simulators.stove.StoveView.class;
			tr = this.mAirTextureRegion;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Stove(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new StoveStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new StoveStub(regData.getResourceName());
				}
			}

			break;

		// Smart Lamp selected. Creates a new lamp in the scene
		case LAMP:

			c = br.uff.tempo.apps.simulators.lamp.LampView.class;
			tr = this.mLampTextureRegion;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Lamp(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new LampStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new LampStub(regData.getResourceName());
				}
			}

			break;

		// Smart TV selected. Creates a new TV in the scene
		case TV:

			c = br.uff.tempo.apps.simulators.tv.TvView.class;
			tr = this.mTVTextureRegion;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Television(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new TelevisionStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new TelevisionStub(regData.getResourceName());
				}
			}

			break;

		// Smart Bed selected. Creates a new Bed in the scene
		case BED:

			c = br.uff.tempo.apps.simulators.bed.BedView.class;
			tr = this.mBedTextureRegion;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Bed(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new BedStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new BedStub(regData.getResourceName());
				}
			}

			break;

		case PERSON:

			c = br.uff.tempo.apps.simulators.bed.BedView.class;
			tr = this.mPersonBaldMan;

			// create an agent if it's a simulated resource; a stub otherwise
			if (proxyAg == null) {

				if (regData.isFake()) {

					realAg = new Person(regData.getResourceName(),
							regData.getResourceName());
					realAg.identify();
					proxyAg = new PersonStub(realAg.getRANS());

				} else {
					realAg = null;
					proxyAg = new PersonStub(regData.getResourceName());
				}
			}

			break;

		case EXTERNAL:

			// Starts a middleware operation, listing all registered resources
			// ("//")
			MiddlewareOperation m = new MiddlewareOperation(this, this, "//",
					IResourceDiscovery.rans);
			m.execute(null);

			// An external resource... we must exit this method, not only
			// the 'switch case', because we don't know what is the resource
			// chose
			return;

		case LOG:

			i = new Intent(this, LogActivity.class);
			startActivity(i);

			return;

		case SETTINGS:

			i = new Intent(this, MapSettings.class);
			startActivity(i);

			return;

		case ERASE:

			editor.remove("state");
			editor.remove("rdsAddress");
			editor.commit();

			return;

		case RULE:

			this.mBar.show();

			return;

		default:
			// if receive an invalid option, exit method
			// (and doesn't execute the lines above!)
			return;
		}

		if (!regData.isFake()) {
			Position pos = proxyAg.getPosition();

			if (pos != null) {
				regData.setPositionX(pos.getX());
				regData.setPositionY(pos.getY());
			} else if (regData.getPositionX() == RegistryData.INVALID_POSITION
					|| regData.getPositionY() == RegistryData.INVALID_POSITION) {

				float x = houseMap.pixelToMeters(this.mCameraWidth / 2);
				float y = houseMap.pixelToMeters(this.mCameraHeight / 2);

				regData.setPositionX(x);
				regData.setPositionY(y);
			}
		}

		// Subscribe to the agent (all context variables) to receive
		// notifications
		proxyAg.registerStakeholder("all", mAppManager.getRANS());

		// Creates an intent, to pass data to StoveView
		i = new Intent(this, c);

		// create an icon in the map, according to the parameters
		INotificationBoxReceiver res = (INotificationBoxReceiver) createSprite(
				tr, i, regData);

		mAppManager.addResource(proxyAg.getRANS(), res);

		regData.setAgent(realAg);
		regData.setStub(proxyAg);

		// add only if the state is unlocked (tested internally)
		state.addData(regData);
	}

	private Sprite createSprite(final ITextureRegion pTextureRegion,
			final Intent intent, final RegistryData data) {

		// Create a new Sprite that shows pTextureImage as graphical
		// representation
		// It's initially positioned at (x,y) position (pixels)
		float x = houseMap.metersToPixel(data.getPositionX());
		float y = houseMap.metersToPixel(houseMap.invertYcoordinate(data
				.getPositionY()));

		Sprite sprite = null;

		VertexBufferObjectManager vbom = this.getVertexBufferObjectManager();
		FontManager fm = this.getFontManager();
		TextureManager tm = this.getTextureManager();

		if (data.getIconType() == PERSON) {

			AnimatedResourceObject aro = new AnimatedResourceObject(x, y,
					(ITiledTextureRegion) pTextureRegion, vbom, fm, tm) {

				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {

//					IPerson ag = (IPerson) intent.getExtras().getSerializable(
//							"agent");
//					String message = ag.getName();
					this.showMessage("Yeah!");

					return super.onAreaTouched(pSceneTouchEvent,
							pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};

			aro.setSpace(houseMap);

			sprite = aro;

		} else {
			sprite = new ResourceObject(x, y, pTextureRegion, vbom, fm, tm) {

				@Override
				public void onLongPressMove(TouchEvent pSceneTouchEvent) {

					// Can freely move the resource in the screen
					this.setPosition(pSceneTouchEvent.getX() - this.getWidth()
							/ 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
				}

				@Override
				public void onTap(TouchEvent pSceneTouchEvent) {

					// Start the resource app (e.g. stove, tv)
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					Log.d("IPGAP",
							"A resource was selected. Opening the Application");

					Bundle bundle = new Bundle();
					bundle.putSerializable("agent", data.getStub());

					intent.putExtras(bundle);

					MapActivity.this.startActivity(intent);
				}

				@Override
				public void onStartLongPress(TouchEvent pSceneTouchEvent) {

					// When start the long press event, vibrate the device for
					// 'VIBRATE_TIME' ms
					MapActivity.this.mEngine.vibrate(VIBRATE_TIME);
				}

				@Override
				public void onEndLongPressMove(TouchEvent pSceneTouchEvent) {

					float x = houseMap.pixelToMeters((int) this.getX());
					float y = houseMap.invertYcoordinate(houseMap
							.pixelToMeters((int) this.getY()));

					data.setPositionX(x);
					data.setPositionY(y);
				}
			};
		}

		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);

		return sprite;
	}

	public void pushMap() {

		// Get the TXM Groups from map (actually there's only one, so using
		// get(0))
		final TMXObjectGroup group = this.tiledMap.getTMXObjectGroups().get(0);



		final int mapWidth = this.mapFloorLayer.getWidth();
		final int mapHeight = this.mapFloorLayer.getHeight();

		IResourceLocation rls = new ResourceLocationStub(IResourceLocation.rans);
		// Create a new Space (a set of places)
		if (houseMap == null) {

			houseMap = new Space(mapWidth, mapHeight);

		}
		rls.setMap(houseMap);

		for (TMXObject obj : group.getTMXObjects()) {

			String roomName = obj.getName();

			// [x0, y0] -> bottom-left corner
			float x0 = houseMap.pixelToMeters(obj.getX());

			// Y coordinate is transformed. System origin is in bottom-left
			// corner
			// The original one was in top-left corner
			float y0 = houseMap.invertYcoordinate(houseMap.pixelToMeters(obj
					.getY() + obj.getHeight()));

			// [x1, y1] -> top-right
			float x1 = x0 + houseMap.pixelToMeters(obj.getWidth());
			float y1 = houseMap.pixelToMeters(mapHeight - obj.getY());

			Place place = new Place(roomName, new Position(x0, y0),
					new Position(x1, y1));
			Log.i("SmartAndroid", "Created a new Place. lower = " + x0 + " "
					+ y0 + " and Upper = " + x1 + " " + y1);
			rls.addPlace(place);
		}

		//rls.insertMap(houseMap);

		state.setMapInfo(houseMap);
	}

	// The main menu, accessed by Android menu button (in the Android device)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// sub menu: simulated resources
		SubMenu simulated = menu.addSubMenu("Add Simulated Resource").setIcon(
				R.drawable.add);

		// Simulated resources to add
		simulated.add(GPR_RESOURCES, TV, Menu.NONE, "TV");
		simulated.add(GPR_RESOURCES, DVD, Menu.NONE, "DVD");
		simulated.add(GPR_RESOURCES, STOVE, Menu.NONE, "Smart Stove");
		simulated.add(GPR_RESOURCES, LAMP, Menu.NONE, "Smart Lamp");
		simulated.add(GPR_RESOURCES, BED, Menu.NONE, "Smart Bed");
		simulated.add(GPR_RESOURCES, AIR_CONDITIONER, Menu.NONE,
				"Ar-conditioner");

		simulated.add(GPR_RESOURCES, TEMPERATURE, Menu.NONE,
				"Temperature Sensor");

		simulated
				.add(GPR_RESOURCES, LUMINOSITY, Menu.NONE, "Luminosity Sensor");

		// Option to connect an icon to an external resource
		menu.add(Menu.NONE, EXTERNAL, Menu.NONE, "Connect to External Resource")
				.setIcon(R.drawable.connect);

		// Option to open a settings screen of the application
		menu.add(Menu.NONE, SETTINGS, Menu.NONE, "Settings").setIcon(
				R.drawable.settings);
		// Option to load a different map file
		menu.add("Load Map").setIcon(R.drawable.map);
		// Option to create a logical expression (called context rule)
		menu.add(Menu.NONE, RULE, Menu.NONE, "Create rule").setIcon(
				R.drawable.thunder);

		menu.add(Menu.NONE, LOG, Menu.NONE, "View Log").setIcon(
				R.drawable.log_icon);

		menu.add(Menu.NONE, ERASE, Menu.NONE, "Clear Saved Data").setIcon(
				R.drawable.cancel);

		return super.onCreateOptionsMenu(menu);
	}

	// when user select an item from menu...
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// A resource was selected
		// Call a configuration dialog and get some information about the
		// resource. Pass only if the resource selected is not configured yet!
		// (and if groupID == GPR_RESOURCES :D )

		if (item.getGroupId() == GPR_RESOURCES && !resConfigured) {

			callConfigDialog();
			itemSelected = item;

			// if the resource isn't already configured, exit the method
			// probably it's not configured yet... just certifying,
			// because the variable is modified in another thread too...
			if (!resConfigured)
				return false;
		}

		resConfigured = false;
		regData = resConf.getData();
		regData.setFake(true);
		regData.setIconType(item.getItemId());

		float x = houseMap.pixelToMeters(this.mCameraWidth / 2);
		float y = houseMap.pixelToMeters(this.mCameraHeight / 2);

		regData.setPositionX(x);
		regData.setPositionY(y);

		// Process the menu items...
		createResourceIcon(regData);

		return super.onOptionsItemSelected(item);
	}

	// When the dialog is closed, call this call back
	@Override
	public void onDialogFinished(Dialog dialog) {

		resConfigured = true;

		onOptionsItemSelected(itemSelected);
	}

	// Executed when the activity successfully receives a list of registered
	// resources
	@Override
	public void onGetList(List<ResourceData> list) {

		// Call a dialog to show the registered resource list
		externalList.showDialog(list);
	}

	// It is called when user select an item from registered resources list
	@Override
	public void onResourceChosen(ChosenData choosedData) {

		ResourceData resourceData = choosedData.getData();
		
		regData = new RegistryData(resourceData.getRans());
		regData.setFake(false);

		// TODO Add middleware support to get the resource types
		// Toast.makeText(this, resourceRAI, Toast.LENGTH_LONG).show();
		if (resourceData.getType().equals(ResourceAgent.type(Stove.class))) {
			regData.setIconType(STOVE);
		} else if (resourceData.getType()
				.equals(ResourceAgent.type(Lamp.class))) {
			regData.setIconType(LAMP);
		} else if (resourceData.getType().equals(ResourceAgent.type(Bed.class))) {
			regData.setIconType(BED);
		} else if (resourceData.getType().equals(
				ResourceAgent.type(Television.class))) {
			regData.setIconType(TV);
		} else if (resourceData.getType().equals(
				ResourceAgent.type(Person.class))) {
			regData.setIconType(PERSON);
		}

		createResourceIcon(regData);
	}

	// It is called when a preference is changed
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		// Save the new preference to memory
		MapSettings.setAddress(sharedPreferences.getString("rdsAddress", null));
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

		// TODO use the code above in the future, to find a wall
		// if (pSceneTouchEvent.isActionDown()) {
		//
		// final float x = pSceneTouchEvent.getX();
		// final float y = pSceneTouchEvent.getY();
		//
		// final TMXTile currentTile = this.mapWallLayer.getTMXTileAt(x, y);
		// int id = currentTile.getGlobalTileID();
		//
		// try {
		//
		// TMXProperties<TMXTileProperty> prop = this.tiledMap
		// .getTMXTileProperties(id);
		//
		// if (prop != null && prop.containsTMXProperty("block", "true")) {
		// this.runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// Toast.makeText(MapActivity.this,
		// "x = " + x + " | y = " + y + " has a wall",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// });
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }

		this.mSurfaceGestureDetector.onTouchEvent(pSceneTouchEvent);

		return true;
	}

	private void setupGestureDetection() {

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				MapActivity.this.mSurfaceGestureDetector = new SurfaceGestureDetector(
						MapActivity.this, 1f) {

					@Override
					protected boolean onSwipeUp() {

						// Toast.makeText(MapActivity.this, "onSwipeUp",
						// Toast.LENGTH_SHORT).show();
						return false;
					}

					@Override
					protected boolean onSwipeRight() {

						// Toast.makeText(MapActivity.this, "onSwipeRight",
						// Toast.LENGTH_SHORT).show();
						return false;
					}

					@Override
					protected boolean onSwipeLeft() {

						// Toast.makeText(MapActivity.this, "onSwipeLeft",
						// Toast.LENGTH_SHORT).show();
						return false;
					}

					@Override
					protected boolean onSwipeDown() {

						// Toast.makeText(MapActivity.this, "onSwipeDown",
						// Toast.LENGTH_SHORT).show();
						return false;
					}

					@Override
					protected boolean onSingleTap() {

						// Toast.makeText(MapActivity.this, "onSingleTap",
						// Toast.LENGTH_SHORT).show();
						return false;
					}

					@Override
					protected boolean onDoubleTap() {

						Log.d("SmartAndroid", "onDoubleTap");

						// Reset the zoom to 100%
						MapActivity.this.mCamera.setZoomFactor(1f);
						MapActivity.this.mBar.dismiss();

						return true;
					}

					@Override
					public boolean onManagedTouchEvent(
							TouchEvent pSceneTouchEvent) {

						return super.onManagedTouchEvent(pSceneTouchEvent);
					}

					@Override
					public boolean onSceneTouchEvent(Scene pScene,

					TouchEvent pSceneTouchEvent) {

						return super
								.onSceneTouchEvent(pScene, pSceneTouchEvent);
					}
				};

				MapActivity.this.mSurfaceGestureDetector.setEnabled(true);
			}
		});
	}

	@Deprecated
	// It's not been used yet
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