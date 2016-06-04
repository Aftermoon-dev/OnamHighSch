package darkhost.onamhighsch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 민재 on 2016-06-04.
 */

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Runnable r = new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {

				}
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				SplashActivity.this.finish();
			}
		};
		new Thread(r).start();
	}

	// 스플래시에서 뒤로가기를 막음
	@Override
	public void onBackPressed() { }
}
