package com.mrntlu.bisu

import com.mrntlu.bisu.util.getAsPureString
import com.mrntlu.bisu.util.getStringAsDate
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class UtilsTest {

    @Test
    fun testIsStringPruified() {
        val testString = "nmhE`uvOU0#\$q#7}7]_x.mQH`L"
        val purified = testString.getAsPureString()
        val regex = Pattern.compile("^[A-Z0-9]+\$")
        assert(!purified.matches(regex.toRegex()))
    }

    @Test
    fun testIsStringDate() {
        val string = "2022-09-26T12:42:10Z"
        val dateString = string.getStringAsDate()

        val date = SimpleDateFormat("MMM dd',' yy 'at' HH:mm", Locale.getDefault()).parse(dateString)

        assert(date != null)
    }
}