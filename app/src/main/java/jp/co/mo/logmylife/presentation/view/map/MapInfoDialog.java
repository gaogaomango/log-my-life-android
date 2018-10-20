package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.enums.MapDataType;
import jp.co.mo.logmylife.common.enums.RestaurantType;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapInfoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

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

    private MapUseCaseImpl mMapUseCase = null;

    HashMap<Integer, String> typeMap = null;

    public MapInfoDialog(Activity activity, MapPlaceData mapPlaceData, Marker marker) {
        super(activity);
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
            setDataType(type);
            if(mMapPlaceData.getTypeId() != null) {
                type.setSelection(mMapPlaceData.getTypeId());
                if(MapDataType.RESTAURANT.equals(MapDataType.getById(mMapPlaceData.getTypeId()))
                        || MapDataType.RESTAURANT.equals(MapDataType.getById((Integer)mDataTypeSelected.id))) {
                    typeDetailTitle.setVisibility(View.VISIBLE);
                    typeDetail.setVisibility(View.VISIBLE);
                }
            }
            setRestaurantType(typeDetail);
            if(mMapPlaceData.getTypeDetailId() != null) {
                type.setSelection(mMapPlaceData.getTypeDetailId());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_place_info:
                changeInfoUpdateStatus(true);
                break;
            case R.id.update_place_info:
                // TODO: update using api.
                updateData();
                changeInfoUpdateStatus(false);
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
        // TODO: 数字を選択式にする。
        mMapPlaceData.setTypeId((Integer)mDataTypeSelected.id);
        mMapPlaceData.setTypeDetailId((Integer)mRestaurantTypeSelected.id);
        mMapPlaceData.setUrl(url.getText().toString());
        mMapPlaceData.setDetail(details.getText().toString());
        mMapPlaceData.setCreateDate(mMapPlaceData.getCreateDate());
        mMapPlaceData.setUpdateDate(mMapPlaceData.getUpdateDate());

        mMapUseCase.saveMapPlaceData(mContext, mMapPlaceData);
        // マーカーにセットし直す。
        mMarker.setTag(mMapPlaceData);
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

