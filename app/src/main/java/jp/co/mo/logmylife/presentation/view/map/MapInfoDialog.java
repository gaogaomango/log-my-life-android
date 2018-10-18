package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.domain.entity.map.InfoWindowData;

public class MapInfoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = MapInfoDialog.class.getSimpleName();

    private Activity mActivity;
    private InfoWindowData mInfoWindowData;
    private Button editBtn, updatePlaceInfo;
    private EditText nameTv, detailsTv, hotelTv, foodTv, transportTv;

    public MapInfoDialog(Activity activity, InfoWindowData infoWindowData) {
        super(activity);
        this.mActivity = activity;
        this.mInfoWindowData = infoWindowData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_custom_infowindow);

        nameTv = findViewById(R.id.name);
        detailsTv = findViewById(R.id.details);
        ImageView img = findViewById(R.id.pic);
        hotelTv = findViewById(R.id.hotels);
        foodTv = findViewById(R.id.food);
        transportTv = findViewById(R.id.transport);
        nameTv.setText("hogeTitle");
        detailsTv.setText("hogeTV");

        int imageId = mActivity.getApplicationContext().getResources().getIdentifier(mInfoWindowData.getImage().toLowerCase(),
                "drawable", mActivity.getApplicationContext().getPackageName());
        img.setImageResource(imageId);

        hotelTv.setText(mInfoWindowData.getHotel());
        foodTv.setText(mInfoWindowData.getFood());
        transportTv.setText(mInfoWindowData.getTransport());

        editBtn = findViewById(R.id.edit_place_info);
        editBtn.setOnClickListener(this);
        updatePlaceInfo = findViewById(R.id.update_place_info);
        updatePlaceInfo.setOnClickListener(this);

        changeInfoUpdateStatus(false);
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

    private void changeInfoUpdateStatus(boolean canEdit) {
        nameTv.setEnabled(canEdit);
        detailsTv.setEnabled(canEdit);
        hotelTv.setEnabled(canEdit);
        foodTv.setEnabled(canEdit);
        transportTv.setEnabled(canEdit);

        if(canEdit) {
            editBtn.setVisibility(View.GONE);
            updatePlaceInfo.setVisibility(View.VISIBLE);
        } else {
            editBtn.setVisibility(View.VISIBLE);
            updatePlaceInfo.setVisibility(View.GONE);
        }
    }
}

