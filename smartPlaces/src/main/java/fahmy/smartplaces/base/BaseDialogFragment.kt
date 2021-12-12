package fahmy.smartplaces.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import fahmy.smartplaces.base.helper.handleApiError
import fahmy.smartplaces.base.helper.showInternetMessageError


abstract class BaseActivity : AppCompatActivity(), (Any) -> Unit {
    private lateinit var progressBar: CustomProgressPar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = CustomProgressPar(this)
        setContentView(getInflateView())
        initialComponent()
        getState()?.observe(this) {
            if (it is CommonStates<*>) {
                handleResponse(it) { states ->
                    if (states != null)
                        this(states)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        clicks()

    }

    fun showLoading() {
        progressBar.show()
    }

    fun hideLoading() {
        progressBar.hide()
    }

    private fun cancelLoading() {
        progressBar.dismiss()
        progressBar.cancel()
    }

    fun setCanClickable(canClickable: Boolean) {
        progressBar.setCancelable(canClickable)
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelLoading()

    }

    override fun onStop() {
        super.onStop()
        getState()?.removeObserver(this)

    }

    abstract fun initialComponent()
    abstract fun clicks()
    abstract fun getInflateView(): View
    abstract fun getState(): LiveData<*>?
    private fun handleResponse(response: CommonStates<*>, result: (Any?) -> Unit) {
        when (response) {
            is CommonStates.LoadingShow -> {
                showLoading()
                setCanClickable(false)
            }
            is CommonStates.NoInternet -> {
                hideLoading()
                showInternetMessageError()
            }
            is CommonStates.Error -> {
                hideLoading()
                handleApiError(response.code, response.exp)
            }
            is CommonStates.Success -> {
                hideLoading()
                result(response.data)
            }
            is CommonStates.EmptyState -> {
                hideLoading()
                showEmptyView()
            }
        }
    }

    open fun showEmptyView() {
        hideLoading()
    }

}