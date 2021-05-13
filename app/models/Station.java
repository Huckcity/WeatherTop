package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Station extends Model {

  public String name;
  @OneToMany(cascade = CascadeType.ALL)
  public List<Reading> readings = new ArrayList<>();
  public double latitude;
  public double longitude;
  public boolean publicStation = false;

  public Station(String name, double latitude, double longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static List<Station> findPublicStations() {
    return find("publicStation", true).fetch();
  }

  public String getName() {
    return this.name;
  }

  public void toggleVisibility() {
    this.publicStation = !this.publicStation;
  }
}
