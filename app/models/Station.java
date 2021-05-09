package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.Logger;
import play.db.jpa.Model;
import utils.StationDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Station extends Model {

    public String name;
    @OneToMany(cascade = CascadeType.ALL)
    public List<Reading> readings = new ArrayList<>();
    public double latitude;
    public double longitude;
    public StationDetails stats;
    public boolean publicStation = false;

    public Station(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<Station> findPublicStations() {
        List<Station> publicStations = find("publicStation", true).fetch();
        return publicStations;
    }

    public String getName() {
        return this.name;
    }

    public void setVisibility(boolean publicStation) {
        this.publicStation = !this.publicStation;
    }
}
