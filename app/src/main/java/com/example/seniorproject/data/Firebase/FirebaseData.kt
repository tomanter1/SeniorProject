package com.example.seniorproject.data.Firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import java.util.*


//@Module
//@Suppress("unused")
private const val TAG = "MyLogTag"

class FirebaseData {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

   // var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()
    var savedPosts : PostLiveData = PostLiveData()
    var changed : Boolean = false

    fun CurrentUser() = firebaseAuth.currentUser


    fun logout() = firebaseAuth.signOut()

    fun RegisterUser(username: String, email: String, password: String) =
        Completable.create { emitter ->
            Log.d(TAG, "Entered register user function!!! Email: " + email)
            Log.d(TAG, "pass: " + password)

            //Firebase Authentication is being performed inside the completeable
            //emitter indicated weather the task was completed
            //double check this code
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                            Log.d(TAG, "NEW USER, uid: ${it.result?.user?.uid}")
                            saveUserToFirebaseDatabase(username, email, password)
                        } else {
                            emitter.onError(it.exception!!)
                            //return@addOnCompleteListener
                        }
                    }
                }
        }


    fun LoginUser(email: String, password: String) = Completable.create { emitter ->
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                        Log.d(TAG, "user has logged in")
                        val currentuser = FirebaseAuth.getInstance().currentUser
                        currentuser?.let {
                            val username = currentuser.displayName
                            val email = currentuser.email
                            val uid = currentuser.uid
                            val user = User(username, email, uid)
                        }
                    } else {
                        emitter.onError(it.exception!!)
                        //should not be using two exclaimation points
                    }
                }
            }
    }


    private fun saveUserToFirebaseDatabase(username: String, email: String, password: String) {
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username, email, password)

        ref.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "saving to database worked")
            } else {
                Log.d(TAG, "not saved")
            }
        }.addOnFailureListener {
            Log.d(TAG, "Error ${it.message}")
        }
    }


    fun saveNewPost(postTitle: String, postText: String) {
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
            val post = Post(postTitle, postText)

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
        }
    }

    /*fun getSavedPost(): MutableLiveData<List<Post>> {
        listenforPosts()
        return savedPosts
    }*/
    fun getSavedPost() : PostLiveData{
        listenforPosts()
        return savedPosts
}



    private fun listenforPosts() {
        val reference = FirebaseDatabase.getInstance().getReference("/posts")


        reference.addChildEventListener(object : ChildEventListener {
           var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost = p0.getValue(Post::class.java)

                if (newPost != null) {
                    Log.d("ACCESSING", newPost?.text)
                    savedPostsList.add(newPost)

                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }
                savedPosts.value = savedPostsList

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }



        })

    }
     fun loook()  {
        val reference = FirebaseDatabase.getInstance().getReference("/posts")

        reference.addValueEventListener(object : PostListener
        {
            override fun onStarted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {


            }

            override fun onSuccess() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(message: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }






    companion object {
        @Volatile
        private var instance: FirebaseData? = null

        //rights to this properties are immediate visible ot other threats
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseData().also { instance = it }
            }
        //if the instance is not n
    }

}






