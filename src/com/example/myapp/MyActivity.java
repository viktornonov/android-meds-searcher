package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MyActivity extends Activity {
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView results;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        results = (ListView)findViewById(R.id.resultList);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        results.setAdapter(adapter);
    }

    public void searchMed(View view) {
        EditText editText = (EditText) findViewById(R.id.searchTerm);
        String searchTerm = editText.getText().toString();
        //ListView results = (ListView)findViewById(R.id.resultList);
        MySqlLiteHelper db = new MySqlLiteHelper(this);
        ArrayList<Med> meds = db.getMedsByName(searchTerm);
        if (meds != null) {
            listItems.clear();
            for(Med m: meds) {
                listItems.add(m.toString());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
