package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.weatherapi.model.Item;
import com.example.weatherapi.model.WeatherClient;
import com.example.weatherapi.model.WeatherModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView max_tmp, min_tmp, tmp, t_time;
    String base_date = "20231010";
    String base_time = "0500";
    String nx = "58";
    String ny = "125";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findId();

        setWeather(nx, ny);

    }


    private void setWeather(String nx, String ny) {
        // 현재 날짜, 시간 정보 가져오기
        Calendar cal = Calendar.getInstance();
        String base_date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime()); //현재 날짜
        String time = new SimpleDateFormat("HH", Locale.getDefault()).format(cal.getTime());    //현재 시간
        String base_time = getTime(time);

        //시간 api 가져오기 위해 변환
        // 현재 시각이 00시가 넘었다면 하루 전 날짜로 설정하여 다음 날의 데이터를 요청해야 한다.
        if (Integer.parseInt(base_time) >= 2300) {
            cal.add(Calendar.DATE, -1);
            base_date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime());
        }
        base_date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime());

        //날씨 정보 가져오기
        // (응답 자료 형식-"JSON", 한 페이지 결과 수 = 1000, 페이지 번호 = 1, 발표 날싸, 발표 시각, 예보지점 좌표)
        Call<WeatherModel> call = WeatherClient.weatherInterface.GetWeather("JSON", 1000, 1, base_date, base_time, nx, ny);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    List<Item> it = response.body().getResponse().getBody().getItems().getItem();

                    String temp = "";        // 기온
                    String max_temp = "";   //최고 기온
                    String min_temp = "";   //최저 기온
                    String r_time="";

                    for (int i = 0; i < 10; i++) {
                        String category = it.get(i).getCategory();
                        String fcstValue = it.get(i).getFcstValue();
                        String fcstTime = it.get(i).getFcstTime();
                        if (category.equals("TMP")) {
                            temp = fcstValue;
                            r_time = fcstTime;
                        } else {
                            continue;
                        }
                        //최저, 최고 기온 알기위해서(전체 사이즈 중에서, 가장 높은값 / 가장 낮은 값)
                        for (int i1 = 0; i1 < it.size(); i1++) {
                            String category2 = it.get(i1).getCategory();
                            String fcstValue2 = it.get(i1).getFcstValue();
                            if (category2.equals("TMX")) {
                                if (max_temp.isEmpty() || Float.parseFloat(fcstValue2) > Float.parseFloat(max_temp)){   //이전 최고 기온 값이 비어 있거나 현재 기온 값이 이전 최고 기온 값을 초과하면 새로운 최고 기온 값인 max_temp를 현재 기온 값 fcstValue2로 업데이트
                                max_temp = fcstValue2;
                                }
                            } else if (category2.equals("TMN")) {
                                if (min_temp.isEmpty() || Float.parseFloat(fcstValue2) < Float.parseFloat(min_temp)){
                                min_temp = fcstValue2;
                                }
                            }
                        }
                    }
                    tmp.setText("현재온도: " + temp);
                    max_tmp.setText("최고온도: " + max_temp);
                    min_tmp.setText("최저온도: " + min_temp);
                    t_time.setText("예보 시간: " + r_time);

                    Log.d("MainActivity", "Api Connect Success");
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("api fail", t.getMessage().toString());
            }
        });
    }

    //기상청 날씨 데이터 정보는 하루에 8번 업데이트(새벽 두시 부터 세시간 간격으로)
    private String getTime(String time) {
        String result = "";
        switch (time) {
            case "02":
            case "03":
            case "04":
                result = "0200"; // 02~04
                break;
            case "05":
            case "06":
            case "07":
                result = "0500"; // 05~07
                break;
            case "08":
            case "09":
            case "10":
                result = "0800"; // 08~10
                break;
            case "11":
            case "12":
            case "13":
                result = "1100"; // 11~13
                break;
            case "14":
            case "15":
            case "16":
                result = "1400"; // 14~16
                break;
            case "17":
            case "18":
            case "19":
                result = "1700"; // 18~20
                break;
            case "20":
            case "21":
            case "22":
                result = "2000"; // 21~23
                break;
            default:
                result = "2300";
        }
        return result;
    }


    private void findId() {
        max_tmp = findViewById(R.id.max_temp);
        min_tmp = findViewById(R.id.min_temp);
        tmp = findViewById(R.id.temp);
        t_time = findViewById(R.id.time);
    }
}