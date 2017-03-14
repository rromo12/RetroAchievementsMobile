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


public class MyGamesAdapter extends RecyclerView.Adapter<MyGamesAdapter.GameItemHolder> {
        final private ListItemClickListener mOnClickListener;
        private JSONArray myGamesData;
        List<Structures.Game> gamesList = new LinkedList<Structures.Game>();
        String user;

        MyGamesAdapter(String jsonText,ListItemClickListener listener,String userName) throws JSONException {
            mOnClickListener = listener;
            user =userName;
            try {
                myGamesData = new JSONArray(jsonText);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            //Remove Empty Game
            for(int i =0;i<myGamesData.length();i++){
                JSONObject current = myGamesData.getJSONObject(i);
                String gameID = current.getString("GameID");
                if (gameID.equals("0")){
                    myGamesData.remove(i);
                }
            }

            //Sort
            myGamesData = Utils.getSortedList(myGamesData);

            for(int i =0;i<myGamesData.length();i++) {
                JSONObject current = myGamesData.getJSONObject(i);
                Structures.Game game = new Structures.Game();
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
        public interface ListItemClickListener {
            void onListItemClick(int gameId,String userName);

        }
        @Override
        public GameItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;
            View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
            return new GameItemHolder(view);

        }

        @Override
        public void onBindViewHolder(GameItemHolder holder, int position) {

            Structures.Game game = gamesList.get(position);
            //Set Title
            holder.mTitle.setText(game.Title + " (" + game.NumAchieved + "/" + game.NumAchievements + ")");
            //Set SubTitle
            holder.mSubTitle.setText(game.ConsoleName);
            //Set Icon
            URL image = NetworkUtils.buildGameIconURL(game.GameIcon);
            Picasso.with(holder.mIcon.getContext()).load(image.toString()).resize(96,96).placeholder( R.drawable.progress_animation ).into(holder.mIcon);
            Structures.GameProgressTag  tag = new Structures.GameProgressTag();
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


        class GameItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView mTitle;
            TextView mSubTitle;
            ImageView mIcon;
            //TextView mPoints;

            public GameItemHolder(View itemView) {
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
                mOnClickListener.onListItemClick(gameId,userName);
            }

        }

}





