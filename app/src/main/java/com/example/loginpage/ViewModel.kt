package com.example.loginpage


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: Repository
//    val allWords: LiveData<List<localMessages>>

    init {
        val messdao = AppDatabase.getDatabase(application, scope).messsDao()
        repository = Repository(messdao)
//        allWords = repository.allWords
    }

    fun insert(localMessages: localMessages) = scope.launch(Dispatchers.IO) {
        repository.insert(localMessages)
    }
    fun getmess(name1:String,name2: String) : LiveData<List<localMessages>>{
        return repository.getMess(name1,name2)
    }
    fun getLastmess() : LiveData<List<localMessages>>{
        return repository.getLastMess()
    }
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}