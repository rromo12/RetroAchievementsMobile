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

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Romob on 3/6/2017.
 */

public class GameProgressAdapter  extends RecyclerView.Adapter<GameProgressAdapter.AchievementViewHolder>{


    private JSONObject gameAchievementsData;
    private  List<Structures.Achievement> achievementsList = new LinkedList<Structures.Achievement>();;


    GameProgressAdapter(JSONObject gameAchievementsData) throws JSONException {

        JSONObject achievementsObject = gameAchievementsData.getJSONObject("Achievements");//.getJSONArray(0);
        //String GameTitle = gameAchievementsData.getString("Title");


        //Convert to a Json Array
        Iterator x = achievementsObject.keys();
        JSONArray achievements = new JSONArray();

        while (x.hasNext()){
            String key = (String) x.next();
            achievements.put(achievementsObject.get(key));
        }



        for(int i =0;i<achievements.length();i++) {
            JSONObject current = achievements.getJSONObject(i);
            Structures.Achievement ach = new Structures.Achievement();
            //If actually achievement in activity feed
            //String gameID = current.getString("GameID");
            ach.AchievementID = current.getInt("ID");
            ach.Points = current.getInt("Points");
            if(current.has("DateEarned") ||current.has("DateEarnedHardCore") ){
                ach.IsAwarded = true;
                String date;
                if(current.has("DateEarned")){
                    date = current.getString("DateEarned");
                    ach.DateAwarded = Utils.stringtoDate(date);
                }
                if(current.has("DateEarnedHardCore")){
                    date = current.getString("DateEarnedHardCore");
                    ach.DateAwardedHardCore = Utils.stringtoDate(date);
                }



            }
            else{
                ach.IsAwarded = false;
            }


            ach.Achievement = current.getString("Title");
            ach.Description = current.getString("Description");
            ach.Badge = current.getString("BadgeName");
            achievementsList.add(ach);

        }

        if(achievements.length()==0){
            //Set Text View To Let User Know No Achievements Exist for this Game;

        }
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        AchievementViewHolder viewHolder = new AchievementViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {
        Structures.Achievement achievement = achievementsList.get(position);
        //Set Title
        holder.mTitle.setText(achievement.Achievement);
        //Set SubTitle
        holder.mSubTitle.setText(achievement.Description);
        //Set Icon
        holder.mIcon.setImageDrawable(null);
        //if ach.isAwarded
        URL image;
        if(achievement.IsAwarded){
             image = NetworkUtils.buildAchievementBadgeURL(achievement.Badge);
        }else{
             image = NetworkUtils.buildAchievementBadgeLockURL(achievement.Badge);
        }

        Picasso.with(holder.mIcon.getContext()).load(image.toString()).placeholder( R.drawable.progress_animation ).into(holder.mIcon);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return achievementsList.size();
    }


    class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mSubTitle;
        ImageView mIcon;
        //TextView mPoints;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_itemTitle);
            mSubTitle =(TextView) itemView.findViewById(R.id.tv_itemSubTitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_itemIcon);
        }


    }
}
