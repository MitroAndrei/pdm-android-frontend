package com.example.myapp.coreimport com.google.gson.GsonBuilderimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryobject Api {    private val URL = "http://192.168.0.150:3000/"//    private val URL = "http://172.30.115.75/"    private var gson = GsonBuilder().create()    val retrofit = Retrofit.Builder()        .baseUrl(URL)        .addConverterFactory(GsonConverterFactory.create(gson))        .build()}