package com.zoho.task.todolist

class MainRepository {
 private val employeeservice = ApiClient.apis

    suspend fun list() = employeeservice.list()

}