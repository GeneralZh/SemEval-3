package sentimentClassifier;

import de.bwaldvogel.liblinear.*;
import org.apache.commons.cli.BasicParser;

import java.io.*;
import java.util.*;

class ClassifierHelperL {
    List<LinkedHashMap<Integer, Double>> featureList = new ArrayList<LinkedHashMap<Integer, Double>>();

    ClassifierHelperL(String dataset) throws IOException {
        BufferedReader readerTrain = new BufferedReader(new FileReader(new File(dataset)));

        int count = 0;
        while (readerTrain.readLine() != null) {
            count++;
        }

        for (int i = 0; i < count; i++) {
            featureList.add(i, new LinkedHashMap<Integer, Double>());
        }

    }

    public void setHashMap(int start, List<LinkedHashMap<Integer, Double>> hMap) {
        System.out.println(hMap.size());
        for (int i = 0; i < hMap.size(); i++) {
            for (Map.Entry<Integer, Double> entry : hMap.get(i).entrySet()) {
                featureList.get(i).put(start + entry.getKey(), entry.getValue());
            }
        }
    }

    public List<LinkedHashMap<Integer, Double>> getList() {
        return featureList;
    }
}

public class SentimentClassifierL {

    static String rootDirectory;
    static List<LinkedHashMap<Integer, Double>> trainingFeature;
    static List<LinkedHashMap<Integer, Double>> testFeature;


    SentimentClassifierL(int option, String trainFile, String testFile) throws IOException {

        rootDirectory = System.getProperty("user.dir");
        mainClassifierFunction(option, trainFile, testFile);

        //rootDirectory = "D:\\Course\\Semester VII\\Internship\\sentiment\\indian";
        //return generateFeature();
    }

    private void generateDataset(String input, String output)throws IOException   //changed wrt ai's project
    {
        FileReader fR = new FileReader(input);
        PrintWriter writer = new PrintWriter(output);
        BufferedReader bf = new BufferedReader(fR);
        //BufferedWriter wr = new BufferedWriter(fW);
        String line = null;
        while((line = bf.readLine()) != null)
        {
            String[] part = line.split("\\|");
            writer.println(part[3]);
        }
        fR.close();
        //bf.close();
        writer.close();


    }

    private static String reverseWords(String input) {
        Deque<String> words = new ArrayDeque<String>();
        for (String word: input.split(" ")) {
            if (!word.isEmpty()) {
                words.addFirst(word);
            }
        }
        StringBuilder result = new StringBuilder();
        while (!words.isEmpty()) {
            result.append(words.removeFirst());
            if (!words.isEmpty()) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    private int generateFeature(int option, String trainFile, String testFile) throws IOException {

        generateDataset(rootDirectory + "\\dataset\\dataset_sentimentClassification\\"+trainFile, rootDirectory + "\\dataset\\dataset_sentimentClassification\\Train_Contextual_Cleansed.txt");


        //TRAINING SET
        ClassifierHelperL trainingObject = new ClassifierHelperL(rootDirectory + "\\dataset\\dataset_sentimentClassification\\Train_Contextual_Cleansed.txt");

        //TESTING SET
        ClassifierHelperL testObject = null;
        if(option == 2 || option == 3)
        {
            generateDataset(rootDirectory + "\\dataset\\dataset_sentimentClassification\\"+testFile, rootDirectory + "\\dataset\\dataset_sentimentClassification\\Test_Contextual_Cleansed.txt");
            testObject = new ClassifierHelperL(rootDirectory + "\\dataset\\dataset_sentimentClassification\\Test_Contextual_Cleansed.txt");
        }


        //POS FEATURE
        int start = 0;
        /*POS posObject = new POS(rootDirectory);
        trainingObject.setHashMap(start, posObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, posObject.getTestList());
        }
        System.out.println("F: "+start);*/



        //N GRAM FEATURE
        //start += posObject.getFeatureCount();
        Ngram ngramObject = new Ngram(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, ngramObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, ngramObject.getTestList());
        }
        System.out.println("F: "+start);


        //SENTI WORD NET FEATURE
        /*start += ngramObject.getFeatureCount();
        SentiWordNet sentiWordNetObject = new SentiWordNet(rootDirectory);
        trainingObject.setHashMap(start, sentiWordNetObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, sentiWordNetObject.getTestList());
        }
        System.out.println("F: "+start);*/



        //ENTITY FEATURE
        start += ngramObject.getFeatureCount();
        EntityFeatureLaptops entityObject = new EntityFeatureLaptops(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, entityObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, entityObject.getTestList());
        }

        //DT FEATURE
        start += entityObject.getFeatureCount();
        DT dtObject = new DT(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, dtObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, dtObject.getTestList());
        }

