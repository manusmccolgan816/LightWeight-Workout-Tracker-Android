package com.example.lightweight.util

import com.example.lightweight.data.db.entities.*
import com.example.lightweight.util.PersonalRecordUtil.calculateIsNewSetPr
import com.example.lightweight.util.PersonalRecordUtil.getNewPrSetsOnDeletion
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

    // The following tests test the first value returned from calculateIsNewSetPr

    @Test
    fun `calculateIsNewSetPr first training set, returns true`() {
        val result = calculateIsNewSetPr(
            10,
            22.5f,
            "2023-01-21",
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr higher reps than all PRs, returns true`() {
        val result = calculateIsNewSetPr(
            10,
            12.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr lower reps and higher weight than any PR, returns true`() {
        val result = calculateIsNewSetPr(
            7,
            22.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr higher reps and higher weight than any PR, returns true`() {
        val result = calculateIsNewSetPr(
            9,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr higher reps and equal weight to any PR, returns true`() {
        val result = calculateIsNewSetPr(
            10,
            17.5f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr equal reps and higher weight than any PR, returns true`() {
        val result = calculateIsNewSetPr(
            8,
            21f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr equal reps, equal weight and earlier date than any PR, returns true`() {
        val result = calculateIsNewSetPr(
            8,
            20f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isTrue()
    }

    @Test
    fun `calculateIsNewSetPr lower reps and equal weight to any PR, returns false`() {
        val result = calculateIsNewSetPr(
            7,
            20f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `calculateIsNewSetPr lower reps and lower weight than any PR, returns false`() {
        val result = calculateIsNewSetPr(
            7,
            10f,
            "2023-01-16",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `calculateIsNewSetPr equal reps, equal weight and later date than any PR, returns false`() {
        val result = calculateIsNewSetPr(
            8,
            20f,
            "2023-01-18",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    @Test
    fun `calculateIsNewSetPr equal reps, equal weight and same date as any PR, returns false`() {
        val result = calculateIsNewSetPr(
            8,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.first).isFalse()
    }

    // The following tests test the second value returned from calculateIsNewSetPr

    @Test
    fun `calculateIsNewSetPr first training set, returns empty list`() {
        val result = calculateIsNewSetPr(
            10,
            22.5f,
            "2023-01-21",
            arrayListOf(),
            arrayListOf()
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr higher reps and lower weight than all PRs, returns empty list`() {
        val result = calculateIsNewSetPr(
            20,
            2.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr higher reps than all PRs and equal weight to any PR, returns only ID of said PR and PRs with lower weight`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr higher reps and higher weight than all PRs, returns ID of all PRs`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr lower reps and higher weight than any PR, returns empty list`() {
        val result = calculateIsNewSetPr(
            1,
            60f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr lower reps and equal weight to any PR, returns empty list`() {
        val result = calculateIsNewSetPr(
            5,
            17.5f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr lower reps and lower weight than any PR, returns empty list`() {
        val result = calculateIsNewSetPr(
            5,
            10f,
            "2023-01-21",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr higher reps and higher weight than any PR, returns only ID of said PR and PRs with lower or equal reps`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr higher reps and equal weight to any PR, returns only ID of said PR and PRs with lower or equal reps`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr equal reps and higher weight than any PR, returns only ID of said PR and PRs with lower reps and lower or equal weight`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr equal reps, equal weight and earlier date than any PR, returns only ID of said PR`() {
        val result = calculateIsNewSetPr(
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
    fun `calculateIsNewSetPr equal reps, equal weight and later date than any PR, returns empty list`() {
        val result = calculateIsNewSetPr(
            8,
            20f,
            "2023-01-30",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    @Test
    fun `calculateIsNewSetPr equal reps, equal weight and same date as any PR, returns empty list`() {
        val result = calculateIsNewSetPr(
            8,
            20f,
            "2023-01-17",
            prSets,
            prDates
        )

        assertThat(result.second).isEmpty()
    }

    // The following tests test getNewPrSetsOnDeletion

    @Test
    fun `getNewPrSetsOnDeletion no other sets exist, return empty list`() {
        val curTrainingSet = TrainingSet(
            2,
            1,
            50f,
            10,
            null,
            true
        )
        val prSets = listOf(curTrainingSet)

        val result = getNewPrSetsOnDeletion(
            curTrainingSet,
            prSets,
            listOf(),
            listOf()
        )

        assertThat(result).isEmpty()
    }

    @Test
    fun `getNewPrSetsOnDeletion sets of equal reps exist and no PRs of higher reps and equal or higher weight exist, return only heaviest one's ID`() {
        val trainingSet1 = TrainingSet(
            2,
            1,
            27.5f,
            10,
            null,
            false
        )
        trainingSet1.trainingSetID = 1
        val trainingSet2 = TrainingSet(
            2,
            2,
            45f,
            10,
            null,
            false
        )
        trainingSet2.trainingSetID = 2

        val curTrainingSet = TrainingSet(
            2,
            3,
            50f,
            10,
            null,
            true
        )

        val prSets = listOf(curTrainingSet)
        val sameRepSets = listOf(trainingSet2, trainingSet1)

        val result = getNewPrSetsOnDeletion(
            curTrainingSet,
            prSets,
            sameRepSets,
            listOf()
        )

        assertThat(result).containsExactly(trainingSet2.trainingSetID)
    }

    @Test
    fun `getNewPrSetsOnDeletion set of equal reps exists and PR of higher reps and equal weight exists, return empty list`() {
        // For example, 5RM on 100 is deleted. There is a 6RM on 95. Existing 5 reps on 95 is not a PR
        val curTrainingSet = TrainingSet(
            2,
            1,
            100f,
            5,
            null,
            true
        )

        val trainingSet1 = TrainingSet(
            2,
            2,
            95f,
            6,
            null,
            true
        )
        trainingSet1.trainingSetID = 1
        val trainingSet2 = TrainingSet(
            2,
            3,
            95f,
            5,
            null,
            false
        )
        trainingSet2.trainingSetID = 2

        val prSets = listOf(curTrainingSet, trainingSet1)
        val sameRepSets = listOf(trainingSet2)

        val result = getNewPrSetsOnDeletion(
            curTrainingSet,
            prSets,
            sameRepSets,
            listOf()
        )

        assertThat(result).isEmpty()
    }

    @Test
    fun `getNewPrSetsOnDeletion set of equal reps exists and PR of higher reps and higher weight exists, return empty list`() {
        val trainingSet1 = TrainingSet(
            2,
            1,
            27.5f,
            10,
            null,
            false
        )
        trainingSet1.trainingSetID = 1
        val trainingSet2 = TrainingSet(
            2,
            2,
            45f,
            11,
            null,
            true
        )
        trainingSet2.trainingSetID = 2

        val curTrainingSet = TrainingSet(
            2,
            3,
            50f,
            10,
            null,
            true
        )

        val prSets = listOf(curTrainingSet, trainingSet2)
        val sameRepSets = listOf(trainingSet1)

        val result = getNewPrSetsOnDeletion(
            curTrainingSet,
            prSets,
            sameRepSets,
            listOf()
        )

        assertThat(result).isEmpty()
    }

    @Test
    fun `getNewPrSetsOnDeletion non-PR sets of lower reps and higher weight than existing PRs exist, return only IDs of new PRs`() {
        // For example, 5RM on 100 is deleted. There is a 6RM on 95. Existing 4 reps on 98 and 3
        // reps on 99 are now PRs
        val curTrainingSet = TrainingSet(
            2,
            1,
            100f,
            5,
            null,
            true
        )
        val trainingSet1 = TrainingSet(
            2,
            2,
            98f,
            4,
            null,
            false
        )
        trainingSet1.trainingSetID = 1
        val trainingSet2 = TrainingSet(
            2,
            2,
            99f,
            3,
            null,
            false
        )
        trainingSet2.trainingSetID = 2
        val trainingSet3 = TrainingSet(
            2,
            4,
            95f,
            6,
            null,
            true
        )
        trainingSet3.trainingSetID = 3

        val prSets = listOf(curTrainingSet, trainingSet3)
        val lowerRepSets = listOf(trainingSet1, trainingSet2)

        val result = getNewPrSetsOnDeletion(
            curTrainingSet,
            prSets,
            listOf(),
            lowerRepSets
        )

        assertThat(result).containsExactly(trainingSet1.trainingSetID, trainingSet2.trainingSetID)
    }
}