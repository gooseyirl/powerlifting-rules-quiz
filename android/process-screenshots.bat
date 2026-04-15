@echo off
REM IPF Technical Rules Quiz - Screenshot Processing Script
REM This script organizes and validates screenshots for Google Play Store submission

echo ======================================
echo IPF Technical Rules Quiz
echo Screenshot Processing Script
echo ======================================
echo.

REM Check if screenshots exist
if not exist "screenshots\raw\*.png" (
    echo ERROR: No screenshots found in screenshots\raw\
    echo Please run take-screenshots.bat first to capture screenshots
    pause
    exit /b 1
)

REM Create output directories
echo Creating output directories...
if not exist "screenshots\phone" mkdir screenshots\phone
if not exist "screenshots\tablet-7" mkdir screenshots\tablet-7
if not exist "screenshots\tablet-10" mkdir screenshots\tablet-10
echo.

echo ======================================
echo Screenshot Organization
echo ======================================
echo.
echo This script will help you organize screenshots into categories.
echo Google Play Store requirements:
echo - 2-8 screenshots per device type
echo - Format: 24-bit PNG or JPEG (no alpha)
echo - Min dimension: 320px
echo - Max dimension: 3840px
echo.

echo Please manually organize your screenshots:
echo.
echo 1. Open: screenshots\raw\
echo 2. Copy phone screenshots to: screenshots\phone\
echo 3. (Optional) Copy 7" tablet screenshots to: screenshots\tablet-7\
echo 4. (Optional) Copy 10" tablet screenshots to: screenshots\tablet-10\
echo.
echo Recommended naming convention:
echo   01_home_screen.png
echo   02_quiz_question.png
echo   03_quiz_results.png
echo   04_answer_review.png
echo.

set /p proceed="Open screenshots folder now? (Y/N): "
if /i "%proceed%"=="Y" (
    start "" "screenshots"
    echo.
    echo Folder opened. Organize your screenshots and press any key when done...
    pause >nul
)

echo.
echo ======================================
echo Screenshot Validation
echo ======================================
echo.

REM Count phone screenshots
set phone_count=0
for %%f in (screenshots\phone\*.png screenshots\phone\*.jpg) do set /a phone_count+=1

echo Phone screenshots found: %phone_count%
if %phone_count% LSS 2 (
    echo WARNING: Less than 2 phone screenshots. Minimum 2 required.
)
if %phone_count% GTR 8 (
    echo WARNING: More than 8 phone screenshots. Maximum 8 allowed.
)

REM Count tablet screenshots
set tablet7_count=0
for %%f in (screenshots\tablet-7\*.png screenshots\tablet-7\*.jpg) do set /a tablet7_count+=1
echo 7" Tablet screenshots found: %tablet7_count%

set tablet10_count=0
for %%f in (screenshots\tablet-10\*.png screenshots\tablet-10\*.jpg) do set /a tablet10_count+=1
echo 10" Tablet screenshots found: %tablet10_count%

echo.
echo ======================================
echo Processing Complete
echo ======================================
echo.
echo Screenshot Summary:
echo - Phone: %phone_count% screenshots
echo - 7" Tablet: %tablet7_count% screenshots
echo - 10" Tablet: %tablet10_count% screenshots
echo.

if %phone_count% GEQ 2 if %phone_count% LEQ 8 (
    echo ✓ Phone screenshots meet Google Play requirements
) else (
    echo ✗ Phone screenshots do not meet requirements (need 2-8)
)

echo.
echo Additional Validation Checklist:
echo [ ] Screenshots are 24-bit PNG or JPEG format
echo [ ] No screenshots have alpha channel
echo [ ] All screenshots are between 320px and 3840px
echo [ ] Screenshots show actual app content
echo [ ] Text is readable in all screenshots
echo [ ] No personal or test data visible
echo [ ] Screenshots are named descriptively
echo.

echo Next steps:
echo 1. Verify all screenshots meet Play Store requirements
echo 2. Upload phone screenshots (required) to Play Store Console
echo 3. Upload tablet screenshots (optional) if available
echo 4. Create feature graphic (1024x500px)
echo 5. Create app icon (512x512px)
echo.

pause
