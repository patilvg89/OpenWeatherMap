package com.async;

import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.model.MultiDayWeather;
import com.util.OnMultiDayDataCompleted;

public class GetMultiDayDataAsync extends AsyncTask<String, Void, ArrayList<MultiDayWeather>> {
	private OnMultiDayDataCompleted listener;
	Context context;
	ProgressDialog adb;

	public GetMultiDayDataAsync(Context context, OnMultiDayDataCompleted listener) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		adb = ProgressDialog.show(context, "", "Please wait");
		adb.setCancelable(true);
	}

	@Override
	protected ArrayList<MultiDayWeather> doInBackground(String... params) {
		ArrayList<MultiDayWeather> weatherList = new ArrayList<MultiDayWeather>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			System.out.println("mlti_data url:"+params[0]);
			HttpGet httpGet = new HttpGet(params[0]);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String resp = EntityUtils.toString(entity);

			JSONObject j = new JSONObject(resp);
			JSONObject city=j.getJSONObject("city");
			System.out.println("city:"+city.getString("name")+" id:"+city.getInt("id"));
			
			JSONArray arr = j.getJSONArray("list");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject json = arr.getJSONObject(i);
				
				MultiDayWeather weatherOBJ = new MultiDayWeather();
				weatherOBJ.setDt(json.getInt("dt"));
				
				JSONObject tempObj=json.getJSONObject("temp");
				weatherOBJ.setTempDay(tempObj.getDouble("day"));
				weatherOBJ.setTempMin(tempObj.getDouble("min"));
				weatherOBJ.setTempMax(tempObj.getDouble("max"));
				weatherOBJ.setTempNight(tempObj.getDouble("night"));
				weatherOBJ.setTempEve(tempObj.getDouble("eve"));
				weatherOBJ.setTempMorn(tempObj.getDouble("morn"));
				
				JSONArray weatherArr=json.getJSONArray("weather");
				for (int k = 0; k < weatherArr.length(); k++) {
					JSONObject weather=weatherArr.getJSONObject(k);
					weatherOBJ.setWeatherDescription(weather.getString("description"));
				}
				weatherList.add(weatherOBJ);
			}
			System.out.println("city list:" + j.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return weatherList;
	}

	@Override
	protected void onPostExecute(ArrayList<MultiDayWeather> result) {
		super.onPostExecute(result);
		adb.dismiss();
		listener.onTaskCompleted(result);
	}
}
