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

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.repository.AccountRepository;

@Service
@Path("/user")
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Account> getAll() {
		return accountRepository.findAll();
	}

	@GET
	@Path("/{id_group}")
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Account> getById(@PathParam("id_group") long id) {
		return accountRepository.findById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(Account elem) {
		accountRepository.save(elem);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Account elem) {
		accountRepository.save(elem);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") long id) {

		Optional<Account> optional = accountRepository.findById(id);

		if (optional.isPresent()) {
			accountRepository.deleteById(id);
		}
	}
}
