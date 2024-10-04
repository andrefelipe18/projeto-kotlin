package com.example.p2.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _manutencaoDeleted = MutableLiveData<Boolean>()
    val manutencaoDeleted: LiveData<Boolean> get() = _manutencaoDeleted

    private val _manutencaoUpdated = MutableLiveData<Boolean>()
    val manutencaoUpdated: LiveData<Boolean> get() = _manutencaoUpdated

    fun setManutencaoDeleted(deleted: Boolean) {
        _manutencaoDeleted.value = deleted
    }

    fun setManutencaoUpdated(updated: Boolean) {
        _manutencaoUpdated.value = updated
    }
}