package com.android.mainview;

import com.android.eview.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class Tony extends Activity{
	private ImageView img;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tony);
		img=(ImageView) findViewById(R.id.tonyda);
		Toast.makeText(this, "����С�ƣ��Ұ������ľ����",
				  Toast.LENGTH_SHORT).show(); 
	}
	

}
