package p4.guide_animals;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.yandex.money.api.methods.payment.params.P2pTransferParams;
import com.yandex.money.api.methods.payment.params.PaymentParams;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import p4.guide_animals.Services.ServerServices;
import p4.guide_animals.model.ApiData;
import ru.yandex.money.android.PaymentActivity;

public class PayActivity extends Activity {

    private static final int REQUEST_CODE = 101;

    public static final String AMOUNT = "p4.guide_animals.extra.AMOUNT";
    public static final String ZNORDER = "p4.guide_animals.extra.ZNORDER";
    public static final String ORDERID = "p4.guide_animals.extra.ORDERID";

    private int amount;
    private int  idOrder;
    private String num_zn;
    public Payment payment;
    public Status status;
    private Context context;
    private ApiData apiData;
    private AQuery AQ;
    private ServerServices server;
    private ActionBar actionBar;

    public void startP2P() {
        startPayment(Payment.P2P);
    }

    public void startPhone() {
        startPayment(Payment.PHONE);
    }

    public void startPay() {
        startPayment( Payment.ACCOUNTNUM);
    }

    private void startPayment(Payment payment) {
        this.payment = payment;
        pay();
    }

    public enum  Status{
        NOPAYMENT,
        PAYMENT}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        context = PayActivity.this;
        AQ = new AQuery(this);
        server = new ServerServices(context,AQ);
        actionBar = getActionBar();
        status = Status.NOPAYMENT;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent paramsPay = getIntent();
        if(paramsPay!=null)
        {
                idOrder =  paramsPay.getIntExtra("idOrder",0);
                num_zn = paramsPay.getStringExtra("num_zn");
                amount = paramsPay.getIntExtra("sum",0);
                NumberFormat numberFormat  = new DecimalFormat("#,###,###");
                String strFormat = numberFormat.format(amount);         //
                TextView valueAmount = (TextView) findViewById(R.id.valueAmount);
                valueAmount.setText(String.format("%s руб.",strFormat));
        }

        findButton(R.id.iBtnYaMoney).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(num_zn!=null)
                    startPay();
            }
        });

        findButton(R.id.iBtnCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_zn!=null)
                    startP2P();
            }
        });

        apiData = ApiData.getFromProperties(context);
    }


    @Override
    public void onBackPressed()
    {
        if(server.isPayment())
        {
            Intent intent =  new Intent(context, MainActivity.class);
            intent.putExtra("fragmentMainId", MainActivity.positionMain);
            startActivity(intent);
        }
        else
        {
            super.onBackPressed();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
           AQ.id(R.id.boxBtnPay).gone();
           AQ.id(R.id.boxAlert).visible();
            Spanned result;
            String alertSuccess = String.format(getString(R.string.pay_status_success), num_zn);
           // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
               // result = Html.fromHtml(alertSuccess,Html.FROM_HTML_MODE_LEGACY);
           // } else {
                result = Html.fromHtml(alertSuccess);
          //  }
            AQ.id(R.id.textAlert).text(result);

            AQ.id(R.id.btnSuccess).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            //Запись статуса оплаты и отправка данных на сервер
            server.sendStatusPayServer();
        }
    }



    private ImageButton findButton(int id) {
        return (ImageButton) findViewById(id);
    }


    private void pay() {
        if (num_zn!=null)
        {
            switch (payment) {
                case P2P:
                    startPaymentActivityForResult(new P2pTransferParams.Builder(AuthActivity.ACCOUNT_ID)
                            .setMessage(String.format(getString(R.string.pay_title)," к функциям приложения."))
                            .setAmount(getAmount())
                            .create());
                    break;
                case ACCOUNTNUM:
                    Intent intent = new Intent(context, AuthActivity.class);
                    intent.putExtra(AMOUNT, getAmount().toString());
                    intent.putExtra(ZNORDER, num_zn);
                    intent.putExtra(ORDERID, idOrder);
                    startActivity(intent);
                    finish();
                    break;
            }
        } else {
            Toast.makeText(context, R.string.activity_pay_toast, Toast.LENGTH_SHORT).show();
        }
    }


    private void startPaymentActivityForResult(PaymentParams paymentParams) {

        Intent intent = PaymentActivity.getBuilder(context)
                .setPaymentParams(paymentParams)
                .setClientId(apiData.clientId)
                .setHost(apiData.host)
                .build();
        startActivityForResult(intent, REQUEST_CODE);
    }

    private BigDecimal getAmount() {
        return new BigDecimal(amount);
    }

    public enum Payment {
        P2P,
        PHONE,
        ACCOUNTNUM
    }
}
