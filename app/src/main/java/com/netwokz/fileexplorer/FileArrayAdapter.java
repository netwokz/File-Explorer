package com.netwokz.fileexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Steve-O on 6/11/2016.
 */
public class FileArrayAdapter extends ArrayAdapter<File> {
    private Context mContext; //Activity context.
    private int mResource; //Represents the list_rowl file (our rows) as an int e.g. R.layout.list_row
    private List<File> mObjects; //The List of objects we got from our model.

    public FileArrayAdapter(Context c, int res, List<File> o) {
        super(c, res, o);
        mContext = c;
        mResource = res;
        mObjects = o;
    }

    public FileArrayAdapter(Context c, int res) {
        super(c, res);
        mContext = c;
        mResource = res;
    }

    @Override
    public File getItem(int i) {
        return mObjects.get(i);
    }

    /**
     * Allows me to pull out specific views from the row xml file for the ListView.   I can then
     * make any modifications I want to the ImageView and TextViews inside it.
     *
     * @param position    - The position of an item in the List received from my model.
     * @param convertView - list_row.xml as a View object.
     * @param parent      - The parent ViewGroup that holds the rows.  In this case, the ListView.
     ***/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));

            v = inflater.inflate(mResource, null);
        }

        /* We pull out the ImageView and TextViews so we can set their properties.*/
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        TextView nameView = (TextView) v.findViewById(R.id.name_text_view);
        TextView sizeView = (TextView) v.findViewById(R.id.size_text_view);
        File file = getItem(position);

        /* If the file is a dir, set the image view's image to a folder, else, a file. */
        if (file.isDirectory()) {
            iv.setImageResource(R.drawable.folder_grey);
        } else {
            iv.setImageResource(R.drawable.file);
        }

        long filesize = getFolderSize(file);
        sizeView.setText(readableFileSize(filesize));

        //Finally, set the name of the file or directory.
        nameView.setText(file.getName());

        //Send the view back so the ListView can show it as a row, the way we modified it.
        return v;
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
