package Group1.FaceReco;

import java.io.File;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceRecoApplication {

	public static void main(String[] args) {
		
		//Chargement de la librairie native d'openCV_contrib.
		String absolutePath = null;
		try {
			absolutePath = new File("./src/main/resources/opencv_contrib/opencv_java420.dll").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.load(absolutePath);
		
		//Création des dossiers s'ils existent pas 
		new File("./context").mkdirs();
		new File("./photo").mkdirs();
		
		SpringApplication.run(FaceRecoApplication.class, args);
	}

}
