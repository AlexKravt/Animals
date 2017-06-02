package p4.guide_animals;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.regex.Pattern;


import cn.pedant.sweetalert.SweetAlertDialog;
import p4.guide_animals.Adapters.MenuListAdapter;
import p4.guide_animals.Dialogs.AlertShow;
import p4.guide_animals.Dialogs.DialogSelect;
import p4.guide_animals.Fragments.DictionaryFragment;
import p4.guide_animals.Fragments.HomeFragment;
import p4.guide_animals.Fragments.WebViewFragment;
import p4.guide_animals.Halper.Halpe;
import p4.guide_animals.Services.MyService;
import p4.guide_animals.Services.ServerServices;
import p4.guide_animals.Utils.GlobalConstant;
import p4.guide_animals.model.DB;
import p4.guide_animals.model.NavDrawerItem;
import p4.guide_animals.model.TableBase;


public class MainActivity extends AppCompatActivity {

    /**/
    private Toolbar toolbar;
    public static int itemId = 0;
    public static int positionMain = 0;
    public final int FRAGMENT_HOME = 0;
    public final static int ANIMAL_BIRDS = 1;
    public final static int ANIMAL_OLEN = 2;
    public final static int ANIMAL_HORSE = 3;
    public final static int ANIMAL_RABBIT = 4;
    public final static int ANIMAL_PREADATOR = 5;
    public final static int ANIMAL_REPTIL = 6;
    public final static int ANIMAL_HOME = 7;
    int countBackPress = 0;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    /**/
    public SharedPreferences mSettings;
    public static final String APP_PREFERENCES_TABLE = "table_creat";
    public static final String APP_PREFERENCES_COUNTER = "count_stars_back";
    public static final String USER_EMAIL = "user_email";
    public static final String APP_PREFERENCES="settings";
    public static final String NAME_NOTIFICATION = "notif_check";
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerMenu;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public String[] userSession;

    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private TableBase tableBase;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private MenuListAdapter adapter;
    private Context context;
    private Halpe helper;
    private AQuery AQ;
    private ImageView imgMenu;
    private View viewBox;


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

        context = MainActivity.this;
        AQ = new AQuery(context);
        viewBox = findViewById(R.id.coordinat_view);
        //Скрываем клавиатуру при просмотре фрагмента, если есть поля для ввода
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //Получаем файл настроек
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        helper = new Halpe();
       /* tableBase = new TableBase(context);
        userSession = tableBase.getUserIdSession();
        //
        if (userSession != null) {
            //Регистрация пользователя для уведомлений
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (!sentToken)
                getTokenRegistration();
        }*/

        // getTokenRegistration();
       /* new TableBase(context).delTableSession();
        new TableBase(context).delTable();
        creatTable();*/



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


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                //  R.drawable.apptheme_ic_navigation_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
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

        //getEmailUserToSave();

        //Если записи нет то создаём необходимые SQL таблицы и заполняем данными
        if (!mSettings.contains(APP_PREFERENCES_TABLE)) {
            creatTable();
            //
            startUpdateService();

        }

