package ru.shprot.sudokumobdevkz.core.base.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

private const val MARKET_PREFIX = "market://details?id="
private const val WEB_PREFIX = "https://play.google.com/store/apps/details?id="

object PlayStoreLauncher {

    fun openPlayStore(context: Context, packageName: String) {
        val market = Intent(Intent.ACTION_VIEW, "$MARKET_PREFIX$packageName".toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (market.resolveActivity(context.packageManager) != null) {
            context.startActivity(market)
            return
        }

        val web = Intent(Intent.ACTION_VIEW, "$WEB_PREFIX$packageName".toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (web.resolveActivity(context.packageManager) != null) {
            context.startActivity(web)
        }
    }
}