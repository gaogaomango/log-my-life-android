package jp.co.mo.logmylife.common.enums;

import android.text.TextUtils;

public enum MapDataType {

    RESTAURANT(1, "Restaurant"),
    SUPERMARKET(2, "Super Market"),
    AMUSEMENT_PARK(3, "Amusement Park"),
    CLUB(4, "Club"),
    CAFE(5, "Cafe"),
    MUSEUM(6, "Museum"),
    NIGHT(7, "Night"),
    CHEMIST(8, "Chemist"),
    LIBRARY(9, "LIBRARY"),
    CONVINIENCE_STORE(10, "Convinience Store"),
    GAS_STATION(11, "Gas Station"),
    GAME_CENTER(12, "Game center"),
    HOUSE(13, "House"),
    FRIENDS(14, "Friends")
    ;

    private int id;
    private String type;

    MapDataType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId(){
        return this.id;
    }

    public String getType(){
        return this.type;
    }

    public static MapDataType getById(int id) {
        for(MapDataType r : MapDataType.values()) {
            if(id == r.getId()) {
                return r;
            }
        }
        return null;
    }

    public static MapDataType getByType(String type) {
        if(TextUtils.isEmpty(type)) {
            return null;
        }
        for(MapDataType r : MapDataType.values()) {
            if(type.equals(r.getType())) {
                return r;
            }
        }
        return null;
    }

}
