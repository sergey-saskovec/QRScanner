package com.codescannerqr.generator.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.helpers.SettingsHelpers;
import com.codescannerqr.generator.model.SectionSettingsList;
import com.codescannerqr.generator.view.fragments.bottomNavigate.SettingsBottomFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsChildRecyclerViewAdapter extends
        RecyclerView.Adapter<SettingsChildRecyclerViewAdapter.ViewHolder> {

    private final List<SectionSettingsList> items;
    private final RecyclerView recyclerView;
    private final Activity activity;
    private final SettingsBottomFragment fragment;

    public SettingsChildRecyclerViewAdapter(List<SectionSettingsList> items,
                                            RecyclerView recyclerView,
                                            Activity activity,
                                            SettingsBottomFragment fragment) {
        this.items = items;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_row_settings, parent, false);
        View.OnClickListener childRecyclerClick = new SettingsChildClick(recyclerView, items, activity, fragment);
        view.setOnClickListener(childRecyclerClick);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SectionSettingsList sectionSettingsList = items.get(position);
        if (sectionSettingsList.isVisibleImage()) {
            holder.imageViewRightSettings.setVisibility(View.VISIBLE);
            holder.switchItemSettings.setVisibility(View.GONE);
            holder.imageViewRightSettings.setImageResource(sectionSettingsList.getImage());

        } else if (sectionSettingsList.isVisibleSwitch()) {
            holder.imageViewRightSettings.setVisibility(View.GONE);
            holder.switchItemSettings.setVisibility(View.VISIBLE);
            holder.switchItemSettings.setChecked(sectionSettingsList.isSwitchBools());
        } else {
            holder.imageViewRightSettings.setVisibility(View.GONE);
            holder.switchItemSettings.setVisibility(View.GONE);
        }
        holder.imageViewLogoSettings.setImageResource(sectionSettingsList.getLogo());
        holder.textViewNameSettings.setText(sectionSettingsList.getName());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageViewLogoSettings;
        private final TextView textViewNameSettings;
        private final SwitchCompat switchItemSettings;
        private final ImageView imageViewRightSettings;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageViewLogoSettings = itemView.findViewById(R.id.imageViewLogoSettings);
            textViewNameSettings = itemView.findViewById(R.id.textViewNameSettings);
            switchItemSettings = itemView.findViewById(R.id.switchItemSettings);
            imageViewRightSettings = itemView.findViewById(R.id.imageViewRightSettings);
            switchItemSettings.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = recyclerView.getChildAdapterPosition(itemView);
            Config.listSectionTwoItemsSettings.get(pos).setSwitchBools(
                    switchItemSettings.isChecked()
            );
            switch (pos) {
                case 0:
                    Config.settingsVibration = switchItemSettings.isChecked();
                    break;
                case 1:
                    Config.settingsSound = switchItemSettings.isChecked();
                    break;
                case 2:
                    Config.settingsAutoCopy = switchItemSettings.isChecked();
                    break;
                case 3:
                    Config.settingsAutoSearch = switchItemSettings.isChecked();
                    break;
                case 4:
                    Config.settingsSaveHistory = switchItemSettings.isChecked();
                    break;
            }
            SettingsHelpers.saveSettings(activity);
        }
    }
}
