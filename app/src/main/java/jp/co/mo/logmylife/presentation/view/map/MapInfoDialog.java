package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.enums.MapDataType;
import jp.co.mo.logmylife.common.enums.RestaurantType;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

public class MapInfoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private Context mContext;
    private MapPlaceData mMapPlaceData;
    private Marker mMarker;
    @BindView(R.id.title) EditText title;
    @BindView(R.id.type) EditText type;
    @BindView(R.id.type_details) EditText typeDetail;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.details) EditText details;
    @BindView(R.id.create_date) TextView createDate;
    @BindView(R.id.update_date) TextView updateDate;
    @BindView(R.id.edit_place_info) Button editBtn;
    @BindView(R.id.update_place_info) Button updatePlaceInfo;

    private MapUseCaseImpl mMapUseCase = null;

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
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        if(mMapPlaceData != null) {
            title.setText(mMapPlaceData.getTitle());
            if(mMapPlaceData.getTypeId() != null) {
                type.setText(MapDataType.getById(mMapPlaceData.getTypeId()).getType());
            }
            if(mMapPlaceData.getTypeDetailId() != null) {
                typeDetail.setText(RestaurantType.getById(mMapPlaceData.getTypeDetailId()).getType());
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
        mMapPlaceData.setTypeId(1);
        mMapPlaceData.setTypeDetailId(1);
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

