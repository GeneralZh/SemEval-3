package sentimentClassifier;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.*;

/**
 * Created by krayush on 13-07-2015.
 */
public class RemoveOutOfScope {
    String rootDirectory;

    RemoveOutOfScope() throws IOException {
        rootDirectory = System.getProperty("user.dir")+"\\dataset\\dataset_sentimentClassification\\";
        removeDuplicates(rootDirectory, "\\CleansedFiles\\Mobiles_Dutch_Train.txt", "\\CleansedFiles\\Mobiles_Dutch_Train_Cleansed.txt");
    }

    public static void main(String[] args) throws IOException {
        RemoveOutOfScope ob = new RemoveOutOfScope();
    }

    void removeDuplicates(String root, String original, String modifiedFile) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(root + original), "UTF-8"));

        /*Writer writer = new OutputStreamWriter(new FileOutputStream("D:\\Course\\Semester VII\\SemEval16\\ABSA\\Mobiles Dutch\\Mobiles_Dutch_Test_Cleansed.txt"), "UTF-8");
        BufferedWriter fbw = new BufferedWriter(writer);*/

        Writer writer = new OutputStreamWriter(new FileOutputStream(root + modifiedFile), "UTF-8");
        BufferedWriter fbw = new BufferedWriter(writer);

        String line = null;
        ListMultimap<String, String> tags = ArrayListMultimap.create();

        while ((line = bf.readLine()) != null) {
            line = line.trim();
            String[] tokens = line.split("\\|");
            try {
                //if (tokens.length <= 4) {
                if (tokens.length <= 4 || tokens[4].compareToIgnoreCase("")==0) {
                    /*if (tokens[2].compareToIgnoreCase("True")==0) {
                        //fbw1.write(tokens[0] + "|" + tokens[1] + "|" + "0" + "\n");
                    } else {
                        fbw.write(line + "\n");
                        tags.put(tokens[1], tokens[5]);
                    }*/
                } else {
                    /*if (tags.containsKey(tokens[1]) && tags.get(tokens[1]).contains(tokens[5])) {
                        //fbw1.write(tokens[0] + "|" + tokens[1] + "|" + "0" + "\n");
                    } else {*/
                        //fbw1.write(tokens[0] + "|" + tokens[1] + "|" + "1" + "\n");
                        fbw.write(line + "\n");
                        tags.put(tokens[1], tokens[5]);
                    //}
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }

        //fbw1.close();
        fbw.close();
    }
}
