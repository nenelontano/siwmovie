package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;

@Service
public class ArtistService {
	
	@Autowired 
	ArtistRepository artistRepository;
	
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
}
