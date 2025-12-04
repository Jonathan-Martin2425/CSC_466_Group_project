package CSC_466_Group_project;

import java.util.ArrayList;
import java.util.Random;

public class CrossValidation {

    private ProjectMatrix data;
    private int labelCol;
    private int numFolds;
    private double C;
    private double learningRate;
    private int epochs;

    public CrossValidation(ProjectMatrix data, int labelCol, int numFolds, double C, double learningRate, int epochs) {
        this.data = data;
        this.labelCol = labelCol;
        this.numFolds = numFolds;
        this.C = C;
        this.learningRate = learningRate;
        this.epochs = epochs;
    }

    public void run() {

        data.shuffle(new Random(42));
        int totalSize = data.getSize();
        int foldSize = totalSize / numFolds;

        double totalAccuracy = 0.0;

        for (int fold = 0; fold < numFolds; fold++) {
            ProjectMatrix trainSet = new ProjectMatrix();
            ProjectMatrix testSet = new ProjectMatrix();

            for (int i = 0; i < totalSize; i++) {
                ArrayList<Double> row = data.getRow(i);
                if (i >= fold * foldSize && i < (fold + 1) * foldSize) {
                    testSet.addRow(row);
                } else {
                    trainSet.addRow(row);
                }
            }

            int numFeatures = data.getRowSize() - 1;
            LinearSVM svm = new LinearSVM(numFeatures, C, learningRate, epochs);
            svm.fit(trainSet, labelCol);

            // Evaluate accuracy
            int correct = 0;
            for (int i = 0; i < testSet.getSize(); i++) {
                int predicted = svm.predictSurvived(testSet, i, labelCol);
                int actual = (int) testSet.getVal(i, labelCol);
                if (predicted == actual) {
                    correct++;
                }
            }

            double accuracy = (double) correct / testSet.getSize();
            System.out.printf("Fold %d accuracy: %.4f. Correct: %d. Total: %d\n", fold + 1, accuracy, correct, testSet.getSize());
            totalAccuracy += accuracy;
        }

        System.out.printf("Average %d-fold CV accuracy: %.4f\n", numFolds, totalAccuracy / numFolds);
    }
}
