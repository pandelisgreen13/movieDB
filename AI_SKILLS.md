# Project Skills & Architectural Reference

This document outlines the technical stack, architectural patterns, and coding standards used in the **movieDb** project. It serves as a reference for AI agents and developers to maintain consistency and leverage existing patterns.

## 🏛️ Architecture & Patterns
- **Clean Architecture**: Use of Interactors (Use Cases) to encapsulate business logic.
- **MVVM (Model-View-ViewModel)**: Unidirectional data flow using `StateFlow` and lifecycle-aware components.
- **Data Mapping**: Entities from Network and Database layers are explicitly mapped to Domain/UI models (e.g., `HomeDataModelMapper`).
- **Dependency Injection**: Managed by **Hilt**. Modules are located in `gr.pchasapis.moviedb.di`.

## 🎨 UI Layer (Jetpack Compose)
- **Declarative UI**: Built with Jetpack Compose and Material 3.
- **State Management**: State is hoisted to ViewModels and observed via `collectAsStateWithLifecycle()`.
- **Navigation**: Uses **Navigation Compose** with type-safe arguments.
- **Pagination**: Lists are managed using **Paging 3**, often integrated with **RemoteMediator** for offline caching.

## ⚙️ Technical Stack
- **Languages**: 100% Kotlin with Coroutines and Flow.
- **Networking**: Retrofit 2 + OkHttp + Kotlinx Serialization.
- **Database**: Room Persistence Library with KSP.
- **Image Loading**: Coil (Compose) and Picasso (Legacy).
- **Dependency Injection**: Hilt (Dagger).
- **Static Analysis**: Detekt with custom rules for Compose.

## 📦 Data Flow
1. **Compose Screen** triggers an event in the **ViewModel**.
2. **ViewModel** calls a method in the **Interactor**.
3. **Interactor** fetches data from **MovieClient** (Network) or **Room** (Local).
4. **Interactor** uses a **Mapper** to transform the data into a UI model.
5. **ViewModel** updates the `UiState` (StateFlow).
6. **Compose Screen** recomposes based on the new state.

## 🛠️ Development Standards
- Use `build.gradle.kts` with Version Catalogs (`libs.versions.toml`).
- Maintain high test coverage using **JUnit**, **Mockito**, and **Robolectric**.
- Follow Detekt linting rules.
- Use **Timber** for all logging.
