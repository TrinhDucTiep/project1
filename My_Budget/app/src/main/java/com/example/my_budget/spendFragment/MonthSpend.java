package com.example.my_budget.spendFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_budget.database.DataSpend;
import com.example.my_budget.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MonthSpend extends Fragment {

    private TextView budgetAmountTextView, moneyInTextView, moneyOutTextView;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef;

    //argument for update and delete
    private String postKey = "";
    private String item = "";
    private EditText mAmount;
    private Long amount = Long.parseLong("0");
    private String note = "";
    private Months months;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    public MonthSpend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        budgetAmountTextView = view.findViewById(R.id.m_budgetAmountTextView);
        moneyInTextView = view.findViewById(R.id.m_moneyIn);
        moneyOutTextView = view.findViewById(R.id.m_moneyOut);
        recyclerView = view.findViewById(R.id.m_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());

        MutableDateTime epouch = new MutableDateTime();
        epouch.setDate(0);
        DateTime now = new DateTime();
        months = Months.monthsBetween(epouch, now);
        Query query = budgetRef.orderByChild("month").equalTo(months.getMonths());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long moneyIn=0, moneyOut=0;
                long totalAmount = 0;


                for(DataSnapshot snap : snapshot.getChildren()){
                    DataSpend data = snap.getValue(DataSpend.class);
                    if(data.getItem().equals("L????ng") || data.getItem().equals("Ti???n th?????ng") || data.getItem().equals("Tr??? c???p") || data.getItem().equals("Thu nh???p kh??c")){
                        moneyIn+=data.getAmount();
                        String strMoneyIn = String.valueOf(formatter.format(moneyIn) + " ??");
                        moneyInTextView.setText(strMoneyIn);
                    }else {
                        moneyOut+=data.getAmount();
                        String strMoneyOut = String.valueOf(formatter.format(moneyOut) + " ??");
                        moneyOutTextView.setText(strMoneyOut);
                    }

                    totalAmount = moneyIn - moneyOut;
                    String strTotal = String.valueOf(formatter.format(totalAmount) + " ??");
                    budgetAmountTextView.setText(strTotal);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        MutableDateTime epouch = new MutableDateTime();
        epouch.setDate(0);
        DateTime now = new DateTime();
        months = Months.monthsBetween(epouch, now);
        Query query = budgetRef.orderByChild("month").equalTo(months.getMonths());

        FirebaseRecyclerOptions<DataSpend> options = new FirebaseRecyclerOptions.Builder<DataSpend>()
                .setQuery(query, DataSpend.class)
                .build();

        FirebaseRecyclerAdapter<DataSpend, MonthSpend.MyViewHolder> adapter = new FirebaseRecyclerAdapter<DataSpend, MonthSpend.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MonthSpend.MyViewHolder holder, int position, @NonNull DataSpend model) {
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

                // update and delete
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postKey = getRef(holder.getBindingAdapterPosition()).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        note = model.getNotes();

                        update();
                    }
                });
            }

            @NonNull
            @Override
            public MonthSpend.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MonthSpend.MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
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

    private void update(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(mView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.udItemName);
        mAmount = mView.findViewById(R.id.udAmount);
        final EditText mNote = mView.findViewById(R.id.udNote);

        mAmount.addTextChangedListener(onTextChangedListener());

        mNote.setText(note);
//        mNote.setSelection(note.length()); b??? null
        mItem.setText(item);
        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        Button updateBtn = mView.findViewById(R.id.updateBtn);
        Button deleteBtn = mView.findViewById(R.id.deleteBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetAmount = mAmount.getText().toString();
                if (budgetAmount.contains(",")) {
                    budgetAmount = budgetAmount.replaceAll(",", "");
                }
                if(TextUtils.isEmpty(budgetAmount)){
                    mAmount.setError("B???n ch??a nh???p s??? ti???n");
                    return;
                }
                amount = Long.parseLong(budgetAmount);
                note = mNote.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());

                MutableDateTime epouch = new MutableDateTime();
                epouch.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epouch, now.minusDays(4));
                months = Months.monthsBetween(epouch, now);

                DataSpend data = new DataSpend(item, date, postKey, note, amount, weeks.getWeeks(), months.getMonths());
                budgetRef.child(postKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budgetRef.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "X??a th??nh c??ng", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getContext(),task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //text wachter for editText's format
    private TextWatcher onTextChangedListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAmount.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    // DecimalFormat formatter = new DecimalFormat("###,###,###");
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    mAmount.setText(formattedString);
                    mAmount.setSelection(mAmount.getText().length());
                } catch (NumberFormatException nfe){
                    nfe.printStackTrace();
                }
                mAmount.addTextChangedListener(this);
            }
        };
    }
}