package Group1.FaceReco.FaceRecognitionFiles;

public class RecognitionResult {
    int[] label;
    double[] confidence;

    public int[] getLabel() {
        return label;
    }

    public void setLabel(int[] label) {
        this.label = label;
    }

    public double[] getConfidence() {
        return confidence;
    }

    public void setConfidence(double[] confidence) {
        this.confidence = confidence;
    }
}
