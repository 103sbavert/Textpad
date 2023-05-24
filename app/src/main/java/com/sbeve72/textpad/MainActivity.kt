package com.sbeve72.textpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbeve72.textpad.ui.theme.TextpadTheme

enum class AppBarState {
    Expanded, Collapsed
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}


@Preview
@Composable
private fun MyApp() {
    val modifier = Modifier
    var appBarState by remember { mutableStateOf(AppBarState.Collapsed) }

    val deltaConsumer = { delta: Float ->
        if (appBarState == AppBarState.Expanded) {
            if (delta > 0) {
                appBarState = AppBarState.Collapsed
            }
        } else {
            if (delta < 0) {
                appBarState = AppBarState.Expanded
            }
        }
        delta
    }

    val scrollState = rememberScrollableState(deltaConsumer)

    TextpadTheme {
        Surface(modifier = modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.surface) {
            Column {
                AppBar(appBarState = appBarState)
                Column(
                    modifier.scrollable(scrollState, Orientation.Vertical)
                ) {
                    for (i in 1..100) {
                        Text(text = "ITS")
                    }
                }
            }
        }
    }

    if (scrollState.isScrollInProgress) {
        // collapse and expand the appbar
    }
}


@Preview
@Composable
fun Button(modifier: Modifier = Modifier) {
    Box (modifier.width(100.dp).heightIn(32.dp)) {
        Icon (
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = modifier.align(Alignment.CenterStart).fillMaxWidth()
        )
        Text(
            text = "String",
            modifier = modifier.align(Alignment.Center).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AppBar(modifier: Modifier = Modifier, appBarState: AppBarState) {
    Surface(modifier.fillMaxWidth()) {
        if (appBarState == AppBarState.Collapsed) {
            CollapsedAppBar(modifier)
        } else {
            ExpandedAppBar(modifier)
        }
    }
}


@Composable
fun CollapsedAppBar(modifier: Modifier) {
    Box(
        modifier = modifier.heightIn(min = 48.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = modifier.align(Alignment.BottomStart),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}


@Composable
fun ExpandedAppBar(modifier: Modifier) {
    Box(
        modifier = modifier.heightIn(min = 96.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = modifier.align(Alignment.BottomCenter),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
