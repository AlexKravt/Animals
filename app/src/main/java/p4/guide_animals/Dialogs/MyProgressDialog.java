package p4.guide_animals.Dialogs;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 06.03.2015.
 */
public class MyProgressDialog extends ProgressDialog {

    Context context;

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context =context;
        this.setIcon(R.mipmap.ic_launcher);
        this.setTitle(context.getString(R.string.alert));
    }


    public MyProgressDialog(Context context) {
        super(context);
        this.context =context;
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Resources res = getContext().getResources();
            final int color = res.getColor(Color.GRAY);

            int versionId = Build.VERSION.SDK_INT;
            if(versionId>=17)
            setParamsDialog(res);

            // Icon
            final int iconId = res.getIdentifier("icon", "id", "android");
            final View iconImg = findViewById(iconId);
            if (iconImg != null) {
                LinearLayout.LayoutParams imgParams = new  LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT);
                ImageView iconView = ((ImageView) iconImg);
                iconView.setLayoutParams(imgParams);
            }
            // Title
            final int titleId = res.getIdentifier("alertTitle", "id", "android");
            final View title = findViewById(titleId);
            if (title != null) {
                TextView titleTxt = ((TextView) title);
                //noinspection ResourceType
                titleTxt.setTextColor(res.getColor(R.color.white));
                titleTxt.setTextSize(18);
            }

            // Title divider
            final int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
            final View titleDivider = findViewById(titleDividerId);
            if (titleDivider != null) {
                titleDivider.setBackgroundColor(color);
            }

            // Content
           /* final int contentId = res.getIdentifier("content", "id", "android");
            final View content = findViewById(contentId);
            if (content != null) {
                content.setBackgroundColor(yellow);
            }*/


            // Message
            final int messageId = res.getIdentifier("message", "id", "android");
            final View message =  findViewById(messageId);
            if (message != null) {
                TextView textmessage = ((TextView) message);
                textmessage.setTextSize(14);
            }
        }

   @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
   private void setParamsDialog(Resources res)
   {
       // ContanerTitle
       final int contTitleId = res.getIdentifier("title_template", "id", "android");
       final View contTitle = findViewById(contTitleId);

           if (contTitle != null)
           {
                   LinearLayout blockTitle =  ((LinearLayout) contTitle);
                   LinearLayout.LayoutParams blockTitleParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                   blockTitleParams.setMarginEnd(0);
                   blockTitleParams.setMarginStart(0);
                   blockTitle.setPadding(10,0,10,0);
                   blockTitle.setBackgroundColor(context.getResources().getColor(R.color.alert_bg));
                   blockTitle.setLayoutParams(blockTitleParams);
           }

   }
   }
