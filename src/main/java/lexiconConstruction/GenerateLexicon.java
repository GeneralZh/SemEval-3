package lexiconConstruction;

import java.io.*;
import java.util.*;

/**
 * Created by krayush on 31-12-2015.
 */
public class GenerateLexicon {
    public static void main(String[] args)throws IOException{
        String rootDirectory = System.getProperty("user.dir");
        String inpFile = args[0];
        double posTh = Double.parseDouble(args[2]);
        double negTh = Double.parseDouble(args[3]);
        double neuTh = Double.parseDouble(args[4]);
        String outFile = args[1]+posTh+"_"+negTh+"_"+neuTh+".txt";

        BufferedReader inpReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rootDirectory+"\\resources\\DTExpandedLexiconWithoutThreshold\\"+inpFile)), "UTF-8"));
        BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(rootDirectory+"\\resources\\DTExpandedLexicon\\"+outFile)), "UTF-8"));
        BufferedWriter outWriter1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(rootDirectory+"\\resources\\DTExpandedLexicon\\"+"fr_neu.txt")), "UTF-8"));

        String line;
        HashMap<String, Double> neutralPolarityValues = new HashMap<String, Double>();
        HashMap<String, Double> polarityValues = new HashMap<String, Double>();

        double posExpansion = 1543;
        double negExpansion = 1262;
        double neuExpansion = 5063;
        double totalExpansion = posExpansion+negExpansion+neuExpansion;

        HashMap<String, Double> posPolarityValues = new HashMap<String, Double>();
        HashMap<String, Double> negPolarityValues = new HashMap<String, Double>();
        HashMap<String, Double> neuPolarityValues = new HashMap<String, Double>();
        HashMap<String, Double> corpusCountValue = new HashMap<String, Double>();
        HashMap<String, Double> totalCountValue = new HashMap<String, Double>();
        HashSet<String> wordHash = new HashSet<String>();

        BufferedWriter outWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(rootDirectory+"\\resources\\DTExpandedLexiconWithoutThreshold\\Lowercase_"+inpFile)), "UTF-8"));
        while((line = inpReader.readLine()) != null) {
            String tokens[] = line.split("\\|");
            String word = tokens[0].toLowerCase();
            if(!wordHash.contains(word)){
                wordHash.add(word);
                corpusCountValue.put(word, Double.parseDouble(tokens[1]));
                totalCountValue.put(word, Double.parseDouble(tokens[2]));
                posPolarityValues.put(word, Double.parseDouble(tokens[3]));
                negPolarityValues.put(word, Double.parseDouble(tokens[4]));
                neuPolarityValues.put(word, Double.parseDouble(tokens[5]));
            } else {
                corpusCountValue.put(word, corpusCountValue.get(word)+Double.parseDouble(tokens[1]));
                totalCountValue.put(word, totalCountValue.get(word)+Double.parseDouble(tokens[2]));
                posPolarityValues.put(word, posPolarityValues.get(word)+Double.parseDouble(tokens[3]));
                negPolarityValues.put(word, negPolarityValues.get(word)+Double.parseDouble(tokens[4]));
                neuPolarityValues.put(word, neuPolarityValues.get(word)+Double.parseDouble(tokens[5]));
            }
        }

        Set set = posPolarityValues.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry mentry = (Map.Entry) iterator.next();
            String polarityWord = mentry.getKey().toString();
            outWriter2.write(polarityWord+"|"+corpusCountValue.get(polarityWord)+"|"+totalCountValue.get(polarityWord)+"|"+posPolarityValues.get(polarityWord)+"|"+negPolarityValues.get(polarityWord)+"|"+neuPolarityValues.get(polarityWord)+"\n");
        }
        outWriter2.close();
        /*while((line = inpReader.readLine()) != null) {
            String tokens[] = line.split("\\|");
            double corpusCount = Double.parseDouble(tokens[1]);
            double total = Double.parseDouble(tokens[2]);

            if (total != 0 && corpusCount >= 20) {

                double absPos = ((1+(Double.parseDouble(tokens[3]))) / (total+3)) * (totalExpansion/posExpansion);
                double absNeg = ((1+(Double.parseDouble(tokens[4]))) / (total+3)) * (totalExpansion/negExpansion);
                double absNeu = ((1+(Double.parseDouble(tokens[5]))) / (total+3)) * (totalExpansion/neuExpansion);
                //double absNeu = 0.0;

                double absTotal = absPos+absNeg+absNeu;

                double posVal = absPos/absTotal;
                double negVal = absNeg/absTotal;
                double neuVal = absNeu/absTotal;

                //System.out.println(neuVal+", "+(Double.parseDouble(tokens[5]) / total));

                /*double neuVal = Double.parseDouble(tokens[5]) / total;
                double posVal = Double.parseDouble(tokens[3]) / total; //(total-Double.parseDouble(tokens[5]));
                double negVal = Double.parseDouble(tokens[4]) / total; //(total-Double.parseDouble(tokens[5]));*/
                /*if (neuVal > neuTh) {
                    if (!neutralPolarityValues.containsKey(tokens[0]) || Math.abs(posVal-negVal)<=0.10) {
                        //System.out.println(tokens[0]);
                        outWriter1.write(tokens[0] + "|" + "0" + "\n");
                        neutralPolarityValues.put(tokens[0], neuVal);
                    } else {
                        System.out.println(tokens[0] + "|" + neutralPolarityValues.get(tokens[0]));
                    }
                }
            }
        }*/
        inpReader.close();

        inpReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(rootDirectory+"\\resources\\DTExpandedLexiconWithoutThreshold\\Lowercase_"+inpFile)), "UTF-8"));

        while((line = inpReader.readLine()) != null) {
            String tokens[] = line.split("\\|");
            double corpusCount = Double.parseDouble(tokens[1]);
            double total = Double.parseDouble(tokens[2]);// - Double.parseDouble(tokens[5]);

            totalExpansion = posExpansion+negExpansion+neuExpansion;

            if (corpusCount >= 50) {

                double absPos = ((0+(Double.parseDouble(tokens[3]))) / (total+0)) * (totalExpansion/posExpansion);
                double absNeg = ((0+(Double.parseDouble(tokens[4]))) / (total+0)) * (totalExpansion/negExpansion);
                double absNeu = (Double.parseDouble(tokens[5]) / total) * (totalExpansion/neuExpansion);
                //double absNeu = 0;
                //double absNeu = 0;

                double absTotal = absPos+absNeg+absNeu;

                double posVal = absPos/absTotal;
                double negVal = absNeg/absTotal;
                double neuVal = absNeu/absTotal;

                if(corpusCount >= 50 && total>=10){
                    outWriter.write(tokens[0]+"|"+posVal+"|"+negVal+"|"+neuVal+"\n");
                }

                //System.out.println(posVal+", "+negVal+", "+neuVal);

                /*double posVal = Double.parseDouble(tokens[3]) / total;
                double negVal = Double.parseDouble(tokens[4]) / total;
                double neuVal = Double.parseDouble(tokens[5]) / total;// / (total+Double.parseDouble(tokens[5]));*/

                /*if (posVal >= posTh) {
                    if (!polarityValues.containsKey(tokens[0]) && !neutralPolarityValues.containsKey(tokens[0])) {
                        outWriter.write(tokens[0] + "|" + "1" + "\n");
                        polarityValues.put(tokens[0], posVal);
                    } else {
                        //System.out.println(tokens[0] + ", " + polarityValues.get(tokens[0]));
                    }
                }

                if (negVal >= negTh) {
                    if (!polarityValues.containsKey(tokens[0]) && !neutralPolarityValues.containsKey(tokens[0])) {
                        outWriter.write(tokens[0] + "|" + "-1" + "\n");
                        polarityValues.put(tokens[0], negVal);
                    } else {
                        //System.out.println((tokens[0] + ": " + polarityValues.get(tokens[0])));
                    }
                }*/

                /*if(neuVal >= neuTh){
                    if(!polarityValues.containsKey(tokens[0])){
                        outWriter.write(tokens[0]+"|"+"0"+"\n");
                        polarityValues.put(tokens[0],neuVal);
                    }else{
                        System.out.println(tokens[0]+polarityValues.get(tokens[0]));
                    }
                }*/
            }
        }

        //System.out.println(polarityValues.size());
        inpReader.close();
        outWriter.close();
        outWriter1.close();
    }
}
