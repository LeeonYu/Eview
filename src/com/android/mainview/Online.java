package com.android.mainview;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import com.android.eview.R;
public class Online extends Activity {
	private TextView title;
	private WebView wv;
	private String url="http://m.qidian.com/";
	private Button shelf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.left);
		Initview();
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(url);
		wv.setWebViewClient(new HelloWebViewClient ());
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) { 
            wv.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        }else if((keyCode == KeyEvent.KEYCODE_BACK) && !wv.canGoBack()){
        	finish();
        	return true;
        }
        return false; 
}
	
	void Initview(){
		title=(TextView) findViewById(R.id.textView1);
		wv=(WebView) findViewById(R.id.wv);
		shelf=(Button) findViewById(R.id.shelf);
		shelf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	private class HelloWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
	}
	
}
