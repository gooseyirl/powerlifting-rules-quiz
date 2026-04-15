import SwiftUI

private let developerEmail = "gooseyirl+plrulesquiz@gmail.com"

struct ReviewView: View {
    @Environment(QuizViewModel.self) private var quizViewModel
    @Environment(\.openURL) private var openURL

    @State private var result: QuizResult?

    var body: some View {
        Group {
            if let result {
                List(Array(result.answeredQuestions.enumerated()), id: \.offset) { index, answered in
                    AnswerReviewRow(
                        questionNumber: index + 1,
                        answered: answered,
                        onReport: { sendReportEmail(question: answered.question) }
                    )
                    .listRowInsets(EdgeInsets(top: 12, leading: 16, bottom: 12, trailing: 16))
                    .listRowBackground(Color(.secondarySystemGroupedBackground))
                }
                .listStyle(.insetGrouped)
            } else {
                ProgressView()
            }
        }
        .navigationTitle("Review Answers")
        .navigationBarTitleDisplayMode(.large)
        .onAppear {
            if result == nil {
                result = quizViewModel.getQuizResult()
            }
        }
    }

    private func sendReportEmail(question: QuizQuestion) {
        let subject = "[Powerlifting Rules Quiz] Report Question #\(question.id)"
            .addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        let body = "Question ID: #\(question.id)\n\nQuestion: \(question.question)\n\n---\nPlease describe the issue:\n\n"
            .addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        if let url = URL(string: "mailto:\(developerEmail)?subject=\(subject)&body=\(body)") {
            openURL(url)
        }
    }
}

// MARK: - Row

private struct AnswerReviewRow: View {
    let questionNumber: Int
    let answered: AnsweredQuestion
    let onReport: () -> Void

    @State private var showReportDialog = false

    private var question: QuizQuestion { answered.question }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            // Header row: Q1 / #id / CORRECT badge
            HStack {
                Text("Q\(questionNumber)")
                    .font(.caption)
                    .fontWeight(.semibold)
                    .foregroundStyle(.secondary)
                Text("#\(question.id)")
                    .font(.caption)
                    .foregroundStyle(.tertiary)
                Spacer()
                resultBadge
            }

            // Question text
            Text(question.question)
                .font(.subheadline)
                .fontWeight(.medium)

            Divider()

            // Answers
            answerLine(label: "Your Answer:", text: question.options[answered.userAnswerIndex], color: answered.isCorrect ? .green : .red)

            if !answered.isCorrect {
                answerLine(label: "Correct Answer:", text: question.options[question.correctAnswerIndex], color: .green)
            }

            // Rule reference
            VStack(alignment: .leading, spacing: 2) {
                Text("Rule Reference:")
                    .font(.caption)
                    .foregroundStyle(.secondary)
                Text(question.ruleReference.fullReference)
                    .font(.caption)
                    .foregroundStyle(.blue)
            }

            // Explanation
            if let explanation = question.explanation {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Explanation:")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                    Text(explanation)
                        .font(.caption)
                        .foregroundStyle(.primary)
                }
            }

            // Report button
            Button {
                showReportDialog = true
            } label: {
                Label("Report question", systemImage: "flag")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            .buttonStyle(.plain)
        }
        .confirmationDialog(
            "Report this question?",
            isPresented: $showReportDialog,
            titleVisibility: .visible
        ) {
            Button("Open Email") { onReport() }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This will open your email app so you can send a report to the developer.")
        }
    }

    @ViewBuilder
    private var resultBadge: some View {
        Text(answered.isCorrect ? "CORRECT" : "INCORRECT")
            .font(.caption2)
            .fontWeight(.bold)
            .foregroundStyle(.white)
            .padding(.horizontal, 8)
            .padding(.vertical, 3)
            .background(answered.isCorrect ? Color.green : Color.red)
            .clipShape(Capsule())
    }

    @ViewBuilder
    private func answerLine(label: String, text: String, color: Color) -> some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(label)
                .font(.caption)
                .foregroundStyle(.secondary)
            Text(text)
                .font(.subheadline)
                .foregroundStyle(color)
        }
    }
}
