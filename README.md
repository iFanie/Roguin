# Roguin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Bintray](https://img.shields.io/badge/Bintray-0.2-lightgrey.svg)](https://dl.bintray.com/ifanie/izilib/com/izikode/izilib/roguin/0.2/)

One stop shop for Social Network integrations

Use the same code for **Google**, **Facebook** and **Twitter**

### What is Roguin
Social Network integrations can be a pain to write and maintain. This goes double if you want to add more than one Network into a 
single project. With Roguin, the whole process is simplified and **Google**, **Facebook** and **Twitter** are unified and handled
with the same code.

#### You can add a **'Sing In With'** feature for all three networks with the same exact code!

## Preparation
Roguin helps with code but you still need valid Applications as per Social Network regulations and guidelines.
So, you would still need to:
- **Configure a Project** for Google through https://developers.google.com/identity/sign-in/android/start-integrating#configure_a_console_name_project
- **Create A New App** for Facebook through https://developers.facebook.com/docs/facebook-login/android
- **Create a New Twitter App** from https://apps.twitter.com

## Installation
#### 1. Dependency
```
implementation 'com.izikode.izilib:roguin:0.2'
```

#### 2. Social Network Application keys
You must add your valid App key and secrets in Manifest placeholders, for both Facebook and Twitter. Google does not require this. So in your app's build.gradle file, add the following:
```groovy
android {
    defaultConfig {
        manifestPlaceholders = [
                facebook_api_key   : 'my valid facebook app key',
                twitter_api_key    : 'my valid twitter app key',
                twitter_api_secret : 'my valid twitter app secret'
        ]
    }
}
```

#### 3. Extend RoguinActivity
Every social network SDK uses the ```startActivityForResult``` in one way or another. In the spirit of making stuff as easy as possible, Roguin provides a Base Activity class, ```RoguinActivity``` which handles registering, unregistering, request codes and the lot. Just extend in your Activities and you are done.

#### 4. Endpoint initialization
Prior to using, the Endpoints you need must be initialized. A good place to do this is in the ```onCreate``` of your app's Application class. And you can do so like this:
```kotlin
override fun onCreate() {
    super.onCreate()

    FacebookEndpoint.initialize(this)
    TwitterEndpoint.initialize(this)
}
```

## Usage
All three wrappers, GoogleEndpoint, FacebookEndpoint and TwitterEndpoint, implement the ```RoguinEndpoint``` interface. So they all expose the following:
- ```val isSignedIn``` for checking the **Sign In** status for your app.
- ```fun requestSignIn``` for starting a new **Sing In** flow in your app.
- ```fun requestSignOut``` for starting a new **Sign Out** flow in your app.

The following code is an outtake from the demo project in this repository and shows how simple signing in and out of all three networks is with Roguin.
```kotlin
googleButton.setOnClickListener {
    if (googleEndpoint.isSignedIn) {
        googleEndpoint.requestSignOut { success ->
            if (success) {
                googleStatus.text = "Google is DISCONNECTED"
            }
        }
    } else {
        googleEndpoint.requestSignIn { success, token, error ->
            if (success) {
                googleStatus.text = "Google is CONNECTED"
            }
        }
    }
}

facebookButton.setOnClickListener {
    if (facebookEndpoint.isSignedIn) {
        facebookEndpoint.requestSignOut { success ->
            if (success) {
                facebookStatus.text = "Facebook is DISCONNECTED"
            }
        }
    } else {
        facebookEndpoint.requestSignIn { success, token, error ->
              if (success) {
                facebookStatus.text = "Facebook is CONNECTED"
            }
        }
    }
}

twitterButton.setOnClickListener {
    if (twitterEndpoint.isSignedIn) {
        twitterEndpoint.requestSignOut { success ->
            if (success) {
                twitterStatus.text = "Twitter is DISCONNECTED"
            }
        }
    } else {
        twitterEndpoint.requestSignIn { success, token, error ->
            if (success) {
                twitterStatus.text = "Twitter is CONNECTED"
            }
        }
    }
}
```

#### For a full example, see the sample app.

## Licence
```
Copyright 2018 Fanie Veizis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
