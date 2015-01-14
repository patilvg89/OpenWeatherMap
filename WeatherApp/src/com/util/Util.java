package com.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class Util {

	public static String storeCityToPref(JSONObject object, Context context) {
		SharedPreferences.Editor edit = context.getSharedPreferences("NAME",
				Context.MODE_PRIVATE).edit();
		edit.putString("CitiesID", object.toString());
		edit.commit();

		return getCityListInString(object.toString());
	}

	public static String getFromPrefs(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("NAME",
				Context.MODE_PRIVATE);
		return prefs.getString("CitiesID", "");
	}

	public static String getCityListInString(String object) {
		String list = "";
		try {
			JSONObject json = new JSONObject(object);
			JSONArray arr = json.getJSONArray("list");
			for (int i = 0; i < arr.length(); i++) {
				list = list + "," + arr.getInt(i);
			}
			list = list.trim();
			if (list.endsWith(",")) {
				list = list.substring(0, list.length() - 1);
			}
			if (list.startsWith(",")) {
				list = list.substring(1);
			}
			System.err.println("string from json:"+list);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return list;
	}
}
