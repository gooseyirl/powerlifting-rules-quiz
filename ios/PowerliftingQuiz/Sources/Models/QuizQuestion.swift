import Foundation

struct QuizQuestion: Codable, Identifiable {
    let id: Int
    let question: String
    let options: [String]
    let correctAnswerIndex: Int
    let ruleReference: RuleReference
    let explanation: String?
    let ruleQuote: String?
    let status: String

    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(Int.self, forKey: .id)
        question = try container.decode(String.self, forKey: .question)
        options = try container.decode([String].self, forKey: .options)
        correctAnswerIndex = try container.decode(Int.self, forKey: .correctAnswerIndex)
        ruleReference = try container.decode(RuleReference.self, forKey: .ruleReference)
        explanation = try container.decodeIfPresent(String.self, forKey: .explanation)
        ruleQuote = try container.decodeIfPresent(String.self, forKey: .ruleQuote)
        status = (try container.decodeIfPresent(String.self, forKey: .status)) ?? "pending"
    }
}

struct RuleReference: Codable {
    let section: String
    let subsection: String?
    let ruleNumber: String
    let pageNumber: Int?

    var fullReference: String {
        var parts = ["Section \(section)"]
        if let sub = subsection { parts.append(sub) }
        parts.append("Rule \(ruleNumber)")
        if let page = pageNumber { parts.append("(Page \(page))") }
        return parts.joined(separator: " - ")
    }
}
