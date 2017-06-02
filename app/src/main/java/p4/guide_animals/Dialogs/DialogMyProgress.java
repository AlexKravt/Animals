package p4.guide_animals.Dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;

import p4.guide_animals.R;

public class DialogMyProgress extends AlertDialog
{
    AQuery AQ;
    String mtitle = null;
    String mtext = null;
    public DialogMyProgress(Context context)
    {
        super(context);

        this.setCanceledOnTouchOutside(false);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View tamplate = mInflater.inflate(R.layout.inflate_progress, null);

        AQ =new AQuery(tamplate);
        setView(tamplate);
    }

    public String setmTitle(String t)
    {
        mtitle = t;
        return mtitle;
    }

    public String setmText(String t)
    {
        mtext = t;
        return mtext;
    }

    @Override
    public void show()
    {
        AQ.id(R.id.textTitle).text(mtitle);
        AQ.id(R.id.textAlert).text(mtext);
        dismiss();
        super.show();
    }

    public interface OnSelectedListener
    {
        public void onSelected(long id);
    }

}
