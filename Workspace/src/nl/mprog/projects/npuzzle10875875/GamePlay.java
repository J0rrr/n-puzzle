package nl.mprog.projects.npuzzle10875875;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 	Jordi van Ditmar
 * 			jorditmar@hotmail.com
 * 			Student ID: 10875875
 */

public class GamePlay extends Activity {
	
	private int dimension;
	private int imageID;
	private int moves;
	private int[][] cellArray;
	private int[][] defaultCellArray;
	private Bitmap[] imageArray;
	private GridView gridView;
	private int cellWidth;
	private int cellHeight;
	private ImageAdapter imgAdapter;
	private int screenWidth;
	private int screenHeight;
	private boolean doSave;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set to fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.game_play);		
	}
	
	public void onResume() {
		super.onResume();
		loadData();
		doSave = true;
		
		//Toast.makeText(GamePlay.this, "Moves: " + moves, Toast.LENGTH_SHORT).show();
		
		cutImageToPieces();		
		generateGridView();		
		
		if (moves == 0) {
			// wait for 3 seconds
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			    @Override
			    public void run() {
			        // Do this after 3000 milliseconds
			        shuffleTiles();
			        handleClick();
			    }
			}, 3000);
		}
		else {
	        handleClick();
		}
	}
	
	public void onPause() {
		super.onPause();
		
		// save to sharedprefs
		if (doSave == true)
			saveData();
	}
	
	public void shuffleTiles() {
		// put the tiles in reversed order 
		// (from left to right, up to down: counting from dimension^2 to 0)
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				cellArray[x][y] = dimension * dimension - 2 - ((x % dimension) + (y * dimension));
			}
		}
		// set the empty tile to be the lower right one
		cellArray[dimension - 1][dimension - 1] = dimension * dimension - 1;
		
		// swap the two tiles next to the empty tile when necessary
		if (dimension % 2 == 0) {
			cellArray[dimension - 2][dimension - 1] = 1;
			cellArray[dimension - 3][dimension - 1] = 0;
		}
		
		imgAdapter.notifyDataSetChanged();
	}
	
	public void generateGridView() {
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setNumColumns(dimension);
		
		ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
		layoutParams.width = cellWidth * dimension;
		layoutParams.height = cellHeight * dimension;
		gridView.setLayoutParams(layoutParams);
		
		imgAdapter = new ImageAdapter(this);
		gridView.setAdapter(imgAdapter);	
		
		imgAdapter.notifyDataSetChanged();
	}
	
	public void handleClick() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				// ACTUAL BRAINS OF THE GAME
				int cur_x = position % dimension;
				int cur_y = position / dimension;
				
				int empty_tile = dimension * dimension - 1;
				
				// check to the right of tapped tile for empty tile
				if ((cur_x + 1 < dimension) && (cellArray[cur_x + 1][cur_y] == empty_tile)) {
					cellArray[cur_x + 1][cur_y] = cellArray[cur_x][cur_y];
					cellArray[cur_x][cur_y] = empty_tile;
					moves++;
				}
				
				// check to the left of tapped tile for empty tile
				else if ((cur_x - 1 >= 0) && (cellArray[cur_x - 1][cur_y] == empty_tile)) {
					cellArray[cur_x - 1][cur_y] = cellArray[cur_x][cur_y];
					cellArray[cur_x][cur_y] = empty_tile;
					moves++;
				}
				
				// check below tapped tile for empty tile
				else if ((cur_y + 1 < dimension) && (cellArray[cur_x][cur_y + 1] == empty_tile)) {
					cellArray[cur_x][cur_y + 1] = cellArray[cur_x][cur_y];
					cellArray[cur_x][cur_y] = empty_tile;
					moves++;
				}
				
				// check above tapped tile for empty tile
				else if ((cur_y - 1 >= 0) && (cellArray[cur_x][cur_y - 1] == empty_tile)) {
					cellArray[cur_x][cur_y - 1] = cellArray[cur_x][cur_y];
					cellArray[cur_x][cur_y] = empty_tile;
					moves++;
				}
				
				else {
					Toast.makeText(GamePlay.this, "Invalid move!", Toast.LENGTH_SHORT).show();
				}				
				
				checkIfWon();
				imgAdapter.notifyDataSetChanged();
			}
        });
	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return imageArray.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(cellWidth, cellHeight));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(3, 3, 3, 3);
	            imageView.setBackgroundColor(Color.BLACK);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        int y = position / dimension;
	        int x = position % dimension;
	        imageView.setImageBitmap(imageArray[cellArray[x][y]]);

	        return imageView;
	    }
	}
	
	public boolean checkIfWon() {
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if (cellArray[x][y] != (x % dimension) + (y * dimension)) {
					return false;
				}
			}
		}    	                    	
        // start the YouWin activity
        Intent intent = new Intent(GamePlay.this, YouWin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
		GamePlay.this.finish();
		return true;
	}
	
	public void loadData() {
		// load data from shared preferences (if there is data)
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		
		dimension = gameSave.getInt("dimension", 4);
		imageID = gameSave.getInt("imageID", R.drawable.puzzle_0);
		moves = gameSave.getInt("moves", 0);
		
		// load the 'array' with the tile positions
		cellArray = new int[dimension][dimension];
		defaultCellArray = new int[dimension][dimension];
		
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				defaultCellArray[x][y] = (x % dimension) + (y * dimension);
				cellArray[x][y] = gameSave.getInt("cellArray_" + x + y, defaultCellArray[x][y]);
			}
		}		
		
		// get screen width and height
	 	screenWidth = this.getResources().getDisplayMetrics().widthPixels;
	 	screenHeight = this.getResources().getDisplayMetrics().heightPixels;
	}
	
	public void saveData() {
		// save to shared preferences
    	SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
    	editor.putInt("dimension", dimension);
    	editor.putInt("imageID", imageID);
    	editor.putInt("moves", moves);
    	
    	for(int x = 0; x < dimension; x++)
        	for(int y = 0; y < dimension; y++)
        		editor.putInt("cellArray_" + x + y, cellArray[x][y]);
    	
    	// commit the preferences
    	editor.commit();
		
	}
	
	public void deleteData() {
    	SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
    	
    	editor.remove("moves");
    	moves = 0;
    	
    	for(int x = 0; x < dimension; x++) {
        	for(int y = 0; y < dimension; y++) {
        		editor.remove("cellArray_" + x + y);
    			cellArray[x][y] = (x % dimension) + (y * dimension);
        	}
    	}
    	editor.commit();
	}
	
	public void reload() {
        Intent intent = new Intent(GamePlay.this, GamePlay.class);
        startActivity(intent);
		GamePlay.this.finish();
		
//		
//	    Intent intent = getIntent();
//	    overridePendingTransition(0, 0);
//	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	    finish();
//
//	    overridePendingTransition(0, 0);
//	    startActivity(intent);
	}
	
	public void cutImageToPieces() {
		// load image from image id
		Bitmap image = BitmapFactory.decodeResource(this.getResources(), (int) imageID);

		// get width and height from image
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		// compute a matrix scale that keeps the images' aspect ratio
		Matrix matrix = new Matrix();
		matrix.setRectToRect(
				new RectF(0, 0, imageWidth, imageHeight), 
				new RectF(0, 0, screenWidth, screenHeight), 
				Matrix.ScaleToFit.CENTER);
		
		// scale the image to fit the screen (keeping its original aspect ratio)
		Bitmap scaledImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true);	
		
		// cell size
		cellWidth = scaledImage.getWidth() / dimension;
		cellHeight = scaledImage.getHeight() / dimension;
				
		imageArray = new Bitmap[dimension * dimension];
		
		// create an array of cropped images
		int n = 0;
		for (int y = 0; y < dimension; y++) {
			for (int x = 0; x < dimension; x++) {
				imageArray[n] = Bitmap.createBitmap(scaledImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
				n++;
			}
		}
		
		// set the last tile to be the 'empty' one
		imageArray[dimension * dimension - 1] = null;
		
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
			dimension = 3;
			
			SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
	    	SharedPreferences.Editor editor = gameSave.edit();
			editor.clear();
			editor.putInt("dimension", dimension);
	    	editor.putInt("imageID", imageID);
			editor.commit();
			doSave = false;

			reload();
			return true;
		}
		
		if (id == R.id.medium) {
			dimension = 4;
			
			SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
	    	SharedPreferences.Editor editor = gameSave.edit();
			editor.clear();
			editor.putInt("dimension", dimension);
	    	editor.putInt("imageID", imageID);
			editor.commit();
			doSave = false;

			reload();
			return true;
		}
		
		if (id == R.id.hard) {
			dimension = 5;
			
			SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
	    	SharedPreferences.Editor editor = gameSave.edit();
			editor.clear();
			editor.putInt("dimension", dimension);
	    	editor.putInt("imageID", imageID);
			editor.commit();
			doSave = false;

			reload();
			return true;
		}
		
		if (id == R.id.change_image) {			
			SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
	    	SharedPreferences.Editor editor = gameSave.edit();
			editor.clear();
			editor.putInt("dimension", dimension);
			editor.commit();
			doSave = false;
			
            Intent intent = new Intent(GamePlay.this, ImageSelection.class);
            startActivity(intent);
			GamePlay.this.finish();			
			
			
			return true;
		}
		
		if (id == R.id.reshuffle) {
			Toast.makeText(GamePlay.this, "Reshuffle", Toast.LENGTH_SHORT).show();

			deleteData();
			doSave = false;
			
			reload();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
