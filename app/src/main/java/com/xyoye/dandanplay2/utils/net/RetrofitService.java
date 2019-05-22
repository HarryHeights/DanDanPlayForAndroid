package com.xyoye.dandanplay2.utils.net;

import com.xyoye.dandanplay2.bean.BangumiBean;
import com.xyoye.dandanplay2.bean.AnimeDetailBean;
import com.xyoye.dandanplay2.bean.AnimeFavoriteBean;
import com.xyoye.dandanplay2.bean.AnimeTypeBean;
import com.xyoye.dandanplay2.bean.BannerBeans;
import com.xyoye.dandanplay2.bean.DanmuDownloadBean;
import com.xyoye.dandanplay2.bean.DanmuMatchBean;
import com.xyoye.dandanplay2.bean.DanmuSearchBean;
import com.xyoye.dandanplay2.bean.MagnetBean;
import com.xyoye.dandanplay2.bean.PersonalBean;
import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.bean.RegisterBean;
import com.xyoye.dandanplay2.bean.SeasonAnimeBean;
import com.xyoye.dandanplay2.bean.SubGroupBean;
import com.xyoye.dandanplay2.bean.UploadDanmuBean;
import com.xyoye.dandanplay2.bean.params.HistoryParam;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by YE on 2018/7/9.
 */


public interface RetrofitService {

    @FormUrlEncoded
    @POST("api/v2/match")
    Observable<DanmuMatchBean> matchDanmu(@FieldMap Map<String, String> params);

    @GET("/api/v2/search/episodes")
    Observable<DanmuSearchBean> searchDanmu(@Query("anime") String anime, @Query("episode") String episode);

    @FormUrlEncoded
    @POST("api/v2/comment/{episodeId}")
    Observable<UploadDanmuBean> uploadDanmu(@FieldMap Map<String, String> params, @Path("episodeId") String episodeId);

    @GET("api/v2/comment/{episodeId}?withRelated=true")
    Observable<DanmuDownloadBean> downloadDanmu(@Path("episodeId") String episodeId);

    @GET("api/v2/homepage/banner")
    Observable<BannerBeans> getBanner();

    @GET("api/v2/bangumi/shin")
    Observable<BangumiBean> getAnimes();

    @GET("api/v2/bangumi/{animeId}")
    Observable<AnimeDetailBean> getAnimaDetail(@Path("animeId") String animaId);

    @FormUrlEncoded
    @POST("api/v2/login")
    Observable<PersonalBean> login(@FieldMap Map<String, String> params);

    @GET("api/v2/login/renew")
    Observable<PersonalBean> reToken();

    @GET("api/v2/favorite")
    Observable<AnimeFavoriteBean> getFavorite();

    @GET("/api/v2/playhistory")
    Observable<PlayHistoryBean> getPlayHistory();

    @FormUrlEncoded
    @POST("/api/v2/favorite")
    Observable<CommJsonEntity> addFavorite(@FieldMap Map<String, String> params);

    @DELETE("/api/v2/favorite/{animeId}")
    Observable<CommJsonEntity> reduceFavorite(@Path("animeId") String animaId);

    @FormUrlEncoded
    @POST("/api/v2/register")
    Observable<RegisterBean> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/user/password")
    Observable<CommJsonEntity> changePassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/register/resetpassword")
    Observable<CommJsonEntity> resetPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/v2/user/profile")
    Observable<CommJsonEntity> changeScreenName(@Field("screenName") String screenName);

    @GET("/list")
    Observable<MagnetBean> searchMagnet(@Query("keyword") String keyword, @Query("type") String typeId, @Query("subgroup") String subGroupId);

    @GET("/type")
    Observable<AnimeTypeBean> getAnimeType();

    @GET("/subgroup")
    Observable<SubGroupBean> getSubGroup();

    @POST("/Magnet/Parse")
    Observable<ResponseBody> downloadTorrent(@Body RequestBody requestBody);

    @POST("/api/v2/playhistory")
    Observable<CommJsonEntity> addPlayHistory(@Body HistoryParam params);

    @GET("/api/v2/bangumi/season/anime")
    Observable<SeasonAnimeBean> getAnimeSeason();

    @GET("/api/v2/bangumi/season/anime/{year}/{month}")
    Observable<BangumiBean> getSeasonAnime(@Path("year") String year, @Path("month") String month);

    @FormUrlEncoded
    @Headers({"query:shooter"})
    @POST("/api/subapi.php")
    Observable<ResponseBody> queryShooter(@FieldMap Map<String, String> map);

    @Headers({"query:thunder"})
    @GET("/subxl/{videoHash}.json")
    Observable<ResponseBody> queryThunder(@Path("videoHash") String videoHash);

    @GET("/{@link}")
    Observable<ResponseBody> downloadSubtitle(@Path("link") String link);
}
