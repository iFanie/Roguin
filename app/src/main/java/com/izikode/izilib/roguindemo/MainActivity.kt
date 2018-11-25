package com.izikode.izilib.roguindemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.facebook.login.widget.LoginButton
import com.izikode.izilib.roguin.FacebookEndpoint
import com.izikode.izilib.roguin.GoogleEndpoint
import com.izikode.izilib.roguin.RoguinActivity
import com.izikode.izilib.roguin.TwitterEndpoint
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class MainActivity : RoguinActivity() {

    private val googleEndpoint by lazy { GoogleEndpoint(this) }
    private val facebookEndpoint by lazy { FacebookEndpoint(facebookButton) }
    private val twitterEndpoint by lazy { TwitterEndpoint(twitterButton) }

    private val googleStatus by lazy { findViewById<TextView>(R.id.googleStatus) }
    private val facebookStatus by lazy { findViewById<TextView>(R.id.facebookStatus) }
    private val twitterStatus by lazy { findViewById<TextView>(R.id.twitterStatus) }

    private val googleButton by lazy { findViewById<Button>(R.id.googleStatus) }
    private val facebookButton by lazy { findViewById<LoginButton>(R.id.facebookButton) }
    private val twitterButton by lazy { findViewById<TwitterLoginButton>(R.id.twitterButton) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleStatus.text = "Google is " + if (googleEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"
        facebookStatus.text = "Facebook is " + if (facebookEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"
        twitterStatus.text = "Twitter is " + if (twitterEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"
    }

}
