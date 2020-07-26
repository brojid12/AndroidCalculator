package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText result; //buat object yang bisa berubah valuenya
    private EditText newNumber;
    private TextView displayOperation;

    //variables to hold the operands and type of calculations
    private Double operand1 = null; //declaration variable pengali awal/angka yang di oper
    private String pendingOperation = "="; //nunjukkin buat mulai lg dr awal

    private static final String STATE_PENDING_OPERATION = "PnedingOperation";
    private static final String STATE_OPERAND1 = "Operand1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (EditText) findViewById(R.id.result); //deklarasi object - widgetID
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        Button button0 = (Button) findViewById(R.id.button0); //deklarasi object angka - widgetID
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);

        Button buttonEqual = (Button) findViewById(R.id.buttonResult); //deklarasi object pengali - widgetID
        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonErase = (Button) findViewById(R.id.buttonErase);
        Button buttonNeg = (Button) findViewById(R.id.buttonNeg);
        Button buttonSpecial = (Button) findViewById(R.id.buttonSpecial);

        View.OnClickListener listener = new View.OnClickListener() { //DEKLARASI OBJECT NNUMBER
            @Override
            public void onClick(View view) {
                Button b = (Button) view; //declaration for button yang bakal di klik dari view. Setiap button yg di klik ditranslate nya jadi b HARUS
                newNumber.append(b.getText().toString()); //setiap klik yg di pencet, masuk ke new NUMBER
            }
        };

        button0.setOnClickListener(listener);       //DEKLARASI KIRA2 BUTTON APA AJA YANG INGIN DIPAKAI LISTENER
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);

        View.OnClickListener opListener = new View.OnClickListener() { //DEKLARASI BUTTON PENGALI
            @Override
            public void onClick(View view) {
                Button b = (Button) view; //declaration for button yang bakal di klik dari view. Setiap button yg di klik ditranslate nya jadi b HARUS
                String op = b.getText().toString(); //DEKLARASI TULISAN PENGALI +-/* DIJADIIN STRING, YANG NANTINYA BAKAL DIPAKE DI METHOD MENJADI SWITCH CASE
                String value = newNumber.getText().toString(); //DEKLARASI value SEBAGAI ANGKA YANG ADA DI NEW NUMBER
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, op);
                } catch (NumberFormatException e) {
                    newNumber.setText("");
                }

                pendingOperation = op;
                displayOperation.setText(pendingOperation);

            }
        };

        buttonEqual.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);



        View.OnClickListener newNegative = new View.OnClickListener() { //buat negative number
            @Override
            public void onClick(View view) {
                String value = newNumber.getText().toString();
                if(value.length()==0){
                    newNumber.setText("-");
                }
                else{
                    try {
                        Double doubleValue = Double.valueOf(value);
                        doubleValue *= -1;
                        newNumber.setText(doubleValue.toString());
                    }
                    catch (NumberFormatException e){
                        //newNumber was "-" or ",", so clear it
                        newNumber.setText("");
                    }
                }
            }
        };
        buttonNeg.setOnClickListener(newNegative);

        View.OnClickListener newButton = new View.OnClickListener() { //buat apus semuanya C
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                    try {
                        newNumber.setText("");
                        result.setText("");
                        if(operand1 != null){
                            operand1 = operand1*0;
                        }
                    }
                    catch (NumberFormatException e){
                        //newNumber was "-" or ",", so clear it
                        newNumber.setText("");
                    }

            }
        };
        buttonErase.setOnClickListener(newButton);

        View.OnClickListener specialButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                newNumber.setText("MAJID GANTENG");
                result.setText("TERIMAKASIH");
            }
        };
        buttonSpecial.setOnClickListener(specialButton);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //SAVE DATA KE BUNDLE DI FASE INI, SEBELUM DIA DIJALANKAN (di super) DAN SEBELUM DI ROTATE
        outState.putString(STATE_PENDING_OPERATION, pendingOperation); //PUT OPERATION "string"
        if(operand1 != null){
            outState.putDouble(STATE_OPERAND1,operand1); //PUT DOUBLE OPERAND1 "double"
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState); //BEKERJA DULU -> BARU FILE MANA YANG PENGEN DI AMBIL/PAKE
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1);
        displayOperation.setText(pendingOperation);
    }

    private void performOperation(Double value, String operation) {
        if (null == operand1) {
            operand1 = value;
        } else {
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }
            switch (pendingOperation) {
                case "=":
                    operand1 = value;
                    break;
                case "+":
                    operand1 += value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "/":
                    if (value == 0) {
                        operand1 = 0.0;
                    } else {
                        operand1 /= value;
                    }
                    break;
            }
        }
        result.setText(operand1.toString());
        newNumber.setText("");
    }
}

