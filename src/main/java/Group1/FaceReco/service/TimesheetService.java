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
	public Iterable<Timesheet> getAll() {
		return timesheetRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Timesheet> getById(@PathParam("id") long id) {
		return timesheetRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(Timesheet elem) {
		timesheetRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Timesheet elem) {
		timesheetRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") long id) {
		
		Optional<Timesheet> optional = timesheetRepository.findById(id);
		
		if(optional.isPresent()) {
			timesheetRepository.deleteById(id);
		}
	}
	
	@POST
	@Path("/{id}/recognition")
	@Consumes({"image/gif","image/jpeg","image/png","application/octet-stream"})
	public void recognition(@PathParam("id") long id, InputStream file) {
		System.out.println(id);
		System.out.println(file);
	}
}
