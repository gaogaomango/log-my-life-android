package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.enums.MapDataType;
import jp.co.mo.logmylife.common.enums.RestaurantType;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;

public class MapInfoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private Activity mActivity;
    private MapPlaceData mMapPlaceData;
    @BindView(R.id.title) EditText title;
    @BindView(R.id.type) EditText type;
    @BindView(R.id.type_details) EditText typeDetail;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.details) EditText details;
    @BindView(R.id.create_date) TextView createDate;
    @BindView(R.id.update_date) TextView updateDate;
    @BindView(R.id.edit_place_info) Button editBtn;
    @BindView(R.id.update_place_info) Button updatePlaceInfo;

    public MapInfoDialog(Activity activity, MapPlaceData mapPlaceData) {
        super(activity);
        this.mActivity = activity;
        this.mMapPlaceData = mapPlaceData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_custom_infowindow);
        ButterKnife.bind(this);

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
                changeInfoUpdateStatus(false);
                break;
            default:
                break;
        }
    }

    private void updateData() {

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

