package de.uni_weimar.m18.backupfestival.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import de.uni_weimar.m18.backupfestival.R;


public class MainActivity extends AppCompatActivity {

    public enum Category {
        EVENTS(1000),
        FILMS(1001);

        public final int id;
        private Category(int id) {
            this.id = id;
        }
    }

    public Drawer.Result result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);

        setSupportActionBar(toolbar);

        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header_drawer)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_events)
                                .withIdentifier(Category.EVENTS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_schedule),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_films)
                                .withIdentifier(Category.FILMS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_theaters)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        if (iDrawerItem != null) {
                            toolbar.setTitle(((Nameable) iDrawerItem).getNameRes());
                        }
                    }
                })
                .build();

        // disable scrollbar
        result.getListView().setVerticalScrollBarEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setIcon(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings)
                    .color(Color.LTGRAY)
                    .actionBarSize()
        );
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
