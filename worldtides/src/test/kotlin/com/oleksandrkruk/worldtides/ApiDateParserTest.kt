package com.oleksandrkruk.worldtides

import org.junit.Assert
import org.junit.jupiter.api.Test
import java.util.*

class ApiDateParserTest {

    @Test
    fun parsesCorrectDateStringWithoutErrors() {
        val validDateStr = "2021-07-22T00:32:09+00:00"
        val date = Configs.apiDateFormat.parse(validDateStr)
        val calendar = Calendar.getInstance().apply { time = date }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        Assert.assertEquals(2021, year)
        Assert.assertEquals(6, month)
        Assert.assertEquals(22, day)
        Assert.assertEquals(0, hour)
        Assert.assertEquals(32, minute)
        Assert.assertEquals(9, second)
    }
}
