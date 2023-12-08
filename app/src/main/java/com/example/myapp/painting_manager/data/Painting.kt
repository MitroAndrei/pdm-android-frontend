package com.example.myapp.painting_manager.data


data class Painting(val _id: String? = null,
                    val title: String = "",
                    val value: Int = 0,
//                val date: LocalDate = LocalDate.now(),
                    val date: String = "",
                    val forSale: Boolean = false
    )
