package p4.guide_animals.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import org.json.JSONArray;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 04.02.2015.
 */
public class SelectItemsJsonCountryAdapter extends BaseAdapter {
    private Context context;
    private JSONArray jn;
    int COUNT = 0;


    public SelectItemsJsonCountryAdapter(Context cont, JSONArray json)
    {
        this.context = cont;
        this.COUNT = json.length();
        this.jn = json;
    }


    public int getCount() {
        return COUNT;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    //
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView==null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.inflate_items_dict, null);
        }

        AQuery AQ = new AQuery(convertView);
       try
       {
           String nameCountry = jn.getString(position).toString();
           AQ.id(R.id.textTitle).text(nameCountry);
         //  AQ.id(R.id.icon).gone();

       }
       catch (Exception e)
        {
         e.printStackTrace();
        }

        return convertView;
    }

}
