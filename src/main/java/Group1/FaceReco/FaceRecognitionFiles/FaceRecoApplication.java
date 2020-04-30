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

		for(Long studentId : studentsIdList){

			List<File> listOfFaces = new ArrayList<>();
			String pathToPhoto = "photo\\" + studentId.toString();
			File currentPhotosFile = new File(pathToPhoto);

			if(currentPhotosFile.exists() && currentPhotosFile.isDirectory()){

				File[] listFiles = currentPhotosFile.listFiles();

				for(File file : listFiles){
					if (!file.getName().endsWith(".pgm")) { //search how to convert all image to .pgm
						throw new IllegalArgumentException("The file of the student " + studentId + " contains other files than '.pgm'.");
					}
					listOfFaces.add(file);
				}
			}
			else{
				throw new IllegalArgumentException("The file of the student " + studentId + "  do not exist or is not a directory.");
			}

			List<Mat> src = new ArrayList<>();
			Mat labels = new Mat(listOfFaces.size(), 1, CV_32SC1);

			int counter = 0;
			for (File file : listOfFaces) {
				Integer fileID = Integer.parseInt(file.getName().split("_")[0]);
				labels.put(counter, 0, fileID);
				src.add(Imgcodecs.imread(pathToPhoto + "\\" + file.getName(), Imgcodecs.IMREAD_GRAYSCALE));
				counter++;
			}

			myFaceRecognizer.train(src, labels);
		}
	}

	public void save(Path path){
		myFaceRecognizer.save(path);
	}

	public void load(Path path){
		myFaceRecognizer.load(path);
	}

    public Mat imageTreatment(Mat inputImage){
        Mat equalizedImage = myFaceDetection.equalize(inputImage);
        Size minimumFaceSize = new Size(equalizedImage.width() * minimumFaceSizeProportion, equalizedImage.height() * minimumFaceSizeProportion);
        return myFaceDetection.detect(equalizedImage, minimumFaceSize, universalFaceSize);
    }
}
