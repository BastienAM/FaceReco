package Group1.FaceReco.recognition;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionTests {

    /**
     * This class is for test purposes, it's really messy and is not meant to be used seriously (many things are hardcoded...)
     * If you want to use it and don't understand anything, contact edgar.lebreton.35@gmail.com
     */

    FaceRecoApplication recoApp = new FaceRecoApplication();

    List<Long> idsToTest = new ArrayList<>();

    /**
     * Initialize the ids for the test
     */
    public FaceRecognitionTests(){
        for(int i = 0; i < 25; i++){
            idsToTest.add(new Long(i + 15));
        }
    }

    /**
     * Train "recoApp" with initialized ids
     */
    public void train(){
        recoApp.training(idsToTest);
    }

    /**
     * Save the context
     */
    public void save(){
        String stringPath = "context\\testContext.xml";
        Path testContextPath = Paths.get(stringPath);
        recoApp.save(testContextPath);
    }

    /**
     * Load the context
     */
    public void load(){
        String stringPath = "context\\testContext.xml";
        Path testContextPath = Paths.get(stringPath);
        recoApp.load(testContextPath);
    }

    /**
     * Allow to bypass studentService by treating all the photo in the list of ids to treat (some may not recognize a face, and must be deleted afterward)
     * @throws IllegalArgumentException Throw if an error occur while searching the files
     */
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

    /**
     * Try to recognize a face on a given image (need to have a context trained or loaded)
     * @param inputImage Image to recognize face
     * @return RecognitionResult with label of recognized face, plus its confidence
     */
    public RecognitionResult recognize(Mat inputImage){
        return recoApp.recognition(inputImage);
    }

    /**
     * Test all the photos in the "photo\testing" folder (need to have a context trained or loaded)
     * @return the percentage of photo that have been well recognized
     */
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
