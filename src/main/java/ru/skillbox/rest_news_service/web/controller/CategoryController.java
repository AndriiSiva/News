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
import org.springframework.web.bind.annotation.*;
import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "Category v1", description = "Category API version V1")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get categories", description = "Get all categories", tags = {"category"})
    @GetMapping
    public ResponseEntity<CategoryListResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok((categoryService.findAll(page, size)));
    }

    @Operation(summary = "Get category by id",
            description = "Get category by id",
            tags = {"category, id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok((categoryService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid UpsertCategoryRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable("id") Long categoryId,
                                                   @RequestBody @Valid UpsertCategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(categoryId, request));
    }

    @Operation(summary = "Delete category by id",
            description = "Delete category by id",
            tags = {"category, id"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}