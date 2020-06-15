package com.e_suratpermintaan.core.domain.pojos

class SuratPermintaanDataChange(var changeType: Int) {

    companion object {
        const val ITEM_EDITED = 0
        const val ITEM_DELETED = 1
        const val FILE_ITEM_EDITED = 2
        const val FILE_ITEM_DELETED = 3
        const val SP_DELETED = 4
        const val SP_EDITED = 5
    }

}