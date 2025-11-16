# Weather App

This is a weather application for Android, designed to provide real-time weather information using the [OpenWeather API](https://openweathermap.org/current). The app automatically detects the user's current location to deliver instant weather updates. Users also have the ability to search for and save other locations to easily access their weather forecasts. To optimize performance and minimize unnecessary API calls, the application uses a JSON document from OpenWeather to populate a local SQL database (Room) with all supported locations.

The application is built entirely with modern Android development practices, following the MVVM (Model-View-ViewModel) architecture. Its user interface is crafted with Jetpack Compose, and it leverages Kotlin Coroutines for managing background tasks and asynchronous data streams. Dependency injection is handled by Hilt, while local data persistence (such as saved locations) is managed through Room. Network requests to the weather data provider are made using Retrofit.

## Key Technologies

*   **Jetpack Compose:** The entire UI is built using Jetpack Compose, a modern toolkit for building native Android UI.
*   **Kotlin:** The app is written entirely in Kotlin.
*   **Coroutines and Flow:** Used for asynchronous operations and managing data streams.
*   **Hilt:** For dependency injection.
*   **Room:** For local data storage.
*   **Retrofit:** For making network requests to a weather API.
*   **Coil:** For image loading.
*   **Jetpack Navigation:** For navigating between screens.
*   **Material 3:** The app uses the Material 3 design system.
*   **Accompanist Permissions:** To simplify handling of runtime permissions.
*   **Google Play Services Location:** To get the device's location for weather data.

## Screenshots

![Screenshot 1](screenshots/screenshot1.jpeg)
![Screenshot 2](screenshots/screenshot2.jpeg)
![Screenshot 3](screenshots/screenshot3.jpeg)
![Screenshot 4](screenshots/screenshot4.jpeg)
![Screenshot 5](screenshots/screenshot5.jpeg)
![Screenshot 6](screenshots/screenshot6.jpeg)
![Screenshot 7](screenshots/screenshot7.jpeg)
