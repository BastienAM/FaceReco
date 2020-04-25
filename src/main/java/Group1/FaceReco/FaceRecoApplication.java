package main.java.Group1.FaceReco;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.core.CvType.CV_32SC1;

public class FaceRecoApplication {

	Map<String, TimeSheet> timeSheetsMap = new HashMap<>();


	MyFaceDetection myFaceDetection = new MyFaceDetection();
	MyFaceRecognizer myFaceRecognizer = new MyFaceRecognizer();

	Size minimumFaceSize;
	Size universalFaceSize;

	public FaceRecoApplication(Path cascadeClassifierPath, int minimumFaceSizeInt, int universalSizeFaceInt){
		myFaceDetection.initCascadeClassifier(cascadeClassifierPath);

		minimumFaceSize = new Size(minimumFaceSizeInt, minimumFaceSizeInt);
		universalFaceSize = new Size(universalSizeFaceInt, universalSizeFaceInt);
	}

	public void newTimeSheet(String sheetName, List<Integer> studentsIDList){
		timeSheetsMap.put(sheetName, new TimeSheet(studentsIDList));

		/*Create and train a new group based on the list of students, need to be connected to the database for that*/
		//- from student list, create a trainingSet
		//- train and add the trainingset to the timeSheet
	}

	public void loadTimeSheet(String sheetName){
		myFaceRecognizer.load(timeSheetsMap.get(sheetName).getTrainedGroupPath());
	}
	
	public RecognitionResult recognition (Mat inputImage){
		Mat equalizedImage = myFaceDetection.equalize(inputImage);
		Mat detectedFace = myFaceDetection.detect(equalizedImage, minimumFaceSize, universalFaceSize);
		RecognitionResult recognitionResult = new RecognitionResult();
		myFaceRecognizer.predict(detectedFace, recognitionResult.label, recognitionResult.confidence);
		return recognitionResult;
	}

	public void training(Path filePath) throws IllegalArgumentException {
		File trainingFolder = new File(filePath.toString());
		if (trainingFolder.exists() && trainingFolder.isDirectory()) {
			File[] listOfFaces = trainingFolder.listFiles();

			for (File file : listOfFaces) {
				if (!file.getName().endsWith(".pgm")) {
					throw new IllegalArgumentException("Given path contains other files than '.pgm'.");
				}
			}

			List<Mat> src = new ArrayList<>();
			Mat labels = new Mat(listOfFaces.length, 1, CV_32SC1);

			int counter = 0;
			for (File file : listOfFaces) {
				Integer fileID = Integer.parseInt(file.getName().split("_")[0]);
				labels.put(counter, 0, fileID);
				src.add(Imgcodecs.imread(filePath.toString() + "\\" + file.getName(), Imgcodecs.IMREAD_GRAYSCALE));
				counter++;
			}

			myFaceRecognizer.train(src, labels);
		}
		else{
			throw new IllegalArgumentException("Given path either do not exist or is not a directory.");
		}
	}






