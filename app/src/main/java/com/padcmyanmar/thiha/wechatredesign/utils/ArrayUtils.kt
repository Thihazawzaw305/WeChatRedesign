package com.padcmyanmar.thiha.wechatredesign.utils

import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactGroupVO
import com.padcmyanmar.thiha.wechatredesign.data.vos.ContactVO


fun List<ContactVO>.toContactGroupList() : List<ContactGroupVO>{
    var symbolList : ArrayList<String> = arrayListOf()
    val contactGroupList: ArrayList<ContactGroupVO> = arrayListOf()

    this.map { contact ->
        symbolList.add(contact.name.first().toString())
    }

    symbolList = ArrayList(symbolList.toSet().sorted())

    symbolList.forEach { symbol ->
        contactGroupList.add(
            ContactGroupVO(
                symbol = symbol,
                contactList = this.filter { it.name.startsWith(symbol) }
            )
        )
    }

    return contactGroupList
}