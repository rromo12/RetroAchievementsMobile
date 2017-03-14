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
 * Created by Romob on 3/11/2017.
 */

public class RecentAchievementsAdapter extends RecyclerView.Adapter<RecentAchievementsAdapter.FeedItemHolder> {
    //For Use In Main Activity
    private JSONArray feed;
    private List<Structures.FeedItem> feedItems = new LinkedList<>();
    String userName;



    RecentAchievementsAdapter(String jsonText) throws JSONException {
        try {
            feed = new JSONArray(jsonText);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        for(int i =0;i<feed.length();i++) {
            JSONObject current = feed.getJSONObject(i);
            Structures.FeedItem feedItem = new Structures.FeedItem();
            //If actually achievement in activity feed
            feedItem.ActivityType = current.getInt("activitytype");
            switch(feedItem.ActivityType){
                case 1:
                    //Achievement Earned
                    feedItem.GameID = current.getInt("GameID");
                    feedItem.User = current.getString("User");
                    feedItem.UserIconUrl = NetworkUtils.buildUserPicURL(feedItem.User).toString();
                    feedItem.AchievementID = current.getInt("ID");
                    feedItem.AchievementPoints = current.getInt("AchPoints");
                    feedItem.GameTitle = current.getString("GameTitle");
                    feedItem.GameConsole = current.getString("ConsoleName");
                    feedItem.GameIconUrl = NetworkUtils.buildGameIconURL(current.getString("GameIcon")).toString();
                    feedItem.AchievementTitle = current.getString("AchTitle");
                    feedItem.AchievementDescription = current.getString("AchDesc");
                    feedItem.AchievementBadgeUrl = NetworkUtils.buildAchievementBadgeURL(current.getString("AchBadge")).toString();
                    feedItem.TimeStamp = current.getString("timestamp");
                    feedItems.add(feedItem);
                    break;
                default:
                    break;
            }


        }

    }

    @Override
    public FeedItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,viewGroup, false);
        return new FeedItemHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedItemHolder holder, int position) {
        Structures.FeedItem feedItem = feedItems.get(position);
        String feedItemText;
        switch(feedItem.ActivityType){
            case 1:
                //Achievement Earned
                //TODO Replace User with YOU when looking at your own feed;
                //(" + feedItem.AchievementPoints + ")
                feedItemText = feedItem.User  + " earned " + feedItem.AchievementTitle + " in " + feedItem.GameTitle + " (" + feedItem.GameConsole + ")";
                holder.mTitle.setText(feedItemText);
                //Set SubTitle
                holder.mSubTitle.setText(feedItem.AchievementDescription);
                //Set Icon
                Picasso.with(holder.mIcon.getContext()).load(feedItem.AchievementBadgeUrl).placeholder( R.drawable.progress_animation ).into(holder.mIcon);
                break;
            case 2:
                //User Logged
                feedItemText = feedItem.User + " logged in";
                holder.mTitle.setText(feedItemText);
                holder.mSubTitle.setText("");
                Picasso.with(holder.mIcon.getContext()).load(feedItem.UserIconUrl).resize(64,64).into(holder.mIcon);
                break;
            case 3:
                feedItemText = feedItem.User + " started playing " + feedItem.GameTitle + " (" + feedItem.GameConsole +")";
                holder.mTitle.setText(feedItemText);
                holder.mSubTitle.setText("");
                Picasso.with(holder.mIcon.getContext()).load(feedItem.GameIconUrl).resize(64,64).into(holder.mIcon);
                break;
            default:


        }
        //Set Title


        //Set Tag to AchievementID
        //holder.setTag(1,ach.AchievementID);



    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }


    class FeedItemHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mSubTitle;
        ImageView mIcon;
        //TextView mPoints;

        FeedItemHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_itemTitle);
            mSubTitle =(TextView) itemView.findViewById(R.id.tv_itemSubTitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_itemIcon);
        }
    }

}
