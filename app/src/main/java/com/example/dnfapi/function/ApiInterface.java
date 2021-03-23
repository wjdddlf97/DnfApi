package com.example.dnfapi.function;

import com.example.dnfapi.function.VOS.ApiForm;
import com.example.dnfapi.function.VOS.ApiItemForm;
import com.example.dnfapi.function.VOS.ApiSetItemForm;
import com.example.dnfapi.function.VOS.ApiStatForm;
import com.example.dnfapi.function.VOS.CharacterDetailListVO;
import com.example.dnfapi.function.VOS.ItemInfo;
import com.example.dnfapi.function.VOS.SetItemInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // @GET( EndPoint-자원위치(URI) )
    @GET("characters?"+"&apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")          //캐릭터이름으로 정보받아오기
    Call<ApiForm> getPosts(@Query("characterName") String characterName);

    @GET("{characterId}"+"?apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")        //아이디로 정보받아오기
    Call<CharacterDetailListVO> getDetailCharacter(@Path("characterId") String characterId);

    @GET("{characterId}/status"+"?apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")        //캐릭터 스탯에 대한 정보 받아오기
    Call<ApiStatForm> getDetailCharacterStat(@Path("characterId") String characterId);

    @GET("{characterId}/equip/equipment"+"?apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")        //캐릭터 착용장비
    Call<ApiSetItemForm> getDetailCharacterItem(@Path("characterId") String characterId);

    @GET("items?" + "&apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")           //아이템 검색 ( 사전에서 )
    Call<ApiItemForm> getSearchItem(@Query("itemName") String itemName);

    @GET("auction?" + "&apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")           //경매장의 평균 시세/ 이름 / id 검색
    Call<ApiItemForm> getItemPrice(@Query("itemName") String itemName);

    @GET("{itemId}"+"?apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")           //id 로 아이템 상세 정보 검색
    Call<ItemInfo> getItemDetailInfo(@Path("itemId") String itemId);

    @GET("{setItemId}"+"?apikey=SngTBISFyj3WmHO1EUuGUQJGIu9v1wv8")           //setItemId 로 아이템 장착부위 정보 검색
    Call<SetItemInfo> getSetItemDetailInfo(@Path("setItemId") String itemId);

}
