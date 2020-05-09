package Group1.FaceReco.recognition;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.nio.file.Files;
import java.nio.file.Path;

public class FaceDetection {
    CascadeClassifier faceCascade = null;

    /**
     * Transform a given image from RGB to grey.
     * @param inputRGBImage RGB image to grey scale
     * @return inputRGBImage greyscaled
     */
    public static Mat greyScale(Mat inputRGBImage) throws IllegalArgumentException{
        if(inputRGBImage.channels() == 3) {
            Mat greyScaledInput = inputRGBImage.clone();
            Imgproc.cvtColor(inputRGBImage, greyScaledInput, Imgproc.COLOR_RGB2GRAY);
            return greyScaledInput;
        }
        else{
            throw new IllegalArgumentException("Inputted image is not RGB, image need to be RGB.");
        }
    }

    /**
     * Equalize a given image by adjusting its contrast.
     * @param inputImage image to equalize
     * @return inputImage equalized
     */
    public static Mat equalize(Mat inputImage){
        Mat equalizedInput = inputImage.clone();
        Imgproc.equalizeHist(inputImage, equalizedInput);
        return equalizedInput;
    }

    /**
     * Initialize faceCascade with given path to a classifier.
     * @param path path to the classifier that will be used by faceCascade
     */
    public void initCascadeClassifier(Path path) throws IllegalArgumentException{
        if(Files.isReadable(path)) {
            faceCascade = new CascadeClassifier();
            faceCascade.load(path.toString());
            if(faceCascade.empty()){
                throw new IllegalArgumentException("The CascadeClassifier has not been loaded correctly, " +
                        "please verify that the given path lead to a valid classifier.");
            }
        }
        else{
            throw new IllegalArgumentException("The path given is not readable.");
        }
    }

    /**
     * Detect and crop one face from a given image.
     * @param inputImage full image inputted by user
     * @return cropped image of the face detected, null if none have been found
     */
    public Mat detect(Mat inputImage, Size minimumFaceSize, Size universalFaceSize){
        // Detect faces in inputImage
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(inputImage, faces, 1.1, 10, Objdetect.CASCADE_SCALE_IMAGE,
                minimumFaceSize, new Size());

        // Return the first face found
        Rect[] facesArray = faces.toArray();
        Rect faceRect;
        Mat face = null;
        if(facesArray.length != 0) {
            faceRect = new Rect(facesArray[0].x, facesArray[0].y, facesArray[0].width, facesArray[0].height);
            face = new Mat(inputImage, faceRect);
            Imgproc.resize(face, face, universalFaceSize);
        }

        return face;
    }
}
