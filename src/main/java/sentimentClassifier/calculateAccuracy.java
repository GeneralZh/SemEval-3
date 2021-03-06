package sentimentClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by krayush on 12-06-2015.
 */
public class calculateAccuracy {

    calculateAccuracy()throws IOException
    {
        mainFunction();
    }

    public static void main(String[] args) throws IOException {
        new calculateAccuracy();
    }

    private void mainFunction() throws IOException
    {
        final String rootDirectory = System.getProperty("user.dir");
        /*File file = new File("rootDir.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine())!=null)
        {
            rootDirectory = line;
            System.out.println("Root Directory is: "+rootDirectory);
        }*/

        File file1 = new File(rootDirectory + "\\dataset\\testLabels.txt");
        BufferedReader reader1 = new BufferedReader(new FileReader(file1));

        File file2 = new File(rootDirectory + "\\dataset\\predictedLabels.txt");
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));

        String line;
        int count = 0;
        double num = 0;
        while ((line = reader1.readLine()) != null) {
            num++;

            if (Double.parseDouble(line) == Double.parseDouble(reader2.readLine())) {
                count++;
                //System.out.println(num);
            } else {
                //System.out.println(num);
            }
        }
        System.out.println("Number of correct predictions is: "+count);
        System.out.println("Accuracy is: "+count/num);
    }
}
