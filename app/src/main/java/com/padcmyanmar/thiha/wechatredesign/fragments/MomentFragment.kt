package com.padcmyanmar.thiha.wechatredesign.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.padcmyanmar.thiha.wechatredesign.adapter.MomentRecyclerViewAdapter
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.activities.CreateMomentActivity
import com.padcmyanmar.thiha.wechatredesign.activities.LikePeopleActivity
import com.padcmyanmar.thiha.wechatredesign.data.vos.MomentVO
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.MomentPresenter
import com.padcmyanmar.thiha.wechatredesign.mvp.presenters.MomentPresenterImpl
import com.padcmyanmar.thiha.wechatredesign.mvp.views.MomentView
import kotlinx.android.synthetic.main.fragment_moment.*

class MomentFragment : Fragment(),MomentView{
    private lateinit var mAdapter: MomentRecyclerViewAdapter
    private lateinit var mPresenter: MomentPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPresenter()
        setUpRecyclerView()

        mPresenter.onUiReady(this)
        setUpListeners()
    }

    private fun setUpListeners(){
        btnCreateMoment.setOnClickListener {
            mPresenter.onTapCreateMoment()


        }
    }


    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[MomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView(){
        mAdapter = MomentRecyclerViewAdapter(mPresenter)
        rvPost.adapter = mAdapter
        rvPost.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    }

    override fun showPost(momentList: List<MomentVO>) {
       mAdapter.setNewData(momentList)

    }

    override fun navigateToCreateMoment() {
      val intent = Intent(requireContext(),CreateMomentActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToLikePeople(millis: String) {
      startActivity(LikePeopleActivity.newIntent(requireContext(),millis))
    }

    override fun showError(error: String) {

    }


}