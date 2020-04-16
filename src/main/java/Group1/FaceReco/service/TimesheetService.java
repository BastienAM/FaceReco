package Group1.FaceReco.service;

import java.io.InputStream;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Group1.FaceReco.domain.Timesheet;
import Group1.FaceReco.repository.TimesheetRepository;

@Service
@Path("/timesheet")
public class TimesheetService {
	
	@Autowired
    private TimesheetRepository timesheetRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne toutes les feuilles de présence de la base de données", response = Timesheet.class)
	public Iterable<Timesheet> getAll() {
		return timesheetRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la feuille de présence correspondant à l'identifiant passé en paramètre", response = Timesheet.class)
	public Optional<Timesheet> getById(@ApiParam(value = "L'identifiant de la feuille de présence", required = true) @PathParam("id") long id) {
		return timesheetRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute une feuille de présence dans la base de données")
	public void create(@ApiParam(value = "La feuille de présence à ajouter", required = true) Timesheet elem) {
		timesheetRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie une feuille de présence dans la base de données")
	public void update(@ApiParam(value = "La feuille de présence à modifier", required = true) Timesheet elem) {
		timesheetRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime une feuille de présence dans la base de données")
	public void delete(@ApiParam(value = "L'identifiant de la feuille de présence à supprimer", required = true) @PathParam("id") long id) {
		
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
		System.out.println(id);
		System.out.println(file);
	}
}
