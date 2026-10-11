package com.abhinav.spendwise.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.abhinav.spendwise.Domain.Dtos.CreateCategoryRequest;
import com.abhinav.spendwise.Domain.Dtos.CategoryDto;
import com.abhinav.spendwise.Domain.Entity.Category;

@Mapper(componentModel="spring", unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryDto toDto(Category category);
    Category  toEntity(CreateCategoryRequest createCategoryRequest);    
}
