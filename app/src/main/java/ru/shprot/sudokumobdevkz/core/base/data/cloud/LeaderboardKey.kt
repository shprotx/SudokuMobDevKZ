package ru.shprot.sudokumobdevkz.core.base.data.cloud

import java.security.MessageDigest

object LeaderboardKey {

    fun hash(stableId: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(stableId.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}