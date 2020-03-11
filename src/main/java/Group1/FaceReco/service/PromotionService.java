package Group1.FaceReco.service;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Promotion;
import Group1.FaceReco.domain.PromotionRepository;


@Service
@Path("/promotion")
public class PromotionService {
	
	@Autowired
    private PromotionRepository repository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterator<Promotion> getAllPromotion() {
		Iterator<Promotion> it = repository.findAll().iterator();
		return it;
	}

}
