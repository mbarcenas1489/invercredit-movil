package com.example.creditosappandroidx.cswebservice

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.preference.PreferenceManager
import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

object network
{
     fun getLocalIpAddress(context: Context): String? {
        try {

            val wifiManager: WifiManager = context.getApplicationContext()?.getSystemService(
                WIFI_SERVICE
            ) as WifiManager

            return ipToString(wifiManager.connectionInfo.ipAddress)
        } catch (ex: Exception) {
            Log.e("direccion", ex.toString())
        }

        return null
    }
    private fun ipToString(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)

    }

    fun validIPLocal(context: Context): Boolean {
        var ip = getLocalIpAddress(context)
        if (ip == null || ip.isEmpty()) return false
        ip = ip.trim { it <= ' ' }
        return if ((ip.length < 6) and (ip.length > 15)) false else try
        {
            val pattern: Pattern =
                Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
            val matcher: Matcher = pattern.matcher(ip)
            matcher.matches()
        }
        catch (ex: PatternSyntaxException)
        {
            false
        }
    }
    fun validIPServer(context: Context): Boolean {

       var pref = PreferenceManager
            .getDefaultSharedPreferences(context)
        var ip: String? = pref.getString("ip", "192.168.1")
        //var ip = getLocalIpAddress(context)
        if (ip == null || ip.isEmpty()) return false
        ip = ip.trim { it <= ' ' }
        return if ((ip.length < 6) and (ip.length > 15)) false else try
        {
            val pattern: Pattern =
                Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
            val matcher: Matcher = pattern.matcher(ip)
            matcher.matches()
        }
        catch (ex: PatternSyntaxException)
        {
            false
        }
    }


}