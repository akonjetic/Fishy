package hr.konjetic.fishy.network

import hr.konjetic.fishy.network.model.FishyAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class Network {

    private val fishyAPIService: FishyAPIService
    private val baseUrl = "http://10.6.145.104:5072/"

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager).hostnameVerifier { _, _ -> true }

        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()

        fishyAPIService = retrofit.create(FishyAPIService::class.java)
    }

    fun getService() : FishyAPIService = fishyAPIService
}