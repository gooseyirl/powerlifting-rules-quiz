@echo off
REM IPF Technical Rules Quiz - Screenshot Capture Script
REM This script helps capture screenshots from a connected Android device or emulator

echo ======================================
echo IPF Technical Rules Quiz
echo Screenshot Capture Script
echo ======================================
echo.

REM Check if adb is available
where adb >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: ADB not found in PATH
    echo Please ensure Android SDK platform-tools is installed and in your PATH
    echo Typical location: C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools
    pause
    exit /b 1
)

REM Check for connected devices
echo Checking for connected devices...
adb devices -l
echo.

REM Create screenshots directory if it doesn't exist
if not exist "screenshots" mkdir screenshots
if not exist "screenshots\raw" mkdir screenshots\raw

REM Create timestamp for this session
for /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
for /f "tokens=1-2 delims=/:" %%a in ('time /t') do (set mytime=%%a%%b)
set timestamp=%mydate%_%mytime%

echo Screenshots will be saved to: screenshots\raw\
echo.

:menu
echo ======================================
echo Screenshot Capture Menu
echo ======================================
echo 1. Capture Screenshot
echo 2. View Screenshots on Device
echo 3. Exit
echo ======================================
echo.

set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" goto capture
if "%choice%"=="2" goto view
if "%choice%"=="3" goto end
echo Invalid choice. Please try again.
echo.
goto menu

:capture
echo.
set /p screenname="Enter screenshot name (e.g., home_screen): "

REM Capture screenshot on device
echo Capturing screenshot...
adb shell screencap -p /sdcard/screenshot_temp.png

REM Pull screenshot to local folder
echo Pulling screenshot from device...
adb pull /sdcard/screenshot_temp.png "screenshots\raw\%timestamp%_%screenname%.png"

REM Delete screenshot from device
adb shell rm /sdcard/screenshot_temp.png

echo Screenshot saved as: %timestamp%_%screenname%.png
echo.
goto menu

:view
echo.
echo Opening screenshots folder...
start "" "screenshots\raw"
echo.
goto menu

:end
echo.
echo Screenshot capture complete!
echo Screenshots saved in: screenshots\raw\
echo.
echo Next steps:
echo 1. Review screenshots in screenshots\raw\
echo 2. Run process-screenshots.bat to organize them for Play Store
echo.
pause
