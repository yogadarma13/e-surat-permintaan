package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataProfile
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.viewmodel.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class LoginFragment : Fragment() {

    val profileViewModel: ProfileViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()
    val profilePreference: ProfilePreference by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordSeek.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        btnSignin.setOnClickListener {
            authViewModel
                .doLogin(Login(etEmail.text.toString(), etPassword.text.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::loginResponse, this::handleError)
        }
    }

    private fun loginResponse(response: LoginResponse) {
        val dataLogin = response.dataLogin

        profilePreference.saveProfile(
            DataProfile(
                id = dataLogin?.id,
                name = dataLogin?.name,
                email = dataLogin?.email
            )
        )

        view?.findNavController()?.navigate(R.id.action_loginFragment_to_mainFragment)

        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

//        dataLogin?.id?.let {
//            profileViewModel.getProfile(it)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(
//                    {
//                        var dataProfile: DataProfile? = DataProfile()
//
//                        it.data?.forEach {
//                            dataProfile = it
//                        }
//
//                        if (dataProfile != null){
//                            profilePreference.saveProfile(dataProfile)
//                        } else {
//                            Toast.makeText(context, "Terjadi kesalahan saat mengambil data profile", Toast.LENGTH_LONG).show()
//                        }
//                    },
//                    {
//                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
//                    }
//                )
//        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
