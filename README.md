# IPF Technical Rules Quiz

> **Disclaimer:** This is an unofficial, independent app. It is not affiliated with, endorsed by, or associated with the International Powerlifting Federation (IPF).

An Android application for testing knowledge of the International Powerlifting Federation (IPF) Technical Rules Book.

## Overview

This app provides an interactive multiple-choice quiz based on the IPF Technical Rules Book (March 2025 version). It helps powerlifters, coaches, and officials learn and test their understanding of the technical rules governing IPF competitions.

## Features

- **Multiple Choice Quiz**: 10 randomly selected questions per quiz session
- **Comprehensive Coverage**: Questions covering equipment specifications, rules of performance, judging, and personal equipment
- **Immediate Results**: View your score and performance summary upon completion
- **Answer Review**: Review all answers with detailed explanations and rulebook references
- **Rule References**: Each question includes the specific section, subsection, and rule number from the official rulebook
- **Pass/Fail Indication**: 70% passing threshold with clear visual feedback
- **Material Design 3**: Modern, clean UI with IPF-themed colors
- **Custom Icon**: Professional powerlifting-themed app icon

## Version Information

- **App Version**: 1.0.0
- **Version Code**: 1
- **Target SDK**: API 35 (Android 15)
- **Min SDK**: API 24 (Android 7.0)
- **Compile SDK**: API 35
- **AGP Version**: 8.13.0
- **Gradle Version**: 8.13
- **Rules Version**: IPF Technical Rules Book (March 2025)

## Screenshots

Screenshots will be available in the `screenshots/` directory after running the screenshot automation scripts.

## Building the App

### Prerequisites

- Android Studio Otter 2025.2.1 or later
- JDK 17
- Android SDK with API 35
- Gradle 8.13 or later

### Debug Build

1. Clone the repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Run the app on an emulator or device:
   ```
   gradlew installDebug
   ```
   Or use Android Studio's Run button

### Release Build

1. Generate or configure a signing key
2. Update `app/build.gradle` with signing configuration
3. Build the release APK:
   ```
   gradlew assembleRelease
   ```
4. Or build the Android App Bundle (recommended for Play Store):
   ```
   gradlew bundleRelease
   ```

## Testing

### Running Tests

Run unit tests:
```
gradlew test
```

Run instrumented tests:
```
gradlew connectedAndroidTest
```

### Manual Testing

1. Install the app on a device or emulator
2. Start a quiz and verify all 10 questions load correctly
3. Select answers and verify navigation works
4. Complete the quiz and verify results screen shows correct score
5. Review answers and verify all rule references are displayed
6. Test retake quiz and back to home navigation

## Screenshot Automation

To capture and process screenshots for the Play Store:

1. Connect a device or start an emulator
2. Run the screenshot capture script:
   ```
   take-screenshots.bat
   ```
3. Process screenshots to meet Play Store requirements:
   ```
   process-screenshots.bat
   ```

## Architecture

The app follows modern Android development practices:

- **MVVM Architecture**: Separation of concerns with ViewModel
- **ViewBinding**: Type-safe view access
- **Navigation Component**: Fragment navigation management
- **LiveData**: Reactive data observation
- **Material Design 3**: Modern UI components and theming
- **Repository Pattern**: Data access abstraction

### Project Structure

```
app/src/main/java/com/ipf/technicalrulesquiz/
├── data/
│   ├── model/          # Data classes (QuizQuestion, QuizResult, etc.)
│   └── repository/     # Data repository (QuizRepository)
├── ui/
│   ├── home/          # Home screen
│   ├── quiz/          # Quiz screen and ViewModel
│   ├── result/        # Results screen
│   └── review/        # Answer review screen with RecyclerView
└── MainActivity.kt
```

## Adding More Questions

To expand the quiz question bank:

1. Open `app/src/main/java/com/ipf/technicalrulesquiz/data/repository/QuizRepository.kt`
2. Add new `QuizQuestion` objects to the `sampleQuestions` list
3. Include accurate rule references from the IPF Technical Rules Book
4. Ensure questions have 2-4 answer options with the correct answer index
5. Add explanations to help users learn

Example:
```kotlin
QuizQuestion(
    id = 16,
    question = "Your question here?",
    options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
    correctAnswerIndex = 2,  // Zero-based index
    ruleReference = RuleReference(
        section = "Section Name",
        subsection = "Subsection Name",
        ruleNumber = "X.Y.Z",
        pageNumber = 42
    ),
    explanation = "Brief explanation of the correct answer."
)
```

## Development Guidelines

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Keep functions small and focused
- Document complex logic with comments

### Security

- No user data is collected or transmitted
- No internet permissions required
- All quiz data is local
- No analytics or tracking

### Contributing

When contributing:

1. Test all changes thoroughly
2. Update documentation if needed
3. Follow existing code patterns
4. Ensure builds succeed before committing

## Version History

### Version 1.0.0 (2025-11-22)
- Initial release
- 15 sample questions covering key IPF technical rules
- Quiz functionality with results and review
- Material Design 3 UI
- Custom powerlifting-themed app icon
- Based on March 2025 IPF Technical Rules
- Bug fix: Added missing Fragment, RecyclerView, and Activity dependencies

## License

This application is for educational purposes. The IPF Technical Rules Book is the property of the International Powerlifting Federation.

## Resources

- [IPF Official Website](https://www.powerlifting.sport/)
- [IPF Technical Rules Book](https://www.powerlifting.sport/rules/codes/info/technical-rules)
- [Android Developer Documentation](https://developer.android.com/)

## Contact

For questions, issues, or suggestions, please open an issue in the repository.

---

This application is for educational purposes only. The IPF Technical Rules Book is the property of the International Powerlifting Federation.
