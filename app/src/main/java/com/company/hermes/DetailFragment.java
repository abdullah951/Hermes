package com.company.hermes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    EditText txtInput, txtKey, txtCipher;
    Button btnEncrypt, btnKey;
    static String alphabet[];
    String plainText = "";
    String[] sentence, key, cipher;
    StringBuilder[] builderSentence, builderKey;
    Random r = new Random();

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        return rootView;
    }
}
