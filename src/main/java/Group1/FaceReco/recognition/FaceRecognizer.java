package Group1.FaceReco.recognition;

import org.opencv.core.Mat;
import org.opencv.face.FisherFaceRecognizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FaceRecognizer {
    FisherFaceRecognizer recognizer = FisherFaceRecognizer.create();

    /**
     * Trains a FaceRecognizer with given data and associated labels.
     * @param src The training images, that means the faces you want to learn
     * @param labels The labels corresponding to the images
     */
    public void train(List<Mat> src, Mat labels){
        recognizer.train(src, labels);
    }

    /**
     * Saves the trained recognizer to a file.
     * @param path path and name of where to save the recognizer (as to be .xml)
     */
    public void save(Path path){
        recognizer.save(path.toString());
    }

    /**
     * Load the trained recognizer from a file.
     * @param path path and name of the recognizer to load
     */
    public void load(Path path) throws IllegalArgumentException{
        if(Files.isReadable(path)) {
            recognizer.read(path.toString());
            if(recognizer.empty()){
                throw new IllegalArgumentException("The FisherFaceRecognizer has not been loaded correctly, " +
                        "please verify that the given path lead to a valid save.");
            }
        }
        else{
            throw new IllegalArgumentException("The path given is not readable.");
        }
    }

    /**
     * Predict the id of the face present in a given image with a certain confidence
     * @param inputImage Inputted image containing a face
     * @param label object that will be filled with predicted label
     * @param confidence object that will be filled with the confidence of the prediction
     */
    public void predict(Mat inputImage, int[] label, double[] confidence){
        recognizer.predict(inputImage, label, confidence);
    }
}
