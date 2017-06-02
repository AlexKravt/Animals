package p4.guide_animals.Libraries;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import p4.guide_animals.R;


/**
 * Created by Kravtsov.a on 03.02.2015.
 */
public class LibView {

    public LibView(){}


    /*******************************************************************/
    public Spinner getSpinnerCursor(View rootFrame,int IdSpinner, String[] column_name, Cursor cursor,int SelectionId)
    {

        Context context = rootFrame.getContext();
        // Настраиваем адаптер
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1,
                cursor,
                column_name,
                new int[] { android.R.id.text1 },0);
        //
        final Spinner listSpinner = (Spinner) rootFrame.findViewById(IdSpinner);
        listSpinner.setAdapter(adapter);
        listSpinner.setSelection(SelectionId);
        return listSpinner;
    }
}
