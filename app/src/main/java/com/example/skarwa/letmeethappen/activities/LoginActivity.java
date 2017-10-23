package com.example.skarwa.letmeethappen.activities;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.skarwa.letmeethappen.R;
import com.example.skarwa.letmeethappen.models.User;
import com.example.skarwa.letmeethappen.models.UserGroupStatus;
import com.example.skarwa.letmeethappen.network.FirebaseDatabaseClient;
import com.example.skarwa.letmeethappen.services.MyEventTrackingService;
import com.example.skarwa.letmeethappen.services.RegistrationIntentService;
import com.example.skarwa.letmeethappen.utils.Constants;
import com.example.skarwa.letmeethappen.utils.DBUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener ,
        Constants{

    private static final String TAG = "LoginActivity";
    private static int RC_SIGN_IN = 88;
    private static int RC_REAUTHORIZE = 2;
    private static int RC_AUTHORIZE_CONTACTS=63;
    FirebaseAuth mAuth;
    FirebaseUser fbaseUser;
    GoogleApiClient mGoogleApiClient;
    DatabaseReference mDatabase;
    Account mAuthorizedAccount;
    private List<Parcelable> friends;
    SharedPreferences sharedPref;
    DBUtils DBUtils;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]



        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        if(checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                // TODO: If can't login to gmail, temporarily set this to false to test other functionalities.
                // Please set it back to true when done and before pushing back the changes.
                boolean GMAIL_WORKS = true;

                if (GMAIL_WORKS) {
                    signIn();
                } else {
                    onLoginSuccess();
                }
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN || requestCode == RC_AUTHORIZE_CONTACTS) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == RC_REAUTHORIZE) {
            Log.d ("DEBUG", "Reauthorize!! ");
            getContacts();
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
             mAuthorizedAccount = acct.getAccount();
            firebaseAuthWithGoogle(acct);

        } else {
            //TODO display message
            // Signed out, show unauthenticated UI.
            Log.d(TAG, "unauthenticated google:" + result.isSuccess());
            //updateUI(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fbaseUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        if (fbaseUser == null) {

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                fbaseUser = mAuth.getCurrentUser();
                                Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                getContacts();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        } else {
            getContacts();
        }
    }



    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    //@Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, ViewEventsActivity.class);
        //send user details to the next activity to fetch groups and events

        i.putExtra(Constants.USER_OBJ, Parcels.wrap(createUserFromFirebaseAuthUser(fbaseUser)));
        i.putParcelableArrayListExtra(Constants.FRIENDS_OBJ, (ArrayList<? extends Parcelable>) friends);
        //i.putParcelableArrayListExtra("persons", persons);
        startActivity(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        new Throwable().printStackTrace();
    }


    public User createUserFromFirebaseAuthUser(FirebaseUser fbaseUser){
        User user = new User();
        user.setDisplayName(fbaseUser.getDisplayName());
        user.setEmail(fbaseUser.getEmail());
        user.setTokenId(FirebaseInstanceId.getInstance().getToken());
        // TODO : SAVE IT TO FIREBASE
        user.setPhoneNum(fbaseUser.getPhoneNumber());
        user.setUserStatus(UserGroupStatus.ACTIVE.name());
        user.setProfilePicUrl(fbaseUser.getPhotoUrl().toString());
        user.setUserSettings(null); //setting settings null for now.


        // save user details to shared preferences
        sharedPref = this.getSharedPreferences(
                USER_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_ID, user.getId());
        editor.putString(USER_DISPLAY_NAME,fbaseUser.getDisplayName());
        editor.apply();

        DBUtils.saveUser(user);
        return user;
    }




    //private void getContacts(Account account) {
    private void getContacts() {
        GetContactsTask task = new GetContactsTask(mAuthorizedAccount);
        task.execute();
    }

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private class GetContactsTask extends AsyncTask<Void, Void, List<Person>> {

        Account mAccount;
        public GetContactsTask(Account account) {
            mAccount = account;
        }

        @Override
        protected List<Person> doInBackground(Void... params) {
            List<Person> result = null;
            try {

                GoogleAccountCredential credential =
                        GoogleAccountCredential.usingOAuth2(
                                LoginActivity.this,
                                Collections.singleton(
                                        "https://www.googleapis.com/auth/contacts.readonly")
                        );
                credential.setSelectedAccount(mAccount);

                PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                        .setApplicationName("Let Meet Happen")
                        .build();
                ListConnectionsResponse connectionsResponse = service
                        .people()
                        .connections()
                        .list("people/me")
                        .setPageSize(30)
                        .setPersonFields("names,emailAddresses,coverPhotos,phoneNumbers")
                         .execute();
                result = connectionsResponse.getConnections();
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                // Explain to the user again why you need these OAuth permissions
                // And prompt the resolution to the user again:
                userRecoverableException.printStackTrace();
                startActivityForResult(userRecoverableException.getIntent(),RC_REAUTHORIZE);
            } catch (IOException e) {
                // Other non-recoverable exceptions.
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected void onPostExecute(List<Person> connections) {
            super.onPostExecute(connections);

            friends = new ArrayList<Parcelable>();
            for (Person person : connections) {
                User user = new User();
                List<Name> names = person.getNames();

                // create unique ID for the user
                user.setId(String.valueOf(UUID.randomUUID()));
                user.setUserStatus(UserGroupStatus.ACTIVE.name());
                user.setUserSettings(null);

                if (names != null && names.size() > 0) {
                    user.setDisplayName(person.getNames().get(0).getDisplayName());
                    //System.out.println("Name: " + person.getNames().get(0).getDisplayName());
                } else {
                    //System.out.println("No names available for connection.");
                }

                List<EmailAddress> emails = person.getEmailAddresses();
                if (emails == null || emails.size() == 0 ||
                        !emails.get(0).getValue().endsWith("@gmail.com")) {
                    continue;
                }
                user.setEmail(emails.get(0).getValue());


                List<Photo> photos = person.getPhotos();
                if (photos != null && photos.size() > 0) {
                    user.setProfilePicUrl(photos.get(0).getUrl());
                }

                List<PhoneNumber> phones = person.getPhoneNumbers();
                if (phones != null && phones.size() > 0) {
                    user.setPhoneNum(phones.get(0).getFormattedType());
                }
                friends.add(Parcels.wrap(user));
            }
            onLoginSuccess();
        }
    }
}
