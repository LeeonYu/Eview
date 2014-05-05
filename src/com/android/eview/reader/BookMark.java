
package com.android.eview.reader;

import java.io.File;
import java.io.IOException;



import com.android.eview.R;
import com.sqlite.DbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BookMark extends Activity
{
	public ListView listview;
	BookPageFactory pagefactory;
	Context mContext;
	int position;
	DbHelper db;
	int id;
	int count;
	String filename;
	String fname;
	float bookmark;
	float fPercent;
	int rbookmark;
	private PageWidget mPageWidget;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);
		listview =(ListView)findViewById(R.id.listview1);
		listview.setAdapter(new Mydadpter());
		listview.setOnCreateContextMenuListener(mbshuqian);
		Intent intent=getIntent();
		filename = intent.getStringExtra("sbsb");
		fPercent = intent.getFloatExtra("jindu", 0);
		
		
		
	}
	
	OnCreateContextMenuListener mbshuqian = new OnCreateContextMenuListener() {
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, 1, 1, "添加书签");
			menu.add(0, 2, 2, "读取书签");
		}
	};
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
    	switch (item.getItemId()) {
		case 1:
			addBookMark();
			
			break;
		case 2:
			
			break;
		default:
			break;
		}
    	return true;
    }

	class Mydadpter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv=new TextView (getApplicationContext());
			System.err.println(position);
			
//			if( mark.bookname!=null){
//				tv.setText(mark.bookname+"   当前进度："+mark.bookmark);
//			}
				tv.setText("空白书签");
				
			
			tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setPadding(10, 10, 10, 10);
			return tv;
		}
		
	
	}
	public void addBookMark()
	{

	
    	mContext = this;
		db = new DbHelper(mContext);		
		System.err.println(fPercent);
		System.err.println(filename);
		
		db.updatemark(position+1,fPercent,filename); 
	}
	public void getmark() {
		mContext = this;
		db = new DbHelper(mContext);	
		rbookmark=(int) db.getBookMark(1).bookmark;
		
	}
}

