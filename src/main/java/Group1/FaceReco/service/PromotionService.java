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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.domain.Promotion;
import Group1.FaceReco.repository.PromotionRepository;

@Service
@Path("/promotion")
@Api("Promotion API")
public class PromotionService {
	
	@Autowired
	private PromotionRepository promotionRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne les promotions présentes dans la base de données", response = Promotion.class)
	public Iterable<Promotion> getAllPromotion() {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("PromotionRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		return promotionRepository.findAll();
	}
	
	@GET
	@Path("/{id_promotion}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la promotion correspondante à l'identifiant passé en paramètre", response = Promotion.class)
	public Optional<Promotion> getPromotionById(@ApiParam(value = "L'identifiant de la promotion", required = true)@PathParam("id_promotion") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("PromotionRead"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		return promotionRepository.findById(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute une promotion dans la base de données")
	public void createPromotion(@ApiParam(value = "La promotion à ajouter", required = true)Promotion elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("PromotionCreate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		promotionRepository.save(elem);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie une promotion dans la base de données")
	public void updatePromotion(@ApiParam(value = "La promotion à modifier", required = true)Promotion elem) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("PromotionUpdate"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		promotionRepository.save(elem);
	}
	
	@DELETE
	@Path("/{id_promotion}")
	@ApiOperation(value = "Supprime une promotion dans la base de données")
	public void deletePromotion(@ApiParam(value = "L'identifiant de la promotion à supprimer", required = true)@PathParam("id_promotion") long id) {
		
		if(!((Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole().hasRight("PromotionDelete"))
			throw new AccessDeniedException("You don't have the permission.");
		
		
		Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
		
		if(optionalPromotion.isPresent()) {
			Promotion promotion = optionalPromotion.get();
			if(promotion.getGroup().size() == 0) {
				promotionRepository.deleteById(id);
			}
		}
	}
}
