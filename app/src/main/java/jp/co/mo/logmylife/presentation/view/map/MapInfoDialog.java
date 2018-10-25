package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.enums.MapDataType;
import jp.co.mo.logmylife.common.enums.RestaurantType;
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapInfoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private static final int PREV_BUTTON = 0;
    private static final int NEXT_BUTTON = PREV_BUTTON + 1;

    public static final int ADD_IMAGE_BUTTON_RESULT = 0;


    private Activity mActivity;
    private Context mContext;
    private MapPlaceData mMapPlaceData;
    private Marker mMarker;

    private MapDataTypeItem mDataTypeSelected;
    private MapRestaurantTypeItem mRestaurantTypeSelected;

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

    public MapInfoDialog(Activity activity, MapPlaceData mapPlaceData, Marker marker) {
        super(activity);
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
        this.mMapPlaceData = mapPlaceData;
        this.mMarker = marker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_custom_infowindow);
        ButterKnife.bind(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        if(mMapPlaceData != null) {
            title.setText(mMapPlaceData.getTitle());
            PicturesPagerAdapter picsPagerAdapter;
            if(mMapPlaceData.getId() == null) {
                picsPagerAdapter = new PicturesPagerAdapter(mContext);
            } else {
                picsPagerAdapter = new PicturesPagerAdapter(mContext, mMapPlaceData.getId());
            }
            picsViewPager.setAdapter(picsPagerAdapter);
            prevImg.setClickable(true);
            prevImg.setOnClickListener(onClickListener(PREV_BUTTON));
            nextImg.setClickable(true);
            nextImg.setOnClickListener(onClickListener(NEXT_BUTTON));
            addImage.setOnClickListener(this);

            setDataType(type);
            if(mMapPlaceData.getTypeId() != null) {
                if(MapDataType.RESTAURANT.equals(MapDataType.getById(mMapPlaceData.getTypeId()))
                        || (mDataTypeSelected != null && mDataTypeSelected.id != null && MapDataType.RESTAURANT.equals(MapDataType.getById((Integer)mDataTypeSelected.id)))) {
                    typeDetailTitle.setVisibility(View.VISIBLE);
                    typeDetail.setVisibility(View.VISIBLE);
                }
                type.setSelection(mMapPlaceData.getTypeId());
            }
            setRestaurantType(typeDetail);
            if(mMapPlaceData.getTypeDetailId() != null) {
                typeDetail.setSelection(mMapPlaceData.getTypeDetailId());
            }
            url.setText(mMapPlaceData.getUrl());
            details.setText(mMapPlaceData.getDetail());
            createDate.setText(mMapPlaceData.getCreateDate());
            updateDate.setText(mMapPlaceData.getUpdateDate());
            editBtn.setOnClickListener(this);
            updatePlaceInfo.setOnClickListener(this);
            changeInfoUpdateStatus(false);
        }
    }

    private void setDataType(Spinner spinner) {
        List<MapDataTypeItem> itemList = new ArrayList<>();
        for (MapDataType m : MapDataType.values()) {
            if(!m.equals(MapDataType.NO_VALUE)) {
                itemList.add(new MapDataTypeItem(m.getId(), m.getType()));
            }
        }
        ArrayAdapter<MapDataTypeItem> spinnerAdapter = new ArrayAdapter<>(this.mContext, android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                MapDataTypeItem item = (MapDataTypeItem) parent.getItemAtPosition(position);
                mDataTypeSelected = item;
                if(MapDataType.RESTAURANT.equals(MapDataType.getById(((Integer)item.id).intValue()))) {
                    typeDetailTitle.setVisibility(View.VISIBLE);
                    typeDetail.setVisibility(View.VISIBLE);
                } else {
                    typeDetailTitle.setVisibility(View.GONE);
                    typeDetail.setVisibility(View.GONE);
                    mRestaurantTypeSelected = null;
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
        List<MapRestaurantTypeItem> itemList = new ArrayList<>();
        for (RestaurantType r : RestaurantType.values()) {
            if(!r.equals(RestaurantType.NO_VALUE)) {
                itemList.add(new MapRestaurantTypeItem(r.getId(), r.getType()));
            }
        }
        ArrayAdapter<MapRestaurantTypeItem> spinnerAdapter = new ArrayAdapter<>(this.mContext, android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                MapRestaurantTypeItem item = (MapRestaurantTypeItem) parent.getItemAtPosition(position);
                mRestaurantTypeSelected = item;
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
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_place_info:
                changeInfoUpdateStatus(true);
                break;
            case R.id.update_place_info:
                updateData();
                changeInfoUpdateStatus(false);
                break;
            case R.id.add_image:
                // TODO: show photo garally
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("hoge", "foo");
                mActivity.startActivityForResult(intent, ADD_IMAGE_BUTTON_RESULT);
                break;
            default:
                break;
        }
    }

    private void updateData() {
        if(mMapUseCase == null) {
            mMapUseCase = new MapUseCaseImpl();
        }

        if(mMapPlaceData.getId() != null) {
            mMapPlaceData.setId(mMapPlaceData.getId());
        }
        if(mMapPlaceData.getUserId() != null) {
            mMapPlaceData.setUserId(mMapPlaceData.getUserId());

        }
        mMapPlaceData.setTitle(title.getText().toString());
        mMapPlaceData.setLat(mMapPlaceData.getLat());
        mMapPlaceData.setLng(mMapPlaceData.getLng());
        mMapPlaceData.setTypeId((Integer)mDataTypeSelected.id);
        if(mRestaurantTypeSelected != null && mRestaurantTypeSelected.id != null) {
            mMapPlaceData.setTypeDetailId((Integer)mRestaurantTypeSelected.id);
        }
        mMapPlaceData.setUrl(url.getText().toString());
        mMapPlaceData.setDetail(details.getText().toString());
        mMapPlaceData.setCreateDate(mMapPlaceData.getCreateDate());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS);
            String date = sdf.format(new Date());
            mMapPlaceData.setUpdateDate(date);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        mMapUseCase.saveMapPlaceData(mContext, mMapPlaceData);
        mMapUseCase.saveMapPicData(mContext, mMapPlaceData);
        // if it's new record, getting record.
        if(mMapPlaceData.getId() == null) {
            mMapPlaceData = mMapUseCase.getLastInsertedMapData();
        }
        // マーカーにセットし直す。
        mMarker.setTitle(mMapPlaceData.getTitle());
        mMarker.setTag(mMapPlaceData);

        // TODO: refresh
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

