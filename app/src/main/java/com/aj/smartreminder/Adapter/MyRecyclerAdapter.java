package com.aj.smartreminder.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aj.smartreminder.Model.LocationDataModel;
import com.aj.smartreminder.Model.RecyclerViewholder;
import com.aj.smartreminder.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewholder>{

    private Context context;
    private List<LocationDataModel> locationData=new ArrayList<>();
    private RecyclerViewholder.MyRecyclerItemClick myRecyclerItemClick;

    public MyRecyclerAdapter(Context context, RecyclerViewholder.MyRecyclerItemClick myRecyclerItemClick){
        this.context=context;
        this.myRecyclerItemClick=myRecyclerItemClick;
    }

    @NonNull
    @Override
    public RecyclerViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_item,viewGroup,false);
        return new RecyclerViewholder(v,myRecyclerItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewholder recyclerViewholder, int i) {
        LocationDataModel dataModel=locationData.get(i);
        recyclerViewholder.title.setText(String.valueOf(dataModel.getTitle()));
        recyclerViewholder.latitude.setText(String.valueOf(dataModel.getLatitude()));
        recyclerViewholder.longitude.setText(String.valueOf(dataModel.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return locationData.size();
    }

    public void setData(List<LocationDataModel> locationData){
        this.locationData=locationData;
        notifyDataSetChanged();
    }

    public LocationDataModel getSingleData(int position){
         return locationData.get(position);
    }
}
