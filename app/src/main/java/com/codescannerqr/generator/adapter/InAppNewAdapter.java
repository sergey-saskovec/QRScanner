package com.codescannerqr.generator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.model.HistoryItem;
import com.codescannerqr.generator.model.InAppNewModel;
import com.codescannerqr.generator.model.ResultList;

import java.util.List;

public class InAppNewAdapter extends RecyclerView.Adapter<InAppNewAdapter.ViewHolder>{

    private List<InAppNewModel> lists;
    private final Context context;
    private static OnItemClickListener onItemClickListener;

    public InAppNewAdapter(List<InAppNewModel> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_inapp_boarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageViewInAppNew.setImageResource(lists.get(position).getIconItem());
        if (lists.get(position).getTitleID() != 0){
            holder.textViewNameInAppNew.setText(lists.get(position).getTitleID());
            holder.textViewDescInAppNew.setText(lists.get(position).getDescID());
        }
        else{
            holder.textViewNameInAppNew.setTextSize(14);
            holder.textViewNameInAppNew.setTextColor(context.getColor(R.color.gray));
            holder.textViewNameInAppNew.setText(lists.get(position).getDescID());
            holder.textViewDescInAppNew.setVisibility(View.GONE);
            if (position == 5){
                SpannableString content = new SpannableString(context.getString(R.string.in_app_new_desc_6));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                holder.textViewNameInAppNew.setText(
                        content
                );
            }
            else{
                holder.textViewNameInAppNew.setText(Html.fromHtml(
                        lists.get(position).getDescID()
                ));
            }
        }

        holder.textViewNameInAppNew.setOnClickListener(v -> {
            onItemClickListener.onItemClick(
                    holder.textViewNameInAppNew,
                    position
            );
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<InAppNewModel> list){
        lists = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        InAppNewAdapter.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewInAppNew;
        private final TextView textViewNameInAppNew;
        private final TextView textViewDescInAppNew;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewInAppNew = itemView.findViewById(R.id.iconInAppNew);
            textViewNameInAppNew = itemView.findViewById(R.id.textViewTitleInAppNew);
            textViewDescInAppNew = itemView.findViewById(R.id.textViewDescInAppNew);
        }
    }
}
