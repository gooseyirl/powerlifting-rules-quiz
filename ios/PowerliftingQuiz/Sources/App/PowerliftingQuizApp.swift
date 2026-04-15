import SwiftUI
import GoogleMobileAds

@main
struct PowerliftingQuizApp: App {
    init() {
        MobileAds.initialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
