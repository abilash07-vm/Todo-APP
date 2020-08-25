package com.example.todoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements AddDialog.OnSubmit, RemainderAdaptor.DeleteInterface , RemainderAdaptor.Completed {
    private static final String TAG = "MainActivity";
    @Override
    public void oncomplete(Remainder remainder) {
        db.remainderDao().setCompleted(remainder.getId(),true);
        setAdapter("");
    }

    @Override
    public void onDelete(Remainder remainder) {
        db.remainderDao().deleteRemainder(remainder);
        setAdapter("");
        new getRemainderReached().cancel(true);
        new getRemainderReached().execute();
    }

    @Override
    public void submit() {
        setAdapter("");
        new getRemainderReached().execute();
    }

    private FloatingActionButton fab;
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    static RemainderDatabase db;
    static ArrayList<Remainder> remainders;
    static RemainderAdaptor adapter;
    private Button btnGoBack;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        setSupportActionBar(toolbar);
        toolbar.setElevation(4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setAdapter("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about_us:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Designed and Developed by Abilash\n For more details vist my website.")
                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(MainActivity.this,WebActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                        builder.create().show();
                        break;
                    case R.id.completed:
                        setAdapter("completed");
                        fab.setVisibility(View.GONE);
                        btnGoBack.setVisibility(View.VISIBLE);
                        btnGoBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setAdapter("");
                                fab.setVisibility(View.VISIBLE);
                                btnGoBack.setVisibility(View.GONE);
                            }
                        });

                        break;
                    default:
                        break;
                }
                return false;
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog addDialog=new AddDialog();
                addDialog.show(getSupportFragmentManager(),"dialog");
                new getRemainderReached().cancel(true);

            }
        });
    }

    private void initViews() {
        fab=findViewById(R.id.fab);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyView);
        db=RemainderDatabase.getInstance(this);
        adapter=new RemainderAdaptor(this);
        btnGoBack=findViewById(R.id.btngoBack);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    private class getRemainderReached extends AsyncTask<Void,Remainder,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Remainder> remainders= (ArrayList<Remainder>) db.remainderDao().getALlRemaindersByCompleteBySmall(false);
            int i=0;
            for (Remainder remainder:remainders) {
                while (!remainder.isIscomplete()) {
                    if (isCancelled()) {
                        return null;
                    }
                    if (check(remainder.getDate(), remainder.getT24hrtime())) {
                        db.remainderDao().setCompleted(remainder.getId(), true);
                        remainder.setIscomplete(true);
                        new getRemainderReached().cancel(true);
                        publishProgress(remainder);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        private boolean check(String txtDate, String txtTime) {
            Calendar calendar=Calendar.getInstance();
            Calendar calendar1=Calendar.getInstance();

            txtTime=txtTime.replaceAll(" ",":");
            String[] temp=txtTime.split(":");
            int hour=Integer.parseInt(temp[0]),min=Integer.parseInt(temp[1]);
            String[] fulldate=(txtDate.replaceAll("-",":")).split(":");
            int year=Integer.parseInt(fulldate[2]),month=Integer.parseInt(fulldate[1]),day=Integer.parseInt(fulldate[0]);

            calendar.set(year,month-1,day,hour,min);

            calendar1.setTime(Calendar.getInstance().getTime());
            Log.d(TAG, "check: checkFunction "+calendar.getTime()+"\n"+calendar1.getTime()+String.valueOf(calendar.compareTo(calendar1) <= 0));
            return calendar.compareTo(calendar1) <= 0;
        }
        @Override
        protected void onProgressUpdate(Remainder... values) {
            super.onProgressUpdate(values);
            new notifyAsyncTask(values[0]).execute();
        }
    }

    public class notifyAsyncTask extends AsyncTask<Remainder,Integer,Void>{
        Remainder remainder;
        public notifyAsyncTask(Remainder remainder) {
            this.remainder=remainder;
        }

        @Override
        protected Void doInBackground(Remainder... remainders) {
                try {
                    Thread.sleep(1000);
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled: Current task is cancelled");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,2,intent,0);


            NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this,"channel1")
                    .setSmallIcon(R.drawable.ic_bell)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentTitle(remainder.getTitle())
                    .setContentIntent(pendingIntent)
                    .setContentText(remainder.getBody());
            NotificationManagerCompat manager=NotificationManagerCompat.from(MainActivity.this);
            manager.notify(remainder.getId(),builder.build());
            setAdapter("");
            new getRemainderReached().execute();
        }

    }
    public static void setAdapter(String a){
        if( a.equals("completed")){
            remainders= (ArrayList<Remainder>) db.remainderDao().getALlRemaindersByCompleteBySmall(true);
        }else{
            remainders= (ArrayList<Remainder>) db.remainderDao().getALlRemaindersByCompleteBySmall(false);
        }
        adapter.setActivity(a);
        adapter.setRemainders(remainders);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new getRemainderReached().cancel(true);
        new getRemainderReached().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new getRemainderReached().cancel(true);
        new getRemainderReached().execute();
    }
}