package CSC_466_Group_project;


public class LinearSVM {
    private double[] w; // weight vector
    private double b; // bias
    private double C; // regularization parameter
    private double learningRate; // learning rate
    private int epochs; // number of passes over data

    public LinearSVM(int numFeats, double C, double learningRate, int epochs) {
        this.w = new double[numFeats];
        this.b = 0.0;
        this.C = C;
        this.learningRate = learningRate;
        this.epochs = epochs;

    
    }

    // train

    public void fit(ProjectMatrix train, int labelCol) {
        int n = train.getSize();
        int numFeats = train.getRowSize() - 1; 

        for (int epoch = 0; epoch < epochs; epoch++) {
            // iterate rows in order
            for (int i = 0; i < n; i++) {
                // get label y in {+1, -1}
                double yRaw = train.getVal(i, labelCol);
                double y = (yRaw == 1.0) ? 1.0 : -1.0;

                // build the feature vector x
                double[] x = extractFeatures(train, i, labelCol, numFeats);

                double dot = dot(w, x) + b;
                double margin = y * dot;

                // gradient step for the soft-margin SVM
                if (margin >= 1.0) {
                // only regularization term
                    for (int j = 0; j < numFeats; j++) {
                        w[j] = w[j] - learningRate * (w[j]); // d/dw = w
                    }
                    // b is unchanged
                } else {
                    // regularization + hinge loss
                    for (int j = 0; j < numFeats; j++) {
                        w[j] = w[j] - learningRate * (w[j] - C * y * x[j]);
                    }
                    b = b + learningRate * C * y;
                }
            }
        }
    }

    // preict the raw sign for a row in project matrix
    // should return +1 or -1
    public int predictRow(ProjectMatrix data, int row, int labelCol) {
        int numFeats = data.getRowSize() - 1;
        double[] x = extractFeatures(data, row, labelCol, numFeats);

        double s = dot(w, x) + b;
        return s >= 0 ? 1 : -1;
    }

    // predict Survived {0,1} for a row
    public int predictSurvived(ProjectMatrix data, int row, int labelCol) {
        int sign = predictRow(data, row, labelCol);
        return (sign == 1) ? 1 : 0;
    }

    // heler functions

    private double[] extractFeatures(ProjectMatrix m, int row, int labelCol, int numFeats) {
        double[] x = new double[numFeats];
        int idx = 0;
        for (int j = 0; j < m.getRowSize(); j++) {
            if (j == labelCol) {
                continue;
            }
            x[idx++] = m.getVal(row, j);
        }
        return x;
    }

    private double dot(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
}
