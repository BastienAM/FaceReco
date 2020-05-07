package Group1.FaceReco.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

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
import Group1.FaceReco.FaceRecognitionFiles.RecognitionResult;
import Group1.FaceReco.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.domain.Photo;
import Group1.FaceReco.domain.Presence;
import Group1.FaceReco.domain.PresenceId;
import Group1.FaceReco.domain.Student;
import Group1.FaceReco.domain.Timesheet;
import Group1.FaceReco.model.PresenceModel;
import Group1.FaceReco.model.TimesheetModel;

import static Group1.FaceReco.utils.StreamReaderFunctions.readStream;

@Service
@Path("/timesheet")
@Api(value = "Timesheet API")
public class TimesheetService {

	@Autowired
	private TimesheetRepository timesheetRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PresenceRepository presenceRepository;

	@Autowired
	private SignatureRepository signatureRepository;

	@Autowired
	private PhotoRepository photoRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne toutes les feuilles de présence de la base de données", response = Timesheet.class)
	public Iterable<Timesheet> getAll() {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetRead"))
			throw new AccessDeniedException("You don't have the permission.");

		return timesheetRepository.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la feuille de présence correspondant à l'identifiant passé en paramètre", response = Timesheet.class)
	public Optional<Timesheet> getById(
			@ApiParam(value = "L'identifiant de la feuille de présence", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetRead"))
			throw new AccessDeniedException("You don't have the permission.");

		return timesheetRepository.findById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute une feuille de présence dans la base de données")
	public void create(@ApiParam(value = "La feuille de présence à ajouter", required = true) TimesheetModel elem) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetCreate"))
			throw new AccessDeniedException("You don't have the permission.");

		Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Création de la feuille de présence en assignant les étudiants qui doivent
		// être présents
		Timesheet timesheet = new Timesheet();

		Set<Presence> presence = new HashSet<Presence>();
		Iterable<Student> iterableStudent = studentRepository.findAllById(elem.getStudent());

		List<Long> studentsIdList = new ArrayList<>();

		for (Student student : iterableStudent) {
			PresenceId pi = new PresenceId();
			pi.setStudent(student);
			pi.setTimesheet(timesheet);
			Presence p = new Presence();
			p.setId(pi);
			presence.add(p);

			studentsIdList.add(student.getNumber());
		}

		timesheet.setDate(Timestamp.valueOf(elem.getDate()));
		timesheet.setWording(elem.getWording());
		timesheet.setPresence(presence);
		timesheet.setAccount(account);

		timesheetRepository.save(timesheet);

		// Création du contexte avec les étudiants de la feuille de présence et
		// l'enregistre dans le dossier context
		FaceRecoApplication faceRecoApplication = new FaceRecoApplication();
		faceRecoApplication.training(studentsIdList);
		faceRecoApplication.save(Paths.get("./context/" + timesheet.getId() + ".xml"));
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie une feuille de présence dans la base de données")
	public void update(@ApiParam(value = "La feuille de présence à modifier", required = true) TimesheetModel elem) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetUpdate"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Timesheet> optional = timesheetRepository.findById(elem.getId());

		if (optional.isPresent()) {
			Timesheet timesheet = optional.get();

			Set<Student> currentStudent = new HashSet<Student>();
			Set<Student> newStudent = new HashSet<Student>();

			for (Presence presence : timesheet.getPresence()) {
				currentStudent.add(presence.getStudent());
			}

			Iterator<Student> it = studentRepository.findAllById(elem.getStudent()).iterator();

			while (it.hasNext()) {
				newStudent.add(it.next());
			}

			// Si les étudiants ont changés, on recrée le contexte et les présences
			if (!currentStudent.equals(newStudent)) {
				presenceRepository.deleteAll(timesheet.getPresence());

				Set<Presence> presence = new HashSet<Presence>();
				Iterable<Student> iterableStudent = studentRepository.findAllById(elem.getStudent());

				List<Long> studentsIdList = new ArrayList<>();

				for (Student student : iterableStudent) {
					PresenceId pi = new PresenceId();
					pi.setStudent(student);
					pi.setTimesheet(timesheet);
					Presence p = new Presence();
					p.setId(pi);
					presence.add(p);

					studentsIdList.add(student.getNumber());
				}

				// On supprime l'ancien contexte et en recrée un nouveau
				FaceRecoApplication faceRecoApplication = new FaceRecoApplication();
				faceRecoApplication.training(studentsIdList);

				java.nio.file.Path path = Paths.get("./context/" + timesheet.getId() + ".xml");

				File file = path.toFile();
				file.delete();

				faceRecoApplication.save(path);

			}

			timesheet.setDate(Timestamp.valueOf(elem.getDate()));
			timesheet.setWording(elem.getWording());

			timesheetRepository.save(timesheet);
		}
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime une feuille de présence dans la base de données")
	public void delete(
			@ApiParam(value = "L'identifiant de la feuille de présence à supprimer", required = true) @PathParam("id") long id) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetDelete"))
			throw new AccessDeniedException("You don't have the permission.");

		Optional<Timesheet> optional = timesheetRepository.findById(id);

		if (optional.isPresent()) {
			timesheetRepository.deleteById(id);

			// On supprime le contexte en relation avec la feuille de présence.
			File file = new File("./context/" + id + ".xml");
			file.delete();
		}
	}

	@POST
	@Path("/{id}/recognition")
	@Consumes({"image/jpeg", "image/png", "application/octet-stream" })
	@ApiOperation(value = "Reconnaît un élève par reconnaissance faciale", response = Student.class)
	public Student recognition(@PathParam("id") long id, InputStream file) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("Recognition"))
			throw new AccessDeniedException("You don't have the permission.");

		
		Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(id);

		if (!optionalTimesheet.isPresent())
			return null;

		//On ouvre le context de la feuille de présence
		FaceRecoApplication faceRecoApplication = new FaceRecoApplication();
		faceRecoApplication.load(Paths.get("./context/" + id + ".xml"));

		try {
			//On utilise la photo en paramètre pour la reconnaissance faciale
			byte[] temporaryImageInMemory = readStream(file);
			Mat inputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.IMREAD_GRAYSCALE);
			RecognitionResult recognitionResult = faceRecoApplication.recognition(inputImage);

			Optional<Student> optionalStudent = studentRepository.findById((long) recognitionResult.getLabel()[0]);
			Student student = null;

			if (optionalStudent.isPresent())
				student = optionalStudent.get();
			else
				return null;

			//Enregistre la photo après traitement dans le dossier tmp en attente de validation
			Mat treatedImage = faceRecoApplication.imageTreatment(inputImage);
			Imgcodecs.imwrite("./tmp/" + id + "-" + student.getNumber() + ".pgm", treatedImage);

			return student;

		} catch (IOException e) {
			System.out.println("An error occurred while reading the image.");
		}

