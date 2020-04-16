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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Role;
import Group1.FaceReco.repository.RoleRepository;

@Service
@Path("/role")
public class RoleService {
	
	@Autowired
    private RoleRepository roleRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne les rôles définis dans la base de données", response = Role.class)
	public Iterable<Role> getAll() {
		return roleRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne le rôle correspondant à l'identifiant passé en paramètre", response = Role.class)
	public Optional<Role> getById(@ApiParam(value = "L'identifiant du rôle", required = true) @PathParam("id") long id) {
		return roleRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute un rôle dans la base de données")
	public void create(@ApiParam(value = "Le rôle à ajouter", required = true)Role elem) {
		roleRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un rôle dans la base de données")
	public void update(@ApiParam(value = "Le rôle à modifier", required = true)Role elem) {
		roleRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime un rôle dans la base de données")
	public void delete(@ApiParam(value = "L'identifiant du rôle à supprimer", required = true) @PathParam("id") long id) {
		
		Optional<Role> optional = roleRepository.findById(id);
		
		if(optional.isPresent()) {
			Role role = optional.get();
			/*if(group.getStudent().size() == 0) {
				groupRepository.deleteById(id);
			}*/
		}
	}
}
