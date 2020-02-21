package Group1.FaceReco.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

@Service
@Path("/student")
public class StudentService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStudent() {
		return "test";
	}
}
