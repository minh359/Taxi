package com.example.taxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvBill;
    private EditText etSearch;
    private Bill bill;
    TextView totalt;
    private int count=0;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    DBHelper mysqlitedb;
    ArrayList<Bill> listBill;
    AdapterBill listBillAdapter;
    int selectedid = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvBill = findViewById(R.id.lvBill);
        etSearch = findViewById(R.id.etSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listBillAdapter.getFilter().filter(s);
                listBillAdapter.notifyDataSetChanged();
                lvBill.setAdapter(listBillAdapter);
            }
            @Override
            public void afterTextChanged(Editable s) {
                listBillAdapter.getFilter().filter(s);
                listBillAdapter.notifyDataSetChanged();
                lvBill.setAdapter(listBillAdapter);
            }
        });
        mysqlitedb = new DBHelper(this, "BillDB", null, 1);
        mysqlitedb.createDefaultBillIfNeed();
        listBill = mysqlitedb.getAllBill();
        listBillAdapter = new AdapterBill(MainActivity.this, listBill);
        lvBill.setAdapter(listBillAdapter);
//        lvBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedid = position;
//                TextView totalt=(TextView) view.findViewById(R.id.tvTotal);
//                Float f=Float.parseFloat(totalt.getText().toString());
//                for (int i=0;i<lvBill.getCount();i++)
//                {
//                    View v = lvBill.getChildAt(i);
//                    TextView tt=(TextView) v.findViewById(R.id.tvTotal);
//                    Float ss=Float.parseFloat(tt.getText().toString());
//                    if(f<ss)
//                    {
//                        count++;
//                    }
//                }
//                Toast.makeText(MainActivity.this,"Có "+count+" hóa đơn có giá trị cao hơn hóa đơn này!",Toast.LENGTH_SHORT);
//            }
//        });
        //Khi nhấn lâu vào 1 phần tử thì toast
        lvBill.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedid = position;
                totalt=(TextView) view.findViewById(R.id.tvTotal);
                Float f=Float.parseFloat(totalt.getText().toString());
                for (int i=0;i<lvBill.getCount();i++)
                {
                    View v = lvBill.getChildAt(i);
                    TextView tt=(TextView) v.findViewById(R.id.tvTotal);
                    Float ss=Float.parseFloat(tt.getText().toString());
                    if(f<ss)
                    {
                        count++;
                    }
                }
                registerForContextMenu(lvBill);
                Toast.makeText(MainActivity.this,"Có "+count+" hóa đơn có giá trị cao hơn hóa đơn này!",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View");
        menu.add(0, MENU_ITEM_CREATE , 1, "Create");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete");
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int code = listBill.get(selectedid).getId();
        final Bill selectedBill = listBill.get(selectedid);
        Intent intent = new Intent(MainActivity.this, Crud_Bill.class);
        switch (item.getItemId())
        {
            case MENU_ITEM_VIEW:
                Toast.makeText(MainActivity.this,"ID: "+code+" Biển số xe: "+selectedBill.getCarNum()+" di chuyển quãng đường "+selectedBill.getDistance()+" gia: "+selectedBill.getTotal(),Toast.LENGTH_LONG).show();
                break;
            case MENU_ITEM_CREATE:
                startActivityForResult(intent, 100);
                break;
            case MENU_ITEM_EDIT:
                intent.putExtra("bill", selectedBill);
                startActivityForResult(intent,100);
                break;
            case MENU_ITEM_DELETE:
            {
                int id1=mysqlitedb.getBill(listBill.get(selectedid).getCarNum(),listBill.get(selectedid).getDistance(),listBill.get(selectedid).getPrice(),listBill.get(selectedid).getDiscount());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Confirm");
                builder1.setMessage("Are you sure want to delete bill " +id1
                        + "  selected id= " + selectedid );
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //xử lý kết quả dialog tại đây
                                mysqlitedb.deleteBill(id1);
                                listBillAdapter.notifyDataSetChanged();
                                listBill = mysqlitedb.getAllBill();
                                listBillAdapter = new AdapterBill(MainActivity.this, listBill);
                                //gan adapter vao cho listview
                                Log.e("1", "Del");
                                lvBill.setAdapter(listBillAdapter);
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
                break;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
        } else if (requestCode == 100 && resultCode == 201) {
            listBillAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"update successfull",Toast.LENGTH_SHORT).show();
        }
        listBill = mysqlitedb.getAllBill();
        listBillAdapter = new AdapterBill(MainActivity.this, listBill);
        lvBill.setAdapter(listBillAdapter);
        listBillAdapter.notifyDataSetChanged();
    }
    public void Add(View view) {
        Intent intent = new Intent(MainActivity.this, Crud_Bill.class);
        startActivityForResult(intent, 100);
    }
}