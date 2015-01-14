package com.model;

import java.io.Serializable;

public class MultiDayWeather implements Serializable{

	int dt;
	double tempDay;
	double tempMin;
	double tempMax;
	double tempNight;
	double tempEve;
	double tempMorn;
	String weatherDescription;

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}

	public double getTempDay() {
		return tempDay;
	}

	public void setTempDay(double tempDay) {
		this.tempDay = tempDay;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempNight() {
		return tempNight;
	}

	public void setTempNight(double tempNight) {
		this.tempNight = tempNight;
	}

	public double getTempEve() {
		return tempEve;
	}

	public void setTempEve(double tempEve) {
		this.tempEve = tempEve;
	}

	public double getTempMorn() {
		return tempMorn;
	}

	public void setTempMorn(double tempMorn) {
		this.tempMorn = tempMorn;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

}
