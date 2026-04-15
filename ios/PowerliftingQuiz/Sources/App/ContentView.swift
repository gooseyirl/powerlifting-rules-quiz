import SwiftUI

struct ContentView: View {
    @State private var path = NavigationPath()
    @State private var repository = QuizRepository()
    @State private var quizViewModel = QuizViewModel()

    var body: some View {
        NavigationStack(path: $path) {
            HomeView(path: $path)
                .navigationDestination(for: AppRoute.self) { route in
                    switch route {
                    case .quizSetup:
                        QuizSetupView(path: $path)
                    case .quiz:
                        QuizView(path: $path)
                    case .result:
                        // Implemented in Chunk 4
                        Text("Results coming soon")
                            .navigationTitle("Results")
                    case .review:
                        // Implemented in Chunk 4
                        Text("Review coming soon")
                            .navigationTitle("Review Answers")
                    }
                }
        }
        .environment(repository)
        .environment(quizViewModel)
        .onAppear { repository.loadIfNeeded() }
    }
}
