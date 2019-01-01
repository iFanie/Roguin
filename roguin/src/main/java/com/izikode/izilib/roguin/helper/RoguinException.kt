package com.izikode.izilib.roguin.helper

import android.content.Intent
import java.lang.Exception

open class RoguinException(

        /**
         * The error that produced the Exception.
         */
        val internalException: Exception? = null,

        /**
         * The unsuccessful data that produced the Exception.
         */
        val internalIntent: Intent? = null,

        /**
         * The error message that produced the Exception.
         */
        val internalMessage: String? = null

) : Exception(internalException)