package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.CategoryEntity;
import com.poly.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public CategoryEntity addCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity updateCategory(int id, CategoryEntity categoryDetails) {
        CategoryEntity existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setTenDanhMuc(categoryDetails.getTenDanhMuc());
            existingCategory.setMoTa(categoryDetails.getMoTa());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    } 
}