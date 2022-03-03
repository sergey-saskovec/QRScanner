package com.codescannerqr.generator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.InAppNewModel;

import java.util.List;

public class AdapterInApp2 extends RecyclerView.Adapter<AdapterInApp2.ViewHolder>{

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_in_app_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewNameInAppNew2.setText(Config.listInAppNewFragment2.get(position));
        if (position == Config.listInAppNewFragment2.size() - 1){
            holder.view6.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Config.listInAppNewFragment2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameInAppNew2;
        private View view6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameInAppNew2 = itemView.findViewById(R.id.textViewItem);
            view6 = itemView.findViewById(R.id.view6);
        }
    }
}
