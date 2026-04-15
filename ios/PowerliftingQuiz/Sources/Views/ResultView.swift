import SwiftUI

private let resultBannerAdUnitID = "ca-app-pub-3826192057448984/8060929824"

struct ResultView: View {
    @Binding var path: NavigationPath
    @Environment(QuizViewModel.self) private var quizViewModel
    @Environment(QuizRepository.self) private var repository
    @Environment(StoreManager.self) private var storeManager

    // Capture result once so it doesn't change if quiz is restarted
    @State private var result: QuizResult?

    var body: some View {
        ScrollView {
            if let result {
                VStack(spacing: 24) {
                    // Header
                    VStack(spacing: 8) {
                        Image(systemName: result.passed ? "checkmark.seal.fill" : "book.fill")
                            .font(.system(size: 64))
                            .foregroundStyle(result.passed ? .green : .orange)

                        Text("Quiz Completed!")
                            .font(.title)
                            .fontWeight(.bold)

                        Text(result.passed ? "You Passed!" : "Keep Studying!")
                            .font(.headline)
                            .foregroundStyle(result.passed ? .green : .orange)
                    }
                    .padding(.top, 8)

                    // Score card
                    VStack(spacing: 16) {
                        Text("\(result.scorePercentage)%")
                            .font(.system(size: 72, weight: .bold, design: .rounded))
                            .foregroundStyle(result.passed ? .green : .orange)

                        Text("\(result.correctAnswers) out of \(result.totalQuestions) correct")
                            .font(.subheadline)
                            .foregroundStyle(.secondary)

                        Divider()

                        HStack(spacing: 32) {
                            statPill(
                                value: result.correctAnswers,
                                label: "Correct",
                                color: .green
                            )
                            statPill(
                                value: result.incorrectAnswers,
                                label: "Incorrect",
                                color: .red
                            )
                        }
                    }
                    .padding()
                    .background(Color(.secondarySystemGroupedBackground))
                    .clipShape(RoundedRectangle(cornerRadius: 16))

                    // Actions
                    VStack(spacing: 12) {
                        Button {
                            path.append(AppRoute.review)
                        } label: {
                            Label("Review Answers", systemImage: "list.bullet.clipboard")
                                .font(.headline)
                                .frame(maxWidth: .infinity)
                                .padding()
                        }
                        .buttonStyle(.borderedProminent)
                        .controlSize(.large)

                        Button {
                            quizViewModel.startQuiz(count: quizViewModel.lastQuestionCount, from: repository)
                            path.removeLast() // pop .result → back to QuizView
                        } label: {
                            Label("Retake Quiz", systemImage: "arrow.clockwise")
                                .font(.headline)
                                .frame(maxWidth: .infinity)
                                .padding()
                        }
                        .buttonStyle(.bordered)
                        .controlSize(.large)

                        Button {
                            path = NavigationPath()
                        } label: {
                            Label("Back to Home", systemImage: "house")
                                .font(.headline)
                                .frame(maxWidth: .infinity)
                                .padding()
                        }
                        .buttonStyle(.bordered)
                        .controlSize(.large)
                        .tint(.secondary)
                    }
                }
                .padding()
            }
        }
        .navigationTitle("Results")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .background(Color(.systemGroupedBackground))
        .safeAreaInset(edge: .bottom) {
            if !storeManager.isAdFree {
                BannerAdView(adUnitID: resultBannerAdUnitID)
                    .frame(height: 50)
                    .background(Color(.systemBackground))
            }
        }
        .onAppear {
            if result == nil {
                result = quizViewModel.getQuizResult()
            }
        }
    }

    @ViewBuilder
    private func statPill(value: Int, label: String, color: Color) -> some View {
        VStack(spacing: 4) {
            Text("\(value)")
                .font(.system(.title, design: .rounded, weight: .bold))
                .foregroundStyle(color)
            Text(label)
                .font(.caption)
                .foregroundStyle(.secondary)
        }
    }
}
