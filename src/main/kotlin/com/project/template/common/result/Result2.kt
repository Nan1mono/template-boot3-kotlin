package com.project.template.com.project.template.common.result

sealed class Result2<T> {

    abstract val code: Int
    abstract val message: String
    abstract val data: T?

    data class Success<T>(
        override val code: Int = ResultCodeEnum.SUCCESS.code,
        override val message: String = ResultCodeEnum.SUCCESS.message,
        override val data: T?
    ) : Result2<T>()

    data class Fail<T>(
        override val code: Int = ResultCodeEnum.FAIL.code,
        override val message: String = ResultCodeEnum.FAIL.message,
        override val data: T?
    ) : Result2<T>()

    data class Unidentified<T>(
        override val code: Int = ResultCodeEnum.UNKNOWN_ERROR.code,
        override val message: String = ResultCodeEnum.UNKNOWN_ERROR.message,
        override val data: T?
    ) : Result2<T>()

    companion object {
        fun <T> build(data: T?, resultCodeEnum: ResultCodeEnum): Result2<T> {
            return when (resultCodeEnum) {
                ResultCodeEnum.SUCCESS -> Success(data = data)
                ResultCodeEnum.FAIL -> Fail(data = data)
                else -> Unidentified(resultCodeEnum.code, resultCodeEnum.message, data)
            }
        }

        fun <T> success(data: T?): Result2<T> {
            return Success(data = data)
        }

        fun <T> fail(data: T?): Result2<T> {
            return Fail(data = data)
        }

        fun <T> fail(code: Int, message: String, data: T?): Result2<T> {
            return Fail(code = code, message = message, data = data)
        }

        fun <T> fail(code: Int, message: String): Result2<T> {
            return Fail(code = code, message = message, data = null)
        }

        fun <T> fail(message: String): Result2<T> {
            return Fail(code = ResultCodeEnum.FAIL.code, message = message, data = null)
        }
    }

}