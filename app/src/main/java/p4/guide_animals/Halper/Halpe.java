package p4.guide_animals.Halper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 15.12.14.
 */
public class Halpe {
    public void Halpe(){}

    //
    public static String phoneNumCreatFormat(String numPhone)
    {
        String region = numPhone.substring(0,3);
        String num_end_fore = numPhone.substring(6);
        String numendPhone ="("+region+")"+" *** "+num_end_fore;
        return numendPhone;
    }
    //
    public String NumFormat(String number)
    {
        String print = number;
        int not_long = number.indexOf(".");
        if(not_long>0)
        { number = number.substring(0,not_long);}

        if(number.length()==7)//1 000 000
        {
            String num0 = number.substring(0,1);
            String num1 = number.substring(1,4);
            String num2 = number.substring(4,7);
            print =num0+" "+num1+" "+num2;
        }

        if(number.length()==6)//100 000
        {
            String num0 = number.substring(0,3);
            String num1 = number.substring(3,6);
            print =num0+" "+num1;
        }

        if(number.length()==5)//10 000
        {
            String num0 = number.substring(0,2);
            String num1 = number.substring(2,5);
            print =num0+" "+num1;
        }

        if(number.length()==4)//1 000
        {
            String num0 = number.substring(0,1);
            String num1 = number.substring(1,4);
            print =num0+" "+num1;
        }
        if(number.length()==3)//100
        {
            print =number;
        }
        return print;
    }

    public String ArrayToString(String[] arr)
    {
        String print=null;
        for (int i=0 ;i<arr.length;i++)
        {
            if(arr[i]!=null)
            {
                if(print!=null)
                {
                    print = print+","+arr[i];
                }
                else
                {
                    print = arr[i];
                }
            }
        }
        return print;
    }
    //
    public int ArrayNoNullCount(String[] arr)
    {
        int print= 0;
        for (int i=0 ;i<arr.length;i++)
        {
            if(arr[i]!=null)
            {
                print++;
            }
        }
        return print;
    }
    //
    public boolean isValArray(double[] arr, double value)
    {
        boolean print = false;
        for (int i=0 ;i<arr.length;i++)
        {
            if(arr[i]==value)
            {
                print = true;
                break;
            }
        }
        return print;
    }
    //
    public String NumSumm(String[] number)
    {
        int printSumm =0;
        if(number.length!=0)
        {
            for (int i=0; i < number.length; i++)
            {
                int not_long = number[i].indexOf(".");
                if(not_long>0)
                {number[i] = number[i].substring(0,not_long);}
                printSumm = Integer.parseInt(number[i])+printSumm;
            }
        }

        return printSumm+"";
    }

    //
    public int typeImage(int number)
    {
        int printType = 1;
        if(number==3)
        {
           printType=0;
        }

        return printType;
    }



/********************************************************************************************/

    //16.08.2013
    public String DateFormate(String date_sql, String thisformat, String endformat)
    {
        String print_date = date_sql;
        if(thisformat==null)
            thisformat = "yyyy-mm-dd'T'HH:mm:ssZ";

        if(endformat==null)
            endformat = "yyyy-mm-dd";

        print_date = getDate(print_date,thisformat,endformat);
        return print_date;
    }
    //
    public String DateFormate(String date_sql)
    {
        String print_date = date_sql;
        String thisformat = "yyyy-mm-dd'T'HH:mm:ssZ";
        String endformat = "yyyy-mm-dd";
        print_date = getDate(print_date,thisformat,endformat);
        return print_date;
    }
    //
    public String NumFormatDate(int Num)
    {
        String print = Num+"";
        String[] intArray = new String[]{"01","02","03","04","05","06","07","08","09"};
      try{
          print = intArray[Num-1];
      }
      catch (Exception ex)
      {
          ex.printStackTrace();
      }
        return print;
    }




