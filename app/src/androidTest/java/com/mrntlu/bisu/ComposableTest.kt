package com.mrntlu.bisu

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.models.response.Source
import com.mrntlu.bisu.ui.composeable.NewsDetailView
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ComposableTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            NewsDetailView(item = Article(null, "Content", "Description", "2022-09-26T12:42:10Z", Source(), "Title", "https://test.com", null))
        }
    }

    @Test
    fun readOnBrowser_isClickable() {
        val composeTestCase = composeTestRule.onNodeWithText("Read on Browser")
        composeTestCase.performClick()
    }

    @Test
    fun newsImage_isVisibleWhenImageIsNull() {
        composeTestRule.onNodeWithTag("newsImage").assertDoesNotExist()
    }
}