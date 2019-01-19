package com.example.esraa.fitpass.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface ILoginPresenter extends IBasePresenter{

    void signInWithMailAndPass(String mail, String pass);

    void saveUserAndNavigate(String userId);

    void handleSignInResult(Task<GoogleSignInAccount> task);

    void checkIfUserLoggedIn();

    void deleteEventListenerOfDatabase();
}
