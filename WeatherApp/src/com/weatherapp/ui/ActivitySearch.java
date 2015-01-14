package com.weatherapp.ui;

import java.util.ArrayList;
import com.adapter.AdapterMultiCityWeather;
import com.async.GetMultiCityDataAsync;
import com.model.City;
import com.util.Constants;
import com.util.OnTaskCompleted;
import com.weatherapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ActivitySearch extends Activity implements OnTaskCompleted {
	EditText cityToSearch;
	ListView citiListView;
	AdapterMultiCityWeather adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchpage);

		Button search = (Button) findViewById(R.id.buttonSearch);
		cityToSearch = (EditText) findViewById(R.id.editTextCityName);
		citiListView = (ListView) findViewById(R.id.listCity);
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapter!=null&&adapter.getCount() > 0) {
					adapter.clear();
					adapter.notifyDataSetChanged();
				}

				new GetMultiCityDataAsync(ActivitySearch.this,ActivitySearch.this).execute(Constants.GetCities
						+ cityToSearch.getText().toString().trim().replace(" ", "%20"));
			}
		});

		citiListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				City c = (City) parent.getItemAtPosition(position);
				Bundle extras=new Bundle();
				extras.putSerializable("CityInfo",c);
				
				startActivity(new Intent(ActivitySearch.this, ActivityWeather.class).putExtras(extras));
				Toast.makeText(ActivitySearch.this, "City: " + c.getCityName(),Toast.LENGTH_SHORT).show();
			}

		});
	}

	@Override
	public void onTaskCompleted(ArrayList<City> list) {
		if (list.size() > 0) {
			adapter = new AdapterMultiCityWeather(ActivitySearch.this,R.layout.cardview_multicity_item, list);
			citiListView.setAdapter(adapter);
		} else {
			Toast.makeText(ActivitySearch.this, "City not found", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
