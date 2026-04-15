import SwiftUI

private let rulesBookURL = URL(string: "https://www.powerlifting.sport/fileadmin/ipf/data/rules/technical-rules/english/2026_IPF_Technical_Rulebook__effective_01_March_2026__v3.pdf")!
private let developerEmail = "gooseyirl+plrulesquiz@gmail.com"

struct HomeView: View {
    @Binding var path: NavigationPath
    @Environment(QuizRepository.self) private var repository
    @Environment(\.openURL) private var openURL

    @State private var showSettings = false
    @State private var showSuggestDialog = false

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Hero
                VStack(spacing: 12) {
                    Image(systemName: "trophy.fill")
                        .font(.system(size: 72))
                        .foregroundStyle(.blue)
                    Text("Powerlifting Rules Quiz")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center)
                    Text("Test your knowledge of the International Powerlifting Federation Technical Rules")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                        .multilineTextAlignment(.center)
                }
                .padding(.top, 16)

                // About card
                VStack(alignment: .leading, spacing: 8) {
                    Label("About This Quiz", systemImage: "info.circle.fill")
                        .font(.headline)
                    Text("This quiz contains multiple-choice questions based on the IPF Technical Rules Book (March 2026). Each question includes a reference to the specific rule section for learning purposes.")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
                .padding()
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color(.secondarySystemGroupedBackground))
                .clipShape(RoundedRectangle(cornerRadius: 12))

                // Start quiz
                Button {
                    path.append(AppRoute.quizSetup)
                } label: {
                    Label("Start Quiz", systemImage: "play.fill")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
                .controlSize(.large)
                .disabled(repository.totalValidated == 0)

                Divider()

                // Secondary actions
                VStack(spacing: 12) {
                    Link(destination: rulesBookURL) {
                        Label("Based on IPF Technical Rules (March 2026)", systemImage: "doc.text")
                            .font(.footnote)
                            .foregroundStyle(.secondary)
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.plain)

                    Button {
                        showSuggestDialog = true
                    } label: {
                        Label("Suggest a question", systemImage: "lightbulb")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
            }
            .padding()
        }
        .navigationTitle("Home")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color(.systemGroupedBackground))
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button {
                    showSettings = true
                } label: {
                    Image(systemName: "gear")
                }
            }
        }
        .sheet(isPresented: $showSettings) {
            SettingsView()
        }
        .confirmationDialog(
            "Suggest a question?",
            isPresented: $showSuggestDialog,
            titleVisibility: .visible
        ) {
            Button("Open Email") { sendSuggestEmail() }
            Button("Cancel", role: .cancel) {}
        } message: {
            Text("This will open your email app with a template for submitting a question suggestion to the developer.")
        }
    }

    private func sendSuggestEmail() {
        let subject = "[Powerlifting Rules Quiz] Question Suggestion".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        let body = "Question:\n\n\nOption A:\nOption B:\nOption C:\nOption D:\n\nCorrect Answer:\n\nRule Reference (section/page):\n"
            .addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        if let url = URL(string: "mailto:\(developerEmail)?subject=\(subject)&body=\(body)") {
            openURL(url)
        }
    }
}
