package it.uniroma3.siw.controller;


import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.validator.MovieValidator;

import java.io.IOException;

import javax.validation.Valid;

@Controller
public class MovieController {
	
	@Autowired
	private MovieService movieService;
	
	@Autowired 
	private MovieValidator movieValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private ArtistService artistService;

	@Autowired
	private RecensioneService recensioneService;
	
	@GetMapping(value="/admin/indexMovie")
	public String indexMovie(Model model) {
		return "admin/indexMovie.html";
	}
	
	//28/03 aggiungo nel modello la lista di tutti i film
	@GetMapping(value = "/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "admin/manageMovies.html";
	}
	
	/*Risponde con una pagina che contiene la form per inserire i dati di un nuovo film*/
	@GetMapping(value="/admin/formNewMovie") 
	public String formNewMovie(Model model) { 
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}
	
	/*Gestisce i dati di un nuovo film raccolti dalla form: se il film non esiste, 
	 * salva i dati nel db e risponde con una pagina che mostra i dati così come 
	 * sono salvati nel db, se il film esiste già risponde con la pagina che mostra
	 *  la form e con un messaggio di errore (il film esiste)*/
	@PostMapping("/admin/movie") 
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile[] file) throws IOException { 
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieService.newMovie(movie, file, model);   //se non inserisci la foto da errore
			model.addAttribute("movie", movie); 
			return "redirect:/movies/" + movie.getId();
		} else { 
			return "admin/formNewMovie.html";
		}
	}

	private Credentials getCredentials() {
		if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			return credentials;
		}
		return null;
	}
	
	/*Risponde con con una pagina che mostra i dati del film con il codice 
	 * specificato nell'ultima parte dell'URL*/
	@GetMapping("/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.findMovieById(id);
		if(movie!=null) {
			model.addAttribute("movie", movie);
			model.addAttribute("recensione", new Recensione());
			if (this.getCredentials() != null) {
				model.addAttribute("giaRecensito", this.recensioneService.existsByAutoreAndMovie(id,
						this.getCredentials().getUser().getId()));
				model.addAttribute("credentials", this.getCredentials());
			}
			return "movie.html";		}
		else {
			return "/movieError.html";
		}
	}
	
	/*Risponde con con una pagina che mostra la lista di tutti i film*/
	@GetMapping("/movies")
	public String showMovies(Model model) {
		
		/*UserDetails userDetails =  (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());*/
		
		model.addAttribute("movies", this.movieService.findAllMovie());
		//model.addAttribute("user", credentials.getUser());
		return "movies.html";
	}
	
	/*Risponde con una pagina che mostra la form per cercare un film per anno*/
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() { 

		return "formSearchMovies.html";
	}
	
	/*Gestisce i dati (un intero) per la ricerca di film per anno: risponde con
	 *  una pagina con la lista dei film usciti nell'anno specificato dall'utente*/
	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) { 
		model.addAttribute("movies", this.movieService.findByYear(year));
		return "foundMovies.html";
	}
	
	//28/03 
	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = movieService.findMovieById(id);
		if(movie!=null) 
			model.addAttribute("movie", movie);
		else {
			return "movieError.html";
		}
		return "admin/formUpdateMovie.html";
	}

	@PostMapping(value="/admin/updateMovie/{id}")
	public String updateMovie(@PathVariable("id") Long id, @Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
		if(!bindingResult.hasErrors()) {
			this.movieService.updateMovie(id, movie);
			return "redirect:/movies/" + id;
		}
		return "movieError";
	}

	@PostMapping (value="/admin/updateMoviePicture/{id}")
	public String updateMoviePicture(@PathVariable("id") Long id,@RequestParam("file") MultipartFile[] file,  Model model) throws IOException {
		Movie movie = movieService.findMovieById(id);
		if(movie!=null) {
			movieService.updateMoviePicture(id, file);
			return "redirect:/movies/" + id;
		} else {
			return "movieError.html";
		}
	}
	
	//28/03
	@GetMapping(value="/admin/addDirectorToMovie/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		Movie movie = this.movieService.findMovieById(id);
		if(movie != null) {
			model.addAttribute("movie", movie);
			return "admin/directorsToAdd.html";
		} else {
			return "movieError.html";
		}
	}
	
	//28/03
	@GetMapping(value="/admin/setDirectorToMovie/{idArtist}/{idMovie}")
	public String setDirectorToMovie(@PathVariable("idArtist") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		
		Movie movie = this.movieService.saveDirectorToMovie(idM, idA);
		if(movie!=null) {
			model.addAttribute("movie",movie);
			return "admin/formUpdateMovie.html";
		} else {
			return "movieError.html";
		}
		
		
	}
	
	@GetMapping("/admin/updateActorsOnMovie/{id}")
	public String updateActorsOnMovie(@PathVariable("id") Long id, Model model) {
		
		Movie movie = this.movieService.findMovieById(id);
		if(movie!=null) {
			model.addAttribute("movie", movie);
			model.addAttribute("artists", this.artistService.findActorsInMovie(movie));
			model.addAttribute("notMovieActors", this.artistService.findActorsNotInMovie(movie));
			return "admin/actorsToAdd.html";
			
		} else {
			return "movieError.html";
		}
	}
	
	@GetMapping(value="/admin/addActorToMovie/{idActor}/{idMovie}")
	public String addActorToMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = this.movieService.saveActorToMovie(idM, idA);
		
		if(movie != null) {
			model.addAttribute("movie", movie);
			model.addAttribute("artists", this.artistService.findActorsInMovie(movie));
			model.addAttribute("notMovieActors", this.artistService.findActorsNotInMovie(movie));
			
			return "admin/actorsToAdd.html";
		} else {
			return "movieError.html";
		}
		
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActorFromMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = this.movieService.deleteActorFromMovie(idA, idM);
		
		if(movie != null) {
			model.addAttribute("movie", movie);
			model.addAttribute("artists", this.artistService.findActorsInMovie(movie));   
			model.addAttribute("notMovieActors", this.artistService.findActorsNotInMovie(movie));
			return "admin/actorsToAdd.html";
		} else {
			return "movieError.html";
		}


	}
}
 