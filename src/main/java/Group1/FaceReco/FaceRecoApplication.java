package Group1.FaceReco;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceRecoApplication {
	
	public static void main(String[] args) {
		
		//Chargement de la librairie native d'openCV_contrib.
		System.loadLibrary("opencv_contrib-4.2");
		
		//Création des dossiers s'ils existent pas 
		new File("./context").mkdirs();
		new File("./photo").mkdirs();
		new File("./tmp").mkdirs();
		
		SpringApplication.run(FaceRecoApplication.class, args);
	}

}
