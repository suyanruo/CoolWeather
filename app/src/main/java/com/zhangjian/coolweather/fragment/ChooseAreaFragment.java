package com.zhangjian.coolweather.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangjian.coolweather.R;
import com.zhangjian.coolweather.activity.MainActivity;
import com.zhangjian.coolweather.activity.WeatherActivity;
import com.zhangjian.coolweather.db.City;
import com.zhangjian.coolweather.db.County;
import com.zhangjian.coolweather.db.Province;
import com.zhangjian.coolweather.util.DataUtil;
import com.zhangjian.coolweather.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static String sWeatherUrlPre = "http://guolin.tech/api/china/";

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        titleText = view.findViewById(R.id.tv_title);
        backButton = view.findViewById(R.id.btn_back);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (LEVEL_PROVINCE == currentLevel) {
                selectedProvince = provinceList.get(i);
                queryCities();
            } else if (LEVEL_CITY == currentLevel) {
                selectedCity = cityList.get(i);
                queryCounties();
            } else if (LEVEL_COUNTY == currentLevel) {
                String weatherId = countyList.get(i).getWeatherId();
                if (getActivity() instanceof MainActivity) {
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    Log.d(TAG, "onActivityCreated: weather_id:" + countyList.get(i).getWeatherId());
                    startActivity(intent);
                } else if (getActivity() instanceof WeatherActivity) {
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.changeCity(weatherId);
                }
            }
        });
        backButton.setOnClickListener((view) -> {
            backPressed();
        });
        queryProvinces();
    }

    /**
     * 查询全国所有省，优先从数据库中查询，如果没有再从服务器上查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            Log.d(TAG, "queryProvinces in database");
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            Log.d(TAG, "queryProvinces in server");
            queryFromServer(sWeatherUrlPre, "province");
        }
    }

    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            Log.d(TAG, "queryCities in database");
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            Log.d(TAG, "queryCities in server");
            queryFromServer(String.format("%s%d", sWeatherUrlPre, selectedProvince.getProvinceCode()), "city");
        }
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityId=?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            Log.d(TAG, "queryCounties in database");
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            Log.d(TAG, "queryCounties in server");
            queryFromServer(String.format("%s%d/%d", sWeatherUrlPre, selectedProvince.getProvinceCode(), selectedCity.getCityCode()), "county");
        }
    }

    private void queryFromServer(String url, String type) {
        Log.d(TAG, "queryFromServer: " + url);
        showDialog();
        HttpUtil.getRequestAsyn(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                getActivity().runOnUiThread(() -> {
                    closeDialog();
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = DataUtil.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = DataUtil.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = DataUtil.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(() -> {
                        closeDialog();
                        if ("province".equals(type)) {
                            queryProvinces();
                        } else if ("city".equals(type)) {
                            queryCities();
                        } else if ("county".equals(type)) {
                            queryCounties();
                        }
                    });
                }
            }
        });
    }

    public void backPressed() {
        if (LEVEL_COUNTY == currentLevel) {
            queryCities();
        } else if (LEVEL_CITY == currentLevel) {
            queryProvinces();
        } else if (LEVEL_PROVINCE == currentLevel) {
            getActivity().finish();
        }
    }

    private void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
