package com.example.einkaufsliste.viewmodel

import androidx.lifecycle.ViewModel
import com.example.einkaufsliste.model.models.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel() : ViewModel() {

    private val listStateFlow = MutableStateFlow(ListViewModelState(ShoppingList(1, "")))
    val listViewState = listStateFlow.asStateFlow()
}