package com.example.lightweight.ui.category

import com.example.lightweight.data.repositories.CategoryRepositoryInterface
import com.example.lightweight.data.repositories.FakeCategoryRepository
import org.junit.Assert.*
import org.junit.Before

class CategoryViewModelTest {

    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        viewModel = CategoryViewModel(FakeCategoryRepository())
    }


}