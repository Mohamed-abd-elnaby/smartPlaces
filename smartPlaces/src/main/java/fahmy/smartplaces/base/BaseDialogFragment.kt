package fahmy.smartplaces.base

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import fahmy.smartplaces.base.helper.handleApiError
import fahmy.smartplaces.base.helper.showInternetMessageError


abstract class BaseDialogFragment() :
    DialogFragment() {
    private var mView: View? = null

    private lateinit var progressBar: CustomProgressPar


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mView != null) {
            val parent = mView?.parent as ViewGroup
            parent.removeView(mView)
        }
        try {
            mView = inflater.inflate(getInflateView(), container, true)
            progressBar = CustomProgressPar(requireContext())
            initialComponent()
            initialObserve()
            clicks()
        } catch (e: InflateException) {
            /* map is already there, just return view as it is */
        }

        return mView
    }

    override fun onResume() {
        super.onResume()

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
        removeObserve()

    }

    abstract fun initialComponent()
    abstract fun clicks()
    abstract fun getInflateView(): Int
    abstract fun initialObserve()
    abstract fun removeObserve()
    fun handleResponse(response: CommonStates<*>, result: (Any?) -> Unit) {
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