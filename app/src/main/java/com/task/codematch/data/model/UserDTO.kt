package com.task.codematch.data.model

import androidx.room.Embedded
import com.task.codematch.data.source.local.entity.Address
import com.task.codematch.data.source.local.entity.Company

data class UserDTO(
    var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var phone: String = "",
    var website: String = "",
    var address: Address,
    var company: Company
)
