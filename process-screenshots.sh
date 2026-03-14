#!/bin/bash
# IPF Technical Rules Quiz - Screenshot Processing Script
# Organises and validates screenshots for Google Play Store submission

echo "======================================"
echo "IPF Technical Rules Quiz"
echo "Screenshot Processing Script"
echo "======================================"
echo

# Check if screenshots exist
if ! ls screenshots/raw/*.png &> /dev/null; then
    echo "ERROR: No screenshots found in screenshots/raw/"
    echo "Please run ./take-screenshots.sh first to capture screenshots"
    exit 1
fi

# Create output directories
echo "Creating output directories..."
mkdir -p screenshots/phone
mkdir -p screenshots/tablet-7
mkdir -p screenshots/tablet-10
echo

echo "======================================"
echo "Screenshot Organisation"
echo "======================================"
echo
echo "Google Play Store requirements:"
echo "- 2-8 screenshots per device type"
echo "- Format: 24-bit PNG or JPEG (no alpha)"
echo "- Min dimension: 320px, Max dimension: 3840px"
echo
echo "Please manually organise your screenshots:"
echo "1. Open: screenshots/raw/"
echo "2. Copy phone screenshots to: screenshots/phone/"
echo "3. (Optional) Copy 7\" tablet screenshots to: screenshots/tablet-7/"
echo "4. (Optional) Copy 10\" tablet screenshots to: screenshots/tablet-10/"
echo
echo "Recommended naming convention:"
echo "  01_home_screen.png"
echo "  02_quiz_question.png"
echo "  03_quiz_results.png"
echo "  04_answer_review.png"
echo

read -p "Open screenshots folder now? (y/n): " proceed
if [[ "$proceed" =~ ^[Yy]$ ]]; then
    open screenshots
    echo
    read -p "Organise your screenshots then press Enter to continue..."
fi

echo
echo "======================================"
echo "Screenshot Validation"
echo "======================================"
echo

# Count phone screenshots
phone_count=$(ls screenshots/phone/*.png screenshots/phone/*.jpg 2>/dev/null | wc -l | tr -d ' ')
echo "Phone screenshots found: $phone_count"
if [ "$phone_count" -lt 2 ]; then
    echo "WARNING: Less than 2 phone screenshots. Minimum 2 required."
fi
if [ "$phone_count" -gt 8 ]; then
    echo "WARNING: More than 8 phone screenshots. Maximum 8 allowed."
fi

# Count tablet screenshots
tablet7_count=$(ls screenshots/tablet-7/*.png screenshots/tablet-7/*.jpg 2>/dev/null | wc -l | tr -d ' ')
echo "7\" Tablet screenshots found: $tablet7_count"

tablet10_count=$(ls screenshots/tablet-10/*.png screenshots/tablet-10/*.jpg 2>/dev/null | wc -l | tr -d ' ')
echo "10\" Tablet screenshots found: $tablet10_count"

echo
echo "======================================"
echo "Processing Complete"
echo "======================================"
echo
echo "Screenshot Summary:"
echo "- Phone: $phone_count screenshots"
echo "- 7\" Tablet: $tablet7_count screenshots"
echo "- 10\" Tablet: $tablet10_count screenshots"
echo

if [ "$phone_count" -ge 2 ] && [ "$phone_count" -le 8 ]; then
    echo "✓ Phone screenshots meet Google Play requirements"
else
    echo "✗ Phone screenshots do not meet requirements (need 2-8)"
fi

echo
echo "Additional Validation Checklist:"
echo "[ ] Screenshots are 24-bit PNG or JPEG format"
echo "[ ] No screenshots have alpha channel"
echo "[ ] All screenshots are between 320px and 3840px"
echo "[ ] Screenshots show actual app content"
echo "[ ] Text is readable in all screenshots"
echo "[ ] No personal or test data visible"
echo "[ ] Screenshots are named descriptively"
echo
echo "Next steps:"
echo "1. Verify all screenshots meet Play Store requirements"
echo "2. Upload phone screenshots (required) to Play Console"
echo "3. Upload tablet screenshots (optional) if available"
echo "4. Create feature graphic (1024x500px)"
echo
