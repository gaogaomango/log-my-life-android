package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.repository.MapRepository;

public class MapUseCaseImpl extends AbstractUseCase implements MapUseCase {

    private MapRepository mapRepository;

    @Override
    public List<MapPlaceData> getMapPlaceDatas(Context context) {
        mapRepository = new MapRepository();
        return mapRepository.getInfoWindowDatas(context);
    }

}
