package darkhost.onamhighsch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 민재 on 2016-03-07.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {

    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 데이터 초기화 카드뷰
        CardView button = (CardView) view.findViewById(R.id.resetbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataReset();
            }
        });

        // 오픈소스 라이센스 카드뷰
        CardView opensource = (CardView) view.findViewById(R.id.opensource);
        opensource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://raw.githubusercontent.com/Darkhost/OnamHighSch/master/NOTICES.txt");
                Intent open = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(open);
            }
        });
        return view;
    }

    public void DataReset() {
        if (isNetworkAvailable() == true) { // 인터넷 연결 체크
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                     builder.setTitle("데이터 재설정")
                    .setMessage("데이터를 정말 재설정 하시겠습니까?\n에러가 나지 않는다면 하지 않으셔도 됩니다.")
                    .setCancelable(true)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(isMobileNetworkConnected() == true) { // 모바일 네트워크이면 경고를 띄움
                                AlertDialog.Builder warningbuilder = new AlertDialog.Builder(getActivity());
                                warningbuilder.setTitle("모바일 네트워크 경고")
                                        .setMessage(getString(R.string.intenetwarning))
                                        .setCancelable(true)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Toast.makeText(getActivity(), getString(R.string.data_reset), Toast.LENGTH_SHORT).show();
                                                DataThread t = new DataThread(getActivity());
                                                t.start();
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Toast.makeText(getActivity(), getString(R.string.cancel), Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialogwarning = warningbuilder.create();
                                dialogwarning.show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), getString(R.string.data_reset), Toast.LENGTH_SHORT).show();
                                DataThread t = new DataThread(getActivity());
                                t.start();
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connec = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private boolean isMobileNetworkConnected()
    {
        // Made by Seven (seven@sevens.pe.kr)
        ConnectivityManager CManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NetworkInfo = CManager.getActiveNetworkInfo();
        if(NetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) { return true; } else { return false; }
    }
}