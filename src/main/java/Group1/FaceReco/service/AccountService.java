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
import org.springframework.stereotype.Service;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.repository.AccountRepository;

@Service
@Path("/user")
@Api(value= "Account API")
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne la liste des utilisateurs de l'application", response = Account.class)
	public Iterable<Account> getAll() {
		return accountRepository.findAll();
	}

	@GET
	@Path("/{id_group}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retourne l'utilisateur correspondant à l'identifiant passé en paramaètre", response = Account.class)
	public Optional<Account> getById(@ApiParam(value = "L'identifiant de l'utilisateur", required = true)@PathParam("id_group") long id) {
		return accountRepository.findById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Ajoute un utilisateur")
	public void create(@ApiParam(value = "L'utilisateur à ajouter", required = true)Account elem) {
		accountRepository.save(elem);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifie un utilisateur")
	public void update(@ApiParam(value = "L'utilisateur à modifier", required = true) Account elem) {
		accountRepository.save(elem);
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Supprime un utilisateur")
	public void delete(@ApiParam(value = "L'identifiant de l'utilisateur à supprimer", required = true) @PathParam("id") long id) {

		Optional<Account> optional = accountRepository.findById(id);

		if (optional.isPresent()) {
			accountRepository.deleteById(id);
		}
	}
}
