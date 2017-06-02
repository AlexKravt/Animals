package p4.guide_animals.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidquery.AQuery;

import java.util.Dictionary;

import p4.guide_animals.Dialogs.AlertShow;
import p4.guide_animals.DictionaryActivity;
import p4.guide_animals.Halper.Halpe;
import p4.guide_animals.MainActivity;
import p4.guide_animals.R;
import p4.guide_animals.model.TableBase;

public class HomeFragment extends Fragment
{
    private View rootView;
    private Context context;
    private AQuery AQ;
    private TableBase tableBase;
    private Halpe helper;
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
 
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //
        context = rootView.getContext();
        //
        AQ = new AQuery(rootView);
        //
        tableBase = new TableBase(context);
        //
        helper = new Halpe();



        AQ.id(R.id.btnFishRoot).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://play.google.com/store/search?q=pub%3A%22Aleksandr%20K.%22
                //Открываем свои приложения в Google Play
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(context.getString(R.string.play_google_my_apps)));
                getActivity().startActivity(intent);

            }
        });


        Bundle bundle = getArguments();
        if(bundle!=null && bundle.containsKey(DictionaryActivity.HOME_BUNDLE))
        {
            AQ.id(R.id.bg_top_img).image(R.drawable.home_animals);
            getListMenuAnimalsHome();
        }
        else{
            getListMenu();
        }

          return rootView;
        }

    private void getListMenu()
    {

        LinearLayout lm_block =(LinearLayout) rootView.findViewById(R.id.list_menu_blok);
        //Иконки
        TypedArray Icons = context.getResources().obtainTypedArray(R.array.nav_dict_icons);
        //Заголовки
        String[] itemsTitle = getResources().getStringArray(R.array.nav_dict_items);
        //Комментарий
        String[] itemsComment = getResources().getStringArray(R.array.nav_dict_comment);

        for (int i=0;i<itemsTitle.length; i++)
        {
           if(i!=0)
           {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View tampl = mInflater.inflate(R.layout.inflate_list_menu_fragment, null);
            AQuery AQ = new AQuery(tampl);

            final int id = i;
            AQ.id(R.id.icon).image(Icons.getResourceId(i, -1));
            AQ.id(R.id.tv_root).text(itemsTitle[i]);

            if(!itemsTitle[i].equals(""))
            AQ.id(R.id.tv_comment).text(itemsComment[i]).visible();

            AQ.id(R.id.list_menu_fragment).clicked(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("fragmentMainId",id);
                    startActivity(intent);
                }
            });
            lm_block.addView(tampl, i-1);
           }
        }

    }

    private void getListMenuAnimalsHome()
    {

        LinearLayout lm_block =(LinearLayout) rootView.findViewById(R.id.list_menu_blok);
        //Иконки
        TypedArray Icons = context.getResources().obtainTypedArray(R.array.nav_dict_animals_home_icons);
        //Заголовки
        String[] itemsTitle = getResources().getStringArray(R.array.nav_animals_home_items);
        //Комментарий
        String[] itemsComment = getResources().getStringArray(R.array.nav_dict_animals_home_comment);

        for (int i=0;i<itemsTitle.length; i++)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View tampl = mInflater.inflate(R.layout.inflate_list_menu_fragment, null);
            AQuery AQ = new AQuery(tampl);

            final int id = i;
            AQ.id(R.id.icon).image(Icons.getResourceId(i, -1));
            AQ.id(R.id.tv_root).text(itemsTitle[i]);

            if(!itemsComment[i].equals(""))
            AQ.id(R.id.tv_comment).text(itemsComment[i]).visible();

            AQ.id(R.id.list_menu_fragment).clicked(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, DictionaryActivity.class);
                    intent.putExtra("fragmentMainId",(id+1));
                    startActivity(intent);
                }
            });
            lm_block.addView(tampl, i);

        }

    }
    }
