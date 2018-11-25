package com.izikode.izilib.roguin

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class TwitterEndpoint(

    private val twitterLoginButton: TwitterLoginButton

) : RoguinEndpoint {

    override val isSignedIn: Boolean
        get() = TwitterCore.getInstance().sessionManager.activeSession != null

    override fun requestSignIn(response: (success: Boolean, result: RoguinProfile?, error: RoguinException?) -> Unit) {
        twitterLoginButton.callback = object : Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>?) {
                if (result != null) {
                    response.invoke(true, parseToProfile(result), null)
                } else {
                    response.invoke(false, null, null)
                }
            }

            override fun failure(exception: TwitterException?) {
                response.invoke(false, null, RoguinException(exception))
            }

        }

        twitterLoginButton.performClick()
    }

    private fun parseToProfile(twitterLoginResult: Result<TwitterSession>) = RoguinProfile().apply {
        /* TODO actually parse */
    }

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        response.invoke(true)
    }

}