package com.cloupix.groupmail.business.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloupix.groupmail.R;
import com.cloupix.groupmail.business.Group;
import com.cloupix.groupmail.logic.GroupLogic;
import com.cloupix.groupmail.ui.activities.GroupDetailActivity;

import java.util.ArrayList;

/**
 * Created by alonsoapp on 24/07/16.
 *
 */
public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Group> groupList;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView textViewGroupName, textViewEmailPreview;
        ImageView imgViewSend;
        public ViewHolder(View v) {
            super(v);
            textViewGroupName = (TextView) v.findViewById(R.id.textViewGroupName);
            textViewEmailPreview = (TextView) v.findViewById(R.id.textViewEmailPreview);
            imgViewSend = (ImageView) v.findViewById(R.id.imgViewSend);

            v.setOnClickListener(this);
            imgViewSend.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.imageView){
                GroupLogic groupLogic = new GroupLogic();
                groupLogic.composeEmail(groupList.get(getAdapterPosition()).getContactList(), context);
            } else {
                Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.EXTRA_GROUP_ID, groupList.get(getAdapterPosition()).getGroupId());
                context.startActivity(intent);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GroupRecyclerViewAdapter(ArrayList<Group> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_group, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewGroupName.setText(groupList.get(position).getName());

        int color;
        String emailCountStr = "";
        if(groupList.get(position).getContactList().size()>0){
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                color = context.getResources().getColor(R.color.colorPrimaryDark, null);
            else
                color = context.getResources().getColor(R.color.colorPrimaryDark);
            Resources res = context.getResources();
            emailCountStr = res.getQuantityString(R.plurals.email_count,
                    groupList.get(position).getContactList().size(),
                    groupList.get(position).getContactList().size());

        }else{
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                color = context.getResources().getColor(R.color.neutral_grey, null);
            else
                color = context.getResources().getColor(R.color.neutral_grey);

            emailCountStr = context.getString(R.string.empty_group);
        }

        holder.textViewEmailPreview.setText(emailCountStr);
        holder.textViewEmailPreview.setTextColor(color);


        final int finalPosition = holder.getAdapterPosition();
        holder.imgViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupLogic groupLogic = new GroupLogic();
                groupLogic.composeEmail(groupList.get(finalPosition).getContactList(), context);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public void addGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

    public void addGroup(Group newGroup) {
        this.groupList.add(newGroup);
    }

    public void removeGroup(int position) {
        this.groupList.remove(position);
    }

}
