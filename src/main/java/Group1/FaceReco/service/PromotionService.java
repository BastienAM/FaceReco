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

import Group1.FaceReco.domain.Promotion;
import Group1.FaceReco.repository.PromotionRepository;

@Service
@Path("/promotion")
public class PromotionService {
	
	@Autowired
	private PromotionRepository promotionRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Promotion> getAllPromotion() {
		return promotionRepository.findAll();
	}
	
	@GET
	@Path("/{id_promotion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Promotion> getPromotionById(@PathParam("id_promotion") long id) {
		return promotionRepository.findById(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createPromotion(Promotion elem) {
		promotionRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updatePromotion(Promotion elem) {
		promotionRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id_promotion}")
	public void deletePromotion(@PathParam("id_promotion") long id) {
		
		Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
		
		if(optionalPromotion.isPresent()) {
			Promotion promotion = optionalPromotion.get();
			if(promotion.getGroup().size() == 0) {
				promotionRepository.deleteById(id);
			}
		}
	}
}
