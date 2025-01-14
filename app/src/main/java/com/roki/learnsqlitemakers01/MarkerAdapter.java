package com.roki.learnsqlitemakers01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder> {

    private Context context;
    private List<MarkerModel> markerList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MarkerModel marker);
        void onDeleteClick(MarkerModel marker);
    }

    public MarkerAdapter(Context context, List<MarkerModel> markerList, OnItemClickListener listener) {
        this.context = context;
        this.markerList = markerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MarkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.marker_item, parent, false);
        return new MarkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerViewHolder holder, int position) {
        MarkerModel marker = markerList.get(position);
        holder.textViewName.setText(marker.getName());
        holder.textViewAddress.setText(marker.getAddress());
        holder.textViewLatLng.setText(marker.getLatitude() + ", " + marker.getLongitude());

        // Click listener for updating the marker
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(marker);
            }
        });

        // Click listener for deleting the marker
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(marker);
            }
        });
    }

    @Override
    public int getItemCount() {
        return markerList.size();
    }

    public static class MarkerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewLatLng;
        ImageView iconDelete;

        public MarkerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewLatLng = itemView.findViewById(R.id.textViewLatLng);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}
