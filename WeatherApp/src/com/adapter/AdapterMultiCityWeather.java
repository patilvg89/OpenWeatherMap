package com.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.model.City;
import com.util.Util;
import com.weatherapp.R;

public class AdapterMultiCityWeather extends ArrayAdapter<City> {
	int resource;
	ArrayList<City> data;
	Context context;

	// View lookup cache
	private static class ViewHolder {
		TextView textViewCityName;
		TextView textViewDescription;
		TextView textViewTodayTemp;
		ImageButton buttonAddRemove;
	}

	public AdapterMultiCityWeather(Context context, int resource,
			ArrayList<City> citys) {
		super(context, resource, citys);
		this.resource = resource;
		this.data = citys;
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		City c = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(resource, parent, false);
			viewHolder.textViewCityName = (TextView) convertView
					.findViewById(R.id.textViewCityName);
			viewHolder.textViewDescription = (TextView) convertView
					.findViewById(R.id.textViewDescription);
			viewHolder.textViewTodayTemp = (TextView) convertView
					.findViewById(R.id.textViewTodayTemp);
			viewHolder.buttonAddRemove = (ImageButton) convertView
					.findViewById(R.id.butttonAddRemove);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.textViewCityName.setText(c.getCityName() + ", "
				+ c.getCountry());
		viewHolder.textViewDescription.setText("Description: "
				+ c.getDescription());
		viewHolder.textViewTodayTemp.setText("Temp: " + c.getTemp());

		// set add button click listener
		viewHolder.buttonAddRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				City city = data.get(position);
				try {
					JSONObject json = new JSONObject(Util.getFromPrefs(context));

					JSONArray arr = json.getJSONArray("list");
					arr.put(city.getCityId());
					json.put("list", arr);
					Util.storeCityToPref(json, context);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				System.out.println("selected city:" + city.getCityId());
				data.remove(position);
				notifyDataSetChanged();
			}
		});

		// Return the completed view to render on screen
		return convertView;
	}
}
