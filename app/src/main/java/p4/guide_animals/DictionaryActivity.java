package p4.guide_animals;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import java.util.ArrayList;
import p4.guide_animals.Adapters.MenuListAdapter;
import p4.guide_animals.Dialogs.AlertShow;
import p4.guide_animals.Fragments.DictionaryFragment;
import p4.guide_animals.Fragments.HomeFragment;
import p4.guide_animals.Fragments.WebViewFragment;
import p4.guide_animals.Halper.Halpe;
import p4.guide_animals.model.NavDrawerItem;



public class DictionaryActivity extends AppCompatActivity {

    /**/
    public static int itemId = 0;
    public static int PositionMain = 0;

    public final int FRAGMENT_HOME = 0;
    public final static int ANIMAL_DOG = 1;
    public final static int ANIMAL_CAT = 2;
    public final static int ANIMAL_RABBIT = 3;
    //public final static int ANIMAL_COW = 4;
    public final static int ANIMAL_HORSE = 4;
    public final static int ANIMAL_PIG = 5;
    public final static int ANIMAL_BIRDS = 6;
    public final static int CATALOG_ID_DOG = 7;
    public final static int CATALOG_ID_CAT = 8;
    public final static int CATALOG_ID_RABBIT = 9;
    public final static int CATALOG_ID_COW = 10;
    public final static int CATALOG_ID_HORSE = 11;
    public final static int CATALOG_ID_PIG = 12;
    public final static int CATALOG_ID_BIRDS = 13;
    public final static String HOME_BUNDLE ="animal_home";

    /**/
    public SharedPreferences mSettings;
    public static final String APP_PREFERENCES="settings";
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerMenu;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private MenuListAdapter adapter;
    private Context context;
    private Toolbar toolbar;
    private Halpe helper;
    private AQuery AQ;
    private ImageView imgMenu;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = DictionaryActivity.this;
        AQ = new AQuery(context);

        //Скрываем клавиатуру при просмотре фрагмента, если есть поля для ввода
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //Получаем файл настроек
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        helper = new Halpe();

        imgMenu = (ImageView) findViewById(R.id.img_menu);


