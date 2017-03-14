package com.rromo12.retroachivementsmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Romob on 3/12/2017.
 */

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Top10UserHolder> {
    private JSONArray top10Data;
    private List<Structures.top10User> userList = new LinkedList<>();
    final private ListItemClickListener mOnClickListener;

    Top10Adapter(String jsonText,ListItemClickListener listener) throws JSONException {
        mOnClickListener = listener;
        try {
            top10Data = new JSONArray(jsonText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0 ; i<top10Data.length();i++) {
            Structures.top10User user = new Structures.top10User();
            JSONObject current = top10Data.getJSONObject(i);
            user.Name = current.getString("1");
            user.points = current.getInt("2");
            user.truePoints = current.getInt("3");
            user.userPicUrl = NetworkUtils.buildUserPicURL(user.Name).toString();
            userList.add(user);
        }

    }
    public interface ListItemClickListener {
        void onListItemClick(String userName);

    }

    @Override
    public Top10UserHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new Top10UserHolder(view);
    }

    @Override
    public void onBindViewHolder(Top10UserHolder holder, int position) {
        Structures.top10User user = userList.get(position);
        holder.mTitle.setText(user.Name);
        holder.mSubTitle.setText(Integer.toString(user.points));
        Picasso.with(holder.mIcon.getContext()).load(user.userPicUrl).resize(96,96).placeholder( R.drawable.progress_animation ).into(holder.mIcon);
        holder.itemView.setTag(user.Name);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class Top10UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mSubTitle;
        ImageView mIcon;
        //TextView mPoints;

        Top10UserHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_itemTitle);
            mSubTitle = (TextView) itemView.findViewById(R.id.tv_itemSubTitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_itemIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String userName = (String) v.getTag();
            mOnClickListener.onListItemClick(userName);
        }

    }
}

