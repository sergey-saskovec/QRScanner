package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codescannerqr.generator.Config;
import com.codescannerqr.generator.R;
import com.codescannerqr.generator.adapter.recyclerAds.NativeAdManager;
import com.codescannerqr.generator.helpers.BitmapHelpers;
import com.codescannerqr.generator.helpers.ListHistoryHelpers;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.view.activity.MainActivity;
import com.codescannerqr.generator.view.fragments.historyCodes.ItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryRecyclerViewAdapter2 extends RecyclerView.Adapter<HistoryRecyclerViewAdapter2.ViewHolder> {

    private List<HistoryItem> items;
    private final RecyclerView recyclerView;
    private final Activity activity;
    private final String key;
    private final ItemViewModel viewModel;
    private static HistoryRecyclerViewAdapter2.OnItemClickListener onItemClickListener;

    public HistoryRecyclerViewAdapter2(List<HistoryItem> items, RecyclerView recyclerView, Activity activity, String key, ItemViewModel viewModel) {
        this.items = items;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.key = key;
        this.viewModel = viewModel;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.inflate(R.layout.item_history_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (position == 0){
            NativeAdManager.loadNative(Config.nativeID, activity,
                    holder.adContainer);
            holder.adContainer.setVisibility(View.VISIBLE);
        }
        else{
            holder.adContainer.setVisibility(View.GONE);
        }

        holder.textViewTitleHistory.setText(items.get(position).getTitleHistory());
        holder.textViewUrlHistory.setText(items.get(position).getUrlHistory());
        holder.imageViewQrCodeHistory.setImageBitmap(
                BitmapHelpers.decodeByteToBitmap(items.get(position).getArrayQRCode())
        );
        holder.imageViewIconHistory.setImageResource(items.get(position).getIconHistory());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<HistoryItem> list){
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            PopupMenu.OnMenuItemClickListener {

        private final ImageView imageViewQrCodeHistory;
        private final ImageView imageViewIconHistory;
        private final TextView textViewTitleHistory;
        private final TextView textViewUrlHistory;
        LinearLayout adContainer;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewQrCodeHistory = itemView.findViewById(R.id.imageViewQrCodeHistory);
            imageViewIconHistory = itemView.findViewById(R.id.imageViewIconHistory);
            textViewTitleHistory = itemView.findViewById(R.id.textViewTitleHistory);
            textViewUrlHistory = itemView.findViewById(R.id.textViewUrlHistory);
            ImageButton imageViewThreeDots = itemView.findViewById(R.id.imageViewThreeDots);
            imageViewThreeDots.setOnClickListener(this);
            itemView.setOnClickListener(this);
            adContainer = itemView.findViewById(R.id.native_ad_container);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageViewThreeDots) {
                showPopUpMenu(v);
            } else {
                navResultFragment(v);
            }

        }

        private void showPopUpMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            if (key.equals("list_bookmark")){
                popupMenu.inflate(R.menu.popup_history_menu_bookmark);
            }
            else {
                popupMenu.inflate(R.menu.popup_history_menu_default);
            }
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.item_add_bookmark:
                    addToBookmark(getRealPosition(getAdapterPosition()));
                    return true;
                case R.id.item_delete:
                    itemDeleteList(getRealPosition(getAdapterPosition()));
                    return true;
                case R.id.item_preview:
                    setPreview(getRealPosition(getAdapterPosition()));
                    return true;
                default:
                    return false;
            }
        }
    }

    private int getRealPosition(int position){
        return position;

    }

    private void setPreview(int adapterPosition) {
        Bundle bundle = new Bundle();
        Bitmap bitmap = BitmapHelpers.decodeByteToBitmap(items.get(adapterPosition).getArrayQRCode());
        bundle.putParcelable("imageBitmap", bitmap);

        ((MainActivity)activity).navController.navigate(
                R.id.action_navigation_history_to_stylePreviewFragment ,bundle);
    }

    private void navResultFragment(View view) {
        int position = getRealPosition(recyclerView.getChildAdapterPosition(view));
        HistoryItem historyItem = items.get(position);
        onItemClickListener.onItemClick(view,position, historyItem);
    }

    private void addToBookmark(int position) {
        ListHistoryHelpers.setListHistoryCreateAddItem(
                activity,
                items.get(position).getTitleHistory(),
                items.get(position).getUrlHistory(),
                items.get(position).getIconHistory(),
                BitmapHelpers.decodeByteToBitmap(items.get(position).getArrayQRCode()),
                "list_bookmark");
        viewModel.selectItem(ListHistoryHelpers.getListHistoryCreate(activity, "list_bookmark"));
    }

    private void itemDeleteList(int adapterPosition) {
        items.remove(adapterPosition);
        ListHistoryHelpers.setListHistoryCreateAddList(activity, items, key);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, HistoryItem historyItem);
    }

    public void setOnItemClickListener(HistoryRecyclerViewAdapter2.OnItemClickListener onItemClickListener){
        HistoryRecyclerViewAdapter2.onItemClickListener = onItemClickListener;
    }
}
