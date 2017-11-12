package com.zombietechinc.rovingrepairsadmin;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 9/1/2017.
 */

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.PartViewHolder>{


    public static class PartViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView nameTV;
        TextView priceTV;


        PartViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.jobRV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            priceTV = (TextView) itemView.findViewById(R.id.priceTV);

        }
    }
    List<Part> parts;
    private LayoutInflater inflater;
    Part part;
    String partType;


    PartAdapter(Context context, List<Part> parts){
        inflater = LayoutInflater.from(context);
        this.parts = parts;
        part = new Part();
    }
    @Override
    public PartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.job_card, parent, false);
        PartViewHolder holder = new PartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PartViewHolder holder, int position) {
        part = parts.get(position);
        partType = part.getName();

        holder.nameTV.setText(partType);
        holder.priceTV.setText(String.valueOf(part.getPrice()));

    }

    @Override
    public int getItemCount() {
        return parts.size();
    }


}