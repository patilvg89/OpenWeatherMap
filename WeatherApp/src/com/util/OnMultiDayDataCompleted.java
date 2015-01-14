package com.util;

import java.util.ArrayList;
import com.model.MultiDayWeather;

public interface OnMultiDayDataCompleted 
{
	public void onTaskCompleted(ArrayList<MultiDayWeather> o);
}
