package Group1.FaceReco.FaceRecognitionFiles;

import Group1.FaceReco.domain.Presence;
import Group1.FaceReco.domain.Timesheet;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.opencv.core.CvType.CV_32SC1;

public class FaceRecoApplication {
	MyFaceDetection myFaceDetection = new MyFaceDetection();
	MyFaceRecognizer myFaceRecognizer = new MyFaceRecognizer();

	float minimumFaceSizeProportion = 0.2f;
	Size universalFaceSize = new Size(100, 100);
	String cascadeClassifierPath = ".\\src\\main\\resources\\haarcascade\\haarcascade_frontalface_default.xml";

	public FaceRecoApplication(){
		myFaceDetection.initCascadeClassifier(Paths.get(cascadeClassifierPath));
	}

	public RecognitionResult recognition (Mat inputImage){
		Mat detectedFace = imageTreatment(inputImage);
		RecognitionResult recognitionResult = new RecognitionResult();
		myFaceRecognizer.predict(detectedFace, recognitionResult.label, recognitionResult.confidence);
		return recognitionResult;
	}

	public void training(List<Long> studentsIdList) throws IllegalArgumentException {

		List<Mat> src = new ArrayList<>();
		List<Long> labelsList = new ArrayList<>();

		for(Long studentId : studentsIdList){

			String pathToPhoto = "photo\\" + studentId.toString();
			File currentPhotosFile = new File(pathToPhoto);

			if(currentPhotosFile.exists() && currentPhotosFile.isDirectory()){

				File[] listFiles = currentPhotosFile.listFiles();

				for(File file : listFiles){
					if (!file.getName().endsWith(".pgm")) { //search how to convert all image to .pgm
						throw new IllegalArgumentException("The file of the student " + studentId + " contains other files than '.pgm'.");
					}

					labelsList.add(Long.valueOf(studentId));
					src.add(Imgcodecs.imread(pathToPhoto + "\\" + file.getName(), Imgcodecs.IMREAD_GRAYSCALE));
				}
			}
			else{
				throw new IllegalArgumentException("The file of the student " + studentId + "  do not exist or is not a directory.");
			}
		}


		Mat labels = new Mat(labelsList.size(), 1, CV_32SC1);

		int counter = 0;
		for(Long currentLabel : labelsList){
			labels.put(counter, 0, currentLabel);
			counter++;
		}

		myFaceRecognizer.train(src, labels);
	}

	public void save(Path path){
		myFaceRecognizer.save(path);
	}

	public void load(Path path){
		myFaceRecognizer.load(path);
	}

    public Mat imageTreatment(Mat inputImage)  throws IllegalArgumentException {
        Mat equalizedImage = myFaceDetection.equalize(inputImage);
        Size minimumFaceSize = new Size(equalizedImage.width() * minimumFaceSizeProportion, equalizedImage.height() * minimumFaceSizeProportion);

        Mat detectedFace = myFaceDetection.detect(equalizedImage, minimumFaceSize, universalFaceSize);

        if(detectedFace == null){
			throw new IllegalArgumentException("No face detected in the given image.");
		}
        return detectedFace;
    }
}
