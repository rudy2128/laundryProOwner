package com.anthony.laundrypro.entity

import java.sql.Time

class Product(
    var codeProduct:String? = null,
    var name:String? = null,
    var price:Int ? = null,
    var time: Int? = null,
    var satuan: String? = null,
    var minimalOrder:String? = null
)