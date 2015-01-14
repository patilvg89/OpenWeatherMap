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

import com.model.City;
import com.util.OnTaskCompleted;

public class GetMultiCityDataAsync extends
		AsyncTask<String, Void, ArrayList<City>> {
	private OnTaskCompleted listener;
	Context context;
	ProgressDialog adb;

	public GetMultiCityDataAsync(Context context, OnTaskCompleted listener) {
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
	protected ArrayList<City> doInBackground(String... params) {
		ArrayList<City> cityList = new ArrayList<City>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String resp = EntityUtils.toString(entity);

			JSONObject j = new JSONObject(resp);
			JSONArray arr = j.getJSONArray("list");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject json = arr.getJSONObject(i);
				City city = new City();

				city.setCityName(json.getString("name"));
				city.setCityId(json.getInt("id"));

				JSONObject sysObj = json.getJSONObject("sys");
				city.setCountry(sysObj.getString("country"));

				JSONObject coord = json.getJSONObject("coord");
				city.setLat(coord.getDouble("lat"));
				city.setLon(coord.getDouble("lon"));

				JSONArray weather = json.getJSONArray("weather");
				JSONObject zeroPosition = weather.getJSONObject(0);
				city.setDescription(zeroPosition.getString("description"));

				JSONObject main = json.getJSONObject("main");
				city.setTemp(main.getDouble("temp"));
				city.setTemp_min(main.getDouble("temp_min"));
				city.setTemp_max(main.getDouble("temp_max"));
				cityList.add(city);
			}
			System.out.println("city list:" + j.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return cityList;
	}

	@Override
	protected void onPostExecute(ArrayList<City> result) {
		super.onPostExecute(result);
		adb.dismiss();
		listener.onTaskCompleted(result);
	}
}
