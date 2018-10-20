package jp.co.mo.logmylife.common.enums;

import android.text.TextUtils;

public enum RestaurantType {

    NO_VALUE(-1, ""),
    JAPANESE(1, "JAPANESE"),
    CHINESE(2, "CHINESE"),
    KOREAN(3, "KOREAN"),
    ITALIAN(4, "ITALIAN"),
    FRENCH(5, "FRENCH"),
    MEXICAN(6, "MEXICAN"),
    THAI_FOOD(7, "THAI");

    private Integer id;
    private String type;

    RestaurantType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId(){
        return this.id;
    }

    public String getType(){
        return this.type;
    }

    public static RestaurantType getById(Integer id) {
        for(RestaurantType r : RestaurantType.values()) {
            if(id.intValue() == r.getId().intValue()) {
                return r;
            }
        }
        return NO_VALUE;
    }

    public static RestaurantType getByType(String type) {
        if(TextUtils.isEmpty(type)) {
            return null;
        }
        for(RestaurantType r : RestaurantType.values()) {
            if(type.equals(r.getType())) {
                return r;
            }
        }
        return NO_VALUE;
    }

}
