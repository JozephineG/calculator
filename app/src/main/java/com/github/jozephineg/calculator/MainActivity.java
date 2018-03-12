package com.github.jozephineg.calculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.jozephineg.calculator.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private double valueOne = Double.NaN;
    private double valueTwo;

    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';

    private final DecimalFormat decimalFormat = new DecimalFormat("#.########");

    private char ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editText.setText(binding.editText.getText() + ".");
            }
        });

        binding.buttonZero.setOnClickListener(numberListener);
        binding.buttonOne.setOnClickListener(numberListener);
        binding.buttonTwo.setOnClickListener(numberListener);
        binding.buttonThree.setOnClickListener(numberListener);
        binding.buttonFour.setOnClickListener(numberListener);
        binding.buttonFive.setOnClickListener(numberListener);
        binding.buttonSix.setOnClickListener(numberListener);
        binding.buttonSeven.setOnClickListener(numberListener);
        binding.buttonEight.setOnClickListener(numberListener);
        binding.buttonNine.setOnClickListener(numberListener);

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
                ACTION = ADDITION;
                binding.infoTextView.setText(decimalFormat.format(valueOne) + "+");
                binding.editText.setText(null);
            }
        });

        binding.buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
                ACTION = SUBTRACTION;
                binding.infoTextView.setText(decimalFormat.format(valueOne) + "-");
                binding.editText.setText(null);
            }
        });

        binding.buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
                ACTION = MULTIPLICATION;
                binding.infoTextView.setText(decimalFormat.format(valueOne) + "*");
                binding.editText.setText(null);
            }
        });

        binding.buttonDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
                ACTION = DIVISION;
                binding.infoTextView.setText(decimalFormat.format(valueOne) + "/");
                binding.editText.setText(null);
            }
        });

        binding.buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
                binding.infoTextView.setText(binding.infoTextView.getText().toString() +
                        decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
                ACTION = '0';
            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.editText.getText().length() > 0) {
                    CharSequence currentText = binding.editText.getText();
                    binding.editText.setText(currentText.subSequence(0, currentText.length() - 1));
                } else {
                    valueOne = Double.NaN;
                    valueTwo = Double.NaN;
                    binding.editText.setText("");
                    binding.infoTextView.setText("");
                }
            }
        });
    }

    private void calculate() {
        // to be able to keep going after we press equals
        if (binding.editText.getText().toString().isEmpty()) {
            return;
        }

        // https://docs.oracle.com/javase/6/docs/api/java/text/DecimalFormat.html
        // infinity symbol shown when dividing with zero
        if(Double.isNaN(valueOne)) {
            try {
                valueOne = Double.parseDouble(binding.editText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            valueTwo = Double.parseDouble(binding.editText.getText().toString());
            binding.editText.setText(null);

            if (ACTION == ADDITION) {
                valueOne = this.valueOne + valueTwo;
            } else if (ACTION == SUBTRACTION) {
                valueOne = this.valueOne - valueTwo;
            } else if (ACTION == MULTIPLICATION) {
                valueOne = this.valueOne * valueTwo;
            } else if (ACTION == DIVISION) {
                valueOne = this.valueOne / valueTwo;
                if(Double.isInfinite(valueOne)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("You can't divide by zero!!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        }
    }

    View.OnClickListener numberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b = (Button)view;
            String number = b.getText().toString();
            binding.editText.setText(binding.editText.getText() + number);
        }
    };
}
