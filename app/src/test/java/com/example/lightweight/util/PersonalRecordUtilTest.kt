package com.example.lightweight.util

import com.example.lightweight.data.db.entities.*
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class PersonalRecordUtilTest {

    private lateinit var category: Category
    private lateinit var exercise: Exercise
    private lateinit var workout: Workout
    private lateinit var exerciseInstance: ExerciseInstance

    private lateinit var prSets: ArrayList<TrainingSet>
    private lateinit var prDates: ArrayList<String>

    private lateinit var trainingSet1: TrainingSet
    private lateinit var trainingSet2: TrainingSet
    private lateinit var trainingSet3: TrainingSet

    @Before
    fun setup() {
        category = Category("CategoryTest")
        category.categoryID = 1
        exercise = Exercise("ExerciseTest", category.categoryID)
        exercise.exerciseID = 1
        workout = Workout("2023-01-17", null)
        workout.workoutID = 1
        exerciseInstance = ExerciseInstance(
            workout.workoutID,
            exercise.exerciseID,
            1,
            null
        )
        exerciseInstance.exerciseInstanceID = 1

        prSets = arrayListOf()
        prDates = arrayListOf(workout.date, workout.date, workout.date)

        trainingSet1 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            2,
            22.5f,
            6,
            null,
            true
        )
        trainingSet1.trainingSetID = 1
        prSets.add(trainingSet1)
        trainingSet2 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            1,
            20f,
            8,
            null,
            true
        )
        trainingSet2.trainingSetID = 2
        prSets.add(trainingSet2)
        trainingSet3 = TrainingSet(
            exerciseInstance.exerciseInstanceID,
            3,
            17.5f,
            9,
            null,
            true
        )
        trainingSet3.trainingSetID = 3
        prSets.add(trainingSet3)
    }

    @After
    fun teardown() {
        prSets.clear()
        prDates.clear()
    }

    // The following tests test the first value returned from calculateIsPR

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
    fun `higher reps than all PRs, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            10,
            12.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `lower reps and higher weight than any PR, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            7,
            22.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `higher reps and higher weight than any PR, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            9,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `higher reps and equal weight to any PR, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            10,
            17.5f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `equal reps and higher weight than any PR, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            21f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `equal reps, equal weight and earlier date than any PR, returns true`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `lower reps and equal weight to any PR, returns false`() {
        val result = PersonalRecordUtil.calculateIsPR(
            7,
            20f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `lower reps and lower weight than any PR, returns false`() {
        val result = PersonalRecordUtil.calculateIsPR(
            7,
            10f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `equal reps, equal weight and later date than any PR, returns false`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-18",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `equal reps, equal weight and same date as any PR, returns false`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    // The following tests test the second value returned from calculateIsPR

    @Test
    fun `first training set, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            10,
            22.5f,
            "2023-01-21",
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `higher reps and lower weight than all PRs, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            20,
            2.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `higher reps than all PRs and equal weight to any PR, returns only ID of said PR and PRs with lower weight`() {
        val result = PersonalRecordUtil.calculateIsPR(
            20,
            20f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).doesNotContain(trainingSet1.trainingSetID)
        assertThat(result.second).contains(trainingSet2.trainingSetID)
        assertThat(result.second).contains(trainingSet3.trainingSetID)
    }

    @Test
    fun `higher reps and higher weight than all PRs, returns ID of all PRs`() {
        val result = PersonalRecordUtil.calculateIsPR(
            20,
            40f,
            "2023-01-21",
            prSets,
            prDates
        )

        for (prSet in prSets) {
            assertThat(result.second).contains(prSet.trainingSetID)
        }
    }

    @Test
    fun `lower reps and higher weight than any PR, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            1,
            60f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `lower reps and equal weight to any PR, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            5,
            17.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `lower reps and lower weight than any PR, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            5,
            10f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `higher reps and higher weight than any PR, returns only ID of said PR and PRs with lower or equal reps`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            23f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).contains(trainingSet1.trainingSetID)
        assertThat(result.second).contains(trainingSet2.trainingSetID)
        assertThat(result.second).doesNotContain(trainingSet3.trainingSetID)
    }

    @Test
    fun `higher reps and equal weight to any PR, returns only ID of said PR and PRs with lower or equal reps`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            22.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).contains(trainingSet1.trainingSetID)
        assertThat(result.second).contains(trainingSet2.trainingSetID)
        assertThat(result.second).doesNotContain(trainingSet3.trainingSetID)
    }

    @Test
    fun `equal reps and higher weight than any PR, returns only ID of said PR and PRs with lower reps and lower or equal weight`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            22.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).contains(trainingSet1.trainingSetID)
        assertThat(result.second).contains(trainingSet2.trainingSetID)
        assertThat(result.second).doesNotContain(trainingSet3.trainingSetID)
    }

    @Test
    fun `equal reps, equal weight and earlier date than any PR, returns only ID of said PR`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-10",
            prSets,
            prDates
        )

        assertThat(result.second).doesNotContain(trainingSet1.trainingSetID)
        assertThat(result.second).contains(trainingSet2.trainingSetID)
        assertThat(result.second).doesNotContain(trainingSet3.trainingSetID)
    }

    @Test
    fun `equal reps, equal weight and later date than any PR, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-30",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `equal reps, equal weight and same date as any PR, returns empty list`() {
        val result = PersonalRecordUtil.calculateIsPR(
            8,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }
}