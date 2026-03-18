package com.gooseco.technicalrulesquiz.data.model

import com.google.gson.annotations.SerializedName

data class QuizQuestion(
    @SerializedName("id")
    val id: Int,

    @SerializedName("question")
    val question: String,

    @SerializedName("options")
    val options: List<String>,

    @SerializedName("correctAnswerIndex")
    val correctAnswerIndex: Int,

    @SerializedName("ruleReference")
    val ruleReference: RuleReference,

    @SerializedName("explanation")
    val explanation: String? = null,

    @SerializedName("ruleQuote")
    val ruleQuote: String? = null,

    @SerializedName("status")
    val status: String = "pending"  // "pending", "validated", "review_later", "rejected"
)

data class RuleReference(
    @SerializedName("section")
    val section: String,

    @SerializedName("subsection")
    val subsection: String? = null,

    @SerializedName("ruleNumber")
    val ruleNumber: String,

    @SerializedName("pageNumber")
    val pageNumber: Int? = null
) {
    fun getFullReference(): String {
        val parts = mutableListOf<String>()
        parts.add("Section $section")
        subsection?.let { parts.add(it) }
        parts.add("Rule $ruleNumber")
        pageNumber?.let { parts.add("(Page $it)") }
        return parts.joinToString(" - ")
    }
}
