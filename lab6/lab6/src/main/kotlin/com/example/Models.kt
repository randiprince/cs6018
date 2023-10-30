package com.example

import org.jetbrains.exposed.dao.id.IntIdTable

object Post: IntIdTable(){
    val postText = varchar("text", 150).uniqueIndex()
    val timeStamp = long("timeStamp")
}