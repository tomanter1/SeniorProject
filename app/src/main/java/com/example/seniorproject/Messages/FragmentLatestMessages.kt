package com.example.seniorproject.Messages

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.Nullable
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.R
import javax.inject.Inject
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.MainForum.Adapters.LatestMessageAdapter
import com.example.seniorproject.data.models.LatestMessage
import com.example.seniorproject.viewModels.MessagesFragmentViewModel
import kotlinx.android.synthetic.main.m_fragment_latest_messages.*
import kotlinx.android.synthetic.main.m_fragment_latest_messages.view.recyclerView_latest_messages

class FragmentLatestMessages : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: MessagesFragmentViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        activity?.title = "Messages"
        val view = inflater.inflate(R.layout.m_fragment_latest_messages, container, false)
        val factory = InjectorUtils.provideMessagesFragmentViewModelFactory()
        myViewModel = ViewModelProviders.of(this, factory).get(MessagesFragmentViewModel::class.java)

        view.recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages().observe(this, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                view.recyclerView_latest_messages.adapter?.notifyDataSetChanged()
                view.recyclerView_latest_messages.adapter  = LatestMessageAdapter(view.context, messages)
            }
        })


        return view

    }

    override fun onResume() {
        super.onResume()


        recyclerView_latest_messages.layoutManager = LinearLayoutManager(context)

        myViewModel.getRecentMessages().observe(this, object : Observer<List<LatestMessage>> {
            override
            fun onChanged(@Nullable messages: List<LatestMessage>) {
                recyclerView_latest_messages.adapter  =
                    context?.let { LatestMessageAdapter(it, messages) }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.fragment_new_message_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.newMessage -> {
                val intent = Intent(context, NewMessage::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}