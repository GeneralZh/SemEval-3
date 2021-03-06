package sentimentClassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

//package de.tu.darmstadt.lt.ner.util;

public class Ngram {
    List<LinkedHashMap<Integer, Double>> trainingFeature;
    List<LinkedHashMap<Integer, Double>> testFeature;
    LinkedHashMap<String, Integer> ngramMap;

    String rootDirectory;

    int featureCount;

    Ngram(String rootDirectory, String trainFile, String testFile) {
        this.rootDirectory = rootDirectory;
        trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();
        testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();

        LinkedHashMap<String, Integer> indexedNgrams = new LinkedHashMap<String, Integer>();
        ngramMap = new LinkedHashMap<String, Integer>();

        indexedNgrams = generateNgrams();

        trainingFeature = generateFeature(indexedNgrams, rootDirectory + "\\dataset\\tokenized_Train.txt");
        //testFeature = generateFeature(indexedNgrams, rootDirectory + "\\dataset\\tokenized_Test.txt");
        if(testFile.compareToIgnoreCase("dummyparameter")!=0) {
            System.out.println("*******NGram");
            testFeature = generateFeature(indexedNgrams, rootDirectory + "\\dataset\\tokenized_Test.txt");
        }

        //featureCount = 1;
    }

    private LinkedHashMap<String, Integer> generateNgrams() {
        LinkedHashMap<String, Integer> indexedNgram = new LinkedHashMap<String, Integer>();
        try {
            //new BufferedReader(new InputStreamReader(new FileInputStream(rootDirectory + "\\dataset\\tokenized_Train.txt"), "UTF-8"));
            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(rootDirectory + "\\dataset\\tokenized_Train.txt"), "UTF-8"));
            String line = "";
            String text = "";
            int num = 0;
            while ((line = read.readLine()) != null) {
                line = line.toLowerCase();
                line = line.trim();
                text += line + " ";
                num++;
            }

            int count = 1;
            for (int n = 1; n < 2; n++) {

                // Use .isEmpty() instead of .length() == 0
                if (text == null || text.isEmpty()) {
                    throw new IllegalArgumentException("null or empty text");
                }
                String[] words = text.split(" ");
                List<String> list = new ArrayList<String>();

                for (int i = 0; i <= words.length - n; i++) {
                    //StringBuilder keyBuilder = new StringBuilder(words[i].trim());
                    words[i].trim();
                    String ngram = words[i];
                    for (int j = 1; j <= n - 1; j++) {
                        ngram += " " + words[i + j];
                    }
                    //System.out.println(ngram);
                    if (ngramMap.containsKey(ngram)) {
                        ngramMap.put(ngram, ngramMap.get(ngram) + 1);
                    } else {
                        ngramMap.put(ngram, 1);
                        indexedNgram.put(ngram, count++);
                        //System.out.println(ngram+" "+count);
                    }

                    if (indexedNgram.containsKey(ngram)) {
                    } else {
                        indexedNgram.put(ngram, count++);
                    }

                    //************System.out.println(ngram + ": " + count);
                }
            }

            indexedNgram = sortHashMapByValuesD(indexedNgram);


            setFeatureCount(count);

            System.out.println("Func: " + featureCount);

            read.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        return indexedNgram;
    }

    private List<LinkedHashMap<Integer, Double>> generateFeature(LinkedHashMap<String, Integer> indexedNgram, String fileName) {
        List<LinkedHashMap<Integer, Double>> featureVector = new ArrayList<LinkedHashMap<Integer, Double>>();
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String line = "";
            int count = 0;
            //String text = "";

            while ((line = read.readLine()) != null) {
                line = line.toLowerCase();
                line = line.replace("\n", "").replace("\r", "");
                line = line.trim();
                //System.out.println(line);
                //String tokens[] = line.split(" ");
                //trainingFeature.add(count, new LinkedHashMap<Integer, Double>());
                featureVector.add(count, new LinkedHashMap<Integer, Double>());

                //System.out.println(line);

                for (int n = 1; n < 2; n++) {

                    // Use .isEmpty() instead of .length() == 0
                    if (line == null || line.isEmpty()) {
                        throw new IllegalArgumentException("null or empty text");
                    }
                    String[] words = line.split(" ");
                    //List<String> list = new ArrayList<String>();
                    //System.out.println();
                    for (int i = 0; i <= words.length - n; i++) {
                        //StringBuilder keyBuilder = new StringBuilder(words[i].trim());
                        words[i].trim();
                        String ngram = words[i];
                        for (int j = 1; j <= n - 1; j++) {
                            ngram += " " + words[i + j];
                        }
                        //System.out.println(ngram);
                        if (indexedNgram.containsKey(ngram)) {
                            //System.out.println(ngram+":"+ngramMap.get(ngram));
                            //ngramMap.put(ngram, ngramMap.get(ngram) + 1);
                            //trainingFeature.get(count).remove(indexedNgram.get(ngram).intValue());
                            //if(ngramMap.get(ngram)>=2)
                            {
                                featureVector.get(count).put(indexedNgram.get(ngram).intValue(), 1.0);
                            }
                        } /*else {
                            //ngramMap.put(ngram, 1);
                            //indexedNgram.put(ngram, count++);
                            trainingFeature.get(count).put(indexedNgram.get(ngram).intValue(), 0.0);
                        }*/
                    }

                /*PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter("E:\\COURSE\\Semester VII\\Internship\\sentiment\\dataset\\ngram_Train" + n + ".txt")));
                write.println(indexedNgram);
                System.out.println(ngramMap.size());
                write.close();*/
                }
                count++;
            }
            read.close();


            for (int i = 0; i < featureVector.size(); i++)    //Print the feature values in sorted order
            {

                //System.out.println(trainingFeature.get(i));
                LinkedHashMap<Integer, Double> linkedHashMap = new LinkedHashMap<Integer, Double>();
                Map<Integer, Double> sortedMap = new TreeMap<Integer, Double>(featureVector.get(i));
                //System.out.println(sortedMap);

                Set set = sortedMap.entrySet();
                Iterator iterator = set.iterator();
                featureVector.get(i).clear();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    featureVector.get(i).put(Integer.parseInt(mentry.getKey().toString()), Double.parseDouble(mentry.getValue().toString()));
                    //linkedHashMap.put(Integer.parseInt(mentry.getKey().toString()), 1.0);
                    //System.out.print("key is: "+ mentry.getKey() + " ");
                    //System.out.println(mentry.getValue());
                }
            }
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

    /*public static void main(String[] args)
    {
        Ngram ng = new Ngram(System.getProperty("user.dir"));
    }*/
}