package com.zhulin.fakedet.data.helpers

import android.content.Context
import com.zhulin.fakedet.R
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object ChannelHelpers {
    fun createGrpcChannel(context: Context, host: String?, port: Int): ManagedChannel {
        val certInputStream: InputStream = context.resources.openRawResource(R.raw.certificate)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry(
                "cert",
                CertificateFactory.getInstance("X.509").generateCertificate(certInputStream)
            )
        }

        val hostnameVerifier = HostnameVerifier { _, _ -> true }

        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, trustManagerFactory.trustManagers, null)
        }

        return OkHttpChannelBuilder
            .forAddress(host, port)
            .sslSocketFactory(sslContext.socketFactory)
            .hostnameVerifier(hostnameVerifier)
            .build()
    }
}