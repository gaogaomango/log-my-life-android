package jp.co.mo.logmylife.presentation.view.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import android.widget.Toast;

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
import jp.co.mo.logmylife.common.util.RealPathUtil;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;
import jp.co.mo.logmylife.domain.usecase.MapUseCaseImpl;

import static android.app.Activity.RESULT_OK;

public class MapDetailDialogFragment extends DialogFragment {

    private static final String TAG = MapDetailDialogFragment.class.getSimpleName();

    private static final String MAP_PLACE_DATA_KEY = "mapPlaceDataKey";

    private static final int ADD_IMAGE_BUTTON_RESULT = 0;

    private static final int PREV_BUTTON = 0;
    private static final int NEXT_BUTTON = PREV_BUTTON + 1;
    private static final int ADD_IMAGE_BUTTON = NEXT_BUTTON + 1;
    private static final int EDIT_BUTTON = ADD_IMAGE_BUTTON + 1;
    private static final int UPDATE_BUTTON = EDIT_BUTTON + 1;
    private static final int DELETE_BUTTON = UPDATE_BUTTON + 1;

    private MapPlaceData mMapPlaceData;

    private MapDetailDialogFragment.MapDataTypeItem mDataTypeSelected;
    private MapDetailDialogFragment.MapRestaurantTypeItem mRestaurantTypeSelected;

    @BindView(R.id.title) EditText title;
    @BindView(R.id.title_txt) TextView titleTxt;
    @BindView(R.id.type) Spinner type;
    @BindView(R.id.type_details_title) TextView typeDetailTitle;
    @BindView(R.id.type_details) Spinner typeDetail;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.url_txt) TextView urlTxt;
    @BindView(R.id.details) EditText details;
    @BindView(R.id.details_txt) TextView detailsTxt;
    @BindView(R.id.create_date) TextView createDate;
    @BindView(R.id.update_date) TextView updateDate;
    @BindView(R.id.edit_place_info) Button editBtn;
    @BindView(R.id.update_place_info) Button updatePlaceInfo;
    @BindView(R.id.delete_place_info) Button deletePlaceInfo;
    @BindView(R.id.pictures) ViewPager picsViewPager;
    @BindView(R.id.prev_img) ImageView prevImg;
    @BindView(R.id.next_img) ImageView nextImg;
    @BindView(R.id.add_image) Button addImage;

    private MapUseCaseImpl mMapUseCase = null;

    private UpdateMarker mUpdateMarker;

    public static MapDetailDialogFragment newInstance(MapPlaceData mapPlaceData) {
        if(mapPlaceData == null) {
            return null;
        }
        MapDetailDialogFragment fragment = new MapDetailDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(MAP_PLACE_DATA_KEY, mapPlaceData);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
            mUpdateMarker = (UpdateMarker) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_custom_infowindow, null);
        ButterKnife.bind(this, view);
        Bundle b = getArguments();
        mMapPlaceData = (MapPlaceData) b.getSerializable(MAP_PLACE_DATA_KEY);
        setParams();
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
            titleTxt.setText(mMapPlaceData.getTitle());
            PicturesPagerAdapter picsPagerAdapter;
            if(mMapPlaceData.getId() == null) {
                picsPagerAdapter = new PicturesPagerAdapter(getActivity().getApplicationContext());
            } else {
                picsPagerAdapter = new PicturesPagerAdapter(getActivity().getApplicationContext(), mMapPlaceData.getId());
            }
            picsViewPager.setAdapter(picsPagerAdapter);
            picsPagerAdapter.notifyDataSetChanged();
            prevImg.setOnClickListener(onClickListener(PREV_BUTTON));
            nextImg.setOnClickListener(onClickListener(NEXT_BUTTON));
            addImage.setOnClickListener(onClickListener(ADD_IMAGE_BUTTON));

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
            urlTxt.setText(mMapPlaceData.getUrl());
            details.setText(mMapPlaceData.getDetail());
            detailsTxt.setText(mMapPlaceData.getDetail());
            createDate.setText(mMapPlaceData.getCreateDate());
            updateDate.setText(mMapPlaceData.getUpdateDate());
            editBtn.setOnClickListener(onClickListener(EDIT_BUTTON));
            updatePlaceInfo.setOnClickListener(onClickListener(UPDATE_BUTTON));
            deletePlaceInfo.setOnClickListener(onClickListener(DELETE_BUTTON));
            changeInfoUpdateStatus(false);
        }

    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case NEXT_BUTTON:
                        if (picsViewPager.getCurrentItem() < picsViewPager.getAdapter().getCount() - 1) {
                            picsViewPager.setCurrentItem(picsViewPager.getCurrentItem() + 1);
                        }
                        break;
                    case PREV_BUTTON:
                        if (picsViewPager.getCurrentItem() > 0) {
                            picsViewPager.setCurrentItem(picsViewPager.getCurrentItem() - 1);
                        }
                        break;
                    case ADD_IMAGE_BUTTON:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra("hoge", "foo");
                        startActivityForResult(intent, ADD_IMAGE_BUTTON_RESULT);

                        // TODO:
                        break;
                    case EDIT_BUTTON:
                        changeInfoUpdateStatus(true);
                        break;

                    case UPDATE_BUTTON:
                        updateData();
                        changeInfoUpdateStatus(false);
                        break;
                    case DELETE_BUTTON:
                        showRemoveDialog(v.getContext());
