package sentimentClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by krayush on 18-06-2015.
 */
public class Tf_IdfFeature {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    LinkedHashMap<String, Double> tfidfPos;
    LinkedHashMap<String, Double> tfidfNeg;
    LinkedHashMap<String, Double> tfidfNeutral;


    String rootDirectory;

    Tf_IdfFeature(String rootDirectory, String trainFile, String testFile) throws IOException {
        this.rootDirectory = rootDirectory;
        trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        tfidfPos = new LinkedHashMap<String, Double>();
        tfidfNeg = new LinkedHashMap<String, Double>();
        tfidfNeutral = new LinkedHashMap<String, Double>();
        //posDoc = new LinkedHashMap<String, Double>();
        //negDoc = new LinkedHashMap<String, Double>();
        //neutralDoc = new LinkedHashMap<String, Double>();

        trainingFeature = generateFeatureTrain(rootDirectory + "\\dataset\\tokenized_Train_Pruned.txt", rootDirectory + "\\dataset\\tokenized_Train_Pruned.txt", rootDirectory + "\\dataset\\trainingLabels.txt");
        if(testFile.compareToIgnoreCase("dummyparameter")!=0) {
            System.out.println("*******Tf-Idf");
        testFeature = generateFeatureTest(rootDirectory + "\\dataset\\tokenized_Test_Pruned.txt");}

    }

    private List<LinkedHashMap<Integer, Double>> generateFeatureTrain(String posFileName, String tokenizedFile, String labelFile) throws IOException {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();

        LinkedHashMap<String, Double> posDoc = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> negDoc = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> neutralDoc = new LinkedHashMap<String, Double>();
        BufferedReader posFile = new BufferedReader(new FileReader(new File(posFileName)));
        BufferedReader polarityFile = new BufferedReader(new FileReader(new File(labelFile)));

        String line, data;

        String pattern = "[%^&#@,;!\\-:\\.'()?<>{}=/]";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        double totalPosWords = 0;
        double totalNegWords = 0;
        double totalNeuWords = 0;

        while ((line = polarityFile.readLine()) != null) {
            String review = posFile.readLine();
            if (line.compareToIgnoreCase("1") == 0) {
                //System.out.println(review);
                String pos[] = review.split(" ");
                for (int i=0; i<pos.length; i++) {
                    Matcher m = r.matcher(pos[i]);
                    if(!m.find()) {
                        totalPosWords++;
                        if (posDoc.containsKey(pos[i])) {
                            posDoc.put(pos[i], posDoc.get(pos[i]) + 1);
                        } else {
                            posDoc.put(pos[i], 1.0);
                        }
                    }
                }
            } else if (line.compareToIgnoreCase("-1") == 0) {
                String neg[] = review.split(" ");
                for (int i=0; i<neg.length; i++) {
                    Matcher m = r.matcher(neg[i]);
                    if(!m.find()) {
                        totalNegWords++;
                        if (negDoc.containsKey(neg[i])) {
                            negDoc.put(neg[i], negDoc.get(neg[i]) + 1);
                        } else {
                            negDoc.put(neg[i], 1.0);
                        }
                    }
                }
            } else if (line.compareToIgnoreCase("0") == 0) {
                String neutral[] = review.split(" ");
                for (int i=0; i<neutral.length; i++) {
                    Matcher m = r.matcher(neutral[i]);
                    if(!m.find()) {
                        totalNeuWords++;
                        if (neutralDoc.containsKey(neutral[i])) {
                            neutralDoc.put(neutral[i], neutralDoc.get(neutral[i]) + 1);
                        } else {
                            neutralDoc.put(neutral[i], 1.0);
                        }
                    }
                }
            }
        }

        posFile.close();
        polarityFile.close();

        /*posDoc = sortHashMapByValuesD(posDoc);
        //System.out.println(posDoc);
        //System.out.println();
        negDoc = sortHashMapByValuesD(negDoc);
        //System.out.println(negDoc);
        //System.out.println();
        neutralDoc = sortHashMapByValuesD(neutralDoc);*/
        //System.out.println(neutralDoc);
        //System.out.println();


        Set set = posDoc.entrySet();
        // Get an iterator
        Iterator iPos = set.iterator();
        // Display elements
        while (iPos.hasNext()) {
            Map.Entry me = (Map.Entry) iPos.next();
            String key = me.getKey().toString();
            int count = 1;
            if (negDoc.containsKey(key)) {
                count++;
            }
            if (neutralDoc.containsKey(key)) {
                count++;
            }

            Double idf = Math.log10(3 / count);
            Double tf = Double.parseDouble(me.getValue().toString())/totalPosWords;

            if(Double.parseDouble(me.getValue().toString()) >= 5){
                tfidfPos.put(key, tf * idf);
            }

            //System.out.print(me.getKey());
            //System.out.println(me.getValue());
        }

        tfidfPos = sortHashMapByValuesD(tfidfPos);
        //System.out.println(tfidfPos);

        Set setNeg = negDoc.entrySet();
        // Get an iterator
        Iterator iNeg = setNeg.iterator();
        // Display elements
        while (iNeg.hasNext()) {
            Map.Entry me = (Map.Entry) iNeg.next();
            String key = me.getKey().toString();
            int count = 1;
            if (posDoc.containsKey(key)) {
                count++;
            }
            if (neutralDoc.containsKey(key)) {
                count++;
            }

            Double idf = Math.log10(3 / count);
            Double tf = Double.parseDouble(me.getValue().toString())/totalNegWords;

            if(Double.parseDouble(me.getValue().toString()) >= 5){
                tfidfNeg.put(key, tf * idf);
            }


            //System.out.print(me.getKey());
            //System.out.println(me.getValue());
        }

        tfidfNeg = sortHashMapByValuesD(tfidfNeg);
        //System.out.println();
        //System.out.println(tfidfNeg);

        Set setNeutral = neutralDoc.entrySet();
        // Get an iterator
        Iterator iNeutral = setNeutral.iterator();
        // Display elements
        while (iNeutral.hasNext()) {
            Map.Entry me = (Map.Entry) iNeutral.next();
            String key = me.getKey().toString();
            int count = 1;
            if (negDoc.containsKey(key)) {
                count++;
            }
            if (posDoc.containsKey(key)) {
                count++;
            }

            Double idf = Math.log10(3 / count);
            Double tf = Double.parseDouble(me.getValue().toString())/totalNeuWords;

            if(Double.parseDouble(me.getValue().toString()) >= 5){
                tfidfNeutral.put(key, tf * idf);
            }


            //System.out.print(me.getKey());
            //System.out.println(me.getValue());
        }

        tfidfNeutral = sortHashMapByValuesD(tfidfNeutral);
        //System.out.println();
        //System.out.println(tfidfNeutral);

        System.out.println(tfidfPos);
        System.out.println(tfidfNeg);
        System.out.println(tfidfNeutral);

        int count = 0;
        BufferedReader dataFile1 = new BufferedReader(new FileReader(new File(tokenizedFile)));

        while ((line = dataFile1.readLine()) != null) {
            double pos = 0.0, neg = 0.0, neutral = 0.0;
            String words[] = line.split(" ");
            for (String word : words) {
                if (tfidfPos.containsKey(word) && tfidfPos.get(word)!=0) {
                    //pos += tfidfPos.get(word);
                    pos++;
                }

                if (tfidfNeg.containsKey(word)&& tfidfNeg.get(word)!=0) {
                    //neg += tfidfNeg.get(word);
                    neg++;
                }

                if (tfidfNeutral.containsKey(word) && tfidfNeutral.get(word)!=0) {
                    //neutral += tfidfNeutral.get(word);
                    neutral++;
                }
            }

            featureVector.add(count, new LinkedHashMap<Integer, Double>());


            featureVector.get(count).put(1, pos);
            featureVector.get(count).put(2, neg);
            //featureVector.get(count).put(3, neutral);
            //System.out.println(featureVector.get(count));
            count++;

        }

        return featureVector;
    }

