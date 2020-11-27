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

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BloodPressureFragment extends Fragment {

    private static final String myId = BloodPressureFragment.class.getName();
    private DataManagerBloodPressure dataManager;
    private int choice;
    private RecyclerView recyclerView;
    private EditText systolicEdit;
    private EditText diastolicEdit;
    private ImageButton saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dataManager = new DataManagerBloodPressure(getActivity());
        setRetainInstance(true);
    }

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_bloodpressure, container, false);

        // Create some dummy data.
        // TODO: change the choice, compile and test again.
        createSomeDummyData();
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Spinner choiceSpinner = root.findViewById(R.id.choiceGroup);
        choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                choice = position;
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Hook up input widgets

        systolicEdit = root.findViewById(R.id.bloodPressureSystolic);
        diastolicEdit = root.findViewById(R.id.bloodPressureDiastolic);
        saveButton = root.findViewById(R.id.saveButton);
        systolicEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        diastolicEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                long systolic = Long.parseLong(systolicEdit.getText().toString());
                long diastolic = Long.parseLong(diastolicEdit.getText().toString());
                long date = System.currentTimeMillis();
                dataManager.insert(date, systolic, diastolic);
                updateList();
                clear();

            }

        });

        clear();
        return root;
    }

    void validate() {

        if (systolicEdit.getText() != null && systolicEdit.getText().length() > 0
                && diastolicEdit.getText() != null && diastolicEdit.getText().length() > 0)
            saveButton.setEnabled(true);
        else
            saveButton.setEnabled(false);
    }

    void clear() {

        systolicEdit.setText("");
        diastolicEdit.setText("");
        saveButton.setEnabled(false);
    }

    private void updateList() {
        ListAdapter listAdapter = new ListAdapter(getActivity(), convert(search()));
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> convert(ArrayList<String[]> data) {
        ArrayList<String> lines = new ArrayList<>();
        for (String[] fields : data)
            lines.add(fields[0] + "<br>" + "Systolic: " +  fields[1] +
                    "<br>" + "Diastolic: " +  fields[2]);

        return lines;
    }

    public void createSomeDummyData() {

        dataManager.insert(System.currentTimeMillis(), 67, 121);
        dataManager.insert(System.currentTimeMillis(), 102, 142);
        dataManager.insert(System.currentTimeMillis(), 79, 149);
        dataManager.insert(System.currentTimeMillis(), 74, 161);
        dataManager.insert(System.currentTimeMillis(), 81, 181);
        dataManager.insert(System.currentTimeMillis(), 83, 109);
        dataManager.insert(System.currentTimeMillis(), 45, 124);
        dataManager.insert(System.currentTimeMillis(), 43, 130);
        dataManager.insert(System.currentTimeMillis(), 87, 172);
    }

    public ArrayList<String[]> search() {
        Cursor cursor;
        if (choice == 1)
            cursor = dataManager.selectLastK(5);
        else if (choice == 2)
            cursor = dataManager.selectLastK(10);
        else if (choice == 3)
            cursor = dataManager.selectLastK(30);
        else if (choice == 4)
            cursor = dataManager.selectAverage();
        else if (choice == 5)
            cursor = dataManager.selectAverageLastK(5);
        else if (choice == 6)
            cursor = dataManager.selectAverageLastK(10);
        else if (choice == 7)
            cursor = dataManager.selectAverageLastK(30);
        else
            cursor = dataManager.selectAll();

        return dataManager.getData(cursor);

    }

}