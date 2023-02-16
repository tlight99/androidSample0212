package com.kyle.sample0212_2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Date
import android.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var btnCall:Button
    lateinit var edtCall:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // permission 작성
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG), Context.MODE_PRIVATE)

        btnCall = findViewById(R.id.btnCall)
        edtCall = findViewById(R.id.edtCall)

        btnCall.setOnClickListener {
            edtCall.setText(findCallHistory())
        }
    }

    fun findCallHistory() : String{
        var callSet = arrayOf(CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION)
        var c = contentResolver.query(CallLog.Calls.CONTENT_URI, callSet, null, null, null)

        // 만약에 통화 기록이 없으면 종료
        if (c!!.count == 0)
            return "통화 기록 없음"

        var callBuff = StringBuffer()
        callBuff.append("\n날짜 :  구분 :  전화번호 :  통화시간\n")
        c.moveToFirst()
        do{
            var callDate = c.getLong(0)
            var datepattern = SimpleDateFormat("yyyy-mm-dd")
            var date_str = datepattern.format(Date(callDate))
            callBuff.append("$date_str : ")
            if(c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                callBuff.append("착신 : ")
            else
                callBuff.append("발신 : ")
            callBuff.append(c.getString(2) + ":")
            callBuff.append(c.getString(3) + "초\n")
        }while (c.moveToNext())

        c.close()
        return callBuff.toString()
    }
}