package com.izikode.izilib.roguindemo

import android.app.Application
import com.izikode.izilib.roguin.FacebookEndpoint
import com.izikode.izilib.roguin.TwitterEndpoint

class RoguinDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FacebookEndpoint.initialize(this)
        TwitterEndpoint.initialize(this)
    }

}