package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	protected UserRepository userRepository;
	
	/*recupero utente da database in base al suo id*/
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	/*salvo utente nel database*/
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	/*ritorna la lista di user memorizzati nel database*/
	@Transactional
	public List<User> getAllUser() {
		List<User> result = new ArrayList<>();
		Iterable<User> iterable = this.userRepository.findAll();
		for(User user : iterable)
			result.add(user);
		return result;
	}
}
