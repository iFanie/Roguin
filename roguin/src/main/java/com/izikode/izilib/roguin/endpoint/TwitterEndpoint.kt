package com.izikode.izilib.roguin.endpoint

import android.content.Context
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import android.content.pm.PackageManager
import android.net.Uri
import com.izikode.izilib.roguin.BuildConfig
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.RoguinEndpoint
import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.helper.UserNotSignedInException
import com.izikode.izilib.roguin.model.RoguinProfile
import com.izikode.izilib.roguin.model.RoguinToken
import com.twitter.sdk.android.core.models.User

class TwitterEndpoint(

    private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val twitterLoginButton = TwitterLoginButton(roguinActivity)

    override val isSignedIn: Boolean
        get() = TwitterCore.getInstance().sessionManager.activeSession != null

    override fun requestSignIn(response: (success: Boolean, result: RoguinToken?, error: RoguinException?) -> Unit) {
        twitterLoginButton.callback = object : Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>?) {
                twitterLoginButton.removeCallbacks {}
                roguinActivity.unregisterLoginButton(twitterLoginButton)

                if (result != null) {
                    response.invoke(true, result.toToken(), null)
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

    private fun Result<TwitterSession>.toToken() = RoguinToken(
        endpoint = this@TwitterEndpoint::class,
        authenticatedToken = this.data.authToken.token,
        userId = this.data.userId.toString()
    )

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        response.invoke(true)
    }

    override fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit) {
        val session = TwitterCore.getInstance().sessionManager.activeSession

        if (session == null) {
            response.invoke(false, null, UserNotSignedInException())
        } else {
            TwitterCore.getInstance().getApiClient(session).accountService
                .verifyCredentials(true, true, false)
                .enqueue(object : Callback<User>() {

                    override fun success(result: Result<User>) {
                        response.invoke(true, result.data.toProfile(), null)
                    }

                    override fun failure(e: TwitterException) {
                        response.invoke(false, null, RoguinException(e))
                    }

                })
        }
    }

    private fun User.toProfile() = RoguinProfile(
        email = this.email,
        name = this.name,
        photo = this.profileImageUrl?.replace("_normal", "")?.let { Uri.parse(it) }
    )

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