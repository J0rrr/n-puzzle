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


public class YouWin extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set to fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.you_win);
		
		// load the data to be displayed
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		int imageID;
		imageID = gameSave.getInt("imageID", 0);
		
		// display the competed image
		ImageView imgView = (ImageView) findViewById(R.id.you_win_image);
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), imageID);
		imgView.setImageBitmap(bitmap);
	}
}
