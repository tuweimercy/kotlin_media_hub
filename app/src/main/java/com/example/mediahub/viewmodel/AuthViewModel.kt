package com.example.mediahub.viewmodel

// viewmodel packages to allow state/data management for our ui
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// firebase authentication/firestore product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
// import our models (use your own package)
import com.example.mediahub.model.UserRole
import com.example.mediahub.model.UserProfile
// coroutines for background processing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// sealed class : it cannot be used to create objects
// confined for usage inside our viewmodel
sealed class AuthState{
    object Idle : AuthState() // state when user is not logged in
    object Loading: AuthState() // state when user attempts to
    //login
    // when user is successfully logged in tag the profile
    data class Success(val profile: UserProfile) : AuthState()
    // if error occurred on login attempt
    data class Error(val message: String) : AuthState()
    // state on password reset process
    object PasswordResetSent : AuthState()
    // state when user logs out
    object Logout : AuthState()
}

class AuthViewModel : ViewModel(){
    // reference to firebase auth classes
    // private means variable can only be used inside script
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _authState = MutableStateFlow<AuthState>(
        AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState
    private val _currentProfile =
        MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile

    // Called on app start to check if user is already logged in
    // if so redirect to dashboard skip auth screens
    init {
        val user = auth.currentUser
        if(user != null){
            fetchUserProfile(user.uid)
        }
    }
    //    Registration logic
    fun register(fullName: String,
                 email: String,
                 password: String,
                 role: String){
        viewModelScope.launch {
            // set auth state as loading when user hits submit
            _authState.value  = AuthState.Loading
            try{
                //1. create firebase account for user
                val result = auth.createUserWithEmailAndPassword(
                    email,password
                ).await() // being a background process we wait
                // for result
                // on success we capture the user id
                val uid = result.user!!.uid
                //2. create profile and store in firestore
                val profile = UserProfile(
                    uid = uid,
                    fullName = fullName,
                    email = email,
                    role = role
                )
                // save to firestore inside the users collection
                db.collection("users")
                    .document(uid) //unique identifier
                    .set(profile.toMap())
                    .await()
                // load the current profile for the current user
                _currentProfile.value = profile
                _authState.value = AuthState.Success(profile)
            } catch (e: Exception){
                _authState.value = AuthState.Error(
                    e.message ?: "Registration Failed"
                )
            }
        }

    }
    // Login
    fun Login(email: String ,password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try{
                val result = auth.
                signInWithEmailAndPassword(email,
                    password)
                    .await()
                fetchUserProfile(result.user!!.uid)
            } catch (e: Exception){
                _authState.value = AuthState.Error(
                    e.message ?: "Login Failed"
                )
            }
        }
    }

    // fetch user profile from firestore
    fun fetchUserProfile(uid: String){
        viewModelScope.launch {
            try{
                _authState.value = AuthState.Loading
                // first we create a reference to our firestore collection
                val doc = db.collection("users")
                    .document(uid)
                    .get().await()
                // pack our data into our model class
                val profile = UserProfile(
                    uid = uid,
                    fullName = doc.getString("fullName") ?: "",
                    email = doc.getString("email") ?: "",
                    role = doc.getString("role") ?: "student"
                )
                //set the data to our viewmodel observer
                _currentProfile.value = profile
                _authState.value = AuthState.Success(profile)
            } catch(e: Exception){
                _authState.value = AuthState.Error(
                    e.message ?: "Failed to load profile"
                )
            }
        }
    }
    // forgot password
    fun sendPasswordReset(email: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try{
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.PasswordResetSent
            }catch(e: Exception){
                _authState.value = AuthState.Error(
                    e.message ?: "Reset Failed"
                )
            }
        }
    }

    // Logout
    fun logout(){
        auth.signOut()
        _currentProfile.value = null
        _authState.value = AuthState.Logout
    }
    //clear states
    fun clearState(){
        _authState.value = AuthState.Idle
    }
}

