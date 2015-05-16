package xworks.aungkhantmyo.parallel;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;


import java.util.List;


public class AppsListFragment extends ListActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<AppEntry>> {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppsListFragment() {
    }

    private AppListAdapter mAdapter;
    private String mCurFilter;
    private ProgressBar mIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_app);

        mAdapter = new AppListAdapter(this);
        setListAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                AppEntry item = (AppEntry) mAdapter.getItem(pos);

                String packageName = item.getPackageName();
                String label = item.getLabel();

                // TODO: SOMETHING
            }
        });
        mIndicator = (ProgressBar)findViewById(R.id.indicator);
        getLoaderManager().initLoader(0, null, this);

//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


    }


    @Override
    public Loader<List<AppEntry>> onCreateLoader(int i, Bundle bundle) {
        mIndicator.setVisibility(View.VISIBLE);
        return new AppLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> objectLoader, List<AppEntry> o) {

        mAdapter.setData(o);
        mIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> objectLoader) {

        mAdapter.setData(null);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mCurFilter = !TextUtils.isEmpty(s) ? s : null;
        mAdapter.getFilter().filter(mCurFilter);
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


}
