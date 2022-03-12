package com.example.pmu_project.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.GeneralRepositoryService;
import com.example.pmu_project.service.QuestionRepositoryService;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryServiceImpl extends SQLiteOpenHelper implements QuestionRepositoryService, CurrentSessionRepositoryService, GeneralRepositoryService {

    private static final String dataFileName = "data.yaml";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "QUIZ_GAME";

    private static final String TABLE_NAME_RESULTS = "PREVIOUS_SCORES";
    private static final String TABLE_NAME_QUESTIONS = "ALL_QUESTIONS";
//    private static final String TABLE_NAME_PREVIOUS_SESSION = "PREVIOUS_SESSION";

    private static final String KEY_ID = "id";

    private static final String KEY_QUESTION = "question";
    private static final String KEY_QUESTION_ANSWER = "questionAnswer";
    private static final String KEY_QUESTION_USER_ANSWER = "questionUserAnswer";
//    private static final String KEY_QUESTION_USER_ANSWER = "questionUserAnswer";


    private static final String KEY_LAST_SESSION_CURRENT_QUESTION = "currentQuestion";
    private static final String KEY_LAST_SESSION_ALL_QUESTIONS = "allQuestions";
//    private static final String KEY_LAST_SESSION_CURRENT_QUESTION = "currentQuestion";


    public RepositoryServiceImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_RESULTS
                + "("
                + KEY_ID                    + " INTEGER PRIMARY KEY,"
                + KEY_QUESTION              + " TEXT NOT NULL UNIQUE,"
                + KEY_QUESTION_ANSWER       + " TEXT NOT NULL,"
                + KEY_QUESTION_USER_ANSWER  + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_QUESTIONS
                + "("
                + KEY_ID                    + " INTEGER PRIMARY KEY,"
                + KEY_QUESTION              + " TEXT NOT NULL UNIQUE,"
                + KEY_QUESTION_ANSWER       + " TEXT NOT NULL"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME_RESULTS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME_QUESTIONS + "'");
        onCreate(db);
    }


    public void LoadDataFromFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(dataFileName);
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        try {
            List<Question> questions = om.readValue(is, new TypeReference<List<Question>>(){});
            AddQuestions(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isQuestionAlreadyAnswered(String name){ //
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_RESULTS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
                KEY_QUESTION + "=?",
                new String[] {name},
                null, null, null,
                null
        );

        return cursor.moveToFirst();
    }

    public void AddAnsweredQuestions(HashMap<Question, String> questionsAndAnswers){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Map.Entry<Question, String> entry : questionsAndAnswers.entrySet()) {
            values.put(KEY_QUESTION, entry.getKey().getQuestion());
            values.put(KEY_QUESTION_ANSWER, entry.getKey().getAnswer());
            values.put(KEY_QUESTION_USER_ANSWER, entry.getValue());
            if (isQuestionAlreadyAnswered(entry.getKey().getQuestion())){
                db.update(TABLE_NAME_RESULTS, values, KEY_QUESTION + "= ?", new String[]{ entry.getKey().getQuestion()});
                continue;
            }
            db.insert(TABLE_NAME_RESULTS, null, values);
        }


        db.close();
    }

    public boolean SavedSession(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_RESULTS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
                KEY_QUESTION_USER_ANSWER + " IS NULL",
                null,
                null, null, null,
                null
        );

        return cursor.moveToFirst();
    }

    public int GetAnsweredQuestionsInt() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_RESULTS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
                KEY_QUESTION_USER_ANSWER + " IS NOT NULL",
                null,
                null, null, null,
                null
        );


        return cursor.getCount();
    }

    public int GetCorrectlyAnsweredQuestionsInt() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_RESULTS + " WHERE " + KEY_QUESTION_ANSWER + "=" + KEY_QUESTION_USER_ANSWER,
                                        new String[]{});


        return cursor.getCount();
    }

    public List<String> GetResultsRecordsString() throws EmptyDatabaseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_RESULTS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
//                KEY_QUESTION_USER_ANSWER + " IS NOT NULL",
                null,
                null, null, null,
                null
        );

        List<String> previousResults = new ArrayList<>();

        if (!cursor.moveToLast()){
            throw new EmptyDatabaseException("Database is empty!");
        }

       do{
           previousResults.add(cursor.getString(1) + ", отговор: " +
                   cursor.getString(2) + ",твоят отговор: " +
                   cursor.getString(3) + "\n");
       }while (cursor.moveToPrevious());

        return previousResults;
    }

    public void DeleteSavedResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_RESULTS,null,null);
        db.close();
//        return rowsDeleted;
    }

    public HashMap<Question, String> GetQuestionsAndAnswers() throws EmptyDatabaseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_RESULTS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER, KEY_QUESTION_USER_ANSWER},
                null,
                null,
                null, null, null,
                null
        );
        if (!cursor.moveToFirst()){
           throw new EmptyDatabaseException("Error occurred: Database is empty!");
        }

        HashMap<Question, String> questionsAndUserAnswers =  new HashMap<>();

        do{
            questionsAndUserAnswers.put(new Question(cursor.getString(1),cursor.getString(2)), cursor.getString(3));
        } while (cursor.moveToNext());

        return questionsAndUserAnswers;
    }

    public Question GetQuestion(int x) throws EmptyDatabaseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_QUESTIONS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER},
                KEY_ID + "=?",
                new String[] {String.valueOf(x)},
                null, null, null,
                null
        );
        if (!cursor.moveToFirst()){
            throw new EmptyDatabaseException("Database is empty!");
        }
        return new Question(cursor.getString(1),cursor.getString(2));
    }

    private void AddQuestions(List<Question> questions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Question q : questions){
            if (doQuestionExistInDb(q)){
                continue;
            }
            values.put(KEY_QUESTION, q.getQuestion());
            values.put(KEY_QUESTION_ANSWER, q.getAnswer());
            db.insert(TABLE_NAME_QUESTIONS, null, values);
        }

        db.close();
    }

    private boolean doQuestionExistInDb(Question question){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_QUESTIONS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER},
                KEY_QUESTION + "=?",
                new String[] {question.getQuestion()},
                null, null, null,
                null
        );
        return cursor.moveToFirst();
    }

    public int GetAllQuestionsInt(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_QUESTIONS,
                new String[] { KEY_ID, KEY_QUESTION, KEY_QUESTION_ANSWER},
                null,
                null,
                null, null, null,
                null
        );
        return cursor.getCount();
    }

}
