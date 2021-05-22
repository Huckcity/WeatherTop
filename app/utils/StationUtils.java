package utils;

import models.Reading;
import models.Station;
import play.Logger;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class StationUtils {

  private static final HashMap<Integer, String> weatherIcons = new HashMap<>();
  static {
    weatherIcons.put(100, "fa-sun");
    weatherIcons.put(200, "fa-cloud-sun");
    weatherIcons.put(300, "fa-cloud");
    weatherIcons.put(400, "fa-cloud-rain");
    weatherIcons.put(500, "fa-cloud-showers-heavy");
    weatherIcons.put(600, "fa-tint");
    weatherIcons.put(700, "fa-snowflake");
    weatherIcons.put(800, "fa-poo-storm");
  }

  public static StationDetails calcStationDetails(Station s) {

    StationDetails stats = new StationDetails();
    if (s.readings.size() > 0) {
      Logger.info("attempting to populate station stats");
      try {
        Reading latest = s.readings.get(s.readings.size() - 1);
        stats.weatherCode = codeToText(latest.code);
        stats.fahrenheit = calcFahrenheit(latest.temperature);
        stats.windBeaufort = calcBeaufort(latest.windSpeed);
        stats.windDirection = calcWindDir(latest.windDirection);
        stats.windChill = calcWindChill(latest.temperature, latest.windSpeed);
        stats.weatherIcon = weatherIcon(latest.code);
        stats.celsius = latest.temperature;
        stats.pressure = latest.pressure;

        // get max/min temp, windspeed, pressure
        stats.minTemp = getMinTemp(s.readings);
        stats.maxTemp = getMaxTemp(s.readings);
        stats.minWind = getMinWind(s.readings);
        stats.maxWind = getMaxWind(s.readings);
        stats.minPressure = getMinPress(s.readings);
        stats.maxPressure = getMaxPress(s.readings);

        // set prettyTime for more readable data
        for (Reading reading : s.readings) {
          SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
          reading.prettyTime = formatter.format(reading.date);
        }

        // get trends
        double[] lastThreeTemps = new double[3];
        double[] lastThreeWinds = new double[3];
        double[] lastThreePress = new double[3];

        if (s.readings.size() > 2) {
          for (int i = 0; i < 3; i++) {
            lastThreeTemps[i] = s.readings.get(s.readings.size() - (i + 1)).temperature;
            lastThreeWinds[i] = s.readings.get(s.readings.size() - (i + 1)).windSpeed;
            lastThreePress[i] = s.readings.get(s.readings.size() - (i + 1)).pressure;
          }
        }

        stats.tempTrend = checkTrend(lastThreeTemps);
        stats.windTrend = checkTrend(lastThreeWinds);
        stats.presTrend = checkTrend(lastThreePress);

      } catch (Exception e) {
        Logger.info("Failed to populate station vals: " + e);
      }
    }
    return stats;
  }

  private static int getMaxPress(List<Reading> readings) {
    int maxPress = readings.get(0).pressure;
    for (Reading r : readings) {
      maxPress = r.pressure > maxPress ? maxPress = r.pressure : maxPress;
    }
    return maxPress;
  }

  private static int getMinPress(List<Reading> readings) {
    int minPress = readings.get(0).pressure;
    for (Reading r : readings) {
      minPress = r.pressure < minPress ? minPress = r.pressure : minPress;
    }
    return minPress;
  }

  private static double getMaxWind(List<Reading> readings) {
    double maxWind = readings.get(0).windSpeed;
    for (Reading r : readings) {
      maxWind = r.windSpeed > maxWind ? maxWind = r.windSpeed : maxWind;
    }
    return maxWind;
  }

  private static double getMinWind(List<Reading> readings) {
    double minWind = readings.get(0).windSpeed;
    for (Reading r : readings) {
      minWind = r.windSpeed < minWind ? minWind = r.windSpeed : minWind;
    }
    return minWind;
  }

  private static double getMaxTemp(List<Reading> readings) {
    double maxTemp = readings.get(0).temperature;
    for (Reading r : readings) {
      maxTemp = r.temperature > maxTemp ? maxTemp = r.temperature : maxTemp;
    }
    return maxTemp;
  }

  private static double getMinTemp(List<Reading> readings) {
    double minTemp = readings.get(0).temperature;
    for (Reading r : readings) {
      minTemp = r.temperature < minTemp ? minTemp = r.temperature : minTemp;
    }
    return minTemp;
  }

  private static String calcWindChill(double t, double v) {
    double wc = 13.12 + 0.6215 * t - 11.37 * (Math.pow(v, 0.16)) + 0.3965 * t * (Math.pow(v, 0.16));
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(wc);
  }

  private static String calcWindDir(double windDirection) {
    if (windDirection >= 11.25 && windDirection <= 33.75) return "North North East";
    if (windDirection >= 33.75 && windDirection <= 56.25) return "North East";
    if (windDirection >= 56.25 && windDirection <= 78.75) return "East North East";
    if (windDirection >= 78.75 && windDirection <= 101.25) return "East";
    if (windDirection >= 101.25 && windDirection <= 123.75) return "East South East";
    if (windDirection >= 123.75 && windDirection <= 146.25) return "South East";
    if (windDirection >= 146.25 && windDirection <= 168.75) return "South South East";
    if (windDirection >= 168.75 && windDirection <= 191.25) return "South";
    if (windDirection >= 191.25 && windDirection <= 213.75) return "South South West";
    if (windDirection >= 213.75 && windDirection <= 236.25) return "South West";
    if (windDirection >= 236.25 && windDirection <= 258.75) return "West South West";
    if (windDirection >= 258.75 && windDirection <= 281.25) return "West";
    if (windDirection >= 281.25 && windDirection <= 303.75) return "West North West";
    if (windDirection >= 303.75 && windDirection <= 326.25) return "North West";
    if (windDirection >= 326.25 && windDirection <= 348.75) return "North North West";
    if (windDirection >= 348.75 && windDirection <= 11.25) return "North";
    return "Unknown";
  }

  private static int calcBeaufort(double windSpeed) {
    if (windSpeed <= 1) return 0;
    if (windSpeed <= 5) return 1;
    if (windSpeed <= 11) return 2;
    if (windSpeed <= 19) return 3;
    if (windSpeed <= 28) return 4;
    if (windSpeed <= 38) return 5;
    if (windSpeed <= 49) return 6;
    if (windSpeed <= 61) return 7;
    if (windSpeed <= 74) return 8;
    if (windSpeed <= 88) return 9;
    if (windSpeed <= 102) return 10;
    if (windSpeed <= 117) return 11;
    return -1;
  }

  private static int calcFahrenheit(double temperature) {
    return (int) (temperature * (9 / 5) + 32);
  }

  private static String codeToText(int code) {
    // example of switch, hashmap could work here too
    switch (code) {
      case 100:
        return "Clear";
      case 200:
        return "Partial Clouds";
      case 300:
        return "Cloudy";
      case 400:
        return "Light Showers";
      case 500:
        return "Heavy Showers";
      case 600:
        return "Rain";
      case 700:
        return "Snow";
      case 800:
        return "Thunder";
      default:
        return "Error!";
    }
  }

  private static String weatherIcon(int code) {
    return weatherIcons.get(code);
  }

  public static String checkTrend(double[] vals) {
    if (vals[0] > vals[1] && vals[1] > vals[2]) {
      return "fa-arrow-up";
    }
    if (vals[0] < vals[1] && vals[1] < vals[2]) {
      return "fa-arrow-down";
    }
    return "fa-arrows-alt-h";
  }

}
