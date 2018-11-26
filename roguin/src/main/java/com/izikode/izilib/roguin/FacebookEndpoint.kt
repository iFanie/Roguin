package com.izikode.izilib.roguin

import android.content.Context
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

class FacebookEndpoint(

        private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val facebookLoginButton = LoginButton(roguinActivity)

    override val isSignedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

    override fun requestSignIn(response: (success: Boolean, result: RoguinProfile?, error: RoguinException?) -> Unit) {
        facebookLoginButton.registerCallback(CallbackManager.Factory.create(), object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                facebookLoginButton.removeCallbacks {}

                if (result != null) {
                    response.invoke(true, parseToProfile(result), null)
                } else {
                    response.invoke(false, null, null)
                }
            }

            override fun onCancel() {
                facebookLoginButton.removeCallbacks {}
                response.invoke(false, null, null)
            }

            override fun onError(error: FacebookException?) {
                facebookLoginButton.removeCallbacks {}
                response.invoke(false, null, RoguinException(error))
            }

        })

        facebookLoginButton.performClick()
    }

    private fun parseToProfile(facebookLoginResult: LoginResult) = RoguinProfile().apply {
        /* TODO actually parse */
    }

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {

            if (it.error == null) {
                LoginManager.getInstance().logOut()
                response.invoke(true)
            } else {
                response.invoke(false)
            }

        }).executeAsync()
    }

    companion object {

        @JvmStatic
        fun initialize(applicationContext: Context) {
            FacebookSdk.sdkInitialize(applicationContext)
        }

    }

}