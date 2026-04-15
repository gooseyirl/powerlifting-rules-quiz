# Build Instructions

## Initial Setup

Before building the project, you need to set up the Gradle wrapper. This only needs to be done once.

### Option 1: Using Android Studio (Recommended)

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the project directory and open it
4. Android Studio will automatically:
   - Download the Gradle wrapper
   - Sync Gradle files
   - Download dependencies
   - Set up the build environment

### Option 2: Manual Gradle Wrapper Setup

If you want to build from command line without Android Studio:

1. Ensure you have Gradle installed on your system
2. Navigate to the project directory
3. Run: `gradle wrapper --gradle-version 8.13`
4. This will create the gradle/wrapper/gradle-wrapper.jar file

### Option 3: Download Gradle Wrapper Jar

If you don't have Gradle installed:

1. Download gradle-wrapper.jar from: https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar
2. Place it in: `gradle/wrapper/gradle-wrapper.jar`

## Building the App

Once the Gradle wrapper is set up:

### Debug Build

Windows:
```batch
gradlew.bat assembleDebug
```

Linux/Mac:
```bash
./gradlew assembleDebug
```

The APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release Build

Windows:
```batch
gradlew.bat assembleRelease
```

Linux/Mac:
```bash
./gradlew assembleRelease
```

### Android App Bundle (for Play Store)

Windows:
```batch
gradlew.bat bundleRelease
```

Linux/Mac:
```bash
./gradlew bundleRelease
```

## Requirements

- JDK 17 or later
- Android SDK with API 35
- Gradle 8.13 or later (via wrapper)
- Android SDK Build Tools 35.0.0 or later

## Troubleshooting

### "gradlew.bat is not recognized"

Make sure you're in the project root directory where gradlew.bat is located.

### "JAVA_HOME is not set"

Set the JAVA_HOME environment variable to point to your JDK 17 installation.

Windows:
```batch
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

Linux/Mac:
```bash
export JAVA_HOME=/path/to/jdk-17
```

### "SDK location not found"

Create a `local.properties` file in the project root with:
```
sdk.dir=C:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
```
(Adjust path for your system)

### Gradle sync failed

1. Open Android Studio
2. File → Invalidate Caches and Restart
3. Let Android Studio re-sync the project

## Verifying the Build

After building, you can install the APK on a device:

```batch
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Clean Build

To perform a clean build:

Windows:
```batch
gradlew.bat clean assembleDebug
```

Linux/Mac:
```bash
./gradlew clean assembleDebug
```
