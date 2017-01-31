package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.config.Constants;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.service.BookService;
import com.finki.emt.bookstore.web.rest.response.MinifiedBookResponse;
import com.finki.emt.bookstore.web.rest.vm.BookVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);

    @Inject
    private BookService service;

    @GetMapping
    public List<?> index(@RequestParam Optional<Integer> offset,
                         @RequestParam Optional<Integer> limit,
                         @RequestParam Optional<Boolean> latest,
                         @RequestParam Optional<Boolean> minified,
                         @RequestParam Optional<String> categories) {
        List<Book> books = service.findAll(offset, limit, latest, categories);

        boolean minifiedResponse = minified.orElse(true);
        if (minifiedResponse) {
            return books.stream()
                    .map(MinifiedBookResponse::new)
                    .collect(Collectors.toList());
        }
        return books;
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        long count = service.count();
        return Collections.singletonMap("count", count);
    }

    @GetMapping("/{slug}")
    public Book show(@PathVariable String slug) {
        return service.findBySlug(slug)
                .orElseThrow(() -> new HttpServerErrorException(
                        HttpStatus.NOT_FOUND, "Book with slug " +   slug + " don't exists"));
    }

    @GetMapping("/{slug}/image")
    public void image(@PathVariable String slug, HttpServletResponse response) throws IOException {
        Book book = service.findBySlug(slug).orElseThrow(() ->
                new HttpServerErrorException(
                        HttpStatus.NOT_FOUND, "Book with slug " + slug + " don't exists"));
        byte[] image = book.getImage();
        if (image == null) {
            throw new IllegalStateException("Book with slug " + slug + " has a null image");
        }
        OutputStream out = response.getOutputStream();
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        out.write(image);
        out.flush();
        out.close();
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> store(@Valid @RequestPart BookVM bookVM,
                                   @RequestPart MultipartFile image,
                                   @AuthenticationPrincipal Principal principal) {
        ResponseEntity<?> imgInvalid = this.validateImage(image);
        if (imgInvalid != null) {
            return imgInvalid;
        }

        Book book = bookVM.createBook();
        book.setImage(this.getImageBytes(image));
        User admin = principal.getUser();

        Book saved = service.create(book, admin);

        Map<String, String> msg =
                Collections.singletonMap("slug", saved.getSlug());
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<?> update(@PathVariable String slug,
                                    @Valid @RequestPart BookVM bookVM,
                                    @RequestPart(required = false) MultipartFile image,
                                    @AuthenticationPrincipal Principal principal) {
        if (image != null) {
            ResponseEntity<?> imgInvalid = this.validateImage(image);
            if (imgInvalid != null) {
                return imgInvalid;
            }
        }

        Book book = bookVM.createBook();
        if (image != null) {
            book.setImage(this.getImageBytes(image));
        }
        User admin = principal.getUser();

        Book updated = service.update(slug, book, admin);

        Map<String, String> msg =
                Collections.singletonMap("slug", updated.getSlug());
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<?> delete(@PathVariable String slug) {
        service.delete(slug);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public List<MinifiedBookResponse> popular(@RequestParam Optional<Integer> offset,
                                              @RequestParam Optional<Integer> limit) {
        return service.findPopular(offset, limit).stream()
                .map(MinifiedBookResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/_search")
    public List<MinifiedBookResponse> search(@RequestParam String query,
                                             @RequestParam Optional<Integer> offset,
                                             @RequestParam Optional<Integer> limit) {
        return service.search(query, offset, limit).stream()
                .map(MinifiedBookResponse::new)
                .collect(Collectors.toList());
    }

    private ResponseEntity<?> validateImage(MultipartFile image) {
        if (image.getSize() > Constants.BOOK_IMAGE_MAX_SIZE) {
            Map<String, List<String>> msg = Collections.singletonMap("errors",
                    Collections.singletonList("Image size too big"));
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private byte[] getImageBytes(MultipartFile image) {
        try {
            return image.getBytes();
        } catch (IOException e) {
            log.error("Getting the image bytes failed - {}", e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occurred while parsing the image bytes");
        }
    }
}
