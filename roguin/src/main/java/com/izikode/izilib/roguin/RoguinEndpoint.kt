package com.izikode.izilib.roguin

interface RoguinEndpoint {

    /**
     * When {@code true}, there is an active Sing-In token present.
     */
    val isSignedIn: Boolean

}