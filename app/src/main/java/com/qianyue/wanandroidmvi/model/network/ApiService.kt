package com.qianyue.wanandroidmvi.model.network

import com.qianyue.wanandroidmvi.model.bean.AppListData
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.model.bean.BannerItem
import com.qianyue.wanandroidmvi.model.bean.KeyWord
import com.qianyue.wanandroidmvi.model.bean.ProjectCategory
import com.qianyue.wanandroidmvi.model.bean.ProjectItem
import com.qianyue.wanandroidmvi.model.bean.SharedData
import com.qianyue.wanandroidmvi.model.bean.TodoItem
import com.qianyue.wanandroidmvi.model.bean.UserCoin
import com.qianyue.wanandroidmvi.model.bean.UserInfo
import com.qianyue.wanandroidmvi.model.bean.WebAddressItem
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

    @GET("user/logout/json")
    suspend fun logout(): AppResponse<Any?>

    /**
     * 获取用户积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getCoinCount(): AppResponse<UserCoin>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") userName: String,
        @Field("password") pwd: String,
        @Field("repassword") rpwd: String
    ): AppResponse<UserInfo>


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
    suspend fun getProjectList(
        @Path("pageIndex") pageIndex: Int,
        @Query("cid") cid: Int
    ): AppResponse<AppListData<ProjectItem>>

    /**
     * 获取广场页面文章列表数据
     */
    @GET("user_article/list/{pageIndex}/json")
    suspend fun getPlazaArticleList(
        @Path("pageIndex") pageIndex: Int,
        @Query("page_size") pageSize: Int? = null
    ): AppResponse<AppListData<ArticleItem>>


    /**
     * 获取收藏的文章列表
     */
    @GET("lg/collect/list/{pageIndex}/json")
    suspend fun getCollectedArticleList(
        @Path("pageIndex") pageIndex: Int,
        @Query("page_size") pageSize: Int? = null,
    ): AppResponse<AppListData<ArticleItem>>

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{articleId}/json")
    suspend fun collectArticle(@Path("articleId") articleId: Int): AppResponse<Any?>

    /**
     * 取消收藏
     *
     */
    @Suppress("SpellCheckingInspection")
    @POST("lg/uncollect_originId/{articleId}/json")
    suspend fun uncollectArticle(@Path("articleId") articleId: Int): AppResponse<Any?>

    @GET("lg/collect/usertools/json")
    suspend fun getCollectedWebAddressList(): AppResponse<List<WebAddressItem>>

    @FormUrlEncoded
    @Suppress("SpellCheckingInspection")
    @POST("lg/collect/deletetool/json")
    suspend fun uncollectTool(@Field("id") id: Int): AppResponse<Any?>

    @GET("user/lg/private_articles/{pageIndex}/json")
    suspend fun getMySharedArticleList(@Path("pageIndex") pageIndex: Int): AppResponse<SharedData>

    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): AppResponse<Any?>

    @POST("lg/user_article/delete/{articleId}/json")
    suspend fun deleteSharedArticle(@Path("articleId") id: Int): AppResponse<Any?>

    @GET("lg/todo/v2/list/{pageIndex}/json")
    suspend fun getTodoList(@Path("pageIndex") index: Int): AppResponse<AppListData<TodoItem>>

    @GET("hotkey/json")
    suspend fun getHotKeyWords(): AppResponse<List<KeyWord>>

    @FormUrlEncoded
    @POST("article/query/{pageIndex}/json")
    suspend fun searchForKeyWord(
        @Path("pageIndex") index: Int,
        @Field("k") keyword: String
    ): AppResponse<AppListData<ArticleItem>>

    @FormUrlEncoded
    @POST("lg/todo/add/json")
    suspend fun addTodoItem(
        @Field("title") title: String,
        @Field("content") desc: String,
        @Field("date") date: String?,
        @Field("type") type: Int = 1,
        @Field("priority") priority: Int = 1
    ): AppResponse<Any?>

    @POST("lg/todo/delete/{todoId}/json")
    suspend fun deleteTodo(@Path("todoId") id: Int): AppResponse<Any?>

    @FormUrlEncoded
    @POST("lg/todo/update/{todoId}/json")
    suspend fun updateTodo(
        @Path("todoId") id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("status") status: Int,
        @Field("type") type: Int,
        @Field("priority") level: Int
    ): AppResponse<Any?>

    @FormUrlEncoded
    @POST("lg/todo/done/{todoId}/json")
    suspend fun updateStatus(
        @Path("todoId") id: Int,
        @Field("status") status: Int/* 0或1,1代表完成，0未完成 */
    ): AppResponse<Any?>
}