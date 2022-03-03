package com.codescannerqr.generator.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.Section;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainRecyclerViewAdapter extends
        RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private final List<Section> sectionList;
    private final Activity activity;

    public MainRecyclerViewAdapter(List<Section> sectionList, Activity activity) {
        this.sectionList = sectionList;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_row_generate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Section section = sectionList.get(position);

        if (section.getSectionName().equals("")) {
            holder.textViewNameSection.setVisibility(View.GONE);
        } else {
            holder.textViewNameSection.setText(section.getSectionName());
        }

        ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(
                section.getSectionList(),
                holder.recyclerViewSection,
                activity);
        holder.recyclerViewSection.setLayoutManager(
                new GridLayoutManager(
                        holder.itemView.getContext(),
                        3
                )
        );
        holder.recyclerViewSection.setAdapter(childRecyclerAdapter);

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNameSection;
        private final RecyclerView recyclerViewSection;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewNameSection = itemView.findViewById(R.id.textViewNameSection);
            recyclerViewSection = itemView.findViewById(R.id.recyclerViewSection);

        }
    }
}
