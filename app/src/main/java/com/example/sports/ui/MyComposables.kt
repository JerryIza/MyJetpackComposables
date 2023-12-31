package com.example.sports.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sports.R
import com.example.sports.data.LocalSportsDataProvider
import com.example.sports.model.MyLocalData
import com.example.sports.model.Symbol
import com.example.sports.ui.theme.SportsTheme

/**
 * Main composable that serves as container
 * which displays content according to [uiState] and [windowSize]
 */

val listTest = listOf<Symbol>(
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("F", "20.5", "1.0", "3.5", "1.1M"),
    Symbol("NVDA", "402.5", "10", "3", "1.3M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMDDVDVD", "40.5", "3.0", "1.5", "3.1M"),
    Symbol("AMD", "40.5", "3.0", "1.5", "3.1M"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsApp(
) {
    val viewModel: SportsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SportsAppBar(
                isShowingListPage = uiState.isShowingListPage,
                onBackButtonClick = { viewModel.navigateToListPage() },
            )
        }
    ) { innerPadding ->
        if (uiState.isShowingListPage) {
            SportsList(
                sports = uiState.sportsList,
                onClick = {
                    viewModel.updateCurrentSport(it)
                    viewModel.navigateToDetailPage()
                },
                modifier = Modifier.padding((innerPadding))
            )
        } else {
            SportsDetail(
                selectedSport = uiState.currentSport,
                modifier = Modifier.padding((innerPadding)),
                onBackPressed = {
                    viewModel.navigateToListPage()
                }
            )
        }
    }
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsAppBar(
    onBackButtonClick: () -> Unit,
    isShowingListPage: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text =
                if (!isShowingListPage) {
                    stringResource(R.string.detail_fragment_label)
                } else {
                    stringResource(R.string.list_fragment_label)
                }
            )
        },
        navigationIcon = if (!isShowingListPage) {
            {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        } else {
            { Box() {} }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SportsListItem(
    data: MyLocalData,
    onItemClick: (MyLocalData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
//        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onItemClick(data) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_small),
//                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                    .weight(1f)
            ) {
                Row {
                    Text(
                        text = stringResource(data.titleResourceId),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.card_text_vertical_space))
                    )
                    Spacer(Modifier.weight(1f))
                    if (data.olympic) {
                        SwitchButton()
                    }
                }
                Spacer(Modifier.weight(.5f))
                Row(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)

                ) {
                    WidgetContent(data)
                }
                Spacer(Modifier.weight(1f))
                Row {
                    Text(
                        text = stringResource(data.playerCount),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.weight(1f))
                    if (data.olympic) {
                        Text(
                            text = stringResource(R.string.option_bp_balance),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
//text = pluralStringResource(R.plurals.player_count_caption, sport.playerCount, sport.playerCount),


@Composable
private fun SportsList(
    sports: List<MyLocalData>,
    onClick: (MyLocalData) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
//        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(sports, key = { sport -> sport.id }) { sport ->
            SportsListItem(
                data = sport,
                onItemClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SportsDetail(
    selectedSport: MyLocalData,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackPressed()
    }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .verticalScroll(state = scrollState)
    ) {
        Column {
            Box {
                Box() {
                    Image(
                        painter = painterResource(selectedSport.sportsImageBanner),
                        contentDescription = null,
                        alignment = Alignment.TopCenter,
                        contentScale = ContentScale.FillWidth,
                    )
                }
                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, MaterialTheme.colorScheme.scrim),
                                0f,
                                400f
                            )
                        )
                ) {

                    Text(
                        text = stringResource(selectedSport.titleResourceId),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    )
                    Row(
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    ) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.player_count_caption,
                                selectedSport.playerCount,
                                selectedSport.playerCount
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.olympic_caption),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                    }
                }
            }
            Text(
                text = stringResource(selectedSport.sportDetails),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.padding_detail_content_vertical),
                    horizontal = dimensionResource(R.dimen.padding_detail_content_horizontal)
                )
            )
        }
    }
}

@Preview()
@Composable
fun SportsListItemPreview() {
    SportsTheme {
        SportsListItem(
            data = LocalSportsDataProvider.defaultSport,
            onItemClick = {}
        )
    }
}

@Preview()
@Composable
fun SportsListPreview() {
    SportsTheme() {
        Surface {
            SportsList(
                sports = LocalSportsDataProvider.getSportsData(),
                onClick = {}
            )
        }
    }
}


@Composable
fun SwitchButton() {
    val checked = remember { mutableStateOf(true) }
    Switch(
        checked = checked.value,
        onCheckedChange = { checked.value = it },
    )
}

@Composable
fun WidgetContent(data: MyLocalData) {

    when (data.id) {
        1 -> AccountBalancesContent(data)
        2 -> Log.d("Jerry", "")
        3 -> MyTable(listTest)
        4 -> Log.d("Jerry", "")
    }
}

@Preview()
@Composable
fun WatchListItem() {
    SportsTheme {

        MyTable(listTest)
    }
}

@Composable
fun AccountBalancesContent(data: MyLocalData) {
    Text(
        text = stringResource(data.subtitleResourceId),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.secondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 6,
    )
}


data class Cell(
    val name: String,
    val weight: Float
)

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),

        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )

}

@Composable
fun MyTable(data: List<Symbol>) {
    val parentColumn = listOf(
        Cell("Symbol", .25f),
        Cell("Last", .25f),
        Cell("Net Chg", .25f),
        Cell("% Chg", .25f),
        Cell("Volume", .25f)
    )

    LazyColumn(
        Modifier
            .height(450.dp)
    ) {
        item {
            Row {
                parentColumn.forEach {
                    TableCell(
                        text = it.name,
                        weight = it.weight
                    )
                }
            }
        }

        items(data) {
            Row(Modifier.fillMaxWidth()) {
                TableCell(
                    text = it.symbol,
                    weight = parentColumn[0].weight
                )

                TableCell(
                    text = it.last,
                    weight = parentColumn[1].weight
                )

                TableCell(
                    text = it.netChg,
                    weight = parentColumn[2].weight
                )

                TableCell(
                    text = it.percentChg,
                    weight = parentColumn[3].weight
                )
                TableCell(
                    text = it.volume,
                    weight = parentColumn[4].weight
                )
            }
        }
    }
}




