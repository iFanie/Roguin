package com.izikode.izilib.roguin

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn

class GoogleEndpoint(private val context: Context) : RoguinEndpoint {

    override val isSignedIn: Boolean
        get() = GoogleSignIn.getLastSignedInAccount(context) != null

}