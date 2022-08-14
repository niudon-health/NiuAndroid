package edu.stanford.cardinalkit.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.stanford.cardinalkit.common.Constants
import edu.stanford.cardinalkit.domain.models.Response
import edu.stanford.cardinalkit.domain.use_cases.auth.AuthUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named(Constants.AUTH_USE_CASES)
    private val useCases: AuthUseCases,
    val client: SignInClient
): ViewModel() {
    private val _oneTapSignInState = mutableStateOf<Response<BeginSignInResult>>(Response.Success(null))
    val oneTapSignInState: State<Response<BeginSignInResult>> = _oneTapSignInState

    private val _signInState = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val signInState: State<Response<Boolean>> = _signInState

    private val _saveUserState = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val saveUserState: State<Response<Boolean>> = _saveUserState

    private val _updateLastActive = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val updateLastActive: State<Response<Boolean>> = _updateLastActive

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            useCases.signInWithEmail(email, password).collect { result ->
                _signInState.value = result
            }
        }
    }

    fun oneTapSignIn() {
        viewModelScope.launch {
            useCases.oneTapSignIn().collect { result ->
                _oneTapSignInState.value = result
            }
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) {
        viewModelScope.launch {
            useCases.signInWithGoogle(googleCredential).collect { result ->
                _signInState.value = result
            }
        }
    }

    fun saveUser() {
        viewModelScope.launch {
            useCases.saveUser().collect { result ->
                _saveUserState.value = result
            }
        }
    }

    fun updateLastActive() {
        viewModelScope.launch {
            useCases.updateLastActive().collect { result ->
                _updateLastActive.value = result
            }
        }
    }


    fun getAuthStatus() = liveData(Dispatchers.IO) {
        useCases.getAuthStatus().collect { result ->
            emit(result)
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            useCases.resetPassword(email).collect { result ->
                _signInState.value = result
            }
        }
    }

}