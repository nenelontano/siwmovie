package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.RecensioneRepository;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    public Recensione deleteRecensione(Long recId, Long movieId){
        Recensione rec = this.recensioneRepository.findById(recId).orElse(null);
        Movie movie = this.movieRepository.findById(movieId).orElse(null);

        if(rec != null && movie!=null) {
            movie.getRecensioni().remove(rec);
            rec.getAutore().getRecensioni().remove(rec);

            this.recensioneRepository.save(rec);
            this.movieRepository.save(movie);
            this.userRepository.save(rec.getAutore());
        }
        return rec;
    }

    public void newRecensione(Recensione rec, Long idU, Long idM, Model model) {
        Movie movie = this.movieRepository.findById(idM).get();
        User user = this.userRepository.findById(idU).get();
    	

        if(movie != null && user != null) {

            rec.setAutore(user);
            rec.setMovie(movie);
            movie.getRecensioni().add(rec);
            user.getRecensioni().add(rec);

            this.recensioneRepository.save(rec);
            this.movieRepository.save(movie);
            this.userRepository.save(user);
        }
    }

    public boolean existsByAutoreAndMovie(Long movieId, Long autoreId) {
        User autore = userRepository.findById(autoreId).orElse(null);
        Movie movie = movieRepository.findById(movieId).orElse(null);

        return this.recensioneRepository.existsByAutoreAndMovie(autore, movie);
    }

}
