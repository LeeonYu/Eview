package com.android.eview.reader;

import java.io.IOException;

import org.w3c.dom.Text;

import com.android.eview.R;
import com.sqlite.BookInfo;
import com.sqlite.SetupInfo;
import com.sqlite.DbHelper;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("WrongCall")
public class turntest extends Activity {
	public final static int OPENMARK = 0;
	public final static int SAVEMARK = 1;
	public final static int TEXTSET = 2;
	int totalSize;
	float fPercent;
	float mark;
	String fname;
	String filename;
	int pro;
	int id;
	int count;
	float bookmark;
	float automark;
	float jindu;
	private int whichSize = 20;
	private int txtProgress = 0;
	final String[] font = new String[] { "15", "20", "25", "30", "35", "40" };
	private PageWidget mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	SeekBar sbFontSize;
	float sbBrightness;
	public PopupWindow popup;
	int curPostion;
	DbHelper db; 
	Context mContext;
	Cursor mCursor;
	String value;
	BookInfo book = null; 
	SetupInfo setup = null;
	


	@SuppressLint("WrongCall")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		DisplayMetrics jb =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(jb);
		mPageWidget = new PageWidget(this,jb.widthPixels,jb.heightPixels);
		setContentView(mPageWidget);
		
		
		Intent intent = getIntent();
		value = intent.getStringExtra("pa");
		fname = intent.getStringExtra("fn");
		pro =intent.getIntExtra("pro", 0);
		id  =intent.getIntExtra("bookid", 0);
		
		
		mCurPageBitmap = Bitmap.createBitmap(jb.widthPixels, jb.heightPixels, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap
				.createBitmap(jb.widthPixels, jb.heightPixels, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(jb.widthPixels, jb.heightPixels,fname);

		sbFontSize = (SeekBar) findViewById(R.id.seek);

		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.normal));

		//打开书本
		pagefactory.openbook(value);
		
		
		pagefactory.onDraw(mCurPageCanvas);

		//设置初始亮度
		/*WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 0.25f;
		getWindow().setAttributes(lp);*/
		
		
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		
		    String bookid = ("1");
			mContext = this;
			db = new DbHelper(mContext);
			try {
				book = db.getBookInfo(Integer.parseInt(bookid));
				setup = db.getSetupInfo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.err.println(book.automark);
		    if (book.automark > 0) 
		    { 
		    	pagefactory.setBeginPos(Integer.valueOf(book.automark));
				try {
					pagefactory.prePage();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pagefactory.onDraw(mNextPageCanvas);
				mPageWidget.setBitmaps(mNextPageBitmap, mNextPageBitmap);
				mPageWidget.postInvalidate();
				db.close(); 
			
		    }
		
 
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("WrongCall")
			@Override
			public boolean onTouch(View v, MotionEvent e) {

				boolean ret = false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage())
								return false;
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.islastPage())
								return false;
							pagefactory.onDraw(mNextPageCanvas);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}

					ret = mPageWidget.doTouchEvent(e, popup);
					return ret;
					}
				return false;
			}

		});
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {// 创建菜单
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
		int ID = item.getItemId();
		switch (ID) {
		case R.id.size:
			new AlertDialog.Builder(this)
					.setTitle("请选择")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(font, whichSize,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									setFontSize(Integer.parseInt(font[which]));
									whichSize = which;
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.light:
			
			LayoutInflater inflater1 = getLayoutInflater();
			final View layout1 = inflater1.inflate(R.layout.light_bar,
					(ViewGroup) findViewById(R.id.seekbar_light));
			SeekBar seek1 = (SeekBar) layout1.findViewById(R.id.seek_light);
			Log.i("huangnima", "sb");
			
			seek1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
					
				}
				
				@Override
				public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
					WindowManager.LayoutParams lp = getWindow().getAttributes();

					sbBrightness=(float)(arg1+1)/101;
					lp.screenBrightness =sbBrightness ;
					

					getWindow().setAttributes(lp);
					
				}
			});
			new AlertDialog.Builder(this)
			.setTitle("亮度调节")
			.setView(layout1)
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {

							dialog.dismiss();
						}
					}).show();


			break;
		case R.id.background:
			
			View root= this.getLayoutInflater().inflate(R.layout.popup, null);
			popup = new PopupWindow(root, 480, 150);
			popup.showAsDropDown(root);
			popup.showAtLocation(root, ID, ID, ID);
			popup.setOutsideTouchable(true);
			popup.setFocusable(false);	
			mPageWidget.postInvalidate();
		
			
			
			
			break;
		case R.id.bookmark:
			Log.i("huangnima", "nimabi");
			//System.err.println(db.getBookMark(1).bookname);
		    jindu=pagefactory.getfPercent();
		    System.err.println(jindu);
			Intent intent=new Intent("com.android.eview.xiaohuang");
			intent.putExtra("sbsb", fname);
			intent.putExtra("jindu", jindu);
			startActivity(intent);
			break;
		case R.id.jump:
			LayoutInflater inflater = getLayoutInflater();
			final View layout = inflater.inflate(R.layout.bar,
					(ViewGroup) findViewById(R.id.seekbar));
			SeekBar seek = (SeekBar) layout.findViewById(R.id.seek);
			final TextView textView = (TextView) layout
					.findViewById(R.id.textprogress);
			txtProgress = pagefactory.getCurProgress();
			seek.setProgress(txtProgress);
			textView.setText(String.format(getString(R.string.progress),
					txtProgress + "%"));
			seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					int progressBar = seekBar.getProgress();
					int m_mbBufLen = pagefactory.getBufLen();
					int pos = m_mbBufLen * progressBar / 100;
					if (progressBar == 0) {
						pos = 1;
					}
					pagefactory.setBeginPos(Integer.valueOf(pos));
					try {
						pagefactory.prePage();
					} catch (IOException e) {

						e.printStackTrace();
					}

					pagefactory.onDraw(mCurPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

					mPageWidget.postInvalidate();
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if (fromUser) {
						textView.setText(String.format(
								getString(R.string.progress), progress + "%"));
					}
				}
			});
			new AlertDialog.Builder(this)
					.setTitle("跳转")
					.setView(layout)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
								}
							}).show();
			break;
		case R.id.exit:
			
			
			updateBookMark();
			Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}



	private void setFontSize(int size) {
		pagefactory.setFontSize(size);
		int pos = pagefactory.getCurPostionBeg();
		pagefactory.setBeginPos(pos);
		try {
			pagefactory.nextPage();
		} catch (IOException e) {

			e.printStackTrace();
		}
		setContentView(mPageWidget);
		pagefactory.onDraw(mNextPageCanvas);

		mPageWidget.setBitmaps(mNextPageBitmap, mNextPageBitmap);
		mPageWidget.invalidate();

	}
	public void bg1(View view)
	{
		popup.dismiss();
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
	    this.getResources(), R.drawable.normal));
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.invalidate();
		mPageWidget.postInvalidate();
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

		   
		
	}
	public void bg2(View view)
	{
		popup.dismiss();
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.pink));
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.invalidate();
		mPageWidget.postInvalidate();
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

	
		
	}
	public void bg3(View view)
	{
		popup.dismiss();
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.cyan));
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.invalidate();
		mPageWidget.postInvalidate();
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);

    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==event.KEYCODE_BACK){
		
	    updateBookMark();
		
		finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	public void updateBookMark()
	{
	
		mContext = this;
		int curPostion = pagefactory.getCurPostion();
		System.err.println(curPostion);
		db = new DbHelper(mContext);
		db.update(1,fname, curPostion);
		db.updateSetup(1,whichSize,sbBrightness , 0);
		db.close();
	}



}
