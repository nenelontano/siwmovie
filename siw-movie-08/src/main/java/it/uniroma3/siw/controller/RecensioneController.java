package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.RecensioneService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.RecensioneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecensioneValidator recensioneValidator;


    private Credentials getCredentials() {
        if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            return credentials;
        }
        return null;
    }


    @GetMapping(value="/formNewRecensione/{idUser}/{idMovie}")
    public String formNewRecensione(@PathVariable("idUser") Long idU, @PathVariable("idMovie") Long idM, Model model) {
        Movie movie = this.movieService.findMovieById(idM);

        if(movie!= null) {
            model.addAttribute("recensione", new Recensione());
            model.addAttribute("movie", movie);
            model.addAttribute("credentials", this.getCredentials());
            return "formNewRecensione";
        } else
            return "recensioneError.html";
    }

    @PostMapping("/addRecensione/{idMovie}/{idUser}")
    public String newRecensione(@Valid @ModelAttribute("recensione") Recensione recensione, @PathVariable("idUser") Long idU, @PathVariable("idMovie") Long idM, BindingResult bindingResult, Model model) {
        this.recensioneValidator.validate(recensione, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.recensioneService.newRecensione(recensione, idU, idM, model);
            model.addAttribute("recensione", recensione);
            return "redirect:/movies/"+idM;
        } else {
            return "formNewRecensione.html";
        }
    }


    @GetMapping(value="/deleteRecensione/{idRecensione}/{idMovie}")
    public String deleteRecensioneToMovie(@PathVariable("idRecensione") Long idR, @PathVariable("idMovie") Long idM, Model model) {
        Recensione rec = this.recensioneService.deleteRecensione(idR, idM);
        if(rec != null) {
            model.addAttribute("recensione", rec);
            return "redirect:/movies/"+idM;
        } else {
            return "recensioneError.html";
        }
    }


}
