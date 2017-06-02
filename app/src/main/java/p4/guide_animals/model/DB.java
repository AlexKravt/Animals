package p4.guide_animals.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import p4.guide_animals.Services.BaseAplication;

/**
 * Created by Kravtsov.a on 05.12.14.
 */
public class DB {

    public BaseAplication BaseAc;

    public DB(Context context)
   {
       BaseAc = new BaseAplication(context);
   }

    public long insert(String name_tab,ContentValues Valus)
    {

        SQLiteDatabase db = SQLdb();
        long idInsert =   db.insert(name_tab, null, Valus);
        db.close();
        BaseAc.close();
        return idInsert;
    }

    public long update(String name_tab, ContentValues Valus, String WHERE)
    {

        SQLiteDatabase db = SQLdb();
        long idUpdate =   db.update(name_tab, Valus, WHERE, null);
        db.close();
        BaseAc.close();
        return idUpdate;
    }

    public long delete(String name_tab,  String WHERE, String[] whereArgs)
    {

        SQLiteDatabase db = SQLdb();
        long idDelete =   db.delete(name_tab, WHERE, whereArgs);
        db.close();
        BaseAc.close();
        return idDelete;
    }
  public SQLiteDatabase SQLdb(){
      SQLiteDatabase db;
      try {
          db = BaseAc.getWritableDatabase();
      } catch (SQLiteException ex) {
          db = BaseAc.getReadableDatabase();
      }
      return db;
  }



}
