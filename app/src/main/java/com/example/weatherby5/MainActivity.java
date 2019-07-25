package com.example.weatherby5;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private String url = "https://api.openweathermap.org/data/2.5/forecast?q=Kolkata&units=metric&mode=json&appid=c384533d67a035570bf8791ca2eb4162";
    private String url1= "https://api.openweathermap.org/data/2.5/weather?q=Kolkata&units=metric&appid=2215bb88ae43c1cd66e40339f8dd0fa6";

    private LinkedHashMap<String,Double> min_temp = new LinkedHashMap<>();
    private LinkedHashMap<String,Double> max_temp = new LinkedHashMap<>();
    private LinkedHashMap<String,String> weather= new LinkedHashMap<>();
    private ArrayList<String> weatherpriority = new ArrayList<>();
    private ArrayList<Integer> iconarray = new ArrayList<>();


    private double curr_temp, press, humidity, wind_speed;
    private JSONObject resp;
    private long timezone,curr_epoch;
    private ConstraintLayout CL;
    TextView tv11, tv12, tv21, tv22, tv31, tv32, city_name, pressureval, humidityval, wind_speedval;
    ImageView im1, im2, im3;

    View view;
    Context context = this;


   @Override
    public void onStart()
    {
        super.onStart();
        String str = getIntent().getStringExtra("str");
        if(str!=null && !str.equals(""))
        {
            int i,j,k,l;
            String u1, u2;
            i = url.indexOf('=');
            j = url1.indexOf('=');
            k = url.indexOf('&');
            l = url1.indexOf('&');

            u1 = url.substring(0,i+1)+str+url.substring(k);
            u2 = url1.substring(0,j+1)+str+url1.substring(l);
            url = u1;
            url1 = u2;

            OnCreate2();
        }
        else if(getIntent().getBooleanExtra("flag",false))
            Toast.makeText(this,"Enter a valid city name!!",Toast.LENGTH_LONG).show();
    };
