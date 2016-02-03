package sentimentClassifier;

/**
 * Created by krayush on 27-08-2015.
 */

import java.io.IOException;

public class SentimentClassificationWrapper {
    public static void main(String[] args) throws IOException
    {
        if(args.length > 0)
        {
            int option = Integer.parseInt(args[0]);
            if(option == 1)
            {
                if(args[1].compareToIgnoreCase("r") == 0)
                {
                    new PolarityLabelsRestaurantsHotels(option, args[2], "dummyParameter");
                    new SentimentClassifierR(option, args[2], "dummyParameter");
                    //new CreatePredictedXML();
                }
                else if(args[1].compareToIgnoreCase("m") == 0)
                {
                    new PolarityLabelsLaptopsMobiles(option, args[2], "dummyParameter");
                    new SentimentClassifierM(option, args[2], "dummyParameter");
                    //new CreatePredictedXML();
                }
                else if(args[1].compareToIgnoreCase("l") == 0)
                {
                    new PolarityLabelsLaptopsMobiles(option, args[2], "dummyParameter");
                    new SentimentClassifierL(option, args[2], "dummyParameter");
                    //new CreatePredictedXML();
                }
                else if(args[1].compareToIgnoreCase("h") == 0)
                {
                    new PolarityLabelsRestaurantsHotels(option, args[2], "dummyParameter");
                    new SentimentClassifierH(option, args[2], "dummyParameter");
                    //new CreatePredictedXML();
                }
                new CalculateFScore();
            }
            else if(option == 2)
            {
                if(args[1].compareToIgnoreCase("r") == 0)
                {
                    new PolarityLabelsRestaurantsHotels(option, args[2], "dummyParameter");
                    new SentimentClassifierR(option, args[2], args[3]);
                }
               /* else if(args[1].compareToIgnoreCase("l") == 0)
                {
                    new PolarityLabelsLaptopsMobiles(option, args[2], "dummyParameter");
                    new SentimentClassifierM(option, args[2], args[3]);
                }
                else if(args[1].compareToIgnoreCase("h") == 0)
                {
                    new PolarityLabelsHotels(option, args[2], "dummyParameter");
                    new SentimentClassifierH(option, args[2], args[3]);
                }*/
            }
            else if(option == 3)
            {
                if(args[1].compareToIgnoreCase("r") == 0)
                {
                    new PolarityLabelsRestaurantsHotels(option, args[2], args[3]);
                    new SentimentClassifierR(option, args[2], args[3]);
                    new CreatePredictedXML();
                }
                else if(args[1].compareToIgnoreCase("l") == 0)
                {
                    new PolarityLabelsLaptopsMobiles(option, args[2], args[3]);
                    new SentimentClassifierL(option, args[2], args[3]);
                }
                else if(args[1].compareToIgnoreCase("m") == 0)
                {
                    new PolarityLabelsLaptopsMobiles(option, args[2], args[3]);
                    new SentimentClassifierM(option, args[2], args[3]);
                    new CreatePredictedXML();
                }
                else if(args[1].compareToIgnoreCase("h") == 0)
                {
                    new PolarityLabelsRestaurantsHotels(option, args[2], args[3]);
                    new SentimentClassifierH(option, args[2], args[3]);
                    new CreatePredictedXML();
                }
                /*else if(args[1].compareToIgnoreCase("h") == 0)
                {
                    new PolarityLabelsHotels(option, args[2], args[4]);
                    new SentimentClassifierH(option, args[2], args[3]);
                }*/
                //new CalculateFScore();
                //CalculateFScore fob = new CalculateFScore();
            }
        }
    }

}

