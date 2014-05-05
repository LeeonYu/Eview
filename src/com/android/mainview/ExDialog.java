package com.android.mainview;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.BaseAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.app.ListActivity;
import android.view.WindowManager;
import android.view.Display;
import android.view.WindowManager.LayoutParams;

import com.android.eview.R;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sqlite.BookInfo;
import com.sqlite.DbHelper;


public class ExDialog extends ListActivity {
	private static final int RUSULT_CODE_1 = 1;
	private List<Map<String, Object>> mData;
	private String mDir = "/sdcard";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bl = intent.getExtras();
		String title = bl.getString("explorer_title");
		Uri uri = intent.getData();
		mDir = uri.getPath();

		setTitle(title);
		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();
		LayoutParams p = getWindow().getAttributes();
		p.height = (int) (d.getHeight() * 0.8);
		p.width = (int) (d.getWidth() * 0.95);
		getWindow().setAttributes(p);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&!mDir.equals("/sdcard")){
			//mDir = (String) mData.get(position).get("info");
			mData = getData();
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		File f = new File(mDir);
		File[] files = f.listFiles();

		if (!mDir.equals("/mnt/shell/emulated/0")) {
			map = new HashMap<String, Object>();
			map.put("title", "Back to ../");
			map.put("info", f.getParent());
			map.put("img", R.drawable.ex_folder);
			list.add(map);
		}
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				map = new HashMap<String, Object>();
				//if(files[i].isFile()&&getMIMEType(files[i].getName())=="text"){}
				map.put("title", files[i].getName());
				map.put("info", files[i].getPath());
				
				if (files[i].isDirectory()&&!files[i].isHidden())
					map.put("img", R.drawable.ex_folder);
				//if( getExtensionName(files[i].getName()).equals("txt"))
				else
					map.put("img", R.drawable.ex_doc);
				
				list.add(map);
			}
		}
		return list;
	}
	
	/** 获取MIME类型 **/
	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
				|| end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
				|| end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("MyListView4-click", (String) mData.get(position).get("info"));
		String fpath=(String) mData.get(position).get("info");
		String fname=getFileNameNoEx(fpath.substring(fpath.lastIndexOf("/")+1)) ;
		float pro = 0;
		if ((Integer) mData.get(position).get("img") == R.drawable.ex_folder) {
			mDir = (String) mData.get(position).get("info");
			mData = getData();
			MyAdapter adapter = new MyAdapter(this);
			setListAdapter(adapter);
		} else {
			//finishWithResult((String) mData.get(position).get("info"));
			//readtext(fpath);
			boolean fExist=false;
			int i;
			
			DbHelper db=new DbHelper(this);
			int count=db.getcount();

			
			for ( i = 1; i < count+1; i++){
			if(fname.equals(db.getBookInfo(i).bookname))
		    {
				fExist=true;
				pro=db.getBookInfo(i).automark;
			}
			}
			if(fExist==false){
				db.insert(db.getcount()+1,fname,null,fpath);
				
			}
			Intent intent=new Intent("com.android.eview.reader");
			intent.putExtra("pa", fpath);
			intent.putExtra("fn", fname);
			intent.putExtra("pro", pro);
			intent.putExtra("id", db.getcount()+1);
	     	startActivity(intent);
	     	setResult(RUSULT_CODE_1);
	     	finish();
		}
	}
	
    public static String getFileNameNoEx(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length()))) {    
                return filename.substring(0, dot);    
            }    
        }    
        return filename;    
    } 
    public static String getExtensionName(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(dot + 1);    
            }    
        }    
        return filename;    
    }  

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return mData.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listview, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));
			return convertView;
		}
	}

	private void finishWithResult(String path) {
		Bundle conData = new Bundle();
		conData.putString("results", "Thanks Thanks");
		Intent intent = new Intent();
		intent.putExtras(conData);
		Uri startDir = Uri.fromFile(new File(path));
		intent.setDataAndType(startDir,
				"vnd.android.cursor.dir/lysesoft.andexplorer.file");
		Intent intent2 = new Intent("com.android.eview.reader");
		startActivity(intent2);
		finish();
	}
	private void readtext(String fpatch){
		try{
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				
				Bundle conData = new Bundle();
				conData.putString("results", "Thanks Thanks");
				Intent intent = new Intent();
				intent.putExtras(conData);
				Uri startDir = Uri.parse(fpatch.toString());
				intent.setDataAndType(startDir,
						"vnd.android.cursor.dir/lysesoft.andexplorer.file");
				setResult(RESULT_OK, intent);
				finish();
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//转变编码 
	/*public String convertCodeAndGetText(String str_filepath) {
		  
        File file = new File(str_filepath);  
        BufferedReader reader;  
        String text = "";  
        try {  
              
            FileInputStream fis = new FileInputStream(file);  
            BufferedInputStream in = new BufferedInputStream(fis);  
            in.mark(4);  
            byte[] first3bytes = new byte[3];  
            in.read(first3bytes);  
            in.reset();  
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB  
                    && first3bytes[2] == (byte) 0xBF) {// utf-8  
                  reader = new BufferedReader(new InputStreamReader(in, "utf-8"));  
            } else if (first3bytes[0] == (byte) 0xFF  
                    && first3bytes[1] == (byte) 0xFE) {  
                  reader = new BufferedReader(new InputStreamReader(in, "unicode"));  
            } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {  
                  reader = new BufferedReader(new InputStreamReader(in,  "utf-16be"));  
            } else if (first3bytes[0] == (byte) 0xFF  && first3bytes[1] == (byte) 0xFF) {  
                  reader = new BufferedReader(new InputStreamReader(in,  "utf-16le"));  
            } else {  
                  reader = new BufferedReader(new InputStreamReader(in, "GBK"));  
            }  
            String str = reader.readLine();  
  
            while (str != null) {  
                text = text + str + "\n";
                str = reader.readLine();  
            }  
            reader.close();  
      } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return text;  
    }  */

}
