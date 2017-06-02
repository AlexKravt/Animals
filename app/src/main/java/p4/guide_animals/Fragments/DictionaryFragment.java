package p4.guide_animals.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.sweetalert.SweetAlertDialog;
import p4.guide_animals.Dialogs.DialogMyProgress;
import p4.guide_animals.DictionaryActivity;
import p4.guide_animals.MainActivity;
import p4.guide_animals.PayActivity;
import p4.guide_animals.R;
import p4.guide_animals.Services.ServerServices;
import p4.guide_animals.model.TableBase;


public class DictionaryFragment extends Fragment
{
    private final static int ID_ORDER = 0;
    private final static String ID_ZN = "";
    private final static int AMOUNT_PAY = 50;
    private Context context;
    private View rootView;
    private AQuery AQ;
    private ServerServices server;
    private int fragment_id, type_id, type_url;
    private String url_items;
    private TableBase tableBase;
    private TypedArray Icons;
    private int idIcon;
    private boolean isPayment;
    private boolean isHomeAnimals = false;
    public DictionaryFragment(){}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            this.fragment_id = bundle.getInt("_position");
            this.type_id =     bundle.getInt("_type");
            this.url_items =   bundle.getString("_url");

            if(bundle.containsKey(DictionaryActivity.HOME_BUNDLE))
            this.isHomeAnimals =   bundle.getBoolean(DictionaryActivity.HOME_BUNDLE);
        }

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        AQ = new AQuery(rootView);
        context = rootView.getContext();
        server = new ServerServices(context,AQ);
        isPayment = server.isPayment();
        tableBase = new TableBase(context);
        AQ.id(R.id.btnFishRoot).gone();
        //Иконки
         Icons = context.getResources().obtainTypedArray((!isHomeAnimals?R.array.nav_dict_icons:R.array.nav_dict_animals_home_icons));
         idIcon = (!isHomeAnimals?fragment_id:fragment_id-1);
         //
         getImageBig();
        //
         getListMenu();
         return rootView;
        }

    public void getImageBig()
    {
       if(!isHomeAnimals)
       {

        switch (fragment_id)
        {
            case MainActivity.ANIMAL_BIRDS:
                AQ.id(R.id.bg_top_img).image(R.drawable.birds);
                break;
            case MainActivity.ANIMAL_OLEN:
                AQ.id(R.id.bg_top_img).image(R.drawable.oleny);
                break;
            case MainActivity.ANIMAL_HORSE:
                AQ.id(R.id.bg_top_img).image(R.drawable.horse);
                break;
            case MainActivity.ANIMAL_RABBIT:
                AQ.id(R.id.bg_top_img).image(R.drawable.belka);
                break;
            case MainActivity.ANIMAL_PREADATOR:
                AQ.id(R.id.bg_top_img).image(R.drawable.preadator);
                break;
            case MainActivity.ANIMAL_REPTIL:
                AQ.id(R.id.bg_top_img).image(R.drawable.reptilii);
                break;
            case MainActivity.ANIMAL_HOME:
                AQ.id(R.id.bg_top_img).image(R.drawable.reptilii);
                break;
            default:
                AQ.id(R.id.bg_top_img).image(R.drawable.reptilii);
                break;
        }
       }
        else
       {
           switch (fragment_id)
           {
               case DictionaryActivity.ANIMAL_DOG:
                   AQ.id(R.id.bg_top_img).image(R.drawable.dog_big);
                   break;
               case DictionaryActivity.ANIMAL_CAT:
                   AQ.id(R.id.bg_top_img).image(R.drawable.cat_big);
                   break;
               case DictionaryActivity.ANIMAL_HORSE:
                   AQ.id(R.id.bg_top_img).image(R.drawable.horse);
                   break;
               case DictionaryActivity.ANIMAL_RABBIT:
                   AQ.id(R.id.bg_top_img).image(R.drawable.more_pig);
                   break;
               case DictionaryActivity.ANIMAL_PIG:
                   AQ.id(R.id.bg_top_img).image(R.drawable.pig);
                   break;
               case DictionaryActivity.ANIMAL_BIRDS:
                   AQ.id(R.id.bg_top_img).image(R.drawable.birds);
                   break;
               default:
                   AQ.id(R.id.bg_top_img).image(R.drawable.home_animals);
                   break;
           }
       }
    }



    //
    public void getListMenu()
    {
        AQ.id(R.id.no_content).gone();

        //
       Cursor c = tableBase.getItemsCatalog(type_id);
       if(c==null)
       {
           getMenuAnimals();
       }
        else
       {
           LinearLayout lm_block = (LinearLayout) rootView.findViewById(R.id.list_menu_blok);

           for (int i = 0; i < c.getCount(); i++)
           {

               try {
                     c.moveToPosition(i);

                   LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                   View tampl = mInflater.inflate(R.layout.inflate_list_menu_fragment, null);
                   //
                   AQuery AQ = new AQuery(tampl);
                   final int _id = c.getInt(c.getColumnIndex("ap_id"));
                   AQ.id(R.id.icon).image(Icons.getResourceId(idIcon, -1));
                   AQ.id(R.id.tv_root).text(c.getString(c.getColumnIndex("ap_title")));
                   String comment = c.getString(c.getColumnIndex("ap_comment"));
                   if(!comment.equals(""))
                   AQ.id(R.id.tv_comment).text(comment).visible();

                   if(!isPayment)
                   {
                       if (i > 0) {
                           AQ.id(R.id.icon1).visible();
                       }
                   }

                   //Действия по выбору раздела
                   onClickItem(AQ, i, _id);

                   lm_block.addView(tampl, i);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }

    }


   private void getMenuAnimals()
   {
       DialogMyProgress dialog = new DialogMyProgress(context);
       dialog.setmTitle(getString(R.string.title_load_items));
       dialog.setmText(getString(R.string.alert_load_items));

       //Получаем данные
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("itemsList", "true");
       params.put("type", type_id);

       AQ.progress(dialog).ajax(url_items, params, JSONArray.class, new AjaxCallback<JSONArray>()
       {
           @Override
           public void callback(String url, JSONArray json, AjaxStatus status) {

               if (json != null)
               {
                   //
                   ContentValues Data = new ContentValues();
                   //
                   LinearLayout lm_block = (LinearLayout) rootView.findViewById(R.id.list_menu_blok);
                   for (int i = 0; i < json.length(); i++)
                   {
                       try {
                           final JSONObject itemObject = json.getJSONObject(i);
                           LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                           View tampl = mInflater.inflate(R.layout.inflate_list_menu_fragment, null);
                           //
                           AQuery AQ = new AQuery(tampl);
                           final int _id = json.getJSONObject(i).getInt("id");
                           AQ.id(R.id.icon).image(Icons.getResourceId(idIcon, -1));
                           AQ.id(R.id.tv_root).text(itemObject.getString("title"));
                           String comment = itemObject.getString("comment");
                           if(!comment.equals(""))
                               AQ.id(R.id.tv_comment).text(comment).visible();

                           if(!isPayment)
                           {
                               if (i > 0) {
                                   AQ.id(R.id.icon1).visible();
                               }
                           }

                           //Действия по выбору раздела
                           onClickItem(AQ, i, _id);

                           //Заполняем данными таблицу меню каталога
                           Data.put("ap_id", itemObject.getString("id"));
                           Data.put("ap_title", itemObject.getString("title"));
                           Data.put("ap_comment", itemObject.getString("comment"));
                           Data.put("ap_content", itemObject.getString("content"));
                           Data.put("ap_vid", itemObject.getString("cat_vid"));
                           Data.put("ap_cat_famely", itemObject.getString("cat_famely"));
                           Data.put("ap_cat_rod", itemObject.getString("cat_rod"));
                           tableBase.InsertCatalog(Data);


                           lm_block.addView(tampl, i);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }


               }
               else
               {
                   AQ.id(R.id.no_content).visible();
               }

           }
       });
   }

   private void onClickItem(AQuery AQ,final int i, final int _id)
   {
       AQ.id(R.id.list_menu_fragment).clicked(new View.OnClickListener() {
           public void onClick(View v)
           {
               if(i==0 || isPayment)
               {
                   openContent(_id);
               }
               else
               {
                   dialogSendPay();
               }
           }
       });
   }


    private void openContent(int id)
    {
        Intent intent = new Intent(context, (!isHomeAnimals ?MainActivity.class:DictionaryActivity.class));
        intent.putExtra("itemId", id);
        intent.putExtra("fragmentMainId", fragment_id);
        startActivity(intent);
    }

    private void openPayContent()
    {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("idOrder", ID_ORDER);
        intent.putExtra("num_zn",ID_ZN);
        intent.putExtra("sum", AMOUNT_PAY);
        startActivity(intent);
    }
    //
    private void  goMainActivity()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("inet_connect",false);
        startActivity(intent);
    }


    private void dialogSendPay()
    {

        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(getString(R.string.alert));
        dialog.setContentText(getString(R.string.alert_pay_content));
        dialog.setConfirmText(getString(R.string.btn_pay_go_url));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                openPayContent();
            }
        });


            dialog.setCancelText(getString(R.string.close));
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });


        dialog.show();
    }

    //
   /* private  void getAlertError(String text)
    {
        AlertShow.setDialogAlert(context, getString(R.string.alert),
                text,
                false,
                true,
                "",
                getString(R.string.close)
        );
    }*/
    }
