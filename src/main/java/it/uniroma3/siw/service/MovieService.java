package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.MovieRepository;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	public Movie findMovieById(Long id) {
		return this.movieRepository.findById(id).orElse(null);
	}
	
	public Iterable<Movie> findAllMovie() {
		return this.movieRepository.findAll();
	}
	
	public Movie saveMovie(Movie movie) {
		return this.movieRepository.save(movie);
	}
	
	public List<Movie> findByYear(Integer year) {
		return this.movieRepository.findByYear(year);
	}
	
	public Movie saveDirectorToMovie(Long movieId, Long artistId) {
		Movie res= null;
		Movie movie = this.movieService.findMovieById(movieId);
		Artist director = this.artistRepository.findById(artistId).orElse(null);
		if(movie!=null && director!= null) {
			movie.setDirector(director);
			this.saveMovie(movie);
			res=movie;
		}
		return res;
	}
	
	public Movie saveActorToMovie(Long movieId, Long artistId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		Artist actor = this.artistRepository.findById(artistId).orElse(null);
		
		if(movie != null && actor != null) {
			if(!movie.getActors().contains(actor)) {
				actor.getStarredMovies().add(movie);
				movie.getActors().add(actor);
				artistRepository.save(actor);
				movieRepository.save(movie);
			}
		}
		return movie;
	}
	
	public Movie deleteActorFromMovie(Long artistId, Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		Artist actor = this.artistRepository.findById(artistId).orElse(null);
		
		if(movie != null && actor != null) {
			movie.getActors().remove(actor);
			actor.getStarredMovies().remove(movie);
			
			this.artistRepository.save(actor);
			this.movieRepository.save(movie);
		}
		return movie;
	}
	
	
	@Transactional
	public void newMovie(Movie movie, @RequestParam("file") MultipartFile[] file, Model model) throws IOException {
		movie.setImages(new HashSet<Image>());
		for (MultipartFile f : file) {
			System.out.println(f.toString());
		}
		for (MultipartFile f : file) {
			Image image = new Image();
			image.setName(f.getResource().getFilename());
			image.setData(f.getBytes());
			this.imageRepository.save(image);
			movie.getImages().add(image);
		}
		this.movieRepository.save(movie);
	}

	@Transactional
	public void updateMoviePicture(Long id, MultipartFile[] file) throws IOException {
		Movie movie = this.findMovieById(id);
		for (MultipartFile f : file) {
			Image img = new Image();
			img.setName(f.getResource().getFilename());
			img.setData(f.getBytes());
			this.imageRepository.save(img);
			movie.getImages().add(img);
		}
		this.movieRepository.save(movie);
	}
}
