package ru.yomu.slaviksoft.led2;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class LoginFragment extends Fragment {

    private String mLogin;
    private String mPassword;
    private boolean mPassLoaded;
    private ProgressDialog progressDialog;

    final private String OPTIONS = "options";
    final private String OPTIONS_REMEMBER = "remember_password";
    final private String OPTIONS_LOGIN = "login";
    final private String OPTIONS_PASSWORD = "password";

    private CheckBox cbRemember;
    private EditText edLogin;
    private EditText edPassword;

    private OnLoginListener mListener;

    private void log(String text) {
        Log.d("DEBUG1", text);
    }

    interface OnLoginListener {
        void onLoginRequestResult(String Login, int id);
    }

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPassLoaded = savedInstanceState.getBoolean("mPassLoaded");
            mLogin = savedInstanceState.getString("mLogin");
            mPassword = savedInstanceState.getString("mPassword");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mPassLoaded", mPassLoaded);
        outState.putString("mLogin", mLogin);
        outState.putString("mPassword", mPassword);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnLoginListener) activity;
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);

        edLogin = (EditText) layout.findViewById(R.id.edLogin);
        edPassword = (EditText) layout.findViewById(R.id.edPassword);
        cbRemember = (CheckBox) layout.findViewById(R.id.cbRemember);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) loadPreferences();

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEditsEnabled(!isChecked);
                if (!isChecked && mPassLoaded) {
                    setPassword("");
                    mPassLoaded = false;
                }
            }
        });

        Button btnLogin = (Button) getActivity().findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mPassLoaded) {
                    if (edPassword.getText().toString().length() < 2) {
                        edPassword.setError("Password is too short");
                        edPassword.requestFocus();
                        return;
                    }
                    mLogin = edLogin.getText().toString();
                    mPassword = getMD5(edPassword.getText().toString());
                }

                doLoginRequest();

            }

        });

    }

    private void setLogin(String login) {
        mLogin = login;
        edLogin.setText(login);
    }

    private void setPassword(String pass) {
        mPassword = pass;
        edPassword.setText(pass);
    }

    private void setEditsEnabled(boolean val) {
        edLogin.setEnabled(val);
        edPassword.setEnabled(val);
    }

    private void doLoginRequest() {

        log("Request: " + mLogin + ", " + mPassword);

        HttpRequest.login_request(mLogin, mPassword, new HttpRequest.OnHttpRequestListener() {
            @Override
            public void onHttpRequestResult(boolean result) {
                progressDialog.dismiss();
                if (result) {
                    savePreferences();
                    mListener.onLoginRequestResult(mLogin, 0);
                }
            }
        });

        progressDialog = ProgressDialog.show(getActivity(), "Login in", "Wait a moment please", true, false);
    }

    private void loadPreferences() {

        SharedPreferences sp = getActivity().getSharedPreferences(OPTIONS, Context.MODE_PRIVATE);

        boolean rem = sp.getBoolean(OPTIONS_REMEMBER, false);
        if (rem) {
            mPassLoaded = true;
            edPassword.setText("####");
            cbRemember.setChecked(true);
            mPassword = sp.getString(OPTIONS_PASSWORD, "");
        }

        setLogin(sp.getString(OPTIONS_LOGIN, ""));
        setEditsEnabled(!rem);

    }

    private void savePreferences() {

        SharedPreferences.Editor ed = getActivity().getSharedPreferences(OPTIONS, Context.MODE_PRIVATE).edit();
        ed.putString(OPTIONS_LOGIN, mLogin);
        ed.putBoolean(OPTIONS_REMEMBER, cbRemember.isChecked());
        if (cbRemember.isChecked() && !mPassLoaded) {
            ed.putString(OPTIONS_PASSWORD, getMD5(edPassword.getText().toString()));
        }
        ed.commit();

    }

    public String getMD5(String pass) {
        return "md5_password:" + pass;
    }

}
