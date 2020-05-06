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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.domain.Group;
import Group1.FaceReco.repository.GroupRepository;

@Service
@Path("/group")
@Api("Group API")
public class GroupService {
	
	@Autowired
    private GroupRepository groupRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne les groupes définis dans la base de données", response = Group.class)
	public Iterable<Group> getAll() {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("GroupRead"))
		throw new AccessDeniedException("You don't have the permission.");
		
		return groupRepository.findAll();
	}
	
	@GET
	@Path("/{id_group}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne le groupe correspondant à l'identifiant passé en paramètre", response = Group.class)
	public Optional<Group> getById(@ApiParam(value = "L'identifiant du groupe", required = true)@PathParam("id_group") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("GroupRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		return groupRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute un groupe dans la base")
	public void create(@ApiParam(value = "Le groupe à ajouter", required = true)Group elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("GroupCreate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		groupRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un groupe dans la base")
	public void update(@ApiParam(value = "Le groupe à modifier", required = true)Group elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("GroupUpdate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		groupRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime un groupe dans la base")
	public void delete(@ApiParam(value = "L'identifiant du groupe à supprimer", required = true)@PathParam("id") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("GroupDelete"))
			throw new AccessDeniedException("You don't have the permission.");
		
		Optional<Group> optional = groupRepository.findById(id);
		
		if(optional.isPresent()) {
			Group group = optional.get();
			if(group.getStudent().size() == 0) {
				groupRepository.deleteById(id);
			}
		}
	}
}
