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
        int remainder = totalSize % numFolds;

        double totalAccuracy = 0.0;

        double totalF1 = 0.0;
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
            int tp = 0;
            int fp = 0;
            int tn = 0;
            int fn = 0;

            for (int i = 0; i < testSet.getSize(); i++) {
                int predicted = svm.predictSurvived(testSet, i, labelCol);
                int actual = (int) testSet.getVal(i, labelCol);
                if (predicted == 1 && actual == 1) tp++;
                else if (predicted == 1 && actual == 0) fp++;
                else if (predicted == 0 && actual == 0) tn++;
                else if (predicted == 0 && actual == 1) fn++;

            }
            int correct = tp + tn;
            double accuracy = (double) correct / testSet.getSize();
            double precision = (tp + fp > 0) ? (double) tp / (tp + fp) : 0.0;
            double recall = (tp + fn > 0) ? (double) tp / (tp + fn) : 0.0;
            double f1 = (precision + recall > 0) ? 2 * precision * recall / (precision + recall) : 0.0;



            System.out.printf("Fold %d metrics:\n", fold + 1);
            System.out.printf("Accuracy: %.4f | Precision: %.4f | Recall: %.4f | F1: %.4f\n", accuracy, precision, recall, f1);
            System.out.printf("TP: %d, TN: %d, FP: %d, FN: %d, Total: %d\n", tp, tn, fp, fn, testSet.getSize());

            totalAccuracy += accuracy;
            totalF1 += f1;
        }

        System.out.printf("\nAverage %d-fold CV metrics:\n", numFolds);
        System.out.printf("Accuracy: %.4f | F1-score: %.4f\n", totalAccuracy / numFolds, totalF1 / numFolds);
    }
}
