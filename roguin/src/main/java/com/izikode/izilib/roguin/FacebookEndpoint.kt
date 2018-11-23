package com.izikode.izilib.roguin

import com.facebook.AccessToken

class FacebookEndpoint : RoguinEndpoint {

    override val isSignedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

}