package p4.guide_animals.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import p4.guide_animals.Libraries.ImageProcessor;
import p4.guide_animals.R;
import p4.guide_animals.model.TableBase;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends IntentService {

    final String LOG_TAG = "IntentServiceLogs";

    private Context context;
    private TableBase tableBase;
    private HTTPtransport httPtransport;
    private ImageProcessor fileProcessor;

    public MyService() {
        super("MyService");
        // TODO Auto-generated constructor stub
    }

    public void onCreate() {
        super.onCreate();
        context = MyService.this;
        tableBase = new TableBase(context);
        httPtransport =  new HTTPtransport();
        fileProcessor = new ImageProcessor();
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.d(LOG_TAG, "start service update razdel");
        //Обновление заголовков
          updateDataCatalog();
        //Обновление контента
          updateDataRazdel();

        //Загрузка картинок из справочника
        dawnloadAllFiles();
        Log.d(LOG_TAG, "update razdel");

        //Обновление списка рыб
       // updateDataFishs();
       // Log.d(LOG_TAG, "update fishs");
    }

    private void updateDataCatalog()
    {

            //Получаем данные
            Map<String, String> params = new HashMap<String, String>();
            params.put("get_items_catalog", "true");
            String url_dict = getString(R.string.url_api) +getString(R.string.url_api_get_dictionary);
            updateDataList(url_dict, params);

    }

    private void updateDataRazdel()
    {
       /* TableBase tableBase = new TableBase(context);
        Cursor c = tableBase.getItemsRazdelAll();
        if(c==null)
        {*/
            //Получаем данные
            Map<String, String> params = new HashMap<String, String>();
            params.put("get_items_razdel", "true");
            String url_dict = getString(R.string.url_api) +getString(R.string.url_api_get_dictionary);
            updateDataList(url_dict, params);
      /*  }*/

    }

    private void dawnloadAllFiles()
    {
        //Получаем данные
        Map<String, String> params = new HashMap<String, String>();
        params.put("getFileList", "true");
        String url_dict = getString(R.string.url_api) +getString(R.string.url_api_get_dictionary);
        updateFileList(url_dict, params);

    }

    private void updateDataFishs()
    {

        Cursor c = tableBase.getItemsFish();
        if(c==null)
        {
            //Получаем данные
            Map<String, String> params = new HashMap<String, String>();
            params.put("fishList", "true");
            String url_fish = getString(R.string.url_api) +getString(R.string.url_api_get_fish);
            updateDataList(url_fish, params);
        }

    }

    //
    public void updateDataList(String url, Map<String, String> params)
    {

        try {
            JSONArray json =  httPtransport.getJSONArray(url, params);
            if (json != null)
            {
                if (params.containsKey("get_items_razdel"))
                {
                    tableBase.InsertItemsRazdel(json,false);
                    Log.d(LOG_TAG, "json razdel insert items =" + json.length());
                }

                if (params.containsKey("get_items_catalog"))
                {
                    tableBase.InsertCatalogAll(json);
                    Log.d(LOG_TAG, "json catalog insert items =" + json.length());
                }
            }
        }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }

    //
    public void updateFileList(String url, Map<String, String> params)
    {

        try {
            JSONObject json =  httPtransport.getJSONObject(url, params);
            if(json != null)
            {
                //Загрузка полного каталога по всем видам рыб
                if (params.containsKey("getFileList"))
                {

                    try {
                        JSONObject all_file = json.getJSONObject("fileAll");
                        setJSONDataFiles(all_file,null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }


    private void setJSONDataFiles(JSONObject all_file,String rootDir)  throws Exception
    {
        Iterator<String> iter = all_file.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = all_file.get(key);
                if(value instanceof JSONArray)
                {
                    String dir = (rootDir!=null?String.format("%s/%s",rootDir,key):key);
                    JSONArray jsonArray = ((JSONArray) value);
                    dawnloadFiles(jsonArray,dir);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void dawnloadFiles(JSONArray jsonArray,String key) throws Exception
    {

        int count = jsonArray.length();
        if(count!=0)
        {
            for(int i=0;i<count;i++)
            {
                String name_image = jsonArray.getString(i).toString().toLowerCase();
                if(name_image.contains("{"))
                {
                    setJSONDataFiles(new JSONObject(name_image),key);
                }
                else
                {
                    String file_dir = fileProcessor.getNewDir(getString(R.string.dir_root),key);
                    String url_file = String.format("%s%s/%s/%s",getString(R.string.url_api),"image",key,name_image);

                    httPtransport.getDownloadFile(url_file, file_dir + "/" + name_image);
                    Log.d(LOG_TAG, String.format("Файл - %s/%s загружен",file_dir,name_image));
                }

            }
        }
    }

}
