package com.izikode.izilib.roguin.endpoint

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.RoguinEndpoint
import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.helper.UserNotSignedInException
import com.izikode.izilib.roguin.model.RoguinProfile
import com.izikode.izilib.roguin.model.RoguinToken
import android.content.pm.PackageManager
import android.util.Log

class GoogleEndpoint(

        private val roguinActivity: RoguinActivity

) : RoguinEndpoint {
    private var myApiKey: String? = null
    private val googleClient by lazy {
            
        try {
            val ai = nsSocialActivity.packageManager.getApplicationInfo(nsSocialActivity.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            myApiKey = bundle.getString("google_server_client_id")
        } catch (e: Exception) {
            Log.e(
                "RoguinActivity",
                "Dear developer. Don't forget to configure <meta-data android:name=\"google_server_client_id\" android:value=\"testValue\"/> in your AndroidManifest.xml file."
            )
        }
            
        val options = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(myApiKey)
            .requestProfile()
            .requestEmail()
            .build()

        GoogleSignIn.getClient(roguinActivity, options)
    }

    override val isSignedIn: Boolean
        get() = GoogleSignIn.getLastSignedInAccount(roguinActivity) != null

    override fun requestSignIn(response: (success: Boolean, token: RoguinToken?, error: RoguinException?) -> Unit) {
        roguinActivity.requestResult(googleClient.signInIntent) { success, result ->
            if (!success) {
                response.invoke(false, null, RoguinException(null, result))
            } else {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result)

                try {
                    val taskResult = task.getResult(ApiException::class.java)
                    response.invoke(true, taskResult.toToken(), null)
                } catch (googleApiException: ApiException) {
                    response.invoke(false, null, RoguinException(googleApiException, result))
                }
            }
        }
    }

    private fun GoogleSignInAccount.toToken() = RoguinToken(
        endpoint = this@GoogleEndpoint::class,
        authenticatedToken = this.idToken ?: "",
        userId = this.id ?: ""
    )

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        googleClient.signOut().addOnCompleteListener {
            response.invoke(it.isSuccessful)
        }
    }

    override fun requestProfile(response: (success: Boolean, profile: RoguinProfile?, error: RoguinException?) -> Unit) {
        val lastAccount = GoogleSignIn.getLastSignedInAccount(roguinActivity)

        if (lastAccount != null) {
            response.invoke(true, lastAccount.toProfile(), null)
        } else {
            response.invoke(false, null, UserNotSignedInException())
        }
    }

    private fun GoogleSignInAccount.toProfile() = RoguinProfile(
        email = this.email,
        name = this.displayName,
        photo = this.photoUrl
    )

    companion object {

        @JvmStatic
        fun initialize(applicationContext: Context) {
            /* Reserved for upcoming functionality */
        }

    }

}
