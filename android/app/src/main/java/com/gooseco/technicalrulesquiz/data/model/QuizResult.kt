package com.gooseco.technicalrulesquiz.data.model

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val answeredQuestions: List<AnsweredQuestion>
) {
    val scorePercentage: Int
        get() = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0

    val passed: Boolean
        get() = scorePercentage >= 70
}

data class AnsweredQuestion(
    val question: QuizQuestion,
    val userAnswerIndex: Int,
    val isCorrect: Boolean,
    val timeTaken: Long = 0L
)
