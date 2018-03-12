package com.example.hp.memomanagerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 11/03/2018.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyViewHolder> {
//TODO: Memo Adapter
    private ArrayList<Memo> memoList;
    public final static int VIEWMEMODETAILS_CODE=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, deadline, preview, status;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_itemTitle);
            deadline = (TextView) view.findViewById(R.id.tv_itemDate);
            //TODO: limit note to a number of characters
            preview = (TextView) view.findViewById(R.id.tv_itemPreview);
            status = (TextView) view.findViewById(R.id.tv_itemStatus);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: on item Click
                    //gets item position
                    int itemClicked=getAdapterPosition();
                    Log.d("MyViewHolder","Clicked Item Number:"+itemClicked);
                    Intent viewMemoDetails = new Intent(view.getContext(), ViewMemoDetails.class);
                    Memo memoItem = memoList.get(itemClicked);
                    viewMemoDetails.putExtra("title",memoItem.getTitle());
                    viewMemoDetails.putExtra("category",memoItem.getCategory());
                    viewMemoDetails.putExtra("deadline",memoItem.getDeadline());
                    viewMemoDetails.putExtra("note",memoItem.getNote());
                    viewMemoDetails.putExtra("intervals",memoItem.getNotificationIntervals());
                    viewMemoDetails.putExtra("time",memoItem.getNotificationTime());
                    viewMemoDetails.putExtra("level",memoItem.getPriorityLevel());
                    viewMemoDetails.putExtra("status",memoItem.getStatus());

                    //viewMemoDetails.putExtra("itemClicked",itemClicked);
                    //ArrayList m=getMemoList();
                    //viewMemoDetails.putParcelableArrayListExtra("memoList", m);
                    view.getContext().startActivity(viewMemoDetails);
                }
            });
        }
    }



    public MemoAdapter(ArrayList<Memo> memoList) {
        this.memoList = memoList;
    }
    //TODO: addEditButton method here
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memo, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Memo memo = memoList.get(position);
        holder.title.setText(memo.getTitle());
        holder.deadline.setText(memo.getDeadline());
        //TODO: limit the note to a number of characters
        holder.preview.setText(String.valueOf(memo.getNote()));
        holder.status.setText(String.valueOf(memo.getStatus()));
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public ArrayList<Memo> getMemoList() {
        return memoList;
    }
}
