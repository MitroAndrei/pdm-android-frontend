package com.example.myapp.core.data.remoteimport com.google.gson.GsonBuilderimport okhttp3.OkHttpClientimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryobject Api {//    private val url = "192.168.0.150:3000"    private val url = "192.168.56.1:3000"    private val httpUrl = "http://$url/"    val wsUrl = "ws://$url"    private var gson = GsonBuilder().create()    val tokenInterceptor = TokenInterceptor()    val okHttpClient = OkHttpClient.Builder().apply {        this.addInterceptor(tokenInterceptor)    }.build()    val retrofit = Retrofit.Builder()        .baseUrl(httpUrl)        .addConverterFactory(GsonConverterFactory.create(gson))//        .client(okHttpClient) // does not work in android with the latest libs        .build()}