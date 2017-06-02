package p4.guide_animals.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import p4.guide_animals.Halper.Halpe;


public class TableBase {

    private static DB MYbase;
    private SQLiteDatabase db;
    private Context context;


    public TableBase(Context c)
    {
        this.context = c;
        MYbase = new DB(c);
    }

    public  String[] getUserIdSession()
    {

            String[] userIdSession = new String[4];
            SQLiteDatabase db = MYbase.SQLdb();
            String[] param = new String[]{"ap_Login", "ap_Pass","ap_Email","ap_EmailDecode"};
        try
        {
              Cursor c = db.query(MYbase.BaseAc.TABLE_USER_SESSION, param, null, null, null, null, null);
               if (c.moveToFirst())
               {
                   for(int i=0;i<param.length;i++)
                   {
                       userIdSession[i] = c.getString(c.getColumnIndex(param[i]));
                   }
               }
               else
               {
                   userIdSession = null;
               }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
            db.close();

        return userIdSession;
    }

    //
    public boolean isUser(String l,String p)
    {
       boolean is = false;

        try {
            String[] userdata = getUserIdSession();
            if (userdata[0] != null) {
                if (userdata[0].equals(l) && userdata[1].equals(p)) {
                    is = true;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
       return is;
    }

    //
    public boolean isEmail()
    {
        boolean is = false;

        try {
            String[] userdata = getUserIdSession();
            if (userdata[0] != null)
            {
               is = true;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }


    //
    public static void setUserSession(String login, String pass, String email)
    {


        String emailDecode = new Halpe().getMD5(email,2);
        ContentValues UserSession = new ContentValues();
        UserSession.put("ap_Login",login);
        UserSession.put("ap_Pass",pass);

        String WHERE ="ap_Email='"+email+"'";
        //Заполняем таблицу сессии
        //MYbase.BaseAc.onCreatSession(MYbase.SQLdb());
        long idIndex = 0;
        if(!email.equals(""))
        {
            try{

                idIndex = MYbase.update(MYbase.BaseAc.TABLE_USER_SESSION, UserSession, WHERE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        UserSession.put("ap_Email",email);
        UserSession.put("ap_EmailDecode",emailDecode);

        if(idIndex==0)
            idIndex = MYbase.insert(MYbase.BaseAc.TABLE_USER_SESSION, UserSession);


        MYbase.BaseAc.close();
        Log.i("IndexInsertSession", "Index User Session (" + idIndex + ")");

    }

    //
    public static void updUserSession( String[] userSession)
    {
        ContentValues UserSession = new ContentValues();
        UserSession.put("ap_Login",userSession[0]);
        UserSession.put("ap_Pass",userSession[1]);
        UserSession.put("ap_Email",userSession[2]);

        String WHERE ="ap_Login='"+userSession[0]+"'";
        //Заполняем таблицу сессии
        long idIndex = MYbase.update(MYbase.BaseAc.TABLE_USER_SESSION, UserSession, WHERE);
        MYbase.BaseAc.close();
        Log.i("IndexUpdatetSession", "Index User Session (" + idIndex + ")");

    }
    // Метод удаления таблицы сессии
    public static void  delTableSession()
    {

        MYbase.SQLdb().execSQL("DROP TABLE IF EXISTS " + MYbase.BaseAc.TABLE_USER_SESSION);
        MYbase.SQLdb().close();
    }




    public static void  delTable()
    {
        MYbase.SQLdb().execSQL("DROP TABLE IF EXISTS " + MYbase.BaseAc.TABLE_CATALOG);
        MYbase.SQLdb().execSQL("DROP TABLE IF EXISTS " + MYbase.BaseAc.TABLE_RAZDEL);
        MYbase.SQLdb().close();
    }


    //Обновление или добавление меню каталога
    public void InsertCatalogAll(JSONArray json)
    {
        try {
            ContentValues Data = new ContentValues();
            int Total = json.length();//Количество ключей в масиве
            if(Total>1)
            {    //Заполняем таблицу
                for (int i=0;i<Total; i++)
                {
                    JSONObject itemObject = json.getJSONObject(i);
                    //
                    Data.put("ap_id", itemObject.getString("id"));
                    Data.put("ap_title", itemObject.getString("title"));
                    Data.put("ap_comment", itemObject.getString("comment"));
                    Data.put("ap_content", itemObject.getString("content"));
                    Data.put("ap_vid", itemObject.getString("cat_vid"));
                    Data.put("ap_cat_famely", itemObject.getString("cat_famely"));
                    Data.put("ap_cat_rod", itemObject.getString("cat_rod"));

                    String WHERE ="ap_id='"+itemObject.getString("id")+"'";
                    long Index = MYbase.update(MYbase.BaseAc.TABLE_CATALOG, Data, WHERE);
                    if(Index==0)
                        Index = MYbase.insert(MYbase.BaseAc.TABLE_CATALOG, Data);

                    Log.i("IndexInsert", "Index TABLE_CATALOG: (" + Index + ")");
                }
            }
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    //Добавляем данные
    public void InsertCatalog(ContentValues Data)
    {

        long Index = MYbase.insert(MYbase.BaseAc.TABLE_CATALOG, Data);
        MYbase.BaseAc.close();
        Log.i("IndexInsert", "Index TABLE_CATALOG: (" + Index + ")");
    }

    //Обновляем данные
    public void UpdateRazdel(int id_raz, ContentValues Data)
    {
        String WHERE ="ap_id='"+id_raz+"'";

        long Index = MYbase.update(MYbase.BaseAc.TABLE_RAZDEL, Data, WHERE);
        MYbase.BaseAc.close();
        Log.i("IndexInsert", "Index TABLE_RAZDEL: (" + Index + ")");
    }
    //
    public Cursor getItemsCatalog(int type_id)
    {
        String WHERE =  "ap_cat_rod ='"+type_id+"'";
        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_CATALOG,null, WHERE, null, null, null, null);
        if(c!=null)
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }

    //
    public Cursor getItemCatalog(int itemid)
    {
        String WHERE =  "ap_id ='"+itemid+"'";
        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_CATALOG,null, WHERE, null, null, null, null);
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }

    //
    public Cursor getItemsRazdelAll()
    {

        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_RAZDEL,null,null, null, null, null, null);
        if(c!=null)
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }

    //
    public Cursor getItemsRazdel(int id_cat)
    {
        String WHERE =  "ap_cat_id ='"+id_cat+"'";
        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_RAZDEL,null, WHERE, null, null, null, null);
        if(c!=null)
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }

    //
    public Cursor getItemRazdel(int id_raz)
    {
        String WHERE =  "ap_id ='"+id_raz+"'";
        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_RAZDEL,null, WHERE, null, null, null, null);
        if(c!=null)
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }



    //
    public void InsertItemsRazdel(JSONArray json,boolean is_item)
    {
        try {
            ContentValues Data = new ContentValues();
            int Total = json.length();//Количество ключей в масиве

            if(Total>1)
            {    //Заполняем таблицу
                for (int i=0;i<Total; i++)
                {
                    JSONObject itemObject = json.getJSONObject(i);
                    //
                    Data.put("ap_id", itemObject.getString("id"));
                    Data.put("ap_cat_id", itemObject.getString("cat_id"));
                    Data.put("ap_name", itemObject.getString("name"));
                    if(is_item)
                    Data.put("ap_content", itemObject.getString("content"));

                    String WHERE ="ap_id='"+itemObject.getString("id")+"'";

                    long Index = MYbase.update(MYbase.BaseAc.TABLE_RAZDEL, Data, WHERE);
                    if(Index==0)
                    {
                        Data.put("ap_content",(is_item?itemObject.getString("content"):"-"));
                        Index = MYbase.insert(MYbase.BaseAc.TABLE_RAZDEL, Data);
                    }
                    Log.i("IndexInsert", "Index Items: (" + Index + ")");
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //
    public Cursor getItemsFish()
    {
        db = MYbase.SQLdb();
        Cursor c = db.query(MYbase.BaseAc.TABLE_ITEMS_FISH,null, null, null,null, null, null);
        if (c.moveToFirst())
        {
            db.close();
            return c;
        }
        db.close();
        return null;
    }


}