        if (savedInstanceState == null) {

            //Выводим нужный нам фрагмент если не задан то выводится по умалчанию
            positionMain = getIntent().getIntExtra("fragmentMainId", 0);
            itemId = getIntent().getIntExtra("itemId", 0);
            displayView(positionMain);
        }

    }

    private void getEmailUserToSave()
    {

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(!mSettings.contains(USER_EMAIL))
        {
           // API level 8+
           Account[] accounts = AccountManager.get(context).getAccounts();
             for (Account account : accounts) {
                 if (emailPattern.matcher(account.name).matches()) {
                   //Получение e-mail пользователя для привязки оплаты
                     SharedPreferences.Editor editor = mSettings.edit();
                     editor.putString(USER_EMAIL,account.name);
                     editor.apply();
                 }
             }
        }

        getDialogAlert();
    }


    private void getDialogAlert()
    {
        if(mSettings.contains(USER_EMAIL))
        {
           final String userEmail = mSettings.getString(USER_EMAIL,"abracadabra");
           SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
           dialog.setTitleText(getString(R.string.alert));
           dialog.setContentText(String.format(getString(R.string.alert_set_email),userEmail));
           dialog.setConfirmText(getString(R.string.btn_status_go));
           dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();

                    ServerServices server =  new ServerServices(context,AQ);
                    server.getStatusPayment(userEmail);
                    Toast.makeText(context,"Получение статуса оплаты!",Toast.LENGTH_LONG).show();
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
        else
        {
            Toast.makeText(context,"Ваши учётные данные не определены!",Toast.LENGTH_LONG).show();
        }
    }


    //
    private void startUpdateService()
    {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void getTokenRegistration()
    {

        mRegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {

                SharedPreferences sharedPreferences =   PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken)
                {
                    return;
                }
            }
        };

        if (checkPlayServices())
        {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    //
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getFragmentManager();
        Fragment fr = fm.findFragmentById(R.id.frame_container);

        if (fr instanceof WebViewFragment)
        {
            displayView(positionMain);
        }
        else if (fr instanceof DictionaryFragment)
        {
            displayView(FRAGMENT_HOME);
        }
        else if (fr instanceof HomeFragment && countBackPress==0)
        {
            countBackPress++;
            getStarsDialog();
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            finish();
        }
    }

    //
    public void getStarsDialog()
    {

        String ok = getString(R.string.ok);
        String no = getString(R.string.no);


        if (!mSettings.contains(APP_PREFERENCES_COUNTER))
        {

            setDialogSelect(context,
                    getString(R.string.liked_app),
                    getString(R.string.your_feedback),
                    true,
                    true,
                    getString(R.string.leavereview),
                    no
            );

        }
        else
        {

            setDialogSelect(context,
                    getString(R.string.exit),
                    getString(R.string.yes_exit),
                    true,
                    true,
                    ok,
                    no);

        }
    }

    //
    public void setDialogSelect(final Context context, String title, String text, Boolean buttonpositiv, Boolean buttonnegativ, String BtnPName, String BtnNName) {

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(text)
                .setConfirmText(BtnPName)
                .setCancelText(BtnNName)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        if (!mSettings.contains(APP_PREFERENCES_COUNTER)) {
                            //Создаём файлик с параметром если пользователь нажал да.
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putInt(APP_PREFERENCES_COUNTER, 1);
                            editor.apply();

                            final String appName = context.getString(R.string.app_name_package);
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.market) + appName)));
                            } catch (android.content.ActivityNotFoundException anfe)
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.play_google) + appName)));
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                        if (!mSettings.contains(APP_PREFERENCES_COUNTER)) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                        countBackPress = 0;
                    }
                })
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
		switch (item.getItemId())
        {
            case R.id.action_set:
            /*Принудительное обновление*/
                if(isDetectPermissions(GlobalConstant.WRITE_FILE_REQUEST_CODE))
                {
                  startUpdateService();
                  Toast.makeText(context,"Обновляется!",Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_set0:
                /*Получение статуса оплаты*/
                if(isDetectPermissions(GlobalConstant.GET_ACCOUNTS_REQUEST_CODE))
                {
                    getEmailUserToSave();
                }

                return true;
            case R.id.action_set1:
            /*Показываем список всех приложений*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(context.getString(R.string.play_google_my_apps)));
            startActivity(intent);
            return true;
            case R.id.action_set2:
                /*Обратная связь*/
                sendEmailUser(getString(R.string.admin_email));
                return true;
            case R.id.action_set3:
                //Политика конфидициальности
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(context.getString(R.string.url_politika)));
                startActivity(intent);
                return true;
		default:
			return super.onOptionsItemSelected(item);
		}

    }

    //
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerMenu);
		menu.findItem(R.id.action_set).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onLowMemory(){

        //clear all memory cached images when system is in low memory
        //note that you can configure the max image cache count, see CONFIGURATION
        BitmapAjaxCallback.clearCache();
    }

    public  void creatTable()
    {
        //Создаём таблицы и заполняем данными
        DB MYbase = new DB(context);
        SQLiteDatabase db = MYbase.SQLdb();
        MYbase.BaseAc.onCreate(db); //Создаём таблицы в базе данных
        db.close();
        //
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_TABLE, 1);
        editor.apply();
    }

    //
    public ArrayList<NavDrawerItem> ListMenuIcons()
    {

        navDrawerItems = new ArrayList<NavDrawerItem>();

        //меню
        navMenuTitles = getResources().getStringArray(R.array.nav_dict_items);
        //Иконки для меню
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_dict_icons);

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
            positionMain = position;
			// Выводим необходимый фрагмент на экран по выбору из меню
			displayView(position);
		}
	}

    public void displayView(int position)
    {
        toolbar.setVisibility(View.VISIBLE);
        setActionBarNoBgColor();


        if(position!=FRAGMENT_HOME)
        {
            if(!isDetectPermissions(GlobalConstant.GET_ACCOUNTS_REQUEST_CODE))
                position = FRAGMENT_HOME;
            if(!isDetectPermissions(GlobalConstant.WRITE_FILE_REQUEST_CODE))
                position = FRAGMENT_HOME;
        }
        //
        positionMain = position;
        Fragment fragment = null;



        switch (position)
        {
            case ANIMAL_BIRDS://Птицы
                imgMenu.setImageResource(R.drawable.birds_mini);
                fragment = getContent(position, ANIMAL_BIRDS,1);//Параметры: 1-Текущий фрагмент,2-тип выбора данных,2-По какому url обращаться
                break;
            case ANIMAL_OLEN://Парнокопытные
                imgMenu.setImageResource(R.drawable.oleny_mini);
                fragment = getContent(position, ANIMAL_OLEN,1);
                break;
            case ANIMAL_HORSE://Не парнокопотные
                imgMenu.setImageResource(R.drawable.horse_mini);
                fragment = getContent(position, ANIMAL_HORSE,1);
                break;
            case ANIMAL_RABBIT://Грызуны
                imgMenu.setImageResource(R.drawable.belka_mini);
                fragment = getContent(position, ANIMAL_RABBIT,1);
                break;
            case ANIMAL_PREADATOR://Хишники
                imgMenu.setImageResource(R.drawable.preadator_mini);
                fragment = getContent(position, ANIMAL_PREADATOR,1);
                break;
            case ANIMAL_REPTIL://Рептилии
                imgMenu.setImageResource(R.drawable.reptilii_mini);
                fragment = getContent(position, ANIMAL_REPTIL,1);
                break;
            case ANIMAL_HOME://Домашние животные
                Intent intent = new Intent(context, DictionaryActivity.class);
                startActivity(intent);
                break;
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }


        if (fragment != null)
        {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            if(position!=FRAGMENT_HOME)
            {
                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);

            }
            setTitle(navMenuTitles[position]);
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
        if(itemId==0)
        {
            Bundle bund = new Bundle();
            bund.putInt("_position",position);
            bund.putInt("_type", type);
            bund.putString("_url", url);
            fragment.setArguments(bund);
        }
        else
        {
            countBackPress = 0;
            setActionBarBgColor();
           // url_web = url+"?contentID="+itemId;
            fragment = new WebViewFragment();
        }

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
        switch (positionMain)
        {
            case 0:
                resIdColor = ContextCompat.getColor(context, R.color.apptheme_primary);
                break;
            case 1:
                resIdColor = ContextCompat.getColor(context, R.color.color_birds);
                break;
            case 2:
                resIdColor = ContextCompat.getColor(context, R.color.color_parn);
                break;
            case 3:
                resIdColor = ContextCompat.getColor(context, R.color.color_no_parn);
                break;
            case 4:
                resIdColor = ContextCompat.getColor(context, R.color.color_mous);
                break;
            case 5:
                resIdColor = ContextCompat.getColor(context, R.color.color_predators);
                break;
            case 6:
                resIdColor = ContextCompat.getColor(context, R.color.color_rept);
                break;
            case 7:
                resIdColor = ContextCompat.getColor(context, R.color.apptheme_primary);
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

    /*********************************/
    //Методы проверки доступности необходимых разрешений
    /**
     * *****************************
     */
    private void sendEmailUser(String Email)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("message/*");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.tieleemail));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(shareIntent, getString(R.string.titleemailclient)));
    }

    public boolean isDetectPermissions(int key_permission)
    {
        switch (key_permission){
            case GlobalConstant.MY_LOCATION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        actionAlertSnackbar(GlobalConstant.MY_LOCATION_REQUEST_CODE,"Для правильной работы, нужны Ваши координаты!",false);
                    }
                    else {
                        onStartRequestPermissions(GlobalConstant.MY_LOCATION_REQUEST_CODE);
                    }
                    return false;
                }
                break;
            case GlobalConstant.GET_ACCOUNTS_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.GET_ACCOUNTS))
                    {
                        actionAlertSnackbar(GlobalConstant.GET_ACCOUNTS_REQUEST_CODE,"Для регистрации, нужны Ваши контакты!",false);
                    }
                    else {
                        onStartRequestPermissions(GlobalConstant.GET_ACCOUNTS_REQUEST_CODE);
                    }
                    return false;
                }
                break;
            case GlobalConstant.WRITE_FILE_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    {
                        actionAlertSnackbar(GlobalConstant.WRITE_FILE_REQUEST_CODE,"Для правильной работы, нужен доступ к памяти!",false);
                    }
                    else {
                        onStartRequestPermissions(GlobalConstant.WRITE_FILE_REQUEST_CODE);
                    }

                    return false;
                }
                break;
        }

        return true;
    }

    private void onStartRequestPermissions(int requestCode){
        switch (requestCode){
            case GlobalConstant.MY_LOCATION_REQUEST_CODE:
                // Request permission.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        GlobalConstant.MY_LOCATION_REQUEST_CODE);
                break;

            case GlobalConstant.GET_ACCOUNTS_REQUEST_CODE:
                // Request permission.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.GET_ACCOUNTS},
                        GlobalConstant.GET_ACCOUNTS_REQUEST_CODE);
                break;
            case GlobalConstant.WRITE_FILE_REQUEST_CODE:
                // Request permission.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        GlobalConstant.WRITE_FILE_REQUEST_CODE);
                break;
            case GlobalConstant.CAMERA_REQUEST_CODE:
                // Request permission.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA},
                        GlobalConstant.CAMERA_REQUEST_CODE);
                break;
        }
    }

    private void actionAlertSnackbar(final int requestCode,final String message,boolean isSettings)
    {
        if(!isSettings)
            if(viewBox!=null)
                Snackbar.make(viewBox, message, Snackbar.LENGTH_LONG)
                        .setAction("Получить", new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                onStartRequestPermissions(requestCode);
                            }
                        })
                        .setDuration(5000) // 5 секунд
                        .show();

        if(isSettings)
            if(viewBox!=null)
                Snackbar.make(viewBox, "Для выполнения действия, нет нужных прав!" , Snackbar.LENGTH_LONG)
                        .setAction("Настройки", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openApplicationSettings(requestCode);

                                Toast.makeText(getApplicationContext(),
                                        "Включите пожалуйста, необходимые разрешения!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setDuration(5000) // 5 секунд
                        .show();
    }

    public void openApplicationSettings(final int requestCode) {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent,requestCode);
    }

    public void isNeverAskAgain(int requestCode)
    {
        switch (requestCode){
            case GlobalConstant.MY_LOCATION_REQUEST_CODE:
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    actionAlertSnackbar(GlobalConstant.MY_LOCATION_REQUEST_CODE,null,true);
                }
                break;
            case GlobalConstant.GET_ACCOUNTS_REQUEST_CODE:

                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.GET_ACCOUNTS))
                {
                    actionAlertSnackbar(GlobalConstant.GET_ACCOUNTS_REQUEST_CODE,null,true);
                }


                break;
            case GlobalConstant.WRITE_FILE_REQUEST_CODE:

                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    actionAlertSnackbar(GlobalConstant.WRITE_FILE_REQUEST_CODE,null,true);
                }



                break;
            case GlobalConstant.CAMERA_REQUEST_CODE:

                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA))
                {
                    actionAlertSnackbar(GlobalConstant.CAMERA_REQUEST_CODE,null,true);
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case GlobalConstant.MY_LOCATION_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals("android.permission.ACCESS_FINE_LOCATION")&&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    displayView(positionMain);
                }
                else
                {
                    isNeverAskAgain(requestCode);
                }
                break;
            case GlobalConstant.GET_ACCOUNTS_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals("android.permission.GET_ACCOUNTS")&&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    displayView(positionMain);
                }
                else
                {
                    isNeverAskAgain(requestCode);
                }
                break;
            case GlobalConstant.WRITE_FILE_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals("android.permission.WRITE_EXTERNAL_STORAGE") &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    displayView(positionMain);
                }
                else
                {
                    isNeverAskAgain(requestCode);
                }
                break;
            case GlobalConstant.CAMERA_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals("android.permission.CAMERA") &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    displayView(positionMain);
                }
                else
                {
                    isNeverAskAgain(requestCode);
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
