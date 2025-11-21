# 📱 Contact Management System - Android App

A modern, feature-rich Android contact management application built with Java. This app allows users to efficiently manage their device contacts with an intuitive interface and comprehensive CRUD operations.

## ✨ Features

### Core Functionality
- 📋 **View Contacts**: Display all device contacts in a scrollable RecyclerView with Material Design cards
- ➕ **Add Contacts**: Create new contacts with name, phone number, and email
- ✏️ **Edit Contacts**: Update existing contact information
- 🗑️ **Delete Contacts**: Remove unwanted contacts with confirmation dialog
- 📞 **Call Contacts**: Direct calling functionality with permission handling
- 💬 **Send SMS**: Quick messaging feature integrated with SMS app


### User Experience
- 🎨 Modern Material Design UI
- 🔒 Runtime permission management using Dexter library
- ⚡ Fast and responsive interface
- 📱 Optimized for Android 7.0 (API 24) and above
- 🌈 Clean and intuitive navigation

## 🛠️ Technologies Used

- **Language**: Java
- **IDE**: Android Studio 2025
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Build System**: Gradle (Kotlin DSL)

### Libraries & Dependencies
```gradle
// Permission handling
implementation 'com.karumi:dexter:6.2.3'

// Material Design Components
implementation 'com.google.android.material:material:1.9.0'

// AndroidX Libraries
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.recyclerview:recyclerview:1.3.1'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
```

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/yourname/contactsapp/
│   │   │   ├── MainActivity.java              # Main activity with contacts list
│   │   │   ├── ContactDetailActivity.java     # Contact details & actions
│   │   │   ├── CreateNewContactActivity.java  # Add/Edit contact form
│   │   │   ├── Contacts.java                  # Contact model class
│   │   │   └── Adapter.java                   # RecyclerView adapter
│   │   │
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_contact_detail.xml
│   │   │   │   ├── activity_create_new_contact.xml
│   │   │   │   └── contacts_rv_item.xml
│   │   │   │
│   │   │   ├── drawable/
│   │   │   │   ├── person.xml
│   │   │   │   └── circle_background.xml
│   │   │   │
│   │   │   └── values/
│   │   │       ├── colors.xml
│   │   │       └── strings.xml
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
```

## 🚀 Getting Started

### Prerequisites
- Android Studio 2025 or later
- JDK 17 or higher
- Android SDK (API 24+)
- Physical Android device or emulator

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/mariem1mansour/ContactManagementSystem_Java_Android.git
cd ContactManagementSystem_Java_Android
```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle files
   - Wait for dependencies to download

4. **Update Package Name** (if needed)
   - Replace `com.yourname.contactsapp` with your package name in all Java files

5. **Run the App**
   - Connect your Android device or start an emulator
   - Click the "Run" button (green play icon)
   - Select your device/emulator
   - Grant necessary permissions when prompted

## 📱 Required Permissions

The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.SEND_SMS" />
```

All permissions are handled at runtime using the Dexter library for a smooth user experience.

## 🎯 How to Use

### Viewing Contacts
1. Launch the app
2. Grant required permissions when prompted
3. All device contacts will be displayed in a scrollable list

### Adding a New Contact
1. Tap the floating action button (➕) at the bottom right
2. Fill in contact details:
   - Name (required)
   - Phone number (required)
   - Email (optional)
3. Tap "Save Contact"

### Viewing Contact Details
1. Tap any contact from the list
2. View full contact information
3. Use action buttons:
   - 📞 **Call**: Initiate a phone call
   - 💬 **Message**: Send an SMS
   - ✏️ **Edit**: Modify contact information
   - 🗑️ **Delete**: Remove the contact

### Editing a Contact
1. Open contact details
2. Tap the "Edit" button
3. Modify the information
4. Save changes

### Deleting a Contact
1. Open contact details
2. Tap the "Delete" button
3. Confirm deletion in the dialog

## 🎨 UI Components

### Main Screen
- **RecyclerView**: Displays all contacts in a scrollable list
- **FloatingActionButton**: Quick access to add new contacts
- **CardView**: Material Design cards for each contact item

### Contact Detail Screen
- **Profile Section**: Visual representation with colored background
- **Action Buttons**: Call and message functionality
- **Edit/Delete Options**: Manage contact information

### Add/Edit Contact Screen
- **TextInputLayout**: Material Design text fields
- **Validation**: Input validation for required fields
- **Save/Cancel Actions**: User-friendly form controls

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Mariem Mansour**
- GitHub: [@mariem1mansour](https://github.com/mariem1mansour)
- Repository: [ContactManagementSystem_Java_Android](https://github.com/mariem1mansour/ContactManagementSystem_Java_Android)

## 🙏 Acknowledgments

- [Dexter](https://github.com/Karumi/Dexter) - Android permissions library
- [Material Design](https://material.io/design) - UI/UX guidelines
- [Android Developers](https://developer.android.com/) - Documentation and resources
- GeeksforGeeks - Tutorial inspiration

## 📸 Screenshots

> To Add :)

## 🔮 Future Enhancements

- [ ] Search and filter functionality
- [ ] Contact grouping and favorites
- [ ] Export/Import contacts (VCF format)
- [ ] Dark mode support
- [ ] Contact backup to cloud
- [ ] Advanced contact editing (multiple phone numbers, addresses)
- [ ] Contact sharing functionality
- [ ] Integration with social media

## 📞 Support

If you encounter any issues or have questions, please [open an issue](https://github.com/mariem1mansour/ContactManagementSystem_Java_Android/issues) on GitHub.

---

**⭐ If you find this project useful, please consider giving it a star!**
