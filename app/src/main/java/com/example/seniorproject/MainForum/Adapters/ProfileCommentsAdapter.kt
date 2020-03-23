package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.R
import com.example.seniorproject.Utils.Callback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.rv_post.view.*
import kotlinx.android.synthetic.main.rv_post_comment.view.*

class ProfileCommentsAdapter(context: Context, var ProfileComments: CommentLive) :
    RecyclerView.Adapter<CustomViewHolders>() {
    val mContext:Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post_comment, parent, false)
        getItemCount()
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (ProfileComments.value != null)
            return ProfileComments.value!!.size
        else
            return 0
    }

   /* fun readPostValues(crn: String, postkey: String, callBack : Callback){
        FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$postkey/text").addListenerForSingleValueEvent( object :
            ValueEventListener {
            override fun onDataChange(p0: DataSnapshot){
                if(p0.exists()){
                    var ptext = p0.getValue().toString()
                    callBack.onCallback(ptext)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }*/

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {
        if (ProfileComments.value == null || getItemCount() == 0) {
                holder.itemView.comment_text.text = "No Comments yet"
                //this is not showing up
        } else {
            val comment: Comment = ProfileComments.value!![position]
            holder.itemView.comment_text.text = comment.text
            holder.itemView.authcom.text=comment.crn
            var size: Float= 12F
            holder.itemView.authcom.textSize=size
            holder.itemView.comment_timestamp.text=comment.Ptime
            //holder.itemView.username.text = post.author

            holder.itemView.authcom.setOnClickListener{
                val intent = Intent(mContext, CommunityPosts::class.java)
                intent.putExtra("ClassName", comment.crn)
                mContext.startActivity(intent)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(mContext, ClickedPost::class.java)
                var crn= comment.crn
                var postkey = comment.Postkey
                var callback: Callback? = null

                FirebaseData.getInstance().readPostValues(crn!!, postkey!!, object: Callback{
                    override fun onCallback(value: ArrayList<String>){
                        Log.d("spider", value[0])
                        intent.putExtra("Text",value[1])
                        Log.d("spider", "HELLO")

                        intent.putExtra("Title", value[0])
                        intent.putExtra("Pkey", value[2])
                        intent.putExtra("Classkey", value[4])
                        intent.putExtra("UserID", value[5])
                        intent.putExtra("Author", value[6])
                        intent.putExtra("subject", crn)
                        intent.putExtra("Ptime", value[3])
                        mContext.startActivity(intent)

                    }

                })
            }

        }

        //val mContext: Context = context
    }
    



    //crn same
    /*intent.putExtra("Title", post.title) //NEED
    intent.putExtra("Text", post.text) //NEED
    intent.putExtra("Pkey", post.key) //postkey, post key
    intent.putExtra("Classkey", post.Classkey) //class key, same
    intent.putExtra("UserID", post.UserID)  //posterID = post author's uid
    intent.putExtra("Author", post.author) //posterID = post author's uid
    intent.putExtra("crn", post.crn)*/ //same

    fun getCommentKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.UserComkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getUserKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.PosterID

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getClassKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.Classkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getClassProfileKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.ProfileComKey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun pkeyUserProfile(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.Postkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getCrn(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.crn

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getText(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.text

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }



}