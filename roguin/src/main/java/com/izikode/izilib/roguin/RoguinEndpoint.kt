package com.izikode.izilib.roguin

import com.izikode.izilib.roguin.helper.RoguinException
import com.izikode.izilib.roguin.model.RoguinToken

interface RoguinEndpoint {

    /**
     * When {@code true}, there is an active Sing-In token present.
     */
    val isSignedIn: Boolean

    /**
     * Initiates a SignIn flow.
     *
     * @param response  The Unit to be invoked when the flow ends.
     */
    fun requestSignIn(response: (success: Boolean, result: RoguinToken?, error: RoguinException?) -> Unit)

    /**
     * Initiates a SignOut flow.
     *
     * @param response  The Unit to be invoked when the flow ends.
     */
    fun requestSignOut(response: (success: Boolean) -> Unit)

}