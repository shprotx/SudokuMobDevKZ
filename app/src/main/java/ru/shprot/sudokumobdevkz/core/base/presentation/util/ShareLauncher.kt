package ru.shprot.sudokumobdevkz.core.base.presentation.util

import android.content.Context
import android.content.Intent

object ShareLauncher {

    fun launch(
        context: Context,
        text: String,
        subject: String? = null,
        chooserTitle: String? = null,
    ) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            if (subject != null) putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        val chooser = Intent.createChooser(sendIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
