package it.uniroma3.siw.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Objects;

@Entity
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String titolo;

    @NotNull
    @Min(1)
    @Max(5)
    private int valutazione;
    
    private String testo;

    @ManyToOne
    //@NotNull
    private User autore;

    @ManyToOne
    //@NotNull
    private Movie movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public User getAutore() {
        return autore;
    }

    public void setAutore(User autore) {
        this.autore = autore;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recensione that = (Recensione) o;
        return Objects.equals(autore, that.autore) && Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autore, movie);
    }
}
