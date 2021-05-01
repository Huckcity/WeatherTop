package utils;

import java.text.DecimalFormat;
import java.util.HashMap;

public class StationUtils {

    public static String calcWindChill(double t, double v) {
        double wc =  13.12 + 0.6215*t - 11.37*(Math.pow(v, 0.16)) + 0.3965*t*(Math.pow(v, 0.16));
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(wc);
    }

    public static String calcWindDir(double windDirection) {
        if (windDirection >= 348.75 && windDirection <= 11.25) return "North";
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
        return "Unknown";
    }

    public static int calcBeaufort(double windSpeed) {
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

    public static int calcFahrenheit(double temperature) {
        return (int)(temperature*(9/5) + 32);
    }

    public static String codeToText(int code) {

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

}
