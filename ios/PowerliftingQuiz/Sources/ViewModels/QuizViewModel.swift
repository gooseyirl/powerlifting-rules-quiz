import Foundation

@Observable
final class QuizViewModel {
    private(set) var currentQuestion: QuizQuestion?
    private(set) var currentQuestionNumber = 0
    private(set) var totalQuestions = 0
    private(set) var selectedAnswerIndex: Int?
    private(set) var quizCompleted = false
    private(set) var lastQuestionCount = 10

    private var questions: [QuizQuestion] = []
    private var currentIndex = 0
    private var answeredQuestions: [AnsweredQuestion] = []
    private var questionStartTime = Date()

    func startQuiz(count: Int, from repository: QuizRepository) {
        lastQuestionCount = count
        questions = repository.randomQuestions(count: count)
        currentIndex = 0
        answeredQuestions = []
        totalQuestions = questions.count
        quizCompleted = false
        selectedAnswerIndex = nil
        loadCurrentQuestion()
    }

    func selectAnswer(_ index: Int) {
        selectedAnswerIndex = index
    }

    func submitAnswer() {
        guard let index = selectedAnswerIndex,
              let question = currentQuestion else { return }

        answeredQuestions.append(AnsweredQuestion(
            question: question,
            userAnswerIndex: index,
            isCorrect: index == question.correctAnswerIndex,
            timeTaken: Date().timeIntervalSince(questionStartTime)
        ))

        currentIndex += 1
        loadCurrentQuestion()
    }

    func getQuizResult() -> QuizResult {
        let correct = answeredQuestions.filter(\.isCorrect).count
        return QuizResult(
            totalQuestions: questions.count,
            correctAnswers: correct,
            incorrectAnswers: questions.count - correct,
            answeredQuestions: answeredQuestions
        )
    }

    private func loadCurrentQuestion() {
        if currentIndex < questions.count {
            currentQuestion = questions[currentIndex]
            currentQuestionNumber = currentIndex + 1
            selectedAnswerIndex = nil
            questionStartTime = Date()
        } else {
            quizCompleted = true
        }
    }
}
