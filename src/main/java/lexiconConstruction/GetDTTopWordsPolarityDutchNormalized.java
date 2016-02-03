package lexiconConstruction;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by krayush on 25-12-2015.
 */
public class GetDTTopWordsPolarityDutchNormalized {
    public static void main(String[] args) throws IOException {
        String rootDirectory = System.getProperty("user.dir");
        LinkedHashMap<String, Double> frequency = new LinkedHashMap();
        LinkedHashMap<String, Double> wordCount = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> posCount = new LinkedHashMap<String, Double>();
        LinkedHashMap<String, Double> negCount = new LinkedHashMap<String, Double>();

        double positiveExp = 2994;
        double negativeExp = 4895;

        File fR = new File(rootDirectory + "\\resources\\DTExpansion\\DTPolarity\\valuesDTNormalized.txt");
        //PrintWriter writer = new PrintWriter("D:\\Course\\Semester VII\\Internship\\Results\\Maggie TUD\\sentimentJavaWords.txt");
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fR), "UTF-8"));
        String line;
        while ((line = bf.readLine()) != null) {
            String tokens[] = line.split("\\|");
            //System.out.println(line);
            //try {
                //frequency.put(tokens[0], Double.parseDouble(tokens[1]));
                wordCount.put(tokens[0], Double.parseDouble(tokens[2]));
                posCount.put(tokens[0], Double.parseDouble(tokens[3]));
                negCount.put(tokens[0], Double.parseDouble(tokens[4]));
            /*}catch(NumberFormatException e){
                System.out.println(e);
            }*/
        }


        //modifiedFrequency = sortHashMapByValuesD(modifiedFrequency);
        Iterator modifiedIterator = posCount.entrySet().iterator();
        int count = 1;

        Writer polWriter1 = new OutputStreamWriter(
                new FileOutputStream(rootDirectory+"\\resources\\DTExpansion\\DTPolarity\\normalizedPositive.txt"), "UTF-8");
        BufferedWriter pfout1 = new BufferedWriter(polWriter1);

        Writer polWriter2 = new OutputStreamWriter(
                new FileOutputStream(rootDirectory+"\\resources\\DTExpansion\\DTPolarity\\normalizedNegative.txt"), "UTF-8");
        BufferedWriter pfout2 = new BufferedWriter(polWriter2);

        Writer polWriter3 = new OutputStreamWriter(
                new FileOutputStream(rootDirectory+"\\resources\\DTExpansion\\DTPolarity\\normalizedPolarityLexicon.txt"), "UTF-8");
        BufferedWriter pfout3 = new BufferedWriter(polWriter3);

        while (modifiedIterator.hasNext()) {
            Map.Entry me = (Map.Entry) modifiedIterator.next();
            String key = me.getKey().toString();
            Double val = Double.parseDouble(me.getValue().toString());

            /*double pos = posCount.get(key)/positiveExp;
            double neg = negCount.get(key)/negativeExp;*/

            double pos = posCount.get(key)/wordCount.get(key);
            double neg = negCount.get(key)/wordCount.get(key);

            if(pos>=0.80){
                pfout1.write(key+"|"+pos+"\n");
                pfout3.write(key+"|"+pos+"\n");
            }

            if(neg>=0.80){
                pfout2.write(key+"|-"+neg+"\n");
                pfout3.write(key+"|-"+neg+"\n");
            }

            if(pos>=0.80 && neg>=0.80){
                System.out.println(key+"|"+pos+"|"+neg);
            }


        }

        pfout1.close();
        pfout2.close();
        pfout3.close();
    }
}
