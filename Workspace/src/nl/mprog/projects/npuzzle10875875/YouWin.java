package nl.mprog.projects.npuzzle10875875;

import nl.mprog.projects.npuzzle10875875.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 	Jordi van Ditmar
 * 			jorditmar@hotmail.com
 * 			Student ID: 10875875
 */

public class YouWin extends Activity {
	int dimension;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set to fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.you_win);
	}
	
	public void onResume() {
		// load the data to be displayed
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		int imageID = gameSave.getInt("imageID", 0);
		int moves = gameSave.getInt("moves", 99999);
		dimension = gameSave.getInt("dimension", 4);
		
		// display the competed image (with text)
		ImageView imgView = (ImageView) findViewById(R.id.you_win_image);
		TextView txtView = (TextView) findViewById(R.id.you_win_text);
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), imageID);
		imgView.setImageBitmap(bitmap);
		txtView.setText("Congratulations, you won! It took you " + moves + " moves.");
	}
	
	public void onPause() {
		super.onPause();
		
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
		editor.clear();
		editor.putInt("dimension", dimension);
		editor.commit();
	}
}
