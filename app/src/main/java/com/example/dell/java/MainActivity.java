package com.example.dell.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void increment(View view) {
        if(num>=100)
            Toast.makeText(this,"Enough Coffee For Today!", Toast.LENGTH_SHORT).show();

        else {
            num=++num;
            displayQuantity(num);
        }

    }

    public void decrement(View view) {
        if(num<=0)
            Toast.makeText(this,"Stay Awake, Order Some Coffee!", Toast.LENGTH_SHORT).show();
        else
            num=--num;
        displayQuantity(num);
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate){
        int basePrice=70;

        if(addWhippedCream)
            basePrice+=20;
        if(addChocolate)
            basePrice+=30;

        return num*basePrice;
    }
    public void submitOrder(View view) {
        CheckBox wcBox = (CheckBox) findViewById(R.id.wc_checkBox);
        boolean wcBoolean = wcBox.isChecked();

        CheckBox chocoBox = (CheckBox) findViewById(R.id.choco_checkBox);
        boolean chocoBoolean = chocoBox.isChecked();

        EditText editText = (EditText) findViewById(R.id.edit_text);
        String text = editText.getText().toString();

        int price = calculatePrice(wcBoolean,chocoBoolean);
        String sum = createOrderSummary(text,price,num,wcBoolean,chocoBoolean);
        displayMessage(sum);

        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER);
            intent.putExtra(AlarmClock.EXTRA_MESSAGE, "->Collect Your Order");
            intent.putExtra(AlarmClock.EXTRA_LENGTH, num*30);
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void submitOrderPay(View view) {
        CheckBox wcBox = (CheckBox) findViewById(R.id.wc_checkBox);
        boolean wcBoolean = wcBox.isChecked();

        CheckBox chocoBox = (CheckBox) findViewById(R.id.choco_checkBox);
        boolean chocoBoolean = chocoBox.isChecked();

        EditText editText = (EditText) findViewById(R.id.edit_text);
        String text = editText.getText().toString();

        int price = calculatePrice(wcBoolean,chocoBoolean);
        String sum = createOrderSummary(text,price,num,wcBoolean,chocoBoolean);
        displayMessage(sum);

        Uri webpage = Uri.parse("http://www.paytm.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public String createOrderSummary(String n,int p,int q,boolean wc,boolean choco){

        String summary = getString(R.string.name)+" :"+n;
        summary+= "\n" + getString(R.string.quantity)+" :" +q;
        if(wc==true)
            summary+= "\nWhipped Cream: Yes";
        else summary+= "\nWhipped Cream: No";
        if(choco==true)
            summary+= "\nChocolate: Yes";
        else summary+= "\nChocolate: No";
        summary+= "\n" + getString(R.string.total)+": ₹"+p;
        summary+= "\n" + getString(R.string.thank_you);
        return summary;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int num) {
        TextView quantityTextView = (TextView) findViewById(R.id.Quantity);
        quantityTextView.setText("" + num);
    }
    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary);
        orderSummaryTextView.setText(message);
    }
}