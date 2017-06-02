package p4.guide_animals.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import p4.guide_animals.MainActivity;
import p4.guide_animals.R;

/**
 * Created by kravtsov.a on 09.11.2016.
 */

public class ServerServices {

    private Context context;
    private AQuery AQ;
    private SharedPreferences mSettings;
    public final static String IS_PAYMENT="isPayment";
    public ServerServices(Context context,AQuery AQ)
    {
        this.context = context;
        this.AQ = AQ;
        mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean isPayment()
    {
        if(mSettings.contains(IS_PAYMENT) && mSettings.getBoolean(IS_PAYMENT,false))
            return true;
        else
        {
            return  false;
        }
    }

    public void sendStatusPayServer()
    {
        //
        mSettings.edit().putBoolean(IS_PAYMENT,true).apply();
        //
        String url = context.getString(R.string.url_site);
        Map<String, Object> params = new HashMap<>();
        params.put("setStatusPayment", "true");
        params.put("isAnimals", "true");
        params.put("idUser", mSettings.getString(MainActivity.USER_EMAIL,""));
        //Отправляем данные о статусе оплаты
        AQ.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    try
                    {
                        Toast.makeText(context, String.format(context.getString(R.string.pay_status_order),""), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public void getStatusPayment(String idUser)
    {
        //
        String url = context.getString(R.string.url_site);
        Map<String, Object> params = new HashMap<>();
        params.put("getStatusPayment", "true");
        params.put("isAnimals", "true");
        params.put("idUser",idUser);
        //Отправляем данные о статусе оплаты
        AQ.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if(json != null)
                {
                    mSettings.edit().putBoolean(IS_PAYMENT,true).apply(); //Если статус оплачен то создаём запись
                    Toast.makeText(context,"Статус оплаты подтверждён!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mSettings.edit().putBoolean(IS_PAYMENT,false).apply(); //Если статус не оплачен то создаём запись
                    Toast.makeText(context,"Статус оплаты не подтверждён!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
