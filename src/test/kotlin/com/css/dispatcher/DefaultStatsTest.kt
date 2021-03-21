package com.css.dispatcher

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultStatsTest {
    lateinit var stats: Stats

    @BeforeEach
    internal fun setUp() {
        stats = DefaultStats()
    }

    @Test
    internal fun `calculateStatistics returns average food wait time and average courier wait time`() {
        val expectedStatistics = Statistics(
            ordersProcessed = 1,
            averageFoodWaitTime = 3,
            averageCourierWaitTime = 0
        )

        val actualStatistics = stats.calculateStatistics(3, 0)

        assertEquals(expectedStatistics, actualStatistics)
    }

    @Test
    internal fun `calculateStatistics when there are already orders stored, weighs the new numbers correctly`() {
        val expectedStatistics = Statistics(
            ordersProcessed = 2,
            averageFoodWaitTime = 4,
            averageCourierWaitTime = 3
        )

        stats.calculateStatistics(5, 2)
        val actualStatistics = stats.calculateStatistics(3, 4)

        assertEquals(expectedStatistics, actualStatistics)
    }
}