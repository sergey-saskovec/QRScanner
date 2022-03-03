package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.ResultButtons;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ButtonsRecyclerViewAdapter extends RecyclerView.Adapter<ButtonsRecyclerViewAdapter.ViewHolder> {

    private List<ResultButtons> items;
    private static OnItemClickListener onItemClickListener;

    public ButtonsRecyclerViewAdapter(List<ResultButtons> items) {
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_result_button_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textViewButton.setText(items.get(position).getNameButton());
        holder.imageViewLogo.setImageResource(items.get(position).getLogoButton());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewLogo;
        private final TextView textViewButton;
        private final CardView cardView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewResult0);
            imageViewLogo = itemView.findViewById(R.id.imageViewResult0);
            textViewButton = itemView.findViewById(R.id.textViewResult0);

            cardView.setOnClickListener(v -> onItemClickListener.onItemClick(
                    cardView,
                    textViewButton.getText().toString()
            ));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<ResultButtons> list){
        this.items = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String nameButtons);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        ButtonsRecyclerViewAdapter.onItemClickListener = onItemClickListener;
    }
}
