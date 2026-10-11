package com.abhinav.spendwise.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhinav.spendwise.Domain.Dtos.CategoryDto;
import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Dtos.CreateCategoryRequest;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Services.CategoryService;
import com.abhinav.spendwise.Mapper.CategoryMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    //Create a new Category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest,
                                                        @AuthenticationPrincipal(expression = "user") User user){


            Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
            Category savedCategory = categoryService.createCategory(categoryToCreate, user);
            CategoryDto categoryDto = categoryMapper.toDto(savedCategory);
           return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    // Get Categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(@AuthenticationPrincipal(expression = "user") User user){

        log.debug("Fetching categories for user [{}]", user.getEmail());
            List<Category> categories = categoryService.getAllCategory(user);
            List<CategoryDto> categoryDtos = categories.stream()
                                                        .map(categoryMapper::toDto)
                                                        .toList();
            return ResponseEntity.ok(categoryDtos);
    }

    // Get Category By Id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id){

        Category category = categoryService.getCategoryById(id);
        CategoryDto categoryDto = categoryMapper.toDto(category);

        return ResponseEntity.ok(categoryDto);
    }

    // Update Category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id,
                                                @AuthenticationPrincipal(expression="user") User user,
                                                @Valid @RequestBody CreateCategoryRequest updateCategoryRequest){

                log.info("User [{}] updating category [{}] with new name: {}", user.getEmail(), id, updateCategoryRequest.getName());
                Category updatedCategory = categoryService.updateCategory(id, user, updateCategoryRequest);
                CategoryDto updatedDto = categoryMapper.toDto(updatedCategory);
                return ResponseEntity.ok(updatedDto);
        }
     // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id,
                                            @AuthenticationPrincipal(expression="user") User user){

        log.warn("User [{}] deleting category with ID: {}", user.getEmail(), id);
        categoryService.deleteCategory(id, user);

        return ResponseEntity.noContent().build();
    }

    //Get total expense for category in a month
    @GetMapping("/month/total-amount/{id}")
    public ResponseEntity<Double> getTotalAmountByCategoryAndMonth(@PathVariable UUID id,
                                                        @AuthenticationPrincipal(expression="user") User user)
    {
        log.debug("User [{}] fetching total monthly expense for category ID: {}", user.getEmail(), id);
        Double totalAmount = categoryService.getTotalAmountByCategoryAndMonth(id, user);
        return ResponseEntity.ok(totalAmount);
    }
    
}
