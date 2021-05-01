package controllers;

import models.Member;
import models.Reading;
import models.Station;
import play.Logger;
import play.mvc.Controller;

import java.util.*;

import static utils.StationUtils.*;

public class StationCtrl extends Controller {

    public static void stations() {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            List<Station> stations = loggedInUser.stations;
            for (Station station : stations) {
                populateStationValues(station);
            }
            render("stations.html", loggedInUser, stations);
        } catch (Exception e) {
            Logger.info("Failed to load all stations: "+e.toString());
            render("errors/404.html");
        }
    }

    public static void station(Long id) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = Station.findById(id);
            populateStationValues(station);
            render("station.html", station);
        } catch (Exception e) {
            Logger.info("Failed to add station: "+e.toString());
            render("errors/404.html");
        }
    }

    public static void addStation(String station_name, double latitude, double longitude) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = new Station(station_name, latitude, longitude);
            station.save();
            loggedInUser.stations.add(station);
            loggedInUser.save();
            Logger.info("adding station" + station + " to user " + loggedInUser.lastName);
            redirect("/stations");
        } catch (Exception e) {
            Logger.info("Failed to add station: "+e.toString());
            redirect("errors/404.html");
        }
    }

    public static void addReading(Long id, int code, double temperature, int windSpeed, int pressure, int windDirection) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = Station.findById(id);
            Reading newReading = new Reading(new Date(), code, temperature, windSpeed, pressure, windDirection);
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
            List<Reading> readings = s.readings;
            Reading latest = readings.get(readings.size() - 1);
            s.weatherCode = codeToText(latest.code);
            s.fahrenheit = calcFahrenheit(latest.temperature);
            s.windBeaufort = calcBeaufort(latest.windSpeed);
            s.windDirection = calcWindDir(latest.windDirection);
            s.windChill = calcWindChill(latest.temperature, latest.windSpeed);
            s.weatherIcon = weatherIcon(latest.code);
            s.celsius = latest.temperature;
            s.pressure = latest.pressure;

            // init min/max vars for comparison against readings
            // set min value high initially to allow for all positive readings
            s.maxPressure = 0;
            s.minPressure = 1000;
            s.maxTemp = 0;
            s.minTemp = 1000;
            s.maxWind = 0;
            s.minWind = 10000;

            // get max/min temp, windspeed, pressure

            for (Reading reading : readings) {
                if (reading.temperature < s.minTemp) { s.minTemp = reading.temperature; }
                if (reading.temperature > s.maxTemp) { s.maxTemp = reading.temperature; }
                if (reading.windSpeed < s.minWind) { s.minWind = reading.windSpeed; }
                if (reading.windSpeed > s.maxWind) { s.maxWind = reading.windSpeed; }
                if (reading.pressure < s.minPressure) { s.minPressure = reading.pressure; }
                if (reading.pressure > s.maxPressure) { s.maxPressure = reading.pressure; }
            }

        } catch (Exception e) {
            Logger.info("Failed to populate station vals: "+e.toString());
        }
    }
}