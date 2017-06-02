package p4.guide_animals.Services;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;

public class BaseAplication extends SQLiteOpenHelper {

    public  final String TABLE_USER_SESSION = "usersession";
    public  final String TABLE_CATALOG = "items_catalog";
    public  final String TABLE_RAZDEL = "items_razdel";
    public  final String TABLE_ITEMS_FISH = "items_fish";
    private static String DB_NAME = "BaseAplication";
    private static String DB_PATH = "/data/data/p4.guide_animals/databases/";

    public BaseAplication(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
    }
    public BaseAplication(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseAplication(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    public boolean databaseExist()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

/*
*  NULL	пустое значение
   INTEGER	целочисленное значение
   REAL	значение с плавающей точкой
   TEXT	строки или символы в кодировке UTF-8, UTF-16BE или UTF-16LE
   BLOB	бинарные данные*/

        //tUserSession
        String tUserSession = TABLE_USER_SESSION +" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ap_Login TEXT UNIQUE," +
                "ap_Pass TEXT UNIQUE," +
                "ap_Email TEXT UNIQUE," +
                "ap_EmailDecode TEXT UNIQUE" +
                ");";
        execSQL(db,tUserSession);

        /******MySQL******/
       /* CREATE TABLE IF NOT EXISTS `api_animals_catalog` (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
        `comment` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
        `content` text COLLATE utf8_unicode_ci NOT NULL,
        `cat_vid` int(5) NOT NULL,
        `cat_famely` int(5) NOT NULL,
        `cat_rod` int(5) NOT NULL,
        PRIMARY KEY (`id`)
        )
        */
        //tCatalog
        String tCatalog = TABLE_CATALOG+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ap_id INTEGER NOT NULL," +
                "ap_title TEXT NOT NULL," +
                "ap_comment TEXT NOT NULL," +
                "ap_content TEXT NOT NULL," +
                "ap_vid INTEGER NOT NULL," +
                "ap_cat_famely INTEGER NOT NULL," +
                "ap_cat_rod INTEGER NOT NULL" +
                ");";
        execSQL(db,tCatalog);


        /*CREATE TABLE IF NOT EXISTS `api_animals_razdel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cat_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `content` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)*/


         //tRazdel
        String tRazdel = TABLE_RAZDEL+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ap_id INTEGER NOT NULL," +
                "ap_cat_id INTEGER NOT NULL," +
                "ap_name TEXT NOT NULL," +
                "ap_content TEXT NOT NULL" +
                ");";
        execSQL(db,tRazdel);

        //tItemsFishData
        String tItemsFishData = TABLE_ITEMS_FISH+" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ap_idItem INTEGER," +
                "ap_image_icon TEXT," +
                "ap_name TEXT"+
                ");";
        execSQL(db,tItemsFishData);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    //
    public void onCreatSession(SQLiteDatabase db)
    {
        String tUserSession = TABLE_USER_SESSION +" (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ap_Login TEXT UNIQUE," +
                "ap_Pass TEXT UNIQUE," +
                "ap_Email TEXT UNIQUE," +
                "ap_EmailDecode TEXT UNIQUE" +
                ");";
        execSQL(db,tUserSession);
    }

    //
    private void execSQL(SQLiteDatabase db, String columns)
    {
        try{
            db.execSQL("CREATE TABLE "+columns);
        }
        catch(Exception e) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+columns);
        }

    }
}
