# Roguin
One stop shop for Social Network integrations

Use the same code for **Google**, **Facebook** and **Twitter**

### What is Roguin
Social Network integrations can be a pain to write and maintain. This goes double if you want to add more than one Network into a 
single project. With Roguin, the whole process is simplified and **Google**, **Facebook** and **Twitter** are unified and handled
with the same code.

#### You can add **'Sing In With'** feature for all three networks with the same exact code!

## Preparation
Roguin helps with code but you still need valid Applications as per Social Network regulations and guidelines.
So, you would still need to:
- **Configure a Project** for Google through https://developers.google.com/identity/sign-in/android/start-integrating#configure_a_console_name_project
- **Create A New App** for Facebook through https://developers.facebook.com/docs/facebook-login/android
- **Create a New Twitter App** from https://apps.twitter.com

## Installation
#### 1. Dependency
```
implementation 'link coming soon'
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
#### 3. Endpoint initialization
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
        googleEndpoint.requestSignIn { success, result, error ->
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
        facebookEndpoint.requestSignIn { success, result, error ->
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
        twitterEndpoint.requestSignIn { success, result, error ->
            if (success) {
                twitterStatus.text = "Twitter is CONNECTED"
            }
        }
    }
}
```
