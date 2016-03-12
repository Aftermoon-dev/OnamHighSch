package darkhost.onamhighsch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        CardView button = (CardView) view.findViewById(R.id.resetbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataReset();
            }
        });
        return view;
    }

    public void DataReset() {
        if (isNetworkAvailable() == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog.Builder builders = builder.setTitle("데이터 재설정")
                    .setMessage("데이터를 정말 재설정 하시겠습니까?\n에러가 나지 않는다면 하지 않으셔도 됩니다.")
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
}