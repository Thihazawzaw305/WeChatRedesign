package com.padcmyanmar.thiha.wechatredesign.mvp.views


 interface SignUpView: BaseView {
     fun navigateToSetUpProfile()
     fun navigateToGetOTPScreen()
     fun showLoadingDialog()
     fun dismissLoadingDialog()
}