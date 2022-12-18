package com.example.lightweight.ui.trainingset

import androidx.lifecycle.ViewModel
import com.example.lightweight.data.db.entities.TrainingSet
import com.example.lightweight.data.repositories.TrainingSetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingSetViewModel(private val repository: TrainingSetRepository) : ViewModel() {

    fun insert(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(trainingSet)
    }

    fun delete(trainingSet: TrainingSet) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(trainingSet)
    }

    // Doesn't need to be executed in a coroutine because it is just a read operation
    fun getAllTrainingSets() = repository.getAllTrainingSets()
}