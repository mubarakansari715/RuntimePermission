
# Android Runtime User Permissions - Kotlin

This library simplifies handling runtime permissions in Android applications. Follow the steps below to integrate it into your project.

### Key Features:
- **Supports Android Versions up to 34**: The library is fully compatible with the latest Android APIs, ensuring seamless operation across all Android versions.
- **Easy Integration**: Minimal setup to add runtime permission handling.
- **Single and Multiple Permission Requests**: Easily request one or more permissions at once.
- **Customizable Rationale Dialogs**: Handle cases when users deny permissions and show a rationale dialog to guide them.
- **Optimized for Fragments and Activities**: Works smoothly with both Activity and Fragment.

---

## Step 1: Add the JitPack Maven Repository
Ensure your project includes the JitPack Maven repository. Add the following to your `dependencyResolutionManagement` section in your `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
    }
}
```

## Step 2: Add the Dependency
Add the following dependency to your app's `build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.mubarakansari715:RuntimePermission:1.0.0'
}
```

## Step 3: Extend BasePermission Classes
To use the permission functionality, extend either the `BasePermissionActivity` for an Activity or `BasePermissionFragment` for a Fragment.

**For Activity:**
```kotlin
class MainActivity : BasePermissionActivity() {
    // Your code
}
```

**For Fragment:**
```kotlin
class MyFragment : BasePermissionFragment() {
    // Your code
}
```

## Step 4: Request Single User Permission
To request a single permission from the user, override the following function:

```kotlin
override fun getSinglePermission(): String = android.Manifest.permission.CAMERA
```

To trigger the permission request, use a button or similar UI component:

```kotlin
binding.btnSingleUserPermission.setOnClickListener {
    //ask user runtime permission
    requestSinglePermission()
}
```

Handle the permission result with this method:

```kotlin
override fun onSinglePermissionGranted() {
    super.onSinglePermissionGranted()
    // Your logic after the permission is granted
    takePicture()
}
```

## Step 5: Request Multiple User Permissions
For requesting multiple permissions, override the following:

```kotlin
override fun getMultiplePermissions(): Array<String> = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.ACCESS_FINE_LOCATION
)
```

To request multiple permissions:

```kotlin
binding.btnMultiUserPermission.setOnClickListener {
    //ask user runtime permission
    requestMultiplePermissions()
}
```

Handle the granted permissions:

```kotlin
override fun onMultiplePermissionsGranted() {
    super.onMultiplePermissionsGranted()
    // Your logic after permissions are granted
    takePicture()
}
```

## Step 6: Handle Rationale for Denied Permissions
If the user denies permissions, you can show a rationale dialog by overriding this function:

```kotlin
override fun showPermissionRationaleDialog(permission: String) {
    // Direct user to app settings if necessary
    showSettingsDialog(this)
}
```

## Step 7: Handling Permission Result
To handle the result of the permission request and access additional data (such as intent data), override the following function:

```kotlin
override fun onPermissionResult(activityResult: ActivityResult?) {
    // Your logic after receiving the permission result
    val data = activityResult?.data
    
    // Use 'data' to handle actions after permission is granted or denied
}
```

## Step 8: Video Tutorials
For more information, check out our video tutorials covering how to implement and use these permission methods effectively:

- ![Screen_recording_20241004_224301-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/928f13b1-fb57-4e1a-8559-3b248ef85139)

- ![Screen_recording_20241004_224205-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/dfcad089-dbf3-4d19-bca6-9caea531c741)

---

### Contributions & Feedback

We welcome contributions, feedback, and suggestions to help improve this project! Whether you encounter any issues or have ideas for enhancements, feel free to:

- **Open an Issue**: Report bugs, request new features, or suggest improvements by opening an issue in the [Issues tab](https://github.com/your-repo/issues).
  
- **Submit a Pull Request**: If you'd like to contribute code, feel free to submit a pull request. Please ensure your contributions adhere to our coding guidelines and are well-tested.

- **Provide Feedback**: Share your suggestions via text or video! If you have detailed feedback, a video explaining your ideas would be incredibly helpful. This helps us refine the project and improve the experience for everyone.
