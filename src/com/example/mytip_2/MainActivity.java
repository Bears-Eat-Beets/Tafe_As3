package com.example.mytip_2;

import java.text.NumberFormat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
implements OnEditorActionListener, OnClickListener {

	/* Variables for Widgets */
	private EditText billAmountEditText;
	private TextView percentTextView;
	private Button percentUpButton;
	private Button percentDownButton;
	private TextView tipTextView;
	private TextView totalTextView;

	/* Define the SharedPreferences object */
	private SharedPreferences savedValues;
	
	/* Define instance variables that should be saved */
	private String billAmountString = "";
	private float tipPercent = .15f;
	
	/* Program */
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* Get the references to the widgets */
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentUpButton = (Button) findViewById(R.id.percentUpButton);
        percentDownButton = (Button) findViewById(R.id.percentDownButton);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        
        /* Set the listeners */
        billAmountEditText.setOnEditorActionListener(this);
        percentUpButton.setOnClickListener(this);
        percentDownButton.setOnClickListener(this);
        
        /* Get SharedPreferences object */
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }
	
	@Override
	public void onPause() {
		/* Save the instance variables */
		Editor editor = savedValues.edit();
		editor.putString("billAmouontString", billAmountString);
		editor.putFloat("tipPercent", tipPercent);
		editor.commit();
		
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		/* Get the instance variables */
		billAmountString = savedValues.getString("billamountString", "");
		tipPercent = savedValues.getFloat("tipPercent", 0.15f);
		
		/* Set the bill amount on its widget */
		billAmountEditText.setText(billAmountString);
		
		/* Calculate and display */
		calculateAndDisplay();		
	}
	
	public void calculateAndDisplay() {
		/* Get the bill amount */
		billAmountString = billAmountEditText.getText().toString();
		float billAmount;
		if (billAmountString.equals("")) {
			billAmount = 0;
		}
		else {
			billAmount = Float.parseFloat(billAmountString);
		}
		
		/* Calculate Tip and Total */
		float tipAmount = billAmount * tipPercent;
		float totalAmount = billAmount + tipAmount;
		
		/* Display the other results with formatting */
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		tipTextView.setText(currency.format(tipAmount));
		totalTextView.setText(currency.format(totalAmount));
		
		NumberFormat percent = NumberFormat.getPercentInstance();
		percentTextView.setText(percent.format(tipPercent));
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE ||
			actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
			calculateAndDisplay();
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.percentDownButton:
			tipPercent = tipPercent - .01f;
			calculateAndDisplay();
			break;
		case R.id.percentUpButton:
			tipPercent = tipPercent + .01f;
			calculateAndDisplay();
			break;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
