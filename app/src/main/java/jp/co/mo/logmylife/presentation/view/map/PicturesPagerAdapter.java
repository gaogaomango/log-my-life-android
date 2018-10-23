package jp.co.mo.logmylife.presentation.view.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jp.co.mo.logmylife.R;

public class PicturesPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;

    // TODO: remove this.
    private Integer [] images = {R.drawable.sample1,R.drawable.sample2, R.drawable.sample3};

    public PicturesPagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO: get image path from db, and return that number.
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.place_pic_viewpager, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
