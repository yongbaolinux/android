package net.diskroom.weather;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by jsb-hdp-0 on 2016/10/7.
 */
public class wLocationListener implements LocationListener{
    @Override
    public void onLocationChanged(Location location){
        //Toast.makeText(MainActivity.this,location.getLatitude()+":"+location.getLongitude(),1000).show();
        LogUtils.v(location.getLatitude()+":"+location.getLongitude());
        LogUtils.v("abc");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
