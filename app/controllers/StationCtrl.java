package controllers;

import models.Member;
import models.Reading;
import models.Station;
import play.Logger;
import play.mvc.Controller;
import utils.StationDetails;

import java.util.*;

import static utils.StationUtils.*;

public class StationCtrl extends Controller {

    public static void stations() {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            List<Station> stations = loggedInUser.stations;
            for (Station station : stations) {
                station.stats = calcStationDetails(station);
            }

//            Collections.sort(stations,
//                    (o1, o2) -> o1.getName().compareTo(o2.getName()));

//            Comparator<Station> byName = Comparator.comparing(Station::getName);
//            stations.sort(byName);

            stations.sort(Comparator.comparing(Station::getName, String.CASE_INSENSITIVE_ORDER));

            render("stations.html", loggedInUser, stations);
        } catch (Exception e) {
            Logger.info("Failed to load all stations: " + e.toString());
            render("errors/404.html");
        }
    }

    public static void station(Long id) {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            Station station = Station.findById(id);
            station.stats = calcStationDetails(station);
            render("station.html", station, loggedInUser);
        } catch (Exception e) {
            Logger.info("Failed to load station: " + e.toString());
            render("errors/404.html");
        }
    }

    public static void publicStations() {
        try {
            Member loggedInUser = Accounts.getLoggedInMember();
            List<Station> publicStations = Station.findPublicStations();
            if (publicStations.size() > 0) {
                for (Station s : publicStations) {
                    s.stats = calcStationDetails(s);
                }
            }
            render("publicstations.html", publicStations, loggedInUser);
        } catch (Exception e) {
            Logger.info("failed to get public stations: " + e.toString());
            redirect("/stations");
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
            Logger.info("Failed to add station: " + e.toString());
            redirect("errors/404.html");
        }
    }

    public static void deleteStation(Long id) {
        try {
            String memberId = session.get("logged_in_id");
            Member member = Member.findById(Long.parseLong(memberId));
            Station station = Station.findById(id);
            member.stations.remove(station);
            member.save();
            station.delete();
            redirect("/stations");
        } catch (Exception e) {
            Logger.info("Failed to delete station: " + e.toString());
            redirect("/stations");
        }
    }

    public static void setStationPublic(Long id) {
        try {
            Station station = Station.findById(id);
            station.setVisibility(station.publicStation);
            station.save();
            redirect("/stations");
        } catch (Exception e) {
            Logger.info("failed to set station public: " + e.toString());
            redirect("/stations");
        }
    }
}