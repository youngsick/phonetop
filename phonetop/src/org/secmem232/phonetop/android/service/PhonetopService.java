package org.secmem232.phonetop.android.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.secmem232.phonetop.android.MainActivity;
import org.secmem232.phonetop.android.MouseView;
import org.secmem232.phonetop.android.UIHandler;
import org.secmem232.phonetop.android.natives.InputHandler;
import org.secmem232.phonetop.android.util.Util;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class PhonetopService extends Service {
	static String tag = "PhonetopService";
	/**
	 * 화면 세로/가로 상황에 따른 고유값 
	 */
	static final public int ORIENTATION_PORTRAIT = 0;
	static final public int ORIENTATION_LANDSCAPE = 1;
	
	/**
	 *  DISPALY 서비스 고유상태값
	 */
	public static int CLOSE_DISPLAY_SERVICE = 0;
	public static int SET_DISPLAY_ORIENTATION = 10;
	public static int CHANGE_DISPLAY_ORIENTATION = 11;
	
	/**
	 * INPUT 서비스에 관련된 고유상태값
	 */
//	static final public byte  START_MOUSE_SERVICE = 11;
//	static final public byte  END_MOUSE_SERVICE = 12;
//	static final public byte  START_KEYBOARD_SERVICE = 13;
//	static final public byte  END_KEYBOARD_SERVICE = 14;
//	static final public byte  START_MONITOR_SERVICE = 15;
//	static final public byte  END_MONITOR_SERVICE = 16;
//	static final public byte  START_TETHERING_SERVICE = 17;
//	static final public byte  END_TETHERING_SERVICE = 18;
//	static final public byte  SET_MOUSE_WHEEL_VOLUEM = 19;
//	static final public byte  SET_MOUSE_SPEED = 20;
//	static final public byte  SET_MOUSE_MAPPING = 21;
//	static final public byte  SET_KEYBOARD_MAPPING = 22;
//	static final public byte  SET_MONITOR_ORIENTATION = 23;
//	
//	static final public byte NONE_DEVICE = 4;
//	static final public byte INPUT_KEYBOARD = 1;
//	static final public byte INPUT_MOUSE = 2;
//	static final public byte INPUT_ALLDEVICE = 3;
//	
//	static final public byte OUTPUT_MONITOR = 1;
//	static final public byte UTIL_THETHERING = 1;
	
//	static final public int LEFT_BUTTON = 272;
//	static final public int RIGHT_BUTTON = 273;
//	static final public int WHEEL_BUTTON = 274;
//
//	static final public int WHEEL_SLOW = 0;
//	static final public int WHEEL_NORMAL = 1;
//	static final public int WHEEL_FAST = 2;
//
//	static final public int KEY_CLICK = 0;
//	static final public int KEY_BACK = 1;
//	static final public int KEY_HOME = 2;
//	static final public int KEY_MENU = 3;
//	
//	static final public int LEFT_HOME = 125;
//	static final public int RIGHT_HOME = 126;
//	static final public int HOME = 102;
//	static final public int VOLUME_DOWN = 114;
//	static final public int VOLUME_UP = 115;
//	static final public int KEY_F2 = 60;
//	static final public int KEY_F3 = 61;
//	static final public int KEY_F4 = 62;
//	static final public int KEY_POWER = 116;

	//클라이언트 추가 유/무 판별 변수
	int addedClient = 0;
	//실제 마우스를 표현 할 최상위뷰
	public MouseView view;
	
	//서비스 종료시점을 판단해 줄 flag 변수 
//	private boolean isEnd;
	//서비스관련 기능을 수행하는 Binder객체, PhonetopServiceConnection객체를 통해 MainActivity로부터 서비스를 관리. 
	public PhonetopServiceBinder mPhonetopServiceBinder;
	//실제 이벤트 관련한 기능 수행 핸들러.
	public PhonetopInputHandler inputEventHandler;
	
//	InputHandler inputHandler;
//	InputStream in;
//	OutputStream out;
//	byte a[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//			0, 0, 0 };
//	ByteBuffer buffer;
//	int type;
//	int code;
//	int value;
//	int inputMode;
//
//	int btnLeft;
//	int btnRight;
//	int btnWheel;
//
//	int wheelSpeed;
	
	boolean viewAddFlag;
	
	DisplayRotation dr;
	private ServerSocket server;
	private Socket client;
	@Override
	public IBinder onBind(Intent intent) {
		return mPhonetopServiceBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(tag, "service oncreate");

//		wheelSpeed = Util.getIntegerPreferences(this, "wheel");
//		btnLeft = Util.getIntegerPreferences(this, "btn_left");
//		btnRight = Util.getIntegerPreferences(this, "btn_right");
//		btnWheel = Util.getIntegerPreferences(this, "btn_wheel");
//
//		if (btnLeft < 0)
//			btnLeft = 0;
//		if (btnRight < 0)
//			btnRight = 1;
//		if (btnWheel < 0)
//			btnWheel = 2;
		
		//마우스뷰 생성
		view = new MouseView(this); // MainActivity.view=view;
		//마우스뷰 최상위뷰로 등록
		makeView();
		
		//실제 이벤트 관련한 기능 수행 핸들러.
		
		
//		mPhonetopServiceBinder.setMouseWheelVolume(Util.getIntegerPreferences(this, "wheel"));
//		mPhonetopServiceBinder.setMouseSpeed(Util.getIntegerPreferences(this, "speed"));
//		mPhonetopServiceBinder.setMouseMapping(LEFT_BUTTON, Util.getIntegerPreferences(this, "btn_left"));
//		mPhonetopServiceBinder.setMouseMapping(RIGHT_BUTTON, Util.getIntegerPreferences(this, "btn_right"));
//		mPhonetopServiceBinder.setMouseMapping(WHEEL_BUTTON, Util.getIntegerPreferences(this, "btn_wheel"));
//		mPhonetopServiceBinder.setMousePointerIcon(Util.getIntegerPreferences(this, "cursor"));
		
		// view.setOnTouchListener(mViewTouchListener); //팝업뷰에 터치 리스너 등록
//		inputHandler = new InputHandler(this);
		dr = new DisplayRotation(this);
		mPhonetopServiceBinder = new PhonetopServiceBinder(this);
		startInputServer();
	}

	private void startInputServer(){
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					server = new ServerSocket(6155);
					client = server.accept();
					if(MainActivity.handler!=null){
						addedClient++;
						setAddedClient(addedClient);
					}
					inputEventHandler = new PhonetopInputHandler(PhonetopService.this,client,view);
					runInputService();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
		

	private void runInputService() {
		new Thread() {
			@Override
			public void run() {
				if (client == null) {
					Log.d("TCP/IPtest", "Socket is NULL");
					return;
				}				
				inputEventHandler.start();				
			}
		}.start();
	}

	public void makeView(){
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,// TYPE_SYSTEM_ALERT,//TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, // will
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(view, params); // 최상위 윈도우에 뷰 넣기. 권한 필요.
		view.setVisibility(View.INVISIBLE);
	}
	
	public void setVisibleMouseView(int visible){
		view.setVisibility(visible);
	}
	
	public void onDestroy() {
		Log.d("PhonetopService", "onDestroy()");
		inputEventHandler.stop();
//		isEnd = true;
		if (view != null) // 서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
		{
			((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
			view = null;
		}
		view = null;
		try {
			if (client != null)
				client.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAddedClient(0);
		if(MainActivity.handler!=null)MainActivity.handler.sendEmptyMessage(UIHandler.SERVICE_CLOSE);
		super.onDestroy();
	}

	public void setAddedClient(int ClientCnt){
		Util.saveIntegerPreferences(PhonetopService.this, "addedClient", ClientCnt);
		if(ClientCnt>=1){
			MainActivity.handler.sendEmptyMessage(UIHandler.SERVICE_CONNECTED);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		
		super.onConfigurationChanged(newConfig);
         switch(newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
            	view.settingOrientation(MouseView.ORIENTATION_LANDSCAPE);
            	break;
            case Configuration.ORIENTATION_PORTRAIT: 
            	view.settingOrientation(MouseView.ORIENTATION_PORTRAIT);
            	break;
         }
         inputEventHandler.sendEvent(3, 0, view.getValueX());// ABS_X:0 EV_ABS:3
         inputEventHandler.sendEvent(3, 1, view.getValueY());// ABS_Y:1
	}
}
