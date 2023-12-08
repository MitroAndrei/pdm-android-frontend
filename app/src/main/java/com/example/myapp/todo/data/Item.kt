package com.example.myapp.todo.data


data class Item(val _id: String? = null,
                val title: String = "",
                val value: Int = 0,
//                val date: LocalDate = LocalDate.now(),
                val date: String = "",
                val forSale: Boolean = false
    )
