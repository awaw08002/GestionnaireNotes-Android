package com.esp.gestionnairenotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final String NOM_BDD = "notes.db";
    private static final int VERSION_BDD = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COL_ID = "id";
    public static final String COL_TITRE = "titre";
    public static final String COL_CONTENU = "contenu";
    public static final String COL_COULEUR = "couleur";
    public static final String COL_FAVORI = "favori";
    public static final String COL_DATE = "date";

    private static final String CREATION_TABLE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITRE + " TEXT NOT NULL, " +
                    COL_CONTENU + " TEXT, " +
                    COL_COULEUR + " TEXT, " +
                    COL_FAVORI + " INTEGER DEFAULT 0, " +
                    COL_DATE + " TEXT" +
                    ")";

    public NoteDatabase(Context context) {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
