package nl.mprog.projects.npuzzle10875875;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GamePlay extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_play);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_play, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.change_difficulty) {
			//Toast.makeText(GamePlay.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		if (id == R.id.easy) {
			Toast.makeText(GamePlay.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		if (id == R.id.medium) {
			Toast.makeText(GamePlay.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		if (id == R.id.hard) {
			Toast.makeText(GamePlay.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		if (id == R.id.change_image) {
			Toast.makeText(GamePlay.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		if (id == R.id.reshuffle) {
			Toast.makeText(GamePlay.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
