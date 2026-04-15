import SwiftUI

struct ContentView: View {
    var body: some View {
        NavigationStack {
            VStack(spacing: 24) {
                Image(systemName: "trophy.fill")
                    .font(.system(size: 64))
                    .foregroundColor(.blue)
                Text("Powerlifting Quiz")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                Text("Coming soon")
                    .foregroundColor(.secondary)
            }
            .navigationTitle("Powerlifting Quiz")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
