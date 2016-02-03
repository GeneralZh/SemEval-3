package aspectCategorizationSemEval2016;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by krayush on 02-Jun-15.
 */
public class ABSAPolarityRestaurants {

    String rootDirectory;
    LinkedHashMap<String, Integer> polarity;

    ABSAPolarityRestaurants(int option, String trainFileName, String testFileName, String pairFile) throws IOException {
        rootDirectory = System.getProperty("user.dir");
        polarity = new LinkedHashMap<String, Integer>();
        BufferedReader pairs = new BufferedReader(new FileReader(new File(rootDirectory + "\\dataset\\dataset_aspectCategorization\\" + pairFile)));
        String line;
        int count = 1;
        while ((line = pairs.readLine()) != null) {
            line = line.trim();
            polarity.put(line, count);
            count++;
        }
        //System.out.println(count);
        mainFunction(option, trainFileName, testFileName);
        getIdentityWords();
    }

    private void generateTestLabels(String dataFile, String labelFile) {
        try {
            BufferedReader read = new BufferedReader(new FileReader(new File(dataFile)));
            //BufferedReader pairs = new BufferedReader(new FileReader(new File(rootDirectory + "\\dataset\\dataset_aspectCategorization\\"+pairFile)));
            String line = null;
            PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(labelFile)));

            //LinkedHashMap<String, Integer> polarity = new LinkedHashMap<String, Integer>();

            while ((line = read.readLine()) != null) {
                line = line.replace("\n", "").replace("\r", "");
                String words[] = line.split("\\|");
                //System.out.println(words[5]+polarity.get(words[5]));
                if (!polarity.containsKey(words[5])) {
                    write.println(1000);
                } else {
                    System.out.println(polarity.get(words[5]));
                    write.println(polarity.get(words[5]));
                }
            }
            read.close();
            write.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void generateTrainingLabels(String dataFile, String labelFile) {
        try {
            BufferedReader read = new BufferedReader(new FileReader(new File(dataFile)));
            String line = null;
            PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(labelFile)));

