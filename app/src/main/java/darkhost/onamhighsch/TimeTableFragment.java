package darkhost.onamhighsch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 민재 on 2016-03-07.
 */
public class TimeTableFragment extends Fragment {
    String MonTables, TueTables, WedTables, ThuTables, FriTables;

    public TimeTableFragment() {

    }

    public static TimeTableFragment newInstance() {
        TimeTableFragment fragment = new TimeTableFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        EditText Mon = (EditText) view.findViewById(R.id.montable);
        EditText Tue = (EditText) view.findViewById(R.id.tuetable);
        EditText Wed = (EditText) view.findViewById(R.id.wedtable);
        EditText Thu = (EditText) view.findViewById(R.id.thutable);
        EditText Fri = (EditText) view.findViewById(R.id.fritable);
        GetData();
        Mon.setText(MonTables);
        Tue.setText(TueTables);
        Wed.setText(WedTables);
        Thu.setText(ThuTables);
        Fri.setText(FriTables);

        Mon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                MonTables = s.toString();
            }
        });

        Tue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                TueTables = s.toString();
            }
        });

        Wed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                WedTables = s.toString();
            }
        });

        Thu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                ThuTables = s.toString();
            }
        });
        Fri.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                FriTables = s.toString();
            }
        });

        return view;
    }

    public void onDestroyView() {
        SaveData();
        super.onDestroyView();
    }

    public void GetData()
    {
        SharedPreferences montable = getActivity().getSharedPreferences("montable", Context.MODE_MULTI_PROCESS);
        MonTables = montable.getString("montable", "");

        SharedPreferences tuetable = getActivity().getSharedPreferences("tuetable", Context.MODE_MULTI_PROCESS);
        TueTables = tuetable.getString("tuetable", "");

        SharedPreferences wedtable = getActivity().getSharedPreferences("wedtable", Context.MODE_MULTI_PROCESS);
        WedTables = wedtable.getString("wedtable", "");

        SharedPreferences thutable = getActivity().getSharedPreferences("thutable", Context.MODE_MULTI_PROCESS);
        ThuTables = thutable.getString("thutable", "");

        SharedPreferences fritable = getActivity().getSharedPreferences("fritable", Context.MODE_MULTI_PROCESS);
        FriTables = fritable.getString("fritable", "");
    }

    // 내용이 비었을경우엔 저장하지 않도록 함
    public void SaveData() {
        if (!MonTables.equals("")) {
            SharedPreferences MonTable = getActivity().getSharedPreferences("montable", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor MonTableEdit = MonTable.edit();
            MonTableEdit.putString("montable", MonTables);
            MonTableEdit.commit();
        }

        if (!TueTables.equals("")) {
            SharedPreferences TueTable = getActivity().getSharedPreferences("tuetable", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor TueTableEdit = TueTable.edit();
            TueTableEdit.putString("tuetable", TueTables);
            TueTableEdit.commit();
        }

        if (!WedTables.equals("")) {
            SharedPreferences WedTable = getActivity().getSharedPreferences("wedtable", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor WedTableEdit = WedTable.edit();
            WedTableEdit.putString("wedtable", WedTables);
            WedTableEdit.commit();
        }

        if (!ThuTables.equals("")) {
            SharedPreferences ThuTable = getActivity().getSharedPreferences("thutable", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor ThuTableEdit = ThuTable.edit();
            ThuTableEdit.putString("thutable", ThuTables);
            ThuTableEdit.commit();
        }

        if (!FriTables.equals("")) {
            SharedPreferences FriTable = getActivity().getSharedPreferences("fritable", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor FriTableEdit = FriTable.edit();
            FriTableEdit.putString("fritable", FriTables);
            FriTableEdit.commit();
        }
    }
}
