package Group1.FaceReco.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Student;
import Group1.FaceReco.repository.StudentRepository;

@Service
@Path("/student")
public class StudentService {
	
	@Autowired
    private StudentRepository studentRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Student> getAll() {
		return studentRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Student> getById(@PathParam("id") long id) {
		return studentRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(Student elem) {
		studentRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Student elem) {
		studentRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") long id) {
		
		Optional<Student> optional = studentRepository.findById(id);
		
		if(optional.isPresent()) {
			Student student = optional.get();
			if(student.getPresence().size() == 0) {
				studentRepository.deleteById(id);
			}
		}
	}
	
	
	@POST
	@Path("/{id}/photo")
	@Consumes({"image/gif","image/jpeg","image/png","application/octet-stream"})
	@Produces(MediaType.TEXT_PLAIN)
	public void addPhoto(InputStream file) {
		System.out.println("add Image");
		if(file != null)
			System.out.println("non nulle");
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
