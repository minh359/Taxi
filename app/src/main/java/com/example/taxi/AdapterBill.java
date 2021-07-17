package com.example.taxi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterBill extends BaseAdapter implements Filterable {
    private Activity activity;
    //Khai báo đối tượng Arraylist<User> là nguồn dữ liệu cho Adapter
    private ArrayList<Bill> data;
    //Khai báo đối tương LayoutInflater để phân tích giao diện một phần tử
    private LayoutInflater inflater;
    private ArrayList<Bill> databackup;

    public AdapterBill(DialogInterface.OnClickListener onClickListener, ArrayList<Bill> databackup) {
        this.databackup = databackup;
    }
    public AdapterBill(Activity activity, ArrayList<Bill> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
    {
        v = inflater.inflate(R.layout.bill_item, null);
        EditText number=v.findViewById(R.id.etCarNum_main);
        number.setText(data.get(position).getCarNum().toString());
        EditText distance=v.findViewById(R.id.etDistance_Main);
        String dis=String.valueOf(data.get(position).getDistance());
        distance.setText(dis+" km");
        TextView total=v.findViewById(R.id.tvTotal);
//        float quangduong=data.get(position).getDistance();
//        int gia=data.get(position).getPrice();
//        int giam=data.get(position).getDiscount();
//        float tien=quangduong*gia*(100-giam)/100;
//        String hien=String.valueOf(tien);
        String hien=String.valueOf(data.get(position).getTotal());
        total.setText(hien);
    }
        return v;
    }
    @Override
    public Filter getFilter() {Filter f=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults fr=new FilterResults();
            Float f=0.0f;
            int flag=0;
            try
            {
                f=Float.parseFloat(constraint.toString());
                flag=1;
            }
            catch (Exception e){
                flag=0;
            }
            if(databackup==null)
                databackup=new ArrayList<>(data);
            if(constraint==null|| constraint.length()==0)
            {
                fr.values=databackup;
                fr.count=databackup.size();
            }
            else{
                ArrayList<Bill> newdata=new ArrayList<>();
                for (Bill u:data)
//                    if (u.getCarNum().toLowerCase().contains(constraint.toString().toLowerCase()))
                    if (u.getTotal()>f || constraint.toString()=="" || flag==0)
                        newdata.add(u);
                fr.values=newdata;
                fr.count=newdata.size();
            }
            return  fr;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Bill> tmp=(ArrayList<Bill>)results.values;
            data.clear();
            for (Bill c:tmp)
                data.add(c);
            notifyDataSetChanged();
        }
    };
        return f;
    }
}
