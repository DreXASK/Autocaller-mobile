package com.drexask.autocaller.mobile.caller.data.utils

import com.drexask.autocaller.mobile.core.data.utils.CoreHttpRoutes

object CallerHttpRoutes {

    val getCallTask
        get() = "${CoreHttpRoutes.baseUrl}/get_another_call_task"

    val sendResult
        get() = "${CoreHttpRoutes.baseUrl}/send_call_result_to_server"

}