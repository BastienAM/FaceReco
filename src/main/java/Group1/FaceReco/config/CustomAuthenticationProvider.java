package Group1.FaceReco.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import Group1.FaceReco.domain.Account;
import Group1.FaceReco.repository.AccountRepository;

/**
 * 
 * Basic Authentification avec BCrypt et la base de données
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		List<Account> list = accountRepository.findByUsername(name);
		
		for(Account account : list) {
			if(encoder.matches(password, account.getPassword()))
				return new UsernamePasswordAuthenticationToken(account, password, new ArrayList<>());
		}
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
