package com.mcdilan.test_project.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.size.Size
import com.mcdilan.test_project.BuildConfig
import com.mcdilan.test_project.QuotesViewModel
import com.mcdilan.test_project.R
import com.mcdilan.test_project.ext.getColorForPCP
import com.mcdilan.test_project.model.StockInfo
import com.mcdilan.test_project.model.getDisplayLtrAndName

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun QuotesScreen(viewModel: QuotesViewModel = hiltViewModel()) {
    val data by viewModel.data.collectAsState()
    Scaffold {
        LazyColumn(modifier = Modifier.padding(it)) {
            itemsIndexed(data) { index, item ->
                QuoteItem(item)
                if (index < data.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.LightGray,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteItem(item: StockInfo) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .padding(5.dp)
            .fillMaxSize()

    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(5.dp)
        ) {
            Ticker(item)
            QuoteData(item)
        }
    }
}

@Composable
fun Ticker(item: StockInfo) {
    val previousValues = remember { mutableStateMapOf<String, Double>() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${BuildConfig.TICKER_LOGO_URL}${item.c?.lowercase()}")
                    .memoryCacheKey(item.c?.lowercase())
                    .size(Size.ORIGINAL)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .padding(end = 4.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "icon"
            )
            Text(
                text = item.c.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.black)
            )
        }
        val previousValue = previousValues[item.c] ?: item.pcp
        previousValues[item.c!!] = item.pcp
        HighlightedText(
            value = item.pcp,
            previousValue = previousValue
        )
    }
}

@Composable
fun QuoteData(item: StockInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.getDisplayLtrAndName(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.grey)
        )
        Text(
            text = "${item.ltp} (${(item.chg)})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.black)
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun HighlightedText(
    value: Double,
    previousValue: Double,
    modifier: Modifier = Modifier
) {
    val backgroundColor = remember(value, previousValue) {
        when {
            value > previousValue -> Color.Green.copy(alpha = 0.2f)
            value < previousValue -> Color.Red.copy(alpha = 0.2f)
            else -> Color.Transparent
        }
    }
    val animatedColor = remember { Animatable(Color.Transparent) }

    LaunchedEffect(value) {
        animatedColor.animateTo(
            targetValue = backgroundColor,
            animationSpec = tween(durationMillis = 500)
        )
        animatedColor.animateTo(
            targetValue = Color.Transparent,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(animatedColor.value)
            .padding(8.dp)
    ) {
        Text(
            text = String.format("%.2f%%", value),
            color = colorResource(value.getColorForPCP())
        )
    }
}
