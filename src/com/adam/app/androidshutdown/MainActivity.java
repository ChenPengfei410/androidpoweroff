package com.adam.app.androidshutdown;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import android.R.string;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private PowerManager mPm = null;
	
	private Button btn_power_off = null;
	private Button btn_reboot = null;
	private Button btn_set_system_time = null;
	
	private String sClassName = "android.os.SystemProperties";
	
	private void ShutDown() {
		Toast.makeText(this, "press me...", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Intent.ACTION_SHUTDOWN); 
		intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);  
        intent.putExtra(Intent.EXTRA_SHUTDOWN_USERSPACE_ONLY, true);  
		this.sendBroadcast(intent);
	}
	
	private void Reboot() {
		
		mPm.reboot(null);
		
	}
	
	private void PowerOff() {
		
//		SystemProperties.set("ctl.start", "shutdown");
		setSystemProperty("ctl.start", "shutdown");

//        /* sleep for a long time, prevent start another service */
//        try {
//            Thread.currentThread().sleep(Integer.MAX_VALUE);
//        } catch ( Exception e) {
// 
//        }
	}
	
	void setDate(Context context, int year, int month, int day) {
	        Calendar c = Calendar.getInstance();

	        c.set(Calendar.YEAR, year);
	        c.set(Calendar.MONTH, month);
	        c.set(Calendar.DAY_OF_MONTH, day);
	        long when = c.getTimeInMillis();

	        if (when / 1000 < Integer.MAX_VALUE) {
	            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
	        }
	    }
	
	void setTime(Context context, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }
	
	private void setSystemProperty(String key, String str) {
		
		Class<?> c = null;
		Method method_set = null;
		
		Class<?>[] parameterTypes = {			
				String.class,
				String.class
		};
		
		try {
			c = Class.forName(sClassName);
			method_set = c.getDeclaredMethod("set", parameterTypes);
			method_set.setAccessible(true);
			method_set.invoke(null, key, str);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_power_off = (Button)this.findViewById(R.id.btn_power_off);
		btn_power_off.setOnClickListener(this);
		btn_reboot = (Button)this.findViewById(R.id.btn_reboot);
		btn_reboot.setOnClickListener(this);
		btn_set_system_time = (Button)this.findViewById(R.id.btn_set_system_time);
		btn_set_system_time.setOnClickListener(this);
		
		mPm = (PowerManager)getSystemService(
                Context.POWER_SERVICE);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()) {
		case R.id.btn_power_off:
//			PowerOff();
			ShutDown();
			break;
		case R.id.btn_reboot:			
			Reboot();
			break;
		case R.id.btn_set_system_time:
			setTime(this, 9, 00);
			break;
		default:
			break;
		}
	}

}
