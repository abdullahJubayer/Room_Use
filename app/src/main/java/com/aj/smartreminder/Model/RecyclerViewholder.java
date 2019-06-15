package com.aj.smartreminder.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aj.smartreminder.R;

public class RecyclerViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title,latitude,longitude;
    private MyRecyclerItemClick myRecyclerItemClick;

    public RecyclerViewholder(@NonNull View itemView,MyRecyclerItemClick myRecyclerItemClick) {
        super(itemView);

        title=itemView.findViewById(R.id.recycler_title_id);
        latitude=itemView.findViewById(R.id.recycler_lat_id);
        longitude=itemView.findViewById(R.id.recycler_lng_id);
        this.myRecyclerItemClick=myRecyclerItemClick;

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        myRecyclerItemClick.Click(getAdapterPosition());
    }

    public interface MyRecyclerItemClick{
        void Click(int position);
    }
}
