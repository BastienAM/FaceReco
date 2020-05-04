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

	public int getLabelBetterConfidence() {
		int index = -1;
		double betterConfidence = 0;
		for (int i = 0; i < confidence.length - 1; i++) {
			if (confidence[i] > betterConfidence) {
				betterConfidence = confidence[i];
				index = i;
			}
		}
		return label[index];
	}
}
