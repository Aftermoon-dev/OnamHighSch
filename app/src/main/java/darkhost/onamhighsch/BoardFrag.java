package darkhost.onamhighsch;

/**
 * Created by 민재 on 2016-10-09.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class BoardFrag extends Fragment {
    String Code;
    NoticeDownloadProcess noticeDownloadProcess;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    public BoardFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.boardfrag, container, false);
        // Get Code
        Code = getArguments().getString("code");

        // Recycler View Setting
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        // Notice Download Process
        if(isNetworkAvailable()) {
            try {
                noticeDownloadProcess = new NoticeDownloadProcess(view.getContext());
                noticeDownloadProcess.execute();
            } catch (Exception e) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
                Fragment MainFrag = MainFragment.newInstance();
                if(fragment != MainFrag) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_activity_main, MainFrag).commit();
                    Toast.makeText(getActivity(), "공지사항을 가져오는데 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
            Fragment MainFrag = MainFragment.newInstance();
            if(fragment != MainFrag) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, MainFrag).commit();
                Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connec = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connec.getNetworkInfo(0);
        NetworkInfo wifiInfo = connec.getNetworkInfo(1);
        NetworkInfo wimaxInfo = connec.getNetworkInfo(6);
        boolean bm = false;
        boolean bw = false;
        boolean bx = false;
        if (mobileInfo != null) bm = mobileInfo.isConnected();
        if (wimaxInfo != null) bx = wimaxInfo.isConnected();
        if (wifiInfo != null) bw = wifiInfo.isConnected();
        return (bm || bw || bx);
    }

    private class NoticeDownloadProcess extends AsyncTask<ArrayList<String>, ArrayList<String>, ArrayList<String>> {
        ArrayList<String> Notice = new ArrayList<>();
        ProgressDialog dialog = new ProgressDialog(getActivity());
        Context Context;

        public NoticeDownloadProcess(Context context) {
            Context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("공지사항을 불러오는중..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... arrayLists) {
            try {
                if (Code == "Notice") {
                    Notice = HPParser.SchoolHPParser("http://onam.hs.kr/board.list?mcode=1110");
                } else if (Code == "PNotice") {
                    Notice = HPParser.SchoolHPParser("http://onam.hs.kr/board.list?mcode=1112");
                }
            } catch (Exception e) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
                Fragment MainFrag = MainFragment.newInstance();
                if(fragment != MainFrag) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_activity_main, MainFrag).commit();
                    Toast.makeText(getActivity(), "학교 홈페이지에 접속할 수 없었습니다. 잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            return Notice;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if(strings != null) {
                try {
                    List<BoardItem> items = new ArrayList<>();
                    BoardItem[] item = new BoardItem[strings.size()];

                    for (int i = 0; i < strings.size(); i++) {
                        if (strings.get(i) != null) {
                            String[] Data = strings.get(i).split(";");
                            item[i] = new BoardItem(Data[0], Data[2] + " 선생님 / " + Data[3], Data[1]);
                        }
                        items.add(item[i]);
                    }

                    BoardAdapter Adapter = new BoardAdapter(Context, items, R.layout.boardfrag);
                    recyclerView.setAdapter(Adapter);
                } catch (Exception e) {
                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
                    Fragment MainFrag = MainFragment.newInstance();
                    if(fragment != MainFrag) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_activity_main, MainFrag).commit();
                        Toast.makeText(getActivity(), "공지사항을 가져오는데 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
                Fragment MainFrag = MainFragment.newInstance();
                if(fragment != MainFrag) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_activity_main, MainFrag).commit();
                    Toast.makeText(getActivity(), "공지사항을 가져오는데 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
            super.onPostExecute(strings);
        }
    }
}


