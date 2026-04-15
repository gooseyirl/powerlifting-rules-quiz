#!/bin/bash
# IPF Technical Rules Quiz - Screenshot Capture Script
# Captures screenshots from a connected Android device or emulator

echo "======================================"
echo "IPF Technical Rules Quiz"
echo "Screenshot Capture Script"
echo "======================================"
echo

# Check if adb is available
if ! command -v adb &> /dev/null; then
    echo "ERROR: ADB not found in PATH"
    echo "Please ensure Android SDK platform-tools is installed and in your PATH"
    echo "Typical location: ~/Library/Android/sdk/platform-tools"
    exit 1
fi

# Check for connected devices
echo "Checking for connected devices..."
adb devices -l
echo

# Create screenshots directory if it doesn't exist
mkdir -p screenshots/raw

# Create timestamp for this session
timestamp=$(date +"%Y%m%d_%H%M")

echo "Screenshots will be saved to: screenshots/raw/"
echo

capture_screenshot() {
    read -p "Enter screenshot name (e.g., home_screen): " screenname
    echo "Capturing screenshot..."
    adb shell screencap -p /sdcard/screenshot_temp.png
    echo "Pulling screenshot from device..."
    adb pull /sdcard/screenshot_temp.png "screenshots/raw/${timestamp}_${screenname}.png"
    adb shell rm /sdcard/screenshot_temp.png
    echo "Screenshot saved as: ${timestamp}_${screenname}.png"
    echo
}

while true; do
    echo "======================================"
    echo "Screenshot Capture Menu"
    echo "======================================"
    echo "1. Capture Screenshot"
    echo "2. Open screenshots folder"
    echo "3. Exit"
    echo "======================================"
    echo
    read -p "Enter your choice (1-3): " choice

    case $choice in
        1) capture_screenshot ;;
        2) open screenshots/raw ;;
        3) break ;;
        *) echo "Invalid choice. Please try again." ; echo ;;
    esac
done

echo
echo "Screenshot capture complete!"
echo "Screenshots saved in: screenshots/raw/"
echo
echo "Next steps:"
echo "1. Review screenshots in screenshots/raw/"
echo "2. Run ./process-screenshots.sh to organise them for Play Store"
echo
