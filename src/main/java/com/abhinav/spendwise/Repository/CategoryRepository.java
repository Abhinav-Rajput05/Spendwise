package com.abhinav.spendwise.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhinav.spendwise.Domain.Entity.Category;
import com.abhinav.spendwise.Domain.Entity.User;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);
    List<Category> findByUserOrUserIsNull(User user);
    Optional<Category> findByIdAndUser(UUID categoryID, User user);
}
