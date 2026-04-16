package cristiancicale.G4S2U5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import cristiancicale.G4S2U5.entities.Author;
import cristiancicale.G4S2U5.exceptions.BadRequestException;
import cristiancicale.G4S2U5.exceptions.NotFoundException;
import cristiancicale.G4S2U5.payloads.AuthorDTO;
import cristiancicale.G4S2U5.payloads.NewAuthorPayload;
import cristiancicale.G4S2U5.repositories.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final Cloudinary cloudinaryUploader;

    public AuthorService(AuthorRepository authorRepository, Cloudinary cloudinaryUploader) {
        this.authorRepository = authorRepository;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    public Author save(AuthorDTO body) {
        if (this.authorRepository.existsByEmail(body.email()))
            throw new BadRequestException("L'indirizzo email " + body.email() + "è già in uso");

        Author newAuthor = new Author(body.name(), body.surname(), body.email(), body.dateOfBirth());
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

    public Author avatarUpload(MultipartFile file, long authorId) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File non valido o vuoto");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new BadRequestException("File troppo grande (max 2MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/png") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/gif"))) {
            throw new BadRequestException("Formato file non supportato");
        }

        Author found = this.findById(authorId);

        try {
            Map<?, ?> result = cloudinaryUploader.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = (String) result.get("secure_url");

            found.setAvatar(url);

            Author updatedAuthor = this.authorRepository.save(found);

            log.info("Avatar aggiornato per l'autore con id " + updatedAuthor.getId());

            return updatedAuthor;

        } catch (IOException e) {
            throw new RuntimeException("Errore durante upload avatar", e);
        }
    }
}
