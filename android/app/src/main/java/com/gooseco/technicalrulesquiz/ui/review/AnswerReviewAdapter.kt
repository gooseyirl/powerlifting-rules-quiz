package com.gooseco.technicalrulesquiz.ui.review

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gooseco.technicalrulesquiz.R
import com.gooseco.technicalrulesquiz.data.model.AnsweredQuestion
import com.gooseco.technicalrulesquiz.databinding.ItemAnswerReviewBinding

class AnswerReviewAdapter(
    private val answeredQuestions: List<AnsweredQuestion>,
    private val onPageClick: (pageNumber: Int) -> Unit
) : RecyclerView.Adapter<AnswerReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnswerReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onPageClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(answeredQuestions[position], position + 1)
    }

    override fun getItemCount(): Int = answeredQuestions.size

    class ViewHolder(
        private val binding: ItemAnswerReviewBinding,
        private val onPageClick: (pageNumber: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(answeredQuestion: AnsweredQuestion, questionNumber: Int) {
            val context = binding.root.context
            val question = answeredQuestion.question

            binding.questionNumber.text = context.getString(
                R.string.question_number_format,
                questionNumber
            )
            binding.questionId.text = "#${question.id}"

            binding.questionText.text = question.question

            binding.userAnswer.text = question.options[answeredQuestion.userAnswerIndex]

            binding.correctAnswer.text = question.options[question.correctAnswerIndex]

            if (answeredQuestion.isCorrect) {
                binding.resultIndicator.text = context.getString(R.string.result_correct)
                binding.resultIndicator.setBackgroundColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                )
                binding.resultIndicator.setTextColor(
                    ContextCompat.getColor(context, android.R.color.white)
                )
                binding.correctAnswerLabel.visibility = View.GONE
                binding.correctAnswer.visibility = View.GONE
            } else {
                binding.resultIndicator.text = context.getString(R.string.result_incorrect)
                binding.resultIndicator.setBackgroundColor(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
                )
                binding.resultIndicator.setTextColor(
                    ContextCompat.getColor(context, android.R.color.white)
                )
                binding.correctAnswerLabel.visibility = View.VISIBLE
                binding.correctAnswer.visibility = View.VISIBLE
            }

            val pageNumber = question.ruleReference.pageNumber
            binding.ruleReference.text = question.ruleReference.getFullReference()
            if (pageNumber != null) {
                binding.ruleReference.setTextColor(
                    com.google.android.material.color.MaterialColors.getColor(
                        binding.root,
                        android.R.attr.colorPrimary,
                        binding.root.context.getColor(android.R.color.black)
                    )
                )
                binding.ruleReference.setOnClickListener { onPageClick(pageNumber) }
            }

            if (question.explanation != null) {
                binding.explanation.text = question.explanation
                binding.explanation.visibility = View.VISIBLE
            } else {
                binding.explanation.visibility = View.GONE
            }

            binding.reportQuestion.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.report_question_dialog_title)
                    .setMessage(R.string.report_question_dialog_message)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.report_question_open_email) { _, _ ->
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.developer_email)))
                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.report_email_subject, question.id))
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.report_email_body, question.id, question.question))
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                    }
                    .show()
            }
        }
    }
}
