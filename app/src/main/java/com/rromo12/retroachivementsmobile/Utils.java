package com.rromo12.retroachivementsmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

class Utils {


    static Date stringtoDate(String dateString){
        Date date;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = formatter.parse(dateString);
            //String newDateString = formatter.format(date);
            //System.out.println(newDateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
    private static String dateToString(Date date){
        DateFormat df = new SimpleDateFormat("E, MMM dd yyyy");
        return df.format(date);
    }

    static String convertDateString(String dateSting){
        return dateToString(stringtoDate(dateSting));
    }

    static JSONArray getSortedList(JSONArray array) throws JSONException {
        List<JSONObject> list = new ArrayList<>();
        JSONArray sortedJsonArray = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }


        Collections.sort(list, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject lhs, JSONObject rhs) {
                        String valA = "";
                        String valB = "";

                        try {
                            valA = (String) lhs.get("Title");
                            valB = (String) rhs.get("Title");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return valA.compareTo(valB);

                    }
                }

        );

        JSONArray resultArray = new JSONArray(list);

        for (int i = 0; i < array.length(); i++) {
            sortedJsonArray.put(list.get(i));
        }

        return resultArray;
    }




}
