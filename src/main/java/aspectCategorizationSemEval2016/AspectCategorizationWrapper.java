package aspectCategorizationSemEval2016;

/**
 * Created by krayush on 27-08-2015.
 */

import java.io.IOException;

public class AspectCategorizationWrapper {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            int option = Integer.parseInt(args[0]);
            if (option == 1) {
                if (args[1].compareToIgnoreCase("r") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], "dummyParameter", "RestaurantPairs.txt");
                    new ABSAClassifierRestaurants(option, args[2], "dummyParameter", args[4]);
                    new CreateRestaurantsPredictedXMLGoldSet();
                } else if (args[1].compareToIgnoreCase("l") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], "dummyParameter", "LaptopPairs.txt");
                    //new ABSAClassifierLaptops(option, args[2], "dummyParameter");
                } if (args[1].compareToIgnoreCase("h") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], "dummyParameter", "HotelPairs.txt");
                    new ABSAClassifierRestaurants(option, args[2], "dummyParameter", args[4]);
                    new CreateRestaurantsPredictedXMLGoldSet();
                }

                    //new calculateAccuracy();
            } else if (option == 2) {
                if (args[1].compareToIgnoreCase("r") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], "dummyParameter", "RestaurantPairs.txt");
                    new ABSAClassifierRestaurants(option, args[2], args[3], args[4]);
                } else if (args[1].compareToIgnoreCase("l") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], "dummyParameter", "LaptopPairs.txt");
                    //new ABSAClassifierLaptops(option, args[2], args[3]);
                }
            } else if (option == 3) {
                if (args[1].compareToIgnoreCase("r") == 0) {
                    new ABSAPolarityRestaurants(option, args[2], args[4], "RestaurantPairs.txt");
                    new ABSAClassifierRestaurants(option, args[2], args[3], args[4]);
                    new CreateRestaurantsPredictedXML();
                } else if (args[1].compareToIgnoreCase("l") == 0) {
                    new ABSAPolarityLaptops(option, args[2], args[4], "LaptopPairs.txt");
                    new ABSAClassifierLaptops(option, args[2], args[3], args[4]);
                    new CreateRestaurantsPredictedXML();
                } else if (args[1].compareToIgnoreCase("m") == 0) {
                    new ABSAPolarityMobiles(option, args[2], args[4], "MobilePairs.txt");
                    new ABSAClassifierMobiles(option, args[2], args[3], args[4]);
                    new CreateRestaurantsPredictedXML();
                } else if (args[1].compareToIgnoreCase("h") == 0) {
                    new ABSAPolarityMobiles(option, args[2], args[4], "HotelPairs.txt");
                    new ABSAClassifierMobiles(option, args[2], args[3], args[4]);
                    new CreateRestaurantsPredictedXML();
                }
                //new calculateAccuracy();
                //CalculateFScore fob = new CalculateFScore();
            }
        }
    }

}

