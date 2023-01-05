package com.padcmyanmar.thiha.wechatredesign.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.padcmyanmar.thiha.wechatredesign.R
import com.padcmyanmar.thiha.wechatredesign.adapter.LikePeopleRecyclerViewAdapter
import com.padcmyanmar.thiha.wechatredesign.data.vos.UserVO
import com.padcmyanmar.thiha.wechatredesign.network.CloudFirestoreFirebaseApiImpl
import kotlinx.android.synthetic.main.activity_like_people.*

class LikePeopleActivity : AppCompatActivity() {

    private lateinit var mAdapter: LikePeopleRecyclerViewAdapter

    companion object {
        const val Millis = "Millis"
        fun newIntent(context: Context, millis: String): Intent {
            val intent = Intent(context, LikePeopleActivity::class.java)
            intent.putExtra(Millis, millis)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_people)
        setUpRecyclerView()
        test()
    }


    private fun setUpRecyclerView() {
        mAdapter = LikePeopleRecyclerViewAdapter()
        rvLikePeople.adapter = mAdapter
        rvLikePeople.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    fun test() {

        val millis = intent.getStringExtra(Millis)
        if (millis != null) {
            CloudFirestoreFirebaseApiImpl.getLikePeople(millis, onSuccess = {
                mAdapter.setNewData(it)
            }, onFailure = {

            })
        }

    }
}