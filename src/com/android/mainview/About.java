package com.android.mainview;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

import com.android.eview.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class About extends Activity{
	private ListView lv; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		lv=(ListView) findViewById(R.id.about);
		
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2==2){
					tony();
				}
				return true;
			}
		});
/*		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				
				Thread Timer=new Thread()
				{
					public void run(){
						try{
					
							sleep(3000);

							tony();
						}
						catch(Exception ex){
							Log.e("Tony",ex.getMessage());
						}
						finally{
							finish();
						}
					}
				};
				Timer.start();
				// TODO Auto-generated method stub
				
			}
		});*/

	}
	public void tony(){
		Intent intent=new Intent("com.android.eview.tony");
		startActivity(intent);
		
	}
}
