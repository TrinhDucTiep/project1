package com.example.my_budget.analyzeFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_budget.database.DataSpend;
import com.example.my_budget.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TodayAnalyze extends Fragment {

    private PieChart pieChart;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef;

    private long totalFood, totalTransport, totalHouse, totalWater, totalElectric, totalInternet;
    private long totalAnimal, totalShopping, totalEdu, totalHealth, totalBeauty, totalEnt, totalOther, totalSalary, totalBonus, totalParents, totalOtherMoneyIn;
    private long res, res1, sum, sum1;

    public TodayAnalyze() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_today_analyze, container, false);

        mAuth = FirebaseAuth.getInstance();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String dateToday = dateFormat.format(calendar.getTime());
        Query query = budgetRef.orderByChild("date").equalTo(dateToday);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalFood=0; totalTransport=0; totalHouse=0; totalWater=0; totalElectric=0; totalInternet=0;
                totalAnimal=0; totalShopping=0; totalEdu=0; totalHealth=0; totalBeauty=0; totalEnt=0; totalOther=0; totalSalary=0; totalBonus=0; totalParents=0; totalOtherMoneyIn=0;
                sum = 0; res = 0; res1 = 0; sum1=0;
                DataSpend data;
                for(DataSnapshot snap : snapshot.getChildren()){
                    data = snap.getValue(DataSpend.class);
                    sum += data.getAmount();
                    if(data.getItem().equals("??n u???ng")){
                        totalFood += data.getAmount();
                    } else if(data.getItem().equals("Di chuy???n")){
                        totalTransport += data.getAmount();
                    } else if(data.getItem().equals("Thu?? nh??")){
                        totalHouse += data.getAmount();
                    } else if(data.getItem().equals("H??a ????n n?????c")){
                        totalWater += data.getAmount();
                    } else if(data.getItem().equals("H??a ????n ??i???n")){
                        totalElectric += data.getAmount();
                    } else if(data.getItem().equals("H??a ????n internet")){
                        totalInternet += data.getAmount();
                    } else if(data.getItem().equals("V???t nu??i")){
                        totalAnimal += data.getAmount();
                    } else if(data.getItem().equals("Mua s???m")){
                        totalShopping += data.getAmount();
                    } else if(data.getItem().equals("Gi??o d???c")){
                        totalEdu += data.getAmount();
                    } else if(data.getItem().equals("S???c kh???e")){
                        totalHealth += data.getAmount();
                    } else if(data.getItem().equals("L??m ?????p")){
                        totalBeauty += data.getAmount();
                    } else if(data.getItem().equals("Gi???i tr??")){
                        totalEnt += data.getAmount();
                    } else if(data.getItem().equals("Chi ti??u kh??c")){
                        totalOther += data.getAmount();
                    } else if(data.getItem().equals("L????ng")){
                        totalSalary += data.getAmount();
                        sum -= data.getAmount();
                        sum1+=data.getAmount();
                    } else if(data.getItem().equals("Ti???n th?????ng")){
                        totalBonus += data.getAmount();
                        sum -= data.getAmount();
                        sum1+=data.getAmount();
                    } else if(data.getItem().equals("Tr??? c???p")){
                        totalParents += data.getAmount();
                        sum -= data.getAmount();
                        sum1+=data.getAmount();
                    } else if(data.getItem().equals("Thu nh???p kh??c")){
                        totalOtherMoneyIn += data.getAmount();
                        sum -= data.getAmount();
                        sum1+=data.getAmount();
                    }
                }
                //pie chart for money out
                PieChart pieChart = mView.findViewById(R.id.td_piechart);
                ArrayList<PieEntry> numbers = new ArrayList<>();
                if((float)totalFood/sum >= 0.05) numbers.add(new PieEntry(totalFood, "??n u???ng"));
                else { res+=totalFood; }
                if((float)totalTransport/sum >= 0.05) numbers.add(new PieEntry(totalTransport, "Di chuy???n"));
                else { res+=totalTransport; }
                if((float)totalHouse/sum >= 0.05) numbers.add(new PieEntry(totalHouse, "Thu?? nh??"));
                else { res+=totalHouse; }
                if((float)totalWater/sum >= 0.05) numbers.add(new PieEntry(totalWater, "H??a ????n n?????c"));
                else { res+=totalWater; }
                if((float)totalElectric/sum >= 0.05) numbers.add(new PieEntry(totalElectric, "H??a ????n ??i???n"));
                else { res+=totalElectric; }
                if((float)totalInternet/sum >= 0.05) numbers.add(new PieEntry(totalInternet, "H??a ????n internet"));
                else { res+=totalInternet; }
                if((float)totalAnimal/sum >= 0.05) numbers.add(new PieEntry(totalAnimal, "V???t nu??i"));
                else { res+=totalAnimal; }
                if((float)totalShopping/sum >= 0.05) numbers.add(new PieEntry(totalShopping, "Mua s???m"));
                else { res+=totalShopping; }
                if((float)totalEdu/sum >= 0.05) numbers.add(new PieEntry(totalEdu, "Gi??o d???c"));
                else { res+=totalEdu; }
                if((float)totalHealth/sum >= 0.05) numbers.add(new PieEntry(totalHealth, "S???c kh???e"));
                else { res+=totalHealth; }
                if((float)totalBeauty/sum >= 0.05) numbers.add(new PieEntry(totalBeauty, "L??m ?????p"));
                else { res+=totalBeauty; }
                if((float)totalEnt/sum >= 0.05) numbers.add(new PieEntry(totalEnt, "Gi???i tr??"));
                else { res+=totalEnt; }
                if((float)totalOther/sum >= 0.05) numbers.add(new PieEntry(totalOther, "Chi ti??u kh??c"));
                else { res+=totalOther; }
                if((float)res/sum >= 0.04) numbers.add(new PieEntry(res, "...     "));

                PieDataSet pieDataSet = new PieDataSet(numbers, "");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);
                pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterText("Chi ti??u");
                //pieChart.showContextMenu();
                //pieChart.animate();
                pieChart.setExtraBottomOffset(20f);
                pieChart.setExtraLeftOffset(40f);
                pieChart.setExtraRightOffset(40f);
                //pieChart.setHoleRadius(30f);
                pieChart.animateY(1000);

                //piechart for money in

                PieChart pieChart1 = mView.findViewById(R.id.td_piechart1);
                ArrayList<PieEntry> numbers1 = new ArrayList<>();
                if((float)totalSalary/sum1 >= 0.05) numbers1.add(new PieEntry(totalSalary, "L????ng"));
                else { res1+=totalSalary; }
                if((float)totalBonus/sum1 >= 0.05) numbers1.add(new PieEntry(totalBonus, "Ti???n th?????ng"));
                else { res1+=totalBonus; }
                if((float)totalParents/sum1 >= 0.05) numbers1.add(new PieEntry(totalParents, "Tr??? c???p"));
                else { res1+=totalParents; }
                if((float)totalOtherMoneyIn/sum1 >= 0.05) numbers1.add(new PieEntry(totalOtherMoneyIn, "Thu nh???p kh??c"));
                else { res1+=totalOtherMoneyIn; }
                if((float)res1/sum1 >= 0.04) numbers1.add(new PieEntry(res1, "...     "));

                PieDataSet pieDataSet1 = new PieDataSet(numbers1, "");
                pieDataSet1.setColors(ColorTemplate.LIBERTY_COLORS);
                pieDataSet1.setValueTextColor(Color.BLACK);
                pieDataSet1.setValueTextSize(16f);
                pieDataSet1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                PieData pieData1 = new PieData(pieDataSet1);

                pieChart1.setData(pieData1);
                pieChart1.getDescription().setEnabled(false);
                pieChart1.setEntryLabelColor(Color.BLACK);
                pieChart1.setExtraBottomOffset(20f);
                pieChart1.setExtraLeftOffset(40f);
                pieChart1.setCenterText("Thu nh???p");
                pieChart1.setExtraRightOffset(40f);
                pieChart1.animateY(1000);

                pieChart.invalidate();
                pieChart1.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mView;
    }
}