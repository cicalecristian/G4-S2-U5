package cristiancicale.G4S2U5.controllers;

import cristiancicale.G4S2U5.entities.Author;
import cristiancicale.G4S2U5.payloads.NewAuthorPayload;
import cristiancicale.G4S2U5.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public Author saveAuthor(@RequestBody NewAuthorPayload body) {

        return this.authorService.save(body);
    }

    // 2. GET http://localhost:3001/users
    @GetMapping
    public Page<Author> getAuthors(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "cognome") String sortBy) {

        return this.authorService.findAll(page, size, sortBy);
    }

    // 3. GET http://localhost:3001/users/{userId}
    @GetMapping("/{authorId}")
    public Author getById(@PathVariable long authorId) {
        return this.authorService.findById(authorId);
    }

    // 4. PUT http://localhost:3001/users/{userId} (+ req.body)
    @PutMapping("/{authorId}")
    public Author getByIdAndUpdate(@PathVariable long authorId, @RequestBody NewAuthorPayload body) {
        return this.authorService.findByIdAndUpdate(authorId, body);
    }

    // 5. DELETE http://localhost:3001/users/{userId}
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void getByIdAndDelete(@PathVariable long authorId) {
        this.authorService.findByIdAndDelete(authorId);
    }
}