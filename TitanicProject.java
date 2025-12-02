package CSC_466_Group_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TitanicProject {
    public static ProjectMatrix titanicData;

    public static final ArrayList<Integer> omittedColumns = new ArrayList<>();
    public static void main(String[] args){

        // I recommend either setting up environments in Java, or creating a class
        // with the same name and putting the variable there.
        process(TempEnv.DATAPATH);
        standardizeMissingAge();
        titanicData.splitData();

        int labelCol = 0; // column 0 in each row is survived 

        // number of feature columns = row size - 1
        int numFeats = titanicData.getRowSize() - 1;

        // hyperparamters 
        double C = 1.0;
        double eta = 0.0001;
        int epochs = 50;

        LinearSVM svm = new LinearSVM(numFeats, C, eta, epochs);
        // train
        svm.fit(titanicData.trainingSet, labelCol);

        // evaluate on test set
        double accuracy = evaluateAccuracy(svm, titanicData.testingSet, labelCol);
        System.out.printf("Test set accuracy: %.3f%%\n", accuracy); 
        System.out.println(titanicData);
    }

    public static void process(String filename){
        String curLine;
        String[] parsedNums;
        ArrayList<Double> curRow;

        titanicData = new ProjectMatrix();


        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            // read 2nd line before looping
            reader.readLine();
            curLine = reader.readLine();

            // parses line into a double[] where each attribute goes in order as follows,
            // survived, class, sex, age(young, middle, old?), num_siblings, num_parents, fare(cost of ticket)

            // skips indexes 0, 3, 4, and 9 as they act as id's rather than information
            omittedColumns.add(0);
            omittedColumns.add(3);
            omittedColumns.add(4);
            omittedColumns.add(9);


            // all parsed as their given numbers, or converted into numbers, until we decided
            // to do it differently

            while (curLine != null) {
                parsedNums = curLine.split(",");

                curRow = new ArrayList<>();

                // only worried about 1st 10 columns
                for(int i = 0; i < 11; i++){
                    if(!omittedColumns.contains(i)){
                        if (i == 5){
                            // sex: male == 0, female == 1
                            if(parsedNums[i].equals("male")){
                                curRow.add(0.0);
                            }else{
                                curRow.add(1.0);
                            }
                        }else{
                            if (!parsedNums[i].isEmpty()){
                                curRow.add(Double.parseDouble(parsedNums[i]));
                            }else{
                                curRow.add(-1.0);
                            }
                        }
                    }

                }

                titanicData.addRow(curRow);

                curLine = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Bad file name error");
        }
    }

    // sets age values that don't exist in the data to the mean/average
    // of all age values combines
    public static void standardizeMissingAge(){
        double mean = 0.0, sum = 0.0;

        // find mean of age attribute
        for(int i = 0; i < titanicData.getSize(); i++){
            double val = titanicData.getVal(i, 3);
            if(val != -1.0){
                mean += val;
                sum++;
            }
        }
        mean = mean / sum;

        //set each row without an age to the mean
        for(int i = 0; i < titanicData.getSize(); i++){
            if(titanicData.getVal(i, 3) == -1.0){
                titanicData.setVal(i, 3, mean);
            }
        }

        System.out.printf("mean: %f\n", mean);
    }

    public static double evaluateAccuracy(LinearSVM svm, ProjectMatrix test, int labelCol) {
        int correct = 0;
        int total = test.getSize();

        for (int i = 0; i < total; i++) {
            int pred = svm.predictSurvived(test, i, labelCol);
            int actual = (int) test.getVal(i, labelCol);
            if (pred == actual) {
                correct++;
            }
        }
        return (double) correct / total;
    }
}
