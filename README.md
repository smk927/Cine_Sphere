# CineSphere

CineSphere is a modern Android application that allows users to explore movies, web series, and anime. It leverages popular APIs to fetch trending content, search for media, and find nearby cinemas.

## Features

*   **Discover Media:** Browse popular movies, web series, and anime.
*   **Search:** Search for movies, TV shows, and people.
*   **Media Details:** View detailed information including overview, cast, and watch providers (OTT platforms).
*   **Nearby Cinemas:** Find movie theaters near your current location using Google Maps integration.
*   **Wishlist:** Save your favorite movies to a local wishlist.
*   **Trivia:** (Feature in development) Participate in movie trivia.
*   **Authentication:** User authentication via Firebase.

## Technologies Used

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles
*   **Dependency Injection:** Hilt
*   **Networking:** Retrofit & OkHttp
*   **Image Loading:** Coil
*   **Local Database:** Room
*   **Asynchronous Programming:** Coroutines & Flow
*   **Maps:** Google Maps SDK for Android
*   **Backend/Services:** Firebase (Auth, Firestore, Analytics)
*   **Media Playback:** Media3 (ExoPlayer)

## Setup & Configuration

This project requires API keys to function correctly. You will need to obtain keys from the following services:

1.  **TMDB (The Movie Database):** For movie and TV show data.
2.  **Google Maps Platform:** For the nearby cinemas feature (Places API, Maps SDK for Android).
3.  **Firebase:** For authentication and other services.

### 1. TMDB API Key

*   Sign up at [The Movie Database](https://www.themoviedb.org/).
*   Go to your account settings and generate an API Key.
*   Open `app/build.gradle.kts`.
*   Locate the `buildConfigField` for `API_KEY` inside the `defaultConfig` block.
*   Replace `"YOUR_API_KEY"` with your actual TMDB API key.

```kotlin
buildConfigField("String", "API_KEY", "\"YOUR_TMDB_API_KEY_HERE\"")
```

### 2. Google Maps API Key

*   Go to the [Google Cloud Console](https://console.cloud.google.com/).
*   Create a project and enable the **Maps SDK for Android** and **Places API**.
*   Generate an API Key.
*   Open `app/src/main/AndroidManifest.xml`.
*   Locate the `meta-data` tag with `android:name="com.google.android.geo.API_KEY"`.
*   Replace `PLACE_YOUR_GOOGLE_API_KEY_HERE` with your actual API key.

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
```

### 3. Firebase Configuration

*   Go to the [Firebase Console](https://console.firebase.google.com/).
*   Create a new project and add an Android app with the package name `com.example.cinesphere`.
*   Download the `google-services.json` file.
*   Place the `google-services.json` file in the `app/` directory of the project.

## Building and Running

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Sync the project with Gradle files.
4.  Ensure you have added your API keys as described above.
5.  Run the application on an emulator or a physical device.

## License

[Add License Here, e.g., MIT, Apache 2.0]
