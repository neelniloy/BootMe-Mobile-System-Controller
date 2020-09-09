package com.example.bootme;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView rootImage;
    private CardView powerOff,restart,recovery;
    private TextView checkText;
    private ProgressDialog progressDialog;

    private static int LOADING= 2300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerOff = findViewById(R.id.cv_power);
        restart = findViewById(R.id.cv_restart);

        recovery = findViewById(R.id.cv_recovery);

        rootImage = findViewById(R.id.check_root);
        checkText = findViewById(R.id.checkText);


        powerOff.setOnClickListener(this);
        restart.setOnClickListener(this);
        recovery.setOnClickListener(this);

        if(checkRooted()) {
            rootImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.happy));
            checkText.setTextColor(getResources().getColorStateList(R.color.colorGreen));
            checkText.setText("Device is Rooted");
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cv_power :

                if(!checkRooted()) {
                    Toast.makeText(this, "No Root Permission", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.show();
                    progressDialog.setMessage("Switching off");
                    progressDialog.setCanceledOnTouchOutside(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            try {
                                Process proc = Runtime.getRuntime()
                                        .exec(new String[]{ "su", "-c", "reboot -p" });
                                proc.waitFor();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        };

                    },LOADING);

                }

                break;
            case R.id.cv_restart :

                if(!checkRooted()) {
                    Toast.makeText(this, "No Root Permission", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.show();
                    progressDialog.setMessage("Rebooting");
                    progressDialog.setCanceledOnTouchOutside(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            try {
                                Process proc = Runtime.getRuntime()
                                        .exec(new String[]{ "su", "-c", "reboot" });
                                proc.waitFor();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        };

                    },LOADING);
                }

                break;

            case R.id.cv_recovery :
                if(!checkRooted()) {
                    Toast.makeText(this, "No Root Permission", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.show();
                    progressDialog.setMessage("Reboot into Recovery");
                    progressDialog.setCanceledOnTouchOutside(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            try {
                                Process proc = Runtime.getRuntime()
                                        .exec(new String[]{ "su", "-c", "reboot recovery" });
                                proc.waitFor();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        };

                    },LOADING);

                }

                break;

            default:
                break;
        }


    }

    public static boolean checkRooted()
    {
        try
        {
            Process p = Runtime.getRuntime().exec("su", null, new File("/"));
            DataOutputStream os = new DataOutputStream( p.getOutputStream());
            os.writeBytes("pwd\n");
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();
            p.destroy();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

}