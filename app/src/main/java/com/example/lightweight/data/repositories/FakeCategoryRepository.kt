package com.example.lightweight.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.data.db.entities.Category

class FakeCategoryRepository : CategoryRepositoryInterface {

    private val allTag = 0
    private val categoriesTag = 1
    private val categoryOfIDObsTag = 2

    var cycleDayCategoryRepo: FakeCycleDayCategoryRepository? = null
    var cycleDayExerciseRepo: FakeCycleDayExerciseRepository? = null

    private val categories = mutableListOf<Category>()
    private val observableCategories = MutableLiveData<List<Category>>(categories)

    private val observableCategoryOfIDObs = MutableLiveData<Category>()
    private var categoryOfIDObsParam: Int? = null

    private var lastId = 0

    private fun refreshLiveData(tag: Int) {
        if (tag == allTag || tag == categoriesTag) observableCategories.postValue(categories)
        if (tag == allTag || tag == categoryOfIDObsTag) {
            if (categoryOfIDObsParam != null) {
                observableCategoryOfIDObs.postValue(calcCategoryOfIDObs(categoryOfIDObsParam))
            }
        }
    }

    override suspend fun insert(category: Category) {
        if (category.categoryID == null) {
            category.categoryID = ++lastId
        }
        categories.add(category)
        refreshLiveData(allTag)

        if (cycleDayCategoryRepo != null) {
            cycleDayCategoryRepo?.categories?.add(category)
            cycleDayCategoryRepo?.refreshLiveData(allTag)
        }
        if (cycleDayExerciseRepo != null) {
            cycleDayExerciseRepo?.categories?.add(category)
            cycleDayExerciseRepo?.refreshLiveData(allTag)
        }
    }

    override suspend fun update(categoryID: Int?, newName: String) {
        for (category in categories) {
            if (category.categoryID == categoryID) {
                category.categoryName = newName
                refreshLiveData(allTag)
                return
            }
        }

        // TODO Notify cycleDayCategoryRepo
    }

    override suspend fun delete(category: Category) {
        categories.remove(category)
        refreshLiveData(allTag)

        if (cycleDayCategoryRepo != null) {
            cycleDayCategoryRepo?.categories?.remove(category)
            cycleDayCategoryRepo?.refreshLiveData(allTag)
        }
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
        categoryOfIDObsParam = categoryID
        refreshLiveData(categoryOfIDObsTag)
        return observableCategoryOfIDObs
    }

    private fun calcCategoryOfIDObs(categoryID: Int?): Category {
        for (category in categories) {
            if (category.categoryID == categoryID) {
                return category
            }
        }
        throw Exception("No category of ID")
    }
}