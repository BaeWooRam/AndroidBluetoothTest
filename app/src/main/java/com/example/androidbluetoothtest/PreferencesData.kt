package com.example.androidbluetoothtest

import android.content.Context

object PreferencesData{
    enum class Keys{
        IsShowPairedDevice
    }


    /**
     * 페어링 목록 보여주기 여부 Flag
     */
    fun getIsShowPairedDevicesPreferences(context: Context?):Boolean?{
        val preferencesName = context?.getString(R.string.app_name)
        val pref = context?.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return pref?.getBoolean(Keys.IsShowPairedDevice.name, false)
    }

    /**
     * 페어링 목록 보여주기 여부 Flag
     */
    fun setIsShowPairedDevicesPreferences(context: Context?, isShowPairedDevices:Boolean) {
        val preferencesName = context?.getString(R.string.app_name)
        val pref = context?.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putBoolean(Keys.IsShowPairedDevice.name, isShowPairedDevices)
        editor?.commit()
    }

    // 값(Key Data) 삭제하기
    fun removePreferences(context: Context?, keys: Keys) {
        val preferencesName = context?.getString(R.string.app_name)
        val pref = context?.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.remove(keys.name)
        editor?.commit()
    }

    // 값(ALL Data) 삭제하기
    fun removeAllPreferences(context: Context?) {
        val preferencesName = context?.getString(R.string.app_name)
        val pref = context?.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.clear()
        editor?.commit()
    }

}