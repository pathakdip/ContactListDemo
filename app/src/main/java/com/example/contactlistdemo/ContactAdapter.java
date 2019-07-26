package com.example.contactlistdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> contactModelArrayList;

    public ContactAdapter(Context context,ArrayList<String> contactList)
    {
        this.context=context;
        this.contactModelArrayList=contactList;

    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.contacts_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder viewHolder, int i) {
            viewHolder.txtContactDetails.setText(contactModelArrayList.get(i));
            Log.d("adapter","name: "+contactModelArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        Log.d("adapter","list size: "+contactModelArrayList.size());
        return contactModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContactDetails;
        public ViewHolder(View itemView) {
            super(itemView);
            txtContactDetails=itemView.findViewById(R.id.txtname);
        }
    }
}
