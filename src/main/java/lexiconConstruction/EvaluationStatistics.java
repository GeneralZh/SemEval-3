package lexiconConstruction;

import java.io.*;
import java.util.HashMap;

/**
 * Created by krayush on 31-12-2015.
 */
public class EvaluationStatistics {
    public static void main(String[] args)throws IOException{
        String rootDirectory = System.getProperty("user.dir");
        String dtFile = args[0];
        String seedFile = args[1];

        BufferedReader dtReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rootDirectory+"\\resources\\DTExpandedLexicon\\"+dtFile)), "UTF-8"));
        BufferedReader seedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rootDirectory+"\\resources\\seedCorpus\\"+seedFile)), "UTF-8"));

        HashMap<String, Double>seedValues = new HashMap<String, Double>();
        String line;

        while((line = seedReader.readLine())!= null){
            String tokens[] = line.split("\\|");
            seedValues.put(tokens[0], Double.parseDouble(tokens[1]));
        }

        double totalFound = 0.0, totalMatch = 0.0;
        double posFound = 0.0, negFound = 0.0, neuFound = 0.0, posMatch = 0.0, negMatch = 0.0, neuMatch = 0.0;
        double posNeu = 0.0, negNeu=0.0;

        while((line = dtReader.readLine())!= null){
            String tokens[] = line.split("\\|");
            if(seedValues.containsKey(tokens[0])){
                double val = Double.parseDouble(tokens[1]);
                double seedVal = seedValues.get(tokens[0]);

                if(seedVal > 0){
                    posFound++;
                    if(seedVal == val){
                        posMatch++;
                    } /*else if(val == 0){
                        posNeu++;
                    }*/
                }else if(seedVal < 0){
                    negFound++;
                    if(seedVal == val){
                        negMatch++;
                    } /*else if(val == 0){
                        negNeu++;
                    }*/
                } else {
                    neuFound++;
                    if(seedVal == val){
                        neuMatch++;
                    }
                }
            }
        }

        totalFound = posFound+negFound+negFound;
        totalMatch = posMatch+negMatch+neuMatch;

        //System.out.println("Total Found: "+totalFound+", Total Match: "+totalMatch+", Overall Accuracy: "+(totalMatch/totalFound));
        System.out.println("Pos,Neg: Found: "+(posFound+negFound)+", Match: "+(posMatch+negMatch)+", Accuracy: "+(posMatch+negMatch)/(posFound+negFound));
        System.out.println("Pos Found: "+posFound+", Pos Match: "+posMatch+", Pos Accuracy: "+(posMatch/posFound));
        System.out.println("Neg Found: "+negFound+", Neg Match: "+negMatch+", Neg Accuracy: "+(negMatch/negFound));
        System.out.println("Neu Found: "+neuFound+", Neu Match: "+neuMatch+", Neu Accuracy: "+(neuMatch/neuFound));

        System.out.println(posNeu+", "+negNeu);

        dtReader.close();
        seedReader.close();
    }
}
