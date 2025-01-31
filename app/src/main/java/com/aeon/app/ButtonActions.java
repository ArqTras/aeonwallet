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
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.aeon.app.models.Node;
import com.aeon.app.ui.contact.ContactContent;
import com.aeon.app.ui.transfer.TransferFragment;
import com.aeon.app.ui.wallet.WalletContent;
import com.aeon.app.ui.wallet.WalletFragment;
import com.aeon.app.util.SeedWords;
import com.aeon.app.util.SpaceTokenizer;

import java.math.BigDecimal;

public class ButtonActions extends AppCompatActivity {
    protected static BackgroundThread thread;
    private NavOptions navOptions = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    .build();
    public static String password;

    public void createWallet(View v) {
        if (thread == null || !thread.isRunning) {
            TextView passwordInfo = findViewById(R.id.text_wallet_password_info);
            EditText password = findViewById(R.id.text_wallet_password);
            EditText passwordConfirm = findViewById(R.id.text_wallet_password_confirm);
            Button confirm = findViewById(R.id.button_confirm_new_wallet);
            View walletFragmentLayout = findViewById(R.id.layout_wallet);
            WalletFragment.hideUI(walletFragmentLayout);
            passwordInfo.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            passwordConfirm.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            password.requestFocus();
            showKeyboard();
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(password.getText().toString().equals(passwordConfirm.getText().toString())
                        && ! password.getText().toString().equals("")){
                        setPassword(password.getText().toString());
                        thread = new BackgroundThread();
                        thread.start();
                        goToNewWalletFragment(v);
                        WalletContent.clearItems();
                        WalletContent.addItem(new WalletContent.Item("", getResources().getString(R.string.text_loading_wallet)));
                        BackgroundThread.queueWallet(getFilesDir().getAbsolutePath() + "/wallet" + System.currentTimeMillis());
                        View walletFragmentLayout = findViewById(R.id.layout_wallet);
                        WalletFragment.openWalletView(walletFragmentLayout);
                        ConstraintLayout layout = findViewById(R.id.layout_seed_input);
                        layout.setVisibility(View.GONE);
                        layout = findViewById(R.id.layout_keys_input);
                        layout.setVisibility(View.GONE);
                    } else {
                        WalletFragment.closeWalletView(walletFragmentLayout);
                    }
                    hideKeyboard();
                    password.setText("");
                    passwordConfirm.setText("");
                    passwordInfo.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                    passwordConfirm.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                }
            });
        }
    }

    public void createWalletFromSeed(View v) {
        ConstraintLayout seedLayout = findViewById(R.id.layout_seed_input);
        MultiAutoCompleteTextView textView = seedLayout.findViewById(R.id.text_seed_input);
        EditText editText = seedLayout.findViewById(R.id.text_restore_height);
        if(!editText.getText().toString().equals("") && ! textView.getText().toString().equals("")) {
            String seed = textView.getText().toString();
            int restoreHeight = Integer.parseInt(editText.getText().toString());
            if (thread == null || !thread.isRunning) {
                seedLayout.setVisibility(View.GONE);
                TextView passwordInfo = findViewById(R.id.text_wallet_password_info);
                EditText password = findViewById(R.id.text_wallet_password);
                EditText passwordConfirm = findViewById(R.id.text_wallet_password_confirm);
                Button confirm = findViewById(R.id.button_confirm_new_wallet);
                View walletFragmentLayout = findViewById(R.id.layout_wallet);
                WalletFragment.hideUI(walletFragmentLayout);
                passwordInfo.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                passwordConfirm.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                password.requestFocus();
                showKeyboard();
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (password.getText().toString().equals(passwordConfirm.getText().toString())
                                && !password.getText().toString().equals("")) {
                            setPassword(password.getText().toString());
                            thread = new BackgroundThread();
                            thread.start();
                            WalletContent.clearItems();
                            WalletContent.addItem(new WalletContent.Item("", getResources().getString(R.string.text_loading_wallet)));
                            BackgroundThread.queueWallet(getFilesDir().getAbsolutePath() + "/wallet" + System.currentTimeMillis(), seed, restoreHeight);
                            View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
                            WalletFragment.openWalletView(walletFragmentLayout);
                            ConstraintLayout layout = findViewById(R.id.layout_seed_input);
                            layout.setVisibility(View.GONE);
                            layout = findViewById(R.id.layout_keys_input);
                            layout.setVisibility(View.GONE);
                        } else {
                            WalletFragment.closeWalletView(walletFragmentLayout);
                        }
                        hideKeyboard();
                        password.setText("");
                        passwordConfirm.setText("");
                        passwordInfo.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        passwordConfirm.setVisibility(View.GONE);
                        confirm.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    public void createWalletFromKeys(View v) {
        ConstraintLayout layout = findViewById(R.id.layout_keys_input);
        layout.setVisibility(View.VISIBLE);
        EditText account_text = layout.findViewById(R.id.text_account_address);
        EditText view_text = layout.findViewById(R.id.text_secret_view);
        EditText spend_text = layout.findViewById(R.id.text_secret_spend);
        EditText restore_height_text = layout.findViewById(R.id.text_restore_height_keys);
        if(!account_text.getText().toString().equals("") && ! view_text.getText().toString().equals("")
                && ! spend_text.getText().toString().equals("")
                && ! restore_height_text.getText().toString().equals("")) {
            String account = account_text.getText().toString();
            String view = view_text.getText().toString();
            String spend = spend_text.getText().toString();
            int restoreHeight = Integer.parseInt(restore_height_text.getText().toString());
            if (thread == null || !thread.isRunning) {
                layout.setVisibility(View.GONE);
                TextView passwordInfo = findViewById(R.id.text_wallet_password_info);
                EditText password = findViewById(R.id.text_wallet_password);
                EditText passwordConfirm = findViewById(R.id.text_wallet_password_confirm);
                Button confirm = findViewById(R.id.button_confirm_new_wallet);
                View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
                WalletFragment.hideUI(walletFragmentLayout);
                passwordInfo.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                passwordConfirm.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                password.requestFocus();
                showKeyboard();
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (password.getText().toString().equals(passwordConfirm.getText().toString())
                                && !password.getText().toString().equals("")) {
                            setPassword(password.getText().toString());
                            thread = new BackgroundThread();
                            thread.start();
                            WalletContent.clearItems();
                            WalletContent.addItem(new WalletContent.Item("", getResources().getString(R.string.text_loading_wallet)));
                            BackgroundThread.queueWallet(getFilesDir().getAbsolutePath() + "/wallet" + System.currentTimeMillis(), account,view,spend, restoreHeight);
                            View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
                            WalletFragment.openWalletView(walletFragmentLayout);
                            ConstraintLayout layout = findViewById(R.id.layout_seed_input);
                            layout.setVisibility(View.GONE);
                            layout = findViewById(R.id.layout_keys_input);
                            layout.setVisibility(View.GONE);
                        } else {
                            WalletFragment.closeWalletView(walletFragmentLayout);
                        }
                        hideKeyboard();
                        password.setText("");
                        passwordConfirm.setText("");
                        passwordInfo.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        passwordConfirm.setVisibility(View.GONE);
                        confirm.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
    public void setAddressIndex(View v) {
        RecyclerView rv = findViewById(R.id.rv_wallet_info_list);
        NumberPicker index = findViewById(R.id.numberpicker_main_picker);
        TextView help = findViewById(R.id.text_address_index_help);
        Button ok = findViewById(R.id.button_address_ok);
        index.setVisibility(View.VISIBLE);
        index.setMaxValue(500);
        index.setMinValue(0);
        index.setWrapSelectorWheel(false);
        ok.setVisibility(View.VISIBLE);
        help.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundThread.setAddressIndex(index.getValue());
                index.setVisibility(View.GONE);
                ok.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        });
    }


    public void showSeedLayout(View v){
        ConstraintLayout seedLayout = findViewById(R.id.layout_seed_input);
        seedLayout.setVisibility(View.VISIBLE);
        ConstraintLayout layout = findViewById(R.id.layout_keys_input);
        layout.setVisibility(View.GONE);
        View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
        WalletFragment.hideUI(walletFragmentLayout);
        MultiAutoCompleteTextView seedInput = seedLayout.findViewById(R.id.text_seed_input);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, SeedWords.words);
        seedInput.setAdapter(adapter);
        seedInput.setTokenizer(new SpaceTokenizer());
        seedInput.requestFocus();
        showKeyboard();
    }

    public void showKeysLayout(View v){
        ConstraintLayout layout = findViewById(R.id.layout_keys_input);
        layout.setVisibility(View.VISIBLE);
        ConstraintLayout seedLayout = findViewById(R.id.layout_seed_input);
        seedLayout.setVisibility(View.GONE);
        View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
        WalletFragment.hideUI(walletFragmentLayout);
        EditText input = layout.findViewById(R.id.text_account_address);
        input.requestFocus();
        showKeyboard();
    }
    public void createTransaction(View v) {
        View transferFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_transfer);
        EditText recipientInfo = (EditText)transferFragmentLayout.findViewById(R.id.transfer_recipient_info);
        EditText amountInfo = (EditText)transferFragmentLayout.findViewById(R.id.transfer_amount_info);
        BackgroundThread.queueTransaction(
                recipientInfo.getText().toString(),(new BigDecimal(amountInfo.getText().toString()).movePointRight(12).longValue())
        );
        goToTransactionPendingFragment(v);
    }

    public void confirmTransaction(View v) {
        EditText password = findViewById(R.id.text_transfer_password);
        Button confirm = findViewById(R.id.button_confirm_send);
        if(password.getText().toString().equals(getPassword())){
            BackgroundThread.confirmTransaction();
        }
        password.setText("");
        confirm.setVisibility(View.GONE);
        goToTransferFragment(v);
    }

    public void removeWallet(View v) {
        EditText password = findViewById(R.id.text_wallet_password);
        Button confirm = findViewById(R.id.button_confirm_delete);
        View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
        WalletFragment.hideUI(walletFragmentLayout);
        password.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        password.requestFocus();
        showKeyboard();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(getPassword())){
                    WalletFragment.closeWalletView(walletFragmentLayout);
                    thread.interrupt();
                } else {
                    WalletFragment.openWalletView(walletFragmentLayout);
                }
                hideKeyboard();
                password.setText("");
                password.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            }
        });
    }

    public void configureConnection(View v) {
        EditText address = findViewById(R.id.text_node_address);
        Button random = findViewById(R.id.button_node_random);
        Button ok = findViewById(R.id.button_node_ok);
        Button configure = findViewById(R.id.button_configure_connection);
        address.setEnabled(true);
        RecyclerView rv = findViewById(R.id.rv_node_info_list);
        address.setVisibility(View.VISIBLE);
        random.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        configure.setVisibility(View.GONE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!address.getText().toString().equals("")) {
                    if(thread.isRunning) {
                        thread.interrupt();
                    }
                    String input = address.getText().toString();
                        String[] host = input.split(":");
                        if (host.length > 1) {
                            BackgroundThread.setNode(new Node(host[0], host[1]));
                        } else {
                            BackgroundThread.setNode(new Node(host[0]));
                        }
                    address.setEnabled(false);
                    address.setText(null);
                    address.setVisibility(View.GONE);
                    random.setVisibility(View.GONE);
                    ok.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    configure.setVisibility(View.VISIBLE);
                    if(BackgroundThread.path!=null) {
                        thread = new BackgroundThread();
                        thread.start();
                        BackgroundThread.queueWallet(BackgroundThread.path);
                    }
                }
            }
        });
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thread.isRunning) {
                    thread.interrupt();
                }
                BackgroundThread.setNode(Node.pickRandom());
                address.setEnabled(false);
                address.setText(null);
                address.setVisibility(View.GONE);
                random.setVisibility(View.GONE);
                ok.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                configure.setVisibility(View.VISIBLE);
                if(BackgroundThread.path!=null) {
                    thread = new BackgroundThread();
                    thread.start();
                    BackgroundThread.queueWallet(BackgroundThread.path);
                }
            }
        });
    }

    public void addContact(View v) {
        EditText label = findViewById(R.id.text_contact_label);
        EditText address =  findViewById(R.id.text_contact_address);
        Button add =  findViewById(R.id.button_add_contact);
        RecyclerView rv =  findViewById(R.id.rv_contact_item_list);
        label.setEnabled(true);
        address.setEnabled(true);
        label.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!label.getText().toString().equals("") && !address.getText().toString().equals("")) {
                    ContactContent.addItem(new ContactContent.Contact(
                            label.getText().toString(),
                            address.getText().toString()));
                }
                label.setEnabled(false);
                address.setEnabled(false);
                label.setText(null);
                address.setText(null);
                label.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
            }
        });
    }
    public void showSecretInfo(View v) {
        EditText password = findViewById(R.id.text_wallet_password);
        Button confirm = findViewById(R.id.button_confirm_show_secret_info);
        View walletFragmentLayout = (ConstraintLayout) findViewById(R.id.layout_wallet);
        WalletFragment.hideUI(walletFragmentLayout);
        password.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        password.requestFocus();
        showKeyboard();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(getPassword())){
                    WalletFragment.openWalletView(walletFragmentLayout);
                    WalletFragment.walletAdapter.showSecretInfo();
                } else {
                    WalletFragment.openWalletView(walletFragmentLayout);
                }
                hideKeyboard();
                password.setText("");
                password.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            }
        });
    }
    public void setPassword(String password){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password",password);
        ButtonActions.password = password;
        editor.apply();
    }
    public static String getPassword(){
        return password;
    }
    public void showKeyboard(){
        InputMethodManager imm =
                (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.getCurrentFocus(), 0);
    }
    public void hideKeyboard(){
        InputMethodManager imm =
                (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
    public void goToWalletFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_wallet,
                null,
                navOptions
        );
    }

    public void goToTransferFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_transfer,
                null,
                navOptions
        );
    }

    public void goToRecentFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_recent,
                null,
                navOptions
        );
    }

    public void goToContactFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_contact,
                null,
                navOptions
        );
    }

    public void goToNodeFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_node,
                null,
                navOptions
        );
    }

    public void goToNewWalletFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_new_wallet,
                null,
                navOptions
        );
    }
    public void goToTransactionPendingFragment(View view){
        Navigation.findNavController(
                this,
                R.id.nav_host_fragment
        ).navigate(
                R.id.navigation_transaction_pending,
                null,
                navOptions
        );
    }

}