        //TF-IDF FEATURE
        /*start += dtObject.getFeatureCount();
        Tf_IdfFeature tfObject = new Tf_IdfFeature(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, tfObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, tfObject.getTestList());
        }

        //PREFIX 3
        start += tfObject.getFeatureCount();
        CharacterNgramPrefixSize3 pre3ob = new CharacterNgramPrefixSize3(rootDirectory);
        trainingObject.setHashMap(start, pre3ob.getTrainingList());
        if(option == 2|| option == 3) {
            testObject.setHashMap(start, pre3ob.getTestList());
        }

        //SUFFIX 3
        start += pre3ob.getFeatureCount();
        CharacterNgramSuffixSize3 suf3ob = new CharacterNgramSuffixSize3(rootDirectory);
        trainingObject.setHashMap(start, suf3ob.getTrainingList());
        if(option == 2|| option == 3) {
            testObject.setHashMap(start, suf3ob.getTestList());
        }


        //PREFIX 4
        start +=  suf3ob.getFeatureCount();
        CharacterNgramPrefixSize4 pre4ob = new CharacterNgramPrefixSize4(rootDirectory);
        trainingObject.setHashMap(start, pre4ob.getTrainingList());
        if(option == 2|| option == 3) {
            testObject.setHashMap(start, pre4ob.getTestList());
        }


        //SUFFIX 4
        start += pre4ob.getFeatureCount();
        CharacterNgramSuffixSize4 suf4ob = new CharacterNgramSuffixSize4(rootDirectory);
        trainingObject.setHashMap(start, suf4ob.getTrainingList());
        if(option == 2|| option == 3) {
            testObject.setHashMap(start, suf4ob.getTestList());
        }*/

