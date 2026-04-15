import StoreKit

private let adFreeProductID = "com.gooseco.technicalrulesquiz.removeads"
private let adFreeDefaultsKey = "isAdFree"

@Observable
@MainActor
final class StoreManager {
    private(set) var isAdFree: Bool
    private(set) var isLoading = false
    private(set) var purchaseError: String?
    private(set) var displayPrice: String?

    init() {
        // Fast-path: read cached state so ads aren't briefly shown on launch
        isAdFree = UserDefaults.standard.bool(forKey: adFreeDefaultsKey)
    }

    /// Call once at startup to verify entitlements and fetch the product price.
    func loadState() async {
        await checkEntitlements()
        await fetchPrice()
    }

    func purchase() async {
        isLoading = true
        purchaseError = nil
        defer { isLoading = false }

        do {
            let products = try await Product.products(for: [adFreeProductID])
            guard let product = products.first else {
                purchaseError = "Product unavailable. Please try again later."
                return
            }
            let result = try await product.purchase()
            switch result {
            case .success(let verification):
                if case .verified(let transaction) = verification {
                    await transaction.finish()
                    setAdFree(true)
                }
            case .userCancelled, .pending:
                break
            @unknown default:
                break
            }
        } catch {
            purchaseError = error.localizedDescription
        }
    }

    func restore() async {
        isLoading = true
        defer { isLoading = false }
        try? await AppStore.sync()
        await checkEntitlements()
    }

    // MARK: - Private

    private func checkEntitlements() async {
        for await result in Transaction.currentEntitlements {
            if case .verified(let tx) = result,
               tx.productID == adFreeProductID,
               tx.revocationDate == nil {
                setAdFree(true)
                return
            }
        }
    }

    private func fetchPrice() async {
        let products = try? await Product.products(for: [adFreeProductID])
        displayPrice = products?.first?.displayPrice
    }

    private func setAdFree(_ value: Bool) {
        isAdFree = value
        UserDefaults.standard.set(value, forKey: adFreeDefaultsKey)
    }
}
