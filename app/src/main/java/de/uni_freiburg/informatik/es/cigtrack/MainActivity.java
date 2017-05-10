package de.uni_freiburg.informatik.es.cigtrack;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import eu.senseable.sparklib.Spark;

public class MainActivity extends AppCompatActivity {

    private Spark mSpark = null;
    private Spark.Callbacks mSparkCalls = new Spark.Callbacks.Stub() {
        @Override
        public void onEventsChanged(List<Spark.Event> events) {
            super.onEventsChanged(events);

            int numcigs = events.size();
            String msg = getResources().getQuantityString(R.plurals.numcigs, numcigs, numcigs);
            TextView txt = (TextView) findViewById(R.id.text);
            txt.setText(msg);
        }
    };

    private View.OnClickListener mRemoveEventAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                List<Spark.Event> evs = mSpark.getEvents();
                if (evs.size() == 0)
                    return;

                evs.remove(evs.size() - 1);
                mSpark.setEvents(evs);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(mRemoveEventAction);

        mSpark = new Spark(this, mSparkCalls);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
