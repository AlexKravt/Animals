package p4.guide_animals.Dialogs;

import android.content.Context;

public class AlertShow {

    public static void setDialogAlert(final Context context, String title, String text,
                                      Boolean buttonpositiv,Boolean buttonnegativ,String BtnPName,String BtnNName){


        DialogAlert.OnSelectedListener OpenFileView  =  new DialogAlert.OnSelectedListener() {
            @Override
            public void onSelected() {}
        };

        DialogAlert screenDialog = new DialogAlert(context,OpenFileView,title,text,buttonpositiv,buttonnegativ,BtnPName,BtnNName,0);
        screenDialog.setCanceledOnTouchOutside(false);
        screenDialog.show();
    }



}
