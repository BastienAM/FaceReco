package Group1.FaceReco.recognition;

public class RecognitionResult {
	int[] label = new int[1];
	double[] confidence = new double[1];

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
