package p4.guide_animals.Dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONObject;

import p4.guide_animals.Adapters.SelectDictItemsCursorAdapter;
import p4.guide_animals.Adapters.SelectItemsJsonAdapter;

public class DialogItemsDict extends AlertDialog
{



    private ListView listView;
    private LinearLayout llroot;

    public DialogItemsDict(Context context, final OnSelectedListener onSelectedListener, final JSONArray json)
    {
        super(context);
        this.setCanceledOnTouchOutside(false);


        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
       /*------------------------------------------*/
        llroot = new LinearLayout(context);
        listView = new ListView(context);
        SelectItemsJsonAdapter adapter = new SelectItemsJsonAdapter(context,json);
        if(adapter!=null)
        listView.setAdapter(adapter);
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    JSONObject itemObject = json.getJSONObject(position);
                    long id_ = itemObject.getLong("id");
                    onSelectedListener.onSelected(id_);
                    dismiss();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llroot.addView(listView,layoutParams);
        relativeLayout.addView(llroot, layoutParams);
        setView(relativeLayout);
    }



    public DialogItemsDict(Context context, final OnSelectedListener onSelectedListener, final Cursor c)
    {
        super(context);
        this.setCanceledOnTouchOutside(false);


        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
       /*------------------------------------------*/
        llroot = new LinearLayout(context);
        listView = new ListView(context);
        SelectDictItemsCursorAdapter adapter = new SelectDictItemsCursorAdapter(context,c);
        if(adapter!=null)
            listView.setAdapter(adapter);
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    c.moveToPosition(position);
                    long id_ = c.getLong(c.getColumnIndex("ap_idItem"));
                    onSelectedListener.onSelected(id_);
                    dismiss();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llroot.addView(listView,layoutParams);
        relativeLayout.addView(llroot, layoutParams);
        setView(relativeLayout);
    }


    public interface OnSelectedListener
    {
        public void onSelected(long id);
    }
}
