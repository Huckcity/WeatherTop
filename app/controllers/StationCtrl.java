package controllers;

import models.Member;
import models.Station;
import play.Logger;
import play.mvc.Controller;

import java.util.*;

import utils.StationDetails;
import utils.StationUtils;

public class StationCtrl extends Controller {

  public static void station(Long id) {
    try {
      Member loggedInUser = Accounts.getLoggedInMember();
      Station station = Station.findById(id);
      StationDetails stats = StationUtils.calcStationDetails(station);
      render("station.html", station, stats, loggedInUser);
    } catch (Exception e) {
      Logger.info("Failed to load station: " + e);
      render("errors/404.html");
    }
  }

  public static void stations() {
    try {
      Member loggedInUser = Accounts.getLoggedInMember();
      List<Station> stations = loggedInUser.stations;
      List<StationDetails> stats = new ArrayList<>();
      stations.sort(Comparator.comparing(Station::getName, String.CASE_INSENSITIVE_ORDER));
      for (Station station : stations) {
        stats.add(StationUtils.calcStationDetails(station));
      }
      render("stations.html", loggedInUser, stations, stats);
    } catch (Exception e) {
      Logger.info("Failed to load all stations: " + e);
      render("errors/404.html");
    }
  }

  public static void publicStations() {
    try {
      Member loggedInUser = Accounts.getLoggedInMember();
      List<Station> publicStations = Station.findPublicStations();
      List<StationDetails> stats = new ArrayList<>();
      if (publicStations.size() > 0) {
        for (Station s : publicStations) {
          stats.add(StationUtils.calcStationDetails(s));
        }
      }
      render("publicstations.html", publicStations, stats, loggedInUser);
    } catch (Exception e) {
      Logger.info("failed to get public stations: " + e);
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
      Logger.info("Failed to add station: " + e);
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
      Logger.info("Failed to delete station: " + e);
      redirect("/stations");
    }
  }

  public static void setStationPublic(Long id) {
    try {
      Station station = Station.findById(id);
      station.toggleVisibility();
      station.save();
      redirect("/stations");
    } catch (Exception e) {
      Logger.info("failed to set station public: " + e);
      redirect("/stations");
    }
  }
}