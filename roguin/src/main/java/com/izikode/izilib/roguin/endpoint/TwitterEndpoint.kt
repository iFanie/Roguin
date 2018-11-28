package com.izikode.izilib.roguin.endpoint

import android.content.Context
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import android.content.pm.PackageManager
import com.izikode.izilib.roguin.BuildConfig
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.RoguinEndpoint
import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.model.RoguinProfile

class TwitterEndpoint(

    private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val twitterLoginButton = TwitterLoginButton(roguinActivity)

    override val isSignedIn: Boolean
        get() = TwitterCore.getInstance().sessionManager.activeSession != null

    override fun requestSignIn(response: (success: Boolean, result: RoguinProfile?, error: RoguinException?) -> Unit) {
        twitterLoginButton.callback = object : Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>?) {
                twitterLoginButton.removeCallbacks {}
                roguinActivity.unregisterLoginButton(twitterLoginButton)

                if (result != null) {
                    response.invoke(true, parseToProfile(result), null)
                } else {
                    response.invoke(false, null, null)
                }
            }

            override fun failure(exception: TwitterException?) {
                twitterLoginButton.removeCallbacks {}
                roguinActivity.unregisterLoginButton(twitterLoginButton)

                response.invoke(false, null, RoguinException(exception))
            }

        }

        roguinActivity.registerLoginButton(twitterLoginButton)
        twitterLoginButton.performClick()
    }

    private fun parseToProfile(twitterLoginResult: Result<TwitterSession>) = RoguinProfile().apply {
        /* TODO actually parse */
    }

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        response.invoke(true)
    }

    companion object {

        @JvmStatic
        fun initialize(applicationContext: Context) {
            val app = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
            val metaData = app.metaData

            val twitterConfig = TwitterConfig.Builder(applicationContext).apply {
                twitterAuthConfig(TwitterAuthConfig(
                    metaData.getString("com.twitter.sdk.ApplicationKey"),
                    metaData.getString("com.twitter.sdk.ApplicationSecret")
                ))

                if (BuildConfig.DEBUG) {
                    logger(DefaultLogger(Log.DEBUG))
                    debug(true)
                }
            }.build()

            Twitter.initialize(twitterConfig)
        }

    }

}