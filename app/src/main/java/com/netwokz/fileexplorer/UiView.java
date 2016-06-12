package com.netwokz.fileexplorer;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Steve-O on 6/11/2016.
 */
public class UiView extends ListFragment {
    //This is a passive view, so my presenter handles all of the updating, etc.
    private Presenter presenter;
    private boolean doubleBackToExitPressedOnce = false;

    public void setPresenter(Presenter p) {
        presenter = p;
        setHasOptionsMenu(true);
    }


    //Return the view to the Activity for display.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listfragment_main, container, false);
    }

    //This is a good place to do final initialization as the Fragment is finished initializing itself.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenter(new Presenter(this));
    }

    //When we intercept a click, call through to the appropriate method in the presenter.
    @Override
    public void onListItemClick(ListView listView, android.view.View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        presenter.listItemClicked(listView, view, position, id);
    }

    /* Populate options menu and or action bar with menu from res/menu/menu_main.xml*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    //Called when an item in the menu, or the home button (if enabled) is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                presenter.homePressed();
                break;
            case R.id.settings:
                presenter.settings();
        }
        return super.onOptionsItemSelected(item);
    }

    // My custom toast
    public void toast() {
        Context context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customToastroot = inflater.inflate(R.layout.custom_toast, null);
        Toast customtoast = new Toast(context);
        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.BOTTOM, 0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button
                    Log.d("UiView", "Back Pressed!");
                    if (presenter.hasParent())
                        presenter.backPressed();
                    else {
                        if (doubleBackToExitPressedOnce) {
                            getActivity().finish();
                        }

                        doubleBackToExitPressedOnce = true;
                        toast();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}