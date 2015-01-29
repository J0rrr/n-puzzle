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
	private boolean newGame;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// set to fill the entire screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.game_play);		
	}
	
	public void onResume() {
		super.onResume();
		
		// load the shared preferences
		loadData();
		
		// create cropped bitmap 'tiles' from the selected image
		cutImageToPieces();	
		
		// generate the gridview and fill it with the cropped images 
		// (in the 'solved' order)
		generateGridView();		
		
		// if no move has been done yet, preview the solution for 3 seconds
		if (newGame == true) {
			// wait 3 seconds
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			    @Override
			    public void run() {
			        // After 3 seconds, shuffle the board and wait for clicks
			        shufflemptys();
			        handleClick();
			    }
			}, 3000);
		}
		
		// else, wait for clicks right away
		else {
	        handleClick();
		}
		
		newGame = false;
	}
	
	public void onPause() {
		super.onPause();
		
		// save to shared preferences (unless when told not to, see menu items)
		if (newGame == false)
			saveData();
	}
	
	/*
	 * 'Shuffles' the tiles. Note, this is not a true shuffle, this method
	 * merely puts all the tiles in reversed order, puts the empty tile in
	 * the right spot and swaps the second- and third-last tiles if
	 * necessary (in order to make the puzzle solvable).
	 */
	public void shufflemptys() {	
		// put the tiles in reversed order 
		// (from left to right, up to down: counting from dimension^2 to 0)
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				cellArray[x][y] = dimension * dimension - 2 
						- ((x % dimension) + (y * dimension));
			}
		}
		
		// set the empty tile to be the lower right one
		cellArray[dimension - 1][dimension - 1] = dimension * dimension - 1;
		
		// swap the two tiles next to the empty tile when necessary
		if (dimension % 2 == 0) {
			cellArray[dimension - 2][dimension - 1] = 1;
			cellArray[dimension - 3][dimension - 1] = 0;
		}
		
		// notify the image adapter that its content has changed
		imgAdapter.notifyDataSetChanged();
	}
	
	/*
	 * Generates a grid view with dimensions 'dimension' (3x3, 4x4 or 5x5).
	 */
	public void generateGridView() {
		// find the right xml-file item and set the number of columns
		gridView = (GridView) findViewById(R.id.grid_view);
		gridView.setNumColumns(dimension);
		
		// set the width and height of the grids cells
		ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
		layoutParams.width = cellWidth * dimension;
		layoutParams.height = cellHeight * dimension;
		gridView.setLayoutParams(layoutParams);
		
		// set the grid views image adapter
		imgAdapter = new ImageAdapter(this);
		gridView.setAdapter(imgAdapter);	
		imgAdapter.notifyDataSetChanged();
	}
	
	/*
	 * This is the adapter that fills the grid view with images
	 */
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

	    // create a new image view for each item referenced by the adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        
	        // if there is no recyclable image view, create a new one and set 
	        // its attributes
	        if (convertView == null) {
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new 
	            		GridView.LayoutParams(cellWidth, cellHeight));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(3, 3, 3, 3);
	            imageView.setBackgroundColor(Color.BLACK);
	        
	        // if there is a recyclable image view, use that one instead
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        
	        // base x and y on the position in the grid view
	        int y = position / dimension;
	        int x = position % dimension;
	        
	        // fill the grid items with the appropriate cropped image
	        imageView.setImageBitmap(imageArray[cellArray[x][y]]);

	        return imageView;
	    }
	}	
	
	/*
	 * Handles when a tile is clicked during gameplay.
	 * This method can be considered as the actual 'brains' of the game.
	 * Here, calculations are done to check if a valid move is selected.
	 * If so, the move is executed followed by a check for the solved board.
	 */
	public void handleClick() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				
				// derive the x- and y- coordinates clicked from the gridviews
				// clicked position
				int x = position % dimension;
				int y = position / dimension;
				
				// calculate the value of the empty tile
				int empty = dimension * dimension - 1;
				
				// check to the right of tapped tile for empty tile
				if ((x + 1 < dimension) && (cellArray[x + 1][y] == empty)) {
					cellArray[x + 1][y] = cellArray[x][y];
					cellArray[x][y] = empty;
					moves++;
				}
				
				// check to the left of tapped tile for empty tile
				else if ((x - 1 >= 0) && (cellArray[x - 1][y] == empty)) {
					cellArray[x - 1][y] = cellArray[x][y];
					cellArray[x][y] = empty;
					moves++;
				}
				
				// check below tapped tile for empty tile
				else if ((y + 1 < dimension) && (cellArray[x][y + 1] == empty)) {
					cellArray[x][y + 1] = cellArray[x][y];
					cellArray[x][y] = empty;
					moves++;
				}
				
				// check above tapped tile for empty tile
				else if ((y - 1 >= 0) && (cellArray[x][y - 1] == empty)) {
					cellArray[x][y - 1] = cellArray[x][y];
					cellArray[x][y] = empty;
					moves++;
				}
				
				// if none of the above: print that no valid tile was clicked
				else {
					Toast.makeText(GamePlay.this, "Invalid move!", Toast.LENGTH_SHORT).show();
				}				
				
				// check if the puzzle is solved
				checkIfWon();
				
				// notify the grid view adapter that its content has changed
				imgAdapter.notifyDataSetChanged();
			}
        });
	}
	
	/*
	 * Checks if the current board is in its solved state.
	 * Returns false if not so, opens the YouWin activity if so.
	 */
	public boolean checkIfWon() {
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				// if any of the tiles is not in its final place, return false
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
	
	/*
	 * Loads data from the shared preferences.
	 * If no data exists yet, the default data is loaded 
	 * (second argument in the "get..." statements).
	 */
	public void loadData() {
		// load data from shared preferences
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
		
		dimension = gameSave.getInt("dimension", 4);
		imageID = gameSave.getInt("imageID", R.drawable.puzzle_0);
		moves = gameSave.getInt("moves", 0);
		newGame = gameSave.getBoolean("newGame", true);
		
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
	
	/*
	 * Saves data into the shared preferences
	 * (very similar to the loadData() method)
	 */
	public void saveData() {
		// save to shared preferences
    	SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
    	editor.putInt("dimension", dimension);
    	editor.putInt("imageID", imageID);
    	editor.putInt("moves", moves);
    	editor.putBoolean("newGame", newGame);
    	
    	for(int x = 0; x < dimension; x++)
        	for(int y = 0; y < dimension; y++)
        		editor.putInt("cellArray_" + x + y, cellArray[x][y]);
    	
    	// commit the preferences
    	editor.commit();
		
	}
	
	public void deleteData() {
		newGame = true;
		
		SharedPreferences gameSave = getSharedPreferences("gameSave", 0);
    	SharedPreferences.Editor editor = gameSave.edit();
		editor.clear();
		editor.putInt("dimension", dimension);
    	editor.putInt("imageID", imageID);
    	editor.putBoolean("newGame", newGame);
		editor.commit();
		moves = 0;
	}
	
	/*
	 * Reloads the board by restarting the 'GamePlay' activity
	 */
	public void reload() {
        Intent intent = new Intent(GamePlay.this, GamePlay.class);
        startActivity(intent);
		GamePlay.this.finish();
	}
	
	/*
	 * Cuts the image with Id 'imageID' into dimension*dimension pieces.
	 * Puts all these pieces in an array (imageArray), and replaces the last
	 * piece with 'null', as to create an 'empty' tile.
	 */
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
		
		// calculate the gridviews cell size
		cellWidth = scaledImage.getWidth() / dimension;
		cellHeight = scaledImage.getHeight() / dimension;
				
		// create an array of ordered cropped images
		imageArray = new Bitmap[dimension * dimension];
		
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

	/*
	 * Handles what happens when a menu item is clicked.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.change_difficulty) {
			return true;
		}
		
		if (id == R.id.easy) {
			// set the new dimension, delete the data and reload the activity
			dimension = 3;
			deleteData();
			reload();
			
			return true;
		}
		
		if (id == R.id.medium) {
			// set the new dimension, delete the data and reload the activity
			dimension = 4;
			deleteData();
			reload();
			
			return true;
		}
		
		if (id == R.id.hard) {
			// set the new dimension, delete the data and reload the activity
			dimension = 5;
			deleteData();
			reload();
			
			return true;
		}
		
		if (id == R.id.change_image) {
			// delete the data and start the ImageSelection activity
			deleteData();

            Intent intent = new Intent(GamePlay.this, ImageSelection.class);
            startActivity(intent);
			GamePlay.this.finish();
			
			return true;
		}
		
		if (id == R.id.reshuffle) {
			// delete the data and reload the activity
			deleteData();
			reload();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
