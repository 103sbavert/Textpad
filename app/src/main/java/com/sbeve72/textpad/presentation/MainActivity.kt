package com.sbeve72.textpad.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sbeve72.textpad.R
import com.sbeve72.textpad.presentation.ui.AppBar
import com.sbeve72.textpad.presentation.ui.AppBarNestedScrollConnection
import com.sbeve72.textpad.presentation.ui.theme.Constants


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HomeScreen()
            }
        }
    }
}

@Preview
@Composable
fun HomeScreen() {
    val appBarMaxHeightPx = with(LocalDensity.current) {
        Constants.homePageToolBarMaxHeight.toPx()
    }

    val nestedScrollConnection: AppBarNestedScrollConnection = remember {
        AppBarNestedScrollConnection(appBarMaxHeightPx)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HomePageAppBar(
            modifier = Modifier.fillMaxWidth(),
            onSettingsClicked = {},
            onSearchClicked = {},
            nestedScrollConnection = nestedScrollConnection
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            items(100) {
                Text(text = "Text")
            }
        }
    }
}

@Composable
fun HomePageAppBar(
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    nestedScrollConnection: AppBarNestedScrollConnection
) {
    AppBar(
        modifier.padding(6.dp),
        title = stringResource(R.string.app_name),
        scaleFactorProvider = nestedScrollConnection.scaleFactorProvider,
        appBarMinHeight = Constants.homePageToolBarMinHeight,
        appBarMaxHeight = Constants.homePageToolBarMaxHeight
    ) {
        Row(
            verticalAlignment = BiasAlignment.Vertical(
                lerp(
                    0F,
                    1F,
                    nestedScrollConnection.scaleFactorProvider()
                )
            ),
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxSize()

        ) {
            IconButton(onClick = onSearchClicked) {
                Icon(Icons.Default.Search, "Settings Icon")
            }
            IconButton(onClick = onSettingsClicked) {
                Icon(Icons.Default.Settings, "Settings Icon")
            }
        }

    }
}


