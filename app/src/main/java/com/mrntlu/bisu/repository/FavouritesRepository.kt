package com.mrntlu.bisu.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mrntlu.bisu.models.response.Article
import com.mrntlu.bisu.util.getAsPureString
import java.util.*
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) {

    //TODO: Generate a UUID on init and save to pref
    private val _currentUID
        get() = auth.currentUser?.uid ?: UUID.randomUUID().toString()

    private val _favCountLiveData: MutableLiveData<Int> = MutableLiveData()
    private lateinit var _valueEventListener: ValueEventListener

    private var query: Query? = null
    private lateinit var childEventListener: ChildEventListener
    private var singleEventListener: ValueEventListener? = null
    private lateinit var newsList: ArrayList<Article>
    private lateinit var newsListData: MutableLiveData<ArrayList<Article>>

    fun setAnonymousUser() {
        auth.signInAnonymously()
    }

    fun addNewFavouriteNews(article: Article) {
        val childUpdates = hashMapOf<String, Any>(
            "$_currentUID/${article.url.getAsPureString()}" to article.toMap()
        )

        db.updateChildren(childUpdates)
    }

    fun deleteFavouriteNews(url: String) {
        db.child(_currentUID).child(url.getAsPureString()).setValue(null)
    }

    fun setFavsCountListener(): LiveData<Int> {
        _valueEventListener = db.child(_currentUID).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _favCountLiveData.postValue(snapshot.childrenCount.toInt())
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        return _favCountLiveData
    }

    fun getAllFavouritesAndSetListener(): LiveData<ArrayList<Article>> {
        var isInitialSetupFinished = false
        query = db.child(_currentUID)

        if (::childEventListener.isInitialized)
            query?.removeEventListener(childEventListener)

        if (!::newsListData.isInitialized) {
            newsListData = MutableLiveData()
            newsList = arrayListOf()
        }

        childEventListener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (isInitialSetupFinished) {
                    val chatListModel = snapshot.getValue(Article::class.java)
                    chatListModel?.let {
                        newsList.add(chatListModel)
                        newsListData.postValue(newsList)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (isInitialSetupFinished) {
                    val chatListModel = snapshot.getValue(Article::class.java)
                    chatListModel?.let {
                        if (newsList.contains(it)){
                            newsList[newsList.indexOf(it)] = it
                            newsListData.postValue(newsList)
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (isInitialSetupFinished) {
                    val chatListModel = snapshot.getValue(Article::class.java)
                    chatListModel?.let {
                        if (newsList.contains(it)){
                            newsList.remove(it)
                            newsListData.postValue(newsList)
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        query?.addChildEventListener(childEventListener)

        singleEventListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isInitialSetupFinished = true
                newsList.clear()
                newsList.addAll(snapshot.children.map {
                    it.getValue(Article::class.java)!!
                })
                newsListData.postValue(newsList)

                query?.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        query?.addListenerForSingleValueEvent(singleEventListener!!)

        return newsListData
    }

    fun removeListeners(owner: LifecycleOwner) {
        if (::_valueEventListener.isInitialized) {
            _currentUID.let {
                db.child(it).removeEventListener(_valueEventListener)
            }
        }

        if (_favCountLiveData.hasObservers()) {
            _favCountLiveData.removeObservers(owner)
        }
    }

    fun onDestroy(){
        query?.removeEventListener(childEventListener)
        query = null
        singleEventListener = null
    }
}