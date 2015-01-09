package nl.mprog.projects.npuzzle10875875;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
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
		
		//TODO replace hardcoded puzzle names
		String[] puzzles = {"Puzzle 1", "Puzzle 2", "Puzzle 3", "Puzzle 4", "Puzzle 5", "Puzzle 6"};
		
		// set the custom list adapter to the listview
        ListAdapter listAdapter = new CustomAdapter(this, puzzles);
        ListView lstV = (ListView) findViewById(R.id.lstV_choose_img);
        lstV.setAdapter(listAdapter);

        // handle clicks on the list items
        lstV.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String puzzles = String.valueOf(parent.getItemAtPosition(position));
                        
                        // start new gameplay activity
                        Intent intent = new Intent(ImageSelection.this, GamePlay.class);
                        startActivity(intent);
                    }
                }
        );
	}
}
