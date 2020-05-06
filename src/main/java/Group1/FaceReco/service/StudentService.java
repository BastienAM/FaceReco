package Group1.FaceReco.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import Group1.FaceReco.FaceRecognitionFiles.FaceRecoApplication;
import io.swagger.annotations.*;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.domain.Group;
import Group1.FaceReco.domain.Promotion;
import Group1.FaceReco.domain.Photo;
import Group1.FaceReco.domain.Student;
import Group1.FaceReco.repository.GroupRepository;
import Group1.FaceReco.repository.PromotionRepository;
import Group1.FaceReco.repository.SignatureRepository;
import Group1.FaceReco.repository.StudentRepository;

import static Group1.FaceReco.utils.StreamReaderFunctions.readStream;

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
	@Autowired
	private SignatureRepository signatureRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne tous les étudiants contenues dans la base de données", response = Student.class)
	public Iterable<Student> getAll() {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentRead"))
			throw new AccessDeniedException("You don't have the permission.");

		return studentRepository.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne l'étudiant correspondant au numéro étudiant passé en paramètre", response = Student.class)
	public Optional<Student> getById(
			@ApiParam(value = "Le numéro d'étudiant ", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentRead"))
			throw new AccessDeniedException("You don't have the permission.");

		return studentRepository.findById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute un élève dans la base de données")
	public void create(@ApiParam(value = "L'étudiant à ajouter", required = true) Student elem) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentCreate"))
			throw new AccessDeniedException("You don't have the permission.");

		studentRepository.save(elem);
		
		//Crée le dossier correspondant à l'étudiant
		new File("./photo/" + elem.getNumber()).mkdirs();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un élève dans la base de données")
	public void update(@ApiParam(value = "L'étudiant à modifier", required = true) Student elem) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentUpdate"))
			throw new AccessDeniedException("You don't have the permission.");

		studentRepository.save(elem);
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime un élève dans la base de données")
	public void delete(
			@ApiParam(value = "Le numéro étudiant de l'élève à supprimer", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentDelete"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Student> optional = studentRepository.findById(id);

		if (optional.isPresent()) {
			Student student = optional.get();
			if (student.getPresence().size() == 0) {
				studentRepository.deleteById(id);

				//Supprime le dossier et les photos dans son dossier
				try {
					FileUtils.deleteDirectory(new File("./photo/" + student.getNumber()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@GET
	@Path("/group/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la liste des élèves en lien avec un groupe", response = Student.class)
	public Set<Student> getByGroup(
			@ApiParam(value = "L'identifiant du groupe", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentRead"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Group> optional = groupRepository.findById(id);

		if (optional.isPresent()) {
			Group group = optional.get();
			return group.getStudent();
		}
		return null;
	}

	@GET
	@Path("/promotion/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la liste des élèves en lien avec une promotion", response = Student.class)
	public Set<Student> getByPromotion(
			@ApiParam(value = "L'identifiant de la promotion", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentRead"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Promotion> optional = promotionRepository.findById(id);

		if (optional.isPresent()) {
			Set<Student> set = new HashSet<Student>();
			for (Group group : optional.get().getGroup()) {
				set.addAll(group.getStudent());
			}
			return set;
		}
		return null;
	}

	@POST
	@Path("/{id}/photo")
	@Consumes({ "image/gif", "image/jpeg", "image/png", "application/octet-stream" })
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Ajoute une photo à un étudiant")
	public void addPhoto(@ApiParam(value = "L'image à ajouter", required = true) InputStream file,
			@PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("StudentUpdate"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Student> optional = studentRepository.findById(id);

		//Ajoute une photo dans la base de données
		Student student = optional.get();
		Photo photo = new Photo();
		photo.setStudent(student);
		signatureRepository.save(photo);

		FaceRecoApplication faceRecoApplication = new FaceRecoApplication();

		//Fait les traitement sur l'image et l'enregistre dans le bon dossier
		try {
			byte[] temporaryImageInMemory = readStream(file);

			Mat inputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.IMREAD_GRAYSCALE);
			Mat treatedImage = faceRecoApplication.imageTreatment(inputImage);
			Imgcodecs.imwrite("./photo/" + id + "/" + photo.getId() + ".pgm", treatedImage);

		} catch (IOException e) {
			System.out.println("An error occurred while reading the image.");
		}

	}
}
