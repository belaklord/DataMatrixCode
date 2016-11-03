package com.example.pelu.qrcode;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;


public class DataBase {




    public static String DatosTabla(SQLiteDatabase db, String nombreTabla){

        Cursor cursor = db.rawQuery("SELECT* FROM " + nombreTabla, null);
        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }

                resultSet.put(rowObject);
                cursor.moveToNext();
            }


        }

        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());


        return resultSet.toString();
    }
}
