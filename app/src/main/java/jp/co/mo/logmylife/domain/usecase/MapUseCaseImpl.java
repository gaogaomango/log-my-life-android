package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;
import jp.co.mo.logmylife.domain.repository.MapRepository;

public class MapUseCaseImpl extends AbstractUseCase implements MapUseCase {

    private MapRepository mMapRepository;

    public MapUseCaseImpl() {
        mMapRepository = new MapRepository();
    }

    public MapUseCaseImpl(Context context) {
        mMapRepository = new MapRepository(context);
    }

    @Override
    public List<MapPlaceData> getMapPlaceDatas(Context context) {
        return mMapRepository.getInfoWindowDatas(context);
    }

    @Override
    public List<MapPlacePicData> getMapPlacePicDatas(Context context, Integer placeId) {
        return mMapRepository.getMapPlacePicDatas(context, placeId);
    }


    @Override
    public void saveMapPlaceData(Context context, MapPlaceData placeData) {
        mMapRepository.saveInfoWindowData(context, placeData);
    }

    @Override
    public void saveMapPicData(Context context, MapPlaceData placeData) {
        mMapRepository.savePicData(context, placeData);
    }

    public void deleteMapPlaceMarkerData(Context context, Integer placeId) {
        deleteMapPlaceData(context, placeId);
        deleteMapPicData(context, placeId);
    }

    @Override
    public void deleteMapPlaceData(Context context, Integer placeId) {
        mMapRepository.deleteMapPlaceData(context, placeId);
    }

    @Override
    public void deleteMapPicData(Context context, Integer placeId) {
        mMapRepository.deleteMapPicData(context, placeId);
    }

    @Override
    public MapPlaceData getLastInsertedMapData() {
        return mMapRepository.getLastInsertedMapData();
    }

}
