package ru.skillbox.rest_news_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.rest_news_service.aop.CheckOwnership;
import ru.skillbox.rest_news_service.entity.Role;
import ru.skillbox.rest_news_service.entity.RoleType;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.*;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
@Tag(name = "Author v1", description = "Author API version V1")
public class AuthorController {

    private final AuthorService authorService;
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get authors", description = "Get all authors", tags = {"author"})
    @GetMapping
    public ResponseEntity<AuthorListResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(authorService.findAll(page, size));
    }

    @Operation(summary = "Get author by id",
            description = "Get author by id. Return id, list of news and list of comments",
            tags = {"author, id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = AuthorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    })
    })

    @CheckOwnership
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> findById(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Long id) {

        return ResponseEntity.ok((authorService.findById(id)));
    }

    @PostMapping("/account")
    public ResponseEntity<AuthorResponse> createAuthorAccount(@RequestBody @Valid UpsertAuthorRequest request,
                                                              @RequestParam RoleType roleType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.create(request, Role.from(roleType)));
    }

    @CheckOwnership
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(@PathVariable("id") Long Id,
                                                 @RequestBody @Valid UpdateAuthorRequest request,
                                                 @RequestParam(required = false) RoleType roleType,
                                                 @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(authorService.update(request, Id, Role.from(roleType)));
    }

    @CheckOwnership
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @Operation(summary = "Delete author by id",
            description = "Delete author by id",
            tags = {"author, id"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        authorService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/account/create-with-news")
    public ResponseEntity<AuthorResponse> createWithNews(@RequestBody @Valid UpsertAuthorRequestWithNews request,
                                                         @RequestParam RoleType roleType) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.saveWithNews(request, Role.from(roleType)));
    }
}