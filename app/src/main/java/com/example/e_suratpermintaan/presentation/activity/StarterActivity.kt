package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterUOMResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StarterActivity : AppCompatActivity() {

    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)

        initApiRequest()
    }

    private fun initApiRequest() {
        masterViewModel.getCostCodeList("all").subscribe(this::handleResponse, this::handleError)
        masterViewModel.getUomList("all").subscribe(this::handleResponse, this::handleError)
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is MasterCCResponse -> {
                sharedViewModel.setCostCodeList(response.data)
            }

            is MasterUOMResponse -> {
                sharedViewModel.setUomList(response.data)
            }
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(applicationContext, "Error master : " + error.message, Toast.LENGTH_LONG)
            .show()
    }

    override fun finish() {
        super.finish()
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#apply_pop_animations_to_activity_transitions
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

}
