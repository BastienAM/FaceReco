package Group1.FaceReco.service;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
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
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("RightRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		return rightRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne le droit correspondant à l'identifiant passé en paramètre", response = Right.class)
	public Optional<Right> getById(@ApiParam(value = "L'identifiant du droit", required = true)@PathParam("id") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("RightRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		return rightRepository.findById(id);
	}
	
}
