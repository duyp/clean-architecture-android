package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.os.Bundle
import android.view.View
import com.duyp.architecture.clean.android.powergit.NonNullObserver
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.showToastMessage
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : ViewModelActivity<LoginViewState, LoginIntent, LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnLogin.setOnClickListener {
            intent(LoginIntent(edtUsername.text.toString(), edtPassword.text.toString()))
        }

        mViewModel.state.observe(this, NonNullObserver { state ->
            setLoading(state.isLoading)
            showToastMessage(state.errorMessage)
            state.loginSuccess?.getContentIfNotHandled()?.let {
                // todo navigate main screen
                showToastMessage("login success!")
            }
        })
    }

    private fun setLoading(isLoading: Boolean) {
        btnLogin.visibility = if (!isLoading) View.VISIBLE else View.GONE
        progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

}