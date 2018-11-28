package com.izikode.izilib.roguin.model

import com.izikode.izilib.roguin.RoguinEndpoint
import kotlin.reflect.KClass

data class RoguinToken(

    val endpoint: KClass<out RoguinEndpoint>,

    val authenticatedToken: String,

    val userId: String

)