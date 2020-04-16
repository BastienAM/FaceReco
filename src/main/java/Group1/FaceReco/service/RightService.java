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

import Group1.FaceReco.domain.Promotion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Group1.FaceReco.domain.Right;
import Group1.FaceReco.repository.RightRepository;

@Service
@Path("/right")
@Api("Right API")
public class RightService {

	@Autowired
    private RightRepository rightRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne les droits définis dans la base de données", response = Right.class)
	public Iterable<Right> getAll() {
		return rightRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne le droit correspondant à l'identifiant passé en paramètre", response = Right.class)
	public Optional<Right> getById(@ApiParam(value = "L'identifiant du droit", required = true)@PathParam("id") long id) {
		return rightRepository.findById(id);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un droit dans la base de données")
	public void update(@ApiParam(value = "Le droit à modifier", required = true)Right elem) {
		rightRepository.save(elem);
	}
	
}
