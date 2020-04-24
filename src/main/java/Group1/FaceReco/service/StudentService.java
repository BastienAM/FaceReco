package Group1.FaceReco.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Group;
import Group1.FaceReco.domain.Promotion;
import Group1.FaceReco.domain.Student;
import Group1.FaceReco.repository.GroupRepository;
import Group1.FaceReco.repository.PromotionRepository;
import Group1.FaceReco.repository.StudentRepository;

@Service
@Path("/student")
@Api(value = "Student API")
public class StudentService {
	
	@Autowired
    private StudentRepository studentRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private PromotionRepository promotionRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne tous les étudiants contenues dans la base de données", response = Student.class)
	public Iterable<Student> getAll() {
		return studentRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne l'étudiant correspondant au numéro étudiant passé en paramètre", response = Student.class)
	public Optional<Student> getById(@ApiParam(value = "Le numéro d'étudiant ", required = true)@PathParam("id") long id) {
		return studentRepository.findById(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute un élève dans la base de données")
	public void create(@ApiParam(value = "L'étudiant à ajouter", required = true)Student elem) {
		studentRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un élève dans la base de données")
	public void update(@ApiParam(value = "L'étudiant à modifier", required = true)Student elem) {
		studentRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime un élève dans la base de données")
	public void delete(@ApiParam(value = "Le numéro étudiant de l'élève à supprimer", required = true)@PathParam("id") long id) {
		
		Optional<Student> optional = studentRepository.findById(id);
		
		if(optional.isPresent()) {
			Student student = optional.get();
			if(student.getPresence().size() == 0) {
				studentRepository.deleteById(id);
			}
		}
	}
	
	@GET
	@Path("/group/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la liste des élèves en lien avec un groupe", response = Student.class)
	public Set<Student> getByGroup(@ApiParam(value = "L'identifiant du groupe", required = true)@PathParam("id") long id) {
		Optional<Group> optional = groupRepository.findById(id);
		
		if(optional.isPresent()) {
			Group group = optional.get();
			return group.getStudent();
		}
		return null;
	}
	
	@GET
	@Path("/promotion/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la liste des élèves en lien avec une promotion", response = Student.class)
	public Set<Student> getByPromotion(@ApiParam(value = "L'identifiant de la promotion", required = true)@PathParam("id") long id) {
		Optional<Promotion> optional = promotionRepository.findById(id);
		
		if(optional.isPresent()) {
			Set<Student> set = new HashSet<Student>();
			for(Group group : optional.get().getGroup()) {
				set.addAll(group.getStudent());
			}
			return set;
		}
		return null;
	}
	
	
	@POST
	@Path("/{id}/photo")
	@Consumes({"image/gif","image/jpeg","image/png","application/octet-stream"})
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Ajoute une photo à un étudiant")
	public void addPhoto(@ApiParam(value = "L'image à ajouter", required = true)InputStream file) {
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
