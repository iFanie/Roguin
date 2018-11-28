package com.izikode.izilib.roguindemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.izikode.izilib.roguin.endpoint.FacebookEndpoint
import com.izikode.izilib.roguin.endpoint.GoogleEndpoint
import com.izikode.izilib.roguin.helper.RoguinActivity
import com.izikode.izilib.roguin.endpoint.TwitterEndpoint

class MainActivity : RoguinActivity() {

    private val googleEndpoint by lazy { GoogleEndpoint(this) }
    private val facebookEndpoint by lazy { FacebookEndpoint(this) }
    private val twitterEndpoint by lazy { TwitterEndpoint(this) }

    private val googleStatus by lazy { findViewById<TextView>(R.id.googleStatus) }
    private val facebookStatus by lazy { findViewById<TextView>(R.id.facebookStatus) }
    private val twitterStatus by lazy { findViewById<TextView>(R.id.twitterStatus) }

    private val googleButton by lazy { findViewById<Button>(R.id.googleButton) }
    private val facebookButton by lazy { findViewById<Button>(R.id.facebookButton) }
    private val twitterButton by lazy { findViewById<Button>(R.id.twitterButton) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleStatus.text = "Google is " + if (googleEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"
        facebookStatus.text = "Facebook is " + if (facebookEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"
        twitterStatus.text = "Twitter is " + if (twitterEndpoint.isSignedIn) "CONNECTED" else "DISCONNECTED"

        googleButton.setOnClickListener {
            if (googleEndpoint.isSignedIn) {
                googleEndpoint.requestSignOut { success ->
                    if (success) {
                        googleStatus.text = "Google is DISCONNECTED"
                    }
                }
            } else {
                googleEndpoint.requestSignIn { success, token, error ->
                    if (success) {
                        googleStatus.text = "Google is CONNECTED"
                        Log.d("Google TOKEN", token.toString())
                    }
                }
            }
        }

        facebookButton.setOnClickListener {
            if (facebookEndpoint.isSignedIn) {
                facebookEndpoint.requestSignOut { success ->
                    if (success) {
                        facebookStatus.text = "Facebook is DISCONNECTED"
                    }
                }
            } else {
                facebookEndpoint.requestSignIn { success, token, error ->
                    if (success) {
                        facebookStatus.text = "Facebook is CONNECTED"
                        Log.d("Facebook TOKEN", token.toString())
                    }
                }
            }
        }

        twitterButton.setOnClickListener {
            if (twitterEndpoint.isSignedIn) {
                twitterEndpoint.requestSignOut { success ->
                    if (success) {
                        twitterStatus.text = "Twitter is DISCONNECTED"
                    }
                }
            } else {
                twitterEndpoint.requestSignIn { success, token, error ->
                    if (success) {
                        twitterStatus.text = "Twitter is CONNECTED"
                        Log.d("Twitter TOKEN", token.toString())
                    }
                }
            }
        }
    }

}
