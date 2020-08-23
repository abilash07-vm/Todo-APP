package com.example.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDialog extends DialogFragment {
    private static final String TAG = "AddDialog";
    public interface OnSubmit{
        public void submit();
    }
    private TextView title,desc, date,time;
    private RemainderDatabase db;
    private Button btnSubmit,btnDismiss;
    private OnSubmit onSubmit;
    private boolean isafter;
    private Calendar calendar=Calendar.getInstance();


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_add,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        initViews(view);

        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month++;
                                String Changedate=dayOfMonth+ "-" +month+ "-" +year;
                                date.setText(Changedate);
                            }
                        },
                        year,
                        month,
                        day);
                dialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                String changeTime=hourOfDay+":"+minute+" ";
                                SimpleDateFormat f24Hours=new SimpleDateFormat("HH:mm");
                                try {
                                    Date date=f24Hours.parse(changeTime);
                                    SimpleDateFormat f12Hours=new SimpleDateFormat("hh:mm aa");
                                    time.setText(f12Hours.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        hour,min, false
                );
                dialog.show();
            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtTitle=title.getText().toString();
                String txtDesc=desc.getText().toString();
                String txtDate= date.getText().toString();
                String txtTime=time.getText().toString();
                if(!(txtTitle.equals("")||txtDesc.equals("") ) && check(txtDate,txtTime)){
                    db.remainderDao().addRemainder(new Remainder(txtTitle,txtDesc,txtDate,txtTime,to24hr(txtTime)));
                    try{
                        onSubmit= (OnSubmit) getActivity();
                        onSubmit.submit();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dismiss();

                }else if(!isafter){
                    Toast.makeText(getActivity(),"Choose a future date and time",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    private String to24hr(String txtTime) {
        txtTime=txtTime.replaceAll(" ",":");
        String[] temp=txtTime.split(":");
        int hour=Integer.parseInt(temp[0]),min=Integer.parseInt(temp[1]);
        String am_pm=temp[2];
        if(am_pm.equalsIgnoreCase("pm") && hour!=12){
            hour+=12;
        }
        String newtime=String.valueOf(hour)+":"+String.valueOf(min);
        return newtime;
    }

    private boolean check(String txtDate, String txtTime) {
        Calendar calendar=Calendar.getInstance();
        Calendar calendar1=Calendar.getInstance();

        txtTime=txtTime.replaceAll(" ",":");
        String[] temp=txtTime.split(":");
        int hour=Integer.parseInt(temp[0]),min=Integer.parseInt(temp[1]);
        String am_pm=temp[2];
        if(am_pm.equalsIgnoreCase("pm") && hour!=12){
            hour+=12;
        }
        String[] fulldate=(txtDate.replaceAll("-",":")).split(":");
        int year=Integer.parseInt(fulldate[2]),month=Integer.parseInt(fulldate[1])-1,day=Integer.parseInt(fulldate[0]);

        calendar.set(year,month,day,hour,min);

        calendar1.setTime(Calendar.getInstance().getTime());

        if(calendar.compareTo(calendar1)<0){
            isafter=false;
            return false;
        }else{
            isafter=true;
            return true;
        }
    }


    private void initViews(View view) {
        title=view.findViewById(R.id.dialog_title);
        desc=view.findViewById(R.id.dialog_desc);
        date =view.findViewById(R.id.dialogDate);
        time=view.findViewById(R.id.dialogTime);
        btnSubmit=view.findViewById(R.id.btnsubmit);
        btnDismiss=view.findViewById(R.id.btndismiss);
        db=RemainderDatabase.getInstance(getActivity());
    }
}
