# Powerlifting Rules Quiz

> **Disclaimer:** This is an unofficial, independent app. It is not affiliated with, endorsed by, or associated with the International Powerlifting Federation (IPF).

An interactive multiple-choice quiz to test your knowledge of the IPF Technical Rules Book. Aimed at powerlifters, coaches, referees, and meet officials.

## Repository Structure

```
powerlifting-rules-quiz/
├── android/          # Android app (Kotlin, Material Design 3)
├── ios/              # iOS app (SwiftUI)
├── shared/           # Shared resources (questions, app store assets)
├── docs/             # GitHub Pages — privacy policy and app info
├── rulesbooks/       # IPF Technical Rules Book PDFs
└── validation-tool/  # Browser-based question validation tool
```

## Android

See [`android/`](android/) for build instructions and [`android/PLAY_STORE.md`](android/PLAY_STORE.md) for the Play Store listing.

**Play Store**: [Powerlifting Rules Quiz](https://play.google.com/store/apps/details?id=com.gooseco.technicalrulesquiz)

## iOS

See [`ios/`](ios/) for build instructions and [`ios/APP_STORE.md`](ios/APP_STORE.md) for the App Store listing.

**App Store**: Coming soon

## Shared Resources

- **Questions**: [`shared/questions.json`](shared/questions.json) — single source of truth for both platforms
- **Privacy Policy**: [gooseyirl.github.io/ipf-technical-rules-book-quiz/privacy-policy/](https://gooseyirl.github.io/ipf-technical-rules-book-quiz/privacy-policy/)
- **Rules Books**: The [`rulesbooks/`](rulesbooks/) directory contains the official IPF Technical Rules Book PDFs used as the source for all quiz questions.
- **Validation Tool**: The [`validation-tool/`](validation-tool/) directory contains a browser-based tool for reviewing and validating quiz questions before they are added to the app.
