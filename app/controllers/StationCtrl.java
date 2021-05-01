package controllers;

import models.Member;
import models.Reading;
import models.Station;
import play.Logger;
import play.mvc.Controller;

import java.text.DecimalFormat;
import java.util.*;

import static utils.StationUtils.*;

public class StationCtrl extends Controller {

    public static void stations() {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            System.out.println(loggedInUser);
            List<Station> stations = loggedInUser.stations;
            System.out.println(stations);
            for (Station station : stations) {
                populateStationValues(station);
            }
            render("stations.html", stations);
        } catch (Exception e) {
            Logger.info("Failed to load all stations: "+e.toString());
            render("errors/404.html");
        }
    }

    public static void station(Long id) {
        try {
            Station station = Station.findById(id);
            populateStationValues(station);
            render("station.html", station);
        } catch (Exception e) {
            Logger.info("Failed to add station: "+e.toString());
            render("errors/404.html");
        }
    }

    public static void addStation(String station_name) {
        try {
            Station station = new Station(station_name);
            Member currentUser = Accounts.getLoggedInMember();
            station.save();
            currentUser.stations.add(station);
            currentUser.save();
            Logger.info("adding station" + station + " to user " + currentUser.lastName);
            redirect("/stations");
        } catch (Exception e) {
            Logger.info("Failed to add station: "+e.toString());
            redirect("errors/404.html");
        }
    }

    public static void addReading(Long id, int code, double temperature, int windSpeed, int pressure, int windDirection) {
        try {
            Station station = Station.findById(id);
            Reading newReading = new Reading(code, temperature, windSpeed, pressure, windDirection);
            station.readings.add(newReading);
            station.save();
            redirect("/station/" + id);
        } catch (Exception e) {
            Logger.info("Failed to add reading: "+e.toString());
            redirect("errors/404.html");
        }
    }

    private static void populateStationValues(Station s) {
        try {
            Reading r = s.readings.get(s.readings.size() - 1);
            s.weatherCode = codeToText(r.code);
            s.celsius = r.temperature;
            s.fahrenheit = calcFahrenheit(r.temperature);
            s.windBeaufort = calcBeaufort(r.windSpeed);
            s.windDirection = calcWindDir(r.windDirection);
            s.pressure = r.pressure;
            s.windChill = calcWindChill(r.temperature, r.windSpeed);
        } catch (Exception e) {
            Logger.info("Failed to populate station vals: "+e.toString());
        }
    }
}