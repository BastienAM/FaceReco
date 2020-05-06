package Group1.FaceReco.FaceRecognitionFiles;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionTests {
    FaceRecoApplication recoApp = new FaceRecoApplication();

    List<Long> idsToTest = new ArrayList<>();

    public FaceRecognitionTests(){
        for(int i = 0; i < 25; i++){
            idsToTest.add(new Long(i + 15));
        }
    }

    public void train(){
        recoApp.training(idsToTest);
    }

    public void save(){
        String stringPath = "context\\testContext.xml";
        Path testContextPath = Paths.get(stringPath);
        recoApp.save(testContextPath);
    }

    public void load(){
        String stringPath = "context\\testContext.xml";
        Path testContextPath = Paths.get(stringPath);
        recoApp.load(testContextPath);
    }

    public void treatAllPhotos() throws IllegalArgumentException {
        for(Long currentID : idsToTest){
            String pathToPhoto = "photo\\" + currentID.toString();
            File currentPhotosFile = new File(pathToPhoto);

            if(currentPhotosFile.exists() && currentPhotosFile.isDirectory()){

                File[] listFiles = currentPhotosFile.listFiles();

                for(File file : listFiles){
                    if (!file.getName().endsWith(".pgm")) { //search how to convert all image to .pgm
                        throw new IllegalArgumentException("The file of the student " + currentID + " contains other files than '.pgm'.");
                    }
                    else{
                        Mat photoToTreat = Imgcodecs.imread(pathToPhoto + "\\" + file.getName(), Imgcodecs.IMREAD_GRAYSCALE);
                        Mat treatedPhoto = recoApp.imageTreatment(photoToTreat);
                        if(treatedPhoto != null){
                            Imgcodecs.imwrite(pathToPhoto + "\\" + file.getName(), treatedPhoto);
                        }
                    }
                }
            }
            else{
                throw new IllegalArgumentException("The file of the student " + currentID + "  do not exist or is not a directory.");
            }
        }
    }

    public RecognitionResult recognize(Mat inputImage){
        return recoApp.recognition(inputImage);
    }


    public float testAll(){
        List<Long> allIdsToTest = new ArrayList<>();
        allIdsToTest = idsToTest;
        //allIdsToTest.add(Long.valueOf(11));
        //allIdsToTest.add(Long.valueOf(12));
        //allIdsToTest.add(Long.valueOf(13));

        float totalTested = 0;
        float totalCorrect = 0;
        float totalUnrecognized = 0;

        for(Long currentID : allIdsToTest) {
            String pathToPhoto = "photo\\testing\\" + currentID.toString();
            File currentPhotosFile = new File(pathToPhoto);

            if(currentPhotosFile.exists() && currentPhotosFile.isDirectory()) {

                File[] listFiles = currentPhotosFile.listFiles();

                for(File file : listFiles){
                    if (!file.getName().endsWith(".pgm")) { //search how to convert all image to .pgm
                        throw new IllegalArgumentException("The file of the student " + currentID + " contains other files than '.pgm'.");
                    }
                    else{
                        Mat photoToTest = Imgcodecs.imread(pathToPhoto + "\\" + file.getName(), Imgcodecs.IMREAD_GRAYSCALE);
                        try {
                            RecognitionResult testResult = recognize(photoToTest);

                            if(testResult.label[0] == currentID){
                                totalCorrect++;
                            }
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("One face unrecognized!");
                            totalUnrecognized++;
                        }

                        totalTested++;
                    }
                }
            }
        }

        System.out.println(totalUnrecognized + " face unrecognized!");

        System.out.println(totalCorrect + " face correct!");
        System.out.println(totalTested + " face tested!");

        float percentCorrect = totalCorrect / totalTested;
        return percentCorrect;
    }


    public static void main(String[] args){
        //Chargement de la librairie native d'openCV_contrib.
        String absolutePath = null;
        try {
            absolutePath = new File("./src/main/resources/opencv_contrib/opencv_java420.dll").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.load(absolutePath);

        FaceRecognitionTests test = new FaceRecognitionTests();

        //test.treatAllPhotos();
        //test.train();
        //test.save();

        test.load();

        System.out.println("percentage correct : " + test.testAll() * 100 + "%");

        /*
        Mat imageTest = Imgcodecs.imread("photo\\testing\\20\\20_P04A+000E+00.pgm", Imgcodecs.IMREAD_GRAYSCALE);
        RecognitionResult testResult = test.recognize(imageTest);

        System.out.println("suposed result : 20, actual result : " + testResult.label[0] + " with confidence : " + testResult.confidence[0]);
        */
    }
}
