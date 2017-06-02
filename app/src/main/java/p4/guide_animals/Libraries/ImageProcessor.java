package p4.guide_animals.Libraries;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kravtsov.a on 10.10.14.
 */
public class ImageProcessor {
    public static final double PI = 3.14159d;
    public static final double FULL_CIRCLE_DEGREE = 360d;
    public static final double HALF_CIRCLE_DEGREE = 180d;
    public static final double RANGE = 256d;

    public static Bitmap tintImage(Bitmap src, int degree) {

        int width = src.getWidth();
        int height = src.getHeight();

        int[] pix = new int[width * height];
        src.getPixels(pix, 0, width, 0, 0, width, height);

        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (PI * (double)degree) / HALF_CIRCLE_DEGREE;

        int S = (int)(RANGE * Math.sin(angle));
        int C = (int)(RANGE * Math.cos(angle));

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = ( pix[index] >> 16 ) & 0xff;
                int g = ( pix[index] >> 8 ) & 0xff;
                int b = pix[index] & 0xff;
                RY = ( 70 * r - 59 * g - 11 * b ) / 100;
                GY = (-30 * r + 41 * g - 11 * b ) / 100;
                BY = (-30 * r - 59 * g + 89 * b ) / 100;
                Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
                RYY = ( S * BY + C * RY ) / 256;
                BYY = ( C * BY - S * RY ) / 256;
                GYY = (-51 * RYY - 19 * BYY ) / 100;
                R = Y + RYY;
                R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
                G = Y + GYY;
                G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
                B = Y + BYY;
                B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
                pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
            }

        Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);

        pix = null;

        return outBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //Метод получения оптимизированого изображения
    public Bitmap getResizeFile(String path,Point point,Bitmap bp)
    {
// create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();
//just decode the file
        opts.inJustDecodeBounds = true;
        bp = BitmapFactory.decodeFile(path, opts);

//get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;
//initialization of the scale
        int resizeScale = 1;
//get the good scale
        if ( orignalWidth > point.x || orignalHeight > point.y ) {
            final int heightRatio = Math.round((float) orignalHeight / (float) point.y);
            final int widthRatio = Math.round((float) orignalWidth / (float) point.x);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
//put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;
//get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;
//check if it's possible to store into the vm java the picture
        if ( Runtime.getRuntime().freeMemory() > bmSize ) {
//decode the file
            bp = BitmapFactory.decodeFile(path, opts);
        } else
        {  return null;}
        return bp;
    }


    //Метод получения оптимизированого изображения
    public Bitmap getResizeFile(String path,int x, int y)
    {
        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //just decode the file
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;
        //initialization of the scale
        int resizeScale = 1;
        //get the good scale
        if ( orignalWidth > x || orignalHeight > y ) {
            final int heightRatio = Math.round((float) orignalHeight / y);
            final int widthRatio = Math.round((float) orignalWidth / x);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;
        //get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;
        //check if it's possible to store into the vm java the picture
        long memoryLimit = Runtime.getRuntime().freeMemory();

        if(memoryLimit > bmSize)
        {
        //decode the file
         bp = BitmapFactory.decodeFile(path, opts);
        }
        else
        {
            return null;
        }
        return bp;
    }


    public String saveBitmap(Bitmap bitmap,String NameDirLoad)
    {
        String PathDirDCIM = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM;
        String PathDir = PathDirDCIM + "/" +NameDirLoad;// getString(R.string.savedir);
        File dirDCIM = new File(PathDirDCIM);
        if(!dirDCIM.exists()) {
            dirDCIM.mkdir();
        }

        File dir = new File(PathDir);
        if(!dir.exists()) {
            dir.mkdir();
        }

        String filePath = String.format("%s%sscreen_%d.png", PathDir, File.separator, System.currentTimeMillis());
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
           // MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath.getAbsolutePath(), imagePath.getName(),  imagePath.getName()); // регистрация в фотоальбоме
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }



    //Метод для вычисления ширины и высоты экрана
    private static Point getDisplaySize(final Display display) {
        Point point = new Point();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){   //API LEVEL 13
            display.getSize(point);
        }else{
            //Это для более ранних версий
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    //
    public Point Point(Context context)
    {
        //Измеряем ширину и высоту дисплея
        WindowManager wm = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        Point myPoint = this.getDisplaySize(display);//Соответственно получаем координаты по X и Y
        return myPoint;
    }


   //
   public   byte[] resizeImage(byte[] input,int PHOTO_WIDTH,int PHOTO_HEIGHT)
   {
        Bitmap original = BitmapFactory.decodeByteArray(input, 0, input.length);
        Bitmap resized = Bitmap.createScaledBitmap(original, PHOTO_WIDTH, PHOTO_HEIGHT, true);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 80, blob);

        return blob.toByteArray();
   }

   //
  /* public  String resizeImage(String patch,int PHOTO_WIDTH,int PHOTO_HEIGHT)
   {
        //Получаем файл
        File file = new File(patch);
       //Получаем масив байтов из файла
        byte[] input = new byte[(int)file.length()];
       //Получаем оригинал файла в битмап формате
        Bitmap original = BitmapFactory.decodeByteArray(input , 0, input.length);
       //Изменяем размер
        Bitmap resized = Bitmap.createScaledBitmap(original, PHOTO_WIDTH, PHOTO_HEIGHT, true);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 80, blob);

        return blob.toByteArray();
   }*/

    public String saveByteToFileStorag(byte[] ArrayOfByte ,String rootPach, String fileName)
    {

        String fileDir = rootPach+ String.format(fileName + "%d.jpg", System.currentTimeMillis());
        FileOutputStream th_os = null;
        try {
            th_os = new FileOutputStream(fileDir);
            th_os.write(ArrayOfByte);
            th_os.flush();
            th_os.close();
            th_os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       if(th_os!=null)
       {
           return  fileDir;
       }
        else
       {
           return  null;
       }
    }



    public Bitmap FileToBitmap(String pach)
    {
        Bitmap bm = null;
        try
        {
            bm = getResizeFile(pach,400, 400);// BitmapFactory.decodeFile(Uri.parse("file://" + pach).getPath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       return bm;
    }

    public String getResizeFilePach(String pach,int w,int h)
    {
        Bitmap bm = FileToBitmap(pach);
        if(bm!=null)
        {
            bm = Bitmap.createScaledBitmap(bm, w, h, true); //getResizedBitmap(bm, h, w);
            pach = saveBitmap(bm,"GalleryHunting");
        }
        else
        {
            return null;
        }
        return pach;
    }


    public String getNewDir(String nameDirRoot, String nameDirLoad)
    {
        String PathDirDCIM = Environment.getExternalStorageDirectory() + "/" + nameDirRoot;
        String PathDir = PathDirDCIM + "/" +nameDirLoad;

        File dirDCIM = new File(PathDirDCIM);
        if(!dirDCIM.exists()) {
            dirDCIM.mkdir();
        }

        File dir = new File(PathDir);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return PathDir;
    }

}
