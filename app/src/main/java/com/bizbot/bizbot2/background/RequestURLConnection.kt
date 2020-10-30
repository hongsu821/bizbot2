package com.bizbot.bizbot2.background

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

//var : 변수 , val : 상수 (읽기 전용 변수, 값이 변할 수 없는 변수)
class RequestURLConnection(var url: String) {

    fun DataLoad(): String?{

        var conn: HttpURLConnection? = null
        var line: String? = null

        try{
            val url1 = URL(url)
            conn = url1.openConnection() as HttpURLConnection

            val reader = BufferedReader(InputStreamReader(conn.inputStream,"UTF-8"))

            line = reader.readLine()

            return line

        }catch (e:MalformedURLException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }finally {
            conn?.disconnect()
        }

        return line
    }

}