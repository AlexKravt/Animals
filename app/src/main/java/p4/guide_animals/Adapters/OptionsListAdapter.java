package p4.guide_animals.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import p4.guide_animals.R;
import p4.guide_animals.model.OptionsItem;

public class OptionsListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<OptionsItem> nawOptionsItems;

	public OptionsListAdapter(Context context, ArrayList<OptionsItem> navOptionsItem){
		this.context = context;
		this.nawOptionsItems = navOptionsItem;
	}

	@Override
	public int getCount() {
		return nawOptionsItems.size();
	}

	@Override
	public Object getItem(int position) {
		return nawOptionsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.options_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        txtTitle.setText(nawOptionsItems.get(position).getTitle());

        //Добовляем иконки если есть
        if(nawOptionsItems.get(position).getCounterVisibility()){
            imgIcon.setImageResource(nawOptionsItems.get(position).getIcon());
        }else{
            // hide the counter view
            imgIcon.setVisibility(View.GONE);
        }

        //Добавляем в конец заголовка текст
        if(nawOptionsItems.get(position).getCounterVisibility()){
        	txtCount.setText(nawOptionsItems.get(position).getCount());
        }else{
        	// hide the counter view
        	txtCount.setVisibility(View.GONE);
        }
        
        return convertView;
	}

}
