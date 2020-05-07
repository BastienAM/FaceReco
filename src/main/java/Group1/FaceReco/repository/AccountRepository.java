package Group1.FaceReco.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Group1.FaceReco.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{

	List<Account> findByUsername(String username);
}
