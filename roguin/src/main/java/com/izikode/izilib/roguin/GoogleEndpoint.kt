package com.izikode.izilib.roguin

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleEndpoint(

        private val roguinActivity: RoguinActivity

) : RoguinEndpoint {

    private val googleClient by lazy {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(roguinActivity, options)
    }

    override val isSignedIn: Boolean
        get() = GoogleSignIn.getLastSignedInAccount(roguinActivity) != null

    override fun requestSignIn(response: (success: Boolean, result: RoguinProfile?, error: RoguinException?) -> Unit) {
        roguinActivity.requestResult(googleClient.signInIntent) { success, result ->
            if (!success) {
                response.invoke(false, null, RoguinException(null, result))
            } else {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result)

                try {
                    val profile = task.getResult(ApiException::class.java)

                    response.invoke(true, parseToProfile(profile), null)
                } catch (googleApiException: ApiException) {
                    response.invoke(false, null, RoguinException(googleApiException, result))
                }
            }
        }
    }

    private fun parseToProfile(googleSignInAccount: GoogleSignInAccount) = RoguinProfile().apply {
        /* TODO actually parse */
    }

    override fun requestSignOut(response: (success: Boolean) -> Unit) {
        googleClient.signOut().addOnCompleteListener {
            response.invoke(it.isSuccessful)
        }
    }

}