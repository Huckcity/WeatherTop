package controllers;

import models.Station;
import play.mvc.Controller;

import java.util.*;

public class StationCtrl extends Controller {

    public static void stations() {
        List<Station> stationList = Station.findAll();
        render("stations.html", stationList);
    }

    public static void addStation(String name) {
        Station station = new Station(name);
        station.save();
        redirect("/");
    }

    public String codeToText(int code) {
        switch (code) {
            case 100:
                return "Clear";
            case 200:
                return "Partial clouds";
            case 300:
                return "Cloudy";
            case 400:
                return "Light showers";
            case 500:
                return "Heavy showers";
            case 600:
                return "Rain";
            case 700:
                return "Snow";
            case 800:
                return "Thunder";
            default:
                return "Unknown";
        }
    }

}