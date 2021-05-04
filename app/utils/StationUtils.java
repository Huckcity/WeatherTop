package utils;

import models.Reading;
import models.Station;
import play.Logger;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;

public class StationUtils {

    public static StationDetails calcStationDetails(Station s) {

        StationDetails stats = new StationDetails();

        try {
            List<Reading> readings = s.readings;
            Reading latest = readings.get(readings.size() - 1);
            stats.weatherCode = codeToText(latest.code);
            stats.fahrenheit = calcFahrenheit(latest.temperature);
            stats.windBeaufort = calcBeaufort(latest.windSpeed);
            stats.windDirection = calcWindDir(latest.windDirection);
            stats.windChill = calcWindChill(latest.temperature, latest.windSpeed);
            stats.weatherIcon = weatherIcon(latest.code);
            stats.celsius = latest.temperature;
            stats.pressure = latest.pressure;

            // get max/min temp, windspeed, pressure and set prettyTime

            for (Reading reading : readings) {
                if (reading.temperature < stats.minTemp) {
                    stats.minTemp = reading.temperature;
                }
                if (reading.temperature > stats.maxTemp) {
                    stats.maxTemp = reading.temperature;
                }
                if (reading.windSpeed < stats.minWind) {
                    stats.minWind = reading.windSpeed;
                }
                if (reading.windSpeed > stats.maxWind) {
                    stats.maxWind = reading.windSpeed;
                }
                if (reading.pressure < stats.minPressure) {
                    stats.minPressure = reading.pressure;
                }
                if (reading.pressure > stats.maxPressure) {
                    stats.maxPressure = reading.pressure;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
                reading.prettyTime = formatter.format(reading.date);
            }

            // get trends

            double[] lastThreeTemps = new double[3];
            double[] lastThreeWinds = new double[3];
            double[] lastThreePress = new double[3];

            if (readings.size() > 2) {
                for (int i = 0; i < 3; i++) {
                    lastThreeTemps[i] = readings.get(readings.size() - (i + 1)).temperature;
                    lastThreeWinds[i] = readings.get(readings.size() - (i + 1)).windSpeed;
                    lastThreePress[i] = readings.get(readings.size() - (i + 1)).pressure;
                }
            }

            stats.tempTrend = checkTrend(lastThreeTemps);
            stats.windTrend = checkTrend(lastThreeWinds);
            stats.presTrend = checkTrend(lastThreePress);

        } catch (Exception e) {
            Logger.info("Failed to populate station vals: " + e.toString());
        }

        return stats;
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
        if (windSpeed <= 1) {
            return 0;
        } else if (windSpeed <= 5) {
            return 1;
        } else if (windSpeed <= 11) {
            return 2;
        } else if (windSpeed <= 19) {
            return 3;
        } else if (windSpeed <= 28) {
            return 4;
        } else if (windSpeed <= 38) {
            return 5;
        } else if (windSpeed <= 49) {
            return 6;
        } else if (windSpeed <= 61) {
            return 7;
        } else if (windSpeed <= 74) {
            return 8;
        } else if (windSpeed <= 88) {
            return 9;
        } else if (windSpeed <= 102) {
            return 10;
        } else if (windSpeed <= 117) {
            return 11;
        } else {
            return -1;
        }
    }

    private static int calcFahrenheit(double temperature) {
        return (int) (temperature * (9 / 5) + 32);
    }

    private static String codeToText(int code) {

        HashMap<Integer, String> weatherCodes = new HashMap<>();
        weatherCodes.put(100, "Clear");
        weatherCodes.put(200, "Partial clouds");
        weatherCodes.put(300, "Cloudy");
        weatherCodes.put(400, "Light showers");
        weatherCodes.put(500, "Heavy showers");
        weatherCodes.put(600, "Rain");
        weatherCodes.put(700, "Snow");
        weatherCodes.put(800, "Thunder");

        return weatherCodes.get(code);

    }

    private static String weatherIcon(int code) {

        HashMap<Integer, String> weatherIcons = new HashMap<>();
        weatherIcons.put(100, "fa-sun");
        weatherIcons.put(200, "fa-cloud-sun");
        weatherIcons.put(300, "fa-cloud");
        weatherIcons.put(400, "fa-cloud-rain");
        weatherIcons.put(500, "fa-cloud-showers-heavy");
        weatherIcons.put(600, "fa-tint");
        weatherIcons.put(700, "fa-snowflake");
        weatherIcons.put(800, "fa-poo-storm");

        return weatherIcons.get(code);
    }

    public static String checkTrend(double[] vals) {

        if (vals[0] > vals[1] && vals[1] > vals[2]) {
            return "up";
        }
        if (vals[0] < vals[1] && vals[1] < vals[2]) {
            return "down";
        }
        return "none";
    }

}
