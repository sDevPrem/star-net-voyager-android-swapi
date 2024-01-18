# StarNet Voyager

RunTrack is a Fitness Tracking app utilizing modern Android technologies, including
Jetpack Compose, MVVM architecture, and Google Maps API. The app allows users to
track their running activities, displaying real-time routes on an interactive map
while storing essential statistics using Room database.

In StarNet Voyager, you can explore the characters of Star Wars and movies performed by them.
It is an Android client implementation of [SWAPI](https://swapi.dev/documentation)
API using MVVM and clean architecture and uses Room database as a single source of truth.

## Features

1. Explore Star Wars Characters and their movies.
2. Single Activity Architecture and uses Jetpack navigation to navigate
   through destination (fragments).
3. Retrofit to do api calls and gson to parse response.
4. Paging3 to load only required Characters and Movies.
5. Room database to cache Characters and Movies data.
6. Hilt for dependency injection.

## Screenshot

|                                                                                                                                            |                                                                                                                                                         |                                                                                                                                             |
|--------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| ![star_net_voyager_home](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/05d390b2-6a62-4af6-aa87-05365ad75ba3) | ![star_net_voyager_character_filters](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/36c7cdfd-4cd3-485d-9d63-87e08780b534) | ![star_net_voyager_movie](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/1fa2c774-c822-4384-b70f-d0d8a12b9c52) |

## Build With

[Kotlin](https://kotlinlang.org/):
As the programming language.

[Jetpack Navigation](https://developer.android.com/guide/navigation) :
Caption of the app which will take you to new areas ensuring safe delivery of your data.

[Room](https://developer.android.com/jetpack/androidx/releases/room) :
Rest room for the Jedi.

[Hilt](https://developer.android.com/training/dependency-injection/hilt-android) :
Factory and supplier for the ingredients required to cook a feature.

[Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview):
Make the app feel like reading a comic.

[Retrofit](https://square.github.io/retrofit):
Gets the Star Warriors from stars to your world.

[Coroutines](https://developer.android.com/kotlin/coroutines):
Mission Coordinator ensuring co-working.

## Architecture

This app follows MVVM architecture with Clean architecture, Uni Directional Flow (UDF) pattern
and Single Activity architecture pattern.

## Installation

Simple clone this app and open in Android Studio.