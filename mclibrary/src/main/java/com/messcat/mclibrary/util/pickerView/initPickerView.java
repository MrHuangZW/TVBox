package com.messcat.mclibrary.util.pickerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市三级联动
 * Created by Administrator on 2017/8/30 0030.
 */

public class initPickerView {
    private int provinceIndex = 0;//标记已经选过的城市的下标
    private int cityIndex = 0;//标记已经选过的城市的下标
    private int areaIndex = 0;//标记已经选过的城市的下标
    private OptionsPickerView optionsPickerView;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<String> provinceId = new ArrayList<>();
    private List<List<String>> cityId = new ArrayList<>();
    private List<List<List<String>>> areaId = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private String province_provinceId, city_cityID, area_areaId;//省
    private Context mContext;
    private int textSize = 20;
    private onClickOkListener mListener;

    public initPickerView(Context context, onClickOkListener listener) {
        mContext = context;
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mListener = listener;
    }

    /**
     * 弹出选择器
     */
    public void showPickerView() {
        //条件选择器
        optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mListener.setOnSelectOkListener(provinceId.get(options1), options1Items.get(options1).getPickerViewText(),
                        cityId.get(options1).get(option2), options2Items.get(options1).get(option2),
                        areaId.get(options1).get(option2).get(options3), options3Items.get(options1).get(option2).get(options3));
                province_provinceId = provinceId.get(options1);
                city_cityID = cityId.get(options1).get(option2);
                area_areaId = areaId.get(options1).get(option2).get(options3);
                provinceIndex = options1;
                cityIndex = option2;
                areaIndex = options3;
            }
        }).setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(textSize)
                .setTitleText("城市选择")
                .build();
        optionsPickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
        optionsPickerView.setSelectOptions(provinceIndex, cityIndex, areaIndex);
        optionsPickerView.show();
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        textSize = size;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
//                        Toast.makeText(AddAddressActivity.this, "开始解析数据", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(AddAddressActivity.this, "解析数据成功", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
//                    Toast.makeText(AddAddressActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(mContext, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            provinceId.add(jsonBean.get(i).getValue());
            if (province_provinceId != null) {
                if (province_provinceId.equals(jsonBean.get(i).getValue()))//标记已经选过的城市的下标
                    provinceIndex = i;
            }
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            List<String> cityIdList = new ArrayList<>();
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            List<List<String>> province_areaIdList = new ArrayList<>();
            if (jsonBean.get(i).getChildren() != null) {
                for (int c = 0; c < jsonBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                    if (jsonBean.get(i).getChildren() == null) {
                        CityList.add("");//添加城市
                        cityIdList.add("");
                    } else {
                        if (jsonBean.get(i).getChildren().get(c).getValue() != null) {
                            cityIdList.add(jsonBean.get(i).getChildren().get(c).getValue());
                            if (city_cityID != null) {
                                if (city_cityID.equals(jsonBean.get(i).getChildren().get(c).getValue()))//标记已经选过的城市的下标
                                    cityIndex = c;
                            }

                        } else {
                            cityIdList.add("");
                        }
                        if (jsonBean.get(i).getChildren().get(c).getText() != null) {
                            String CityName = jsonBean.get(i).getChildren().get(c).getText();
                            CityList.add(CityName);//添加城市
                        } else {
                            CityList.add("");//添加城市
                        }
                    }

                    ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                    List<String> city_areaIdList = new ArrayList<>();
                    if (jsonBean.get(i).getChildren() != null) {
                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        if (jsonBean.get(i).getChildren().get(c).getChildren() == null
                                || jsonBean.get(i).getChildren().get(c).getChildren().size() == 0) {
                            City_AreaList.add("");
                            city_areaIdList.add("");
                        } else {
                            for (int d = 0; d < jsonBean.get(i).getChildren().get(c).getChildren().size(); d++) {//该城市对应地区所有数据
                                if (jsonBean.get(i).getChildren().get(c).getChildren().get(d).getValue() != null) {
                                    city_areaIdList.add(jsonBean.get(i).getChildren().get(c).getChildren().get(d).getValue());
                                    if (area_areaId != null) {
                                        if (area_areaId.equals(jsonBean.get(i).getChildren().get(c).getChildren().get(d).getValue()))//标记已经选过的城市的下标
                                            areaIndex = d;
                                    }

                                }
                                if (jsonBean.get(i).getChildren().get(c).getChildren().get(d).getText() != null) {
                                    String AreaName = jsonBean.get(i).getChildren().get(c).getChildren().get(d).getText();
                                    City_AreaList.add(AreaName);//添加该城市所有地区数据
                                }
                            }
                        }
                    } else {
                        city_areaIdList.add("");
                        City_AreaList.add("");
                    }
                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                    province_areaIdList.add(city_areaIdList);//添加该省所有地区ID
                }
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加城市ID
             */
            cityId.add(cityIdList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            /**
             * 添加所有城市的ID
             */
            areaId.add(province_areaIdList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    public interface onClickOkListener {
        void setOnSelectOkListener(String provinceId, String provinceName,
                                   String cityId, String cityName, String areaId, String areaName);
    }
}
