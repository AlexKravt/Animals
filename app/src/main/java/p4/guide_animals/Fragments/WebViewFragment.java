package p4.guide_animals.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.yandex.money.api.methods.Token;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p4.guide_animals.Dialogs.DialogMyProgress;

import p4.guide_animals.DictionaryActivity;
import p4.guide_animals.Libraries.ImageProcessor;
import p4.guide_animals.Libraries.LibView;
import p4.guide_animals.MainActivity;
import p4.guide_animals.R;
import p4.guide_animals.Services.HTTPtransport;
import p4.guide_animals.model.TableBase;


public class WebViewFragment extends Fragment
{

    private Context context;
    private View rootView;
    private AQuery AQ;
    private LibView libView;
    private DialogMyProgress mProgress;
    private TableBase tableBase;
    private ImageProcessor fileProcessor;
	private WebView webV;
    private WebSettings webSettings;
    private int position = 0;
    private int itemId = 0;
    private boolean isHomeAnimals = false;
    private String Html;
    private Spinner sp;

    public WebViewFragment(){}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        AQ = new AQuery(rootView);
        context = rootView.getContext();
        tableBase = new TableBase(context);
        libView = new LibView();
        mProgress = new DialogMyProgress(context);
        fileProcessor = new ImageProcessor();
        mProgress.setmTitle(getString(R.string.title_data_site));
        mProgress.setmText(getString(R.string.alert_data_site));

