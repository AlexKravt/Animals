package p4.guide_animals.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 04.02.2015.
 */
public class SelectItemsJsonAdapter extends BaseAdapter {
    private Context context;
    private JSONArray jn;
    int COUNT = 0;


    public SelectItemsJsonAdapter(Context cont, JSONArray json)
    {
        this.context = cont;
        this.COUNT = json.length();
        this.jn = json;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //
    @Override
    public View getView( int position, View convertView, ViewGroup parent) {


        if(convertView==null)
        {    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.inflate_items_dict, null);
        }

        AQuery AQ = new AQuery(convertView);
       try
       {
           JSONObject itemObject = jn.getJSONObject(position);
          // long id = itemObject.getLong("id");

              if(itemObject.has("part"))
              {
                  String title_mini = itemObject.getString("part");
                  AQ.id(R.id.title_mini).text(title_mini).visible();
              }

           String title ="";
           if(itemObject.has("name"))
           {
               title = itemObject.getString("name");
           }

           if(itemObject.has("title"))
           {
               title = itemObject.getString("title");
           }

           if(itemObject.has("city"))
           {
               title = itemObject.getString("city");
           }

           AQ.id(R.id.textTitle).text(title);

           String image_uri ="";
           if(itemObject.has("image_icon"))
           {
               image_uri = itemObject.getString("image_icon");
               if(!image_uri.equals(""))
               AQ.id(R.id.icon).image(context.getString(R.string.url_api)+image_uri,true,true,100, AQuery.GONE);
           }



       }
       catch (Exception e)
        {
         e.printStackTrace();
        }

        return convertView;
    }

}
