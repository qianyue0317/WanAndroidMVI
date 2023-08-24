package com.qianyue.wanandroidmvi.error

import com.qianyue.wanandroidmvi.constant.NetworkConstant

/**
 * @author QianYue
 * @since 2023/8/16
 */
class ResponseError(val errCode: Int = NetworkConstant.SUCCESS, val errMsg: String = "") : Exception(errMsg) {

}