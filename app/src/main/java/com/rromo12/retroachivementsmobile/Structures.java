package com.rromo12.retroachivementsmobile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class Structures {
      static class Achievement {
        String GameTitle;
        String GameIcon;
        String Achievement;
        String Description;
        String Badge;
        Integer GameID;
        Integer Points;
        Integer AchievementID;
        Boolean IsAwarded;
        Date DateAwarded;
        Date DateAwardedHardCore;

        Achievement(JSONObject current) throws JSONException {
            //if user field in object then its from the feed
            /* Feed Structure
            {
				"ID": "4459891",
				"timestamp": "1488248878",
				"activitytype": "2",
				"User": "rromo12",
				"RAPoints": "17",
				"Motto": "",
				"data": null,
				"data2": null,
				"GameTitle": null,
				"GameID": null,
				"GameIcon": null,
				"ConsoleName": null,
				"AchTitle": null,
				"AchDesc": null,
				"AchPoints": null,
				"AchBadge": null,
				"LBTitle": null,
				"LBDesc": null,
				"LBFormat": null,
				"CommentUser": null,
				"CommentMotto": null,
				"CommentPoints": null,
				"Comment": null,
				"CommentPostedAt": null,
				"CommentID": null
			}
             */
            if(current.has("User")){
                this.GameID = current.getInt("GameID");
                this.AchievementID = current.getInt("ID");
                this.Points = current.getInt("AchPoints");
                //ach.IsAwarded = current.getString("IsAwarded");
                this.GameTitle = current.getString("GameTitle");
                this.GameIcon = current.getString("GameIcon");
                this.Achievement = current.getString("AchTitle");
                this.Description = current.getString("AchDesc");
                this.Badge = current.getString("AchBadge");
            }
            /* Achievement Structure
            "4874": {
                        "ID": "4874",
                        "NumAwarded": "3387",
                        "NumAwardedHardcore": "1683",
                        "Title": "I Believe I Can Fly",
                        "Description": "Collect a feather",
                        "Points": "2",
                        "TrueRatio": "2",
                        "Author": "UNHchabo",
                        "DateModified": "2016-12-10 22:56:01",
                        "DateCreated": "2014-02-09 01:07:44",
                        "BadgeName": "05506",
                        "DisplayOrder": "0",
                        "MemAddr": "0xH000019=2",
                        "DateEarned": "2016-12-19 03:23:35",
                        "DateEarnedHardcore": "2016-12-19 03:23:35"
                    },

         */
            else{
                this.AchievementID = current.getInt("ID");
                this.Points = current.getInt("Points");
                //ach.IsAwarded = current.getString("IsAwarded"););
                this.Achievement = current.getString("Title");
                this.Description = current.getString("Description");
                this.Badge = current.getString("BadgeName");




            }



        }
        Achievement(){

        }
    }

    static class Game {
        Integer GameID;
        Integer ConsoleID;
        String ConsoleName;
        String Title;
        String GameIcon;
        int NumAchievements;
        int PossibleScore;
        int NumAchieved;
        int ScoreAchieved;
        Game() {
        }
    }

    static class FeedItem{
        String User;
        String UserIconUrl;
        String GameIconUrl;
        String AchievementTitle;
        String AchievementDescription;
        String GameTitle;
        String GameConsole;
        String AchievementBadgeUrl;
        String TimeStamp;

        int ActivityType;
        int GameID;
        int AchievementID;
        int AchievementPoints;

    }

    static class top10User{
        String Name;
        int points;
        int truePoints;
        String userPicUrl;
    }
}