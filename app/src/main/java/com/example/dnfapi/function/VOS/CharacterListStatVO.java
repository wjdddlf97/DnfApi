package com.example.dnfapi.function.VOS;

import com.google.gson.annotations.SerializedName;

/**
 * HP, MP, 물리방어, 마법방어, 힘, 지능, 체력, 정신력, 물리 공격력, 마법 공격력
 * 물리 크리티컬, 마법 크리티컬, 독립공격력, 공격속도, 캐스팅속도, 이동속도, 항마, 적중률, 회피율, HP회복량
 * MP회복량, 경직도, 히트리커버리, 화속성 강화, 화속성 저항, 수속성 강화, 수속성 저항, 명속성 강화, 명속성 저항, 암속성 강화
 * 암속성 저항
 */
public class CharacterListStatVO {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
