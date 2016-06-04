package darkhost.onamhighsch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 민재 on 2016-03-07.
 */
public class SchFragment extends Fragment {
    public static Context mContext;
    ViewPager pager;
    SchViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    int pages;
    static String ThisMonth, NextMonth;
    CharSequence Titles[]={ ThisMonth, NextMonth };
    int Numboftabs = 2;

    public SchFragment() {
    }

    public static SchFragment newInstance(int page) {
        // 타이틀에 월을 표기하기 위해 캘린더에서 이번달과 다음달을 가져온 후 String에 적용
        Calendar cal = Calendar.getInstance();
        int ThisMonthInt = cal.get(Calendar.MONTH) + 1;
        ThisMonth = ThisMonthInt + "월";
        cal.add(cal.MONTH, +1);
        int NextMonthInt = cal.get(Calendar.MONTH) + 1;
        NextMonth = NextMonthInt + "월";

        // 코드상에서 원하는 페이지부터 시작할 수 있도록 함
        SchFragment fragment = new SchFragment();
        fragment.pages = page; // 0부터 시작
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sch, container, false);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new SchViewPagerAdapter(this.getChildFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        pager.setOffscreenPageLimit(2);

        // 지정된 페이지로 설정
        pager.setCurrentItem(pages);
        return view;
    }
}
