package com.android.mainview;

import com.android.eview.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class Splash extends Activity {

	private long m_dwSplashTime=1500;
	private boolean m_bPaused=false;
	private boolean m_bSplashActive=true;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Thread splashTimer=new Thread()
		{
			public void run(){
				try{
					//wait loop
					long ms=0;
					while(m_bSplashActive && ms<m_dwSplashTime){
						sleep(100);

						if(!m_bPaused)
							ms+=100;
					}

					startActivity(new Intent("com.android.eview.shelf"));
				}
				catch(Exception ex){
					Log.e("Splash",ex.getMessage());
				}
				finally{
					finish();
				}
			}
		};
		splashTimer.start();
	}

	@Override
	protected void onPause() {		
		super.onPause();
		m_bPaused=true;
	}

	@Override
	protected void onResume() {		
		super.onResume();
		m_bPaused=false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		super.onKeyDown(keyCode, event);
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			m_bSplashActive=false;
			break;
		case KeyEvent.KEYCODE_BACK:
			/*�����˳�����*/
			/* System.exit(0);*/
			/* android.os.Process.killProcess(android.os.Process.myPid());*/
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}		
		return true;
	}
}
