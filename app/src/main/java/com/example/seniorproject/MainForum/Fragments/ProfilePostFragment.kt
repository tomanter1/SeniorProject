package com.example.seniorproject.MainForum.Fragments


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.MainForum.Posts.UpdatePost
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.databinding.FragmentProfilePostBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import javax.inject.Inject


class ProfilePostFragment : Fragment() {

    private lateinit var adapter: CustomAdapter
    private lateinit var deleteIcon: Drawable

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val currentuser = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
        var iD = this.arguments?.getString("ID") ?: "null"
        if (iD == "null"){
            iD = currentuser
        }

        //initalization of the viewmodel and dagger app component
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        //initialization of binding variable, binded variables are located in the corresponding XML file
        val binding: FragmentProfilePostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile__post, container, false)
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile__post, container, false)

        //making sure id never equals null to avoid errors
        if (myViewModel.getUserProfilePosts(iD).value == null){
            iD = "0"
        }

        //setting adapter
        view.profile_post_recyclerView.adapter = CustomAdapter(view.context, myViewModel.getUserProfilePosts(iD),0)

        adapter = CustomAdapter(view.context, myViewModel.returnProfilePost(),0)
        view.profile_post_recyclerView.adapter= adapter

        //refreshview UI
        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))

        view.refreshView.setOnRefreshListener {
            view.profile_post_recyclerView.adapter =
                CustomAdapter(view.context, myViewModel.returnProfilePost(),0)
            view.refreshView.isRefreshing = false
        }


        binding.profViewModel = myViewModel
        binding.lifecycleOwner = this

       //ordering the posts from newest to latest
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_post_recyclerView.layoutManager = linearLayoutManager
        view.profile_post_recyclerView.adapter =
            CustomAdapter(
                view.context,
                myViewModel.getUserProfilePosts(iD),
                0
            )

        //delete icon for swipe UI
        deleteIcon = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.ic_delete_24px)!!
        binding.executePendingBindings()

        //checking if a user has no posts, the no post message is not swipable but all other posts are
        myViewModel.noPostsChecker(FirebaseAuth.getInstance().currentUser?.uid ?: "null", object : CheckCallback{
            override fun check(chk: Boolean) {
                if (iD != FirebaseAuth.getInstance().currentUser?.uid || chk){
                    Log.d("wrong","one")
                }
                else if (!chk) {
                    //setting swipe UI
                    object : SwipeHelper(context!!, view.profile_post_recyclerView, 200) {
                        override fun initButton(
                            viewHolders: RecyclerView.ViewHolder,
                            buffer: MutableList<ProfileButton>
                        ) {
                            buffer.add(
                                ProfileButton(context!!, "Delete", 30, 0, Color.parseColor
                                    ("#FF0000"), object : ButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        val postkey: String? =
                                            adapter.removeItem(viewHolders as CustomViewHolders,pos)

                                        val userkey: String? =
                                            adapter.getUserKey(viewHolders)

                                        val crnkey: String? =
                                            adapter.getCrn(viewHolders,pos)

                                        //var builder = AlertDialog.Builder(activity!!.baseContext, R.style.AppTheme_AlertDialog)
                                        val builder = AlertDialog.Builder(
                                            view.context,
                                            R.style.AppTheme_AlertDialog
                                        )

                                        //.getStringExtra("Classkey")
                                        //val postkey = intent.getStringExtra("author")
                                        //myViewModel.deletePost(postkey!!, className)
                                        //myViewModel.deletePost()
                                        builder.setTitle("Are you sure?")
                                        builder.setMessage("You cannot restore posts that have been deleted.")
                                        builder.setPositiveButton("DELETE",
                                            { dialogInterface: DialogInterface?, i: Int ->
                                                myViewModel.deletePost(postkey!!, crnkey!!, userkey!!)
                                            })
                                        builder.setNegativeButton("CANCEL",
                                            { dialogInterface: DialogInterface?, i: Int ->
                                                builder.setCancelable(true)
                                            })

                                        val msgdialog: AlertDialog = builder.create()

                                        msgdialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                        msgdialog.show()

                                    }

                                })
                            )

                            buffer.add(
                                ProfileButton(context!!, "Edit", 30, 0, Color.parseColor
                                    ("#D3D3D3"), object : ButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        val intent = Intent(context, UpdatePost::class.java)

                                        val postkey: String? =
                                            adapter.removeItem(viewHolders as CustomViewHolders,pos)

                                        val userkey: String? =
                                            adapter.getUserKey(viewHolders)

                                        val crnkey: String? =
                                            adapter.getCrn(viewHolders,pos)

                                        val titlekey: String? =
                                            adapter.getTitle(viewHolders,pos)

                                        val textkey: String? =
                                            adapter.getText(viewHolders,pos)

                                        intent.putExtra("crn", crnkey)
                                        intent.putExtra("Classkey", postkey)
                                        intent.putExtra("UserID", userkey)
                                        intent.putExtra("title", titlekey)
                                        intent.putExtra("text", textkey)

                                        context!!.startActivity(intent)
                                    }

                                })
                            )

                        }
                    }
                }

            }

        })





        binding.executePendingBindings()
        return view
    }

    override fun onPause() {
        super.onPause()

    }
    companion object {
        fun newInstance(ID: String): ProfilePostFragment {
            val bundle : Bundle = Bundle()
            bundle.putString("ID",ID)
            val fragment = ProfilePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
