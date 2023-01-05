package com.padcmyanmar.thiha.wechatredesign.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.activities.ChatRoomActivity
import com.padcmyanmar.thiha.wechatredesign.adapter.ActiveChatAdapter
import com.padcmyanmar.thiha.wechatredesign.adapter.ActiveUserAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.ChatPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.ChatPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.ChatView
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment(), ChatView {

    lateinit var mActiveUserAdapter : ActiveUserAdapter
    lateinit var mActiveChatAdapter: ActiveChatAdapter
    lateinit var mPresenter: ChatPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpRecyclerView(view)

        mPresenter.onUiReady(this)
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[ChatPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView(view: View?){
        mActiveUserAdapter = ActiveUserAdapter(mPresenter)
        view?.rvActiveUser?.adapter = mActiveUserAdapter

        mActiveChatAdapter = ActiveChatAdapter(mPresenter)
        view?.rvActiveChat?.adapter = mActiveChatAdapter
    }

    override fun bindContacts(contactList: List<ContactVO>) {
        Log.d("contactList",contactList.toString())
        mActiveUserAdapter.setNewData(contactList)
    }

    override fun bindLastMessage(contactList: List<ContactVO>) {
        Log.d("latestMessageList",contactList.toString())
        mActiveChatAdapter.setNewData(contactList)
    }

    override fun navigateToChatRoomScreen(contactVO: ContactVO) {
        val intent = ChatRoomActivity.getIntent(requireContext())
        ChatRoomActivity.mContact = contactVO
        ChatRoomActivity.isGroup = contactVO.isGroup
        startActivity(intent)
    }

    override fun showError(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}