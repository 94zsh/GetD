package com.future.getd.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.future.getd.log.LogUtils;

public class LocationUtils {
    private String TAG = "LocationUtils";
    private Context mContext;
    private static LocationUtils instance;
    private LocationUtils(){
    }
    private LocationUtils(Context context) {
        this.mContext = context;
    }
    public static LocationUtils getInstance() {
        if (instance == null) {
            synchronized (LocationUtils.class) {
                if (instance == null) {
                    instance = new LocationUtils();
                }
            }
        }
        return instance;
    }
    public void init(Context context){
        this.mContext = context;
    }

    public interface LocationResultListener {
        public void onResult(double lon,double lat,String address);
    }
    private LocationResultListener locationResultListener;


    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    public void startOnceLocation(LocationResultListener listener) {
        LogUtils.e(TAG,"开始定位 :" + listener);
        locationResultListener = listener;
        try {
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(mContext);
            }
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationOption.setNeedAddress(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位回调监听
            mlocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    // 定位结果 aMapLocation ：latitude=22.573611#longitude=113.939481#province=广东省#coordType=GCJ02#city=深圳市#district=南山区#cityCode=0755#adCode=440305#address=广东省深圳市南山区留新一路9号靠近深圳国际创新谷#country=中国#road=留新一路#poiName=深圳国际创新谷#street=留新一路#streetNum=9号#aoiName=万科云城#poiid=#floor=#errorCode=0#errorInfo=success#locationDetail=#id:ScWxsdGpnb210ZmZmMWZrbGpraGd1MjcyMDAwNDY4LA==#csid:95b3a3e954d446a1b6abe04c5dea91ef#pm100011#description=在深圳国际创新谷附近#locationType=5#conScenario=0
                    // 定位结果 code ：0 ,经度：113.939481 ,纬度：22.573611 , mListener: com.future.getd.MainActivity$2$1@913b5cb
                    LogUtils.e(TAG, "定位结果 aMapLocation ：" + aMapLocation);
                    LogUtils.e(TAG, "定位结果 code ：" + aMapLocation.getErrorCode() + " ,经度：" + aMapLocation.getLongitude() + " ,纬度：" + aMapLocation.getLatitude() + " , mListener: " + locationResultListener);
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            if(aMapLocation.getLongitude() != 0 && aMapLocation.getLatitude() != 0){
                                stopLocation();
                                LogUtils.e(TAG,"mListener : " + locationResultListener);
                                if (locationResultListener != null)
                                    locationResultListener.onResult(aMapLocation.getLongitude(),aMapLocation.getLatitude(),aMapLocation.getAddress());
                            }
                        } else {
                            String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                            LogUtils.e(TAG,errText);
                        }
                    }
                }
            });
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            LogUtils.e(TAG,"startLocation ");
            LogUtils.e("startLocation getDeviceId : " + AMapLocationClient.getDeviceId(mContext));
            mlocationClient.startLocation();//启动定位
            LogUtils.e(TAG,"startLocation2 ");
        }catch (Exception e){
            LogUtils.e("startLocation e :" + e);
            e.printStackTrace();
        }
    }

    //百度定位
