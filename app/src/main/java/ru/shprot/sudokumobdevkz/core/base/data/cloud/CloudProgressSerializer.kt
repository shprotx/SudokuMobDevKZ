package ru.shprot.sudokumobdevkz.core.base.data.cloud

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress

object CloudProgressSerializer {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encode(progress: CloudProgress): ByteArray =
        json.encodeToString(progress).toByteArray(Charsets.UTF_8)

    fun decode(bytes: ByteArray): CloudProgress? =
        runCatching {
            json.decodeFromString<CloudProgress>(bytes.toString(Charsets.UTF_8))
        }.getOrNull()
}
