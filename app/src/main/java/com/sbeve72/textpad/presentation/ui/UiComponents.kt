package com.sbeve72.textpad.presentation.ui

import android.util.Log.e
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

class AppBarNestedScrollConnection(private val maxHeight: Float) : NestedScrollConnection {

    private var scaleFactor: Float by mutableStateOf(1F)
    val scaleFactorProvider = {
        e("ScaleFactorProviderCalled", "Value: $scaleFactor")
        scaleFactor
    }

    override fun onPreScroll(
        available: Offset, source: NestedScrollSource
    ): Offset {

        val verticalScroll = available.y
        val oldHeight = scaleFactor * maxHeight
        val newHeight = if (oldHeight + verticalScroll > 0) oldHeight + verticalScroll
        else 0F

        return if (verticalScroll < 0) {
            scaleFactor = newHeight / maxHeight
            e("SCALE FACTOR", scaleFactor.toString())
            Offset(0F, newHeight - oldHeight)
        } else {
            Offset.Zero
        }

    }

    override fun onPostScroll(
        consumed: Offset, available: Offset, source: NestedScrollSource
    ): Offset {
        val verticalScroll = available.y
        val oldHeight = scaleFactor * maxHeight
        val newHeight = if (oldHeight + verticalScroll < maxHeight) oldHeight + verticalScroll
        else maxHeight

        return if (verticalScroll > 0) {
            scaleFactor = newHeight / maxHeight
            Offset(0F, newHeight - oldHeight)
        } else {
            Offset.Zero
        }
    }
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    scaleFactorProvider: () -> Float,
    appBarMinHeight: Dp,
    appBarMaxHeight: Dp,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val appBarMinHeightPx = with(LocalDensity.current) { appBarMinHeight.roundToPx() }

    val appBarMaxHeightPx = with(LocalDensity.current) { appBarMaxHeight.roundToPx() }

    val backgroundColor = androidx.compose.ui.graphics.lerp(
        MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        MaterialTheme.colorScheme.surface,
        scaleFactorProvider()
    )

    val appBarSurfaceLayoutModifier = fun MeasureScope.(
        appBarSurfaceMeasurable: Measurable, constraints: Constraints
    ): MeasureResult {
        val appBarSurfacePlaceable = appBarSurfaceMeasurable.measure(
            Constraints.fixed(
                constraints.maxWidth,
                lerp(appBarMinHeightPx, appBarMaxHeightPx, scaleFactorProvider())
            )
        )

        return layout(constraints.maxWidth, appBarSurfacePlaceable.height) {
            appBarSurfacePlaceable.place(0, 0)
        }
    }


    Surface(
        color = backgroundColor,
        shadowElevation = androidx.compose.ui.unit.lerp(6.dp, 0.dp, scaleFactorProvider()),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .layout(appBarSurfaceLayoutModifier)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .wrapContentSize()
                    .align(BiasAlignment(lerp(-1F, 0F, scaleFactorProvider()), 0F))
            )
            if (content != null) {
                content()
            }
        }
    }

}