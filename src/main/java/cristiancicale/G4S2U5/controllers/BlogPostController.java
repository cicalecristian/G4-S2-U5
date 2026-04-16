package cristiancicale.G4S2U5.controllers;

import cristiancicale.G4S2U5.entities.BlogPost;
import cristiancicale.G4S2U5.payloads.NewBlogPostPayload;
import cristiancicale.G4S2U5.services.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public BlogPost saveBlogPost(@RequestBody NewBlogPostPayload body) {

        return this.blogPostService.save(body);
    }

    @GetMapping
    public Page<BlogPost> getUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "categoria") String sortBy) {

        return this.blogPostService.findAll(page, size, sortBy);
    }

    @GetMapping("/{blogPostId}")
    public BlogPost getById(@PathVariable long blogPostId) {
        return this.blogPostService.findById(blogPostId);
    }

    @PutMapping("/{blogPostId}")
    public BlogPost getByIdAndUpdate(@PathVariable long blogPostId, @RequestBody NewBlogPostPayload body) {
        return this.blogPostService.findByIdAndUpdate(blogPostId, body);
    }

    @DeleteMapping("/{blogPostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void getByIdAndDelete(@PathVariable long blogPostId) {
        this.blogPostService.findByIdAndDelete(blogPostId);
    }
}