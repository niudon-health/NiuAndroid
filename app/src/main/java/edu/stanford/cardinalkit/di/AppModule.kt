package edu.stanford.cardinalkit.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.stanford.cardinalkit.R
import edu.stanford.cardinalkit.common.Constants
import edu.stanford.cardinalkit.data.repositories.AuthRepositoryImpl
import edu.stanford.cardinalkit.data.repositories.SurveyRepositoryImpl
import edu.stanford.cardinalkit.domain.repositories.AuthRepository
import edu.stanford.cardinalkit.domain.repositories.SurveyRepository
import edu.stanford.cardinalkit.domain.use_cases.UploadSurvey
import edu.stanford.cardinalkit.domain.use_cases.UseCases
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Named(Constants.USERS_REF)
    fun provideUsersRef(db: FirebaseFirestore) = db.collection(Constants.FIRESTORE_USERS_COLLECTION)

    @Provides
    @Named(Constants.SURVEYS_REF)
    fun provideSurveysRef(
        db: FirebaseFirestore
    ): CollectionReference? {
        val user = Firebase.auth.currentUser
        user?.let {
            return db.collection(
                "${Constants.FIRESTORE_BASE_DOCUMENT}/${Constants.FIRESTORE_USERS_COLLECTION}/${user.uid}/${Constants.FIRESTORE_SURVEYS_COLLECTION}"
            )
        }
        return null
    }

    @Provides
    fun provideOneTapClient(context: Context) = Identity.getSignInClient(context)

    @Provides
    @Named(Constants.SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(Constants.SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.web_client_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(Constants.SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        signInClient: GoogleSignInClient,
        @Named(Constants.USERS_REF)
        usersRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signInClient = signInClient,
        usersRef = usersRef
    )

    @Provides
    @Named(Constants.SURVEY_REPOSITORY)
    fun provideSurveyRepository(
        @Named(Constants.SURVEYS_REF)
        surveysRef: CollectionReference?
    ): SurveyRepository = SurveyRepositoryImpl(surveysRef)

    @Provides
    @Named(Constants.USE_CASES)
    fun provideUseCases(
        @Named(Constants.SURVEY_REPOSITORY)
        repository: SurveyRepository
    ) = UseCases(
        uploadSurvey = UploadSurvey(repository)
    )
}