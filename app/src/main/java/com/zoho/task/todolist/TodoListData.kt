package com.zoho.task.todolist


// Data class for the entire JSON response
data class TodoListData(
    val todos: List<TodoItem>,
    val total: Int,
    val skip: Int,
    val limit: Int
) {

    // Data class for each todo item
    data class TodoItem(
        val id: Int,
        val todo: String,
        val completed: Boolean,
        val userId: Int
    )
}
