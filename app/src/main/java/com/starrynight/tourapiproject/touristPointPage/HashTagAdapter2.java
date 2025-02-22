package com.starrynight.tourapiproject.touristPointPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import java.util.ArrayList;
import java.util.List;

public class HashTagAdapter2 extends RecyclerView.Adapter<HashTagAdapter2.ViewHolder> {
    private static List<String> items;

    public HashTagAdapter2(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HashTagAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //viewHolder 처음 만드는 함수
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.hashtags_empty2, viewGroup, false);

        return new HashTagAdapter2.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HashTagAdapter2.ViewHolder viewHolder, int position) {
        //viewHolder 재사용 하는 함수
        String item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public void addItem(String item) {
        items.add(item);
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, String item) {
        items.set(position, item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hashTagName;

        public ViewHolder(View itemView) {
            super(itemView);
            hashTagName = itemView.findViewById(R.id.recycler_hashTagName);
        }

        public void setItem(String item) {
            hashTagName.setText("#" + item);
        }
    }

}
