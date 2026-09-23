
package com.example.careerpathai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CareerPathAIUnitTest {

    @Test
    fun careerResultsShouldReturnThreeCareerOptions() {

        val answers = listOf(
            "Technology",
            "Solving problems",
            "Programming",
            "Remote"
        )

        val results = calculateCareerResults(answers)

        assertEquals(3, results.size)
    }

    @Test
    fun programmingAnswersShouldIncreaseSoftwareDeveloperScore() {

        val answers = listOf(
            "Programming"
        )

        val results = calculateCareerResults(answers)

        val softwareDeveloper =
            results.first { it.first == "Software Developer" }

        assertEquals(30, softwareDeveloper.second)
    }

    @Test
    fun dataAnswersShouldIncreaseDataAnalystScore() {

        val answers = listOf(
            "Working with data",
            "Data Analysis"
        )

        val results = calculateCareerResults(answers)

        val dataAnalyst =
            results.first { it.first == "Data Analyst" }

        assertEquals(60, dataAnalyst.second)
    }

    @Test
    fun businessAnswersShouldIncreaseBusinessAnalystScore() {

        val answers = listOf(
            "Business"
        )

        val results = calculateCareerResults(answers)

        val businessAnalyst =
            results.first { it.first == "Business Analyst" }

        assertEquals(30, businessAnalyst.second)
    }

    @Test
    fun careerResultsShouldBeSortedHighestFirst() {

        val answers = listOf(
            "Technology",
            "Programming"
        )

        val results = calculateCareerResults(answers)

        assertTrue(
            results[0].second >= results[1].second
        )
    }
}
