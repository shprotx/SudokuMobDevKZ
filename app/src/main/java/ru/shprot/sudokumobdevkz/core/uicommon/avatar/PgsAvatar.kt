package ru.shprot.sudokumobdevkz.core.uicommon.avatar

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.google.android.gms.common.images.ImageManager
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun PgsAvatar(
    modifier: Modifier,
    size: Dp,
    avatarUrl: String?,
    tint: Color = AppTheme.colors.textSecondary,
) {
    val personPainter = rememberVectorPainter(Icons.Filled.Person)
    val avatarModifier = modifier
        .size(size)
        .clip(CircleShape)

    when {
        avatarUrl.isNullOrBlank() ->
            Icon(
                modifier = avatarModifier,
                painter = personPainter,
                contentDescription = null,
                tint = tint,
            )

        avatarUrl.startsWith("http") ->
            AsyncImage(
                modifier = avatarModifier,
                model = avatarUrl,
                contentDescription = null,
                placeholder = personPainter,
                error = personPainter,
                fallback = personPainter,
                contentScale = ContentScale.Crop,
            )

        avatarUrl.startsWith("content://") ->
            GmsContentAvatar(
                modifier = avatarModifier,
                size = size,
                contentUri = avatarUrl,
                placeholder = personPainter,
                tint = tint,
            )

        else ->
            Icon(
                modifier = avatarModifier,
                painter = personPainter,
                contentDescription = null,
                tint = tint,
            )
    }
}

@Composable
private fun GmsContentAvatar(
    modifier: Modifier,
    size: Dp,
    contentUri: String,
    placeholder: androidx.compose.ui.graphics.painter.Painter,
    tint: Color,
) {
    val context = LocalContext.current
    var drawable by remember(contentUri) { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(contentUri) {
        val imageManager = ImageManager.create(context)
        val listener = ImageManager.OnImageLoadedListener { _, loaded, _ ->
            drawable = loaded
        }
        imageManager.loadImage(listener, contentUri.toUri())
    }

    val bitmap = remember(drawable, size) {
        drawable?.toBitmap()?.asImageBitmap()
    }

    if (bitmap != null) {
        Image(
            modifier = modifier,
            painter = BitmapPainter(bitmap),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    } else {
        Icon(
            modifier = modifier,
            painter = placeholder,
            contentDescription = null,
            tint = tint,
        )
    }
}
