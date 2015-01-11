//src:http://blog.aimanbaharum.com/2014/01/android-development-3-populating-custom.html
package com.aiman.listviewexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// ALL JSON node names
	public static final String TAG_ID = "id";
	public static final String TAG_NAME = "category";
	public static final String TAG_CATEGORIES_COUNT = "categories_count";
	public static final String TAG_CATEGORIES_LOGO = "categories_logo";

	public static final String URL_CATEGORY = "http://54.68.92.191/android_connect_activities/get_all_activities.php";

	// albums JSONArray
	JSONArray categories = null;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	// private ListView lv;
	private BaseAdapter mAdapter;

	ArrayList<HashMap<String, String>> categoryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		// Hashmap for ListView
		categoryList = new ArrayList<HashMap<String, String>>();
		
		Log.d("onCreate: ", "Yep");
		
		// get listview
		ListView lv = getListView();
		lv.setDivider(null);

		lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				
				//Toast.makeText(CategoryActivity.this, "Item selected: " + position,
						//Toast.LENGTH_LONG).show();
				
				//Uncomment this to start a new Activity for a chosen item
				Intent i = new Intent(getApplicationContext(),
						ItemListActivity.class);

				String category_id = ((TextView) view
						.findViewById(R.id.category_id)).getText()
						.toString();
				i.putExtra(TAG_ID, category_id);

				startActivity(i);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
		
		new LoadCategories().execute();
	}

	class LoadCategories extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CategoryActivity.this);
			pDialog.setMessage("Listing Categories...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(URL_CATEGORY, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("Categories JSON: ", "> " + json);

			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			
			try {
				//categories = new JSONArray(json);
				
				JSONObject parentObject = new JSONObject(json);				
				categories = parentObject.getJSONArray("info");		

				if (categories != null) {
					// looping through All albums
						
					for (int i = 0; i < categories.length(); i++) {
						JSONObject c = categories.getJSONObject(i);

						// Storing each json item values in variable
						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);
						String songs_count = c.getString(TAG_CATEGORIES_COUNT);
						String category_logo = c.getString(TAG_CATEGORIES_LOGO);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, id);
						map.put(TAG_NAME, name);
						map.put(TAG_CATEGORIES_COUNT, songs_count);
						map.put(TAG_CATEGORIES_LOGO, category_logo);

						// adding HashList to ArrayList
						categoryList.add(map);
					}

					mAdapter = new CategoryListAdapter(CategoryActivity.this,
							categoryList);
					getListView().setAdapter(mAdapter);

					// dismiss the dialog after getting all albums
					pDialog.dismiss();
				} else {
					Log.d("Categories: ", "null");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}

}
