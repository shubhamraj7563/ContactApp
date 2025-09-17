package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.Contact
import com.example.myapplication.data.database.ContactDataBase
import dagger.hilt.android.lifecycle.HiltViewModel // Added import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Changed from @HiltAndroidApp
class ContactViewModel @Inject constructor(var database: ContactDataBase) : ViewModel() {
   private var isSortedByName = MutableStateFlow(true)
    private var  contact = isSortedByName.flatMapLatest{
        if (it) {
            database.getDao().getContactsSortedByName()
        }
        else{
            database.getDao().getContactsSortedByDate()
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    val _state = MutableStateFlow(ContactState())
    val state = combine(_state,isSortedByName,contact){
        state,isSortedByName,contacts ->
        state.copy(
            contacts = contacts
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())


    fun changeisSorting(){
        isSortedByName.value = !isSortedByName.value

    }

    fun saveContact(){
        val contact = Contact(
            id = _state.value.id.value,
            name = _state.value.name.value,
            phone = _state.value.phone.value,
            email = _state.value.email.value,
            isActive = true,
            dateofCreation = System.currentTimeMillis().toLong(),
            image = _state.value.image.value

        )
        viewModelScope.launch {
            database.getDao().upsertContact(contact)
        }
        state.value.id.value = 0
        state.value.name.value = ""
        state.value.phone.value = ""
        state.value.email.value = ""
        state.value.image.value = null

    }

    fun deleteContact(){
        val contact = Contact(
            id = _state.value.id.value,
            name = _state.value.name.value,
            phone = _state.value.phone.value,
            email = _state.value.email.value,
            isActive = true,
            dateofCreation = System.currentTimeMillis().toLong(),
            image = _state.value.image.value
        )
        viewModelScope.launch {
            database.getDao().deleteContact(contact)
        }
        state.value.id.value = 0
        state.value.name.value = ""
        state.value.phone.value = ""
        state.value.email.value = ""
        state.value.image.value = null
        state.value.dateOfCreation.value = 0
    }

    }
