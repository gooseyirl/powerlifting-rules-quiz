import SwiftUI

private let developerEmail = "gooseyirl+plrulesquiz@gmail.com"

private let quizBannerAdUnitID = "ca-app-pub-3826192057448984/8060929824"

struct QuizView: View {
    @Binding var path: NavigationPath
    @Environment(QuizViewModel.self) private var quizViewModel
    @Environment(StoreManager.self) private var storeManager
    @Environment(\.openURL) private var openURL

    @State private var showReportDialog = false

    var body: some View {
        Group {
            if let question = quizViewModel.currentQuestion {
                quizContent(question: question)
            } else {
                ProgressView("Loading…")
            }
        }
        .navigationTitle("Question \(quizViewModel.currentQuestionNumber) of \(quizViewModel.totalQuestions)")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .background(Color(.systemGroupedBackground))
        .safeAreaInset(edge: .bottom) {
            if !storeManager.isAdFree {
                BannerAdView(adUnitID: quizBannerAdUnitID)
                    .frame(height: 50)
                    .background(Color(.systemBackground))
            }
        }
        .onChange(of: quizViewModel.quizCompleted) { _, completed in
            if completed {
                path.append(AppRoute.result)
            }
        }
    }

    @ViewBuilder
    private func quizContent(question: QuizQuestion) -> some View {
        ScrollView {
            VStack(spacing: 20) {
                // Progress
                VStack(spacing: 6) {
                    ProgressView(
                        value: Double(quizViewModel.currentQuestionNumber),
                        total: Double(quizViewModel.totalQuestions)
                    )
                    HStack {
                        Text("Question \(quizViewModel.currentQuestionNumber) of \(quizViewModel.totalQuestions)")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                        Spacer()
                        Text("#\(question.id)")
                            .font(.caption)
                            .foregroundStyle(.tertiary)
                    }
                }

                // Question
                Text(question.question)
                    .font(.body)
                    .fontWeight(.medium)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .background(Color(.secondarySystemGroupedBackground))
                    .clipShape(RoundedRectangle(cornerRadius: 12))

                // Options
                VStack(spacing: 10) {
                    ForEach(Array(question.options.enumerated()), id: \.offset) { index, option in
                        optionRow(index: index, text: option)
                    }
                }

                // Next button
                Button {
                    quizViewModel.submitAnswer()
                } label: {
                    Text("Next Question")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
                .controlSize(.large)
                .disabled(quizViewModel.selectedAnswerIndex == nil)

                // Report question
                Button {
                    showReportDialog = true
                } label: {
                    Label("Report question", systemImage: "flag")
                        .font(.footnote)
                        .foregroundStyle(.secondary)
                }
                .buttonStyle(.plain)
                .padding(.bottom, 8)
            }
            .padding()
        }
        .confirmationDialog(
            "Report this question?",
            isPresented: $showReportDialog,
            titleVisibility: .visible
        ) {
            Button("Open Email") { sendReportEmail(question: question) }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This will open your email app so you can send a report to the developer.")
        }
    }

    @ViewBuilder
    private func optionRow(index: Int, text: String) -> some View {
        let isSelected = quizViewModel.selectedAnswerIndex == index
        Button {
            quizViewModel.selectAnswer(index)
        } label: {
            HStack(spacing: 12) {
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .font(.title3)
                    .foregroundStyle(isSelected ? Color.blue : Color.secondary)
                Text(text)
                    .foregroundStyle(Color.primary)
                    .multilineTextAlignment(.leading)
                Spacer()
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(isSelected ? Color.blue.opacity(0.1) : Color(.secondarySystemGroupedBackground))
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(isSelected ? Color.blue : Color.clear, lineWidth: 1.5)
            )
        }
        .buttonStyle(.plain)
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
