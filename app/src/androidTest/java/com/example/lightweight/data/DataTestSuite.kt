package com.example.lightweight.data

import com.example.lightweight.data.db.daos.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CategoryDaoTest::class,
    ExerciseDaoTest::class,
    WorkoutDaoTest::class,
    ExerciseInstanceDaoTest::class,
    TrainingSetDaoTest::class,
    CycleDaoTest::class,
    CycleDayDaoTest::class,
    CycleDayCategoryDaoTest::class,
    CycleDayExerciseDaoTest::class
)
class DataTestSuite