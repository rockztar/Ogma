package com.littlewitgames.theelderscrollsquiz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.AlignmentSpan;

import com.littlewitgames.theelderscrollsquiz.Models.StandardQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rasmus on 22-12-2014.
 */
public class QuestionsDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.ID,
            DatabaseHelper.QUESTION,
            DatabaseHelper.CORRECT_ANSWER,
            DatabaseHelper.WRONG_ANSWER_ONE,
            DatabaseHelper.WRONG_ANSWER_TWO,
            DatabaseHelper.WRONG_ANSWER_THREE,
            DatabaseHelper.CATEGORY
    };

    public QuestionsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public StandardQuestion createStandardQuestion(String question, String correct_answer, String wrong_answer_one, String wrong_answer_two, String wrong_answer_three, String category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.QUESTION, question);
        contentValues.put(DatabaseHelper.CORRECT_ANSWER, correct_answer);
        contentValues.put(DatabaseHelper.WRONG_ANSWER_ONE, wrong_answer_one);
        contentValues.put(DatabaseHelper.WRONG_ANSWER_TWO, wrong_answer_two);
        contentValues.put(DatabaseHelper.WRONG_ANSWER_THREE, wrong_answer_three);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        long insertId = database.insert(DatabaseHelper.TABLE, null, contentValues);
        Cursor cursor = database.query(DatabaseHelper.TABLE, allColumns, DatabaseHelper.ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        StandardQuestion newStandardQuestion = cursorToStandardQuestion(cursor);
        cursor.close();
        return  newStandardQuestion;
    }

    private StandardQuestion cursorToStandardQuestion(Cursor cursor) {
        StandardQuestion standardQuestion = new StandardQuestion();
        standardQuestion.setId(cursor.getLong(0));
        standardQuestion.setQuestion(cursor.getString(1));
        standardQuestion.setCorrect_answer(cursor.getString(2));
        standardQuestion.setWrong_answer_one(cursor.getString(3));
        standardQuestion.setWrong_answer_two(cursor.getString(4));
        standardQuestion.setWrong_answer_three(cursor.getString(5));
        standardQuestion.setCategory(cursor.getString(6));
        return standardQuestion;
    }

    public void deleteStandardQuestion(StandardQuestion standardQuestion) {
        long id = standardQuestion.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE, DatabaseHelper.ID + " = " + id, null);
    }

    public List<StandardQuestion> getAllStandardQuestions() {
        List<StandardQuestion> standardQuestions = new ArrayList<StandardQuestion>();

        Cursor cursor = database.query(DatabaseHelper.TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            StandardQuestion standardQuestion = cursorToStandardQuestion(cursor);
            standardQuestions.add(standardQuestion);
            cursor.moveToNext();
        }

        cursor.close();
        return standardQuestions;
    }
}
