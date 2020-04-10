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

import Group1.FaceReco.domain.Group;
import Group1.FaceReco.repository.GroupRepository;

@Service
@Path("/group")
public class GroupService {
	
	@Autowired
    private GroupRepository groupRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Group> getAllGroup() {
		return groupRepository.findAll();
	}
	
	@GET
	@Path("/{id_group}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Group> getGroupById(@PathParam("id_group") long id) {
		return groupRepository.findById(id);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createGroup(Group elem) {
		groupRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateGroup(Group elem) {
		groupRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id_group}")
	public void deleteGroup(@PathParam("id_group") long id) {
		
		Optional<Group> optional = groupRepository.findById(id);
		
		if(optional.isPresent()) {
			Group group = optional.get();
			if(group.getStudent().size() == 0) {
				groupRepository.deleteById(id);
			}
		}
	}
}
