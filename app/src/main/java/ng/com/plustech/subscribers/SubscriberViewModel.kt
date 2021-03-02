package ng.com.plustech.subscribers

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ng.com.plustech.subscribers.database.Subscriber

class SubscriberViewModel(private  val repository: SubscriberRepository): ViewModel(), Observable {

    val subscribers = repository.subscribers
    private  var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if(isUpdateOrDelete) {
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        } else {
            //!! means it must not be null
            val name: String = inputName.value!!
            val email: String = inputEmail.value!!

            //room will ignore the 0 in id and use auto_increment instead
            insert(Subscriber(0, name, email))

            //unset name and email
            inputName.value = null
            inputEmail.value = null
        }
    }

    fun clearAllOrDelete() {
        if(isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            deleteAll()
        }
    }

    fun insert(subscriber: Subscriber) {
        //background thread in viewmodel through viewmodelscope
        viewModelScope.launch {
            val newRowId:Long = repository.insert(subscriber)
            if(newRowId > -1) {
                statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
            }else {
                statusMessage.value = Event("Error Occurred.")
            }
        }
    }

    //another way to write it in one line
    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

        statusMessage.value = Event("Subscriber Updated Successfully")
    }

    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

        statusMessage.value = Event("Subscriber Deleted Successfully")
    }

    fun deleteAll(): Job = viewModelScope.launch {
        repository.deleteAll()

        statusMessage.value = Event("All Subscribers Deleted Successfully")
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}