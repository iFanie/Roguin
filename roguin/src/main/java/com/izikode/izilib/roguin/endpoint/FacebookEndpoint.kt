package com.izikode.izilib.roguin.endpoint

import android.content.Context
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.RoguinEndpoint
import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.model.RoguinToken

class FacebookEndpoint(

        private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val facebookLoginButton = LoginButton(roguinActivity)

    override val isSignedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

    override fun requestSignIn(response: (success: Boolean, result: RoguinToken?, error: RoguinException?) -> Unit) {
        CallbackManager.Factory.create().let { callbackManager ->
            facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {
                    facebookLoginButton.removeCallbacks {}
                    roguinActivity.unregisterCallbackManager(callbackManager)

                    if (result != null) {
                        response.invoke(true, parseToProfile(result), null)
                    } else {
                        response.invoke(false, null, null)
                    }
                }

                override fun onCancel() {
                    facebookLoginButton.removeCallbacks {}
                    roguinActivity.unregisterCallbackManager(callbackManager)

                    response.invoke(false, null, null)
                }

                override fun onError(error: FacebookException?) {
                    facebookLoginButton.removeCallbacks {}
                    roguinActivity.unregisterCallbackManager(callbackManager)

                    response.invoke(false, null, RoguinException(error))
                }

            })

            roguinActivity.registerCallbackManager(callbackManager)
            facebookLoginButton.performClick()
        }
    }

    private fun parseToProfile(facebookLoginResult: LoginResult) = RoguinToken().apply {
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