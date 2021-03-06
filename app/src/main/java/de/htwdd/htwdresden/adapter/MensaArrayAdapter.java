package de.htwdd.htwdresden.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import de.htwdd.htwdresden.R;
import de.htwdd.htwdresden.types.Meal;
import de.htwdd.htwdresden.classes.VolleyDownloader;

/**
 * Adapter für die Anzeige der Speisepläne
 */
public class MensaArrayAdapter extends AbstractBaseAdapter<Meal> {

    public MensaArrayAdapter(Context context, ArrayList<Meal> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_mensa_detail_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.mensa_title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.mensa_price);
            viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.mensa_image);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Meal meal = getItem(position);

        if (meal.getTitle() != null)
            viewHolder.title.setText(meal.getTitle());
        if (meal.getPrice() != null)
            viewHolder.price.setText(meal.getPrice());
        if (!meal.getImageUrl().isEmpty()) {
            ImageLoader imageLoader = VolleyDownloader.getInstance(context).getImageLoader();
            viewHolder.imageView.setImageUrl(meal.getImageUrl(), imageLoader);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }
        else viewHolder.imageView.setVisibility(View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView price;
        NetworkImageView imageView;
    }
}
