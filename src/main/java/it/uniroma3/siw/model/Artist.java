package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Artist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;

	@NotBlank
	private String surname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfDeath;

	@OneToOne
	private Image image;

	@OneToMany(mappedBy = "director")
	private List<Movie> directedMovies;
	
	@ManyToMany(mappedBy = "actors")
	private Set<Movie> starredMovies;
	
	public Artist(){
		this.starredMovies = new HashSet<>();
		this.directedMovies = new LinkedList<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public List<Movie> getDirectedMovies() {
		return directedMovies;
	}

	public void setDirectedMovies(List<Movie> directedMovies) {
		this.directedMovies = directedMovies;
	}

	public Set<Movie> getStarredMovies() {
		return starredMovies;
	}

	public void setStarredMovies(Set<Movie> starredMovies) {
		this.starredMovies = starredMovies;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}
	

}
