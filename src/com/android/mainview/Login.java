package com.android.mainview;

import java.util.ArrayList;
import java.util.List;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSErrorCode;
import com.baidu.pcs.BaiduPCSStatusListener;
import com.baidu.pcs.BaiduPCSActionInfo;
import com.android.eview.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class Login extends Activity{
	
private String mbOauth = null;
    
    // the api key
    /*
     * mbApiKey should be your app_key, please instead of "your app_key"
     */
    private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE"; //your app_key";
    
    // the default root folder
    /*
     * mbRootPath should be your app_path, please instead of "/apps/pcstest_oauth"
     */
    private final static String mbRootPath =  "/apps/pcstest_oauth";
    
    private Handler mbUiThreadHandler = null;

    
    
    private void test_login(){

//    	try {
//    		BaiduPCSClient pcsApi = new BaiduPCSClient();
//    		
//    		pcsApi.startOAuth(this, mbApiKey, new BaiduPCSClient.OAuthListener() {
//				
//				public void onException(String msg) {
//					// TODO Auto-generated method stub
//					Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
//				}
//				
//				public void onComplete(Bundle values) {
//					// TODO Auto-generated method stub
//					if(null != values){
//						mbOauth = values.getString(BaiduPCSClient.Key_AccessToken);
//						Toast.makeText(getApplicationContext(), "Token: " + mbOauth + "    User name:" + values.getString(BaiduPCSClient.Key_UserName), Toast.LENGTH_SHORT).show();
//					}
//				}
//				
//				public void onCancel() {
//					// TODO Auto-generated method stub
//					Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
//				}
//			});
//    		
//    	} catch (Exception e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
		BaiduOAuth oauthClient = new BaiduOAuth();
		oauthClient.startOAuth(this, mbApiKey, new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener() {
			@Override
			public void onException(String msg) {
				Toast.makeText(getApplicationContext(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onComplete(BaiduOAuthResponse response) {
				if(null != response){
					mbOauth = response.getAccessToken();
					Toast.makeText(getApplicationContext(), "Token: " + mbOauth + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onCancel() {
				Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
			}
		});
    }

}
