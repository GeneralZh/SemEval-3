package aspectCategorizationSemEval2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krayush on 12-01-2016.
 */
public class TfIdfDTFeature {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    String rootDirectory;

    int featureCount;

    TfIdfDTFeature(String rootDirectory, String ddgFile) throws IOException {
        this.rootDirectory = rootDirectory;
        trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();

        //LinkedHashMap<String, Integer> indexedNgrams = new LinkedHashMap<String, Integer>();
        //indexedNgrams = generateNgrams();

        trainingFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Train.txt", rootDirectory + "\\resources\\tfIdf\\Restaurants_EnglishDT.txt");
        testFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Test.txt", rootDirectory + "\\resources\\tfIdf\\Restaurants_EnglishDT.txt");

        //featureCount = 1;
    }


    private List<LinkedHashMap<Integer, Double>> generateFeature(String fileName, String tfIdfFile) throws IOException {
        HashMap<String, String> tfIdfHash = new HashMap<String, String>();
        BufferedReader ddg = new BufferedReader(new FileReader(new File(tfIdfFile)));
        String line = "";
        int count = 1;

        while ((line = ddg.readLine()) != null) {
            line = line.toLowerCase();
            String tokens[] = line.split("\\t");
            for (int i = 0; i < tokens.length; i++) {
                if (!tfIdfHash.containsKey(tokens[i])) {
                    tfIdfHash.put(tokens[i], count + "");
                } else {
                    tfIdfHash.put(tokens[i], tfIdfHash.get(tokens[i]) + "\t" + count);
                }
            }
            count++;
        }

        featureCount = count-1;

        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        BufferedReader read = new BufferedReader(new FileReader(new File(fileName)));
        line = "";
        count = 0;
        //int length=0;
        //String text = "";

        while ((line = read.readLine()) != null) {
            line = line.toLowerCase();
            String tokens[] = line.split(" ");
            featureVector.add(count, new LinkedHashMap<Integer, Double>());
            //System.out.println(line);
            for (int i = 0; i < tokens.length; i++) {
                if (tfIdfHash.containsKey(tokens[i])) {
                    //System.out.println(tokens[i]);
                    //System.out.println(tfIdfHash.get(tokens[i]));
                    String cluster[] = tfIdfHash.get(tokens[i]).split("\t");
                    for (int j = 0; j < cluster.length; j++) {
                        int cid = Integer.parseInt(cluster[j]);
                        if (featureVector.get(count).containsKey(cid)) {
                            featureVector.get(count).put(cid, featureVector.get(count).get(cid) + 1.0);
                        } else {
                            featureVector.get(count).put(cid, 1.0);
                        }
                    }
                }
            }
            //System.out.println(featureVector.get(count));
            count++;
        }
        return featureVector;
    }

    public List<LinkedHashMap<Integer, Double>> getTrainingList() {
        return this.trainingFeature;
    }

    public List<LinkedHashMap<Integer, Double>> getTestList() {
        return this.testFeature;
    }

    public int getFeatureCount() {
        //System.out.println(featureCount);
        return featureCount;
    }
}
