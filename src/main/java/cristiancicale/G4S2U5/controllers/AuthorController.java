package cristiancicale.G4S2U5.controllers;

import cristiancicale.G4S2U5.entities.Author;
import cristiancicale.G4S2U5.exceptions.ValidationException;
import cristiancicale.G4S2U5.payloads.AuthorDTO;
import cristiancicale.G4S2U5.payloads.AuthorRespDTO;
import cristiancicale.G4S2U5.payloads.NewAuthorPayload;
import cristiancicale.G4S2U5.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public AuthorRespDTO saveUser(@RequestBody @Validated AuthorDTO body, BindingResult validationResult) {


        if (validationResult.hasErrors()) {

            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        Author newAuthor = this.authorService.save(body);
        return new AuthorRespDTO(newAuthor.getId());
    }

    @GetMapping
    public Page<Author> getAuthors(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "surname") String sortBy) {
        return this.authorService.findAll(page, size, sortBy);
    }

    @GetMapping("/{authorId}")
    public Author getById(@PathVariable long authorId) {
        return this.authorService.findById(authorId);
    }

    @PutMapping("/{authorId}")
    public Author getByIdAndUpdate(@PathVariable long authorId, @RequestBody NewAuthorPayload body) {
        return this.authorService.findByIdAndUpdate(authorId, body);
    }

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void getByIdAndDelete(@PathVariable long authorId) {
        this.authorService.findByIdAndDelete(authorId);
    }

    @PatchMapping("/{authorId}/avatar")
    public void uploadAvatar(@RequestParam("profile_picture") MultipartFile file, @PathVariable long authorId) {

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());

        this.authorService.avatarUpload(file, authorId);
    }
}