    private List<LinkedHashMap<Integer, Double>> generateFeatureTest(String fileName) throws IOException {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        int count = 0;
        String line;
        BufferedReader dataFile1 = new BufferedReader(new FileReader(new File(fileName)));

        while ((line = dataFile1.readLine()) != null) {
            double pos = 0.0, neg = 0.0, neutral = 0.0;
            String words[] = line.split(" ");
            for (String word : words) {
                if (tfidfPos.containsKey(word) && tfidfPos.get(word)!=0) {
                    //pos += tfidfPos.get(word);
                    pos++;
                }

                if (tfidfNeg.containsKey(word)&& tfidfNeg.get(word)!=0) {
                    //neg += tfidfNeg.get(word);
                    neg++;
                }

                if (tfidfNeutral.containsKey(word) && tfidfNeutral.get(word)!=0) {
                    //neutral += tfidfNeutral.get(word);
                    neutral++;
                }
            }

            featureVector.add(count, new LinkedHashMap<Integer, Double>());


            featureVector.get(count).put(1, pos);
            featureVector.get(count).put(2, neg);
            //featureVector.get(count).put(3, neutral);
            System.out.println(featureVector.get(count));
            count++;

        }

        return featureVector;
    }

    public List<LinkedHashMap<Integer, Double>> getTrainingList() {
        //System.out.println(trainingFeature.size());
        return this.trainingFeature;
    }

    public List<LinkedHashMap<Integer, Double>> getTestList() {
        //System.out.println(trainingFeature.size());
        return this.testFeature;
    }

    public int getFeatureCount() {
        //System.out.println(trainingFeature.get(0).size());
        return trainingFeature.get(0).size();
    }

    private LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    /*public static void main(String[] args) throws IOException {
        Tf_IdfFeature ob = new Tf_IdfFeature(System.getProperty("user.dir"));
    }*/

}
