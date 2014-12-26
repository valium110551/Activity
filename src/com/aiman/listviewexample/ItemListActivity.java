package com.aiman.listviewexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemListActivity extends Activity{
	
	private String id;
	private TextView description;
	
	//url to get activity details
	private static final String url_activity_details = "http://54.68.92.191/android_connect_activities/get_activity_details.php";
	
	//JSON node names
	private static final String TAG_ID = "id";
	private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPRION = "description";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "info";
	
	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		
		// getting activity details from intent
		Intent intent = getIntent();
		
		//getting product id (pid) from intent
		id = intent.getStringExtra(TAG_ID);
		
		new GetProductDetails().execute();
		
	}
	
	
	class GetProductDetails extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {
			
			int k;
			k=0;
			String json_string;
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("id", id));
			json_string = jsonParser.makeHttpRequest(
					url_activity_details, "GET", param);
					
				//}
			//});

			return json_string;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String json_string) {
			
			try {
				// Building Parameters
				
				// getting product details by making HTTP request
				// Note that product details url will use GET request		
				
				JSONObject json = new JSONObject(json_string);	

				// check your log for json response
				Log.d("Single Product Details", json.toString());
				
				// json success tag
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully received product details
					JSONArray productObj = json
							.getJSONArray(TAG_PRODUCT); // JSON Array
					
					// get the only one product object from JSON Array
					JSONObject product = productObj.getJSONObject(0);

					// product with this pid found
					// Edit Text
					
					description = (TextView) findViewById(R.id.description);				
					description.setText(product.getString(TAG_DESCRIPRION));
					//mealName = (EditText) findViewById(R.id.inputName);
					
					ImageView actView = (ImageView) findViewById(R.id.activity_image);
					
					String temp = product.getString("categories_logo");
					
					Picasso.with(getApplicationContext())
					.load(//CATEGORY_LOGO_URL+
							product.getString("categories_logo")) // Photo URL
					.placeholder(R.drawable.placeholder) // This image will be displayed while photo URL is loading
					.error(R.drawable.error) // if error shows up during downloading
					.fit().centerCrop() // settings
					.into(actView); // we put it into our layout component (imageview)
					
					

				}else{
					// product with pid not found
				}
			} catch (JSONException e) {
				Log.e("ff", "here");
				e.printStackTrace();
			}
			
		}
	
	}
	
}

