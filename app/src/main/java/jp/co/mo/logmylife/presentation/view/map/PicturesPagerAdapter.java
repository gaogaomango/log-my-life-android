package jp.co.mo.logmylife.presentation.view.map;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class PicturesPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;

    private MapUseCaseImpl mapUseCase;
    private Integer placeId;

    private List<MapPlacePicData> mapPicList;

    public PicturesPagerAdapter(Context context) {
        this.mContext = context;
        mapUseCase = new MapUseCaseImpl(context);
    }

    public PicturesPagerAdapter(Context context, Integer placeId) {
        this.mContext = context;
        this.mapUseCase = new MapUseCaseImpl(context);
        this.placeId = placeId;
    }

    @Override
    public int getCount() {

        if(placeId == null) {
            return 0;
        }

        mapPicList = mapUseCase.getMapPlacePicDatas(mContext, placeId);
        if(mapPicList == null && mapPicList.isEmpty()) {
            return 0;
        }
        return mapPicList.size();
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
        imageView.setImageURI(Uri.parse(mapPicList.get(position).getFilePath()));

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
