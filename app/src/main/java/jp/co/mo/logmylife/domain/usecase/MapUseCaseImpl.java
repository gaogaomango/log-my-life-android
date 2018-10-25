package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;
import jp.co.mo.logmylife.domain.repository.MapRepository;

public class MapUseCaseImpl extends AbstractUseCase implements MapUseCase {

    private MapRepository mapRepository;

    public MapUseCaseImpl() {
        mapRepository = new MapRepository();
    }

    public MapUseCaseImpl(Context context) {
        mapRepository = new MapRepository(context);
    }

    @Override
    public List<MapPlaceData> getMapPlaceDatas(Context context) {
        return mapRepository.getInfoWindowDatas(context);
    }

    @Override
    public List<MapPlacePicData> getMapPlacePicDatas(Context context, Integer placeId) {
        return mapRepository.getMapPlacePicDatas(context, placeId);
    }


    @Override
    public void saveMapPlaceData(Context context, MapPlaceData placeData) {
        mapRepository.saveInfoWindowData(context, placeData);
    }

    @Override
    public void saveMapPicData(Context context, MapPlaceData placeData) {
        mapRepository.savePicData(context, placeData);
    }

    @Override
    public MapPlaceData getLastInsertedMapData() {
        return mapRepository.getLastInsertedMapData();
    }

}
