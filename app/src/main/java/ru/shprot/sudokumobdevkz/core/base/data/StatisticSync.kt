package ru.shprot.sudokumobdevkz.core.base.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticSync @Inject constructor(
    private val repository: SudokuRepository,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val started = AtomicBoolean(false)

    fun ensureStarted() {
        if (started.compareAndSet(false, true)) {
            scope.launch { repository.syncStatisticsFromFirebase() }
        }
    }
}