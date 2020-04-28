package com.dxc.omdpskotlin.api


import com.dxc.omdpskotlin.config.RestHeartProperties
import io.micrometer.core.instrument.MeterRegistry
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.function.Consumer

@Service
class RestHeartClientApi() {

    lateinit var restheartProperties: RestHeartProperties
    companion object {
        val log = LogManager.getLogger()
    }

    fun config(restheartProperties: RestHeartProperties) {
        this.restheartProperties = restheartProperties
    }


    val BASE_URL: String
    val username: String
    val password: String
    val restTemplate: RestTemplate = RestTemplate(HttpComponentsClientHttpRequestFactory())

    val httpHeaders: HttpHeaders
        get() {
            val headers = HttpHeaders()
            headers.setAccept(listOf(MediaType.APPLICATION_JSON))
            headers.setBasicAuth(username, password)
            return headers
        }

    init {
        BASE_URL = "http://" + restheartProperties.hostname + ":" +
                restheartProperties.port + "/"
        username = restheartProperties.username
        password = restheartProperties.password
    }

    fun <T> get(collection: String?): List<T> = get(collection, null, null)

    fun <T> get(collection: String?, filter: String?): List<T> = get(collection, filter, null)

    fun <T> get(collection: String?, keys: List<String>?): List<T> = get(collection, null, keys)

    fun <T> get(collection: String?, filter: String?, keys: List<String>?): List<T> {
        val builder: UriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(BASE_URL + restheartProperties.collections[collection])

        if (filter != null && filter.isNotEmpty())
            builder.queryParam("filter", filter)

        if (keys != null && keys.isNotEmpty())
            keys.stream().forEach(Consumer { k: String -> builder.queryParam("keys", k) })

        val GET_URI: URI = builder.build().encode().toUri()
        val headers: HttpHeaders = httpHeaders
        val req = RequestEntity<T>(headers,
                HttpMethod.GET,
                GET_URI)

        var result: ResponseEntity<List<T>?>? = null
        try {
            result = restTemplate.exchange(req, object : ParameterizedTypeReference<List<T>?>() {})
        } catch (e: HttpClientErrorException) {
            log.info("Error GET:" + e.message)
        }

        log.info("RestHeart GET StatusCode:" + result?.statusCode)
        if (result == null ) {
            return ArrayList()
        }
        return result?.body!!
    }

    fun <T> getById(collection: String?, id: String?): T? {
        val GETBYID_URI = URI.create(BASE_URL + restheartProperties.collections[collection] + "/" + id)
        val headers: HttpHeaders = httpHeaders
        //  HttpEntity <String> entity = new HttpEntity <String> ("parameters", headers);

        val req = RequestEntity<T>(headers,
                HttpMethod.GET,
                GETBYID_URI)

        var result: ResponseEntity<T>? = null
        try {
            result = restTemplate.exchange(req, object : ParameterizedTypeReference<T>() {})
        } catch (e: HttpClientErrorException) {
            log.info("Error GETBYID:" + e.message)
        }

        log.info("RestHeart GETBYID StatusCode:" + result?.statusCode)
        if (result == null ) {
            return null
        }
        return result?.body!!
    }

    fun <T> create(collection: String?, body: T): String {
        val CREATE_URI = URI.create(BASE_URL + restheartProperties.collections[collection])
        val headers: HttpHeaders = httpHeaders

        val req = RequestEntity(body,
                headers,
                HttpMethod.POST,
                CREATE_URI,
                Any::class.java)

        val result: ResponseEntity<String> = restTemplate.exchange(req, String::class.java)

        log.info("RestHeart POST StatusCode:" + result.statusCode)
        return result.headers.location!!.rawPath.split("/")[2]
    }

    fun <T> update(collection: String?, id: String, body: T) {
        val UPDATE_URI = URI.create(BASE_URL + restheartProperties.collections[collection] + "/" + id)
        val headers: HttpHeaders = httpHeaders

        val req = RequestEntity(body,
                headers,
                HttpMethod.PUT,
                UPDATE_URI,
                Any::class.java)

        val result: ResponseEntity<String> = restTemplate.exchange(req, String::class.java)

        RestHeartClientApi.log.info("RestHeart PUT StatusCode:" + result.statusCode)
    }

    fun patch(collection: String?, id: String, patchList: Map<String, String>?) {
        val PATCH_URI = URI.create(BASE_URL + restheartProperties.collections[collection] + "/" + id)
        val headers: HttpHeaders = httpHeaders

        val req = RequestEntity(patchList,
                headers,
                HttpMethod.PATCH,
                PATCH_URI,
                MutableMap::class.java)

        val result: ResponseEntity<String> = restTemplate.exchange(req, String::class.java)

        log.info("RestHeart PATCH StatusCode:" + result.getStatusCode())
    }

    fun delete(collection: String?, id: String) {
        val DELETE_URI = URI.create(BASE_URL + restheartProperties.collections[collection] + "/" + id)
        val headers: HttpHeaders = httpHeaders

        val req = RequestEntity<Any?>(
                headers,
                HttpMethod.DELETE,
                DELETE_URI)
        val result: ResponseEntity<String> = restTemplate.exchange(req, String::class.java)

        RestHeartClientApi.log.info("RestHeart DELETE StatusCode:" + result.statusCode)
    }
}