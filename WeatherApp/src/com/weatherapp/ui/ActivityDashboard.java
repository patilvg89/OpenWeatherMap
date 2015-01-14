package com.weatherapp.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.adapter.AdapterMultiCityWeather;
import com.async.AppLocationService;
import com.async.GetMultiCityDataAsync;
import com.model.City;
import com.util.Constants;
import com.util.OnTaskCompleted;
import com.util.Util;
import com.weatherapp.R;

public class ActivityDashboard extends Activity implements OnTaskCompleted {
	ListView citiListView;
	AdapterMultiCityWeather adapter;
	AppLocationService appLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		appLocationService = new AppLocationService(ActivityDashboard.this);
		citiListView = (ListView) findViewById(R.id.listViewCityWeather);

		// get cities saved in shared prefs
		System.out.println(Constants.GetMultiCityWeather + getCitiesWithId());
		String Url = Constants.linkToVerify = Constants.GetMultiCityWeather + getCitiesWithId();

		new GetMultiCityDataAsync(ActivityDashboard.this,
				ActivityDashboard.this).execute(Url);

		// start search activity
		findViewById(R.id.buttonStartSearch).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(ActivityDashboard.this,
								ActivitySearch.class));
					}
				});
		citiListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				City c = (City) parent.getItemAtPosition(position);
				Bundle extras = new Bundle();
				extras.putSerializable("CityInfo", c);

				startActivity(new Intent(ActivityDashboard.this,
						ActivityWeather.class).putExtras(extras));
				Toast.makeText(ActivityDashboard.this,
						"City: " + c.getCityName(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private String getCitiesWithId() {
		// get city list from shared pref
		String cityIds = Util.getCityListInString(Util
				.getFromPrefs(ActivityDashboard.this));
		// if shared pref has no data
		if (cityIds.equals("")) {
			try {
				JSONObject json = new JSONObject();
				JSONArray arr = new JSONArray();
				arr.put(1269406);
				arr.put(1259229);
				arr.put(1278148);
				arr.put(1275339);
				json.put("list", arr);
				cityIds = Util.storeCityToPref(json, ActivityDashboard.this);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			cityIds = Util.getCityListInString(Util
					.getFromPrefs(ActivityDashboard.this));
		}
		return cityIds;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// id shared pref is updated then fetch new data
		String Url = Constants.GetMultiCityWeather + getCitiesWithId();
		if (!Url.equals(Constants.linkToVerify)) {
			new GetMultiCityDataAsync(ActivityDashboard.this,
					ActivityDashboard.this).execute(Url);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu, (Menu) menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_loc:
			Location gpsLocation = appLocationService
					.getLocation(LocationManager.GPS_PROVIDER);

			if (gpsLocation != null) {
				double latitude = gpsLocation.getLatitude();
				double longitude = gpsLocation.getLongitude();
				Toast.makeText(
						getApplicationContext(),
						"Mobile Location (GPS): \nLatitude: " + latitude
								+ "\nLongitude: " + longitude,
						Toast.LENGTH_LONG).show();

				// set lati and longi and start new intent
				City c = new City();
				c.setLat(latitude);
				c.setLon(longitude);
				c.setCityId(-1);
				Bundle extras = new Bundle();
				extras.putSerializable("CityInfo", c);
				startActivity(new Intent(ActivityDashboard.this,
						ActivityWeather.class).putExtras(extras));

			} else {
				showSettingsAlert("GPS");
			}
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onTaskCompleted(ArrayList<City> list) {
		if (list.size() > 0) {
			adapter = new AdapterMultiCityWeather(ActivityDashboard.this,
					R.layout.cardview_dashboard_item, list);
			citiListView.setAdapter(adapter);
		} else {
			Toast.makeText(ActivityDashboard.this, "Something wrong happens..",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void showSettingsAlert(String provider) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ActivityDashboard.this);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog.setMessage(provider
				+ " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						ActivityDashboard.this.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
}
