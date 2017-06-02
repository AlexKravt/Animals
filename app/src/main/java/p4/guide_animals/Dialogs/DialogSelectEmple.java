package p4.guide_animals.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Kravtsov.a on 19.01.2015.
 */
public class DialogSelectEmple {
    private static final int IDD_THREE_BUTTONS = 0 ;
    private static final int IDD_LIST = 1;
    private static final int IDD_RADIO = 2 ;
    private static final int IDD_CHECK = 3 ;
    private final OnSelectedListener onSelectedListener;
    final Context context;
    public DialogSelectEmple(OnSelectedListener onSelectedListener, Context getContext){
        this.onSelectedListener = onSelectedListener;
        context = getContext;
    }

  public AlertDialog getDialog(int id, String title, String[] SelectTitle,boolean[] check)
  {
      final String[] arrTitle = SelectTitle;
      final boolean[] mCheckedItems = check;
      switch (id) {
          case IDD_THREE_BUTTONS:
              AlertDialog.Builder builder = new AlertDialog.Builder(context);
              builder.setMessage(title)
                      .setCancelable(false)
                      .setPositiveButton("Да",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog,
                                                      int id) {
                                       /* Реализация действия по клику кнопки */
                                      dialog.cancel();
                                  }
                              })
                      .setNeutralButton("Незнаю",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog,
                                                      int id) {
                                       /* Реализация действия по клику кнопки */
                                      dialog.cancel();
                                  }
                              })
                      .setNegativeButton("Нет",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog,
                                                      int id) {
                                       /* Реализация действия по клику кнопки */
                                      dialog.cancel();
                                  }
                              });

              return builder.create();

          case IDD_LIST:

              builder = new AlertDialog.Builder(context);
              builder.setTitle(title); // заголовок для диалога

              builder.setItems(arrTitle, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int item) {


                      /* Реализация действия по выбору из списка */

                      Toast.makeText(context, "Выбранный заголовок: " + arrTitle[item], Toast.LENGTH_SHORT).show();
                  }
              });
              builder.setCancelable(false);
              return builder.create();

          case IDD_RADIO:
              builder = new AlertDialog.Builder(context);
              builder.setTitle(title)
                      .setCancelable(false)
                              // добавляем одну кнопку для закрытия диалога
                      .setNeutralButton("Назад",
                              new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog,
                                                      int id) {
                                       /* Реализация действия по клику кнопки */
                                      dialog.cancel();
                                  }
                              })
                              // добавляем переключатели
                      .setSingleChoiceItems(arrTitle, -1,
                              new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog,
                                                      int item) {
                                       /* Реализация действия по клику кнопки */
                                      Toast.makeText(context, "Заголовок: " + arrTitle[item],
                                              Toast.LENGTH_SHORT).show();
                                  }
                              });
              return builder.create();

          case IDD_CHECK:



              builder = new AlertDialog.Builder(context);
              builder.setTitle(title)
                      .setCancelable(false)
                      .setMultiChoiceItems(arrTitle, mCheckedItems,
                              new DialogInterface.OnMultiChoiceClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog,
                                                      int which, boolean isChecked) {
                                      mCheckedItems[which] = isChecked;
                                  }
                              })

                              // Добавляем кнопки
                      .setPositiveButton("Сохранить",new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int id)
                          {
                              onSelectedListener.onSelected(id, mCheckedItems,arrTitle);

                             /* StringBuilder state = new StringBuilder();
                              for (int i = 0; i < arrTitle.length; i++) {
                                  state.append(arrTitle[i]);
                                           /* Реализация действия по клику кнопки */
                                /*  if (mCheckedItems[i]) {
                                      state.append(" выбран\n");
                                  } else {
                                      state.append(" не выбран\n");
                                  }

                              }
                              Toast.makeText(context, state.toString(), Toast.LENGTH_LONG).show();*/
                          }
                      })

                      .setNegativeButton("Закрыть",
                              new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog,
                                                      int id) {
                                      dialog.cancel();

                                  }
                              });
              return builder.create();

          default:
              return null;
      }
  }

    public interface OnSelectedListener {
        public void onSelected(int id, boolean[] mCheckedItems, String[] arrStringTitle);
    }
}
