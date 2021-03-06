package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;

public interface MapUseCase {

    public List<MapPlaceData> getMapPlaceDatas(Context context);

    public List<MapPlacePicData> getMapPlacePicDatas(Context context, Integer placeId);

    public void saveMapPlaceData(Context context, MapPlaceData placeData);

    public void saveMapPicData(Context context, MapPlaceData placeData);

    public void deleteMapPlaceData(Context context, Integer placeId);

    public void deleteMapPicData(Context context, Integer placeId);

    public MapPlaceData getLastInsertedMapData();

}
