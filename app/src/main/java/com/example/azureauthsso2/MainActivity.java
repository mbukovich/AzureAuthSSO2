package com.example.azureauthsso2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    IMultipleAccountPublicClientApplication mMsalClient;

    String[] mScopes = {"User.Read"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view_name);

        PublicClientApplication.createMultipleAccountPublicClientApplication(this,
                R.raw.msal_config,
                new IPublicClientApplication.IMultipleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(IMultipleAccountPublicClientApplication application) {
                        mMsalClient = application;
                        login();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        mTextView.setText(exception.getMessage());
                    }
                });
    }

    private void login() {
        mMsalClient.acquireToken(this, mScopes, getAuthCallback());
    }

    private AuthenticationCallback getAuthCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onCancel() {
                mTextView.setText("User cancelled Authentication.");
            }

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                mTextView.setText(authenticationResult.getAccount().getUsername());
            }

            @Override
            public void onError(MsalException exception) {
                mTextView.setText(exception.getMessage());
            }
        };
    }
}