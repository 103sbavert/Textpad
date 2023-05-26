package com.sbeve72.textpad.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbeve72.textpad.presentation.ui.theme.TextpadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextpadTheme {
                HomeScreenContainer(Modifier.wrapContentSize())
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenContainer(
    modifier: Modifier = Modifier, appBarMinHeight: Float = with(LocalDensity.current) {
        56.dp.toPx()
    }, appBarMaxHeight: Float = with(LocalDensity.current) {
        100.dp.toPx()
    }
) {
    var scaleFactorDueToScroll by remember { mutableStateOf(1F) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {

                val verticalScroll = available.y
                val oldHeight = scaleFactorDueToScroll * appBarMaxHeight
                val newHeight = if (oldHeight + verticalScroll > 0) oldHeight + verticalScroll
                else 0F

                return if (verticalScroll < 0) {
                    scaleFactorDueToScroll = newHeight / appBarMaxHeight
                    Offset(0F, newHeight - oldHeight)
                } else {
                    Offset.Zero
                }

            }

            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                val verticalScroll = available.y
                val oldHeight = scaleFactorDueToScroll * appBarMaxHeight
                val newHeight =
                    if (oldHeight + verticalScroll < appBarMaxHeight) oldHeight + verticalScroll
                    else appBarMaxHeight

                return if (verticalScroll > 0) {
                    scaleFactorDueToScroll = newHeight / appBarMaxHeight
                    Offset(0F, newHeight - oldHeight)
                } else {
                    Offset.Zero
                }
            }
        }
    }


    Column(modifier.fillMaxSize()) {
        AppBar(
            appBarMinHeight,
            appBarMaxHeight,
            scaleFactorDueToScroll
        )
        LazyColumn(Modifier.nestedScroll(nestedScrollConnection)) {
            items(15) {
                Text("Sample Text $it", fontSize = 60.sp, modifier = modifier.wrapContentSize())
            }
        }

    }
}

@Composable
fun AppBar(
    minHeight: Float, maxHeight: Float, scaleFactor: Float
) {
    val shouldShowShadow = remember {
        mutableStateOf(scaleFactor < 1)
    }

    val maxHeightDp = with(LocalDensity.current) {
        maxHeight.toDp().value
    }

    val minHeightDp = with(LocalDensity.current) {
        minHeight.toDp().value
    }

    val animatedHeight by animateFloatAsState(
        minHeightDp + (maxHeightDp - minHeightDp) * scaleFactor
    )

    val animatedFontSize by animateFloatAsState(
        24 + scaleFactor * (36 - 24)
    )

    val animatedAlignment by animateFloatAsState(
        scaleFactor - 1
    )

    Surface(
        Modifier
            .background(MaterialTheme.colorScheme.error)
            .height(animatedHeight.dp)
            .shadow(if (shouldShowShadow.value) 6.dp else 0.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Text",
                fontSize = animatedFontSize.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .align(BiasAlignment.Horizontal(animatedAlignment))
            )
        }
    }
}