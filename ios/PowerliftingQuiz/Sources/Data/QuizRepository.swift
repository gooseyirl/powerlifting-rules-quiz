import Foundation

@Observable
final class QuizRepository {
    private(set) var allValidatedQuestions: [QuizQuestion] = []
    private var loaded = false

    func loadIfNeeded() {
        guard !loaded else { return }
        loaded = true

        guard let url = Bundle.main.url(forResource: "questions", withExtension: "json"),
              let data = try? Data(contentsOf: url) else {
            return
        }

        let decoded = try? JSONDecoder().decode(QuestionData.self, from: data)
        allValidatedQuestions = (decoded?.questions ?? []).filter { $0.status == "validated" }
    }

    func randomQuestions(count: Int) -> [QuizQuestion] {
        Array(allValidatedQuestions.shuffled().prefix(count))
    }

    var availableSections: [String] {
        Array(Set(allValidatedQuestions.map(\.ruleReference.section))).sorted()
    }

    var totalValidated: Int { allValidatedQuestions.count }
}

// MARK: - Private

private struct QuestionData: Codable {
    let version: String
    let lastUpdated: String
    let questions: [QuizQuestion]
}
