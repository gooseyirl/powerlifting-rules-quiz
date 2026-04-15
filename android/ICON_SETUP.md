# App Icon Setup

## Icon Details

The IPF Technical Rules Quiz app uses a custom icon featuring a powerlifting-themed design with a flexed bicep and technical circuit pattern, symbolizing the combination of strength and technical knowledge.

### Icon Characteristics

- **Design**: Flexed bicep with circuit board pattern overlay
- **Colors**: Navy blue outline with coral/salmon accent lines
- **Style**: Modern, minimalist line art
- **Format**: PNG with transparency
- **Theme**: Represents strength (powerlifting) and technical knowledge (circuit pattern)

## File Locations

The icon has been deployed to all required Android mipmap directories:

```
app/src/main/res/
├── mipmap-mdpi/
│   ├── ic_launcher.png (48x48dp)
│   └── ic_launcher_round.png (48x48dp)
├── mipmap-hdpi/
│   ├── ic_launcher.png (72x72dp)
│   └── ic_launcher_round.png (72x72dp)
├── mipmap-xhdpi/
│   ├── ic_launcher.png (96x96dp)
│   └── ic_launcher_round.png (96x96dp)
├── mipmap-xxhdpi/
│   ├── ic_launcher.png (144x144dp)
│   └── ic_launcher_round.png (144x144dp)
├── mipmap-xxxhdpi/
│   ├── ic_launcher.png (192x192dp)
│   └── ic_launcher_round.png (192x192dp)
└── mipmap-anydpi-v26/
    ├── ic_launcher.xml (Adaptive icon config)
    └── ic_launcher_round.xml (Adaptive icon config)
```

## Adaptive Icon Configuration

For Android 8.0 (API 26) and above, the app uses adaptive icons defined in:
- `mipmap-anydpi-v26/ic_launcher.xml`
- `mipmap-anydpi-v26/ic_launcher_round.xml`

These configurations use:
- **Background**: White (#FFFFFF) defined in `values/colors.xml` as `ic_launcher_background`
- **Foreground**: The icon PNG image

## AndroidManifest Configuration

The icon is referenced in `AndroidManifest.xml`:

```xml
<application
    android:icon="@mipmap/ic_launcher"
    android:roundIcon="@mipmap/ic_launcher_round"
    ...>
```

## Icon Usage in App

The icon is also displayed in the home screen of the app:
- Location: `fragment_home.xml`
- Size: 120dp x 120dp
- Position: Top center with 48dp top margin

## Google Play Store Requirements

For Play Store submission, you'll also need:

### App Icon (Required)
- **Size**: 512 x 512 pixels
- **Format**: 32-bit PNG with alpha
- **Location**: Should be created from the source icon
- **Note**: This is different from the launcher icons

### Feature Graphic (Required)
- **Size**: 1024 x 500 pixels
- **Format**: JPEG or 24-bit PNG (no alpha)
- **Content**: Should incorporate the app icon and branding

## Creating Play Store Assets

To create the Play Store icon (512x512):

### Option 1: Using Image Editor
1. Open `ipf-technical-rules-book-icon.png` in an image editor
2. Resize to 512x512 pixels (maintain transparency)
3. Export as 32-bit PNG
4. Save as `play-store-icon-512.png`

### Option 2: Using ImageMagick (if available)
```bash
convert ipf-technical-rules-book-icon.png -resize 512x512 play-store-icon-512.png
```

## Icon Design Rationale

The icon was chosen because it:
1. **Represents Powerlifting**: The flexed bicep is universally recognized as a strength symbol
2. **Represents Technical Knowledge**: The circuit pattern overlay symbolizes technical rules and systems
3. **Stands Out**: The navy and coral color scheme is distinctive and professional
4. **Scales Well**: The simple line art design remains clear at all sizes
5. **Follows Material Design**: Clean, modern aesthetic that fits Android design principles

## Color Palette

The icon uses colors that complement the app's IPF-themed color scheme:
- **Navy Blue**: Matches the professional, authoritative tone
- **Coral/Salmon**: Provides contrast and visual interest
- **White Background**: Clean, modern appearance that works on any launcher

## Future Considerations

If you want to customize the icon further:

1. **Add IPF Branding**: Could incorporate IPF logo elements (with permission)
2. **Simplify for Small Sizes**: Consider a simpler version for mdpi/hdpi
3. **Create Monochrome Icon**: For Android 13+ themed icons
4. **Seasonal Variants**: Different versions for special events or competitions

## Notes

- The current implementation uses the same PNG file for all densities. Ideally, you would have properly scaled versions for each density.
- Consider creating density-specific versions for optimal quality at each size.
- The icon has transparency, which works well with adaptive icons on Android 8.0+.
- The white background color ensures the icon looks good on all launcher backgrounds.

---

**Icon Source File**: `ipf-technical-rules-book-icon.png` (root directory)

**Last Updated**: November 22, 2025
