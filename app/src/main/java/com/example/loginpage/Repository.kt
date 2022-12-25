package com.example.loginpage

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import android.os.AsyncTask



class Repository(private val messsDao: MessageDAO) {

    val allWords: LiveData<List<localMessages>> = messsDao.getAll()

    @WorkerThread
    suspend fun insert(local: localMessages) {
        messsDao.insert(local)
    }
    fun getMess(name1:String,name2: String) : LiveData<List<localMessages>>{
        return messsDao.getAllMess(name1,name2)
    }
    fun getLastMess() : LiveData<List<localMessages>>{
        return messsDao.getLast1Mess()
    }
    /* --------------- BORRAR TODOS LOS DATOS -------------- */

//    fun deleteAll() {
//        deleteAllWordsAsyncTask(wordDao).execute()
//    }
//
//    private class deleteAllWordsAsyncTask internal constructor(private val mAsyncTaskDao: WordDao) :
//        AsyncTask<Void, Void, Void>() {
//
//        override fun doInBackground(vararg voids: Void): Void? {
//            mAsyncTaskDao.deleteAll()
//            return null
//        }
//    }

    /* ---------------- BORRAR UN SOLO DATO ---------------- */

//    fun deleteWord(word: Word) {
//        deleteWordAsyncTask(wordDao).execute(word)
//    }
//
//    private class deleteWordAsyncTask internal constructor(private val mAsyncTaskDao: WordDao) :
//        AsyncTask<Word, Void, Void>() {
//
//        override fun doInBackground(vararg params: Word): Void? {
//            mAsyncTaskDao.deleteWord(params[0])
//            return null
//        }
//    }

    /* -------------- ACTUALIZAR UN SOLO DATO ---------------- */

//    fun update(word: Word) {
//        updateWordAsyncTask(wordDao).execute(word)
//    }
//
//    private class updateWordAsyncTask internal constructor(private val mAsyncTaskDao: WordDao) :
//        AsyncTask<Word, Void, Void>() {
//        override fun doInBackground(vararg params: Word?): Void? {
//            mAsyncTaskDao.update(params[0]!!)
//            return null
//        }
//    }
}