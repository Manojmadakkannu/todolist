package com.zoho.task.todolist

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("todos")
    suspend fun list(): TodoListData
}