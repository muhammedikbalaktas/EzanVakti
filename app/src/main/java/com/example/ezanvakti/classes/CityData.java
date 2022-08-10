package com.example.ezanvakti.classes;

public class CityData {
    private String name;
    private PrayerTimes []prayerTimes=new PrayerTimes[7];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrayerTimes[] getPrayerTimes() {
        return prayerTimes;
    }

    public void setPrayerTimes(PrayerTimes[] prayerTimes) {
        this.prayerTimes = prayerTimes;
    }
}
