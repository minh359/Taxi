package com.example.taxi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TableName = "BillTaxi";
    public static final   String Id="Id";
    public static final   String CarNum="CarNumber";
    public static final   String Distance="Distance";
    public static final   String Price="Price";
    public static final   String Discount="Discount";
    public DBHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "Create table if not exists " + TableName + "("
                + Id + " Integer Primary key AUTOINCREMENT, "
                + CarNum + " Text,"
                + Distance + " Float,"
                + Price + " Interger,"
                + Discount + " Interger)";
        //chạy câu truy vấn SQL để tạo bảng
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TableName);
        onCreate(db);
    }
    public void createDefaultBillIfNeed()  {
        int count = this.getBillCount();
        if(count ==0 ) {
            Bill bill1 = new Bill("29A1-11720", 17,120000,5);
            Bill bill2 = new Bill("29R7-03034", 22,180000,7);
            Bill bill3 = new Bill("09B9-09934", 30,160000,15);
            Bill bill4 = new Bill("30K4-98514", 120,110000,10);
            this.addBill(bill1);
            this.addBill(bill2);
            this.addBill(bill3);
            this.addBill(bill4);
        }
    }
    public ArrayList<Bill> getAllBill()
    {
        ArrayList<Bill> list = new ArrayList<>();
        String sql = "Select * from " + TableName+ " ORDER BY Id ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor!=null)
            while (cursor.moveToNext())
            {
                Bill item = new Bill(cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getInt(3),
                        cursor.getInt(4));
                item.setId(cursor.getInt(0));
                list.add(item);
            }
        return list;
    }
    public void addBill(Bill item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(CarNum,item.getCarNum());
        value.put(Distance,item.getDistance());
        value.put(Price,item.getPrice());
        value.put(Discount,item.getDiscount());
        db.insert(TableName,null, value);
        db.close();
    }

    public  void updateBill(Bill item)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues value=new ContentValues();

        value.put(Id,item.getId());
        value.put(CarNum,item.getCarNum());
        value.put(Distance,item.getDistance());
        value.put(Price,item.getPrice());
        value.put(Discount,item.getDiscount());
        String strSQL = "UPDATE "+TableName+" SET CarNumber='"+value.get(CarNum)+
                "',Distance="+value.get(Distance)+"," +
                " Price="+value.get(Price)+"," +
                " Discount="+value.get(Discount)+
                " WHERE Id = "+ value.get(Id);

        db.execSQL(strSQL);
        db.close();
    }
    public  void deleteBill(int id)
    {
        SQLiteDatabase db=getWritableDatabase();
        String sql="Delete From "+TableName+" Where Id="+id;
        Log.e("1", sql);
        db.execSQL(sql);
        db.close();
    }
    public int getBillCount() {

        String countQuery = "SELECT  * FROM " + TableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int getBill(String carNum,float distance,int price,int discount)
    {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select Id from "+TableName+" Where CarNumber='"+carNum+
                "' and Distance='"+distance+"' and " +
                " Price='"+price+"' and" +
                " Discount='"+discount+"'";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor!=null)
            while (cursor.moveToNext())
            {
                id=cursor.getInt(0);
            }
        return id;
    }
}
