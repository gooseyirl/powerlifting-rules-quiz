import SwiftUI

private struct QuizOption {
    let count: Int
    let title: String
    let description: String
    let icon: String
}

private let quizOptions: [QuizOption] = [
    QuizOption(count: 10, title: "10 Questions", description: "Quick practice session", icon: "bolt.fill"),
    QuizOption(count: 25, title: "25 Questions", description: "Standard quiz", icon: "chart.bar.fill"),
    QuizOption(count: 50, title: "50 Questions", description: "Full challenge", icon: "flame.fill"),
]

struct QuizSetupView: View {
    @Binding var path: NavigationPath
    @Environment(QuizRepository.self) private var repository
    @Environment(QuizViewModel.self) private var quizViewModel

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                Text("How many questions would you like?")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 4)

                ForEach(quizOptions, id: \.count) { option in
                    Button {
                        quizViewModel.startQuiz(count: option.count, from: repository)
                        path.append(AppRoute.quiz)
                    } label: {
                        HStack(spacing: 16) {
                            Image(systemName: option.icon)
                                .font(.title2)
                                .foregroundStyle(.blue)
                                .frame(width: 36)
                            VStack(alignment: .leading, spacing: 2) {
                                Text(option.title)
                                    .font(.headline)
                                    .foregroundStyle(.primary)
                                Text(option.description)
                                    .font(.subheadline)
                                    .foregroundStyle(.secondary)
                            }
                            Spacer()
                            Image(systemName: "chevron.right")
                                .foregroundStyle(.tertiary)
                        }
                        .padding()
                        .background(Color(.secondarySystemGroupedBackground))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding()
        }
        .navigationTitle("Choose Quiz Length")
        .navigationBarTitleDisplayMode(.large)
        .background(Color(.systemGroupedBackground))
    }
}
