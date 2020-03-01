package com.example.seniorproject.MainForum

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import kotlinx.android.synthetic.main.comment_rv.view.*
import kotlinx.android.synthetic.main.post_rv.view.*

class CommentsAdapter(context: Context, var Comments: CommentLive?) :
    RecyclerView.Adapter<CustomViewHolders>() {
    val mContext: Context = context
    private val TYPE_HEADER: Int = 0
    private val TYPE_LIST: Int = 1


    override fun getItemViewType(position: Int): Int {
        if(position == 0)
        {
            return TYPE_HEADER
        }
        return TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolders {

        if(viewType == 0){
            val layoutInflater = LayoutInflater.from(parent.context)
            // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            val cellForRow = layoutInflater.inflate(R.layout.header_rv, parent, false)
            return CustomViewHolderHeader(cellForRow)
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.comment_rv, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (Comments?.value != null)
            return Comments?.value!!.size + 1
        else
            return 0
    }

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {

        if (Comments?.value == null) {
            holder.itemView.comment_text.text = "No Comments yet"

        } else {
            val comment: Comment = Comments?.value!![position]
            //holder.itemView.post_title.text = .title
            holder.itemView.comment_text.text = comment.text
            holder.itemView.authcom.text = comment.author

            /* holder.itemView.setOnClickListener {
                 val intent = Intent(mContext, ClickedPost::class.java)
                 intent.putExtra("Title", post.title)
                 intent.putExtra("Text", post.text)
                 intent.putExtra("Key", post.Classkey)
                 mContext.startActivity(intent)
             }*/

        }


    }

}

class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}
}