        AQ.id(R.id.spinItemsRazdel).gone();

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            if(bundle.containsKey(DictionaryActivity.HOME_BUNDLE))
                this.isHomeAnimals =   bundle.getBoolean(DictionaryActivity.HOME_BUNDLE);
        }

        itemId = (!isHomeAnimals?MainActivity.itemId:DictionaryActivity.itemId);

        //
        webV = (WebView) rootView.findViewById(R.id.wv_root);
        webSettings = webV.getSettings();
        // включаем выполнение скриптов
        webSettings.setJavaScriptEnabled(true);
        webV.setWebViewClient(new myWebClient());
        //
       // getListTitleRazdelJSON(itemId);
        //
        getViewDataAnimals();

        return rootView;
    }

    private void getViewDataAnimals()
    {
        Cursor c = tableBase.getItemCatalog(itemId);
        if(c!=null)
        {
            String customHtml = "<html><head>" +
                    "<style>\n" +
                    "\t\t body{\n" +
                    "\t\t\t margin: 0px;\n" +
                    "\t padding: 20px 20px 0px 20px;\n" +
                    "\t font-family:Arial, Helvetica, sans-serif;\n" +
                    "\t\t\t font-size:16px;\n" +
                    "\t\t\t }\n" +
                    "\t\t img{ \n" +
                    "\t\t border:0;\n" +
                    "\t\t margin: 10px;\n" +
                    "\t\t}\t \n" +
                    "\t\t</style>" +
                    "<body><h1>"+c.getString(c.getColumnIndex("ap_title"))+"</h1>"
                    +c.getString(c.getColumnIndex("ap_content"))+
                    "</head></body></html>";
            webSettings.setDefaultTextEncodingName("utf-8");
            webV.loadData(customHtml, "text/html; charset=utf-8", null);

            //
            Cursor cr = tableBase.getItemsRazdel(c.getInt(c.getColumnIndex("ap_id")));
            getSpinnerView(cr);
        }
    }

    private void getSpinnerView(final Cursor cr)
    {
        //
        if(cr!=null)
        {

            //Добавляем по умолчанию заголовок с 0 значением по умолчанию
            MatrixCursor extras = new MatrixCursor(new String[] { "_id","ap_id","ap_cat_id","ap_name" });
            extras.addRow(new String[] {"0","0",""+itemId+"","Выберите из списка"});
            Cursor[] cursors = {extras, cr};
            Cursor extendedCursor = new MergeCursor(cursors);
            if(!isHomeAnimals)
            {
                MainActivity.itemId = 0;
            }
            else
            {
                DictionaryActivity.itemId = 0;
            }

            //Заполняем список данными
            sp = libView.getSpinnerCursor(rootView,R.id.spinItemsRazdel,new String[]{"ap_name"},extendedCursor,0);
            sp.setVisibility(View.VISIBLE);
            //Делаем действия при нажптии из выпадающего списка
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                    if(pos!=0)
                    {
                        position = pos;

                        if (cr.moveToPosition(pos - 1))
                        {
                            int id_raz = cr.getInt(cr.getColumnIndex("ap_id"));
                           // Cursor c = tableBase.getItemRazdel(id_raz);
                            String content = cr.getString(cr.getColumnIndex("ap_content"));
                            if (content.equals("-"))
                            {

                                razdelViewJSON(id_raz);

                            }
                            else if (!content.equals("-"))
                            {
                                razdelViewCursor(id_raz);
                            }
                        }
                    }
                    else
                    {
                        sp.setSelection(position);
                    }

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            webV.scrollTo(0,0);

                        }
                    }, 500);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0)
                {

                }
            });
        }
        else
        {
            getListTitleRazdelJSON(itemId);
        }
    }

    private void razdelViewJSON(final int id_raz)
    {
        //

        String url_item = getString(R.string.url_api)+getString(R.string.url_api_get_dictionary);

        DialogMyProgress dialog = new DialogMyProgress(context);
        dialog.setmTitle(getString(R.string.title_photo_site));
        dialog.setmText(getString(R.string.alert_save_data));

            //Получаем данные
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("idItemRaz", id_raz);
            AQ.progress(dialog).ajax(url_item, params, JSONArray.class, new AjaxCallback<JSONArray>() {
                @Override
                public void callback(String url, final JSONArray json, AjaxStatus status) {

                    if (json != null) {
                        //
                        try {
                            final JSONObject itemObject = json.getJSONObject(0);
                            //Удаляем width и height в теге img
                            String customHtml = del_width_and_height_image(itemObject.getString("content"));
                            //Заполняем данными таблицу меню каталога
                            ContentValues Data = new ContentValues();
                            Data.put("ap_id", itemObject.getString("id"));
                            Data.put("ap_cat_id", itemObject.getString("cat_id"));
                            Data.put("ap_name", itemObject.getString("name"));
                            Data.put("ap_content", customHtml);
                            //Обновляем данные
                            tableBase.UpdateRazdel(id_raz, Data);
                            //Выводим
                            razdelViewCursor(id_raz);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }

    private void getListTitleRazdelJSON(int id_catigory)
    {
        //

        String url_item = getString(R.string.url_api)+getString(R.string.url_api_get_dictionary);

        DialogMyProgress dialog = new DialogMyProgress(context);
        dialog.setmTitle(getString(R.string.title_photo_site));
        dialog.setmText(getString(R.string.alert_save_data));

        //Получаем данные
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("getListTitle", id_catigory);
        AQ.progress(dialog).ajax(url_item, params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, final JSONArray json, AjaxStatus status) {

                if (json != null)
                {

                    tableBase.InsertItemsRazdel(json,false);
                    //
                    Cursor cr = tableBase.getItemsRazdel(itemId);
                    getSpinnerView(cr);
                }


            }
        });
    }

    public String replase_src(String s)
    {

        final String regex = "(?<=<img src=\")[^\"]*";
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(s);
        while (m.find())
        {
            String replace = getString(R.string.url_api)+m.group().toString();
            String regex_  = m.group();

            s =  s.replace(regex_, replace);
           // Log.d("log_image_pars", s);
           // System.out.println(m.group());
        }
        return s;
    }

    public String del_width_and_height_image(String s)
    {

        final String regex_w = "(?<= width=\")[^\"]*";
        final Pattern p_w = Pattern.compile(regex_w);
        final Matcher m_w = p_w.matcher(s);
        while (m_w.find())
        {
            String replace = " ";
            String regex_  = "width=\""+m_w.group()+"\"";
            s =  s.replace(regex_, replace);
           // Log.d("log_image_pars", s);
            // System.out.println(m.group());
        }


        final String regex_h = "(?<= height=\")[^\"]*";
        final Pattern p_h = Pattern.compile(regex_h);
        final Matcher m_h = p_h.matcher(s);
        while (m_h.find())
        {
            String replace = " ";
            String regex_  = "height=\""+m_h.group()+"\"";
            s =  s.replace(regex_, replace);
          //  Log.d("log_image_pars", s);
            // System.out.println(m.group());
        }

        return s;
    }

//
    private void razdelViewCursor(int id_raz)
    {
        Cursor c = tableBase.getItemRazdel(id_raz);
        postWebView(c);
    }

    //
    private void postWebView(final Cursor c)
    {
        webV.post(new Runnable()
        {
            @Override
            public void run() {
                 Html = "<html><head>" +
                        "<style>\n" +
                        "\t\t body{\n" +
                        "\t\t\t margin: 0px;\n" +
                        "\t padding: 20px 20px 0px 20px;\n" +
                        "\t font-family:Arial, Helvetica, sans-serif;\n" +
                        "\t\t\t font-size:16px;\n" +
                        "\t\t\t }\n" +
                        " img{ \n" +
                        " border:0;\n" +
                        " margin: 10px;\n" +
                        " max-width:280px;\n" +
                        " }\t \n" +
                        "\t\t</style>" +
                        "</head>" +
                        "<body><h1>" + c.getString(c.getColumnIndex("ap_name")) + "</h1>"
                        +"%s"+
                        "</body></html>";

                String content = c.getString(c.getColumnIndex("ap_content"));
                String base = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                String imagePath = base+"/"+ getString(R.string.dir_root)+"/";
                //Делаем замену в атрибуте src
                String customHtml = replase_src(content,imagePath);
                //Удаляем width и height в теге img
               // customHtml = del_width_and_height_image(customHtml);
                Html = String.format(Html,customHtml);
                webSettings.setDefaultTextEncodingName("utf-8");
                // webV.loadData(customHtml, "text/html; charset=utf-8", null);
                webV.loadDataWithBaseURL("", Html, "text/html", "utf-8", "");
               // webV.loadData(customHtml, "text/html; charset=utf-8", null);
            }
        });
    }

    //
    private String replase_src(String s,String path_image_dir)
    {
        String path_img = null;
        final String regex ="(?<= src=\")[^\"]*";// "(?<=<img src=\")[^\"]*";
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(s);
        while (m.find())
        {
            String regex_  = m.group();
            if(!regex_.contains("zooclub.ru"))
            {
                path_img =  regex_.replace(getString(R.string.url_api),"");
                path_img =  path_img.replace("image/","");
            }
            else
            {
                path_img = null;
            }

            if(path_img!=null)
            {
                String replace = String.format("file://%s%s",path_image_dir,path_img);
                s =  s.replace(regex_, replace);
                creatDir(path_img);//<--Создание директорий
                getFileToSave(path_img,path_image_dir);//<--Загрузка картинки
            }
            else
            {
                s =  s.replace(regex_, "");
            }
           // Log.d("log_image_pars", s);
          //  System.out.println(m.group());
        }
        return s;
    }


    private void getFileToSave(final String path_img,final String rootPathDir)
    {

        Runnable runnable = new Runnable() {
            public void run() {
                // пытаемся получить токен
                try {

                    String url_file = String.format("%s%s/%s", getString(R.string.url_api), "image", path_img);
                    new HTTPtransport().getDownloadFile(url_file, rootPathDir + path_img);
                    webV.post(new Runnable() {
                        @Override
                        public void run() {
                            webV.loadDataWithBaseURL("", Html, "text/html", "utf-8", "");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }


    private void creatDir(String path_img)
    {
        String[] splitDir = path_img.split("/");
        int count = splitDir.length;

            if(count==2)
            {fileProcessor.getNewDir(getString(R.string.dir_root),splitDir[0]);}
            else if(count==3)
            {
                fileProcessor.getNewDir(getString(R.string.dir_root),splitDir[0]);
                fileProcessor.getNewDir(getString(R.string.dir_root),String.format("%s/%s",splitDir[0],splitDir[1]));
            }

    }

//
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgress.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //
            super.onPageFinished(view, url);
            mProgress.dismiss();

        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            try {
                webView.stopLoading();
                 }
            catch (Exception e)
            {
                e.printStackTrace();
            }

           /* if (webView.canGoBack()) {
                webView.goBack();
            }*/
           /* webView.loadUrl("file:///android_asset/path/to/your/missing-page-template.html");*/

           // getAlertError(getString(R.string.error_load_data));
            Toast.makeText(context, getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();

            goActivity();

            super.onReceivedError(webView, errorCode, description, failingUrl);
        }
    }
//
    private void  goActivity()
    {
        Intent intent = new Intent(context, (!isHomeAnimals?MainActivity.class:DictionaryActivity.class));
        intent.putExtra("inet_connect",false);
        startActivity(intent);
    }

    //
   /* private  void getAlertError(String text)
    {
        AlertShow.setDialogAlert(context, getString(R.string.alert),
                text,
                false,
                true,
                "",
                getString(R.string.close)
        );
    }*/

    }
