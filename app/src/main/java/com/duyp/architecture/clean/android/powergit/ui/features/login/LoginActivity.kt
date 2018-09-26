package com.duyp.architecture.clean.android.powergit.ui.features.login

import android.os.Bundle
import android.widget.Toast
import com.duyp.architecture.clean.android.powergit.EventObserver
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.databinding.ActivityLoginBinding
import com.duyp.architecture.clean.android.powergit.ui.base.ViewModelActivity

class LoginActivity : ViewModelActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel

        mBinding.btnLogin.setOnClickListener {
            mViewModel.login(mBinding.edtUsername.text.toString(), mBinding.edtPassword.text.toString())
        }

        mViewModel.errorMessage.observe(this, EventObserver { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })

        mViewModel.loginSuccess.observe(this, EventObserver {
            Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show()
        })
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login;
    }

}