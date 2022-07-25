package com.example.guruapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.YELLOW
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.guruapp.databinding.ActivityMainBinding
import com.nvt.color.ColorPickerDialog
import java.util.*


class MainActivity : AppCompatActivity() {

    var selectedDate: Date? = null

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var EdtSchedulName : EditText
    lateinit var Colorr : Button
    lateinit var BtnColorSelect : Button
    lateinit var BtnDate : Button
    lateinit var SpinnerStartHour : Spinner
    lateinit var SpinnerStartMinute : Spinner
    lateinit var SpinnerEndHour : Spinner
    lateinit var SpinnerEndMinute : Spinner
    lateinit var EdtPlace : EditText
    lateinit var EdtMemo : EditText
    lateinit var BtnInsert : Button



    lateinit var binding : ActivityMainBinding

    private var mMaterialColorCircle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        dbManager = DBManager(this, "schedule", null, 1)

        EdtSchedulName = findViewById(R.id.EdtScheduleName)
        Colorr = findViewById(R.id.Color)
        BtnColorSelect = findViewById(R.id.BtnColorSelect)
        BtnDate = findViewById(R.id.BtnDate)
        SpinnerStartHour = findViewById(R.id.SpinnerStartHour)
        SpinnerStartMinute = findViewById(R.id.SpinnerStartMinute)
        SpinnerEndHour = findViewById(R.id.SpinnerEndHour)
        SpinnerEndMinute = findViewById(R.id.SpinnerEndMinute)
        EdtPlace = findViewById(R.id.EdtPlace)
        EdtMemo = findViewById(R.id.EdtMemo)
        BtnInsert = findViewById(R.id.BtnInsert)


        EdtSchedulName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length > 0) {
                    BtnInsert.setClickable(true)
                    BtnInsert.isEnabled = true
                } else {
                    BtnInsert.setClickable(false)
                    BtnInsert.isEnabled = false
                }
            }
        })

        BtnColorSelect.setOnClickListener {
            val colorPicker = ColorPickerDialog(
            this,
                BLACK, // color init
            true, // true is show alpha
            object : ColorPickerDialog.OnColorPickerListener {
                override fun onCancel(dialog: ColorPickerDialog?) {
                    // handle click button Cancel
                }

                override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                    // handle click button OK
                    Colorr.setBackgroundColor(colorPicker)
                }
            })
            colorPicker.show()
        }



        BtnDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    BtnDate.setText("${year}년 ${month + 1}월 ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()
        }

        val Adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.hour_list,
            android.R.layout.simple_spinner_dropdown_item
        )
        val Adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.minute_list,
            android.R.layout.simple_spinner_dropdown_item
        )

        SpinnerStartHour.setAdapter(Adapter1)
        SpinnerStartMinute.setAdapter(Adapter2)
        SpinnerEndHour.setAdapter(Adapter1)
        SpinnerEndMinute.setAdapter(Adapter2)

        BtnInsert.setOnClickListener {

            var str_sname : String = EdtSchedulName.text.toString()
            var str_color: String = Colorr.background.toString()
            var str_date: String = BtnDate.text.toString()
            var str_startHour: String = SpinnerStartHour.selectedItem.toString()
            var str_startMinute: String = SpinnerStartMinute.selectedItem.toString()
            var str_endHour: String = SpinnerEndHour.selectedItem.toString()
            var str_endMinute: String = SpinnerEndMinute.selectedItem.toString()
            var str_splce: String = EdtPlace.text.toString()
            var str_smemo: String = EdtMemo.text.toString()


            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO schedule VALUES ('" + str_sname + "' , '" + str_color + "', '"
                    + str_date + "', '"+ str_startHour + "' ,'" + str_startMinute + "', '"
                    + str_endHour + "' , '" + str_endMinute + "', '"+ str_splce+ "', '" + str_smemo + "');")
            sqlitedb.close()

            val intent = Intent(this, HomeActivity::class.java)
            Toast.makeText(applicationContext, "입력됨 ${str_color}", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }


    }
    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "scheduleDB", null, 1){
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("CREATE TABLE schedule ( Sname text, Scolor text, Sdate text, SShour text, SSminute text, SEhour text, SEminute text, Splace text, Smemo text);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS groupTBL")
        }
    }

}