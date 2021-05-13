package controllers;

import models.Member;
import models.Reading;
import models.Station;
import play.Logger;
import play.mvc.Controller;

import java.util.Date;

import static play.mvc.Controller.redirect;

public class ReadingCtrl extends Controller {

  public static void addReading(Long id, int code, double temperature, int windSpeed, int pressure, int windDirection) {
    try {
      Member loggedInUser = Accounts.getLoggedInMember();
      Station station = Station.findById(id);
      Reading newReading = new Reading(new Date(), code, temperature, windSpeed, pressure, windDirection);
      station.readings.add(newReading);
      station.save();
      redirect("/station/" + id);
    } catch (Exception e) {
      Logger.info("Failed to add reading: " + e.toString());
      redirect("errors/404.html");
    }
  }

  public static void deleteReading(Long sid, Long rid) {
    try {
      Station station = Station.findById(sid);
      Reading reading = Reading.findById(rid);
      station.readings.remove(reading);
      station.save();
      reading.delete();
      redirect("/station/" + sid);
    } catch (Exception e) {
      Logger.info("Failed to delete reading : " + e.toString());
      redirect("errors/404.html");
    }
  }
}
