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


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Reference:
// https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String myid = ListAdapter.class.getName();

    private ArrayList<String> data;
    private LayoutInflater inflater;
    private ItemClickedListener itemClickedListener;

    public ListAdapter(Context context, ArrayList<String> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // Put the item into the view for display
        String itemValue = data.get(position);
        itemValue = itemValue.replaceAll("Systolic", "<b>Sysolic</b>")
                .replaceAll("Diastolic", "<b>Diastolic</b>");
        holder.textView.setText(Html.fromHtml(itemValue));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    public interface ItemClickedListener {
        void onItemClicked(View view, int selected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout itemLayout;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            // Hook up a listener so that a response can be made when this item is clicked.
            itemLayout.setOnClickListener(this);
            // Within each itemView, find the view that is used for displaying data item
            textView = itemView.findViewById(R.id.textView);
        }
        @Override
        public void onClick(View view) {
            if (itemClickedListener!=null)
                itemClickedListener.onItemClicked(view, getAdapterPosition());
        }
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
