package br.uff.tempo.apps.map;

import java.util.List;

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
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseExternalResource;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.apps.map.objects.ResourceObject;
import br.uff.tempo.apps.map.quickaction.ActionItem;
import br.uff.tempo.apps.map.quickaction.QuickAction;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.stubs.StoveStub;

public class MapActivity extends /* SimpleLayoutGameActivity */
SimpleBaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener, IResourceChooser {

	// ===========================================================
	// Constants
	// ===========================================================

	// private static final int CAMERA_WIDTH = 720;
	// private static final int CAMERA_HEIGHT = 480;

	private static final String TAG = "Test";

	// Time to vibrate (ms)
	private static final long VIBRATE_TIME = 100;

	// TODO: Put these constants in a separate file
	public static final int GPR_RESOURCES = 10;
	public static final int STOVE = GPR_RESOURCES + 1;
	public static final int TV = STOVE + 1;
	public static final int AR_CONDITIONER = TV + 1;
	public static final int DVD = AR_CONDITIONER + 1;
	public static final int BED = DVD + 1;
	public static final int LAMP = BED + 1;
	public static final int TEMPERATURE = LAMP + 1;
	public static final int LUMINOSITY = TEMPERATURE + 1;
	public static final int EXTERNAL = LUMINOSITY + 1;

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

	// Dialog wrappers
	private ResourceConfig resConf;
	private ChooseExternalResource externalList;

	// Information about the resource that will be created
	private RegistryData regData;

	// flag to know if the user's already finished the Config Dialog
	private boolean resConfigured;

	// A helper variable, keeping the selected item from menu
	private MenuItem itemSelected;

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

		this.mCamera = new ZoomCamera(0, 0, this.mCameraWidth, this.mCameraHeight);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(this.mCameraWidth, this.mCameraHeight), this.mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		Log.d(TAG, "Creating resources");

		// Create the dialogs
		resConf = new ResourceConfig(this);
		externalList = new ChooseExternalResource(this);

		// Get an Interface Manager instance
		mAppManager = InterfaceApplicationManager.getInstance();
		mAppManager.identify();

		this.mEngine.enableVibrator(this);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// It's like a catalog of images. The TextureRegion's objects are placed
		// onto the BitmapAtlas
		// Be careful with these integers! They are the TerxtureRegion
		// coordinates (pixels) in the atlas

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 127, 253, TextureOptions.BILINEAR);

		// the stove image is in position (0,0) in the atlas
		this.mStoveTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "stove_small.png", 0, 0);

		// the tv image is at the position (0,52) in the atlas
		this.mTVTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "tv_small.png", 0, 52);

		// the bed image is at the position (0,75) in the atlas
		this.mBedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "bed_small.png", 0, 75);

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
		// see www.mapeditor.org
		final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, this.getVertexBufferObjectManager(), null);

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

		// Set the maximum and minimum bounds from Camera
		this.mCamera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());
		this.mCamera.setBoundsEnabled(true);

		// Background is a kind of blue...
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		// Instantiating detectors (to perform Pan and Zoom)
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);

		// Scene must listen to touch events!
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		return this.mScene;
	}

	// TODO: Try to use Android layouts to implement a Quick action
	// @Override
	// protected int getLayoutID() {
	//
	// return R.layout.andengine;
	// }
	//
	// @Override
	// protected int getRenderSurfaceViewID() {
	//
	// return R.id.xmllayoutexample_rendersurfaceview;
	// }

	// Detectors
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX,
			final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX,
			final float pDistanceY) {
		final float zoomFactor = this.mCamera.getZoomFactor();
		this.mCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent,
			final float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
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

	// The main menu, accessed by Android menu button (in the Android device)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// sub menu: simulated resources
		SubMenu simulated = menu.addSubMenu("Add Simulated Resource").setIcon(R.drawable.add);

		// Simulated resources to add
		simulated.add(GPR_RESOURCES, TV, Menu.NONE, "TV");
		simulated.add(GPR_RESOURCES, DVD, Menu.NONE, "DVD");
		simulated.add(GPR_RESOURCES, STOVE, Menu.NONE, "Smart Stove");
		simulated.add(GPR_RESOURCES, BED, Menu.NONE, "Smart Bed");
		simulated.add(GPR_RESOURCES, AR_CONDITIONER, Menu.NONE, "Ar-conditioner");

		simulated.add(GPR_RESOURCES, TEMPERATURE, Menu.NONE, "Temperature Sensor");

		simulated.add(GPR_RESOURCES, LUMINOSITY, Menu.NONE, "Luminosity Sensor");

		// Option to connect an icon to an external resource
		menu.add(Menu.NONE, EXTERNAL, Menu.NONE, "Connect to External Resource").setIcon(R.drawable.connect);

		// Option to open a settings screen of the application
		menu.add("Settings").setIcon(R.drawable.settings);
		// Option to load a different map file
		menu.add("Load Map").setIcon(R.drawable.map);
		// Option to create a logical expression (called context rule)
		menu.add("Create rule").setIcon(R.drawable.thunder);

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

		// Process the menu items...
		createResourceIcon(item.getItemId(), true);

		return super.onOptionsItemSelected(item);
	}

	// @Override protected void onActivityResult(int requestCode, int
	// resultCode, Intent data) {
	//
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// //mAppManager.addResourceData((ResourceData)
	// data.getSerializableExtra("resourceData");
	// }

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

	// When the dialog is closed, call this call back
	@Override
	public void onDialogFinished(Dialog dialog) {

		regData = new RegistryData(resConf.getName());
		resConfigured = true;

		onOptionsItemSelected(itemSelected);
	}

	// Executed when the activity successfully receives a list of registered
	// resources
	public void onGetResourceList(List<String> list) {

		// Call a dialog to show the registered resource list
		externalList.showDialog(list);
	}

	// It is called when user select an item from registered resources list
	@Override
	public void onRegisteredResourceChoosed(String resourceRAI) {

		int type = -1;
		regData = new RegistryData(resourceRAI);

		// Toast.makeText(this, resourceRAI, Toast.LENGTH_LONG).show();
		if (resourceRAI.contains("Stove")) {
			type = STOVE;
		}

		createResourceIcon(type, false);

	}

	public void createResourceIcon(int iconType, boolean simulated) {

		// init local variables

		// It is used to call another activity and pass values to it!
		Intent i = null;

		// It may contain lots of values wrapped, to send to another activity
		Bundle bundle = new Bundle();

		// The class of the application (StoveView, BedView...)
		// it will be called when the icon is clicked
		Class c = null;

		// The resource icon (image)
		TextureRegion tr = null;

		// An integer that represents the resource type
		// it's not been used yet...
		int resType = -1;

		switch (iconType) {

		// Smart Stove selected. Creates a new stove in the scene
		case STOVE:

			c = br.uff.tempo.apps.stove.StoveView.class;
			tr = this.mStoveTextureRegion;
			resType = InterfaceApplicationManager.STOVE_DATA;

			IStove stove;

			// create an agent if it's a simulated resource; a stub otherwise
			if (simulated) {
				stove = new Stove(regData.getResourceName());
			} else {
				stove = new StoveStub(regData.getResourceName());
				// stove.registerStakeholder("all", mAppManager.getURL());
			}

			// simulated -> put an agent; not simulated -> put a stub (proxy to
			// an agent)
			bundle.putSerializable("agent", stove);

			break;

		// Smart TV selected. Creates a new TV in the scene
		case TV:

			c = br.uff.tempo.apps.tv.TvView.class;
			tr = this.mTVTextureRegion;
			resType = InterfaceApplicationManager.TV_DATA;

			break;

		// Smart Bed selected. Creates a new Bed in the scene
		case BED:

			c = br.uff.tempo.apps.bed.BedView.class;
			tr = this.mBedTextureRegion;
			resType = InterfaceApplicationManager.BED_DATA;

			break;

		case EXTERNAL:

			MiddlewareOperation m = new MiddlewareOperation(this);
			m.execute(null);

			// An external resource... we must exit this method, not only
			// the 'switch case', because we don't know what is the resource
			// chose
			return;
		default:
			// if receive an invalid option, exit method
			// (and doesn't execute the lines above!)
			return;
		}

		// Creates an intent, to pass data to StoveView
		i = new Intent(this, c);

		// Put all information in the intent
		i.putExtras(bundle);

		// create an icon in the map, according to the parameters
		createSprite(tr, i, resType);

	}

	private ResourceObject createSprite(final TextureRegion pTextureRegion, final Intent intent, final int dataType) {

		// Create a new Sprite that shows pTextureImage as graphical
		// representation
		// It's initially positioned at center screen
		ResourceObject sprite = new ResourceObject(this.mCameraWidth / 2, this.mCameraHeight / 2, pTextureRegion, this.getVertexBufferObjectManager()) {

			@Override
			public void onLongPress(TouchEvent pSceneTouchEvent) {

				// Can freely move the resource in the screen
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
			}

			@Override
			public void onTap(TouchEvent pSceneTouchEvent) {

				// Start the resource app (e.g. stove, tv)
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				MapActivity.this.startActivity(intent);
			}

			@Override
			public void onStartLongPress(TouchEvent pSceneTouchEvent) {

				// When start the long press event, vibrate the device for
				// 'VIBRATE_TIME' ms
				MapActivity.this.mEngine.vibrate(VIBRATE_TIME);
			}
		};

		this.mScene.attachChild(sprite);
		this.mScene.registerTouchArea(sprite);

		return sprite;
	}

	private void setupQuickActions() {

		ActionItem unregItem = new ActionItem(ID_UNREG, "Unregister", getResources().getDrawable(R.drawable.add));
		ActionItem removeItem = new ActionItem(ID_REMOVE, "Accept", getResources().getDrawable(R.drawable.cancel));
		ActionItem infoItem = new ActionItem(ID_INFO, "Upload", getResources().getDrawable(R.drawable.info));
		ActionItem settingsItem = new ActionItem(ID_SETTINGS, "Upload", getResources().getDrawable(R.drawable.settings));

		mQuickAction = new QuickAction(getApplicationContext());

		mQuickAction.addActionItem(unregItem);
		mQuickAction.addActionItem(removeItem);
		mQuickAction.addActionItem(infoItem);
		mQuickAction.addActionItem(settingsItem);

		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				ActionItem actionItem = quickAction.getActionItem(pos);

				if (actionId == ID_UNREG) {
					Toast.makeText(getApplicationContext(), "Unregister", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}