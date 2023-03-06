package com.example.lightweight.util

import com.example.lightweight.data.db.entities.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PersonalRecordUtilTest {

    @Test
    fun `first training set, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            10,
            22.5f,
            "2023-01-21",
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `higher reps than any PR training set, returns true`() {
        val category = Category("CategoryTest")
        category.categoryID = 1
        val exercise = Exercise("ExerciseTest", category.categoryID)
        exercise.exerciseID = 1
        val workout = Workout("2023-01-17", null)
        workout.workoutID = 1
        val exerciseInstance = ExerciseInstance(
            workout.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance.exerciseInstanceID = 1

        val prSets = arrayListOf<TrainingSet>()
        val prDates = arrayListOf(workout.date, workout.date, workout.date)

        val trainingSet1 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            2,
            22.5f,
            6,
            null,
            true
        )
        prSets.add(trainingSet1)
        val trainingSet2 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            20f,
            8,
            null,
            true
        )
        prSets.add(trainingSet2)
        val trainingSet3 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            3,
            17.5f,
            9,
            null,
            true
        )
        prSets.add(trainingSet3)

        val result = PersonalRecordUtil.calculateIsPR(
            10,
            12.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }
}