package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Category
import java.lang.Exception

class FakeCategoryRepository : CategoryRepositoryInterface {

    private val categories = mutableListOf<Category>()
    private val observableCategories = MutableLiveData<List<Category>>(categories)

    private fun refreshLiveData() {
        observableCategories.postValue(categories)
    }

    override suspend fun insert(category: Category) {
        categories.add(category)
        refreshLiveData()
    }

    override suspend fun update(categoryID: Int?, newName: String) {
        for (category in categories) {
            if (category.categoryID == categoryID) {
                category.categoryName = newName
                refreshLiveData()
                return
            }
        }
    }

    override suspend fun delete(category: Category) {
        categories.remove(category)
        refreshLiveData()
    }

    override fun getAllCategories(): LiveData<List<Category>> {
        return observableCategories
    }

    override fun getCategoryOfID(categoryID: Int?): Category {
        for (category in categories) {
            if (category.categoryID == categoryID) {
                return category
            }
        }
        throw Exception("No category of ID")
    }

    override fun getCategoryOfIDObs(categoryID: Int?): LiveData<Category> {
        for (category in categories) {
            if (category.categoryID == categoryID) {
                return MutableLiveData(category)
            }
        }
        throw Exception("No category of ID")
    }
}