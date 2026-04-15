import SwiftUI

struct SettingsView: View {
    @AppStorage("appTheme") private var theme = AppTheme.system
    @Environment(StoreManager.self) private var storeManager
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationStack {
            Form {
                // MARK: Appearance
                Section {
                    Picker("Theme", selection: $theme) {
                        ForEach(AppTheme.allCases, id: \.self) { option in
                            Text(option.label).tag(option)
                        }
                    }
                    .pickerStyle(.segmented)
                } header: {
                    Text("Appearance")
                } footer: {
                    Text("Choose how the app looks. \"System\" follows your device's setting.")
                }

                // MARK: Purchases
                Section {
                    if storeManager.isAdFree {
                        Label("Ads Removed", systemImage: "checkmark.shield.fill")
                            .foregroundStyle(.green)
                    } else {
                        Button {
                            Task { await storeManager.purchase() }
                        } label: {
                            HStack {
                                Label("Remove Ads", systemImage: "xmark.shield")
                                    .foregroundStyle(.primary)
                                Spacer()
                                if storeManager.isLoading {
                                    ProgressView()
                                } else if let price = storeManager.displayPrice {
                                    Text(price)
                                        .foregroundStyle(.secondary)
                                }
                            }
                        }
                        .disabled(storeManager.isLoading)

                        Button("Restore Purchase") {
                            Task { await storeManager.restore() }
                        }
                        .foregroundStyle(.secondary)
                        .disabled(storeManager.isLoading)
                    }
                } header: {
                    Text("Purchases")
                } footer: {
                    if !storeManager.isAdFree {
                        Text("One-time purchase to permanently remove all advertisements.")
                    }
                }
            }
            .navigationTitle("Settings")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") { dismiss() }
                }
            }
            .alert("Purchase Failed", isPresented: Binding(
                get: { storeManager.purchaseError != nil },
                set: { _ in }
            )) {
                Button("OK", role: .cancel) {}
            } message: {
                Text(storeManager.purchaseError ?? "")
            }
        }
        .preferredColorScheme(theme.colorScheme)
    }
}
