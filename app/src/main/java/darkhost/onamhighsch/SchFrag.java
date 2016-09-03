package darkhost.onamhighsch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Created by 민재 on 2016-06-04.
 */
public class SchFrag extends Fragment {
    public static Context mContext;
    String Code;

    public SchFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schfrag, container, false);
        // Arguments에서 code를 가져와 어떤 달의 일정을 원하는지 확인
        Code = getArguments().getString("code");
        SchLoad(view);
        return view;
    }

    public void SchLoad(View view)
    {
        String schdb = new String();
        Calendar cal = Calendar.getInstance();

        if(Code.equals("ThisMonth")) {
            SharedPreferences data = SchFrag.this.getActivity().getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
            schdb = data.getString("sch", "");
        }
        else if(Code.equals("NextMonth")) {
            SharedPreferences data = SchFrag.this.getActivity().getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
            schdb = data.getString("schnm", "");
            cal.add(cal.MONTH, 1);
        }

        if (!schdb.equals("")) {
            int lastDay = cal.getActualMaximum(Calendar.DATE);

            try {
                Document sch = Jsoup.parse(schdb);
                ScheduleData[] schs = ScheduleDataParser.parse(sch);

                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setNestedScrollingEnabled(true);

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
            catch (Exception e)
            {
                Fragment MainFrag = MainFragment.newInstance();
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, MainFrag).commit();
                Toast.makeText(getActivity(), "일정을 가져오는데 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else
        {
            Fragment MainFrag = MainFragment.newInstance();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, MainFrag).commit();
            Toast.makeText(getActivity(), "일정 데이터를 불러올 수 없습니다. 다운로드를 진행해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
