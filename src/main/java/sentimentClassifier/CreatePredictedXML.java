package sentimentClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by krayush on 22-07-2015.
 */
public class CreatePredictedXML {
    String rootDirectory;

    CreatePredictedXML() throws IOException {
        rootDirectory = System.getProperty("user.dir");
        String root = rootDirectory + "\\dataset\\dataset_sentimentClassification";
        //\XML Files\Restaurants_English_Train.xml
        create(root, "\\predictedLabels.txt", "\\Test XML\\Restaurants_Turkish_Test.xml", "\\Scorer\\pred.xml");
    }

    void create(String root, String restLabel, String goldXML, String predictedXML) throws IOException {
        //FileReader fR1 = new FileReader(root + "\\RestaurantPairs.txt");

        //BufferedReader bf1 = new BufferedReader(fR1);

        HashMap<String, String> pair = new HashMap<String, String>();
        String line;
        pair.put("0.0", "neutral");
        pair.put("-1.0", "negative");
        pair.put("1.0", "positive");
        pair.put("2.0", "conflict");

        FileReader fR = new FileReader(root + goldXML);
        PrintWriter modified = new PrintWriter(root + predictedXML);

        FileReader fR2 = new FileReader(rootDirectory + "\\dataset\\"+restLabel);
        //FileReader fR2 = new FileReader(root + svmlabels);

        BufferedReader bf = new BufferedReader(fR);
        BufferedReader bf2 = new BufferedReader(fR2);
        //BufferedReader bf2 = new BufferedReader(fR2);

        //String line;

        while ((line = bf.readLine()) != null) {
            //int flag=0;
            if(line.contains("polarity=")){
                String label = bf2.readLine();
                int index = line.indexOf("polarity=");
                //String parts[] = line.split("\" ");
                label = pair.get(label);
                int lastIndex = index+11;
                if(line.contains("polarity=\"positive\"")){
                    lastIndex = line.indexOf("positive\"")+9;
                }
                else if(line.contains("polarity=\"negative\"")){
                    lastIndex = line.indexOf("negative\"")+9;
                }
                    else if(line.contains("polarity=\"neutral\"")){
                    lastIndex = line.indexOf("neutral\"")+8;
                }
                String modifiedLine = line.substring(0, index)+ "polarity=\"" + label + "\" " + line.substring(lastIndex);
                modified.println(modifiedLine);
            } else {
                modified.println(line);
            }
            /*if (line.contains("OutOfScope")) {
                modified.println(line);
                modified.println(bf.readLine());
                modified.println(bf.readLine());
            } /*else if(line.contains("<text></text>")){
                //System.out.println("check");
                modified.println(line);
                /*while( ((line = bf.readLine())!= null) && !line.contains("</sentence>")){
                    modified.println(line);
                }
                modified.println("\t\t\t</sentence>");
                continue;*/
            //}
            /*else if (line.contains("<text>") && !(line.contains("<text></text>"))) {
                modified.println(line);
                String nextLine = bf.readLine();
                if (nextLine.contains("<Opinions/>") || nextLine.contains("</sentence>")) {
                    modified.println(nextLine);
                } else {
                    String labelLine;
                    int flag = 0;
                    if (nextLine.contains("<Opinions>")) {
                        flag = 1;
                    }
                    while (!(line = bf.readLine()).contains("</sentence>")) {
                        if (line.contains("<Opinions>")) {
                            flag = 1;
                        }
                        ;
                    }
                /*if(flag==0){
                    System.out.println("ABC");
                    //modified.println("\t\t\t</sentence>");
                    //continue;
                }*/
                    //System.out.println(line);
                    //bf2.readLine().
                    /*modified.println("\t\t\t\t<Opinions>");
                    while ((labelLine = bf2.readLine()) != null && !(labelLine.contains("next"))) {
                        //System.out.println(labelLine);
                        if (labelLine.compareToIgnoreCase("-1") != 0) {
                            String category = pair.get(labelLine);
                            System.out.println(category);
                            if (category.compareToIgnoreCase("NONE") != 0) {
                                //System.out.println(category);
                                String part1 = "\t\t\t\t\t" + "<Opinion target=\"Al Di La\" category=\"";
                                String part2 = "polarity=\"positive\" from=\"5\" to=\"13\"/>";
                                modified.println(part1 + category + "\" " + part2);
                            }
                        }
                    }
                    modified.println("\t\t\t\t</Opinions>");
                    modified.println("\t\t\t</sentence>");
                }
            } else {
                //System.out.println(line);
                modified.println(line);
            }
            /*if (line.contains("<Opinions>")) {
                modified.println("\t\t\t\t<Opinions>");
                String labelLine;
                //System.out.println(line);
                //bf2.readLine().
                while (!((labelLine = bf2.readLine()).contains("next"))) {
                    //System.out.println(labelLine);
                    if (labelLine.compareToIgnoreCase("-1") != 0) {
                        String category = pair.get(labelLine);
                        System.out.println(category);
                        String part1 = "\t\t\t\t\t" + "<Opinion target=\"Al Di La\" category=\"";
                        String part2 = "polarity=\"positive\" from=\"5\" to=\"13\"/>";
                        modified.println(part1 + category + "\" " + part2);
                    }
                }
                modified.println("\t\t\t\t</Opinions>");
                while (!bf.readLine().contains("</Opinions>")) {
                    ;
                }
            } else {
                //System.out.println(line);
                modified.println(line);
            }*/
        }
        modified.close();
        bf.close();
        bf2.close();
    }

    public static void main(String[] args) throws IOException {
        CreatePredictedXML ob = new CreatePredictedXML();
    }
}
