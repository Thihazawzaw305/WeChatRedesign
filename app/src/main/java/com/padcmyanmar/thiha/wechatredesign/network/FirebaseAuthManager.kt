package com.padcmyanmar.thiha.wechatredesign.network

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.padcmyanmar.thiha.wechatredesign.data.vos.passwordVO

object FirebaseAuthManager : AuthManager {

    private val mFirebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()


    override fun login(
        phoneNumber: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
      mFirebaseAuth.signInWithEmailAndPassword("$phoneNumber@gmail.com", password).addOnCompleteListener {
          if(it.isSuccessful && it.isComplete){
              onSuccess()
          } else {
              onFailure(it.exception?.message ?: "Please Check Internet Connection")
          }
      }
    }

    override fun singUp(
        phoneNumber: String,
        password: String,
        userName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mFirebaseAuth.createUserWithEmailAndPassword("$phoneNumber@gmail.com", password).addOnCompleteListener {
            if (it.isSuccessful && it.isComplete) {
                mFirebaseAuth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(userName).build()
                )
                onSuccess()
            } else {
                onFailure(it.exception?.message ?: "Please check internet connection")
            }}
    }

    override fun updateUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = mFirebaseAuth.currentUser
        val newEmail = phone.plus("@gmail.com")



        val authCredential = EmailAuthProvider.getCredential(user?.email.toString(),password)
        user?.reauthenticate(authCredential)
            ?.addOnCompleteListener {
                if(it.isSuccessful && it.isComplete){
                    user.updateEmail(newEmail)
                        .addOnCompleteListener {
                            onSuccess(newEmail)
                        }.addOnFailureListener { error ->
                            onFailure(error.message ?: "unable to change phone number")
                        }
                } else {
                    onFailure(it.exception?.message ?: "Please Check Internet Connection")
                }
            }
    }


}