package com.github.rovkinmax.githubclientmvc.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Handler
import com.github.rovkinmax.githubclientmvc.account.AuthCallback
import com.github.rovkinmax.githubclientmvc.account.AuthDelegate
import com.github.rovkinmax.githubclientmvc.view.LoginActivity
import java.util.concurrent.TimeUnit

/**
 * @author Rovkin Max
 */
open class SplashPresenter(private val context: Context) {
    private val accountManager: AccountManager;
    private val authDelegate: AuthDelegate
    private val handler = Handler()
    private val callback = Runnable { continueWithAccount() }

    init {
        accountManager = AccountManager.get(context)
        authDelegate = AuthDelegate(context)
    }

    fun checkAndContinue() {
        val account = accountManager.accounts.firstOrNull()
        if (account == null) continueWithoutAccount()
        else {
            handler.postDelayed(callback, TimeUnit.SECONDS.toMillis(10))
            authDelegate.checkAuth(account, object : AuthCallback {
                override fun onAuthSuccessful(account: Account, token: String) {
                    continueWithAccount()
                }

                override fun onAuthFailed() {
                    continueWithoutAccount()
                }
            })
        }
    }

    open fun continueWithAccount() {
        removeCallback()
    }

    open fun continueWithoutAccount() {
        removeCallback()
        context.startActivity(LoginActivity.buildIntent(context))
    }

    private fun removeCallback() {
        handler.removeCallbacksAndMessages(callback)
    }
}