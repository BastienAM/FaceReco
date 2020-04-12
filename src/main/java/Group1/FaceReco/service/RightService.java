package Group1.FaceReco.service;

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
import Group1.FaceReco.domain.Right;
import Group1.FaceReco.repository.RightRepository;

@Service
@Path("/right")
public class RightService {

	@Autowired
    private RightRepository rightRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Right> getAll() {
		return rightRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Right> getById(@PathParam("id") long id) {
		return rightRepository.findById(id);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Right elem) {
		rightRepository.save(elem);
	}
	
}
