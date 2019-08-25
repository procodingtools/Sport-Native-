package org.procodingtools.idealsportscenter.fragments.dialogs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.activities.FragmentContainer;
import org.procodingtools.idealsportscenter.commons.entity.UserEntity;

import static org.procodingtools.idealsportscenter.commons.SharedImportExport.setUser;
import static org.procodingtools.idealsportscenter.commons.WebService.LOGIN;
import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

/**
 * Created by djamiirr on 13/11/17.
 */

public class LoginFragmentDialog extends DialogFragment {

    private EditText login,pass;
    private AppCompatButton ok;
    private AwesomeProgressDialog progress;
    private String fragmentName;

    public LoginFragmentDialog(){
        fragmentName =null;
    }
    @SuppressLint("ValidFragment")
    public LoginFragmentDialog(String fragmentName){
        this.fragmentName =fragmentName;
    }

    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.dialog_fragment_login,null);

        //init views
        initViews();

        //listeners
        initListeners();

        progress=new AwesomeProgressDialog(getContext());
        progress.setTitle(getString(R.string.connecting))
                .setMessage(getString(R.string.wait_progress))
                .setCancelable(false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }


    private void initListeners() {

        //login listener
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pass.getText().toString().trim().length()>0 &&charSequence.toString().trim().length()>0){
                    ok.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.material_green));
                    ok.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ok.setEnabled(true);
                }else{
                    ok.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_green));
                    ok.setTextColor(ContextCompat.getColor(getContext(),R.color.lightGray));
                    ok.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //pass listener
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (login.getText().toString().trim().length()>0&&charSequence.toString().trim().length()>0){
                    ok.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.material_green));
                    ok.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                    ok.setEnabled(true);
                }else{
                    ok.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_green));
                    ok.setTextColor(ContextCompat.getColor(getContext(),R.color.lightGray));
                    ok.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //ok listener
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.findViewById(R.id.main_card).setVisibility(View.GONE);
                tryLogin();
            }
        });
    }

    //starting init views
    private void initViews() {
        login=v.findViewById(R.id.login_et);
        pass=v.findViewById(R.id.pwd_et);
        ok=v.findViewById(R.id.connect_btn);

    }

    //try to connect
    private void tryLogin() {

        //showing progress
        progress.show();

        //creating request
        JsonObjectRequest request=new JsonObjectRequest(LOGIN + "/" + login.getText().toString() + "/" + pass.getText().toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.length()!=0){
                    try {
                        String nom,prenom,id,date,type;
                        //start parsing JSON
                        id=response.getInt("Id")+"";
                        nom=response.getString("Nom").trim();
                        prenom=response.getString("Prenom").trim();
                        date=response.getString("DateCreation").trim().substring(0,response.getString("DateCreation").indexOf("T"));
                        type=response.getString("Type").trim();
                        //end Parsing JSON

                        if(type.equals("Adherent")) {
                            //creating User
                            UserEntity entity = new UserEntity(nom, prenom, id, date, type);

                            //setting User to SharedPrefernces
                            setUser(entity, v.getContext());

                            //setting user entity to static Entity
                            USER_ENTITY = entity;

                            TastyToast.makeText(getContext(),getString(R.string.connected_successfully),TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
                            getDialog().dismiss();
                        }
                        else{
                            new AwesomeErrorDialog(v.getContext())
                                    .setMessage(getString(R.string.coach_error))
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            v.findViewById(R.id.main_card).setVisibility(View.VISIBLE);
                                            getDialog().dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    } catch (JSONException e) {
                        Log.e("Json error",e.getMessage());
                    }
                }
                else{
                    new AwesomeErrorDialog(v.getContext()).setTitle(getString(R.string.error))
                            .setMessage(getString(R.string.verif_mail_pass))
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setCancelable(false)
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    v.findViewById(R.id.main_card).setVisibility(View.VISIBLE);
                                }
                            }).show();
                }
                progress.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Volley error",error.getMessage());
                new AwesomeErrorDialog(v.getContext()).setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.verify_network))
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setCancelable(false)
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                v.findViewById(R.id.main_card).setVisibility(View.VISIBLE);
                                getDialog().dismiss();
                            }
                        }).show();
                progress.hide();
            }
        });
        Volley.newRequestQueue(v.getContext()).add(request);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (fragmentName !=null) {
            getActivity().startActivity(new Intent(getContext(), FragmentContainer.class).putExtra("fragment_name", fragmentName));
            getActivity().finish();
        }

    }
}
