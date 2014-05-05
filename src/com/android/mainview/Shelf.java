package com.android.mainview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.android.eview.R;
import com.android.eview.reader.turntest;
import com.sqlite.BookInfo;
import com.sqlite.DbHelper;
import com.android.mainview.ExDialog.MyAdapter;
import com.android.mainview.MyLinearLayout.OnScrollListener;
import com.android.mainview.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Shelf extends Activity implements OnTouchListener,
		GestureDetector.OnGestureListener, OnItemClickListener {
	private boolean hasMeasured = false;// �Ƿ�Measured.
	private LinearLayout layout_left;// ��߲���
	private LinearLayout layout_right;// �ұ߲���
	private ImageView iv_set;// ͼƬ
	private ListView lv_set;// ���ò˵�
	private ListView shelf;
	List<BookInfo> books;
	private String txtPath="/mnt/sdcard/Eview/˵���ĵ�.txt";
	private ImageView online;
	boolean isExit;
	boolean hasTask;
	
	private static final int REQUEST_EX = 1;

	
	private static Boolean isQuit = false;

	Timer timer = new Timer();

	/** ÿ���Զ�չ��/�����ķ�Χ */
	private int MAX_WIDTH = 0;
	/** ÿ���Զ�չ��/�������ٶ� */
	private final static int SPEED = 30;

	private final static int sleep_time = 5;
	private static final int REQUEST_CODE = 0;
	private static final int RUSULT_CODE_1 = 0;

	private GestureDetector mGestureDetector;// ����
	private boolean isScrolling = false;
	private float mScrollX; // ���黬������
	private int window_width;// ��Ļ�Ŀ��
	private long mExitTime;

	private String TAG = "Smart";

	private View view = null;// �����view

	private String title[] = { "�����ļ�","��������","����","�ҵ����","�������","�˳�" };

	private MyLinearLayout mylaout;
	
	int realTotalRow;
	int bookNumber ;
	int[ ] size = null;
	DbHelper db=new DbHelper(this);
	/***
	 * ��ʼ��view
	 */
	void InitView() {
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (ListView) findViewById(R.id.lv_set);
		shelf = (ListView) findViewById(R.id.shelf);
		online = (ImageView) findViewById(R.id.online);
		
		mylaout = (MyLinearLayout) findViewById(R.id.mylaout);
		
		
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tv_item, title));
		/***
		 * ʵ�ָýӿ�
		 */
		mylaout.setOnScrollListener(new OnScrollListener() {
			@Override
			public void doScroll(float distanceX) {
				doScrolling(distanceX);
			}

			@Override
			public void doLoosen() {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
						.getLayoutParams();
				Log.e("Smart", "layoutParams.leftMargin="
						+ layoutParams.leftMargin);
				// ����ȥ
				if (layoutParams.leftMargin < -window_width / 2) {
					new AsynMove().execute(-SPEED);
				} else {
					new AsynMove().execute(SPEED);
				}
			}
		});
		
		
		BaseAdapter myadapter =new BaseAdapter() {
			
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater layout_inflater = ( LayoutInflater ) Shelf.this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
	            View layout = layout_inflater.inflate ( R.layout.shelf_list_item , null );
	            books=db.getAllBookInfo();
	            
	           
	            bookNumber=db.getcount();
	            Log.i("positon", ""+position);
	            
	            if(position < realTotalRow){
	            	int buttonNum = (position+1) * 3;
	            	if(bookNumber <= 3){
	            		buttonNum = bookNumber;
	            	}
	                for (int i = 0; i < buttonNum; i++) {
	                	if(i == 0){
	                		final BookInfo book = books.get(position*3);
	                		final String buttonName = book.bookname;
	                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
	                		Button button = ( Button ) layout.findViewById ( R.id.button_1 );
	                		button.setVisibility(View.VISIBLE);
	                		Log.i("num", ""+"test");
	                		button.setText(buttonName);
	                		button.setId(book.id);
	                		final String patch0=book.fpath;
	                		final int pro=book.automark;
	                		final int id=book.id;
	                		
	                		button.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.i("num", ""+"test2");
									Intent intent = new Intent();
						    	    intent.setClass(Shelf.this, turntest.class);
						    	    intent.putExtra("pa", patch0);
						    	    intent.putExtra("fn", buttonName);
						    	    intent.putExtra("pro", pro);
						    	    intent.putExtra("id", id);
						    	    startActivity(intent);
								}
							});
	                		button.setOnLongClickListener(new OnLongClickListener() {
								
								@Override
								public boolean onLongClick(View v) {
									// TODO Auto-generated method stub
									Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
											"��ʾ").setMessage(
											"ȷ��Ҫɾ����").setPositiveButton(
											"ȷ��",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													db.delete(book.id);
													refreshShelf();
												}
											}).setNegativeButton("ȡ��",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											}).create();// ������ť
									dialog.show();
									return false;
								}
							});
	                		//button.setOnCreateContextMenuListener(listener);
	                		//button.setOnTouchListener(Shelf.this);
	                	}else if(i == 1){
	                		final BookInfo book = books.get(position*3+1);
	                		final String buttonName = book.bookname;
	                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
	                		Button button = ( Button ) layout.findViewById ( R.id.button_2 );
	                		button.setVisibility(View.VISIBLE);
	                		button.setText(buttonName);
	                		button.setId(book.id);
	                		final String patch1=book.fpath;
	                		final float pro=book.automark;
	                		final int id=book.id;
	                		button.setOnClickListener ( new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.i("num", ""+"test2");
									Intent intent = new Intent();
						    	    intent.setClass(Shelf.this, turntest.class);
						    	    intent.putExtra("pa", patch1);
						    	    intent.putExtra("fn", buttonName);
						    	    intent.putExtra("pro", pro);
						    	    intent.putExtra("id", id);
						    	    startActivity(intent);
								}
							} );
	                		button.setOnLongClickListener(new OnLongClickListener() {
								
								@Override
								public boolean onLongClick(View v) {
									// TODO Auto-generated method stub
									Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
											"��ʾ").setMessage(
											"ȷ��Ҫɾ����").setPositiveButton(
											"ȷ��",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													db.delete(book.id);
													refreshShelf();
												}
											}).setNegativeButton("ȡ��",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											}).create();// ������ť
									dialog.show();
									return false;
								}
							});
	                		//button.setOnCreateContextMenuListener(listener);
	                		//button.setOnTouchListener(Shelf.this);
	                	}else if(i == 2){
	                		final BookInfo book = books.get(position*3+2);
	                		final String buttonName = book.bookname;
	                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
	                		Button button = ( Button ) layout.findViewById ( R.id.button_3 );
	                		button.setVisibility(View.VISIBLE);
	                		button.setText(buttonName);
	                		button.setId(book.id);
	                		final String patch2=book.fpath;
	                		final float pro=book.automark;
	                		final int id=book.id;
	                		button.setOnClickListener ( new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.i("num", ""+"test2");
									Intent intent = new Intent();
						    	    intent.setClass(Shelf.this, turntest.class);
						    	    intent.putExtra("pa", patch2);
						    	    intent.putExtra("fn", buttonName);
						    	    intent.putExtra("pro", pro);
						    	    intent.putExtra("id", id);
						    	    startActivity(intent);
								}
							} );
	                		button.setOnLongClickListener(new OnLongClickListener() {
								
								@Override
								public boolean onLongClick(View v) {
									// TODO Auto-generated method stub
									Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
											"��ʾ").setMessage(
											"ȷ��Ҫɾ����").setPositiveButton(
											"ȷ��",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													db.delete(book.id);
													refreshShelf();
												}
											}).setNegativeButton("ȡ��",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											}).create();// ������ť
									dialog.show();
									return false;
								}
							});
	                		//button.setOnCreateContextMenuListener(listener);
	                		//button.setOnTouchListener(Shelf.this);
	                	}
	    			}
	                bookNumber -= 3;
	            }
	            
	            return layout;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return size[ position ];
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return 4;
		          if ( size.length > 3 ) {
		                return size.length;
		            } else {
		                return 4;
		            }
	            
			}
		};
		shelf.setAdapter(myadapter);

		// �������
		lv_set.setOnItemClickListener(this);

		layout_right.setOnTouchListener(this);
		layout_left.setOnTouchListener(this);
		iv_set.setOnTouchListener(this);
		shelf.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		// ���ó�������
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
		online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.android.eview.online"));
			}
		});
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
        /**************��ʼ�����ͼ��*********************/
        books = db.getAllBookInfo();//ȡ�����е�ͼ��
        bookNumber = db.getcount();
		//db.insert(  1,"readme",null,"/sdcard/Eview/readme.txt");
		//db.close();
        int count = db.getcount();
        int totalRow = count/3; 
        if (count%3 > 0 ){
        	totalRow = count/3 + 1;
        }
        realTotalRow = totalRow;
        if(totalRow<5){
        	totalRow = 5;
        }
        size = new int[totalRow];
		
		                                                                                                     
        if (!copyFile()) {
			//Toast.makeText(this, "�����鲻���ڣ�", Toast.LENGTH_SHORT).show();
		}
        InitView();
	}
	
    public void refreshShelf(){
   	 /**************��ʼ�����ͼ��*********************/
       books = db.getAllBookInfo();//ȡ�����е�ͼ��
       bookNumber = books.size();
       int count = books.size();
       int totalRow = count/3; 
       if (count%3 > 0 ){
       	totalRow = count/3 + 1;
       }
       realTotalRow = totalRow;
       if(totalRow<5){
       	totalRow = 5;
       }
       size = new int[totalRow];
       /***********************************/
		 BaseAdapter mAdapter = new BaseAdapter() 
			
			 {
					
					
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						// TODO Auto-generated method stub
						LayoutInflater layout_inflater = ( LayoutInflater ) Shelf.this.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
			            View layout = layout_inflater.inflate ( R.layout.shelf_list_item , null );
			            books=db.getAllBookInfo();
			            
			           
			            bookNumber=db.getcount();
			            Log.i("position", ""+position);
			            
			            if(position < realTotalRow){
			            	int buttonNum = (position+1) * 3;
			            	if(bookNumber <= 3){
			            		buttonNum = bookNumber;
			            	}
			                for (int i = 0; i < buttonNum; i++) {
			                	if(i == 0){
			                		final BookInfo book = books.get(position*3);
			                		final String buttonName = book.bookname;
			                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
			                		Button button = ( Button ) layout.findViewById ( R.id.button_1 );
			                		button.setVisibility(View.VISIBLE);
			                		Log.i("num", ""+"test");
			                		button.setText(buttonName);
			                		button.setId(book.id);
			                		final String patch0=book.fpath;
			                		final float pro=book.automark;
			                		final int id=book.id;
			                		button.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Log.i("num", ""+"test2");
											Intent intent = new Intent();
								    	    intent.setClass(Shelf.this, turntest.class);
								    	    intent.putExtra("pa", patch0);
								    	    intent.putExtra("fn", buttonName);
								    	    intent.putExtra("pro", pro);
								    	    intent.putExtra("bookid", id);
								    	    
								    	    
								    	    startActivity(intent);
								    	    
										}
									});
			                		button.setOnLongClickListener(new OnLongClickListener() {
										
										@Override
										public boolean onLongClick(View v) {
											// TODO Auto-generated method stub
											Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
													"��ʾ").setMessage(
													"ȷ��Ҫɾ����").setPositiveButton(
													"ȷ��",
													new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int whichButton) {
															db.delete(book.id);
															refreshShelf();
														}
													}).setNegativeButton("ȡ��",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.cancel();
														}
													}).create();// ������ť
											dialog.show();
											return false;
										}
									});
			                		//button.setOnCreateContextMenuListener(listener);
			                		//button.setOnTouchListener(Shelf.this);
			                	}else if(i == 1){
			                		final BookInfo book = books.get(position*3+1);
			                		final String buttonName = book.bookname;
			                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
			                		Button button = ( Button ) layout.findViewById ( R.id.button_2 );
			                		button.setVisibility(View.VISIBLE);
			                		button.setText(buttonName);
			                		button.setId(book.id);
			                		final String patch1=book.fpath;
			                		final float pro=book.automark;
			                		final int id=book.id;
			                		button.setOnClickListener ( new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Log.i("num", ""+"test2");
											Intent intent = new Intent();
								    	    intent.setClass(Shelf.this, turntest.class);
								    	    intent.putExtra("pa", patch1);
								    	    intent.putExtra("fn", buttonName);
								    	    intent.putExtra("pro", pro);
								    	    intent.putExtra("id", id);
								    	    startActivity(intent);
										}
									} );
			                		button.setOnLongClickListener(new OnLongClickListener() {
										
										@Override
										public boolean onLongClick(View v) {
											// TODO Auto-generated method stub
											Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
													"��ʾ").setMessage(
													"ȷ��Ҫɾ����").setPositiveButton(
													"ȷ��",
													new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int whichButton) {
															db.delete(book.id);
															refreshShelf();
														}
													}).setNegativeButton("ȡ��",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.cancel();
														}
													}).create();// ������ť
											dialog.show();
											return false;
										}
									});
			                		//button.setOnCreateContextMenuListener(listener);
			                		//button.setOnTouchListener(Shelf.this);
			                	}else if(i == 2){
			                		final BookInfo book = books.get(position*3+2);
			                		final String buttonName = book.bookname;
			                		//buttonName = buttonName.substring(0,buttonName.indexOf("."));
			                		Button button = ( Button ) layout.findViewById ( R.id.button_3 );
			                		button.setVisibility(View.VISIBLE);
			                		button.setText(buttonName);
			                		button.setId(book.id);
			                		final String patch2=book.fpath;
			                		final float pro=book.automark;
			                		final int id=book.id;
			                		button.setOnClickListener ( new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Log.i("num", ""+"test2");
											Intent intent = new Intent();
								    	    intent.setClass(Shelf.this, turntest.class);
								    	    intent.putExtra("pa", patch2);
								    	    intent.putExtra("fn", buttonName);
								    	    intent.putExtra("pro", pro);
								    	    intent.putExtra("id", id);
								    	    startActivity(intent);
										}
									} );
			                		button.setOnLongClickListener(new OnLongClickListener() {
										
										@Override
										public boolean onLongClick(View v) {
											// TODO Auto-generated method stub
											Dialog dialog = new AlertDialog.Builder(Shelf.this).setTitle(
													"��ʾ").setMessage(
													"ȷ��Ҫɾ����").setPositiveButton(
													"ȷ��",
													new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int whichButton) {
															db.delete(book.id);
															refreshShelf();
														}
													}).setNegativeButton("ȡ��",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															dialog.cancel();
														}
													}).create();// ������ť
											dialog.show();
											return false;
										}
									});
			                		//button.setOnCreateContextMenuListener(listener);
			                		//button.setOnTouchListener(Shelf.this);
			                	}
			    			}
			                bookNumber -= 3;
			                Log.i("num", ""+bookNumber);
			            }
			            
			            return layout;
					}
					
					@Override
					public long getItemId(int position) {
						// TODO Auto-generated method stub
						return position;
					}
					
					@Override
					public Object getItem(int position) {
						// TODO Auto-generated method stub
						return size[ position ];
					}
					
					@Override
					public int getCount() {
						// TODO Auto-generated method stub
						//return 4;
				          if ( size.length > 4 ) {
				                return size.length;
				            } else {
				                return 4;
				            }
			            
					}
				};
	     shelf.setAdapter ( mAdapter );
   }

	/***
	 * listview ���ڻ���ʱִ��.
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:����Ϊ������Ϊ��
		

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		layoutParams.leftMargin -= mScrollX;
		layoutParams_1.leftMargin = window_width + layoutParams.leftMargin;
		if (layoutParams.leftMargin >= 0) {
			isScrolling = false;// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			layoutParams.leftMargin = 0;
			layoutParams_1.leftMargin = window_width;

		} else if (layoutParams.leftMargin <= -MAX_WIDTH) {
			// �Ϲ�ͷ�˲���Ҫ��ִ��AsynMove��
			isScrolling = false;
			layoutParams.leftMargin = -MAX_WIDTH;
			layoutParams_1.leftMargin = window_width - MAX_WIDTH;
		}
		Log.v(TAG, "layoutParams.leftMargin=" + layoutParams.leftMargin
				+ ",layoutParams_1.leftMargin =" + layoutParams_1.leftMargin);

		layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
		System.err.println(requestCode);
    	if(requestCode == 0){
    		System.err.println("OKOKOK!!!!");
    		refreshShelf();
    	}
    }

	/***
	 * ��ȡ�ƶ����� �ƶ��ľ�����ʵ����layout_left�Ŀ��
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// ��ȡ�ؼ����
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_right.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = mylaout
							.getLayoutParams();
					// ע�⣺ ����layout_left�Ŀ�ȡ���ֹ�����ƶ���ʱ��ؼ�����ѹ
					layoutParams.width = window_width;
					layout_left.setLayoutParams(layoutParams);

					// ����layout_right�ĳ�ʼλ��.
					layoutParams_1.leftMargin = window_width;
					layout_right.setLayoutParams(layoutParams_1);
					// ע�⣺����lv_set�Ŀ�ȷ�ֹ�����ƶ���ʱ��ؼ�����ѹ
					layoutParams_2.width = MAX_WIDTH;
					mylaout.setLayoutParams(layoutParams_2);

					Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
							+ window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				new AsynMove().execute(SPEED);
				return false;
			}
		}		
		Timer tExit = new Timer();
	    TimerTask task = new TimerTask() {
	        

			@Override
	        public void run() {
	            isExit = false;
	            hasTask = true;
	        }
	    };	
	    if(keyCode == KeyEvent.KEYCODE_MENU){
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				new AsynMove().execute(SPEED);
				return false;
			}
	    }
	    if(keyCode == KeyEvent.KEYCODE_MENU){
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right 
					
					.getLayoutParams();
			if (layoutParams.leftMargin >= 0) {
				new AsynMove().execute(-SPEED);
				return false;
			}
	    }
		if(isExit == false && keyCode == KeyEvent.KEYCODE_BACK) {
			  isExit = true;
			  Toast.makeText(this, "�ٰ�һ�κ��˼��˳�Ӧ�ó���",
					  Toast.LENGTH_SHORT).show(); 
			  if(!hasTask) {
				  tExit.schedule(task, 2000);
			  }
		  } else if(isExit == true && keyCode == KeyEvent.KEYCODE_BACK){
			  finish();
			  System.exit(0);
		  }
		Log.i("exit", "OK");
		return true;
		//return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		shelf.setOnScrollListener(null);
		view = v;// ��¼����Ŀؼ�
		float x1 = 0,x2,y1,y2;
		// �ɿ���ʱ��Ҫ�жϣ������������Ļλ��������ȥ��
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x1 = event.getX();
			y1 = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			x2 = event.getX();
			y2 = event.getY();
			if (Math.abs(x1 - x2) < 6) {
				return false;// �����С������click�¼�������
			}
			if(Math.abs(x1 - x2) >60){
				if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					
					//����ȥ
					if (layoutParams.leftMargin < -window_width / 2) {
						new AsynMove().execute(-SPEED);
					} else {
						new AsynMove().execute(SPEED);
					}
				}// ������onTouch�¼�
			}
		}
		
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		// ��֮��Ϊtrue���Żᴫ�ݸ�onSingleTapUp,��Ȼ�¼��������´���.
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/***
	 * ����ɿ�ִ��
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// ����Ĳ���layout_left
		if (view != null && view == iv_set) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// ���ƶ�
			if (layoutParams.leftMargin >= 0) {
				new AsynMove().execute(-SPEED);
				lv_set.setSelection(0);// ����Ϊ��λ.
			} else {
				// ���ƶ�
				new AsynMove().execute(SPEED);
			}
		} else if (view != null && view == layout_left) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				// ˵��layout_left�����ƶ������״̬�����ʱ��������layout_leftӦ��ֱ������ԭ��״̬.(�����Ի�)
				// ���ƶ�
				new AsynMove().execute(SPEED);
			}
		}

		return true;
	}

	/***
	 * �������� ����һ�����ƶ�������һ����. distanceX=�����x-ǰ���x���������0��˵���������ǰ�����ұ߼����һ���
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// ִ�л���.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	
    protected boolean copyFile() {
		try {
			String dst = txtPath;
			File outFile = new File(dst);
			if (!outFile.exists()) {
				File destDir = new File("/sdcard/Eview");
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }
				InputStream inStream = getResources().openRawResource(
						R.raw.readme);
				outFile.createNewFile();
				FileOutputStream fs = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024 * 1024];// 1MB
				int byteread = 0;
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				Log.i("file", "copy ok!");
				//db.insert(1,"testme",null,"/sdcard/Eview/readme.txt");
				//db.close();
			}
			return true;
		} catch (Exception e) {
			Log.w("file", "copy fail");
			//db.insert(1,"testme",null,"/sdcard/Eview/readme.txt");
			//db.close();
			e.printStackTrace();
			
		}
		return false;
	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// ����
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// ������

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// ���ƶ�
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], 0);
				layoutParams_1.leftMargin = Math.min(layoutParams_1.leftMargin
						+ values[0], window_width);
				Log.v(TAG, "layout_left��" + layoutParams.leftMargin
						+ ",layout_right��" + layoutParams_1.leftMargin);
			} else {
				// ���ƶ�
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], -MAX_WIDTH);
				layoutParams_1.leftMargin = Math.max(layoutParams_1.leftMargin
						+ values[0], window_width - MAX_WIDTH);
				Log.v(TAG, "layout_left��" + layoutParams.leftMargin
						+ ",layout_right��" + layoutParams_1.leftMargin);
			}
			layout_right.setLayoutParams(layoutParams_1);
			layout_left.setLayoutParams(layoutParams);

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		// ֻҪû�л��������ڵ��
		if (layoutParams.leftMargin == -MAX_WIDTH){
			
			if(title[position].equals("�˳�")){
				System.err.println("test OK!");
				finish();
			}
			if(title[position].equals("��������")){
				Toast.makeText(Shelf.this, "�������ðٶ�PCS api�����С���", 1).show();	
				test_login();
				
			}
			if(title[position].equals("�ҵ����")){
				Toast.makeText(Shelf.this, "��������NDK�����С���", 1).show();
			}
			if(title[position].equals("�����ļ�")){
				Intent intent = new Intent();
				intent.putExtra("explorer_title",
						getString(R.string.read_from_dir));
				intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
				intent.setClass(Shelf.this, ExDialog.class);
				startActivityForResult(intent, RUSULT_CODE_1);

				
			}
			if(title[position].equals("�������")){
				System.err.println("test OK!");
				startActivity(new Intent("com.android.eview.about"));
			}
		}
		
	}
	
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
		
	
    }



}