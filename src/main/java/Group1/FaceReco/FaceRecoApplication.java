package Group1.FaceReco;

import java.io.File;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceRecoApplication {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		new File("./context").mkdirs();
		new File("./photo").mkdirs();
		
		SpringApplication.run(FaceRecoApplication.class, args);
	}

}
