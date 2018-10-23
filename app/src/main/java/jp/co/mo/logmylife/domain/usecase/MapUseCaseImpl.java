package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.repository.MapRepository;

public class MapUseCaseImpl extends AbstractUseCase implements MapUseCase {

    private MapRepository mapRepository;

    @Override
    public List<MapPlaceData> getMapPlaceDatas(Context context) {
        if(mapRepository == null) {
            mapRepository = new MapRepository();
        }
        return mapRepository.getInfoWindowDatas(context);
    }

    @Override
    public void saveMapPlaceData(Context context, MapPlaceData placeData) {
        if(mapRepository == null) {
            mapRepository = new MapRepository();
        }
        mapRepository.saveInfoWindowData(context, placeData);
    }

    @Override
    public MapPlaceData getLastInsertedMapData() {
        if(mapRepository == null) {
            mapRepository = new MapRepository();
        }
        return mapRepository.getLastInsertedMapData();
    }

}
