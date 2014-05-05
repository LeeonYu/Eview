package com.sqlite;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.sqlite.BookInfo;
import com.sqlite.BookMark;
import com.sqlite.SetupInfo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.test.AndroidTestCase;
import android.view.WindowManager;


public class DbHelper<Bookname> extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "yiview_db";
    private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "book_name";
	private final static String TABLE_SETUP = "book_setup";
	private final static String TABLE_MARK = "book_mark";
	public final static String FIELD_ID = "_id";
	public final static String FIELD_FILENAME = "filename";//图书名称
	public final static String FIELD_AUTOMARK = "automark";//自动书签
	public final static String FIELD_BOOKMARK = "bookmark";//书签
    public final static String FIELD_PATH="fpath"; //路径
	public final static String FONT_SIZE = "sbFontSize";//字体大小
	public final static String BRIGHTNESS = "sbBrightness";//亮度
	public final static String BACKGROUND = "sbBackground";//背景
	public int count;
	
	public DbHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

	@Override
	
	public void onCreate(SQLiteDatabase db) {
			
	// TODO Auto-generated method stub
			
	StringBuffer sqlCreateCountTb = new StringBuffer();
	sqlCreateCountTb.append("create table ").append(TABLE_NAME).append("(_id integer primary key autoincrement,").append(" filename text,").append(" automark text,").append(" fpath text );");
	db.execSQL(sqlCreateCountTb.toString());
			
	
			
	//系统设置表
    StringBuffer setupTb = new StringBuffer();
	setupTb.append("create table ").append(TABLE_SETUP).append("(_id integer primary key autoincrement,").append(" sbFontSize text,").append(" sbBrightness text,").append(" sbBackground text);");
			
	db.execSQL(setupTb.toString());
			
	String setup = "insert into " + TABLE_SETUP + "(sbFontSize,sbBrightness,sbBackground) values('25','0.3','0')";
	db.execSQL(setup);
	
	
	 StringBuffer markTb = new StringBuffer();
	 markTb.append("create table ").append(TABLE_MARK).append("(_id integer primary key autoincrement,").append(" filename text,").append(" bookmark text);");
				
	 db.execSQL(markTb.toString());
	
	
	// String mark = "insert into " + TABLE_MARK + "(filename,bookmark) values('0','0')";
	// db.execSQL(mark);
	 
		}


	@Override
	
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			
	// TODO Auto-generated method stub
			
	String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
	db.execSQL(sql);
    onCreate(db);
		}
	public Cursor select() {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null," _id desc");
		return cursor;
			}

			
		public BookInfo getBookInfo(int id){
				
		BookInfo book = new BookInfo();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query(TABLE_NAME, null, "_id=" + id, null, null, null, null);
		cursor.moveToPosition(0);
		book.id = id;
		book.bookname = cursor.getString(1);
		book.automark = cursor.getInt(2);
		book.fpath = cursor.getString(3);
		db.close();
		return book;
			}
						
		public SetupInfo getSetupInfo(){
				
		SetupInfo setup = new SetupInfo();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query(TABLE_SETUP, null, null, null, null, null, null);
		cursor.moveToPosition(0);
		setup.id = cursor.getInt(0); 
		setup.sbFontSize = cursor.getInt(1);
		setup.sbBrightness = cursor.getInt(2);
		setup.sbBackground = cursor.getInt(3);
		db.close();
		return setup;
			}

		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
		public BookMark getBookMark(int id){
			
		BookMark mark =new BookMark();
		SQLiteDatabase db =this.getReadableDatabase();
		Cursor cursor=null;
		cursor=db.query(TABLE_MARK, null, "_id=" + id, null, null, null, null);
		cursor.moveToPosition(0);
		mark.id=id;
		mark.bookname=cursor.getString(1);
		mark.bookmark=cursor.getFloat(2);
		db.close();
		return mark;
		}
		
		public BookMark getAllBookMark(){
			
	    BookMark mark=new BookMark();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=null;
		cursor=db.query(TABLE_MARK, null,null, null, null, null, null);
		count=cursor.getCount();
		db.close();
		return mark;
			
		}
		
		public BookInfo getallBookInfo(){
			
			BookInfo book = new BookInfo();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = null;
			cursor = db.query(TABLE_NAME, null,  null,null, null, null, null);
			cursor.moveToPosition(0);
			count=cursor.getCount();
			db.close();
			return book;
			
			
		}
		public int getcount(){
			
			BookInfo book = new BookInfo();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = null;
			cursor = db.query(TABLE_NAME, null,  null,null, null, null, null);
			cursor.moveToPosition(0);
			count=cursor.getCount();
			db.close();
			return count;
			
			
		}
		public List<BookInfo> getAllBookInfo(){
				
		List<BookInfo> books = new ArrayList<BookInfo>();
				
		SQLiteDatabase db = this.getReadableDatabase();
				
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, " _id desc");

		int count = cursor.getCount();
				
		for (int i = 0; i < count; i++) {
					
		cursor.moveToPosition(i);
					
		BookInfo book = new BookInfo();
					
		book.id = cursor.getInt(0);
					
		book.bookname = cursor.getString(1);
					
		book.automark = cursor.getInt(2);
		
		book.fpath =cursor.getString(3);
					
		books.add(book);
				}
				
		return books;
			}
			

		public long insert(long id,String filename,String automark,String fpath) {
			
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues cv = new ContentValues();
		
		cv.put(FIELD_ID, id);
				
		cv.put(FIELD_FILENAME, filename);
		
		cv.put(FIELD_AUTOMARK, automark);
		
		cv.put(FIELD_PATH, fpath);
				
		long row = db.insert(TABLE_NAME, null, cv);
				
		return row;
			}	
			
		public long insert(long id,String filename, float bookmark){
				
		SQLiteDatabase db = this.getWritableDatabase();
				
		ContentValues cv = new ContentValues();
		
		cv.put(FIELD_ID, id);
				
		cv.put(FIELD_FILENAME, filename);
				
		cv.put(FIELD_BOOKMARK, bookmark);
				
		long row = db.insert(TABLE_MARK, null, cv);
				
		return row;
			}

			
		public void delete(int id) {
				
		SQLiteDatabase db = this.getWritableDatabase();
				
		String where = FIELD_ID + "=?";
				
		String[] whereValue = { Integer.toString(id) };
				
		db.delete(TABLE_NAME, where, whereValue);
			}

			
		public void update( int id, String filename, float automark) {
				
		SQLiteDatabase db = this.getWritableDatabase();
				
		String where = FIELD_ID + "=?";
				
		String[] whereValue = { Integer.toString(id) };
				
		ContentValues cv = new ContentValues();
				
		cv.put(FIELD_FILENAME, filename);
				
		cv.put(FIELD_AUTOMARK, automark);
				
		db.update(TABLE_NAME, cv, where, whereValue);
			}
			
			
		public void updateSetup(int id, int whichSize, float sbBrightness,int i) {
				

		SQLiteDatabase db = this.getWritableDatabase();
		
		String where = FIELD_ID + "=?";
				
		String[] whereValue = { Integer.toString(id) };
				
		ContentValues cv = new ContentValues();
				
		cv.put(FONT_SIZE, whichSize);
				
		cv.put(BRIGHTNESS, sbBrightness);
				
		cv.put(BACKGROUND, i);
				
		db.update(TABLE_SETUP, cv, where, whereValue);
			}

		public void updatemark( int id, float bookmark ,String filename) {
			
			SQLiteDatabase db = this.getWritableDatabase();
					
			String where = FIELD_ID + "=?";
					
			String[] whereValue = { Integer.toString(id) };
					
			ContentValues cv = new ContentValues();
					
			
					
			cv.put(FIELD_BOOKMARK, bookmark);
			
			cv.put(FIELD_FILENAME, filename);
					
			db.update(TABLE_MARK, cv, where, whereValue);
				}
	
		}
 
		


		

	
