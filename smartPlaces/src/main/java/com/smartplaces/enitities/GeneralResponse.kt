package com.smartplaces.enitities

import androidx.annotation.Keep

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

@Keep
internal class GeneralResponse(

    var msg: String,
    var sms_code: String,
    var status: String
)