package com.abhinav.spendwise.Services;

import java.util.UUID;
import java.util.List;

import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Entity.User;
import com.abhinav.spendwise.Domain.Dtos.CreateCategoryRequest;

public interface CategoryService {

    void validateCategoryAccess(UUID categoryId, User user);
    Category getCategoryById(UUID categoryId);
    Category createCategory(Category category, User user);
    List<Category> getAllCategory(User user);
    Category updateCategory(UUID cateogoryId, User user, CreateCategoryRequest request);
    void deleteCategory(UUID categoryId, User user);
    Double getTotalAmountByCategoryAndMonth(UUID categoryId, User user);
}
