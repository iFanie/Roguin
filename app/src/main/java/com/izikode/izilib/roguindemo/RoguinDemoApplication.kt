package com.izikode.izilib.roguindemo

import android.app.Application
import com.izikode.izilib.roguin.endpoint.FacebookEndpoint
import com.izikode.izilib.roguin.endpoint.TwitterEndpoint

class RoguinDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FacebookEndpoint.initialize(this)
        TwitterEndpoint.initialize(this)
    }

}