package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.entity.map.InfoWindowData;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = CustomInfoWindowGoogleMap.class.getSimpleName();
    private Context mContext;

    public CustomInfoWindowGoogleMap(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);

        EditText nameTv = view.findViewById(R.id.name);
        EditText detailsTv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);
        EditText hotelTv = view.findViewById(R.id.hotels);
        EditText foodTv = view.findViewById(R.id.food);
        EditText transportTv = view.findViewById(R.id.transport);
        Button editBtn = view.findViewById(R.id.edit_place_info);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.error(TAG, "edit button onClick");
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.error(TAG, "view onClick");
            }
        });

        nameTv.setText(marker.getTitle());
        detailsTv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = mContext.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", mContext.getPackageName());
        img.setImageResource(imageId);

        hotelTv.setText(infoWindowData.getHotel());
        foodTv.setText(infoWindowData.getFood());
        transportTv.setText(infoWindowData.getTransport());

        return view;
    }
}
