package p4.guide_animals.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 04.02.2015.
 */
public class SelectDictItemsCursorAdapter extends BaseAdapter {
    private Context context;
    private Cursor cr;
    int COUNT;


    public SelectDictItemsCursorAdapter(Context cont, Cursor cursor) {
        context = cont;
        COUNT = cursor.getCount();
        cr = cursor;
    }


    public int getCount() {
        return cr.getCount();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    //
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.inflate_items_dict, null);
        }

        AQuery AQ = new AQuery(convertView);
        if (cr.moveToPosition(position))
        {
            String image_uri = cr.getString(cr.getColumnIndex(cr.getColumnName(2)));
            String title = cr.getString(cr.getColumnIndex(cr.getColumnName(3)));
            AQ.id(R.id.textTitle).text(title);

            if(!image_uri.equals(""))
                AQ.id(R.id.icon).image(context.getString(R.string.url_api)+image_uri,true,true,100, AQuery.GONE);
        }

        return convertView;
    }

}