        //DT SEED FEATURE
        start += dtObject.getFeatureCount();
        DTSeedLexicon dtSeedObject = new DTSeedLexicon(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, dtSeedObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, dtSeedObject.getTestList());
        }

        //PMI FEATURE
        /*start += dtSeedObject.getFeatureCount();
        PMIScore pmiObject = new PMIScore(rootDirectory);
        trainingObject.setHashMap(start, pmiObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, pmiObject.getTestList());
        }*/


        //EX-QUES FEATURE
        /*start += dtSeedObject.getFeatureCount();
        ExclamationQuestion exqObject = new ExclamationQuestion(rootDirectory, trainFile, testFile);
        trainingObject.setHashMap(start, exqObject.getTrainingList());
        if(option == 2 || option == 3) {
            testObject.setHashMap(start, exqObject.getTestList());
        }*/


        trainingFeature = trainingObject.getList();
        if(option == 2 || option == 3) {
            testFeature = testObject.getList();
        }


        int finalSize = start + dtSeedObject.getFeatureCount();

        return finalSize;
    }


    private void mainClassifierFunction(int option, String trainFile, String testFile)throws IOException {
        //SentimentClassifierHindi this = new SentimentClassifierHindi();
        //int finalSize = this.SentimentClassifierHindi();
        int finalSize = this.generateFeature(option, trainFile, testFile);
        System.out.println("Hello Sentiment!");

        // Create features
        Problem problem = new Problem();

        // Save X to problem
        double a[] = new double[this.trainingFeature.size()];
        File file = new File(rootDirectory + "\\dataset\\trainingLabels.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String read;
        int count = 0;
        while ((read = reader.readLine()) != null) {
            a[count++] = Double.parseDouble(read.toString());
        }

        Feature[][] trainFeatureVector = new Feature[trainingFeature.size()][finalSize];
        /*PrintWriter pw = new PrintWriter(rootDirectory+"\\dataset\\dataset_sentimentClassification\\russian.arff");

        pw.println("@RELATION french\n");
        for(int i=0; i<finalSize; i++){
            pw.println("@ATTRIBUTE f"+(i+1)+"\t"+"REAL");
        }
        pw.println("@ATTRIBUTE class\t{NEGATIVE, NEUTRAL, POSITIVE}\n");

        pw.println("@DATA\n");*/

        for (int i = 0; i < trainingFeature.size(); i++) {
            System.out.println(i + " trained.");
            //pw.print("{");
            for (int j = 0; j < finalSize; j++) {
                if (trainingFeature.get(i).containsKey(j + 1)) {
                    trainFeatureVector[i][j] = new FeatureNode(j + 1, trainingFeature.get(i).get(j + 1));
                    //pw.print(j+1+" "+trainingFeature.get(i).get(j + 1)+", ");
                    //pw.print(trainingFeature.get(i).get(j + 1)+",");
                } else {
                    trainFeatureVector[i][j] = new FeatureNode(j + 1, 0.0);
                    //pw.print(0+",");
                }
                /*for(int p=0; p<finalSize; p++){
                    pw.print(trainFeatureVector[i][p]);
                }*/
            }
            //pw.print((finalSize+1)+" ");
            /*if(a[i]==1){
                pw.print("POSITIVE");
            } else if(a[i]==-1){
                pw.print("NEGATIVE");
            } else {
                pw.print("NEUTRAL");
            }
            pw.println();*/
            //pw.println("}");
        }
        //pw.close();


        problem.l = trainingFeature.size(); // number of training examples
        problem.n = finalSize; // number of features
        problem.x = trainFeatureVector; // feature nodes
        problem.y = a; // target values ----

        BasicParser bp = new BasicParser();

        SolverType solver = SolverType.L2R_LR; // -s 7
        double C = 1.3;    // cost of constraints violation
        double eps = 0.0001; // stopping criteria

        int weight_label[] = {-1,0,1};
        double weightVal[] = {2,1.25,1};
        //int* p = weight;

        Parameter parameter = new Parameter(solver, C, eps);
        //parameter.setWeights(weightVal, weight_label);
        Model model = Linear.train(problem, parameter);
        File modelFile = new File("model");
        model.save(modelFile);

        PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(rootDirectory + "\\dataset\\predictedLabels.txt")));

        if (option == 1) {
            double[] val = new double[trainingFeature.size()];
            Linear.crossValidation(problem, parameter, 5, val);
            for (int i = 0; i < trainingFeature.size(); i++) {
                write.println(val[i]);
            }
            write.close();
            return;
        }

        //LinkedHashMap<String, Double>weight = new LinkedHashMap<String, Double>();

        // load model or use it directly
        if (option == 2 || option == 3) {
            model = Model.load(modelFile);
            //BufferedReader testFile = new BufferedReader(new InputStreamReader(new FileInputStream(rootDirectory + "\\dataset\\hindiTest.txt"), "UTF-8"));
            for (int i = 0; i < testFeature.size(); i++) {
                Feature[] instance = new Feature[testFeature.get(i).size()];
                int j = 0;
                int k=0;
                for (Map.Entry<Integer, Double> entry : testFeature.get(i).entrySet()) {
                    instance[j++] = new FeatureNode(entry.getKey(), entry.getValue());
                    //System.out.println(k+", "+entry.getKey());
                    /*for(; k<entry.getKey()-1; k++){
                        pw.write("0,");
                    }
                    k = entry.getKey();
                    pw.write(entry.getValue()+",");*/
                }
                //pw.write("\n");

                double prediction = Linear.predict(model, instance);
                //String id = testFile.readLine().split("\t")[0];
                //write.println(id+"\t"+prediction);
                write.println(prediction);
            }
            //pw.close();
            write.close();
            return;
        }
    }

}