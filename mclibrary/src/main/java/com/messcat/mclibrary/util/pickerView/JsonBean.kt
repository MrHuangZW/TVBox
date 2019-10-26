package com.messcat.mclibrary.util.pickerView

import com.bigkoo.pickerview.model.IPickerViewData

/**
 * 城市三级联动实体类
 */
data class JsonBean(var text: String?, var value: String?, var children: List<ChildrenBeanX>?) : IPickerViewData {

    override fun getPickerViewText(): String? {
        return this.text
    }

    data class ChildrenBeanX(var text: String?, var value: String?, var children: List<ChildrenBean>?) {
        data class ChildrenBean(var text: String?, var value: String?)
    }
}
