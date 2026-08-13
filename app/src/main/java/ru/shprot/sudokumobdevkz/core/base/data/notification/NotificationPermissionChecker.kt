package ru.shprot.sudokumobdevkz.core.base.data.notification

interface NotificationPermissionChecker {
    fun isGranted(): Boolean
}