//    public LocationClient mLocationClient = null;
//    private MyLocationListener myListener = new MyLocationListener();
//    public void startOnceLocationBaidu(LocationResultListener listener) {
//        LogUtils.e(TAG,"开始百度定位 :" + listener);
//        locationResultListener = listener;
//        try {
//            LocationClient.setAgreePrivacy(true);
//            mLocationClient = new LocationClient(mContext);
//            //声明LocationClient类
//            mLocationClient.registerLocationListener(myListener);
//            LocationClientOption option = new LocationClientOption();
//
//            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
////可选，设置定位模式，默认高精度
////LocationMode.Hight_Accuracy：高精度；
////LocationMode. Battery_Saving：低功耗；
////LocationMode. Device_Sensors：仅使用设备；
////LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；
//
//            option.setCoorType("gcj02");
////可选，设置返回经纬度坐标类型，默认GCJ02
////GCJ02：国测局坐标；
////BD09ll：百度经纬度坐标；
////BD09：百度墨卡托坐标；
////海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
//
//            option.setFirstLocType(LocationClientOption.FirstLocType.ACCURACY_IN_FIRST_LOC);
//            option.setOnceLocation(true);
////可选，首次定位时可以选择定位的返回是准确性优先还是速度优先，默认为速度优先
////可以搭配setOnceLocation(Boolean isOnceLocation)单次定位接口使用，当设置为单次定位时，setFirstLocType接口中设置的类型即为单次定位使用的类型
////FirstLocType.SPEED_IN_FIRST_LOC:速度优先，首次定位时会降低定位准确性，提升定位速度；
////FirstLocType.ACCUARACY_IN_FIRST_LOC:准确性优先，首次定位时会降低速度，提升定位准确性；
//
//            option.setScanSpan(3000);
////可选，设置发起定位请求的间隔，int类型，单位ms
////如果设置为0，则代表单次定位，即仅定位一次，默认为0
////如果设置非0，需设置1000ms以上才有效
//
//            option.setOpenGps(true);
////可选，设置是否使用gps，默认false
////使用高精度和仅用设备两种定位模式的，参数必须设置为true
//
//            option.setLocationNotify(false);
////可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
//
//            option.setIgnoreKillProcess(false);
////可选，定位SDK内部是一个service，并放到了独立进程。
////设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//
//            option.SetIgnoreCacheException(false);
////可选，设置是否收集Crash信息，默认收集，即参数为false
//
//            option.setWifiCacheTimeOut(5*60*1000);
////可选，V7.2版本新增能力
////如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
//
//            option.setEnableSimulateGps(false);
////可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
//
//            option.setNeedNewVersionRgc(true);
////可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
//            mLocationClient.setLocOption(option);
////mLocationClient为第二步初始化过的LocationClient对象
////需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
////更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
//            mLocationClient.start();
//        } catch (Exception e) {
//            LogUtils.e("baidu loc e " + e);
//            e.printStackTrace();
//        }
//
//    }
//    public class MyLocationListener extends BDAbstractLocationListener {
//        @Override
//        public void onLocDiagnosticMessage(int i, int i1, String s) {
//            super.onLocDiagnosticMessage(i, i1, s);
//            LogUtils.e(TAG, "百度定位 onLocDiagnosticMessage return " + i + " , type : " + i1 + " , tip:" + s);
//        }
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
//            //以下只列举部分获取经纬度相关（常用）的结果信息
//            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//
//            double latitude = location.getLatitude();    //获取纬度信息
//            double longitude = location.getLongitude();    //获取经度信息
//            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
//
//            String coorType = location.getCoorType();
//            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//
//            //  //BDLocation.getLocationWhere()方法可获得当前定位点是否是国内，它的取值及含义如下：
//            //        //BDLocation.LOCATION_WHERE_IN_CN：当前定位点在国内；
//            //        //BDLocation.LOCATION_WHERE_OUT_CN：当前定位点在海外；
//
//            int errorCode = location.getLocType();
//            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
//            //61	GPS定位结果
//            //GPS定位成功
//            //62	定位失败
//            //无法获取有效定位依据，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
//            //63	网络异常
//            //没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
//            //66	离线定位结果
//            //通过requestOfflineLocaiton调用时对应的返回结果
//            //67	离线定位失败
//            //161	网络定位结果
//            //网络定位成功
//            //162	请求串密文解析失败
//            //一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
//            //167	服务端定位失败
//            //请您检查是否禁用获取位置信息权限，尝试重新请求定位
//            //505	AK不存在或者非法
//            //请按照说明文档重新申请AK
//            LogUtils.e(TAG, "百度定位结果 code ：" + errorCode + " ,经度：" + longitude + " ,纬度：" + latitude + " , mListener: " + locationResultListener);
//            if (errorCode != 62 && radius != 0 && (latitude != 4.9E-324) && (longitude != 4.9E-324)) {
////                stopLocation();
//                LogUtils.e(TAG, "mListener : " + locationResultListener);
//                if (locationResultListener != null)
//                    locationResultListener.onResult(longitude, latitude);
//            } else {
//                String errText = "定位失败," + errorCode;
//                LogUtils.e(TAG, errText);
//            }
//        }
//    }

    public void stopLocation() {
        LogUtils.e(TAG,"停止定位");
//        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
