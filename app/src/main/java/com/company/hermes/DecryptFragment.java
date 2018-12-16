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
public class DecryptFragment extends Fragment {

    EditText txtDtPlaintext, txtDtKey, txtDtCipher;
    Button btnDecrypt;
    static String alphabet[];
    String originalCipher = "", originalKey = "";
    String[] sentence, key, cipher;
    StringBuilder[] builderCipher, builderKey;
    Random r = new Random();

    public DecryptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_decrypt, container, false);

        txtDtKey = (EditText) rootView.findViewById(R.id.txtDtKey);
        txtDtCipher = (EditText) rootView.findViewById(R.id.txtDtCipher);
        txtDtPlaintext = (EditText) rootView.findViewById(R.id.txtDtPlaintext);
        btnDecrypt = (Button) rootView.findViewById(R.id.btnDecrypt);

        alphabet = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9"};

        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Copy key to clipboard.
                //Add option to select cipher.
                originalCipher = txtDtCipher.getText().toString();
                //Split the plainText into an array of Strings separated by a " ".
                StringTokenizer tokens1 = new StringTokenizer(originalCipher, " ");
                cipher = new String[tokens1.countTokens()];
                for(int i =0; i < cipher.length; i++){
                    cipher[i] = tokens1.nextToken();
                }
                builderCipher = new StringBuilder[cipher.length];
                //Obtain the binary of the plainText strings in the form of a StringBuilder object.
                for(int i = 0; i < cipher.length; i++){
                    builderCipher[i] = convertToBinaryBuilder(cipher[i]);
                }
                //Get the key.
                originalKey = txtDtKey.getText().toString();
                StringTokenizer tokens2 = new StringTokenizer(originalCipher, " ");
                key = new String[tokens2.countTokens()];
                for(int i =0; i < key.length; i++){
                    key[i] = tokens2.nextToken();
                }
                builderKey = new StringBuilder[key.length];
                //Obtain the binary of the plainText strings in the form of a StringBuilder object.
                for(int i = 0; i < key.length; i++){
                    builderKey[i] = convertToBinaryBuilder(key[i]);
                }


                sentence = new String[key.length];
                for(int i = 0; i < sentence.length; i++){
                    sentence[i] = "";
                }

                for(int i = 0; i < key.length; i++){
                    for(int j = 0; j < builderKey[i].length(); j++){
                        if((""+builderKey[i].charAt(j)+"").matches(" ")) {
                            sentence[i] += " ";
                            continue;
                        } else {
                            int cipherIndex = Integer.parseInt("" + builderCipher[i].charAt(j) + "");
                            int keyIndex = Integer.parseInt("" + builderKey[i].charAt(j) + "");
                            int plainTextIndex = cipherIndex ^ keyIndex;
                            sentence[i] += plainTextIndex;
                        }
                    }
                    StringTokenizer tokenCipher = new StringTokenizer(sentence[i], " ");
                    String[] temp = new String[tokenCipher.countTokens()];
                    for(int k =0; k < temp.length; k++){
                        temp[k] = tokenCipher.nextToken();
                    }
                    for(int k = 0; k < temp.length; k++){
                        temp[k] = "" + Long.parseLong(temp[k], 2) + "";
                        temp[k] = alphabet[(Integer.parseInt(temp[k]) % 62)];
                    }
                    sentence[i] = "";
                    for(int k =0 ; k< temp.length; k++){
                        sentence[i] += temp[k];
                    }
                    sentence[i] += " ";
//                    cipher[i] = "" + Long.parseLong(cipher[i], 2) + "";
                }

//                Convert sentence to string.
                String completeSentence = "";

                for(int i =0; i < sentence.length; i++){
                    completeSentence += sentence[i];
                    completeSentence += " ";
                }
                txtDtPlaintext.setText(completeSentence);
            }
        });

//        btnKey.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plainText = txtInput.getText().toString();
//                //Split the plainText into an array of Strings separated by a " ".
//                StringTokenizer tokens = new StringTokenizer(plainText, " ");
//                sentence = new String[tokens.countTokens()];
//                for(int i =0; i < sentence.length; i++){
//                    sentence[i] = tokens.nextToken();
//                }
//
//                key = new String[sentence.length];
//                for(int i = 0; i < key.length; i++){
//                    key[i] = "";
//                }
//
//                for(int i = 0; i < key.length; i++ ){
//                    for(int j = 0; j < sentence[i].length(); j++){
//                        key[i] += alphabet[r.nextInt(62)];
//                    }
//                }
//
//                //Convert sentence to string.
//                String completeSentence = "";
//
//                for(int i =0; i < key.length; i++){
//                    completeSentence += key[i];
//                    completeSentence += " ";
//                }
//                txtKey.setText(completeSentence);
//            }
//        });
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
}
