package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Crud_Bill extends AppCompatActivity {
    private EditText name;
    private EditText distance;
    private EditText discount;
    private EditText price;
    private Button submit;
    private Button cancel;
    private Bill bill;
    private int id;
    private boolean needRefresh;
    private int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud__bill);
        name=findViewById(R.id.et_CarNum_crud);
        discount=findViewById(R.id.et_Discount_crud);
        distance=findViewById(R.id.et_Distance_crud);
        price=findViewById(R.id.et_Price_crud);
        submit=findViewById(R.id.btn_edit);
        cancel=findViewById(R.id.btn_back);
        submit.setOnClickListener(v -> buttonSubmitClicked());
        cancel.setOnClickListener(v -> buttonBackClicked());
        Intent intent = this.getIntent();
        this.bill = (Bill) intent.getSerializableExtra("bill");
        if(bill== null)  {
            submit.setText("Thêm");
        } else  {
            submit.setText("Sửa");
            this.name.setText(bill.getCarNum());
            String kc=String.valueOf(bill.getDistance());
            this.distance.setText(kc);
            this.discount.setText(""+bill.getDiscount());
            this.price.setText(""+bill.getPrice());
        }
    }

    private void buttonSubmitClicked() {
        DBHelper db=new DBHelper(this, "BillDB", null, 1);

        String xe =  name.getText().toString();
        float quangduong =  distance.getText().toString().equals("")?0:Float.parseFloat(distance.getText().toString());
        int gia =  price.getText().toString().equals("")?0:Integer.parseInt(price.getText().toString());
        int giam =  discount.getText().toString().equals("")?0:Integer.parseInt( discount.getText().toString());
        if(xe.equals("") || quangduong==0||gia==0||giam<0) {
            Toast.makeText(getApplicationContext(),
                    "Please enter data again ", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Crud_Bill.this, MainActivity.class);
        if(submit.getText().equals("Thêm") ) {
            db.addBill(new Bill(xe,quangduong,gia,giam));
            this.setResult(200,intent);
        } else  {
            Bill ne = new Bill(xe,quangduong,gia,giam);
            ne.setId(bill.getId());
            Log.e("1", String.valueOf(bill.getId()));
            db.updateBill(ne);
            this.setResult(201,intent);
        }
        finish();
    }

    private void buttonBackClicked() {
        this.onBackPressed();
    }
    @Override
    public void finish() {

        super.finish();
    }
}
