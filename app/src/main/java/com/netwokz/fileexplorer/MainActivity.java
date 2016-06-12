package com.netwokz.fileexplorer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toolbar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by Steve-O on 6/11/2016.
 */
public class MainActivity extends Activity {
    private UiView mView;
    private static final String PREF_IS_FIRST_RUN = "firstRun";
    private SharedPreferences prefs;
    private static final String[] PERMS = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    private static final int RESULT_PERMS_INITIAL = 1339;
    private String FRAGMENT_TAG = "listView";
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (isFirstRun() || !canSeeSDCard()) {
            requestPermissions(PERMS, RESULT_PERMS_INITIAL);
        } else {
            ListFragment listFragment = new UiView();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, listFragment, FRAGMENT_TAG);
            ft.commit();
            mView = (UiView) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }
    }

    private boolean isFirstRun() {
        boolean result = prefs.getBoolean(PREF_IS_FIRST_RUN, true);
        if (result) {
            prefs.edit().putBoolean(PREF_IS_FIRST_RUN, false).apply();
        }
        return result;
    }

    private boolean useRuntimePermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean hasPermission(String perm) {
        if (useRuntimePermissions()) {
            return (checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED);
        }
        return (true);
    }

    private boolean canSeeSDCard() {
        return (hasPermission(READ_EXTERNAL_STORAGE) && hasPermission(WRITE_EXTERNAL_STORAGE));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RESULT_PERMS_INITIAL) {
            if (canSeeSDCard()) {
                ListFragment listFragment = new UiView();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, listFragment, FRAGMENT_TAG);
                ft.commit();
                mView = (UiView) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            }
        }
    }
}

