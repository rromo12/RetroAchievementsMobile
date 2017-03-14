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

import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Romob on 3/3/2017.
 */

public class RecentlyPlayedAdapter extends RecyclerView.Adapter<RecentlyPlayedAdapter.RecentlyPlayedItemHolder> {
    final private ListItemClickListener mOnClickListener;
    private JSONArray recentlyPlayedData;
    List<Structures.Game> gamesList = new LinkedList<Structures.Game>();;
    String user;

    RecentlyPlayedAdapter(String jsonText,ListItemClickListener listener,String userName) throws JSONException {
        mOnClickListener = listener;
        user = userName;
        try {
            recentlyPlayedData = new JSONArray(jsonText);

        }
        catch(Exception e){
            e.printStackTrace();
        }


        for(int i =0;i<recentlyPlayedData.length();i++) {
            JSONObject current = recentlyPlayedData.getJSONObject(i);

            //If actually achievement in activity feed
            String gameID = current.getString("GameID");
            Structures.Game game = new Structures.Game();
            if (!gameID.equals("0")) {
                game.GameID = current.getInt("GameID");
                game.ConsoleID = current.getInt("ConsoleID");
                game.ConsoleName = current.getString("ConsoleName");
                game.Title = current.getString("Title");
                game.GameIcon = current.getString("ImageIcon");
                game.NumAchievements = current.getInt("NumPossibleAchievements");
                game.PossibleScore = current.getInt("PossibleScore");
                game.NumAchieved = current.getInt("NumAchieved");
                game.ScoreAchieved = current.getInt("ScoreAchieved");
                gamesList.add(game);
            }

        }
    }
    public interface ListItemClickListener {
        void onListItemClick(int gameId,String userName);

    }
    @Override
    public RecentlyPlayedItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        RecentlyPlayedItemHolder viewHolder = new RecentlyPlayedItemHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecentlyPlayedItemHolder holder, int position) {

        Structures.Game game = gamesList.get(position);
        //Set Title
        holder.mTitle.setText(game.Title + " (" + game.NumAchieved + "/" + game.NumAchievements + ")");
        //Set SubTitle
        holder.mSubTitle.setText(game.ConsoleName);
        //Set Icon
        holder.mIcon.setImageDrawable(null);
        URL image = NetworkUtils.buildGameIconURL(game.GameIcon);
        Picasso.with(holder.mIcon.getContext()).load(image.toString()).resize(96,96).placeholder( R.drawable.progress_animation ).into(holder.mIcon);
        //SET TAG GAME ID to KEY CONSTANT
        Structures.GameProgressTag tag = new Structures.GameProgressTag();
        tag.gameId = game.GameID;
        tag.user = user;
        holder.itemView.setTag(tag);

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }


    class RecentlyPlayedItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mSubTitle;
        ImageView mIcon;
        //TextView mPoints;

        public RecentlyPlayedItemHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_itemTitle);
            mSubTitle =(TextView) itemView.findViewById(R.id.tv_itemSubTitle);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_itemIcon);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Structures.GameProgressTag tag = (Structures.GameProgressTag) v.getTag();
            int gameId = tag.gameId;
            String userName = tag.user;
            mOnClickListener.onListItemClick(gameId,user);
        }

    }



}
