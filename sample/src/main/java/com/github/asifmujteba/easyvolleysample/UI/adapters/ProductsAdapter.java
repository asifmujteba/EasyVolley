package com.github.asifmujteba.easyvolleysample.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.asifmujteba.easyvolleysample.App;
import com.github.asifmujteba.easyvolleysample.Models.Product;
import com.github.asifmujteba.easyvolleysample.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by asifmujteba on 08/08/15.
 */
public class ProductsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> products = new ArrayList<>();

    public ProductsAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (productArrayList.size() > 0) {
            products.addAll(productArrayList);
        }
    }

    public void setProductsAndNotify(ArrayList<Product> productArrayList) {
        products.clear();
        products.addAll(productArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (products != null) {
            return products.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);
        holder.lblTitle.setText(product.getData().getName());
        holder.lblPrice.setText("RM " + product.getData().getPrice());

        if (product.getImages().length > 0) {
            holder.imageView.setImageResource(R.mipmap.unknown);
            App.getInstance().getServerController().loadImage(product.getImages()[0].getPath()+"?thumb=1")
                    .setMaxWidth(375)
                    .setMaxHeight(375)
                    .setScaleType(ImageView.ScaleType.CENTER_INSIDE)
                    .into(holder.imageView)
                    .start();
        }

        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.imageView) public ImageView imageView;
        @InjectView(R.id.lblTitle) public TextView lblTitle;
        @InjectView(R.id.lblPrice) public TextView lblPrice;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}
