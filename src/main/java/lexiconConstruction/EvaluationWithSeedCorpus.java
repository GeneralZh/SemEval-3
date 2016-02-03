package lexiconConstruction;

import java.io.*;
import java.util.LinkedHashMap;

/**
 * Created by krayush on 25-12-2015.
 */
public class EvaluationWithSeedCorpus {
    public static void main(String[] args) throws IOException {
        String rootDirectory = System.getProperty("user.dir");
        LinkedHashMap<String, Double> seedScore = new LinkedHashMap();
        double totalPos = 0.0, correctPos = 0.0, totalNeg = 0.0, correctNeg = 0.0;

        File fR = new File(rootDirectory + "\\resources\\sentimentSeedWordsDutch.txt");
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fR), "UTF-8"));
        String line;
        while ((line = bf.readLine()) != null) {
            String tokens[] = line.split("\\|");
            seedScore.put(tokens[0], Double.parseDouble(tokens[1]));
        }
        bf.close();

        File fR1 = new File(rootDirectory + "\\resources\\DTExpansion\\DTPolarity\\normalizedPolarityLexicon.txt");
        BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(fR1), "UTF-8"));
        while ((line = bf1.readLine()) != null) {
            String tokens[] = line.split("\\|");
            if(seedScore.containsKey(tokens[0])){
                double score = seedScore.get(tokens[0]);
                if(score > 0){
                    if(Double.parseDouble(tokens[1])>0){
                        correctPos++;
                    }
                    totalPos++;
                }
                else if(score<0){
                    if(Double.parseDouble(tokens[1])<0){
                        correctNeg++;
                    }
                    totalNeg++;
                }
            }
        }


        System.out.println("Total Found: "+(totalNeg+totalPos)+" Total Match: "+(correctNeg+correctPos)+", Overall Acc.: "+(correctNeg+correctPos)/(totalNeg+totalPos));
        System.out.println("Pos: "+totalPos+", Match: "+correctPos+", Acc.: "+correctPos/totalPos);
        System.out.println("Neg: "+totalNeg+", Match: "+correctNeg+", Acc.: "+correctNeg/totalNeg);
    }
}
