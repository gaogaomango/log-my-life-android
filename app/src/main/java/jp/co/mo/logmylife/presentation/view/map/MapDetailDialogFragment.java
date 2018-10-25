package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapDetailDialog extends DialogFragment {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private static final int PREV_BUTTON = 0;
    private static final int NEXT_BUTTON = PREV_BUTTON + 1;

    public static final int ADD_IMAGE_BUTTON_RESULT = 0;


    private Activity mActivity;
    private Context mContext;
    private MapPlaceData mMapPlaceData;
    private Marker mMarker;

    private MapInfoDialog.MapDataTypeItem mDataTypeSelected;
    private MapInfoDialog.MapRestaurantTypeItem mRestaurantTypeSelected;

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.type)
    Spinner type;
    @BindView(R.id.type_details_title)
    TextView typeDetailTitle;
    @BindView(R.id.type_details) Spinner typeDetail;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.details) EditText details;
    @BindView(R.id.create_date) TextView createDate;
    @BindView(R.id.update_date) TextView updateDate;
    @BindView(R.id.edit_place_info)
    Button editBtn;
    @BindView(R.id.update_place_info) Button updatePlaceInfo;
    @BindView(R.id.pictures)
    ViewPager picsViewPager;
    @BindView(R.id.prev_img)
    ImageView prevImg;
    @BindView(R.id.next_img) ImageView nextImg;
    @BindView(R.id.add_image) Button addImage;

    private MapUseCaseImpl mMapUseCase = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_custom_infowindow, null);

        return view;
    }

}
