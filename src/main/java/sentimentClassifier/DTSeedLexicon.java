package sentimentClassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by krayush on 23-07-2015.
 */
public class DTSeedLexicon {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    String rootDirectory;
    int featureCount;

    DTSeedLexicon(String rootDirectory, String trainFile, String testFile) throws IOException {
        this.trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        this.testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        this.rootDirectory = rootDirectory;

        trainingFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Train.txt");
        if(testFile.compareToIgnoreCase("dummyparameter")!=0){
            System.out.println("********* DTSeedLexicon");
            testFeature = generateFeature(rootDirectory + "\\dataset\\tokenized_Test.txt");
        }
    }

    private List<LinkedHashMap<Integer, Double>> generateFeature(String fileName) throws IOException {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        LinkedHashMap<String, Double> lexicon = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> dtPos = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> dtNeg = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> dtNeu = new LinkedHashMap<String, Double>();
        try {
            BufferedReader readLexicon = new BufferedReader(new InputStreamReader(new FileInputStream(rootDirectory + "\\resources\\lexicons\\ar_sentimentSeedWords.txt"), "UTF-8"));
            String line = "";
            while ((line = readLexicon.readLine()) != null) {
                line = line.toLowerCase();
                String tokens[] = line.split("\\|");
                if (lexicon.containsKey(tokens[0])) {
                    //System.out.println("DTSeedError: ");
                } else {
                    if(tokens[1].compareToIgnoreCase("-1")==0){
                        dtNeg.put(tokens[0], -1.0);
                    } else if(tokens[1].compareToIgnoreCase("1")==0){
                        dtPos.put(tokens[0], 1.0);
                    } else if(tokens[1].compareToIgnoreCase("0")==0){
                        dtNeu.put(tokens[0], 0.0);
                    }

                    lexicon.put(tokens[0], Double.parseDouble(tokens[1]));
                    //dtPos.put(tokens[0], Double.parseDouble(tokens[2]));
                    //dtNeg.put(tokens[0], Double.parseDouble(tokens[1]));
                }
            }
            readLexicon.close();

            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            int count = 0;
            int p = 0;
            int n = 0;
            int ne = 0;
            //String text = "";

            while ((line = read.readLine()) != null) {
                String tokens[] = line.split(" ");
                double pos = 0, neg = 0, neu = 0;
                double strongPos = 0, strongNeg = 0;
                double totalPos = 0, totalNeg = 0;
                double posScoreDT = 0, negScoreDT = 0;
                double posScoreCOOC = 0, negScoreCOOC = 0;
                for (int i = 0; i < tokens.length; i++) {
                    double val = -5;  //Dummy value not in between -1 to 1
                    double p1;
                    double p2;
                    String token = tokens[i].toLowerCase();
                    if (lexicon.containsKey(token)) {
                        //val = lexicon.get(token);
                        /*totalPos += dtPos.get(token);
                        totalNeg += dtNeg.get(token);

                        p1 = dtPos.get(token);
                        p2 = dtNeg.get(token);

                        if(p1>=0.8){
                            pos++;
                        }
                        if(p2>=0.8){
                            neg++;
                        }*/
                        if(dtPos.containsKey(token)){
                            pos++;
                        }else if(dtNeg.containsKey(token)){
                            neg++;
                        }else if(dtNeu.containsKey(token)){
                            neu++;
                        }
                    }
                }
                featureVector.add(count, new LinkedHashMap<Integer, Double>());
                //featureVector.get(count).put(1, totalPos);
                //featureVector.get(count).put(2, totalNeg);
                featureVector.get(count).put(1, pos);
                featureVector.get(count).put(2, neg);
                //featureVector.get(count).put(3, neu);
                //featureVector.get(count).put(5, totalPos-totalNeg);
                //featureVector.get(count).put(3, pos-neg);
                /*featureVector.get(count).put(1, posScoreDT);
                featureVector.get(count).put(2, negScoreDT);
                featureVector.get(count).put(3, posScoreCOOC);
                featureVector.get(count).put(4, negScoreCOOC);
                featureVector.get(count).put(5, totalDT);
                featureVector.get(count).put(6, totalCOOC);*/

                //System.out.println(totalPos+"\t"+totalNeg);
                //System.out.println(featureVector.get(count));
                featureCount = featureVector.get(count).size();
                count++;
            }
            //System.out.println(p + " " + n + " " + ne);
            read.close();
        } catch (IOException e) {
            System.out.println(e);
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

    private void setFeatureCount(int count) {
        this.featureCount = count;
    }

    /*public static void main(String[] args) throws IOException {
        DTSeedLexicon ob = new DTSeedLexicon("D:\\Course\\Semester VII\\SemEval16\\absaSemEval", "xyz", "dummyparameter");
    }*/
}
