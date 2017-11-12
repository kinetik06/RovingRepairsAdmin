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

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderViewHolder>{


    public static class WorkOrderViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView nameTV;
        TextView priceTV;


        WorkOrderViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.jobRV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            priceTV = (TextView) itemView.findViewById(R.id.priceTV);

        }
    }
    List<Job> jobs;
    private LayoutInflater inflater;
    Job job;
    String jobType;


    WorkOrderAdapter(Context context, List<Job> jobs){
        inflater = LayoutInflater.from(context);
        this.jobs = jobs;
        job = new Job();
    }
    @Override
    public WorkOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.job_card, parent, false);
        WorkOrderViewHolder holder = new WorkOrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkOrderViewHolder holder, int position) {
        job = jobs.get(position);
        jobType = job.getName();

        holder.nameTV.setText(jobType);
        holder.priceTV.setText(String.valueOf(job.getPrice()));

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }


}