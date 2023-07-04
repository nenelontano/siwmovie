package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.repository.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RecensioneValidator implements Validator {
    @Autowired
    private RecensioneRepository recensioneRepository;

    @Override
    public void validate(Object o, Errors errors) {
        Recensione recensione = (Recensione) o;
        if(recensione.getMovie() != null && recensione.getAutore()!= null
                && recensioneRepository.existsByAutoreAndMovie(recensione.getAutore(), recensione.getMovie())) {
            errors.reject("recensione.duplicate");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Recensione.class.equals(aClass);
    }
}
