package com.mrntlu.bisu.ui.composeable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.Constants
import com.mrntlu.bisu.util.getStringAsDate
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NewsDetailView(item: Article) {
    val uri = LocalUriHandler.current
    val theme = LocalContext.current.getSharedPreferences(Constants.THEME_PREF, 0).getInt(Constants.THEME_PREF, Constants.LIGHT_THEME)
    val bgColor = if (theme == Constants.LIGHT_THEME) Color.White else Color.Black
    val contentColor = if (theme == Constants.LIGHT_THEME) Color.Black else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
    ) {
        item.urlToImage?.let {
            GlideImage(
                imageModel = it,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .testTag("newsImage")
            )
        }

        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            text = item.title,
            color = contentColor,
            modifier = Modifier.padding(6.dp),
        )

        Text(
            fontSize = 12.sp,
            text = item.publishedAt.getStringAsDate(),
            color = contentColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            textAlign = TextAlign.Start
        )

        item.content?.let {
            Text(
                fontSize = 16.sp,
                text = it,
                color = contentColor,
                modifier = Modifier.padding(6.dp),
            )
        }

        Button(
            modifier = Modifier
                .padding(top = 12.dp),
            onClick = {
                uri.openUri(item.url)
            }
        ) {
            Text(text = "Read on Browser")
        }
    }
}