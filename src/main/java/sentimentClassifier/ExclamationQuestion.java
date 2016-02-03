package sentimentClassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krayush on 29-01-2016.
 */
public class ExclamationQuestion {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    String rootDirectory;
    int featureCount;

    ExclamationQuestion(String rootDirectory, String trainFile, String testFile) throws IOException {
        this.trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        this.testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        this.rootDirectory = rootDirectory;

        trainingFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Train.txt");
        if(testFile.compareToIgnoreCase("dummyparameter")!=0){
            System.out.println("*******EXQuestion");
            testFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Test.txt");
        }
    }

    private List<LinkedHashMap<Integer, Double>> generateFeature(String fileName) throws IOException {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        BufferedReader readLexicon = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = "";
        int count = 0;
        while ((line = readLexicon.readLine()) != null) {
            String tokens[] = line.split(" ");
            double exFlag = 0.0, qFlag=0.0;
            for(int i=0; i<tokens.length; i++){
                if(tokens[i].contains("!")){
                    exFlag = 1.0;
                }

                if(tokens[i].contains("?")){
                    qFlag = 1.0;
                }
            }

            featureVector.add(count, new LinkedHashMap<Integer, Double>());
            featureVector.get(count).put(1, exFlag);
            featureVector.get(count).put(2, qFlag);

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
        return 2;
    }
}
