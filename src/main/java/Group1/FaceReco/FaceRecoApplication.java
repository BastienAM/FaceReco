package Group1.FaceReco;

import java.io.File;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceRecoApplication {

	public static void main(String[] args) {
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//System.loadLibrary("src/main/resources/opencv_contrib/opencv_java420.dll");
		//System.load("C:/Users/Basti/git/FaceReco/src/main/resources/opencv_contrib/opencv_java420.dll");
		
		String absolute = null;
		
		try {
			absolute = new File("./src/main/resources/opencv_contrib/opencv_java420.dll").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.load(absolute);
		
		new File("./context").mkdirs();
		new File("./photo").mkdirs();
		
		SpringApplication.run(FaceRecoApplication.class, args);
	}

}
