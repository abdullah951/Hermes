package com.company.hermes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncryptFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText txtInput, txtKey, txtCipher;
    Button btnEncrypt, btnKey;
    static String alphabet[];
    String plainText = "";
    String[] sentence, key, cipher;
    StringBuilder[] builderSentence, builderKey;
    Random r = new Random();

    public EncryptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_encrypt, container, false);

        txtInput = (EditText) rootView.findViewById(R.id.txtDtCipher);
        txtCipher = (EditText) rootView.findViewById(R.id.txtDtPlaintext);
        txtKey = (EditText) rootView.findViewById(R.id.txtDtKey);
        btnEncrypt = (Button) rootView.findViewById(R.id.btnDecrypt);
        btnKey = (Button) rootView.findViewById(R.id.btnKey);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerOption);
        spinner.setOnItemSelectedListener(this);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.algorithms_array,
                android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        alphabet = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9"};

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Copy key to clipboard.
                //Add option to select cipher.
                plainText = txtInput.getText().toString();
                //Split the plainText into an array of Strings separated by a " ".
                StringTokenizer tokens = new StringTokenizer(plainText, " ");
                sentence = new String[tokens.countTokens()];
                for(int i =0; i < sentence.length; i++){
                    sentence[i] = tokens.nextToken();
                }
                builderSentence = new StringBuilder[sentence.length];
                //Obtain the binary of the plainText strings in the form of a StringBuilder object.
                for(int i = 0; i < sentence.length; i++){
                    builderSentence[i] = convertToBinaryBuilder(sentence[i]);
                }
                builderKey = new StringBuilder[key.length];
                //Obtain the binary of the plainText strings in the form of a StringBuilder object.
                for(int i = 0; i < key.length; i++){
                    builderKey[i] = convertToBinaryBuilder(key[i]);
                }
                cipher = new String[key.length];
                for(int i = 0; i < cipher.length; i++){
                    cipher[i] = "";
                }
                for(int i = 0; i < key.length; i++){
                    for(int j = 0; j < builderKey[i].length(); j++){
                        if((""+builderSentence[i].charAt(j)+"").matches(" ")) {
                            cipher[i] += " ";
                            continue;
                        } else {
                            int plainTextIndex = Integer.parseInt("" + builderSentence[i].charAt(j) + "");
                            int keyIndex = Integer.parseInt("" + builderKey[i].charAt(j) + "");
                            int cipherIndex = plainTextIndex ^ keyIndex;
                            cipher[i] += cipherIndex;
                        }
                    }
                    StringTokenizer tokenCipher = new StringTokenizer(cipher[i], " ");
                    String[] temp = new String[tokenCipher.countTokens()];
                    for(int k =0; k < temp.length; k++){
                        temp[k] = tokenCipher.nextToken();
                    }
                    for(int k = 0; k < temp.length; k++){
                        temp[k] = "" + Long.parseLong(temp[k], 2) + "";
                        temp[k] = alphabet[(Integer.parseInt(temp[k]) % 62)];
                    }
                    cipher[i] = "";
                    for(int k =0 ; k< temp.length; k++){
                        cipher[i] += temp[k];
                    }
                    cipher[i] += " ";
//                    cipher[i] = "" + Long.parseLong(cipher[i], 2) + "";
                }

//                Convert sentence to string.
                String completeCipher = "";

                for(int i =0; i < cipher.length; i++){
                    completeCipher += cipher[i];
                    completeCipher += " ";
                }
                txtCipher.setText(completeCipher);
            }
        });

        btnKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plainText = txtInput.getText().toString();
                //Split the plainText into an array of Strings separated by a " ".
                StringTokenizer tokens = new StringTokenizer(plainText, " ");
                sentence = new String[tokens.countTokens()];
                for(int i =0; i < sentence.length; i++){
                    sentence[i] = tokens.nextToken();
                }

                key = new String[sentence.length];
                for(int i = 0; i < key.length; i++){
                    key[i] = "";
                }

                for(int i = 0; i < key.length; i++ ){
                    for(int j = 0; j < sentence[i].length(); j++){
                        key[i] += alphabet[r.nextInt(62)];
                    }
                }

                //Convert sentence to string.
                String completeSentence = "";

                for(int i =0; i < key.length; i++){
                    completeSentence += key[i];
                    completeSentence += " ";
                }
                txtKey.setText(completeSentence);
            }
        });
        return rootView;
    }

    public StringBuilder convertToBinaryBuilder(String s){
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String option = parent.getItemAtPosition(position).toString();
        Toast.makeText(getActivity().getBaseContext(), option , Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