        mTitle = mDrawerTitle = getTitle();
        //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //
        mDrawerMenu = (LinearLayout) findViewById(R.id.navigation_drawer);
        //
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        //
        ArrayList<NavDrawerItem> menuListIcons = ListMenuIcons();
        //
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        //
        adapter = new MenuListAdapter(getApplicationContext(), menuListIcons);
        //
        mDrawerList.setAdapter(adapter);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //  R.drawable.apptheme_ic_navigation_drawer, //nav menu toggle icon
                R.string.title_animal_home, // nav drawer open - description for accessibility
                R.string.title_animal_home // nav drawer close - description for accessibility
        )
        {
            public void onDrawerClosed(View view) {
                toolbar.setTitle(mTitle);
                toolbar.setVisibility(View.VISIBLE);

                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle(mDrawerTitle);
                toolbar.setVisibility(View.GONE);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
       // mDrawerLayout.setDrawerListener(mDrawerToggle);

        //
        if (savedInstanceState == null) {

            //Выводим нужный нам фрагмент если не задан то выводится по умалчанию
            PositionMain = getIntent().getIntExtra("fragmentMainId", 0);
            itemId = getIntent().getIntExtra("itemId", 0);
            displayView(PositionMain);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getFragmentManager();
        Fragment fr = fm.findFragmentById(R.id.frame_container);

        if (fr instanceof WebViewFragment)
        {
            itemId = 0;
            displayView(PositionMain);
        }
        else if (fr instanceof DictionaryFragment)
        {
            displayView(FRAGMENT_HOME);
        }
        else if (fr instanceof HomeFragment)
        {
           // super.onBackPressed();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            toolbar.setVisibility(View.GONE);
            return true;
        }

        //
		/*switch (item.getItemId())
        {
        case R.id.action_set1:
            /*Показываем список всех приложений*/
      /*      Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(context.getString(R.string.play_google_my_apps)));
            startActivity(intent);
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}*/
        return true;
    }

    //
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //
	//	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerMenu);
	//	menu.findItem(R.id.action_set1).setVisible(!drawerOpen);
       // return super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onLowMemory(){

        //clear all memory cached images when system is in low memory
        //note that you can configure the max image cache count, see CONFIGURATION
        BitmapAjaxCallback.clearCache();
    }

    //
    public ArrayList<NavDrawerItem> ListMenuIcons()
    {

        navDrawerItems = new ArrayList<NavDrawerItem>();

        //меню
        navMenuTitles = getResources().getStringArray(R.array.nav_animals_home_items);
        //Иконки для меню
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_dict_animals_home_icons);

       for(int i=0;i<navMenuTitles.length;i++)
       {
             navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],  navMenuIcons.getResourceId(i, -1)));
       }

        // Recycle the typed array
        navMenuIcons.recycle();
        return navDrawerItems;
    }


    //
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
        {
            itemId = 0;
            PositionMain = (position+1);
			// Выводим необходимый фрагмент на экран по выбору из меню
			displayView(PositionMain);
		}
	}

    public void displayView(int position)
    {
        toolbar.setVisibility(View.VISIBLE);
        setActionBarNoBgColor();
        //
        PositionMain = position;
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        switch (position)
        {


            case ANIMAL_DOG://Собаки
                imgMenu.setImageResource(R.drawable.dog_mini);
                fragment = getContent(position, CATALOG_ID_DOG,1);
                break;
            case ANIMAL_CAT://Кошки
                imgMenu.setImageResource(R.drawable.cat_mini);
                fragment = getContent(position, CATALOG_ID_CAT,1);
                break;
            case ANIMAL_RABBIT://Грызуны
                imgMenu.setImageResource(R.drawable.more_pig);
                fragment = getContent(position, CATALOG_ID_RABBIT,1);
                break;
           /* case ANIMAL_COW://Коровы
                imgMenu.setImageResource(R.drawable.cow_mini);
                fragment = getContent(position, CATALOG_ID_COW,1);
                break;*/
            case ANIMAL_HORSE://Лошади
                imgMenu.setImageResource(R.drawable.horse_mini);
                fragment = getContent(position, CATALOG_ID_HORSE,1);
                break;
            case ANIMAL_PIG://Свиньи
                imgMenu.setImageResource(R.drawable.pig_mini);
                fragment = getContent(position, CATALOG_ID_PIG,1);
                break;
            case ANIMAL_BIRDS://Птицы
                imgMenu.setImageResource(R.drawable.birds_mini);
                fragment = getContent(position, CATALOG_ID_BIRDS,1);//Параметры: 1-Текущий фрагмент,2-тип выбора данных,2-По какому url обращаться
                break;
            default:
                imgMenu.setImageResource(R.drawable.home_animals_mini);
                bundle.putBoolean(HOME_BUNDLE,true);
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
                break;
        }


        if (fragment != null)
        {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            if(position!=FRAGMENT_HOME)
            {
                position =  (position-1);
                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
            }
           else
            {
                setTitle(getString(R.string.title_animal_home));
            }
            mDrawerLayout.closeDrawer(mDrawerMenu);
        }
    }

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
        toolbar.setTitle(mTitle);
	}

	//
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
    {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


    //
    public Fragment getContent(int position, int type,int type_url)
    {


        String url ="";
        switch (type_url)
        {
            case 1:
                url = getString(R.string.url_api) +getString(R.string.url_api_get_dictionary);
                break;
            case 2:
                url = getString(R.string.url_api) +getString(R.string.url_api_get_guide);
                break;
        }

        Fragment  fragment = new DictionaryFragment();
        Bundle bund = new Bundle();
        bund.putBoolean(HOME_BUNDLE, true);
        if(itemId==0)
        {

            bund.putInt("_position",position);
            bund.putInt("_type", type);
            bund.putString("_url", url);
        }
        else
        {
            setActionBarBgColor();
           // url_web = url+"?contentID="+itemId;
            fragment = new WebViewFragment();
        }

        fragment.setArguments(bund);

        return fragment;
    }



    //
    private  void getAlertError(String text)
    {
        AlertShow.setDialogAlert(context, getString(R.string.alert),
                text,
                false,
                true,
                "",
                getString(R.string.close)
        );
    }

    //
    public void setActionBarBgColor()
    {
        int resIdColor = ContextCompat.getColor(context,R.color.apptheme_primary);
        switch (PositionMain)
        {
            case 0:
                resIdColor = ContextCompat.getColor(context,R.color.apptheme_primary);
                break;
            case 1:
                resIdColor = ContextCompat.getColor(context,R.color.color_birds);
                break;
            case 2:
                resIdColor = ContextCompat.getColor(context,R.color.color_parn);
                break;
            case 3:
                resIdColor = ContextCompat.getColor(context,R.color.color_no_parn);
                break;
            case 4:
                resIdColor = ContextCompat.getColor(context,R.color.color_mous);
                break;
            case 5:
                resIdColor = ContextCompat.getColor(context,R.color.color_predators);
                break;
            case 6:
                resIdColor = ContextCompat.getColor(context,R.color.color_rept);
                break;
            case 7:
                resIdColor = ContextCompat.getColor(context,R.color.apptheme_primary);
                break;
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            toolbar.setBackground(new ColorDrawable(resIdColor));
        }
        else
        {
            toolbar.setBackgroundDrawable(new ColorDrawable(resIdColor));
        }
    }

    //
    public void setActionBarNoBgColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            toolbar.setBackground(new ColorDrawable(Color.parseColor("#33000000")));
        }
        else
        {
            toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        }
    }

}
