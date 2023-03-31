package com.example.lightweight.util

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CalendarUtilTest::class,
    PersonalRecordUtilTest::class
)
class UtilTestSuite