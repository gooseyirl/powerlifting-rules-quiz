import SwiftUI

struct ContentView: View {
    @State private var path = NavigationPath()
    @State private var repository = QuizRepository()
    @State private var quizViewModel = QuizViewModel()
    @State private var storeManager = StoreManager()

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
                        ResultView(path: $path)
                    case .review:
                        ReviewView()
                    }
                }
        }
        .environment(repository)
        .environment(quizViewModel)
        .environment(storeManager)
        .onAppear { repository.loadIfNeeded() }
        .task { await storeManager.loadState() }
    }
}
