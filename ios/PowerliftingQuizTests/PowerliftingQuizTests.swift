import XCTest
@testable import PowerliftingQuiz

final class QuizQuestionTests: XCTestCase {

    // MARK: - RuleReference

    func testFullReferenceAllFields() {
        let ref = RuleReference(section: "Equipment", subsection: "Bars", ruleNumber: "a.3", pageNumber: 8)
        XCTAssertEqual(ref.fullReference, "Section Equipment - Bars - Rule a.3 - (Page 8)")
    }

    func testFullReferenceNoSubsectionNoPage() {
        let ref = RuleReference(section: "Equipment", subsection: nil, ruleNumber: "a.3", pageNumber: nil)
        XCTAssertEqual(ref.fullReference, "Section Equipment - Rule a.3")
    }

    // MARK: - QuizResult

    func testScorePercentage() {
        let result = makeResult(total: 10, correct: 7)
        XCTAssertEqual(result.scorePercentage, 70)
    }

    func testScorePercentageZeroTotal() {
        let result = makeResult(total: 0, correct: 0)
        XCTAssertEqual(result.scorePercentage, 0)
    }

    func testPassedAt70Percent() {
        XCTAssertTrue(makeResult(total: 10, correct: 7).passed)
    }

    func testFailedBelow70Percent() {
        XCTAssertFalse(makeResult(total: 10, correct: 6).passed)
    }

    // MARK: - JSON decoding

    func testDecodeMinimalQuestion() throws {
        let json = """
        {
          "id": 1,
          "question": "Test?",
          "options": ["A", "B", "C", "D"],
          "correctAnswerIndex": 0,
          "ruleReference": {
            "section": "General",
            "ruleNumber": "1.1"
          }
        }
        """.data(using: .utf8)!

        let q = try JSONDecoder().decode(QuizQuestion.self, from: json)
        XCTAssertEqual(q.id, 1)
        XCTAssertEqual(q.correctAnswerIndex, 0)
        XCTAssertNil(q.explanation)
        XCTAssertNil(q.ruleReference.subsection)
        XCTAssertEqual(q.status, "pending")
    }

    func testDecodeFullQuestion() throws {
        let json = """
        {
          "id": 42,
          "question": "Diameter?",
          "options": ["26-27mm", "28-29mm", "30-31mm", "32-33mm"],
          "correctAnswerIndex": 1,
          "ruleReference": {
            "section": "Equipment",
            "subsection": "Bars and Discs",
            "ruleNumber": "a.3",
            "pageNumber": 8
          },
          "explanation": "Must be 28–29mm.",
          "ruleQuote": "\\"Some quote\\"",
          "status": "validated"
        }
        """.data(using: .utf8)!

        let q = try JSONDecoder().decode(QuizQuestion.self, from: json)
        XCTAssertEqual(q.id, 42)
        XCTAssertEqual(q.status, "validated")
        XCTAssertEqual(q.ruleReference.pageNumber, 8)
        XCTAssertNotNil(q.ruleQuote)
    }

    // MARK: - Helpers

    private func makeResult(total: Int, correct: Int) -> QuizResult {
        QuizResult(
            totalQuestions: total,
            correctAnswers: correct,
            incorrectAnswers: total - correct,
            answeredQuestions: []
        )
    }
}
