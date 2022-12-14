package com.example.my_budget.historyFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_budget.R;
import com.example.my_budget.database.DataSpend;
import com.example.my_budget.spendFragment.WeekSpend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class History extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbarHistory;
    private Button searchBtn;
    private TextView giveAmount, getAmount;
    private RecyclerView recyclerView;

    private String date;

    private FirebaseAuth mAuth;
    private DatabaseReference budetRef;

    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbarHistory = findViewById(R.id.toolbar_history);
        searchBtn = findViewById(R.id.search_btn);
        giveAmount = findViewById(R.id.giveAmount_history_txt);
        getAmount = findViewById(R.id.getAmount_history_txt);
        recyclerView = findViewById(R.id.recylerview_history);

        setSupportActionBar(toolbarHistory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("L???ch s???");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
         getAmount.setVisibility(View.GONE);
         giveAmount.setVisibility(View.GONE);

         int months = month + 1;
         String strDay = String.valueOf(dayOfMonth);
         if(dayOfMonth < 10) strDay = "0" + strDay;
         date = strDay + "-" + "0" + months + "-" + year;

        mAuth = FirebaseAuth.getInstance();
        budetRef = FirebaseDatabase.getInstance().getReference("budget").child(mAuth.getCurrentUser().getUid());
        Query query = budetRef.orderByChild("date").equalTo(date);

        FirebaseRecyclerOptions<DataSpend> options = new FirebaseRecyclerOptions.Builder<DataSpend>()
                .setQuery(query, DataSpend.class)
                .build();

        FirebaseRecyclerAdapter<DataSpend, WeekSpend.MyViewHolder> adapter = new FirebaseRecyclerAdapter<DataSpend, WeekSpend.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WeekSpend.MyViewHolder holder, int position, @NonNull DataSpend model) {
                holder.setItemAmount(formatter.format(model.getAmount()) + " ??");
                holder.setDate(model.getDate());
                holder.setItemName(model.getItem());
                holder.setNotes(model.getNotes());


                switch (model.getItem()){
                    case "??n u???ng":
                        holder.imageView.setImageResource(R.drawable.ic_food);
                        break;
                    case "Di chuy???n":
                        holder.imageView.setImageResource(R.drawable.ic_transport);
                        break;
                    case "H??a ????n ??i???n":
                        holder.imageView.setImageResource(R.drawable.ic_electric);
                        break;
                    case "H??a ????n internet":
                        holder.imageView.setImageResource(R.drawable.ic_internet);
                        break;
                    case "H??a ????n n?????c":
                        holder.imageView.setImageResource(R.drawable.ic_water);
                        break;
                    case "Thu?? nh??":
                        holder.imageView.setImageResource(R.drawable.ic_house);
                        break;
                    case "V???t nu??i":
                        holder.imageView.setImageResource(R.drawable.ic_animal);
                        break;
                    case "Mua s???m":
                        holder.imageView.setImageResource(R.drawable.ic_shopping);
                        break;
                    case "Gi??o d???c":
                        holder.imageView.setImageResource(R.drawable.ic_education);
                        break;
                    case "S???c kh???e":
                        holder.imageView.setImageResource(R.drawable.ic_health);
                        break;
                    case "L??m ?????p":
                        holder.imageView.setImageResource(R.drawable.ic_beauty);
                        break;
                    case "Gi???i tr??":
                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
                        break;
                    case "Chi ti??u kh??c":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;
                    case "L????ng":
                        holder.imageView.setImageResource(R.drawable.ic_salary);
                        break;
                    case "Ti???n th?????ng":
                        holder.imageView.setImageResource(R.drawable.ic_bonus);
                        break;
                    case "Tr??? c???p":
                        holder.imageView.setImageResource(R.drawable.ic_parents);
                        break;
                    case "Thu nh???p kh??c":
                        holder.imageView.setImageResource(R.drawable.ic_other_money_in);
                        break;
                }

            }

            @NonNull
            @Override
            public WeekSpend.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new WeekSpend.MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long moneyIn=0, moneyOut=0;
                long totalAmount = 0;


                for(DataSnapshot snap : snapshot.getChildren()){
                    DataSpend data = snap.getValue(DataSpend.class);
                    if(data.getItem().equals("L????ng") || data.getItem().equals("Ti???n th?????ng") || data.getItem().equals("Tr??? c???p") || data.getItem().equals("Thu nh???p kh??c")){
                        moneyIn+=data.getAmount();
                        String strMoneyIn = String.valueOf("Thu: " + formatter.format(moneyIn )+ " ??");
                        getAmount.setText(strMoneyIn);
                        getAmount.setVisibility(View.VISIBLE);
                    }else {
                        moneyOut+=data.getAmount();
                        String strMoneyOut = String.valueOf("Chi: " + formatter.format(moneyOut) + " ??");
                        giveAmount.setText(strMoneyOut);
                        giveAmount.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageView imageView;
        public TextView notes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
        }

        public void setItemName(String itemName){
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }

        public void setItemAmount(String itemAmount){
            TextView amount = mView.findViewById(R.id.amount);
            amount.setText(itemAmount);
        }

        public void setDate(String itemDate){
            TextView date = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }

        public void setNotes(String itemNote){
            TextView note = mView.findViewById(R.id.note);
            note.setText(itemNote);
        }
    }

}