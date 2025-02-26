# OG Star Tracker App
[![minSdk 26](https://img.shields.io/badge/minSdk-26-brightgreen.svg)]()
[![targetsdk 34](https://img.shields.io/badge/targetSdk-34-brightgreen.svg)]()
[![Kotlin 1.9.22](https://img.shields.io/badge/Kotlin-1.9.22-brightgreen.svg)]()

## Overview
The OG Star Tracker app is designed to assist astrophotographers in setting up their equipment and capturing the night sky with precision and ease. It provides a checklist to ensure all necessary steps are taken for a successful astrophotography session, along with features to control and monitor the photography process.

## Features

- **Checklist**: A comprehensive list of steps to prepare your equipment and camera settings.
- **WiFi Connection Checker**: Automatically checks if the device is connected to the correct WiFi network for remote control capabilities.
- **Sidereal Tracking**: Enables sidereal tracking to follow the stars' movement across the sky.
- **Photo Control Inputs Validation**: Validates the necessary inputs for capturing photos, including exposure time and frame count.
- **Dithering Support**: Offers dithering options to improve image quality by reducing noise.
- **Capture Management**: Tracks the capture start time, count, elapsed time, and estimated time for the photography session.

## Technologies
* App is completely written in ***Kotlin***
* App is completely reactive (we use ***coroutines*** and ***flow***)
* For API communication, this app uses ***REST API***, specifically Retrofit and OkHttp3 SDK libraries for Android
* App uses ***Clean architecture***
* App UI is written in ***Jetpack Compose***

## Used libraries
* **Jetpack Compose** - ui library
* **Kotlin Corountines** - flows, corountines, ...
* **Koin** - dependency injection / service provider
* **Retrofit** - network library
* **Moshi** - parsing json
* **Timber** - logging

## Build setup

To prepare your environment to build the app locally you can use your IDE of choice. This guide walks you through the building of the app with the Visual Studio Code IDE.
Extensions you need:
- [Gradle for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-gradle)
- [Kotlin Language](https://marketplace.visualstudio.com/items?itemName=mathiasfrohlich.Kotlin)

What you need now is the android sdk. For that we are not required to use the android studio at all and only require the cli tools.
- Download the [cli-tools for windows](https://developer.android.com/studio?hl=de#command-line-tools-only)
- Extract them into **android/cmdline-tools/tools**
- Install the dependencies and the sdk with the sdkmanager
```shell
OG-star-tracker-App\android\cmdline-tools\tools\bin> .\sdkmanager.bat "platform-tools"
OG-star-tracker-App\android\cmdline-tools\tools\bin> .\sdkmanager.bat "platforms;android-34"
OG-star-tracker-App\android\cmdline-tools\tools\bin> .\sdkmanager.bat "build-tools;34.0.0"
```
- Configure the **$ANDROID_HOME** variable in your PATH or alternatively set the **sdk.dir=android** in your **local.properties**.
- Now reload the window and and let the gradle extension configure the project.
- Once the **OG Star Tracker** project is configured you can check the **Tasks/other** directive and execute the **zipApksForDevDebug** to finally build the App.


## Contributing to the OG Star Tracker App

We welcome contributions from the community! If you're looking to contribute to the OG Star Tracker app, here's how you can do so:

1. **Fork the Repository**
    - Start by forking the repository to your GitHub account. This creates a copy of the repository that you can work on independently.

2. **Clone Your Fork**
    - Clone your forked repository to your local machine to start making changes. Use the following command:
      ```bash
      git clone https://github.com/OndraGejdos/OG-star-tracker-
      ```
    - Replace `yourusername` with your GitHub username.

3. **Create a New Branch**
    - Navigate into the cloned repository and create a new branch for your contributions. It's best practice to name your branch related to the changes you're making. For example:
      ```bash
      git checkout -b feature-add-new-checklist-item
      ```

4. **Make Your Changes**
    - With your new branch checked out, make the changes you'd like to contribute. This could be anything from fixing a bug, adding a new feature, or improving documentation.

5. **Commit Your Changes**
    - After making your changes, commit them to your branch. Be sure to write a clear and concise commit message that explains what you've done:
      ```bash
      git commit -am "Add new checklist item for astrophotography setup"
      ```

6. **Push Your Changes**
    - Push your changes to your fork on GitHub:
      ```bash
      git push origin feature-add-new-checklist-item
      ```

7. **Create a Pull Request**
    - Go to the original OG Star Tracker repository on GitHub, and you'll see a prompt to create a pull request from your newly pushed branch. Click on "Compare & pull request" and describe the changes you've made, why you've made them, and any other relevant information.

8. **Review & Merge**
    - Once your pull request is submitted, the project maintainers will review your changes. If everything looks good, they will merge your changes into the main project. You might also receive feedback on your pull request; be open to making further adjustments.

By following these steps, you can contribute to the development and improvement of the OG Star Tracker app. We look forward to your contributions!