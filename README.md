# GitHub User Search

Android app to browse and search GitHub users and view user details.
Built for the HCS-IDN Android Assessment by Eko Budiarto.

## Features
- **User List Screen**: shows GitHub users (`GET /users`) with username and avatar.
- **Search Screen**: search GitHub users by username (`GET /search/users`). Results are shown in a RecyclerView.
- **User Detail Screen**: avatar, name, username, bio, company, location, blog, repositories, followers and following.
- **Local persistence**: users and user details are cached in Room and shown when the network is unavailable.

## Tech Stack
- Kotlin, XML layout with ViewBinding
- MVVM + Clean Architecture (presentation, domain, data)
- Hilt for dependency injection
- Kotlin Coroutines, Flow, ViewModel, LiveData
- Retrofit, OkHttp, Gson
- Room
- Glide
- Chucker
- Gradle Version Catalog

## Requirements
- Android Studio (latest stable)
- JDK 17
- Android SDK 37

## Build & Run
1. Clone the repository:
   ```
   git clone https://github.com/EkoBudi14/AndroidAssestment.git
   ```
2. Open the project in Android Studio and wait for Gradle sync to finish.
3. Run the `app` configuration on an emulator or device.

Build from the command line:
```
./gradlew assembleDebug
```

## Architecture
The app uses **MVVM with Clean Architecture**, split into three layers:

- **Presentation**: Activities, ViewModels and UI state classes. ViewModels expose `LiveData` and run work in `viewModelScope`.
- **Domain**: plain Kotlin models, the `UserRepository` and `NetworkMonitor` contracts, and use cases. No Android or library dependencies.
- **Data**: Retrofit API, Room database, mappers, `UserRepositoryImpl` and `ConnectivityNetworkMonitor`.

Dependency direction: `presentation -> domain <- data`. Hilt binds the implementations to the domain contracts.

**Why MVVM + Clean Architecture**
- ViewModel and LiveData are lifecycle-aware, so UI state survives configuration changes.
- The domain layer does not depend on frameworks, so business logic is easy to test.
- Each layer has one responsibility, so parts can be replaced (for example Gson to Moshi) without touching other layers.

**Modular code**
The code is modularized by layer and feature using packages (`data`, `domain`, `presentation`, `di`) in a single Gradle module, which fits the size of this app. Shared UI code such as `UserAdapter` lives in `presentation/common` and is reused by the User List and Search screens.

### Caching strategy
- **All screens**: network first. The app checks connectivity before calling the API. When offline, cached data from Room is shown right away. When online, the result is saved to Room, and cached data is used if the request fails.
- Users and user details are stored in separate tables, because list and search responses do not contain detail fields. A single table would overwrite saved details with empty values.

### Project structure
```
data/          remote (Retrofit), local (Room), network monitor, mapper, repository implementation
domain/        models, repository and network monitor contracts, use cases
di/            Hilt modules
presentation/  user list, search and detail screens, shared UI helpers
```

## Nice to Have
- [x] Debugging with Chucker (no-op variant in release build)
- [x] Dependency versioning with Version Catalog
- [ ] Unit test and UI test
- [ ] JSON parsing with Moshi

## Additional Features
- Separate User List and Search screens. Search is opened from the toolbar.
- Live offline banner based on device connectivity (`ConnectivityManager` with a validated internet check).
- Banner that tells the user when the screen shows saved data because the latest data could not be loaded.
- Retry button on error states, so the user can recover without restarting the app.
- Specific error messages for no connection, rate limit, user not found and invalid query.

## Challenges
- **GitHub API rate limit.** Unauthenticated requests ran out quickly during testing. I added an optional token read from `local.properties` into `BuildConfig` and a clear rate limit message in the UI.
- **Offline banner showed while online.** The first version assumed every failed request meant the device was offline, but a rate limit error also falls back to cached data. I added a `NetworkMonitor` that checks real connectivity, and separated the "offline" message from the "showing saved data" message.
- **Offline state not detected on an open screen.** In `onLost`, the lost network could still be reported as active for a moment. The monitor now reports offline immediately when the default network is lost.
- **Slow fallback on a bad network.** Network-first loading waited for the OkHttp timeout before showing cached data. The repository now checks connectivity with `NetworkMonitor` before calling the API, and timeouts were reduced to 15 seconds.

## Future Improvements
- Pagination: the user list and search results load the first 50 items. Next step would be infinite scroll using `since` for `/users` and `page` for `/search/users`, or Paging 3 with a RemoteMediator.
- Unit tests for the repository, use cases and ViewModels, and Espresso UI tests.
- JSON parsing with Moshi.