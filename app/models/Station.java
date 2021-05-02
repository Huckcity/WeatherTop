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
    public List<Reading> readings = new ArrayList<Reading>();
    public double latitude;
    public double longitude;

    public double celsius;
    public double fahrenheit;
    public int windBeaufort;
    public int pressure;
    public String windChill;
    public String windDirection;
    public String weatherCode;
    public String weatherIcon;

    public double maxTemp;
    public double minTemp;
    public double maxWind;
    public double minWind;
    public int maxPressure;
    public int minPressure;

    public Station(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
