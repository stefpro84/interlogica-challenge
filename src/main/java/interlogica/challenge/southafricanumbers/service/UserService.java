package interlogica.challenge.southafricanumbers.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import interlogica.challenge.southafricanumbers.entity.User;
import interlogica.challenge.southafricanumbers.repository.UserRepository;

@Service
public class UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Retrieve a User by its username.
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	public User getUser (String username) throws UsernameNotFoundException {
		
		logger.debug("searching user {}...", username);
		Optional<User> optional = userRepository.findByUsername(username);
		
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException("User '" + username + "' does not exists.");
		}
		
		logger.debug("user found");
		return optional.get();
	}
		
}
