package darkhost.onamhighsch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 민재 on 2016-10-16
 */
public class BoardFragment extends Fragment {
    ViewPager pager;
    BoardViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    int pages;
    CharSequence Titles[]={ "공지사항", "가정통신문" };
    int Numboftabs = 2;

    public BoardFragment() {
    }

    public static BoardFragment newInstance(int page) {
        // 코드상에서 원하는 페이지부터 시작할 수 있도록 함
        BoardFragment fragment = new BoardFragment();
        fragment.pages = page; // 0부터 시작
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sch, container, false);

        if(isMobileNetworkConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("모바일 네트워크 경고")
                    .setMessage(getString(R.string.internetwarning))
                    .setCancelable(true)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SetView(view);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Fragment MainFrag = MainFragment.newInstance();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fl_activity_main, MainFrag).commit();
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            SetView(view);
        }
        return view;
    }

    public void SetView(View view)
    {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new BoardViewPagerAdapter(this.getChildFragmentManager(),Titles,Numboftabs);

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
    }

    private boolean isMobileNetworkConnected()
    {
        // Made by Seven (seven@sevens.pe.kr)
        ConnectivityManager CManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NetworkInfo = CManager.getActiveNetworkInfo();
        if(NetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) { return true; } else { return false; }
    }
}
