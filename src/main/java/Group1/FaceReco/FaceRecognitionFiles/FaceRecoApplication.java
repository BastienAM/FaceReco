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
	String cascadeClassifierPath = "";

	public FaceRecoApplication(){
		myFaceDetection.initCascadeClassifier(Paths.get(cascadeClassifierPath));
	}

	public RecognitionResult recognition (Mat inputImage){
		Mat equalizedImage = myFaceDetection.equalize(inputImage);
		Size minimumFaceSize = new Size(equalizedImage.width() * minimumFaceSizeProportion, equalizedImage.height() * minimumFaceSizeProportion);
		Mat detectedFace = myFaceDetection.detect(equalizedImage, minimumFaceSize, universalFaceSize);
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
