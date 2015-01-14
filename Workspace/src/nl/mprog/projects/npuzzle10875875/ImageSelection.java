package nl.mprog.projects.npuzzle10875875;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageSelection extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_selection);
		
		// if an old puzzle still exists, open it		
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		if (gameSave.contains("Moves")) {
			Intent intent = new Intent(ImageSelection.this, GamePlay.class);
			startActivity(intent);
		}
		
		
		// set the custom list adapter to the listview
		ListAdapter listAdapter = new ImageAdapter(this);
		ListView lstView = (ListView) findViewById(R.id.lstV_choose_img);
		lstView.setAdapter(listAdapter);
 

		// handle clicks on the list items
		lstView.setOnItemClickListener(
			new AdapterView.OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //String puzzles = String.valueOf(parent.getItemAtPosition(position));
                	
                	// save selected image to shared preferences
                	SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
                	SharedPreferences.Editor editor = gameSave.edit();
                	editor.putInt("imageID", (int) id);
                	editor.commit();
                	                    	
                    // start new gameplay activity
                    Intent intent = new Intent(ImageSelection.this, GamePlay.class);
                    startActivity(intent);
                }
            }
        );
	}
}
