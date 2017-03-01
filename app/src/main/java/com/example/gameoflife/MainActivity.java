package com.example.gameoflife;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLineView gridLineView = (GridLineView) findViewById(R.id.grid_line_view);
        gridLineView.setNumberOfColumns(12);
        gridLineView.setNumberofRows(12);
        gridLineView.invalidate();
        gridLineView.toggleGrid();
        Log.e("IsGridShown",gridLineView.isGridShown()+ "");


        TextView view = (TextView)findViewById(R.id.text_label);

        gridLineView.setOnTouchListener(gridLineView);
        Button btnNext = (Button) findViewById(R.id.button_next);
        btnNext.setOnClickListener(this);
        Button btnReset = (Button) findViewById(R.id.button_reset);
        btnReset.setOnClickListener(this);

    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((GridLineView) findViewById(R.id.grid_line_view)).resetGrid();
                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public void onClick(View v){

        switch(v.getId()){
            case R.id.button_reset:
                showInputDialog();
                break;
            case R.id.button_next:
                ((GridLineView) findViewById(R.id.grid_line_view)).nextStep();
                break;
        }
    }

}

