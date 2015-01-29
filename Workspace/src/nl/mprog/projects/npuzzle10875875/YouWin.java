package nl.mprog.projects.npuzzle10875875;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	int moves;
	int imageID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set activity to fill the entire screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.you_win);
	}
	
	public void onResume() {
		super.onResume(); 														// DIT WAS EEN ONPAUSE
		// load the data to be displayed
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		dimension = gameSave.getInt("dimension", 4);
		imageID = gameSave.getInt("imageID", R.drawable.puzzle_0);
		moves = gameSave.getInt("moves", 0);
		
		// display the competed image (with text)
		ImageView imgView = (ImageView) findViewById(R.id.you_win_image);
		TextView txtView = (TextView) findViewById(R.id.you_win_text);
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), imageID);
		imgView.setImageBitmap(bitmap);
		txtView.setText("Congratulations, you won! It took you " + moves + " moves.");
	}
	
	
																				// IS DEZE NOG NODIG?
	public void onPause() {
		super.onPause();
	}
	
	// handle when the button is clicked
	public void onBtnClick(View view) {
		// delete all the shared preferences, except for the difficulty setting
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
		editor.clear();
		editor.putInt("dimension", dimension);
		editor.commit();		
		
		// start the 'image selection' activity
        Intent intent = new Intent(YouWin.this, ImageSelection.class);
        startActivity(intent);
		YouWin.this.finish();	
	}
}
