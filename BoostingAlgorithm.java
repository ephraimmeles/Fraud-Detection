import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BoostingAlgorithm {
    private int[][] inputData; // input data 2D array
    private int[] labels; // array of labels
    private Point2D[] locations; // locations
    private double[] weights; // array of weights
    private int k; // num of clusters
    private List<WeakLearner> learners = new ArrayList<>(); // arraylist of learners
    private Clustering clustering; // clustering object
    private int[][] reducedData; // Reduced data after dimensionality reduction

    // Constructor to initialize the Boosting Algorithm
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        // Validate input data for null values and proper lengths
        if (input == null || labels == null || locations == null) {
            throw new IllegalArgumentException("Input arrays cannot be null.");
        }
        if (input.length == 0 || k < 1 || k > locations.length) {
            throw new IllegalArgumentException("Invalid initialization parameters.");
        }
        if (input.length != labels.length) {
            throw new IllegalArgumentException("Input and labels must have "
                                                       + "the same length.");
        }
        this.inputData = input;
        this.labels = labels;
        this.locations = locations;
        this.k = k;
        this.weights = new double[input.length];
        double initialWeight = 1.0 / input.length;
        for (int i = 0; i < input.length; i++) {
            this.weights[i] = initialWeight;
        }
        this.clustering = new Clustering(locations,
                                         k); // Assuming Clustering is
        // implemented correctly
        this.reducedData = new int[input.length][];
        // Reduce dimensions only once per input in the constructor
        for (int i = 0; i < input.length; i++) {
            this.reducedData[i] = clustering.reduceDimensions(input[i]);
        }
    }

    // Return the current weight of the ith data point
    public double weightOf(int i) {
        if (i < 0 || i >= weights.length) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        return weights[i];
    }

    // Apply one iteration of the AdaBoost algorithm
    public void iterate() {
        WeakLearner learner = new WeakLearner(reducedData, weights, labels);
        learners.add(learner);

        for (int i = 0; i < inputData.length; i++) {
            int prediction = learner.predict(reducedData[i]);
            if (prediction != labels[i]) {
                weights[i] *= 2.0; // Doubling the weight for misclassified points
            }
        }

        normalizeWeights();
    }

    // Normalize weights to ensure their sum is 1
    private void normalizeWeights() {
        double sum = 0;
        for (double weight : weights) {
            sum += weight;
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
    }

    // Predict the label for a new sample using majority voting from all weak learners
    public int predict(int[] sample) {
        int[] reducedSample = clustering.reduceDimensions(sample);
        int votes = 0;
        for (WeakLearner learner : learners) {
            votes += learner.predict(reducedSample);
        }
        int majority;
        if (votes > learners.size() / 2) {
            majority = 1;
        }
        else {
            majority = 0;
        }

        return majority;
    }

    // Main method for unit testing
    public static void main(String[] args) {
        // Assume DataSet is a user-defined class that loads data
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // Initialize the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < T; t++) {
            model.iterate();
        }
        if (training.getN() > 0) {
            System.out.println("Weight of first element: " + model.weightOf(0));
        }

        // Evaluate the model on training data
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++) {
            if (model.predict(trainingInput[i]) == trainingLabels[i]) {
                trainingAccuracy += 1;
            }
        }
        trainingAccuracy /= training.getN();

        // Evaluate the model on testing data
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++) {
            if (model.predict(testingInput[i]) == testingLabels[i]) {
                testAccuracy += 1;
            }
        }
        testAccuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
    }
}
