package com.aeon.app;

/*
Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.math.BigDecimal;

public class TransactionPendingFragment extends Fragment {
    private TextView text_transfer_info;
    private Button understood;
    public TransactionPendingFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_pending, container, false);
        text_transfer_info = (TextView) v.findViewById(R.id.text_transfer_info);
        understood = v.findViewById(R.id.button_confirm_send);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (BackgroundThread.pendingTransaction.fee==0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        text_transfer_info.setText(
                                MainActivity.getString("text_transaction_you_are_sending") +" "
                                        +BigDecimal.valueOf(BackgroundThread.pendingTransaction.amount).movePointLeft(12).toPlainString()
                                        +" "+ MainActivity.getString("text_transaction_aeon_to") +" "
                                        + BackgroundThread.pendingTransaction.recipient
                                        +" "+ MainActivity.getString("text_transaction_for_a_fee_of")  +" "
                                        + BigDecimal.valueOf(BackgroundThread.pendingTransaction.fee).movePointLeft(12).toPlainString()
                                        +" "+ MainActivity.getString("text_transaction_aeon")
                        );
                        understood.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideUI();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}