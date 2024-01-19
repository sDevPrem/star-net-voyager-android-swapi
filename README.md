# StarNet Voyager

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

|                                                                                                                                            |                                                                                                                                                         |                                                                                                                                            |
|--------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| ![star_net_voyager_home](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/1fa2c774-c822-4384-b70f-d0d8a12b9c52) | ![star_net_voyager_character_filters](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/36c7cdfd-4cd3-485d-9d63-87e08780b534) | ![star_net_voyager_home](https://github.com/sDevPrem/star-net-voyager-android-swapi/assets/130966261/05d390b2-6a62-4af6-aa87-05365ad75ba3) |

## Architecture

This app follows MVVM architecture with Clean architecture, Uni Directional Flow (UDF) pattern
and Single Activity architecture pattern.

### Packages

* [`:data:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/usecase/app/src/main/java/com/example/starnetvoyager/data) -
  The data origin point.  
  |---[`:local:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/data/local) -
  Handles data with a local database (Room).  
  |---[`:network:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/data/network) -
  Handles remote APIs.  
  |---[`:repository:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/data/repository) -
  Decides from where and how the data should come (local or remote).
* [`:di:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/di) -
  Hilt modules
* [`:domain:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/domain) -
  Central nervous system of the app containing the contract between UI and Data layer.
* [`:ui:`](https://github.com/sDevPrem/star-net-voyager-android-swapi/tree/main/app/src/main/java/com/example/starnetvoyager/ui) -
  All the Screens UI lies here in subpackages.

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

## Installation

Simple clone this app and open in Android Studio.
