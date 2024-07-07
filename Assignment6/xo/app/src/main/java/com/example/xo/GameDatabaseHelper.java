package com.example.xo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log; // Import statement for Log

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xo_game.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GAME_STATE = "game_state";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BOARD = "board";
    private static final String COLUMN_PLAYER_X_TURN = "player_x_turn";
    private static final String COLUMN_PLAY_WITH_CPU = "play_with_cpu";
    private static final String COLUMN_PLAYER_SYMBOL = "player_symbol";
    private static final String COLUMN_RESULT = "result";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_GAME_STATE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BOARD + " TEXT,"
                + COLUMN_PLAYER_X_TURN + " INTEGER,"
                + COLUMN_PLAY_WITH_CPU + " INTEGER,"
                + COLUMN_PLAYER_SYMBOL + " TEXT,"
                + COLUMN_RESULT + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_STATE);
        onCreate(db);
    }

    public void saveGameState(String board, boolean playerXTurn, boolean playWithCPU, String playerSymbol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARD, board);
        values.put(COLUMN_PLAYER_X_TURN, playerXTurn ? 1 : 0);
        values.put(COLUMN_PLAY_WITH_CPU, playWithCPU ? 1 : 0);
        values.put(COLUMN_PLAYER_SYMBOL, playerSymbol);
        values.put(COLUMN_RESULT, "ongoing");

        db.insert(TABLE_GAME_STATE, null, values);
        db.close();
    }

    public void saveGameResult(String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT, result);

        String whereClause = COLUMN_RESULT + " = ?";
        String[] whereArgs = new String[]{"ongoing"};
        db.update(TABLE_GAME_STATE, values, whereClause, whereArgs);
        db.close();
    }

    public Cursor loadGameState() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_GAME_STATE + " WHERE " + COLUMN_RESULT + " = 'ongoing' ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        return db.rawQuery(query, null);
    }

    public void clearGameState() {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_RESULT + " = ?";
        String[] whereArgs = new String[]{"ongoing"};
        db.delete(TABLE_GAME_STATE, whereClause, whereArgs);
        db.close();
    }

    @SuppressLint("Range")
    public void logGameStates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAME_STATE, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String board = cursor.getString(cursor.getColumnIndex(COLUMN_BOARD));
                int playerXTurn = cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYER_X_TURN));
                int playWithCPU = cursor.getInt(cursor.getColumnIndex(COLUMN_PLAY_WITH_CPU));
                String playerSymbol = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_SYMBOL));
                String result = cursor.getString(cursor.getColumnIndex(COLUMN_RESULT));
                Log.d("GameDatabaseHelper", "ID: " + id + ", Board: " + board + ", Player X Turn: " + playerXTurn +
                        ", Play With CPU: " + playWithCPU + ", Player Symbol: " + playerSymbol + ", Result: " + result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static String getColumnBoard() {
        return COLUMN_BOARD;
    }

    public static String getColumnPlayerXTurn() {
        return COLUMN_PLAYER_X_TURN;
    }

    public static String getColumnPlayWithCPU() {
        return COLUMN_PLAY_WITH_CPU;
    }

    public static String getColumnPlayerSymbol() {
        return COLUMN_PLAYER_SYMBOL;
    }

    public static String getColumnResult() {
        return COLUMN_RESULT;
    }
}
