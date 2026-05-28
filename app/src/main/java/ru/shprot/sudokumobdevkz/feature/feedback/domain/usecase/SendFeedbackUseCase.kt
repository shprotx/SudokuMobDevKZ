package ru.shprot.sudokumobdevkz.feature.feedback.domain.usecase

import android.os.Build
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.data.remote.FeedbackApiHolder
import ru.shprot.sudokumobdevkz.core.base.data.remote.FeedbackRequestDto
import ru.shprot.sudokumobdevkz.core.base.data.util.safeRunCatching
import java.util.Locale
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor(
    private val apiHolder: FeedbackApiHolder,
    private val cloud: CloudGameServices,
) {

    val isConfigured: Boolean get() = apiHolder.value != null

    suspend fun execute(text: String): Result<Unit> {
        val api = apiHolder.value ?: return Result.failure(IllegalStateException("not configured"))
        val body = FeedbackRequestDto(
            text = text,
            appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            androidSdk = Build.VERSION.SDK_INT,
            locale = Locale.getDefault().toLanguageTag(),
            isPgsSignedIn = cloud.signInState.value is SignInState.SignedIn,
        )
        return safeRunCatching {
            val response = api.submit(body)
            if (!response.isSuccessful) {
                throw Exception("HTTP ${response.code()}")
            }
        }
    }
}