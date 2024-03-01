package id.segari.ortools.util;

public class GeoUtils {
    private static final int LAT_LONG_MIN_LENGTH = 2;
    /*
        https://en.wikipedia.org/wiki/Haversine_formula
     */
    public static double getHaversineDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        // converts from degrees to radians
        lat1 = Math.toRadians(lat1); lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2); lon2 = Math.toRadians(lon2);

        // Haversine formula
        double dlat = lat2 - lat1; double dlon = lon2 - lon1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956 for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    public static double getHaversineDistanceInMeter(double lat1, double lon1, double lat2, double lon2){
        return getHaversineDistanceInKm(lat1, lon1, lat2, lon2) * 1000;
    }
}
