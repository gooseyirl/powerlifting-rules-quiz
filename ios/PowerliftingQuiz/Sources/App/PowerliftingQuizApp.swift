import SwiftUI
import GoogleMobileAds
import AppTrackingTransparency

@main
struct PowerliftingQuizApp: App {
    init() {
        MobileAds.initialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    // Request ATT on first launch; AdMob serves non-personalised
                    // ads if the user declines — no action needed on the result.
                    ATTrackingManager.requestTrackingAuthorization { _ in }
                }
        }
    }
}
