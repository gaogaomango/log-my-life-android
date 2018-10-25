package jp.co.mo.logmylife.domain.entity.map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapPlacePicData {

    private int id;
    private int mapPlaceId;
    private String title;
    private String filePath;
    private String createDate;
    private String updateDate;

}
