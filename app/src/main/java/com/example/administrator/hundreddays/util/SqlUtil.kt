package com.example.administrator.hundreddays.util

fun getSql(tableName:String,maps:List<Pair<String,String>>):String{

    val builder = StringBuilder("select from $tableName ")

    if(maps.isNotEmpty()){
        builder.append("where ")
        for((key,value) in maps){
            builder.append("$key=$value and ")
        }
    }

    builder.delete(builder.length-4,builder.length-1)

    return builder.toString()
}