package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.SectionSettings;
import com.codescannerqr.generator.view.fragments.bottomNavigate.SettingsBottomFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsMainRecyclerViewAdapter extends
        RecyclerView.Adapter<SettingsMainRecyclerViewAdapter.ViewHolder> {

    List<SectionSettings> sectionSettingsList;
    private final Activity activity;
    private final SettingsBottomFragment fragment;

    public SettingsMainRecyclerViewAdapter(List<SectionSettings> sectionSettingsList,
                                           Activity activity,
                                           SettingsBottomFragment fragment) {
        this.sectionSettingsList = sectionSettingsList;
        this.activity = activity;
        this.fragment = fragment;
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
        holder.textViewNameSection.setText(sectionSettingsList.get(position).getSectionName());
        SettingsChildRecyclerViewAdapter adapter = new SettingsChildRecyclerViewAdapter(
                sectionSettingsList.get(position).getSectionList(),
                holder.recyclerViewSection,
                activity,
                fragment
        );
        holder.recyclerViewSection.setLayoutManager(new LinearLayoutManager(activity));
        holder.recyclerViewSection.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<SectionSettings> listNew){
        sectionSettingsList = listNew;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sectionSettingsList.size();
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
