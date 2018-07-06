package com.example.user.ocv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import java.io.File;

/**
 * Created by user on 29/10/17.
 */

public class MyPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }
    public static class MyPreferenceFragment extends PreferenceFragment {
        String storagedir = Environment.getExternalStorageDirectory().toString();
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference pLcl = getPreferenceScreen().findPreference("clearCache");
            pLcl.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // handle click here
                    File normalnote = new File(storagedir + "/img.txt");
                    normalnote.delete();
                    File imgnote = new File(storagedir + "/img.jpg");
                    imgnote.delete();
                    System.exit(0);
                    return true;
                }

            });
            Preference pLcl2 = getPreferenceScreen().findPreference("rebuild");
            pLcl2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                // handle click here
                ///Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                ///startActivityForResult(intent, READ_REQUEST_CODE);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                ///intent.setType("file/*");
                //startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                    return true;
            }


        });

        ///public void performFileSearch() {
            ///Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            //startActivityForResult(intent, READ_REQUEST_CODE);
        //}

        ///protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            ///String Fpath = data.getDataString();
            //TODO handle your request here
            ///super.onActivityResult(requestCode, resultCode, data);
    ///}
       }
    }
}