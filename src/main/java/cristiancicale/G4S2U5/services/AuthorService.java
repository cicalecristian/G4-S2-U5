package cristiancicale.G4S2U5.services;

import cristiancicale.G4S2U5.entities.Author;
import cristiancicale.G4S2U5.exceptions.BadRequestException;
import cristiancicale.G4S2U5.exceptions.NotFoundException;
import cristiancicale.G4S2U5.payloads.NewAuthorPayload;
import cristiancicale.G4S2U5.repositories.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(NewAuthorPayload body) {
        if (this.authorRepository.existsByEmail(body.getEmail()))
            throw new BadRequestException("L'indirizzo email " + body.getEmail() + "è già in uso");

        Author newAuthor = new Author(body.getNome(), body.getCognome(), body.getEmail(), body.getDataDiNascita());
        Author savedAuthor = this.authorRepository.save(newAuthor);

        log.info("L'utente con id " + savedAuthor.getId() + "è stato salvato correttamente");

        return savedAuthor;
    }

    public Page<Author> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 10) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.authorRepository.findAll(pageable);
    }

    public Author findById(long authorId) {
        return this.authorRepository.findById(authorId).orElseThrow(() -> new NotFoundException(authorId));
    }

    public Author findByIdAndUpdate(long authorId, NewAuthorPayload body) {
        Author found = this.findById(authorId);

        if (!found.getEmail().equals(body.getEmail())) {
            if (this.authorRepository.existsByEmail(body.getEmail()))
                throw new BadRequestException("L'indirizzo email" + body.getEmail() + "è gia in uso");
        }

        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setEmail(body.getEmail());
        found.setDataDiNascita(body.getDataDiNascita());
        found.setAvatar("https://ui-avatars.com/api/?name=" + body.getNome() + "+" + body.getCognome());

        Author updateAuthor = this.authorRepository.save(found);

        log.info("L'autore " + updateAuthor.getId() + "è stato salvato correttamente");

        return updateAuthor;
    }

    public void findByIdAndDelete(long authorId) {
        Author found = this.findById(authorId);
        this.authorRepository.delete(found);
    }
}
