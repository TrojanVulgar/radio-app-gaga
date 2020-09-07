package nemosofts.single.radio;


import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nemosofts.single.radio.Constant.Constant;

/**
 * Created by thivakaran
 */
public class AboutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView  company, email, website, contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        company = (TextView) findViewById(R.id.company);
        email = (TextView) findViewById(R.id.email);
        website = (TextView) findViewById(R.id.website);
        contact = (TextView) findViewById(R.id.contact);


        company.setText(Constant.Company);
        email.setText(Constant.Email);
        website.setText(Constant.Web_Url);
        contact.setText(Constant.Contact);

    }

}