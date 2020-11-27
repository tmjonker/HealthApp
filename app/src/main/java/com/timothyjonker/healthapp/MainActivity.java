/*
 * Copyright (c) 2020. Timothy Jonker @ NVCC
 *
 * Author: Timothy Jonker
 * Affiliation: NVCC
 *
 * Terms of Use:
 * This application is part of the term projects of the course ITP226 of Fall 2020.  It is not to released to any third party, whether with or without the permission of the author.  Any unauthorized use of this application may be subject to prosecution.
 */

package com.timothyjonker.healthapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentHolder);

        if (fragment == null) {
            fragment = new BloodPressureFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentHolder, fragment).commit();
        }
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.licenses) {
            new AlertDialog.Builder(this)
                    .setTitle("Licenses")
                    .setMessage(readLicenses())
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                // Nothing to be done
                                }
                            }).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String readLicenses() {

        String data = "";

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.eula);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null) {
                data += line + "\n";
                line = reader.readLine();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}