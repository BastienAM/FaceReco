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

import Group1.FaceReco.domain.Role;
import Group1.FaceReco.repository.RoleRepository;

@Service
@Path("/role")
public class RoleService {
	
	@Autowired
    private RoleRepository roleRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Role> getAll() {
		return roleRepository.findAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Role> getById(@PathParam("id") long id) {
		return roleRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(Role elem) {
		roleRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Role elem) {
		roleRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") long id) {
		
		Optional<Role> optional = roleRepository.findById(id);
		
		if(optional.isPresent()) {
			Role role = optional.get();
			/*if(group.getStudent().size() == 0) {
				groupRepository.deleteById(id);
			}*/
		}
	}
}
