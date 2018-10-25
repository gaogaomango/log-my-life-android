package jp.co.mo.logmylife.presentation.view.map;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.enums.MapDataType;
import jp.co.mo.logmylife.common.enums.RestaurantType;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapDetailDialogFragment extends DialogFragment {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private static final String MAP_PLACE_DATA_KEY = "mapPlaceDataKey";

    private static final int PREV_BUTTON = 0;
    private static final int NEXT_BUTTON = PREV_BUTTON + 1;

    public static final int ADD_IMAGE_BUTTON = NEXT_BUTTON + 1;


//    private Activity mActivity;
//    private Context mContext;
    private MapPlaceData mMapPlaceData;
    private Marker mMarker;

//    private MapInfoDialog.MapDataTypeItem mDataTypeSelected;
//    private MapInfoDialog.MapRestaurantTypeItem mRestaurantTypeSelected;

    @BindView(R.id.title) EditText title;
    @BindView(R.id.type) Spinner type;
    @BindView(R.id.type_details_title) TextView typeDetailTitle;
    @BindView(R.id.type_details) Spinner typeDetail;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.details) EditText details;
    @BindView(R.id.create_date) TextView createDate;
    @BindView(R.id.update_date) TextView updateDate;
    @BindView(R.id.edit_place_info) Button editBtn;
    @BindView(R.id.update_place_info) Button updatePlaceInfo;
    @BindView(R.id.pictures) ViewPager picsViewPager;
    @BindView(R.id.prev_img) ImageView prevImg;
    @BindView(R.id.next_img) ImageView nextImg;
    @BindView(R.id.add_image) Button addImage;

    private MapUseCaseImpl mMapUseCase = null;

    // MapPlaceDataの中のMarkerを代入しておくこと。
    public static MapDetailDialogFragment newInstance(MapPlaceData mapPlaceData) {
        MapDetailDialogFragment fragment = new MapDetailDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(MAP_PLACE_DATA_KEY, mapPlaceData);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_custom_infowindow, null);
        Bundle b = getArguments();
        mMapPlaceData = (MapPlaceData) b.getSerializable(MAP_PLACE_DATA_KEY);

        return view;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private void setParams() {
        if(mMapPlaceData != null) {
            title.setText(mMapPlaceData.getTitle());
            PicturesPagerAdapter picsPagerAdapter;
            if(mMapPlaceData.getId() == null) {
                picsPagerAdapter = new PicturesPagerAdapter(getActivity().getApplicationContext());
            } else {
                picsPagerAdapter = new PicturesPagerAdapter(getActivity().getApplicationContext(), mMapPlaceData.getId());
            }
            picsViewPager.setAdapter(picsPagerAdapter);
            prevImg.setClickable(true);
            prevImg.setOnClickListener(onClickListener(PREV_BUTTON));
            nextImg.setClickable(true);
            nextImg.setOnClickListener(onClickListener(NEXT_BUTTON));
            addImage.setOnClickListener(onClickListener(ADD_IMAGE_BUTTON));

            setDataType(type);
            // TODO: やり直し。
//            if(mMapPlaceData.getTypeId() != null) {
//                if(MapDataType.RESTAURANT.equals(MapDataType.getById(mMapPlaceData.getTypeId()))
//                        || (mDataTypeSelected != null && mDataTypeSelected.id != null && MapDataType.RESTAURANT.equals(MapDataType.getById((Integer)mDataTypeSelected.id)))) {
//                    typeDetailTitle.setVisibility(View.VISIBLE);
//                    typeDetail.setVisibility(View.VISIBLE);
//                }
//                type.setSelection(mMapPlaceData.getTypeId());
//            }
            setRestaurantType(typeDetail);
            if(mMapPlaceData.getTypeDetailId() != null) {
                typeDetail.setSelection(mMapPlaceData.getTypeDetailId());
            }
            url.setText(mMapPlaceData.getUrl());
            details.setText(mMapPlaceData.getDetail());
            createDate.setText(mMapPlaceData.getCreateDate());
            updateDate.setText(mMapPlaceData.getUpdateDate());
            // TODO: やり直し
//            editBtn.setOnClickListener(this);
//            updatePlaceInfo.setOnClickListener(this);
            changeInfoUpdateStatus(false);
        }

    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == NEXT_BUTTON) {
                    //next page
                    if (picsViewPager.getCurrentItem() < picsViewPager.getAdapter().getCount() - 1) {
                        picsViewPager.setCurrentItem(picsViewPager.getCurrentItem() + 1);
                    }
                } else if(i == PREV_BUTTON) {
                    //previous page
                    if (picsViewPager.getCurrentItem() > 0) {
                        picsViewPager.setCurrentItem(picsViewPager.getCurrentItem() - 1);
                    }
                } else if(i == ADD_IMAGE_BUTTON) {

                }
            }
        };
    }

    private void setDataType(Spinner spinner) {
        List<MapDetailDialogFragment.MapDataTypeItem> itemList = new ArrayList<>();
        for (MapDataType m : MapDataType.values()) {
            if(!m.equals(MapDataType.NO_VALUE)) {
                itemList.add(new MapDetailDialogFragment.MapDataTypeItem(m.getId(), m.getType()));
            }
        }
        ArrayAdapter<MapDetailDialogFragment.MapDataTypeItem> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                MapDetailDialogFragment.MapDataTypeItem item = (MapDetailDialogFragment.MapDataTypeItem) parent.getItemAtPosition(position);
                // TODO: やり直し
//                mDataTypeSelected = item;
                if(MapDataType.RESTAURANT.equals(MapDataType.getById(((Integer)item.id).intValue()))) {
                    typeDetailTitle.setVisibility(View.VISIBLE);
                    typeDetail.setVisibility(View.VISIBLE);
                } else {
                    typeDetailTitle.setVisibility(View.GONE);
                    typeDetail.setVisibility(View.GONE);
                    // TODO: やり直し
//                    mRestaurantTypeSelected = null;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Logger.error(TAG, "onNothingSelected");
            }
        });
    }

    private static class MapDataTypeItem {
        public Object id;
        public String type;

        public MapDataTypeItem(Object id, String type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private void setRestaurantType(Spinner spinner) {
        List<MapDetailDialogFragment.MapRestaurantTypeItem> itemList = new ArrayList<>();
        for (RestaurantType r : RestaurantType.values()) {
            if(!r.equals(RestaurantType.NO_VALUE)) {
                itemList.add(new MapDetailDialogFragment.MapRestaurantTypeItem(r.getId(), r.getType()));
            }
        }
        ArrayAdapter<MapDetailDialogFragment.MapRestaurantTypeItem> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                MapDetailDialogFragment.MapRestaurantTypeItem item = (MapDetailDialogFragment.MapRestaurantTypeItem) parent.getItemAtPosition(position);
                // TODO: やり直し
//                mRestaurantTypeSelected = item;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Logger.error(TAG, "onNothingSelected");
            }
        });
    }

    private static class MapRestaurantTypeItem {
        public Object id;
        public String type;

        public MapRestaurantTypeItem(Object id, String type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private void changeInfoUpdateStatus(boolean canEdit) {
        title.setEnabled(canEdit);
        type.setEnabled(canEdit);
        typeDetail.setEnabled(canEdit);
        url.setEnabled(canEdit);
        details.setEnabled(canEdit);

        if(canEdit) {
            editBtn.setVisibility(View.GONE);
            updatePlaceInfo.setVisibility(View.VISIBLE);
        } else {
            editBtn.setVisibility(View.VISIBLE);
            updatePlaceInfo.setVisibility(View.GONE);
        }
    }

}
