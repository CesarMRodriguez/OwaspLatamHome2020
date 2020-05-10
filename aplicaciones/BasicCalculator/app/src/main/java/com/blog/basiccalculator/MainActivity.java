package com.blog.basiccalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button,button2,button3,button4,button5;
    private EditText editText,editText2;
    private EditText editText3;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startcalc();
    }

    private void startcalc()
    {
        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        button5=(Button)findViewById(R.id.button5);
        editText=(EditText)findViewById(R.id.editText);
        editText2=(EditText)findViewById(R.id.editText2);
        editText3=(EditText)findViewById(R.id.editText3);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

    }

    private int add(int int1, int int2) {
        return int1 + int2;
    }

    private Integer sub(int int1, int int2) {
        return int1 - int2;
    }

    private Integer multiply(int int1, int int2) {
        return int1 * int2;
    }

    private Integer divide(int int1, int int2) {
        return int1 / int2;
    }

    @Override
    public void onClick(View view)
    {
        String num1=editText.getText().toString();
        String num2=editText2.getText().toString();
        switch(view.getId())
        {
            case R.id.button:
                int mul=multiply(Integer.parseInt(num1), Integer.parseInt(num2));
                editText3.setText(String.valueOf(mul));
                break;
            case R.id.button2:
                int add=add(Integer.parseInt(num1),Integer.parseInt(num2));
                editText3.setText(String.valueOf(add));
                break;
            case R.id.button3:
                int sub=sub(Integer.parseInt(num1),Integer.parseInt(num2));
                editText3.setText(String.valueOf(sub));
                break;
            case R.id.button4:
                try {
                    int div = divide(Integer.parseInt(num1), Integer.parseInt(num2));
                    editText3.setText(String.valueOf(div));
                }
                catch(Exception e)
                {
                    editText3.setText("Div. By 0..!!");
                }
                break;
        }
    }

    public void BasicToast(View view)
    {
        Context context=getApplicationContext();
        CharSequence text="Hello..This is toast..!!";
        int duration= Toast.LENGTH_LONG;

        Toast toast=Toast.makeText(context,text,duration);
        toast.show();
    }

}