// add it to the RequestQueue

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        tv11 = findViewById(R.id.today_desc);
        tv12 = findViewById(R.id.today_temps);
        im1 = findViewById(R.id.today_icon);
        tv21 = findViewById(R.id.tom_desc);
        tv22 = findViewById(R.id.tom_temps);
        im2 = findViewById(R.id.tom_icon);
        tv31 = findViewById(R.id.dat_desc);
        tv32 = findViewById(R.id.dat_temps);
        im3 = findViewById(R.id.dat_icon);
         pressureval = findViewById(R.id.pressureval);
         humidityval = findViewById(R.id.humidityval);
         wind_speedval = findViewById(R.id.wind_speedval);

        city_name = findViewById(R.id.mainDesc);
        iconarray.addAll(Arrays.asList(R.drawable.ic_tornado,R.drawable.ic_thunderstorm,R.drawable.ic_snow,R.drawable.ic_rain,R.drawable.ic_drizzle,R.drawable.ic_clouds,R.drawable.ic_dust,R.drawable.ic_dust,R.drawable.ic_fog,R.drawable.ic_dust,R.drawable.ic_dust,R.drawable.ic_dust,R.drawable.ic_dust,R.drawable.ic_dust,R.drawable.ic_clear));
        weatherpriority.addAll(Arrays.asList("Tornado","Thunderstorm","Snow","Rain","Drizzle","Clouds","Mist","Haze","Fog","Smoke","Dust","Sand","Ash","Squall","Clear"));
        OnCreate2();





    }
    public void getFiveDay(){
        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            String str_date,  tmax, tmin, desc;

                            int c = 0;
                            JSONArray a = response.getJSONArray("list");
                            dateType(a);

                            Set set1 = min_temp.entrySet();
                            Set set2 = max_temp.entrySet();
                            Set set3 = weather.entrySet();
                            //Toast.makeText(context,set2.size()+"",Toast.LENGTH_LONG).show();

                            Iterator k = set3.iterator();
                            Iterator i = set1.iterator();
                            Iterator j = set2.iterator();

                        while (c < 3) {
                                Map.Entry obj2 = (Map.Entry) j.next();
                                Map.Entry obj1 = (Map.Entry) i.next();
                           // Toast.makeText(context,"I have reached here 1",Toast.LENGTH_LONG).show();
                               Map.Entry obj3 = (Map.Entry) k.next();
                          //  Toast.makeText(context,"I have reached here 2",Toast.LENGTH_LONG).show();

                                tmax = new Long(Math.round(Double.parseDouble(obj2.getValue().toString()))).toString();
                                tmin = new Long(Math.round(Double.parseDouble(obj1.getValue().toString()))).toString();
                                String wcon= obj3.getValue().toString();

                                if (c == 0) {
                                    tv11.setText("Today");
                                   im1.setImageResource(iconarray.get(weatherpriority.indexOf(wcon)));
                                    tv12.setText(tmax + "/" + tmin+"°C");

                                }
                                if (c == 1) {
                                    tv21.setText("Tomorrow");
                                    im2.setImageResource(iconarray.get(weatherpriority.indexOf(wcon)));
                                    tv22.setText(tmax + "/" + tmin+"°C");

                                }
                                if (c == 2) {
                                  //  Toast.makeText(context,"I have reached here 3",Toast.LENGTH_LONG).show();

                                    str_date = obj2.getKey().toString();
                                    Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(str_date);
                                    String day = new SimpleDateFormat("EEEE").format(date1);
                                   // Toast.makeText(context,"I have reached here 4",Toast.LENGTH_LONG).show();

                                    tv31.setText(day);
                                    im3.setImageResource(iconarray.get(weatherpriority.indexOf(wcon)));
                                    tv32.setText(tmax + "/" + tmin+"°C");

                                }
                                c++;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        queue.add(getRequest);

    }

          private void dateType(JSONArray a)
    {
        curr_epoch=curr_epoch+timezone;
        String today = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (curr_epoch*1000));

        try {
            min_temp.put(today, resp.getJSONObject("main").getDouble("temp_min"));
            max_temp.put(today, resp.getJSONObject("main").getDouble("temp_max"));
            for (int i = 0; i < 40; i++) {
                long epoch = a.getJSONObject(i).getLong("dt");
                JSONObject obj = a.getJSONObject(i).getJSONObject("main");
                String this_date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new Date(epoch * 1000));
                if (min_temp.get(this_date) == null || obj.getDouble("temp_min") < min_temp.get(this_date))
                    min_temp.put(this_date, obj.getDouble("temp_min"));
                if (max_temp.get(this_date) == null || obj.getDouble("temp_max") > max_temp.get(this_date))
                    max_temp.put(this_date, obj.getDouble("temp_max"));

                    String wc = a.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");

                       if (weather.get(this_date) == null || weatherpriority.indexOf(weather.get(this_date)) > weatherpriority.indexOf(wc))
                            weather.put(this_date, wc);
                    }

            }

        catch(Exception e){
            System.out.println(e);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem obj)
    {
        if(obj.getItemId() == R.id.addcity)
        {


            new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert).setTitle("Add a City").setView(view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent intent2 = new Intent(context, AddCityActivity.class);
                            startActivity(intent2);


                        }
                    }).setNegativeButton("Cancel", null).show();
        }
        return (super.onOptionsItemSelected(obj));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.add_city,menu);
        return true;
    }

    public void OnCreate2()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        resp=response;
                        try {
                            TextView mainTemp = findViewById(R.id.mainTemp);
                            curr_temp = resp.getJSONObject("main").getDouble("temp");
                            press = resp.getJSONObject("main").getDouble("pressure");
                            humidity = resp.getJSONObject("main").getDouble("humidity");
                            wind_speed = resp.getJSONObject("wind").getDouble("speed");
                            double ct = curr_temp;
                            CL = findViewById(R.id.constr1);

                            if(ct<0)
                                CL.setBackgroundResource(R.drawable.gradient1);
                            else if(ct>=0 && ct<5)
                                CL.setBackgroundResource(R.drawable.gradient2);
                            else if(ct>=5 && ct<10)
                                CL.setBackgroundResource(R.drawable.gradient3);
                            else if(ct>=10 && ct <20)
                                CL.setBackgroundResource(R.drawable.gradient4);
                            else if(ct>=20 && ct<30)
                                CL.setBackgroundResource(R.drawable.gradient5);
                            else if(ct>=30 && ct<40)
                                CL.setBackgroundResource(R.drawable.gradient6);
                            else if(ct>40)
                                CL.setBackgroundResource(R.drawable.gradient7);

                            int i,j;
                            i = url1.indexOf('=') + 1;
                            j = url1.indexOf('&');
                            String str = url1.substring(i,j);
                            city_name.setText(str);
                            mainTemp.setText(curr_temp + "°C");
                            curr_epoch=resp.getLong("dt");
                            timezone=resp.getLong("timezone");
                            pressureval.setText(press+" hPa");
                            humidityval.setText(humidity+" %");
                            wind_speedval.setText(wind_speed+" m/s");

                            getFiveDay();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

        queue.add(getRequest);
    }

}

