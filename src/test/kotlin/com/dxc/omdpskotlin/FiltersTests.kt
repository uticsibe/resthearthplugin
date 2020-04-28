package com.dxc.omdps

import com.dxc.omdpskotlin.api.Filters.`in`
import com.dxc.omdpskotlin.api.Filters.and
import com.dxc.omdpskotlin.api.Filters.eq
import com.dxc.omdpskotlin.api.Filters.exists
import com.dxc.omdpskotlin.api.Filters.gt
import com.dxc.omdpskotlin.api.Filters.gte
import com.dxc.omdpskotlin.api.Filters.key
import com.dxc.omdpskotlin.api.Filters.lt
import com.dxc.omdpskotlin.api.Filters.lte
import com.dxc.omdpskotlin.api.Filters.ne
import com.dxc.omdpskotlin.api.Filters.nin
import com.dxc.omdpskotlin.api.Filters.nor
import com.dxc.omdpskotlin.api.Filters.not
import com.dxc.omdpskotlin.api.Filters.or
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FiltersTests {
    
    @Test
    fun testExists() {
        val filter = exists("status", true).toString()
        assertEquals(filter, "{\"status\":{\"\$exists\":true}}")
    }

    @Test
    fun testEq() {
        val filter = eq("status", "TEST").toString()
        assertEquals(filter, "{\"status\":\"TEST\"}")
    }

    @Test
    fun testNe() {
        val filter = ne("status", "TEST").toString()
        assertEquals(filter, "{\"status\":{\"\$ne\":\"TEST\"}}")
    }

    @Test
    fun testGt() {
        val filter = gt("test", 2).toString()
        assertEquals(filter, "{\"test\":{\"\$gt\":2}}")
    }

    @Test
    fun testLt() {
        val filter = lt("test", 2).toString()
        assertEquals(filter, "{\"test\":{\"\$lt\":2}}")
    }

    @Test
    fun testGte() {
        val filter = gte("test", 2).toString()
        assertEquals(filter, "{\"test\":{\"\$gte\":2}}")
    }

    @Test
    fun testLte() {
        val filter = lte("test", 2).toString()
        assertEquals(filter, "{\"test\":{\"\$lte\":2}}")
    }

    @Test
    fun testIn() {
        val filter = `in`("status", "TEST", "TEST2", "TEST3").toString()
        assertEquals(filter, "{\"status\":{\"\$in\":[\"TEST\",\"TEST2\",\"TEST3\"]}}")
    }

    @Test
    fun testNin() {
        val filter = nin("status", "TEST", "TEST2", "TEST3").toString()
        assertEquals(filter, "{\"status\":{\"\$nin\":[\"TEST\",\"TEST2\",\"TEST3\"]}}")
    }

    @Test
    fun testAnd() {
        val filter = and(eq("status", "TEST"),
                eq("order_number", 12345)).toString()
        assertEquals(filter, "{\"\$and\":[{\"status\":\"TEST\"},{\"order_number\":12345}]}")
    }

    @Test
    fun testOr() {
        val filter = or(eq("status", "TEST"),
                eq("order_number", 12345)).toString()
        assertEquals(filter, "{\"\$or\":[{\"status\":\"TEST\"},{\"order_number\":12345}]}")
    }

    @Test
    fun testNot() {
        val filter = not(eq("status", "TEST")).toString()
        assertEquals(filter, "{\"\$not\":{\"status\":\"TEST\"}}")
    }

    @Test
    fun testNor() {
        val filter = nor(eq("status", "TEST"),
                eq("order_number", 12345)).toString()
        assertEquals(filter, "{\"\$nor\":[{\"status\":\"TEST\"},{\"order_number\":12345}]}")
    }

    @Test
    fun testKey() {
        val key = key("status", true).toString()
        println(key)
        assertEquals(key, "{\"status\":1}")
    }
}