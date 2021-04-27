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
    public String weatherCode;
    public double celsius;
    public double fahrenheit;
    public int windBeaufort;
    public String windDirection;
    public int pressure;
    public String windChill;

    public Station(String name) {
        this.name = name;
    }
}
