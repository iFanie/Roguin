package com.izikode.izilib.roguin

import com.twitter.sdk.android.core.TwitterCore

class TwitterEndpoint : RoguinEndpoint {

    override val isSignedIn: Boolean
        get() = TwitterCore.getInstance().sessionManager.activeSession != null

}