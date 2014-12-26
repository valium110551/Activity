package com.aiman.listviewexample;

import java.util.ArrayList;
import java.util.HashMap;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {
	private Context mContext;
	LayoutInflater inflater;
	private final ArrayList<HashMap<String, String>> urls;
	HashMap<String, String> resultp = new HashMap<String, String>();
	
	public static final String CATEGORY_LOGO_URL = "http://zellar.comze.com/webshopper/res/categories/";

	public CategoryListAdapter(Context context,
			ArrayList<HashMap<String, String>> items) {
		mContext = context;
		urls = items;
	}

    public int getCount() {
        return urls.size();
    }
 
    public Object getItem(int position) {
        return position;
    }

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView category_name;
		ImageView category_logo;
		TextView item_count;
		TextView item_id;

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.custom_list_category, parent,
				false);

		resultp = urls.get(position);

		category_name = (TextView) view.findViewById(R.id.category_name);
		category_logo = (ImageView) view.findViewById(R.id.img_category_logo);
		item_count = (TextView) view.findViewById(R.id.songs_count);
		item_id = (TextView) view.findViewById(R.id.category_id);

		category_name.setText(resultp.get(CategoryActivity.TAG_NAME));
		item_count.setText(resultp.get(CategoryActivity.TAG_CATEGORIES_COUNT));
		item_id.setText(resultp.get(CategoryActivity.TAG_ID));

		// Picasso image loader library starts here
		Picasso.with(mContext)
				.load(//CATEGORY_LOGO_URL+
						 resultp.get(CategoryActivity.TAG_CATEGORIES_LOGO)) // Photo URL
				.placeholder(R.drawable.placeholder) // This image will be displayed while photo URL is loading
				.error(R.drawable.error) // if error shows up during downloading
				.fit().centerCrop() // settings
				.into(category_logo); // we put it into our layout component (imageview)

		return view;
	}

}
