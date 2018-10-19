package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;

public interface MapUseCase {

    public List<MapPlaceData> getMapPlaceDatas(Context context);

}
