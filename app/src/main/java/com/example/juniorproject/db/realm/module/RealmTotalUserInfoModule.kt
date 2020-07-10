package com.example.juniorproject.db.realm.module

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class RealmTotalUserInfoModule : RealmObject() {

    @PrimaryKey var idx: Int = 0
    var type: String? = ""
    var error: String? = ""
    var estimated_data: String? = ""
    var original_data: String? = ""
    var reliability: String? = ""

}