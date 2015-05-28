package de.uni_weimar.m18.backupfestival.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import de.uni_weimar.m18.backupfestival.fragments.DummyFragment;
import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.fragments.EventsFragment;
import de.uni_weimar.m18.backupfestival.fragments.FilmsFragment;
import de.uni_weimar.m18.backupfestival.R;


public class MainActivity extends AppCompatActivity {

    private int mCurrentId = -1;

    public enum Category {
        NEWS(1000),
        EVENTS(1001),
        FILMS(1002),
        MAP(1003),
        SOCIAL(1004);

        public final int id;
        private Category(int id) {
            this.id = id;
        }
    }

    public Drawer.Result mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = new Drawer()
                    .withActivity(this)
                    .withHeader(R.layout.header_drawer)
                    .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_news)
                                .withIdentifier(Category.NEWS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_announcement),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_events)
                                .withIdentifier(Category.EVENTS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_schedule),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_films)
                                .withIdentifier(Category.FILMS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_theaters),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_map)
                                .withIdentifier(Category.MAP.id)
                                .withIcon(GoogleMaterial.Icon.gmd_map),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_social)
                                .withIdentifier(Category.SOCIAL.id)
                                .withIcon(GoogleMaterial.Icon.gmd_insert_comment)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                                if (iDrawerItem != null) {
                                    //toolbar.setTitle(((Nameable) iDrawerItem).getNameRes());
                                    int id = iDrawerItem.getIdentifier();
                                    if (mCurrentId != id) {
                                        mCurrentId = id;
                                        Fragment fragment = null;

                                        if (id == Category.FILMS.id) {
                                            fragment = FilmsFragment.newInstance();

                                        } else if (id == Category.EVENTS.id) {
                                            fragment = EventsFragment.newInstance();
                                        }

                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                        // Replace whatever is in the fragment_container view with this fragment,
                                        // and add the transaction to the back stack so the user can navigate back
                                        transaction.replace(R.id.main_container, fragment);
                                        transaction.addToBackStack(null);
                                        // Commit the transaction
                                        transaction.commit();
                                    }
                                }
                            }
                        })
                        .build();

        // disable scrollbar
        mResult.getListView().setVerticalScrollBarEnabled(false);


        FestivalApplication.setDrawer(mResult);

        //showAll();
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
