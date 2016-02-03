package aspectCategorizationSemEval2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by krayush on 12-07-2015.
 */
public class DDGFeature {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    String rootDirectory;

    int featureCount;

    DDGFeature(String rootDirectory, String trainFile, String testFile, String ddgFile) throws IOException {
        this.rootDirectory = rootDirectory;
        trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();

        //LinkedHashMap<String, Integer> indexedNgrams = new LinkedHashMap<String, Integer>();
        //indexedNgrams = generateNgrams();

        trainingFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Train_Pruned.txt", rootDirectory + "\\resources\\ddgFeatures\\" + ddgFile);
        testFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Test_Pruned.txt", rootDirectory + "\\resources\\ddgFeatures\\" + ddgFile);

        //featureCount = 1;
    }


    private List<LinkedHashMap<Integer, Double>> generateFeature(String fileName, String ddgFile) throws IOException {
        List<HashMap<String, Integer>> ddgHash = new ArrayList<HashMap<String, Integer>>();
        BufferedReader ddg = new BufferedReader(new FileReader(new File(ddgFile)));
        String line = "";
        int count = 0;

        HashSet<String> wordFlagHash = new HashSet<String>();

        while ((line = ddg.readLine()) != null) {
            line = line.toLowerCase();
            String tokens[] = line.split("\\t");
            ddgHash.add(count, new HashMap<String, Integer>());
            for (int i = 0; i < tokens.length; i++) {
                ddgHash.get(count).put(tokens[i], count);
                wordFlagHash.add(tokens[i]);
            }
            count++;
        }

        featureCount = count;

        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        BufferedReader read = new BufferedReader(new FileReader(new File(fileName)));
        line = "";
        count = 0;
        //int length=0;
        //String text = "";

        while ((line = read.readLine()) != null) {
            //line = line.replace("\n", "").replace("\r", "");
            //System.out.println(line);
            line = line.toLowerCase();
            String tokens[] = line.split(" ");

            featureVector.add(count, new LinkedHashMap<Integer, Double>());
            for (int i = 0; i < tokens.length; i++) {
                if (wordFlagHash.contains(tokens[i])) {
                    for (int j = 0; j < ddgHash.size(); j++) {
                        if (ddgHash.get(j).containsKey(tokens[i])) {
                            int cid = ddgHash.get(j).get(tokens[i])+1;
                            if (featureVector.get(count).containsKey(cid)) {
                                //featureVector.get(count).put(cid, featureVector.get(count).get(cid) + 1.0);
                            } else {
                                featureVector.get(count).put(cid, 1.0);
                            }
                        }
                    }
                }
            }
            //System.out.println();
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

    /*public static void main(String[] args) throws IOException {
        ABSAEVRestaurantsFeature ob = new ABSAEVRestaurantsFeature("D:\\Course\\Semester VII\\Internship\\sentiment");
        //ob.getFeatureCount();
    }*/
}
