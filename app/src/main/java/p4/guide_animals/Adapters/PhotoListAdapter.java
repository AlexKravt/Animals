package p4.guide_animals.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoListAdapter extends BaseAdapter {

	private Context context;
	private Cursor cursor;
    private final int SMALL_IMAGE =1;
	public PhotoListAdapter(Context context, Cursor c){
		this.context = context;
		this.cursor = c;
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		return cursor.moveToPosition(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes

            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
            /*imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);*/
           // imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        String PhotoPath = cursor.getString(cursor.getColumnIndex("ap_PhotoPathSmall"));
        if(PhotoPath!=null)
        {
            //AQuery AQ = new AQuery(context);
            try
            {
               /* Uri file_patch = Uri.parse("file://" + PhotoPath);
                File f = new File(file_patch.getPath());
                AQ.id(imageView).image(f,150);*/
                imageView.setImageURI(Uri.parse("file://" + PhotoPath));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return imageView;
	}

}
