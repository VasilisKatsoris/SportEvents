package com.sportevents.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sportevents.R
import com.sportevents.network.util.ErrorReason
import com.sportevents.util.Util
import kotlinx.coroutines.delay
import kotlin.math.abs


@Composable
fun SportEventsCompose(viewModel: SportEventsViewModel) {
    val isLoading = viewModel.isLoading.collectAsState()
    val sportEvents = viewModel.sportEvents.collectAsState()
    val error = viewModel.error.collectAsState()

    val actions = SportEventsActions(
        onFavoriteClicked = viewModel::onFavoriteClicked,
        onRefreshClicked = viewModel::refresh,
        clearDb = viewModel::clearDb,
        onFilterFavoriteClicked = viewModel::onFilterFavoriteClicked,
        onExpandToggled = viewModel::onExpandToggled
    )

    SportEventsCompose(
        state = SportEventsState(
            isLoading = isLoading.value,
            sportEvents = sportEvents.value,
            error = error.value
        ),
        actions = actions
    )
}

@Stable
data class SportEventsState(
    val isLoading: Boolean,
    val sportEvents: List<SportListItem>?,
    val error: ErrorReason?
)

@Stable
data class SportEventsActions(
    val onFavoriteClicked: (sportListItem: SportListItem.SportGridItem) -> Unit,
    val onFilterFavoriteClicked: (sportHeader: SportListItem.SportHeader) -> Unit,
    val onExpandToggled: (sportHeader: SportListItem.SportHeader) -> Unit,
    val onRefreshClicked: () -> Unit,
    val clearDb: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SportEventsCompose(
    state: SportEventsState,
    actions: SportEventsActions
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    Row {

                        Icon(
                            modifier = Modifier.clickable { actions.clearDb() },
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                        )

                        Icon(
                            modifier = Modifier.clickable { actions.onRefreshClicked() },
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { it ->
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                val text = when (state.error) {
                    ErrorReason.Authorization -> "Authorization failed"
                    ErrorReason.NetworkConnection -> "Network connection problem"
                    is ErrorReason.Unspecified -> state.error.message ?: "Unknown error"
                }
                ErrorCompose(text, actions.onRefreshClicked)
            } else {
                state.sportEvents?.let {
                    if (state.sportEvents.isEmpty()) {
                        ErrorCompose(
                            "Looks like there are no data. Try again later",
                            actions.onRefreshClicked
                        )
                    } else {
                        SportEventsList(state, actions)
                    }
                }
            }
        }
    }
}

@Composable()
fun SportEventsList(
    state: SportEventsState,
    actions: SportEventsActions
) {

    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Introduce a 1 second delay
            currentTime.value = System.currentTimeMillis() // Update time state
        }
    }

    val columns = 3

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(columns),
    ) {

        items(
            items = state.sportEvents ?: emptyList(),
            key = { it.id },
            span = { GridItemSpan(if (it is SportListItem.SportHeader) columns else 1) }
        ) {
            when (it) {
                is SportListItem.SportHeader -> SportHeader(
                    modifier = Modifier.fillMaxWidth(),
                    item = it,
                    actions = actions
                )

                is SportListItem.SportGridItem -> SportEventsGridItem(
                    sportListItem = it,
                    onFavoriteClicked = actions.onFavoriteClicked,
                    currentTime = currentTime.value
                )
            }
        }
    }
}

@Composable
fun ErrorCompose(
    error: String,
    tryAgainAction: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = error)

            Button(
                onClick = tryAgainAction,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Try again")
            }
        }
    }
}


@Composable
fun SportHeader(
    modifier: Modifier = Modifier,
    item: SportListItem.SportHeader,
    actions: SportEventsActions
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth().padding(5.dp).background(Color.Blue)
            .padding(horizontal = 10.dp)
    ) {
        val (arrow, toggle, title) = createRefs()

        Icon(
            modifier = Modifier.constrainAs(arrow) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }.clickable {
                actions.onExpandToggled(item)
            },
            imageVector = if (item.expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            tint = Color.White,
            contentDescription = ""
        )

        Switch(
            modifier = Modifier.constrainAs(toggle) {
                end.linkTo(arrow.start, margin = 20.dp)
                centerVerticallyTo(parent)
            },
            checked = item.filterFavorites, onCheckedChange = {
                actions.onFilterFavoriteClicked(item)
            },
            thumbContent = {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                linkTo(start = parent.start, end = toggle.start, endMargin = 10.dp, bias = 0F)
                centerVerticallyTo(parent)
                width = Dimension.fillToConstraints
            }.clickable {
                actions.onExpandToggled(item)
            },
            color = Color.White,
            text = item.name
        )
    }
}

@Composable
fun SportEventsGridItem(
    sportListItem: SportListItem.SportGridItem,
    onFavoriteClicked: (sportListItem: SportListItem.SportGridItem) -> Unit,
    currentTime: Long
) {

    Column(
        modifier = Modifier.padding(vertical = 15.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val diff = sportListItem.startTime * 1000 - currentTime
        val started = diff < 0

        Text(
            color = if (started) Color.Red else Color.Green,
            text = Util.millisecondsToDuration(abs(diff))
        )

        Icon(
            modifier = Modifier.padding(8.dp).clickable { onFavoriteClicked(sportListItem) },
            imageVector = Icons.Default.Star,
            tint = if (sportListItem.favorite) Color.Magenta else Color.Gray,
            contentDescription = ""
        )

        Text(
            text = sportListItem.team1 ?: "",
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(4.dp),
            color = Color.Red,
            text = "VS",
            textAlign = TextAlign.Center
        )

        Text(
            text = sportListItem.team2 ?: "",
            textAlign = TextAlign.Center
        )
    }
}