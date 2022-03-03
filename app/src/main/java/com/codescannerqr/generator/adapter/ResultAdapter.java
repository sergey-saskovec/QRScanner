package com.codescannerqr.generator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.ResultList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private final List<ResultList> lists;

    public ResultAdapter(List<ResultList> lists) {
        this.lists = lists;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_result_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textViewResultPlaceHolder.setText(lists.get(position).getKey());
        holder.textViewResultValue.setText(lists.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewResultPlaceHolder;
        private final TextView textViewResultValue;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewResultPlaceHolder = itemView.findViewById(R.id.textViewResultPlaceHolder);
            textViewResultValue = itemView.findViewById(R.id.textViewResultValue);
        }
    }
}
