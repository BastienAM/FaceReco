package main.java.Group1.FaceReco;

import org.opencv.core.*;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.opencv.core.CvType.CV_32SC1;

public class FaceRecognition {
    FisherFaceRecognizer recognizer = FisherFaceRecognizer.create();
    Size faceSize = new Size(100, 100);

    private Mat mainProcess(Mat inputImage){
        Mat inProcessImage = inputImage.clone();
        inProcessImage = greyScale(inProcessImage);
        inProcessImage = equalize(inProcessImage);
        return faceDetection(inProcessImage);
    }

    /**
     * Transform a given image from RGB to grey
     * @param inputImage colored image to grey scale
     * @return inputImage greyscaled
     */
    private Mat greyScale(Mat inputImage){
        Mat greyScaledInput = inputImage.clone();
        Imgproc.cvtColor(inputImage, greyScaledInput, Imgproc.COLOR_RGB2GRAY);
        return greyScaledInput;
    }

    /**
     * Equalize a given image by adjusting its contrast
     * @param inputImage image to equalize
     * @return inputImage equalized
     */
    private Mat equalize(Mat inputImage){
        Mat equalizedInput = inputImage.clone();
        Imgproc.equalizeHist(inputImage, equalizedInput);
        return equalizedInput;
    }

    /**
     * Detect and crop one face from a given image
     * @param inputImage full image inputed by user
     * @return cropped image of the face detected, null if none have been found
     */
    private Mat faceDetection(Mat inputImage){
        // Set the minimum face size to 20% of the image height
        int absoluteFaceSize = 0;
        int height = inputImage.rows();
        if(Math.round(height * 0.2f) > 0){
            absoluteFaceSize = Math.round(height * 0.2f);
        }

        // Detect faces in inputImage
        CascadeClassifier faceCascade = new CascadeClassifier();
        faceCascade.load("resources\\haarcascades\\haarcascade_frontalface_default.xml");
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(inputImage, faces, 1.1, 40, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        // Return the first face found
        Rect[] facesArray = faces.toArray();
        Rect faceRect;
        Mat face = null;
        if(facesArray.length != 0) {
            faceRect = new Rect(facesArray[0].x, facesArray[0].y, facesArray[0].width, facesArray[0].height);
            face = new Mat(inputImage, faceRect);
            Imgproc.resize(face, face, faceSize);
        }

        return face;
    }


    private void fisherTraining(String path, File[] listOfFaces){
        List<Mat> src = new ArrayList<>();
        Mat labels = new Mat(listOfFaces.length, 1, CV_32SC1);

        int counter = 0;
        /*for(File file : listOfFaces) {
            Integer fileID = Integer.parseInt(file.getName().split("_")[0]);
            labels.put(counter, 0, fileID);
            src.add(Imgcodecs.imread(path +"\\"+file.getName(), Imgcodecs.IMREAD_GRAYSCALE));
            counter++;
        }*/
        System.out.println(counter);
        System.out.println(listOfFaces.length);

        recognizer.train(src, labels);
        recognizer.save("groups\\groupe1.xml");
    }

    private int fisherPrediction(Mat inputImage){
        return recognizer.predict_label(inputImage);
    }

    private void fisherPred(Mat inputImage){
        int[] label = new int[2];
        double[] confidance = new double[2];
        recognizer.predict(inputImage, label, confidance);
        System.out.println("labels : " + label[0] + " et " + label[1] + ", Confidences : " + confidance[0] + " et " + confidance[1]);
    }

    public void setSize(Size newSize){
        faceSize = newSize;
    }

    public Size getSize(){
        return faceSize;
    }

    /**
     * Delete all files and directories in path
     * @param path target directory that will be cleaned
     */
    private static void cleanFile(String path){
        File[] listOfFiles = getListOfFile(path);
        if(listOfFiles != null){
            for(File file : listOfFiles){
                if(file.isDirectory()){
                    String newPath = path + "\\" + file.getName();
                    cleanFile(newPath);
                }
                file.delete();
            }
        }
    }

    /**
     * Return the list of all files in given path
     * @param path target directory
     * @return list of all files and directories in target path
     */
    private static File[] getListOfFile(String path){
        File folder = new File(path);
        return folder.listFiles();
    }



    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        FaceRecognition faceReco = new FaceRecognition();

        cleanFile("output");
        File[] listOfTrainingFiles = getListOfFile("trainingSets\\trainingGroupe1");
        HashMap<Integer, List<Mat>> idToTrainingMat = new HashMap<>();

        for(File file : listOfTrainingFiles){
            Integer fileID = Integer.parseInt(file.getName().split("_")[0]);
            if(!idToTrainingMat.containsKey(fileID)){
                idToTrainingMat.put(fileID, new ArrayList<>());
            }
            idToTrainingMat.get(fileID).add(Imgcodecs.imread("trainingSets\\trainingGroupe1\\"+file.getName()));
        }

        for(Integer id : idToTrainingMat.keySet()){
            int counter = 0;
            for(Mat image : idToTrainingMat.get(id)){
                Mat face = faceReco.mainProcess(image);
                if(face != null) {
                    Imgcodecs.imwrite("output\\" + id + "_" + Integer.toString(counter) + ".pgm", face);
                }
                counter++;
            }
        }

        File[] listOfFaces = getListOfFile("output");

        faceReco.fisherTraining("output", listOfFaces);

        File[] listOfTestingFiles = getListOfFile("images\\ExtendedYaleB_Reduced\\_TestingSet");
        HashMap<Integer, List<Mat>> idToTestingMat = new HashMap<>();

        for(File file : listOfTestingFiles){
            Integer fileID = Integer.parseInt(file.getName().split("_")[0]);
            if(!idToTestingMat.containsKey(fileID)){
                idToTestingMat.put(fileID, new ArrayList<>());
            }
            idToTestingMat.get(fileID).add(Imgcodecs.imread("images\\ExtendedYaleB_Reduced\\_TestingSet\\"+file.getName()));
        }

        for(Integer id : idToTestingMat.keySet()){
            int counter = 0;
            for(Mat image : idToTestingMat.get(id)){
                Mat face = faceReco.mainProcess(image);
                if(face != null) {
                    Imgcodecs.imwrite("testing\\" + id + "_" + Integer.toString(counter) + ".pgm", face);
                }
                counter++;
            }
        }

        for(File file : getListOfFile("testing")){
            Mat currentMat = Imgcodecs.imread("testing\\"+file.getName(), Imgcodecs.IMREAD_GRAYSCALE);
            System.out.println("Readed : " + file.getName() + " / find : " + faceReco.fisherPrediction(currentMat));
            faceReco.fisherPred(currentMat);
        }
    }
}