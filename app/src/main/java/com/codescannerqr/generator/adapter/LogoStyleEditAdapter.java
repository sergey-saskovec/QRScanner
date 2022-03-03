package com.codescannerqr.generator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codescannerqr.generator.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class LogoStyleEditAdapter extends RecyclerView.Adapter<LogoStyleEditAdapter.ViewHolder>{

    private final List<Integer> items;
    private int selectedItem;
    private final String name;
    private static OnItemClickListener onItemClickListener;

    public LogoStyleEditAdapter(List<Integer> items, String name) {
        this.items = items;
        this.name = name;
        selectedItem = 0;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_logo_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.imageViewLogo.setImageResource(items.get(position));
        if (selectedItem == position) {
            holder.imageViewBackLogo.setVisibility(View.VISIBLE);
        }
        else{
            holder.imageViewBackLogo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewLogo;
        private final ImageView imageViewBackLogo;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageViewLogo = itemView.findViewById(R.id.imageViewLogo);
            imageViewBackLogo = itemView.findViewById(R.id.imageViewBackLogo);
            if (name.equals("BG")){
                imageViewBackLogo.setImageResource(R.drawable.ic_back_item_border);
            }
            else {
                imageViewBackLogo.setImageResource(R.drawable.ic_back_item_logo);
            }
            imageViewLogo.setOnClickListener(view -> {
                int position  = ViewHolder.super.getAdapterPosition();
                onItemClickListener.onItemClick(view,position);
                int previousItem = selectedItem;
                selectedItem = position;

                notifyItemChanged(previousItem);
                notifyItemChanged(position);

            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        LogoStyleEditAdapter.onItemClickListener = onItemClickListener;
    }
}
