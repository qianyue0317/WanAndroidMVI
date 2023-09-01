package com.qianyue.wanandroidmvi.utils

import android.os.Bundle
import java.lang.Exception
import java.lang.reflect.Field

/**
 * @author QianYue
 * @since 2023/8/29
 */
class MessageCodec {

    companion object {
        const val TAG = "MessageCodec"


        const val NULL_OBJ = "__NULL__"

        const val FIELD_PREFIX = "__FIELD_PREFIX__"

        const val FIELD_ARRAY_PREFIX = "__FIELD_ARRAY_PREFIX__"

        const val ARRAY_ITEM_PREFIX = "__ARRAY_ITEM_PREFIX__"
    }

    fun encode(resultBundle: Bundle, obj: Any?) {
        if (obj == null) return

        val javaClass = obj.javaClass

        if (javaClass == Any::class.java) return
        javaClass.declaredFields.forEach {
            encodeField(resultBundle, it, obj)
        }
    }

    private fun encodeField(bundle: Bundle, field: Field, obj: Any?) {
        val accessible = field.isAccessible
        field.isAccessible = true
        try {
            when {
                field.type == Int::class.java -> {
                    bundle.putInt("${FIELD_PREFIX}${field.name}", field.getInt(obj))
                }

                field.type == Byte::class.java -> {
                    bundle.putByte("${FIELD_PREFIX}${field.name}", field.getByte(obj))
                }

                field.type == Short::class.java -> {
                    bundle.putShort("${FIELD_PREFIX}${field.name}", field.getShort(obj))
                }

                field.type == Long::class.java -> {
                    bundle.putLong("${FIELD_PREFIX}${field.name}", field.getLong(obj))
                }

                field.type == String::class.java -> {
                    bundle.putString("${FIELD_PREFIX}${field.name}", field.get(obj) as String?)
                }

                field.type == Array::class.java -> {
                    val arrayValue = field.get(obj) as Array<*>?
                    val arrayBundle: Bundle? = arrayValue?.let { Bundle() }
                    arrayValue?.forEachIndexed { index, arrayItem ->
                        val tempBundle = arrayBundle!!

                    }
                    bundle.putBundle("${FIELD_ARRAY_PREFIX}${field.name}", arrayBundle)
                }

                field.type.isAssignableFrom(List::class.java) -> {

                }

                field.type.isAssignableFrom(Set::class.java) -> {

                }

                field.type.isAssignableFrom(Map::class.java) -> {

                }
            }
        } catch (e: Exception) {
            WanLog.e(TAG, "encodeError: ${e.message}")
        } finally {
            field.isAccessible = accessible
        }
    }


    private fun encodeArray(bundle: Bundle, obj: Any?) {

    }
}