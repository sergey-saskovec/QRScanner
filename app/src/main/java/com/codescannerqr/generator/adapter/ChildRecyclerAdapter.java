package com.codescannerqr.generator.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.SectionList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ViewHolder> {

    private final List<SectionList> items;
    private final RecyclerView recyclerView;
    private final Activity activity;

    public ChildRecyclerAdapter(List<SectionList> items, RecyclerView recyclerView, Activity activity) {
        this.items = items;
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_row, parent, false);
        View.OnClickListener childRecyclerClick = new ChildRecyclerClick(recyclerView, items, activity);
        view.setOnClickListener(childRecyclerClick);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.itemTextView.setText(items.get(position).getName());
        holder.imageViewItem.setImageResource(items.get(position).getImage());
        holder.constraintItems.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), items.get(position).getColor()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemTextView;
        private final ImageView imageViewItem;
        private final ConstraintLayout constraintItems;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            itemTextView = itemView.findViewById(R.id.itemTextViewRow);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            constraintItems = itemView.findViewById(R.id.constraintItems);
        }
    }
}
