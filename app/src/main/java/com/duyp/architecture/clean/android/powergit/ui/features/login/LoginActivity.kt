package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.duyp.architecture.clean.android.powergit.*
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity
import com.duyp.architecture.clean.android.powergit.ui.features.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : ViewModelActivity<LoginViewState, LoginIntent, LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnLogin.setOnClickListener { login() }
        edtPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                true
            } else false
        }
        tvSkip.setOnClickListener { navigateMain() }

        withState {
            setLoading(isLoading)
            event(errorMessage) {
                showToastMessage(this)
            }
            event(loginSuccess) {
                showToastMessage("login success!")
                hideKeyboard()
                navigateMain()
            }
            event(lastLoggedInUsername) {
                edtUsername.setText(this)
                edtPassword.requestFocus()
                showKeyboard(edtPassword)
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    override fun canBack() = false

    private fun login() {
        onIntent(LoginIntent(edtUsername.text.toString(), edtPassword.text.toString()))
    }

    private fun setLoading(isLoading: Boolean) {
        btnLogin.visibility = if (!isLoading) View.VISIBLE else View.GONE
        progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateMain() {
        MainActivity.start(this@LoginActivity)
        finish()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}