package nl.mprog.projects.npuzzle10875875;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {
	/*
	 * Fills the listview with the data at the appropriate 
	 * places, e.g. the puzzle names in the textview and images
	 * in the imageviews. 
	 */

    CustomAdapter(Context context, String[] puzzles) {
        super(context, R.layout.list_rowlayout , puzzles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutinflater = LayoutInflater.from(getContext());
        View customView = layoutinflater.inflate(R.layout.list_rowlayout, parent, false);

        // connect to the views in the layout (xml) file
        String slingle_item = getItem(position);
        TextView txtV = (TextView) customView.findViewById(R.id.txtV_title);
        ImageView imgV = (ImageView) customView.findViewById(R.id.imgV_image);

        // set the text and the image
        txtV.setText(slingle_item);
        imgV.setImageResource(R.drawable.puzzle_0);
        
        return customView;
    }
}	
