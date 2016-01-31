package audit.audit_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseException;
import com.parse.FindCallback;
/**
 * Created by tammy on 1/23/16.
 */
public class DisplayData  extends Activity {
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_data);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

        final ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    listView = (ListView) findViewById(R.id.list);

                    try {
                        List<ParseUser> queryList = query.find();
                        ArrayList<String> userInputs = getData(queryList);

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(DisplayData.this, android.R.layout.simple_list_item_1, userInputs);

                        listView.setAdapter(adapter);

                    } catch (com.parse.ParseException ex) {
                        System.out.println("Error.");
                    }
                } else {
                    System.out.println("Error: failed to obtain data from Parse");
                }
            }
        });
    }


    public ArrayList<String> getData(List<ParseUser> inputs) {
        ArrayList<String> data = new ArrayList<String>();
        for(int ndx = 0; ndx < inputs.size(); ndx++) {

            ParseObject obj = inputs.get(ndx);
            String input = obj.getString("firstName") + " " + obj.getString("lastName") + " "
                    + obj.getString("username") + " " + obj.getString("email");
            data.add(input);
        }

        return data;
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
