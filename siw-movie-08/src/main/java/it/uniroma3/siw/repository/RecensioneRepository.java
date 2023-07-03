package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    public boolean existsByAutoreAndMovie(User autore, Movie movie);
}
