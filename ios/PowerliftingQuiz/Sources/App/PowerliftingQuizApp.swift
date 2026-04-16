import SwiftUI
import GoogleMobileAds
import AppTrackingTransparency

@main
struct PowerliftingQuizApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    // Request ATT before initialising AdMob so the prompt appears
                    // before any tracking-related SDK activity begins.
                    ATTrackingManager.requestTrackingAuthorization { _ in
                        MobileAds.initialize()
                    }
                }
        }
    }
}
