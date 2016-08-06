package darkhost.onamhighsch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    long backPressedTime;
    private FloatingActionButton fab1, fab2, fab3;
    public static FloatingActionsMenu FabMenu;

    // Fragment
    MainFragment MainFrag;
    MealFragment MealFrag;
    SchFragment SchFrag;
    TimeTableFragment TimeTableFrag;
    SettingFragment SettingFrag;
    SchoolinfoFragment SchoolinfoFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainFrag = MainFragment.newInstance();
        MealFrag = MealFragment.newInstance(0);
        SchFrag = SchFragment.newInstance(0);
        TimeTableFrag = TimeTableFragment.newInstance();
        SettingFrag = SettingFragment.newInstance();
        SchoolinfoFrag = SchoolinfoFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main, MainFrag).commit();

        // Navigator
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 급식 및 학사일정 DB 가져오기
        DataDownload();

        // Fab Button
        FabSetting();

        // Get Fab Menu
        FabMenu = (FloatingActionsMenu) findViewById(R.id.fabmenu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(FabMenu.isExpanded()) {
            FabMenu.collapse();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_activity_main);
            if (fragment != MainFrag)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main, MainFrag).commit();
            }
            else {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backPressedTime;

                if (0 <= intervalTime && 2000 >= intervalTime) super.onBackPressed();
                else {
                    backPressedTime = tempTime;
                    Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, MainFrag).commit();
        } else if (id == R.id.nav_meal) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, MealFrag).commit();
        } else if (id == R.id.nav_sch) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, SchFrag).commit();
        } else if (id == R.id.nav_timetable) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, TimeTableFrag).commit();
        } else if (id == R.id.nav_setting) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, SettingFrag).commit();
        } else if (id == R.id.nav_schoolinfo) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, SchoolinfoFrag).commit();
        } else if (id == R.id.nav_map) {
            Uri uri = Uri.parse("geo:37.6979318,127.2063148");
            Intent it = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(it);
        }

        if(FabMenu.isExpanded()) { FabMenu.collapse(); }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
        ConnectivityManager CManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NetworkInfo = CManager.getActiveNetworkInfo();
        if(NetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) { return true; } else { return false; }
    }



    public void DataDownload()
    {
        // 급식 및 학사 일정 Download 등 처리
        Calendar cal = Calendar.getInstance();
        int month = cal.get ( cal.MONTH ) + 1 ;

        SharedPreferences data = getSharedPreferences("month", MODE_MULTI_PROCESS);
        int dmonth = data.getInt("month", 0);

        SharedPreferences mdata = getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
        String mealdata = mdata.getString("meal", "");

        SharedPreferences mnmdata = getSharedPreferences("mealnm", Context.MODE_MULTI_PROCESS);
        String mealnmdata = mnmdata.getString("mealnm", "");

        SharedPreferences sdata = getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
        String schdata = sdata.getString("sch", "");

        SharedPreferences snmdata = getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
        String schnmdata = snmdata.getString("schnm", "");

        SharedPreferences bmdata = getSharedPreferences("mealbm", Context.MODE_MULTI_PROCESS);
        String mealbmdata = bmdata.getString("mealbm", "");

        // 데이터를 다운받은 달이 다른지, 급식 및 학사일정의 데이터가 비어있는지 확인하고 다운로드
        if(dmonth != month | mealdata.equals("") | mealnmdata.equals("") | schdata.equals("") | schnmdata.equals("") | mealbmdata.equals("")) {
            Log.d("MainActivity", "Data Downloading...");
            if (isNetworkAvailable() == true)
            {
                if(isMobileNetworkConnected() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("모바일 네트워크 경고")
                            .setMessage(getString(R.string.intenetwarning))
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(MainActivity.this, getString(R.string.download_start), Toast.LENGTH_SHORT).show();
                                    DataThread t = new DataThread(MainActivity.this);
                                    t.start();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(MainActivity.this, getString(R.string.intenetwarning1), Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, getString(R.string.download_start), Toast.LENGTH_SHORT).show();
                    DataThread t = new DataThread(MainActivity.this);
                    t.start();
                }
            } else {
                Log.d("MainActivity", "Internet Connection Failed");
                Toast.makeText(MainActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void FabSetting() {
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);


        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:031-570-3300"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.dial_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:031-570-3360"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.dial_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:031-570-3308"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.dial_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    static public void RefreshFrag(Context context)
    {
        Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_LONG).show();
        Fragment MainFrag = MainFragment.newInstance();
        ((FragmentActivity)context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main, MainFrag).commit();
    }
}

class DataThread extends Thread
{
    Context Contexts;
    public DataThread(Context context) {
        Contexts = context;

    }

    public void run() {
        Log.d("DataThread", "Thread Start!");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        Calendar cal = Calendar.getInstance();
        String nowdate = sdf.format(cal.getTime());
        int nowyear = Integer.parseInt(nowdate.substring(0, 4));
        int nowmonth = Integer.parseInt(nowdate.substring(4, 6));

        Calendar NextCal = Calendar.getInstance();
        NextCal.add(cal.MONTH, +1);
        String nextdate = sdf.format(NextCal.getTime());
        int nextyear = Integer.parseInt(nextdate.substring(0, 4));
        String Snextmonth = nextdate.substring(4, 6);

        Calendar BeforeCal = Calendar.getInstance();
        BeforeCal.add(cal.MONTH, -1);
        String beforedate = sdf.format(BeforeCal.getTime());
        int beforeyear = Integer.parseInt(beforedate.substring(0, 4));
        String SBeforemonth = beforedate.substring(4, 6);

        try {

            Document sch = Jsoup.connect("http://stu.goe.go.kr/sts_sci_sf01_001.do?schulCode=J100005670&schulCrseScCode=4&schulKndScCode=04").get();
            Document schnm = Jsoup.connect("http://stu.goe.go.kr/sts_sci_sf01_001.do?schulCode=J100005670&schulCrseScCode=4&schulKndScCode=04&ay=" + nextyear + "&mm=" + Snextmonth).get();

            Document meal = Jsoup.connect("http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005670&schulCrseScCode=4&schulKndScCode=04").get();
            Document mealnm = Jsoup.connect("http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005670&schulCrseScCode=4&schulKndScCode=04&ay=" + nextyear + "&mm=" + Snextmonth).get();
            Document mealbm = Jsoup.connect("http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100005670&schulCrseScCode=4&schulKndScCode=04&ay=" + beforeyear + "&mm=" + SBeforemonth).get();

            Log.d("DataThread", "BEFORE YEAR : " + beforeyear + "BEFORE MONTH : " + SBeforemonth);
            Log.d("DataThread", "THIS YEAR : " + nowyear + "THIS MONTH : " + nowmonth);
            Log.d("DataThread", "NEXT YEAR : " + nextyear + "NEXT MONTH : " + Snextmonth);

            String schs = sch.toString();
            String schnms = schnm.toString();
            String meals = meal.toString();
            String mealnms = mealnm.toString();
            String mealbms = mealbm.toString();

            SharedPreferences schp = Contexts.getSharedPreferences("sch", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor scheditor = schp.edit();
            scheditor.putString("sch", schs);
            scheditor.commit();

            SharedPreferences schnmp = Contexts.getSharedPreferences("schnm", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor schnmeditor = schnmp.edit();
            schnmeditor.putString("schnm", schnms);
            schnmeditor.commit();

            SharedPreferences mealp = Contexts.getSharedPreferences("meal", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor mealeditor = mealp.edit();
            mealeditor.putString("meal", meals);
            mealeditor.commit();

            SharedPreferences mealnmp = Contexts.getSharedPreferences("mealnm", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor mealnmeditor = mealnmp.edit();
            mealnmeditor.putString("mealnm", mealnms);
            mealnmeditor.commit();

            SharedPreferences mealbmp = Contexts.getSharedPreferences("mealbm", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor mealbmeditor = mealbmp.edit();
            mealbmeditor.putString("mealbm", mealbms);
            mealbmeditor.commit();

            SharedPreferences pref = Contexts.getSharedPreferences("month", Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("month", nowmonth);
            editor.commit();

            handler.sendEmptyMessage(0);


        } catch (Exception e) {
            Log.d("DataThread", "Thread Exception Error!");
            errorhandle.sendEmptyMessage(0);
            e.printStackTrace();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MainActivity.RefreshFrag(Contexts);
        }
    };
    Handler errorhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(Contexts, Contexts.getResources().getString(R.string.download_connect_error), Toast.LENGTH_SHORT).show();
        }
    };
}
