package darkhost.onamhighsch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Calendar;

/**
 * Created by 민재 on 2016-03-07.
 */

public class MainFragment extends Fragment {
    String TodayTable;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // 급식 및 일정을 메인 액티비티에 불러옴
        MealSetting(view);
        SchSetting(view);

        // 시간표 로드
        TimeTable();
        TextView TimeTable = (TextView) view.findViewById(R.id.timetable);
        if(!TodayTable.equals("")) { TimeTable.setText(TodayTable); }

        // 카드뷰를 클릭해서 각 프레그먼트로 이동할 수 있도록 함
        CardView Lunch = (CardView) view.findViewById(R.id.lunchcard);
        Lunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment MealFrag = MealFragment.newInstance(0);
                if(MainActivity.FabMenu.isExpanded()) {
                    MainActivity.FabMenu.collapse();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, MealFrag).commit();
            }
        });

        CardView Dinner = (CardView) view.findViewById(R.id.dinnercard);
        Dinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment MealFrag = MealFragment.newInstance(1);
                if(MainActivity.FabMenu.isExpanded()) {
                    MainActivity.FabMenu.collapse();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, MealFrag).commit();
            }
        });

        CardView Sch = (CardView) view.findViewById(R.id.schcard);
        Sch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment SchFrag = SchFragment.newInstance(0);
                if(MainActivity.FabMenu.isExpanded()) {
                    MainActivity.FabMenu.collapse();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, SchFrag).commit();
            }
        });

        CardView TimeTables = (CardView) view.findViewById(R.id.timetablecard);
        TimeTables.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Fragment TimeTableFrag = TimeTableFragment.newInstance();
                if(MainActivity.FabMenu.isExpanded()) {
                    MainActivity.FabMenu.collapse();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, TimeTableFrag).commit();
            }
        });

        return view;
    }

    // 급식을 메인 프레그먼트에 표기
    public void MealSetting(View view)
    {
        String NoMeal = getResources().getString(R.string.nomeal);
        Calendar cal = Calendar.getInstance();
        int days = cal.get(Calendar.DAY_OF_MONTH); // 오늘 날짜의 급식을 찾아오기 위해 오늘 날짜를 찾아옴

        // 급식 정보가 담긴 SP를 가져옴
        SharedPreferences data = MainFragment.this.getActivity().getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdb = data.getString("meal", "");

        TextView lunch = (TextView) view.findViewById(R.id.lunch);
        TextView dinner = (TextView) view.findViewById(R.id.dinner);

        if (!mealdb.equals("")) {
            try {
                Document meal = Jsoup.parse(mealdb);
                MenuData[] meals = MenuDataParser.parse(meal);
                String lunchdata = meals[days - 1].lunch;
                lunch.setText(lunchdata);
                String dinnerdata = meals[days - 1].dinner;
                dinner.setText(dinnerdata);
            } catch (Exception e) {
                lunch.setText(NoMeal);
                dinner.setText(NoMeal);
                Toast.makeText(getActivity(), "급식을 가져오는데 오류가 발생했습니다. 초기화를 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            lunch.setText(NoMeal);
            dinner.setText(NoMeal);
        }
    }

    // 일정을 메인 프레그먼트에 표기
    public void SchSetting(View view) {
        String NoSch = getResources().getString(R.string.nosch);
        Calendar cal = Calendar.getInstance();
        int days = cal.get(Calendar.DAY_OF_MONTH);

        SharedPreferences data = MainFragment.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        String schdb = data.getString("sch", "");
        TextView today = (TextView) view.findViewById(R.id.sch);

        if (!schdb.equals("")) {
            try {
                Document sch = Jsoup.parse(schdb);
                ScheduleData[] schs = ScheduleDataParser.parse(sch);
                String todaydata = schs[days - 1].schedule;
                today.setText(todaydata);
            } catch (Exception e) {
                today.setText(NoSch);
                Toast.makeText(getActivity(), "일정을 가져오는데 오류가 발생했습니다. 초기화를 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            today.setText(NoSch);
        }
    }

    // 시간표 표기
    public void TimeTable() {

        Calendar cal = Calendar.getInstance();
        int dw = cal.get(Calendar.DAY_OF_WEEK);

        if (dw == 1 || dw == 7) { // 주말일때는 주말로 표시
            TodayTable = getString(R.string.Weekend);
        }
        if (dw == 2) {
            SharedPreferences montable = MainFragment.this.getActivity().getSharedPreferences("montable", Context.MODE_MULTI_PROCESS);
            TodayTable = montable.getString("montable", getString(R.string.notime));
        }

        if (dw == 3) {
            SharedPreferences tuetable = MainFragment.this.getActivity().getSharedPreferences("tuetable", Context.MODE_MULTI_PROCESS);
            TodayTable = tuetable.getString("tuetable", getString(R.string.notime));
        }

        if (dw == 4) {
            SharedPreferences wedtable = MainFragment.this.getActivity().getSharedPreferences("wedtable", Context.MODE_MULTI_PROCESS);
            TodayTable = wedtable.getString("wedtable", getString(R.string.notime));
        }

        if (dw == 5) {
            SharedPreferences thutable = MainFragment.this.getActivity().getSharedPreferences("thutable", Context.MODE_MULTI_PROCESS);
            TodayTable = thutable.getString("thutable", getString(R.string.notime));
        }

        if (dw == 6) {
            SharedPreferences fritable = MainFragment.this.getActivity().getSharedPreferences("fritable", Context.MODE_MULTI_PROCESS);
            TodayTable = fritable.getString("fritable", getString(R.string.notime));
        }
    }

}