		return null;
	}

	@POST
	@Path("/validatePresence")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Valide une présence dans la base de données")
	public void validatePresence(PresenceModel elem) {

		if (!((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole()
				.hasRight("TimesheetUpdate"))
			throw new AccessDeniedException("You don't have the permission.");

		Timesheet timesheet = new Timesheet();
		timesheet.setId(elem.getTimesheet());

		Student student = new Student();
		student.setNumber(elem.getStudent());

		Optional<Presence> optional = presenceRepository.findById(new PresenceId(timesheet, student));

		Presence presence = null;

		if (optional.isPresent())
			presence = optional.get();
		else
			return;

		if (elem.isPresent()) {
			//On regarde si la photo temporaire existe, et on la déplace dans le bon dossier
			java.nio.file.Path path = Paths
					.get("./tmp/" + presence.getTimesheet().getId() + "-" + presence.getStudent().getNumber() + ".pgm");

			if (path.toFile().exists()) {
				Photo photo = new Photo();
				photo.setStudent(presence.getStudent());
				signatureRepository.save(photo);

				try {
					Files.move(path,
							Paths.get("./photo/" + presence.getStudent().getNumber() + "/" + photo.getId() + ".pgm"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}else {
			//On supprime la photo temporaire
			File file = new File("./tmp/" + presence.getTimesheet().getId() + "-" + presence.getStudent().getNumber() + ".pgm");
			file.delete();
		}

		presence.setPresent(elem.isPresent());
		presenceRepository.save(presence);
	}
}
