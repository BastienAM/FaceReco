package Group1.FaceReco.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Test;
import Group1.FaceReco.domain.TestRepository;

@Service
@Path("/student")
public class StudentService {

    @Autowired
    private TestRepository repository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStudent() {
		System.out.println("getAllStudent");
		
		Iterator<Test> it = repository.findAll().iterator();
		
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
		
		return "test";
	}
	
	@POST
	@Path("/addImage")
	@Consumes({"image/jpeg"})
	//@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public String addPhoto(InputStream file) {
		System.out.println("add Image");
		if(file != null)
			System.out.println("non nulle");
		return "OK";
	}
	
	@GET
	@Path("/addImage")
	@Produces(MediaType.TEXT_PLAIN)
	public String getPhoto() {
		return "OK";
	}
	
	/**
	 * Utility method to save InputStream data to target location/file
	 * 
	 * @param inStream
	 *            - InputStream to be saved
	 * @param target
	 *            - full path to destination file
	 */
	private void saveToFile(InputStream inStream, String target)
			throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
}
