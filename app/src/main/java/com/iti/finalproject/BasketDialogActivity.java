package com.iti.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class BasketDialogActivity extends AppCompatActivity {
    TextView description;
    TextView itemPrice;
    TextView itemSubTotal;
    ImageView decrement_btn;
    ImageView increment_btn;
    TextView quantity_tv;
    Button cancel;
    Button basket;
    int counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_dialog);

        description = (TextView) findViewById(R.id.description);
        itemPrice = (TextView) findViewById(R.id.itemprice);
        itemSubTotal = (TextView) findViewById(R.id.itemsuptotal);
        decrement_btn = (ImageView) findViewById(R.id.decrement_image);
        increment_btn = (ImageView) findViewById(R.id.increment_image);
        quantity_tv = (TextView) findViewById(R.id.quantity_tv);
        cancel = (Button) findViewById(R.id.cancel_btn);
        basket = (Button) findViewById(R.id.basket_btn);

        final Item item = getIntent().getParcelableExtra("ITEM_INTENT");
        setTitle(item.getName());
        description.setText(item.getDescription());
        itemPrice.setText(String.format(Locale.getDefault(), "%.2f LE", item.getPrice()));
        itemSubTotal.setText(String.format(Locale.getDefault(), "%.2f LE", item.getPrice()));
        quantity_tv.setText(String.valueOf(counter));
        decrement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 1)
                 counter--;
                quantity_tv.setText(String.valueOf(counter));
                itemSubTotal.setText(String.format(Locale.getDefault(), "%.2f LE", (item.getPrice() * (Integer.parseInt(quantity_tv.getText().toString())))));

            }
        });
        increment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < 20)
                    counter++;
                quantity_tv.setText(String.valueOf(counter));
                itemSubTotal.setText(String.format(Locale.getDefault(), "%.2f LE",(item.getPrice() * (Integer.parseInt(quantity_tv.getText().toString())))));

            }
        });
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RETURN_ITEM",item);
                returnIntent.putExtra("QUANTITY",Integer.parseInt(quantity_tv.getText().toString()));

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
            return false;
        return true;
    }
}
