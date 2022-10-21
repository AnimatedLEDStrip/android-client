package animatedledstrip.androidcontrol.utils

import animatedledstrip.client.ALSHttpClient
import io.ktor.client.engine.android.*

data class ALSClientInfo(
    val client: ALSHttpClient<AndroidEngineConfig>,
    val ip: String,
    val name: String = "",
)
