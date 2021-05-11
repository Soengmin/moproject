package project.sec.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sec.domain.Genre;
import project.sec.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> findAll(){
        return genreRepository.findAll();
    }
}
