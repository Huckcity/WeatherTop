package controllers;

import models.Reading;
import models.Station;
import play.mvc.Controller;

import java.util.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void stations() {
        List<Station> stationList = Station.findAll();
        render("stations.html", stationList);
    }

    public static void addStation(String name) {
        Station station = new Station(name);
        station.save();
        redirect("/");
    }


}