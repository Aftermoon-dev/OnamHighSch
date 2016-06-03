package darkhost.onamhighsch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 민재 on 2016-06-03.
 */
public class TSchFrag extends Fragment {
    public static Context mContext;

    public TSchFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schfrag, container, false);
        SchLoad(view);
        return view;
    }

    public void SchLoad(View view)
    {
        String NoSch = getResources().getString(R.string.nosch);

        String schdb = new String();

        SharedPreferences data = TSchFrag.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        schdb = data.getString("sch", "");

        if (!schdb.equals("")) {
            Calendar cal = Calendar.getInstance();
            int lastDay = cal.getActualMaximum(Calendar.DATE);
            ScheduleData[] schs = new ScheduleData[lastDay];

            try {
                Document sch = Jsoup.parse(schdb);
                schs = ScheduleDataParser.parse(sch);

            } catch (Exception e) {
                Log.d("TSchFrag", "Parsing Error!");
            }

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setNestedScrollingEnabled(true);

            Log.d("TSchFrag", "This Month LastDay = " + lastDay);

            try {
                List<SchItem> items = new ArrayList<>();
                SchItem[] item = new SchItem[lastDay];

                for (int i = 0; i < lastDay; i++) {
                    if (schs[i] != null) {
                        item[i] = new SchItem(i+1, schs[i].schedule);
                    }

                    items.add(item[i]);
                }

                SchAdapter Adapter = new SchAdapter(getActivity(), items, R.layout.schfrag);
                recyclerView.setAdapter(Adapter);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                Fragment MainFrag = MainFragment.newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, MainFrag).commit();
                Toast.makeText(getActivity(), "일정을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}