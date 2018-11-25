package com.izikode.izilib.roguin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.util.SparseArray
import java.util.*

abstract class RoguinActivity : AppCompatActivity() {

    private val resultQueue = SparseArray<((success: Boolean, result: Intent?) -> Unit)>()

    private val random by lazy { Random() }

    private fun nextRequestCode(): Int {
        var candidate = random.nextInt()

        while (resultQueue.get(candidate, null) != null) {
            candidate = random.nextInt()
        }

        return candidate
    }

    fun requestResult(intent: Intent, response: (success: Boolean, result: Intent?) -> Unit) {
        val requestCode = nextRequestCode()
        resultQueue.append(requestCode, response)

        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val responseUnit = resultQueue.get(requestCode, null)

        if (responseUnit != null) {
            responseUnit.invoke(resultCode == Activity.RESULT_OK, data)
            resultQueue.remove(requestCode)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}