//
//                        deleteData();
//                        dismiss();
                        break;
                    default:
                        break;
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

        mMapUseCase.saveMapPlaceData(this.getActivity().getApplicationContext(), mMapPlaceData);
        // if it's new record, getting record.
        if(mMapPlaceData.getId() == null) {
            mMapPlaceData.setId(mMapUseCase.getLastInsertedMapData().getId());
        }
        mMapUseCase.saveMapPicData(this.getActivity().getApplicationContext(), mMapPlaceData);
        if(mUpdateMarker != null) {
            mUpdateMarker.updateMarker(mMapPlaceData);
        }

        setParams();
    }

    private void showRemoveDialog(final Context context) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this.getActivity().getApplicationContext());
        ad.setTitle("ALERT!!");
        ad.setMessage("DO YOU WANT TO DELETE THIS DIALOG?\nYOU CANNOT UNDO IT.");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "pressed OK", Toast.LENGTH_LONG).show();
                deleteData();
                dismiss();
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "pressed NO", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }

    private void deleteData() {
        Logger.error(TAG, "deleteData");
        // before remove, check dialog
        if(mMapPlaceData == null) {
            mUpdateMarker.deleteMarker();
        }

        if(mMapPlaceData.getId() == null) {
            mMapPlaceData = null;
            mUpdateMarker.deleteMarker();
        } else {
            mMapUseCase.deleteMapPlaceMarkerData(this.getActivity().getApplicationContext(), mMapPlaceData.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.debug(TAG, "onActivityResult!!");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_IMAGE_BUTTON_RESULT) {
            if (resultCode == RESULT_OK) {

                String realPath = "";
                if(data == null) {
                    Logger.error(TAG, "data is null");
                    return;
                }

                realPath = RealPathUtil.getRealPathFromURI_API19(this.getActivity().getApplicationContext(), data.getData());
                if(!TextUtils.isEmpty(realPath)) {
                    MapPlacePicData picData = new MapPlacePicData();
                    picData.setFilePath(realPath);
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS);
                        String date = sdf.format(new Date());
                        picData.setCreateDate(date);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    if(mMapPlaceData.getPicList() == null || mMapPlaceData.getPicList().isEmpty()) {
                        List<MapPlacePicData> list = new ArrayList<>();
                        list.add(picData);
                        mMapPlaceData.setPicList(list);
                    } else {
                        mMapPlaceData.getPicList().add(picData);
                    }
                }
                // TODO: 画像を一時表示
            }
        }
    }


    private void changeInfoUpdateStatus(boolean canEdit) {
        type.setEnabled(canEdit);
        typeDetail.setEnabled(canEdit);
        addImage.setEnabled(canEdit);

        if(canEdit) {
            title.setVisibility(View.VISIBLE);
            titleTxt.setVisibility(View.GONE);
            url.setVisibility(View.VISIBLE);
            urlTxt.setVisibility(View.GONE);
            details.setVisibility(View.VISIBLE);
            detailsTxt.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            updatePlaceInfo.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
            titleTxt.setVisibility(View.VISIBLE);
            url.setVisibility(View.GONE);
            urlTxt.setVisibility(View.VISIBLE);
            details.setVisibility(View.GONE);
            detailsTxt.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            updatePlaceInfo.setVisibility(View.GONE);
        }
    }

    interface UpdateMarker {
        void updateMarker(MapPlaceData mapPlaceData);

        void deleteMarker();
    }

}
