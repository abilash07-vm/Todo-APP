package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RemainderAdaptor extends RecyclerView.Adapter<RemainderAdaptor.ViewHolder> {
    public interface DeleteInterface{
        void onDelete(Remainder remainder);
    }
    public interface Completed {
        void oncomplete(Remainder remainder);
    }
    private DeleteInterface deleteInterface;
    private Completed completed ;
    private Context context;
    private String activity=" ";

    public RemainderAdaptor(Context context) {
        this.context = context;
    }

    ArrayList<Remainder> remainders=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.remainder_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(remainders.get(position).getTitle());
        holder.body.setText(remainders.get(position).getBody());
        holder.date.setText(remainders.get(position).getDate());
        holder.time.setText(String.valueOf(remainders.get(position).getTime()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    deleteInterface= (DeleteInterface) context;
                                    deleteInterface.onDelete(remainders.get(position));
                                }catch (ClassCastException e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();

            }
        });
        if(activity.equals("completed")){
            holder.imgCompleted.setVisibility(View.GONE);
        }else{
            holder.imgCompleted.setVisibility(View.VISIBLE);
        }
        holder.imgCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    completed= (Completed) context;
                    completed.oncomplete(remainders.get(position));
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void setRemainders(ArrayList<Remainder> remainders) {
        this.remainders = remainders;
        notifyDataSetChanged();
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return remainders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,body,btnDelete,time,date;
        private ImageView imgCompleted;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            body=itemView.findViewById(R.id.body);
            btnDelete=itemView.findViewById(R.id.btndelete);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            imgCompleted=itemView.findViewById(R.id.imgCompleted);
        }
    }

}
