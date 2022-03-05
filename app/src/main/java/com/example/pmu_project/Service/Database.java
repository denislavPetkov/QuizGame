package com.example.pmu_project.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.IService.IResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

public class Database extends SQLiteOpenHelper implements IDatabase {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "QUIZ_GAME";

    private static final String TABLE_NAME = "PREVIOUS_SCORES";

    private static final String KEY_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_QUESTION_ANSWER = "questionAnswer";
    private static final String KEY_QUESTION_USER_ANSWER = "questionUserAnswer";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public void createTableIfNotExist() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
//                + "("
//                + KEY_ID                    + " INTEGER PRIMARY KEY,"
//                + KEY_QUESTION              + " TEXT,"
//                + KEY_QUESTION_ANSWER       + " TEXT,"
//                + KEY_QUESTION_USER_ANSWER  + " TEXT"
//                + ")";
//        db.execSQL(CREATE_CONTACTS_TABLE);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "("
                + KEY_ID                    + " INTEGER PRIMARY KEY,"
                + KEY_QUESTION              + " TEXT,"
                + KEY_QUESTION_ANSWER       + " TEXT,"
                + KEY_QUESTION_USER_ANSWER  + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        // Повторно създаване на базата от данни
        onCreate(db);
    }

//    public int onDeleteTable(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        int rowsDeleted = db.delete(TABLE_NAME,null,null);
//        db.close();
//        return rowsDeleted;
//    }

    public void AddResultRecord (IResults results){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Question q : results.GetCorrectlyAnsweredQuestions()){
            values.put(KEY_QUESTION, q.getQuestion());
            values.put(KEY_QUESTION_ANSWER, q.getAnswer());
            values.put(KEY_QUESTION_USER_ANSWER, q.getAnswer());
            db.insert(TABLE_NAME, null, values);
        }

        Map<Question,String> wronglyAnsweredQuestions = results.GetWronglyAnsweredQuestions();
        for (Map.Entry<Question, String> entry : wronglyAnsweredQuestions.entrySet()) {
            values.put(KEY_QUESTION, entry.getKey().getQuestion());
            values.put(KEY_QUESTION_ANSWER, entry.getKey().getAnswer());
            values.put(KEY_QUESTION_USER_ANSWER, entry.getValue());
            db.insert(TABLE_NAME, null, values);
        }

        // the row ID of the newly inserted row, or -1 if an error occurred
        db.close(); // Затравяне на връзката с базата от данни
    }

    public List<String> GetResultsRecordsString(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
                null,
                null,
                null, null, null,
                null
        );

        List<String> previousResults = new ArrayList<>();

        while (cursor.moveToNext()) {
            previousResults.add(cursor.getString(1) + ": " +
                            cursor.getString(2) + ",твоят отговор: " +
                            cursor.getString(3) + "\n");
        }
        return previousResults;
    }

    public void DeleteSavedResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME,null,null);
        db.close();
//        return rowsDeleted;
    }

}
