package com.abhinav.spendwise.Services.ServiceImp;

import java.util.UUID;
import java.util.List;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Repository.CategoryRepository;
import com.abhinav.spendwise.Repository.ExpenseRepository;
import com.abhinav.spendwise.Services.CategoryService;
import com.abhinav.spendwise.Domain.Dtos.CreateCategoryRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    /**
     * Validates if the given user has access to a category.
     * Throws EntityNotFoundException if category doesn't exist,
     * and IllegalArgumentException if access is denied.
     */
    @Override
    public void validateCategoryAccess(UUID categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found"));

        if (category.getUser() != null && !category.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access Denied for this Category");
        }
    }

    /**
     * Retrieves a category by its ID.
     * Throws EntityNotFoundException if not found.
     */
    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    /**
     * Creates a new category for a user.
     * Throws IllegalArgumentException if category with same name exists.
     */
    @Override
    public Category createCategory(Category category, User user) {
        String categoryName = category.getName();

        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category " + categoryName + " already exists");
        }

        category.setUser(user);
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories for a user.
     * EntityNotFoundException catch here is unnecessary (see review).
     */
    @Override
    public List<Category> getAllCategory(User user) {
        try {
            return categoryRepository.findByUserOrUserIsNull(user);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Categories Not Found");
        }
    }

    /**
     * Updates category name.
     * Throws EntityNotFoundException if category does not exist for the user.
     */
    @Override
    @Transactional
    public Category updateCategory(UUID categoryID, User user, CreateCategoryRequest request) {
        Category existingCategory = categoryRepository.findByIdAndUser(categoryID, user)
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found!"));

        existingCategory.setName(request.getName());
        return categoryRepository.save(existingCategory);
    }

    /**
     * Deletes a category owned by a user.
     * Throws EntityNotFoundException if not found.
     */
    @Override
    public void deleteCategory(UUID categoryId, User user) {
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new EntityNotFoundException("Category Not Found!!"));
        categoryRepository.delete(category);
    }

    /**
     * Calculates total expenses for a category in the current month.
     * Ensures user has access before calculating.
     * Returns 0.0 if no expenses found.
     */
    @Override
    public Double getTotalAmountByCategoryAndMonth(UUID categoryId, User user) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        validateCategoryAccess(categoryId, user);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not Found!"));

        Double totalAmountByCategory = expenseRepository.getTotalAmountByCategoryAndUserAndDateRange(
                category, user, firstDayOfMonth, lastDayOfMonth);

        return totalAmountByCategory != null ? totalAmountByCategory : 0.0;
    }
}
