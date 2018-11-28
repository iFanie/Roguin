package com.izikode.izilib.roguin.helper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.util.SparseArray
import com.facebook.CallbackManager
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import java.util.*

abstract class RoguinActivity : AppCompatActivity() {

    private val resultQueue = SparseArray<((success: Boolean, result: Intent?) -> Unit)>()
    private val resultCallbackManagers = arrayListOf<CallbackManager>()
    private val resultLoginButtons = arrayListOf<TwitterLoginButton>()

    private val random by lazy { Random() }

    private fun nextRequestCode(): Int {
        var candidate = random.nextInt(999)

        while (resultQueue.get(candidate, null) != null) {
            candidate = random.nextInt(999)
        }

        return candidate
    }

    fun requestResult(intent: Intent, response: (success: Boolean, result: Intent?) -> Unit) {
        val requestCode = nextRequestCode()
        resultQueue.append(requestCode, response)

        startActivityForResult(intent, requestCode)
    }

    fun registerCallbackManager(callbackManager: CallbackManager) {
        if (!resultCallbackManagers.contains(callbackManager)) {
            resultCallbackManagers.add(callbackManager)
        }
    }

    fun unregisterCallbackManager(callbackManager: CallbackManager) {
        if (resultCallbackManagers.contains(callbackManager)) {
            resultCallbackManagers.remove(callbackManager)
        }
    }

    fun registerLoginButton(loginButton: TwitterLoginButton) {
        if (!resultLoginButtons.contains(loginButton)) {
            resultLoginButtons.add(loginButton)
        }
    }

    fun unregisterLoginButton(loginButton: TwitterLoginButton) {
        if (resultLoginButtons.contains(loginButton)) {
            resultLoginButtons.remove(loginButton)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultCallbackManagers.forEach { callbackManager ->
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

        resultLoginButtons.forEach { loginButton ->
            loginButton.onActivityResult(requestCode, resultCode, data)
        }

        val responseUnit = resultQueue.get(requestCode, null)

        if (responseUnit != null) {
            responseUnit.invoke(resultCode == Activity.RESULT_OK, data)
            resultQueue.remove(requestCode)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}