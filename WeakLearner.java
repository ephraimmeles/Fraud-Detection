import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    private int dimensionPredictor; // Dimension Predictor
    private int valuePredictor; // Value Predictor
    private int predSign; // Sign Predictor
    private double bestError; // Minimum error encountered during training
    private int k; // num of clusters

    // Initializes the weak learner and trains it with provided data
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("Input arrays cannot be null");
        if (input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException("Lengths of input arrays must match");

        int n = input.length;
        this.k = input[0].length;
        bestError = Double.POSITIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < k; featureIndex++) {
            for (int thresholdIndex = 0; thresholdIndex < n; thresholdIndex++) {
                int threshold = input[thresholdIndex][featureIndex];

                // Loop for sign = 0
                double errorForZero = 0;
                for (int i = 0; i < n; i++) {
                    int predicted;
                    if (input[i][featureIndex] <= threshold) {
                        predicted = 0;
                    }
                    else {
                        predicted = 1;
                    }
                    if (predicted != labels[i]) {
                        errorForZero += weights[i];
                    }
                }
                updateBestLearner(featureIndex, threshold, 0, errorForZero);

                // Loop for sign = 1
                double errorForOne = 0;
                for (int i = 0; i < n; i++) {
                    int predicted;
                    if (input[i][featureIndex] <= threshold) {
                        predicted = 1;
                    }
                    else {
                        predicted = 0;
                    }
                    if (predicted != labels[i]) {
                        errorForOne += weights[i];
                    }
                }
                updateBestLearner(featureIndex, threshold, 1, errorForOne);
            }
        }
    }

    // helper method for constructor
    private void updateBestLearner(int dimension, int value, int sign, double error) {
        if (error < bestError) {
            bestError = error;
            dimensionPredictor = dimension;
            valuePredictor = value;
            predSign = sign;
        }
    }

    // Predicts the label for a new sample based on the trained stump
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("Invalid sample or learner "
                                                       + "not trained");
        }
        if (sample.length != k) {
            throw new IllegalArgumentException("invalid");
        }
        int prediction;
        if (sample[dimensionPredictor()] <= valuePredictor()) {
            prediction = signPredictor();
        }
        else {
            prediction = 1 - signPredictor();
        }
        return prediction;
    }

    // Accessor for the dimension predictor
    public int dimensionPredictor() {
        return dimensionPredictor;
    }

    // Accessor for the value predictor
    public int valuePredictor() {
        return valuePredictor;
    }

    // Accessor for the sign predictor
    public int signPredictor() {
        return predSign;
    }


    // Unit testing
    public static void main(String[] args) {
        In datafile = new In(args[0]);

        int n = datafile.readInt();
        int k = datafile.readInt();

        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        // Assume we want to predict the label for the first sample in the input
        int prediction = weakLearner.predict(input[0]); // Call predict on
        // the first sample

        // Output the prediction result
        StdOut.printf("Prediction for the first sample: %d\n", prediction);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());
    }
}
