package p4.guide_animals.Services;

import android.content.Context;


/**
 * Created by Kravtsov.a on 11.12.14.
 */
public class ImageServices {

    private Context context;
    private static String patch;

 /*public String  setFoto(Context context, String url)
 {
        this.context =context;
        patch = getPatch(url);
        return patch;
 }
 public String  setAvatar(Context context, String url)
 {
     this.context =context;
     patch = getPatch(url);
     return patch;
 }


 //
 public String getPatch(String URLImage)
    {

        String setURL = context.getString(R.string.domain)+URLImage;
        patch = URLImage;
        try{
            URL url = new URL(setURL);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            patch = saveBitmap(bmp,context);
        }
        catch (Exception e){

            e.printStackTrace();
        }
        return patch;
    }
    //
    public String saveBitmap(Bitmap bitmap,Context context)
    {
        String NameDirLoad ="lkfm";
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

        String filePath = String.format("%s%simage_%d.png", PathDir, File.separator, System.currentTimeMillis());
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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

*/

}
