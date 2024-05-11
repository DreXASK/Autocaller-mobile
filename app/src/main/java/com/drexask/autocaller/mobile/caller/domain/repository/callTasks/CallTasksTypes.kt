package com.drexask.autocaller.mobile.caller.domain.repository.callTasks
sealed interface CallTasksTypes {
    sealed interface Parameter {
        interface Get: Parameter
        interface Send: Parameter
    }
    interface Response
}