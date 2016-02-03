package sentimentClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krayush on 15-06-2015.
 */
public class EntityFeatureMobiles {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    String rootDirectory;

    EntityFeatureMobiles(String rootDirectory, String trainingFile, String testFile) throws IOException {
        this.rootDirectory = rootDirectory;
        trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();

        trainingFeature = generateFeature(rootDirectory + "\\dataset\\dataset_sentimentClassification\\"+trainingFile);
        if(testFile.compareToIgnoreCase("dummyparameter") != 0){
            testFeature = generateFeature(rootDirectory + "\\dataset\\dataset_sentimentClassification\\"+testFile);
        }

        getTrainingList();
    }

    private List<LinkedHashMap<Integer, Double>> generateFeature(String fileName) throws IOException {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        LinkedHashMap<String, Integer> entityMap = new LinkedHashMap<String, Integer>();
        entityMap.put("PHONE", 1);
        entityMap.put("DISPLAY", 2);
        entityMap.put("BATTERY", 3);
        entityMap.put("CPU", 4);
        entityMap.put("MEMORY", 5);
        entityMap.put("HARD_DISC", 6);
        entityMap.put("POWER_SUPPLY", 7);
        entityMap.put("KEYBOARD", 8);
        entityMap.put("SHIPPING", 9);
        entityMap.put("SUPPORT", 10);
        entityMap.put("COMPANY", 11);
        entityMap.put("PORTS", 12);
        entityMap.put("MULTIMEDIA_DEVICES", 13);
        entityMap.put("HARDWARE", 14);
        entityMap.put("OS", 15);
        entityMap.put("SOFTWARE", 16);
        entityMap.put("WARRANTY", 17);

        entityMap.put("OPERATION_PERFORMANCE", 18);
        entityMap.put("CONNECTIVITY", 19);
        entityMap.put("USABILITY", 20);
        entityMap.put("GENERAL", 21);
        entityMap.put("PRICE", 22);
        entityMap.put("DESIGN_FEATURES", 23);
        entityMap.put("QUALITY", 24);
        entityMap.put("MISCELLANEOUS", 25);




        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            String words[] = line.split("\\|");
            String entity[] = words[4].split("#");
            if (entityMap.containsKey(entity[0])) {
                featureVector.add(count, new LinkedHashMap<Integer, Double>());
                featureVector.get(count).put(entityMap.get(entity[0]), 1.0);
                featureVector.get(count).put(entityMap.get(entity[1]), 1.0);
                count++;
            } else {
                System.out.println("Error: "+words[4]);
            }
        }
        return featureVector;
    }

    public List<LinkedHashMap<Integer, Double>> getTrainingList() {
        //System.out.println(trainingFeature.size());
        /*for(int i=0; i<trainingFeature.size(); i++)
        {
            System.out.println(trainingFeature.get(i));
        }*/
        return this.trainingFeature;
    }

    public List<LinkedHashMap<Integer, Double>> getTestList() {
        //System.out.println(trainingFeature.size());
        return this.testFeature;
    }

    public int getFeatureCount() {
        //System.out.println(trainingFeature.get(0).size());
        return 25;
    }

    /*public static void main(String[] args) throws IOException {
        EntityFeatureRestaurants ef = new EntityFeatureRestaurants("D:\\Course\\Semester VII\\Internship\\aspectCategorizationSemEval2016");
    }*/
}
