package com.dxc.omdpskotlin.api

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.lang.Nullable
import java.util.function.Consumer

object Filters {

    fun and(filters: Iterable<JSONObject?>): JSONObject {
        val arr = JSONArray()
        filters.forEach(Consumer { f: JSONObject? -> arr.put(f) })
        return JSONObject().put("\$and", arr)
    }

    fun and(vararg filters: JSONObject?): JSONObject {
        return and(listOf(*filters))
    }

    fun or(filters: Iterable<JSONObject?>): JSONObject {
        val arr = JSONArray()
        filters.forEach(Consumer { f: JSONObject? -> arr.put(f) })
        return JSONObject().put("\$or", arr)
    }

    fun or(vararg filters: JSONObject?): JSONObject {
        return or(listOf(*filters))
    }

    fun not(filter: JSONObject?): JSONObject {
        return JSONObject().put("\$not", filter)
    }

    fun nor(vararg filters: JSONObject?): JSONObject {
        return nor(listOf(*filters))
    }

    fun nor(filters: Iterable<JSONObject?>): JSONObject {
        val arr = JSONArray()
        filters.forEach(Consumer { f: JSONObject? -> arr.put(f) })
        return JSONObject().put("\$nor", arr)
    }

    fun exists(fieldName: String?, exists: Boolean = true): JSONObject {
        return JSONObject().put(fieldName, eq("\$exists", exists))
    }

    fun <TItem> eq(@Nullable value: TItem): JSONObject {
        return eq("_id", value)
    }

    fun <TItem> eq(fieldName: String?, @Nullable value: TItem): JSONObject {
        return JSONObject().put(fieldName, value)
    }

    fun <TItem> ne(fieldName: String?, @Nullable value: TItem): JSONObject {
        return JSONObject().put(fieldName, eq("\$ne", value))
    }

    fun <TItem> gt(fieldName: String?, value: TItem): JSONObject {
        return JSONObject().put(fieldName, eq("\$gt", value))
    }

    fun <TItem> lt(fieldName: String?, value: TItem): JSONObject {
        return JSONObject().put(fieldName, eq("\$lt", value))
    }

    fun <TItem> gte(fieldName: String?, value: TItem): JSONObject {
        return JSONObject().put(fieldName, eq("\$gte", value))
    }

    fun <TItem> lte(fieldName: String?, value: TItem): JSONObject {
        return JSONObject().put(fieldName, eq("\$lte", value))
    }

    fun <TItem> `in`(fieldName: String?, vararg values: TItem): JSONObject {
        return `in`(fieldName, listOf(*values))
    }

    fun <TItem> `in`(fieldName: String?, values: Iterable<TItem>): JSONObject {
        val arr = JSONArray()
        values.forEach(Consumer { v: TItem -> arr.put(v) })
        return JSONObject().put(fieldName, eq("\$in", arr))
    }

    fun <TItem> nin(fieldName: String?, vararg values: TItem): JSONObject {
        return nin(fieldName, listOf(*values))
    }

    fun <TItem> nin(fieldName: String?, values: Iterable<TItem>): JSONObject {
        val arr = JSONArray()
        values.forEach(Consumer { v: TItem -> arr.put(v) })
        return JSONObject().put(fieldName, eq("\$nin", arr))
    }

    fun key(fieldName: String, contain: Boolean): JSONObject {
        return JSONObject().put(fieldName, if(contain) 1 else 0)
    }
}