    //
    public String getDateFormat(boolean ThisDate)
    {
        String print;
        //
         Calendar сalendar = Calendar.getInstance();
        //Год
        int mYear = сalendar.get(Calendar.YEAR);
        //Месяц
        int mMonth = сalendar.get(Calendar.MONTH)+1;
        //День
        int mDay = сalendar.get(Calendar.DAY_OF_MONTH);

        String myYear =mYear+"";
        String myMonth = this.NumFormatDate(mMonth);
        String myDay = this.NumFormatDate(mDay);

        if(!ThisDate) {
            if (mMonth == 1) {
                myMonth = "12";
                myYear = "" + (mYear - 1);
            } else {
                myMonth = new Halpe().NumFormatDate((mMonth - 1));
            }
        }
        print = myYear+"-"+myMonth+"-"+myDay;

        return print;
    }
    //
    private String getDate(String date_sql, String thisformat,String endformat){
        String print_date = date_sql;
        SimpleDateFormat sdf = new SimpleDateFormat(thisformat);
        java.util.Date dateFormat;
        try {

            dateFormat =  sdf.parse(date_sql);
            sdf.applyPattern(endformat);
            print_date = sdf.format(dateFormat).toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return print_date;
    }
/********************************************************************************************/


    //
    public String NumFormatPhone(String phone)
    {
        String print = phone;

        try {
            String region = phone.substring(0, 3);
            String num_start = phone.substring(3, 6);
            String num_center = phone.substring(6, 8);
            String num_end = phone.substring(8, 10);
            print = "+7 (" + region + ")" + " " + num_start + " " + num_center + " " + num_end;
        }
        catch (Exception e){e.printStackTrace();}
        return print;
    }
    //
    public String StringToEmpty(String value, String pattern_replace,String replace_to)
    {
        String print = value;
        if(pattern_replace==null)
        {
            pattern_replace ="anyType{}";
        }
        if(value.equals(pattern_replace))
        {
            print = replace_to;
        }

        return print;
    }
    //
    public String StringToEmpty(String value, String pattern_replace)
    {
        String print = value;
        if(pattern_replace==null)
        {
            print ="";
        }

        if(value.equals("anyType{}"))
        {
            print = pattern_replace;
        }

        return print;
    }

    //
    public String StringToEmpty(String value)
    {
        String print = value;


        if(value.equals("anyType{}"))
        {
            print = "";
        }

        return print;
    }

    //
    public int position(Cursor c,String findName)
    {
        int pos=0;
        if(c!=null)
        {
        for (int i=0;i<c.getCount();i++)
        {
            if(findName.equals(c.getString(c.getColumnIndex(c.getColumnName(0)))))
            {
                pos = i;
                break;
            }
        }
        }
        return pos;
    }
//Метод сложения 2-х строковых масивов
public String[] weekArray(String[] a, String[] b) {
    if (a == null)
        return b;
    if (b == null)
        return a;
    String[] r = new String[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
}
    //Метод проверки сети интернет
    public boolean isConnectMobOrWiFi(Context context)
    {

        boolean isConnected =false;
    try {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork.isConnected();

       /* boolean isConnectedOrConnecting = activeNetwork.isConnectedOrConnecting();

        if (isConnectedOrConnecting)
        {
            int typeCon = activeNetwork.getType();

                switch (typeCon) {
                    case 0:
                        Toast.makeText(context, "mobile", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show();
                        break;
                }
        }*/

    } catch (Exception e){
        e.printStackTrace();
    }
    return isConnected;
}

    //Формат если 0 то false a если 1 то true
  public int FormatBoolToInt(boolean boo)
    {
        int B = 0;
        if(boo)
        B = 1;
        return B;
    }
//Формат если 0 то false a если 1 то true
  public boolean FormatIntToBool(int Int)
    {
        boolean B = false;
        if(Int != 0)
            B = true;
        return B;
    }
//
    /** Check if this device has a camera */
    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Method used for encode the file to base64 binary format
     * @param patch String
     * @return encoded file format
     */
    public String encodeFileToBase64Binary(String patch)
    {

        Uri file_patch = Uri.parse("file://" + patch);
        File file = new File(file_patch.getPath());

        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }

    /**
     * Method used for encode the file to base64 binary format
     * @param Base64Str String
     * @return decode byte[] format
     */
    public byte[] decodeBase64ToBinary(String Base64Str)
    {
        byte[] bytes = null;
        try {

            bytes = Base64.decode(Base64Str, Base64.NO_WRAP);
        }
         catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
    }

public String getHashCode(String value)
{
   String print = null;
     if(value!=null)
     {  // String to be encoded with Base64


       byte[] data = null;
       try {
           data = value.getBytes("UTF-8");
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
       print = Base64.encodeToString(data, Base64.NO_WRAP);
     }
    return print;
}
    public String getHashCodeToString(String value)
    {
        String print = null;
        if(value!=null)
        {  // Receiving side
          byte[] data = Base64.decode(value, Base64.NO_WRAP);

          try {
              print = new String(data, "UTF-8");
          } catch (UnsupportedEncodingException e)
          {
              e.printStackTrace();
          }
        }
        return print;
    }

    public String getMD5(String value,int count)
    {
        String print =null;
        for (int i=0; i<count; i++)
        {
            if(i==0)
            print =  getHashCode(value);
            if(i>0)
            print =  getHashCode(print);
        }


        return print;
    }

    public String getMD5toString(String value,int count)
    {
        String print = value;
        for (int i=0; i<count; i++)
        {

                print =  getHashCodeToString(print);

        }


        return print;
    }
   /*Создание директорий по указанному пути*/
   public void mkDirs(String dir)
   {
       if(dir==null){ dir="/sdcard/CameraExample/";}
       File saveDir = new File(dir);
       if (!saveDir.exists())
       {
           saveDir.mkdirs();
       }

   }
    /*Создание директорий по указанному пути*/
    public boolean existFile(String putch)
    {
        boolean exist = false;
        File file = new File(putch);
        if (file.exists())
        {
            exist =true;
        }
        return exist;
    }

    public float dpFromPx(float px,Context context)
    {
        return px/context.getResources().getDisplayMetrics().density;
    }

}
