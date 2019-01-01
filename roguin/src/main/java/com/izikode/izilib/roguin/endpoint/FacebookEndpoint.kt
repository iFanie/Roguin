package com.izikode.izilib.roguin.endpoint

import android.net.Uri
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.RoguinEndpoint
import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.model.RoguinProfile
import com.izikode.izilib.roguin.model.RoguinToken
import android.os.Bundle
import org.json.JSONException
import org.json.JSONObject
import com.facebook.GraphRequest
import com.izikode.izilib.roguin.helper.UserNotSignedInException


class FacebookEndpoint(

        private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val facebookLoginButton = LoginButton(roguinActivity).apply {
        setReadPermissions(arrayListOf("public_profile", "email"))
    }

    override val isSignedIn: Boolean
        get() {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

    override fun requestSignIn(response: (success: Boolean, token: RoguinToken?, error: RoguinException?) -> Unit) {
        CallbackManager.Factory.create().let { callbackManager ->
            facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {
                    facebookLoginButton.removeCallbacks {}
                    roguinActivity.unregisterCallbackManager(callbackManager)

                    if (result != null) {
                        response.invoke(true, result.toToken(), null)
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

    private fun LoginResult.toToken() = RoguinToken(
        endpoint = this@FacebookEndpoint::class,
        authenticatedToken = this.accessToken.token,
        userId = this.accessToken.userId
    )

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

    override fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit) {
        val token = AccessToken.getCurrentAccessToken()

        if (token == null || token.isExpired) {
            response.invoke(false, null, UserNotSignedInException())
        } else {
            GraphRequest.newMeRequest(token) { jsonObject, graphResponse ->
                try {
                    response.invoke(true, jsonObject.toProfile(), null)
                } catch (e: JSONException) {
                    response.invoke(false, null, RoguinException(e))
                }
            }.apply {
                parameters = Bundle().apply {
                    putString("fields", "email, name, picture")
                }
            }.executeAsync()
        }
    }

    private fun JSONObject.toProfile() = RoguinProfile(
        email = this.getString("email"),
        name = this.getString("name"),
        photo = this.getJSONObject("picture")?.getJSONObject("data")?.getString("url")?.let { Uri.parse(it) }
    )

}