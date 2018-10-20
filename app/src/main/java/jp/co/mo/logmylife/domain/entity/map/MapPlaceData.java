package jp.co.mo.logmylife.domain.entity.map;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapPlaceData {

    private Integer id;
    private String userId;
    private String title;
    private List<MapPlacePicData> picList;
    private double lat;
    private double lng;
    private Integer typeId;
    private Integer typeDetailId;
    private String url;
    private String detail;
    private String createDate;
    private String updateDate;

}
