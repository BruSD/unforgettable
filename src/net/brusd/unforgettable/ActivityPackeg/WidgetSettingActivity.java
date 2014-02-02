package net.brusd.unforgettable.ActivityPackeg;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.brusd.unforgettable.GlobalPackeg.SharedPreferencesSticker;
import net.brusd.unforgettable.R;


/**
 * Created by BruSD on 04.01.14.
 */
public class WidgetSettingActivity extends ActionBarActivity {
    private TextView quoteModeTextView, quoteUpdateTimeTextView;
    private  Spinner widgetModeSpiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_setting_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeSpinnerAdapte();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
     class SpinnerAdapter implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            SharedPreferencesSticker.setWidgetShowingMode(WidgetSettingActivity.this, pos);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback

        }
    }

    private void initializeSpinnerAdapte(){
        widgetModeSpiner = (Spinner)findViewById(R.id.widget_setting_quote_mode_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.widget_mode_araay_string, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        widgetModeSpiner.setAdapter(adapter);
        widgetModeSpiner.setSelection(SharedPreferencesSticker.getWidgetShowingMode(this));

        widgetModeSpiner.setOnItemSelectedListener(new SpinnerAdapter());
    }
}
