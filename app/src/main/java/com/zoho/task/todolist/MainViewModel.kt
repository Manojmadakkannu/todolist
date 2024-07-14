package com.zoho.task.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _employees = MutableLiveData<TodoListData>()
    val employees: LiveData<TodoListData> = _employees

    fun fetchTodos() {
        viewModelScope.launch {
            try {
                val list = mainRepository.list()
                _employees.value = list
            } catch (e: Exception) {
                e.printStackTrace() // Handle errors appropriately
            }
        }
    }


}