	/*

	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	String groupPath = "groups\\";
	String trainingPath = "trainingSets\\";
	String testingPath = "testing\\";


	public BufferedReader getReader() {
		return reader;
	}


	public String getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String groupPath) {
		this.groupPath = groupPath;
	}

	public String getTrainingPath() {
		return trainingPath;
	}

	public void setTrainingPath(String trainingPath) {
		this.trainingPath = trainingPath;
	}

	public String getTestingPath() {
		return testingPath;
	}

	public void setTestingPath(String testingPath) {
		this.testingPath = testingPath;
	}

	public static String getNextValidInput(BufferedReader reader){
		Boolean inputIsValid = false;
		String input = null;

		while(!inputIsValid) {
			try {
				input = reader.readLine();
				inputIsValid = true;

			} catch (IOException e) {
				System.out.println("Error while reading user input : " + e.getMessage());
				System.out.println("Please retry : ");
			}
		}

		return input;
	}

	enum YesNoEnum {
		Yes("y"),
		yes("y"),
		Y("y"),
		y("y"),
		No("n"),
		no("n"),
		N("n"),
		n("n");

		private String stringAnswer;

		private YesNoEnum(String answer) {
			this.stringAnswer = answer;
		}

		@Override
		public String toString() {
			return stringAnswer;
		}
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		FaceRecoApplication faceRecoApp = new FaceRecoApplication();

		YesNoEnum loadGroup = null;
		Boolean inputIsValid = false;


		System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
		System.out.println("Face recognizer application");
		System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
		System.out.println("");

		while (!inputIsValid) {
			System.out.print("Do you want to use an existing group?(Yes/No) ");

			String inputAnswer = getNextValidInput(faceRecoApp.getReader());

			try {
				loadGroup = YesNoEnum.valueOf(inputAnswer);
				inputIsValid = true;

			} catch (IllegalArgumentException e) {
				System.out.println("Input not recognized, please retry : ");
			}
		}


		inputIsValid = false;
		if(loadGroup.toString().equals("y")) {
			while(!inputIsValid) {
				System.out.print("Please enter the name of the group to load : ");

				String inputGroupName = getNextValidInput(faceRecoApp.getReader());
				String filePath = faceRecoApp.getGroupPath() + inputGroupName + ".xml";
				File groupFile = new File(filePath);

				if(groupFile.exists() && groupFile.isFile()){
					System.out.println("Loaded group " + inputGroupName + ", in file " + filePath);
					Path pathToLoad = Paths.get(filePath);
					faceRecoApp.getMyFaceRecognizer().load(pathToLoad);
					inputIsValid = true;
				}
				else{
					System.out.println("Group not found, please verify that it exist.");
				}
			}
		}
		else if(loadGroup.toString().equals("n")) {
			String groupName = null;
			String trainedGroupPath = null;

			while(!inputIsValid) {
				System.out.println("Creation of a new group :");
				System.out.print("Enter the name of the group : ");

				groupName = getNextValidInput(faceRecoApp.getReader());
				Pattern groupNamePattern = Pattern.compile("\\p{Alnum}+");
				Matcher groupNameMatcher = groupNamePattern.matcher(groupName);

				if(!groupNameMatcher.matches()){
					groupName = null;
					System.out.println("Group name must only be composed of characters and numbers.");
					System.out.println("Please retry : ");
				}
				else{
					trainedGroupPath = faceRecoApp.getGroupPath() + groupName + ".xml";
					inputIsValid = true;
				}
			}

			inputIsValid = false;
			while(!inputIsValid) {
				System.out.println("Note : the training set photos must start with 'XX_', " +
						"XX being the id of the person in the photo, and they all must be of .pgm extension");
				System.out.print("Please enter the name of the directory containing the training set : ");

				String inputTrainingName = getNextValidInput(faceRecoApp.getReader());
				String filePath = faceRecoApp.getTrainingPath() + inputTrainingName;

				try{
					faceRecoApp.training(filePath);
					faceRecoApp.getMyFaceRecognizer().save(Paths.get(trainedGroupPath));
					System.out.println("Loaded group " + groupName + ", in file " + trainedGroupPath);
					inputIsValid = true;
				}catch (IllegalArgumentException e){
					System.out.println("Error during training : " + e.getMessage());
					System.out.println("Please retry : ");
					continue;
				}
			}
		}
		else {
			System.out.println("userAnswer is neither 'y' nor 'n', this should never happen... What have you done?!?!");
			return;
		}

		boolean doExit = false;

		while(!doExit){
			System.out.print("Name of the photo to identify (type exit to stop) : ");
			String inputPhotoName = getNextValidInput(faceRecoApp.getReader());

			if(inputPhotoName.equals("exit")){
				break;
			}

			Mat selectedImage = Imgcodecs.imread(faceRecoApp.getTestingPath()+inputPhotoName, Imgcodecs.IMREAD_GRAYSCALE);

			int[] label = new int[1];
			double[] confidence = new double[1];
			faceRecoApp.identify(selectedImage, label, confidence);

			System.out.println("Label = " + label[0] + ", confidence = " + confidence[0]);

		}
	}
	*/
}
