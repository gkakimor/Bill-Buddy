package com.example.bill_buddy_v3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bill_buddy_v3.R;
import com.example.bill_buddy_v3.model.Bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<Bill> {

    public BillAdapter(Context context,
                       ArrayList<Bill> billList)
    {
        super(context, 0, billList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bill_list, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtFrequency = convertView.findViewById(R.id.txtFrequency);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount);
        TextView txtDueDate = convertView.findViewById(R.id.txtDueDate);
        TextView txtSelectBill = convertView.findViewById(R.id.txtSelectBill);
        Bill currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            if (currentItem.getId() != 999) {
                txtName.setText(currentItem.getPayee());
                txtFrequency.setText(currentItem.getFrequency().getFrequency());
                //txtAmount.setText(Double.toString(currentItem.getAmount()));
                txtAmount.setText(currentItem.getFormattedAmount());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String formatedDate = sdf.format(currentItem.getDue_date());
                txtDueDate.setText(formatedDate);
            } else {
                txtSelectBill.setText(currentItem.getPayee());
            }
        }
        return convertView;
    }
}
