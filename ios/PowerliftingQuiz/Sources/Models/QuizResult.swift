import Foundation

struct AnsweredQuestion {
    let question: QuizQuestion
    let userAnswerIndex: Int
    let isCorrect: Bool
    let timeTaken: TimeInterval
}

struct QuizResult {
    let totalQuestions: Int
    let correctAnswers: Int
    let incorrectAnswers: Int
    let answeredQuestions: [AnsweredQuestion]

    var scorePercentage: Int {
        guard totalQuestions > 0 else { return 0 }
        return (correctAnswers * 100) / totalQuestions
    }

    var passed: Bool { scorePercentage >= 70 }
}
