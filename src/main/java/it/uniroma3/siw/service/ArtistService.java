package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArtistService {
	
	@Autowired 
	ArtistRepository artistRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	@Transactional
	public boolean createNewArtist(Artist artist) {
		boolean res=false;
		if(!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) { 
			res=true;
			this.artistRepository.save(artist); 
		}
		return res;
	}
	
	public Artist findArtistById(Long id) {
		return this.artistRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public Iterable<Artist> findAllArtist() {
		return this.artistRepository.findAll();
	}
	
	public List<Artist> findByName(String name) {
		return this.artistRepository.findByName(name);
	}

	public List<Artist> findActorsNotInMovie(Movie movie) {
		List<Artist> actorsToAdd = new ArrayList<>();
		
		for(Artist a : this.artistRepository.findAllByStarredMoviesIsNotContaining(movie)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
	
	public List<Artist> findActorsInMovie(Movie movie) {
		List<Artist> actors = new ArrayList<>();
		
		for(Artist a : this.artistRepository.findAllByStarredMoviesIsContaining(movie)) {
			actors.add(a);
		}
		return actors;
	}

	@Transactional
	public void updateArtistPicture(Long id, @RequestParam("file") MultipartFile image) throws IOException {
		Artist artist = this.findArtistById(id);
		Image img = new Image();
		img.setName(image.getResource().getFilename());
		img.setData(image.getBytes());
		this.imageRepository.save(img);
		artist.setImage(img);
		this.artistRepository.save(artist);
	}
}