            while ((line = read.readLine()) != null) {
                line = line.replace("\n", "").replace("\r", "");
                String words[] = line.split("\\|");
                //System.out.println(words[0]);
                if(!polarity.containsKey(words[5])){
                    System.out.println(words[5]);
                }
                write.println(polarity.get(words[5]));
            }
            read.close();
            write.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void getIdentityWords() throws IOException {
        BufferedReader tokenFile = new BufferedReader(new FileReader(new File(rootDirectory + "\\dataset\\tokenized_Train_Pruned.txt")));
        BufferedReader labelFile = new BufferedReader(new FileReader(new File(rootDirectory + "\\dataset\\trainingLabels.txt")));

        PrintWriter tfIdfFile = new PrintWriter(new BufferedWriter(new FileWriter(rootDirectory+"\\resources\\tfIdf\\Restaurants_English.txt")));

        List<LinkedHashMap<String, Double>> wordMap = new ArrayList<LinkedHashMap<String, Double>>();
        List<LinkedHashMap<Integer, Double>> totalDocumentCount = new ArrayList<LinkedHashMap<Integer, Double>>();
        HashMap<String, Double> wordCount = new LinkedHashMap<String, Double>();

        for (int i = 0; i < polarity.size(); i++) {
            wordMap.add(i, new LinkedHashMap<String, Double>());
            totalDocumentCount.add(i, new LinkedHashMap<Integer, Double>());
            totalDocumentCount.get(i).put(i, 0.0);
        }

        System.out.println("Label Size: "+polarity.size());


        String line;

        String pattern = "[%^&#@,;!\\-:\\.'()?<>{}=/]";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        // Now create matcher object.
        //Matcher m = r.matcher(line);

        while ((line = tokenFile.readLine()) != null) {
            //System.out.println(labelFile.readLine());
            String l = labelFile.readLine();
            //System.out.println(l);
            //System.out.println(l);
            int label = Integer.parseInt(l) - 1;
            line = line.toLowerCase();
            String tokens[] = line.split(" ");
            //System.out.println(line);
            for (int i = 0; i < tokens.length; i++) {
                Matcher m = r.matcher(tokens[i]);
                if (!m.find()) {
                    totalDocumentCount.get(label).put(label, 1 + totalDocumentCount.get(label).get(label));
                    if (wordCount.containsKey(tokens[i])) {
                        wordCount.put(tokens[i], 1 + wordCount.get(tokens[i]));
                        if (wordMap.get(label).containsKey(tokens[i])) {
                            //System.out.println("ubbjbj");
                            //System.out.println(label+", "+wordMap.get(label).size());
                            wordMap.get(label).put(tokens[i], 1 + wordMap.get(label).get(tokens[i]));
                        } else {
                            wordMap.get(label).put(tokens[i], 1.0);
                            //totalDocumentCount.get(label).put(label, 1.0);
                        }
                    } else {
                        wordCount.put(tokens[i], 1.0);
                        wordMap.get(label).put(tokens[i], 1.0);
                        //totalDocumentCount.get(label).put(label, 1.0);
                    }
                }
            }
        }

        Writer writer = new OutputStreamWriter(new FileOutputStream(rootDirectory + "\\resources\\tfIdf\\Restaurants_EnglishDT.txt"), "UTF-8");
        BufferedWriter fout = new BufferedWriter(writer);

        for (int i = 0; i < wordMap.size(); i++)    //Print the feature values in sorted order
        {
            //System.out.println(totalDocumentCount.get(i));
            //LinkedHashMap<String, Double> sortedWordMap = new LinkedHashMap<String, Double>();
            //sortedWordMap = sortHashMapByValuesD(wordMap.get(i));
            //System.out.println(sortedWordMap);
            //System.out.println(wordMap.get(i));
            LinkedHashMap<String, Double> tfIdfMap = new LinkedHashMap<String, Double>();

            Set set = wordMap.get(i).entrySet();
            Iterator iterator = set.iterator();
            //featureVector.get(i).clear();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                //featureVector.get(i).put(Integer.parseInt(mentry.getKey().toString()), Double.parseDouble(mentry.getValue().toString()));
                //linkedHashMap.put(Integer.parseInt(mentry.getKey().toString()), 1.0);
                //System.out.print("key is: "+ mentry.getKey() + " ");
                //System.out.println(mentry.getValue());
                String word = mentry.getKey().toString();
                /*if(word.compareToIgnoreCase("+")==0){
                    System.out.println(mentry.getValue().toString());
                    System.out.println(totalDocumentCount.get(i).get(i));
                }*/
                if (Double.parseDouble(mentry.getValue().toString())>=3) {
                    double tf = Double.parseDouble(mentry.getValue().toString()) / totalDocumentCount.get(i).get(i);
                    double countDoc = 0;
                    for (int j = 0; j < wordMap.size(); j++) {
                        if (wordMap.get(j).containsKey(mentry.getKey())) {
                            countDoc++;
                        }
                    }
                    double score = tf * Math.log10(wordMap.size() / countDoc);
                    //if(score != 0 && (Double.parseDouble(mentry.getValue().toString())>=5 || score >= 0.01)){
                        tfIdfMap.put(mentry.getKey().toString(), score * -1);
                    //}
                }
            }

            tfIdfMap = sortHashMapByValuesD(tfIdfMap);
            System.out.println(tfIdfMap);
            tfIdfMap = sortHashMapByValuesD(tfIdfMap);
            set = tfIdfMap.entrySet();
            iterator = set.iterator();
            //featureVector.get(i).clear();

            int counter=1;
            while (iterator.hasNext() && counter<=5) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                //System.out.println(mentry.getKey()+", "+mentry.getValue());
                if(mentry.getValue().toString().compareToIgnoreCase("-0.0") != 0){
                    tfIdfFile.print(mentry.getKey()+"\t");


                    try{
                    URL oracle = new URL("http://maggie.lt.informatik.tu-darmstadt.de:10080/jobim/ws/api/frenchTrigram/jo/similar/" + mentry.getKey() + "?numberOfEntries=10&format=tsv");
                    URLConnection yc = oracle.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
                    String inputLine;
                    int flag = 0;
                    //Writer writer = new OutputStreamWriter(new FileOutputStream(rootDirectory + "\\resources\\DTExpansion\\HTTPResults\\" + count + ".txt"), "UTF-8");
                    while ((inputLine = in.readLine()) != null) {
                        //System.out.println(inputLine);
                        String words[] = inputLine.split("\\t");
                /*if (words.length >= 2) {
                    fout.write(words[0] + "\t" + words[1] + "\n");
                }*/
                        if (words.length >= 2 && words[0].compareToIgnoreCase("# term")!=0) {
                            //flag=1;
                            fout.write(words[0].toLowerCase() + "\t");
                        }
                    }
                    //count++;
                }catch(IOException e){
                    System.out.println(e);
                }}
                counter++;
            }
            fout.write("\n");
            tfIdfFile.println();
        }
        fout.close();
        tfIdfFile.close();
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

    private void mainFunction(int option, String trainingFileName, String testFileName) {

        //String rootDirectory = "D:\\Course\\Semester VII\\Internship\\sentiment\\indian";
        //System.out.println(args.length);
        //String rootDirectory = System.getProperty("user.dir");;
        //SetPolarityLabels this = new SetPolarityLabels();
        this.generateTrainingLabels(rootDirectory + "\\dataset\\dataset_aspectCategorization\\" + trainingFileName, rootDirectory + "\\dataset\\trainingLabels.txt");
        if (option == 1) {
            this.generateTestLabels(rootDirectory + "\\dataset\\dataset_aspectCategorization\\" + trainingFileName, rootDirectory + "\\dataset\\testLabels.txt");
        } else if (option == 2) {

        } else if (option == 3) {
            //this.generateTestLabels(rootDirectory + "\\dataset\\dataset_aspectCategorization\\" + testFileName, rootDirectory + "\\dataset\\testLabels.txt", pairFile);
            //this.generateTestLabels(rootDirectory + "\\dataset\\Gold Set\\HI_Test_Gold.txt", rootDirectory + "\\dataset\\testLabels.txt");
        }

    }

}
