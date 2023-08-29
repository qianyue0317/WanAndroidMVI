package com.qianyue.wanandroidmvi.model.network

import com.qianyue.wanandroidmvi.model.bean.AppListData
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.BannerItem
import com.qianyue.wanandroidmvi.model.bean.ProjectCategory
import com.qianyue.wanandroidmvi.model.bean.ProjectItem
import com.qianyue.wanandroidmvi.model.bean.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author QianYue
 * @since 2023/8/15
 * @desc  请求接口
 */
interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L
    }

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") pwd: String
    ): AppResponse<UserInfo>


    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") userName: String,
        @Field("password") pwd: String,
        @Field("repassword") rpwd: String
    ): AppResponse<Unit>


    /**
     * pageSize null时采用默认的页大小
     */
    @GET("article/list/{pageIndex}/json")
    suspend fun getArticleList(
        @Path("pageIndex") pageIndex: Int,
        @Query("page_size") pageSize: Int? = null
    ): AppResponse<AppListData<ArticleItem>>

    /**
     * 获取banner
     */
    @GET("banner/json")
    suspend fun getBanner(): AppResponse<List<BannerItem>>

    /**
     * 获取置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticleList(): AppResponse<List<ArticleItem>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectCategories(): AppResponse<List<ProjectCategory>>

    /**
     * pageIndex 取值 【1-40】
     */
    @GET("project/list/{pageIndex}/json")
    suspend fun getProjectList(@Path("pageIndex")pageIndex: Int, @Query("cid") cid: Int): AppResponse<AppListData<ProjectItem>>

    /**
     * 获取广场页面文章列表数据
     */
    @GET("user_article/list/{pageIndex}/json")
    suspend fun getPlazaArticleList(
        @Path("pageIndex") pageIndex: Int,
        @Query("page_size") pageSize: Int? = null
    ): AppResponse<AppListData<ArticleItem>>
}