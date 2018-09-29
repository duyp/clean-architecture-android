package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.event
import com.duyp.architecture.clean.android.powergit.showToastMessage
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

        withState {
            setLoading(isLoading)
            event(errorMessage) {
                showToastMessage(this)
            }
            event(loginSuccess) {
                MainActivity.start(this@LoginActivity)
                showToastMessage("login success!")
                finish()
            }
            event(lastLoggedInUsername) {
                edtUsername.setText(this)
                edtPassword.requestFocus()
            }
        }
    }

    private fun login() {
        onIntent(LoginIntent(edtUsername.text.toString(), edtPassword.text.toString()))
    }

    private fun setLoading(isLoading: Boolean) {
        btnLogin.visibility = if (!isLoading) View.VISIBLE else View.GONE
        progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}