package com.gooseco.technicalrulesquiz.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.gooseco.technicalrulesquiz.data.model.QuizQuestion
import java.io.IOException

class QuizRepository(private val context: Context) {

    private var cachedQuestions: List<QuizQuestion>? = null

    /**
     * Load all questions from the JSON asset file
     */
    private fun loadQuestionsFromJson(): List<QuizQuestion> {
        cachedQuestions?.let { return it }

        return try {
            val jsonString = context.assets.open("questions.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val questionData = gson.fromJson(jsonString, QuestionData::class.java)
            val questions = questionData?.questions ?: emptyList()
            cachedQuestions = questions
            questions
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Get all validated questions (status == "validated")
     * These are the questions that appear in the app quiz
     */
    fun getAllQuestions(): List<QuizQuestion> {
        return loadQuestionsFromJson().filter { it.status == "validated" }
    }

    /**
     * Get random validated questions for a quiz session
     */
    fun getRandomQuestions(count: Int): List<QuizQuestion> {
        val validatedQuestions = getAllQuestions()
        return validatedQuestions.shuffled().take(count)
    }

    /**
     * Get validated questions filtered by section
     */
    fun getQuestionsBySection(section: String): List<QuizQuestion> {
        return getAllQuestions().filter { it.ruleReference.section == section }
    }

    /**
     * Get all questions regardless of status (for debugging/admin purposes)
     */
    fun getAllQuestionsUnfiltered(): List<QuizQuestion> {
        return loadQuestionsFromJson()
    }

    /**
     * Get questions by status
     */
    fun getQuestionsByStatus(status: String): List<QuizQuestion> {
        return loadQuestionsFromJson().filter { it.status == status }
    }

    /**
     * Get count of questions by status
     */
    fun getQuestionCountByStatus(): Map<String, Int> {
        val questions = loadQuestionsFromJson()
        return questions.groupBy { it.status }.mapValues { it.value.size }
    }

    /**
     * Clear the cache to reload questions from JSON
     */
    fun clearCache() {
        cachedQuestions = null
    }

    /**
     * Data class to match the JSON structure
     */
    private data class QuestionData(
        @SerializedName("version") val version: String,
        @SerializedName("lastUpdated") val lastUpdated: String,
        @SerializedName("questions") val questions: List<QuizQuestion>
    )
}
