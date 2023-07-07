package it.uniroma3.siw.controller;

import javax.validation.Valid;

import it.uniroma3.siw.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.validator.ArtistValidator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class ArtistController {
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired ArtistValidator artistValidator;

	@GetMapping(value="/admin/indexArtist")
	public String indexArtist(Model model) {
		return "admin/indexArtist.html";
	}
	
	@GetMapping(value="/admin/formNewArtist") 
	public String formNewArtist(Model model) { 
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@PostMapping("/artist") 
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model) { 
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.artistService.createNewArtist(artist);
			model.addAttribute("artist", artist); 
			return "artist.html";
		} else {
			return "admin/formNewArtist.html";
		}
	}
	
	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = this.artistService.findArtistById(id);
		if(artist != null) {
			model.addAttribute("artist", artist); 
			return "artist.html";
		} else {
			return "artistError.html";
		}
	}
	
	@GetMapping("/artist")
	public String showArtists(Model model) { 
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "artists.html";
	}
	
	
	@GetMapping("/formSearchArtists") 
	public String formSearchArtists() { 

		return "formSearchArtists.html";
	}
	
	@PostMapping("/searchArtists")
	public String searchArtists(Model model, @RequestParam String name) { 
		model.addAttribute("artists", this.artistService.findByName(name));
		return "foundArtists.html";
	}

	@GetMapping(value="/admin/formUpdateArtist/{id}")
	public String formUpdateArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = artistService.findArtistById(id);
		if(artist!=null)
			model.addAttribute("artist", artist);
		else {
			return "artistError.html";
		}
		return "admin/formUpdateArtist.html";
	}

	@GetMapping(value = "/admin/manageArtists")
	public String manageArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "admin/manageArtists.html";
	}
	@PostMapping (value="/admin/updateArtistPicture/{id}")
	public String updateArtistPicture(@PathVariable("id") Long id, @RequestParam("file") MultipartFile image, Model model) throws IOException {
		Artist artist = artistService.findArtistById(id);
		if(artist!=null) {
			artistService.updateArtistPicture(id, image);
			return "redirect:/artist/" + id;
		} else {
			return "artistError.html";
		}
	}
}
