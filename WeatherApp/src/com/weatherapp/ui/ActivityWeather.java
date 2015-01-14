package com.weatherapp.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.async.GetMultiDayDataAsync;
import com.model.City;
import com.model.MultiDayWeather;
import com.util.Constants;
import com.util.OnMultiDayDataCompleted;
import com.weatherapp.R;

public class ActivityWeather extends FragmentActivity implements
		OnMultiDayDataCompleted {
	SwipeDatePagerAdapter mSwipeDatePagerAdapter;
	ViewPager mViewPager;
	public static List<MultiDayWeather> list = new ArrayList<MultiDayWeather>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_details);
		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.ViewPager and its adapters use support library
		// fragments, so we must use
		// getSupportFragmentManager.
		mSwipeDatePagerAdapter = new SwipeDatePagerAdapter(
				getSupportFragmentManager());
		// Set up the ViewPager, attaching the adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSwipeDatePagerAdapter);

		// set today data
		City c = (City) getIntent().getExtras().getSerializable("CityInfo");
		// get multi days weather for a city
		if (c.getCityId()!= -1) {
			int cityId = c.getCityId();
			new GetMultiDayDataAsync(ActivityWeather.this, ActivityWeather.this).
			execute(Constants.GetMultiDaysWeather+"id="+ cityId);
		}else{
			new GetMultiDayDataAsync(ActivityWeather.this, ActivityWeather.this).
			execute(Constants.GetMultiDaysWeather+"lat=" + c.getLat()+"&lon="+c.getLon());
		}
		
	}

	public class SwipeDatePagerAdapter extends	FragmentStatePagerAdapter {
		public SwipeDatePagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DataDisplayFragment();
			MultiDayWeather c = list.get(position);
			Bundle extras = new Bundle();
			extras.putSerializable("WeatherInfo", c);
			fragment.setArguments(extras);
			return fragment;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public CharSequence getPageTitle(int position) {
			MultiDayWeather obj = list.get(position);
			long timestamp = obj.getDt();
			return getDate(timestamp);
		}
	}

	public static class DataDisplayFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_collection_object, container, false);

			Bundle args = getArguments();
			MultiDayWeather c = (MultiDayWeather) args.getSerializable("WeatherInfo");
			((TextView) rootView.findViewById(R.id.textViewDescription)).setText("Description: "+ c.getWeatherDescription());
			((TextView) rootView.findViewById(R.id.textViewTodayAverage)).setText("Today Temp: "+ c.getTempDay());
			((TextView) rootView.findViewById(R.id.textViewTodayMin)).setText("Min Temp: "+ c.getTempMin());
			((TextView) rootView.findViewById(R.id.textViewTodayMax)).setText("Max Temp: "+ c.getTempMax());
			((TextView) rootView.findViewById(R.id.textViewTodayMorn)).setText("Morning Temp: "+ c.getTempMorn());
			((TextView) rootView.findViewById(R.id.textViewTodayEve)).setText("Evening Temp: "+ c.getTempEve());
			((TextView) rootView.findViewById(R.id.textViewTodayNight)).setText("Night Temp: "+ c.getTempNight());
			
			return rootView;
		}
	}

	@Override
	public void onTaskCompleted(ArrayList<MultiDayWeather> o) {
		list = o;
		mSwipeDatePagerAdapter.notifyDataSetChanged();
	}

	private CharSequence getDate(long time) {
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTimeInMillis(time*1000);
		String date = DateFormat.format("dd-MM-yyyy", cal).toString();
		return date;
	}
}