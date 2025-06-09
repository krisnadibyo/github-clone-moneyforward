# GitHub Clone - Native Android App

A github client apps: Part of MoneyForward Assessment Test

## 🚀 Features

### Core Features

- **User List & Search**
    - Browse GitHub users
    - Search users with real-time results
    - Infinite scrolling pagination

- **User Profile**
    - Detailed user information
    - Repository list with infinite scrolling

- **Repository Details**
    - Open in Web view

### Additional

- Dark Mode support
- Unit Testing

## 🛠 Technical Stack

### Language & Framework

- **Kotlin** - Modern, concise, and safe programming language
- **Material3** - Latest Material Design components
- **Jetpack Compose** - Modern Android UI toolkit
- **Navigation Compose** - Type-safe navigation

### Dependency Libraries

- **coin** - Dependency Injection
- **ktor** - Http Client Library
- **coil** - Image Loader

### Architecture

- **MVI (Model-View-Intent)**
    - Unidirectional data flow
    - State UI → View → Action (Intent) → Event (Effect)
    - Clean Architecture, testable architecture: Data, Domain, and Presentation Layer
    - Modularized ready

### Unit Tests

- ViewModel and util tests
- Test Doubles: Fake Class

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 11 or newer
- Android SDK 33 or newer

### Installation

1. Clone the repository

```bash
git clone https://github.com/krisnadibyo/github-clone-moneyforward.git
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the application

5. [Optional] Setup github token to prevent API rate Limit

6. Open `local.properties` File

7. Add `API_KEY=<Your_API_TOKEN>`

8. read this to get your
   PAT [HERE](https://docs.github.com/en/rest/authentication/authenticating-to-the-rest-api?apiVersion=2022-11-28#authenticating-with-a-personal-access-token)

9. Rerun the application