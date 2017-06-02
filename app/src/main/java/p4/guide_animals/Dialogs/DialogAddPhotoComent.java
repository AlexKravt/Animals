package p4.guide_animals.Dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import p4.guide_animals.R;


public class DialogAddPhotoComent extends AlertDialog {
    private ImageView logo;
    private TextView tvAlert1,tvAlert2;
    private EditText editText;
    private LinearLayout llroot, child1, child2;
    private  boolean email;
    private Context cont;
    private final OnSelectedListener onSelectedListener;

    public DialogAddPhotoComent(Context context, OnSelectedListener onSelectedListener,
                                String Title, String Description,Boolean addEmail, Boolean BtnP, Boolean BtnN, String BtnPName, String BtnNName) {
        super(context);
        this.cont =context;
        this.email =addEmail;
        this.onSelectedListener = onSelectedListener;

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
       /*------------------------------------------*/
        llroot = new LinearLayout(context);
        child1 = new LinearLayout(context);
        child2 = new LinearLayout(context);
        /*---------------TextView1---------------------------*/
        logo = new ImageView(context);
        logo.setImageResource(R.mipmap.ic_flag);
        LayoutParams lpimg = new LayoutParams(LayoutParams.WRAP_CONTENT,40);
        lpimg.setMargins(10, 0, 0, 0);

        /*---------------TextView1---------------------------*/
        tvAlert1 = new TextView(context);
        tvAlert1.setText(Title);
        tvAlert1.setTextSize(16);
        tvAlert1.setPadding(0,20,20,20);
        tvAlert1.setTypeface(null, Typeface.BOLD);
        /*------------------------------------------*/

        LayoutParams lptxt = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lptxt.setMargins(10, 10, 10, 10);

        /*---------------TextView2---------------------------*/

        tvAlert2 = new TextView(context);
        tvAlert2.setText(Description);
        LayoutParams lptxt1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        /*-----------------------------------------------------*/

        child1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0f));
        child1.setOrientation(LinearLayout.HORIZONTAL);

        child1.setBackgroundResource(R.drawable.dialog_title_style);

        child1.setGravity(Gravity.CENTER);
        child1.addView(logo, lpimg);
        child1.addView(tvAlert1, lptxt);

        /*-----------------------------------------------------*/
        child2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
        child2.setOrientation(LinearLayout.VERTICAL);
        child2.setPadding(20, 20, 20, 20);
        child2.setGravity(Gravity.CENTER);
        child2.addView(tvAlert2, lptxt1);
        /*-------------------------------------------------------*/
        if(addEmail)
        {
          editText = new EditText(context);
          editText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
          editText.setTextSize(14);
          editText.setInputType(InputType.TYPE_MASK_CLASS);
          editText.setHint("E-mail");
          child2.addView(editText);
        }
        /*-----------------------------------------------------*/
        llroot.setOrientation(LinearLayout.VERTICAL);
        llroot.addView(child1);
        llroot.addView(child2);

        relativeLayout.addView(llroot, layoutParams);

        if(BtnP)
           setButton(BUTTON_POSITIVE, BtnPName, onClickListener);
        if(BtnN)
           setButton(BUTTON_NEGATIVE, BtnNName, onClickListener);

        setView(relativeLayout);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case BUTTON_POSITIVE:
                    if(email)
                    {
                        String edText = editText.getText().toString();
                        if(!edText.equals(""))
                        {
                            onSelectedListener.onSelected(edText);
                        }
                        else
                        {
                            Toast.makeText(cont, "Введите E-mail пожалуйста!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        onSelectedListener.onSelected("email_no");
                    }
                    break;
                case BUTTON_NEGATIVE:
                    onSelectedListener.onSelected("remove_point");
                    dialog.dismiss();
                    break;
            }
        }
    };


    public interface OnSelectedListener {
        public void onSelected(String email);
    }

}
