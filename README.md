# BiSU Android Developer Challange - Burak Fidan

## Table of Contents
* **[Basic Info](#basic-info)**
* **[3rd Party Libraries](#3rd-party-libraries)**
* Extras
* * **[Dark Theme](#dark-theme)**
* * **[Real Time Favourite Tracking and Multiple Country Support](#real-time-favourite-tracking-and-multiple-country-support)**
* **[Important Notes](#important-notes)**

## Basic Info

* Language: Kotlin
* Software Architecture: MVVM
* News API: newsapi.org
* Dependency Injection: Dagger Hilt

## 3rd Party Libraries:

* Firebase
* Retrofit
* OkHttp
* Dagger Hilt
* Glide (Image Loading)
* Coroutines
* Jetpack Navigation
* Paging 3
* Leak Canary (Memory Leak Detection)

### Dark Theme

https://user-images.githubusercontent.com/25686023/192276708-577f9e75-eab5-489a-98a9-086fed0585e5.mp4

### Real Time Favourite Tracking and Multiple Country Support

https://user-images.githubusercontent.com/25686023/192276870-9f14b281-3554-4463-8085-c6e56872754f.mp4

## Important Notes
* There is a delay on the [News Api Request](https://github.com/MrNtlu/BiSU-Task/blob/5e3b6ebc62485e9e14597c3102aee881240626f4/app/src/main/java/com/mrntlu/bisu/models/NewsPagingSource.kt#L29) to show "Loading Screen" longer and better.

* I've used the Jetpack Compose for News Detail Screen. [Composable File](https://github.com/MrNtlu/BiSU-Task/blob/5e3b6ebc62485e9e14597c3102aee881240626f4/app/src/main/java/com/mrntlu/bisu/ui/composeable/NewsDetail.kt)

* For some reason on the latest version of the Android Emulator, Firebase doesn't work, it cannot connect to the servers. I had to downgrade to version [31.2.9](https://developer.android.com/studio/emulator_archive). I also tested on Physical device and there wasn't any issue. If you encounter any problems please let me know.

* google-services.json file is hidden. It will be provided externally.