package Group1.FaceReco.service;
import java.io.IOException;
import java.io.InputStream;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.face.Face;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.domain.Presence;
import Group1.FaceReco.domain.PresenceId;
import Group1.FaceReco.domain.Student;
import Group1.FaceReco.domain.Timesheet;
import Group1.FaceReco.model.TimesheetModel;
import Group1.FaceReco.repository.StudentRepository;
import Group1.FaceReco.repository.TimesheetRepository;

import static Group1.FaceReco.utils.StreamReaderFunctions.readStream;

@Service
@Path("/timesheet")
@Api(value = "Timesheet API")
public class TimesheetService {
	
	@Autowired
    private TimesheetRepository timesheetRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne toutes les feuilles de présence de la base de données", response = Timesheet.class)
	public Iterable<Timesheet> getAll() {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("TimesheetRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		return timesheetRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la feuille de présence correspondant à l'identifiant passé en paramètre", response = Timesheet.class)
	public Optional<Timesheet> getById(@ApiParam(value = "L'identifiant de la feuille de présence", required = true) @PathParam("id") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("TimesheetRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		return timesheetRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute une feuille de présence dans la base de données")
	public void create(@ApiParam(value = "La feuille de présence à ajouter", required = true) TimesheetModel elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("TimesheetCreate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		Timesheet timesheet = new Timesheet();
		timesheet.setDate(Timestamp.valueOf(elem.getDate()));
		
		Account account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Set<Presence> presence = new HashSet<Presence>();
		Iterable<Student> iterableStudent  = studentRepository.findAllById(elem.getStudent());

		List<Long> studentsIdList = new ArrayList<>();

		for(Student student : iterableStudent) {
			PresenceId pi = new PresenceId();
			pi.setStudent(student);
			pi.setTimesheet(timesheet);
			Presence p = new Presence();
			p.setId(pi);
			presence.add(p);

			studentsIdList.add(student.getNumber());
		}

		FaceRecoApplication faceRecoApplication = new FaceRecoApplication();
		faceRecoApplication.training(studentsIdList);
		faceRecoApplication.save(Paths.get("context\\" + timesheet.getId() + ".xml"));

		timesheet.setPresence(presence);
		timesheet.setAccount(account);
		timesheetRepository.save(timesheet);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie une feuille de présence dans la base de données")
	public void update(@ApiParam(value = "La feuille de présence à modifier", required = true) Timesheet elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("TimesheetUpdate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		timesheetRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime une feuille de présence dans la base de données")
	public void delete(@ApiParam(value = "L'identifiant de la feuille de présence à supprimer", required = true) @PathParam("id") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("TimesheetDelete"))
			throw new AccessDeniedException("You don't have the permission.");
		
		Optional<Timesheet> optional = timesheetRepository.findById(id);
		
		if(optional.isPresent()) {
			timesheetRepository.deleteById(id);
		}
	}
	
	@POST
	@Path("/{id}/recognition")
	@Consumes({"image/gif","image/jpeg","image/png","application/octet-stream"})
	@ApiOperation(value = "Reconnaît un élève par reconnaissance faciale")
	public void recognition(@PathParam("id") long id, InputStream file) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("Recognition"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		FaceRecoApplication faceRecoApplication = new FaceRecoApplication();
		faceRecoApplication.load(Paths.get("context\\" + id + ".xml"));

		try {
			byte[] temporaryImageInMemory = readStream(file);

			Mat inputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.IMREAD_GRAYSCALE);
			faceRecoApplication.recognition(inputImage);
			System.out.println(id);
			System.out.println(file);
		}
		catch (IOException e){
			System.out.println("An error occurred while reading the image.");
		}
	}
}
