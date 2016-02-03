package lexiconConstruction;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by krayush on 29-06-2015.
 */
public class GetDTTopCandidateWordsDutch {

    String rootDirectory;

    GetDTTopCandidateWordsDutch() throws IOException {
        rootDirectory = System.getProperty("user.dir");
        mainFunction(rootDirectory);
    }

    //public static void main(String[] args)throws IOException {
    private void mainFunction(String rootDirectory) throws IOException {
        //String rootDirectory = "D:\\Course\\Semester VII\\Internship\\sentiment\\indian";
        //Writer writer = new OutputStreamWriter(new FileOutputStream(rootDirectory+"\\resources\\DTExpansion\\HTTPResults\\topModifiedWords.txt"), "UTF-8");
        //BufferedWriter fout = new BufferedWriter(writer);
        LinkedHashMap<String, Double> wordCount = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> wordPolarity = new LinkedHashMap<String, Double>();

        File wordFile = new File(rootDirectory + "\\resources\\sentimentSeedWordsDutch.txt");
        //PrintWriter writer = new PrintWriter("D:\\Course\\Semester VII\\Internship\\Results\\Maggie TUD\\sentimentJavaWords.txt");
        BufferedReader wordReader = new BufferedReader(new InputStreamReader(new FileInputStream(wordFile), "UTF-8"));

        LinkedHashMap<String, Double> polarityRoot = new LinkedHashMap<String, Double>();
        LinkedHashMap<Integer, String> wordIndex = new LinkedHashMap<Integer, String>();
        String line = null;
        int index = 1;          //INDEX_ID+WORD in hashmap to encounter repetitive match of seed word
        while ((line = wordReader.readLine()) != null) {
            String values[] = line.split("\\|");
            if (polarityRoot.containsKey(values[0])) {
                polarityRoot.put(index + values[0], polarityRoot.get(values[0]) + Double.parseDouble(values[1]));
                System.out.println(values[0]);
            } else {
                polarityRoot.put(index + values[0], Double.parseDouble(values[1]));
            }
            wordIndex.put(index, index + values[0]);
            index++;
        }

        //System.out.println(polarityRoot.size());

        /* NORMALIZING DATA STORE */

        LinkedHashMap<String, Double> wordPosCount = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> wordNegCount = new LinkedHashMap<String, Double>();
        double posExpansion = 0.0;
        double negExpansion = 0.0;

        /*************/

        int num = 1;
        while (num <= wordIndex.size()) {
            //System.out.println("Reading file number: " + num);
            File fR = new File(rootDirectory + "\\resources\\DTExpansion\\HTTPResults\\" + num + ".txt");
            //PrintWriter writer = new PrintWriter("D:\\Course\\Semester VII\\Internship\\Results\\Maggie TUD\\sentimentJavaWords.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fR), "UTF-8"));


            int lineCount = 1;
            String root = wordIndex.get(num);
            Double val = 0.0;
            val = polarityRoot.get(wordIndex.get(num));
            //System.out.println(wordIndex.get(num));
            //System.out.println(val);
            while ((line = bf.readLine()) != null) {
                line = line.trim();


                //Double val = polarityRoot.get(root);
                //System.out.println(val);

                /*WORD COUNT OF EXPANSION*/

                if (lineCount > 2) {
                    if (lineCount <= 50) {
                        String[] tokens = line.split("\\t");
                        //System.out.println(tokens[1]);
                        if (wordCount.containsKey(tokens[0])) {
                            wordCount.put(tokens[0], wordCount.get(tokens[0]) + 1);
                        } else {
                            wordCount.put(tokens[0], 1.0);
                        }

                        /*NORM*/
                        if (val > 0) {
                            if (wordPosCount.containsKey(tokens[0])) {
                                wordPosCount.put(tokens[0], wordPosCount.get(tokens[0]) + 1);
                            } else {
                                wordPosCount.put(tokens[0], 1.0);
                            }
                        } else if (val < 0) {
                            if (wordNegCount.containsKey(tokens[0])) {
                                wordNegCount.put(tokens[0], wordNegCount.get(tokens[0]) + 1);
                            } else {
                                wordNegCount.put(tokens[0], 1.0);
                            }
                        }



                        /*WORD POLARITY OF EXPANSION*/

                        if (wordPolarity.containsKey(tokens[0])) {
                            //System.out.println(wordPolarity.get(tokens[0]));
                            wordPolarity.put(tokens[0], wordPolarity.get(tokens[0]) + val);
                        }                      // else if (!wordIndex.containsKey(tokens[0])) {   ---- ?? Maybe not to include words already in seed set
                        else {
                            //System.out.println(polarityRoot.get(root));
                            //System.out.println(val);
                            wordPolarity.put(tokens[0], val);
                        }
                    }
                }
                lineCount++;
                //fout.write(line);
                //System.out.println(line);
            }
            if (lineCount <= 5) {
                System.out.println("No results");
            } else {
                if (val > 0) {
                    posExpansion++;
                } else {
                    negExpansion++;
                }
            }
            num++;
            bf.close();
        }

        System.out.println("Pos Expansion: " + posExpansion + ", Neg Expansion: " + negExpansion);
        //System.out.println(wordPolarity.size());

        LinkedHashMap<String, Double> modifiedFrequency = new LinkedHashMap<String, Double>();
        Writer countWriter = new OutputStreamWriter(
                new FileOutputStream(rootDirectory + "\\resources\\DTExpansion\\DTPolarity\\valuesDT.txt"), "UTF-8");
        BufferedWriter cfout = new BufferedWriter(countWriter);

        Writer countWriter1 = new OutputStreamWriter(
                new FileOutputStream(rootDirectory + "\\resources\\DTExpansion\\DTPolarity\\valuesDTNormalized.txt"), "UTF-8");
        BufferedWriter cfout1 = new BufferedWriter(countWriter1);

        Iterator iPos = wordCount.entrySet().iterator();
        // Display elements
        //Double min=Double.parseDouble(((Map.Entry) iPos.next()).getValue().toString());
        //Double max=0.0;
        int count = 1;
        while (iPos.hasNext()) {
            Map.Entry me = (Map.Entry) iPos.next();
            String key = me.getKey().toString();
            Double val = Double.parseDouble(me.getValue().toString());
            System.out.println("# " + count);
            count++;
            /*String url = URLEncoder.encode(key, "UTF-8");
            URL oracle = new URL("http://maggie.lt.informatik.tu-darmstadt.de:10080/jobim/ws/api/dutchTrigram/jo/count/" + url + "?format=tsv");
            URLConnection yc = oracle.openConnection();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
                String inputLine, prev = "";
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    //fout.write(inputLine+"\n");
                    prev = inputLine;
                }

                double corpusCount = Double.parseDouble(prev);
                modifiedFrequency.put(key, (val / corpusCount) * 10000.00);
                cfout.write(key + "|" + modifiedFrequency.get(key) + "|" + wordCount.get(key) + "|" + wordPolarity.get(key) + "\n");
                //System.out.println(corpusCount);

                in.close();

                /*NORM*/
                /*cfout1.write(key + "|" + corpusCount + "|" + wordCount + "|" + wordPosCount.get(key) + "|" + wordNegCount.get(key) + "\n");
            } catch (IOException e) {
                System.out.println(e);
            }
            //fout.write(key+": "+val+"\n");*/
            cfout1.write(key + "|" + "C#" + "|" + wordCount.get(key) + "|" + wordPosCount.get(key) + "|" + wordNegCount.get(key) + "\n");

        }

        System.out.println("Pos Expansion: " + posExpansion + ", Neg Expansion: " + negExpansion);
        cfout.close();
        cfout1.close();
    }
}
