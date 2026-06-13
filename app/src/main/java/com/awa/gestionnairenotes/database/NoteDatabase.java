package com.awa.gestionnairenotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {

    // Nom du fichier de base de données sur le téléphone
    private static final String NOM_BDD     = "notes.db";
    // Version : à incrémenter si on change la structure plus tard
    private static final int    VERSION_BDD = 1;

    // -------------------------------------------------------
    // Noms de la table et des colonnes
    // Déclarés en public static pour être réutilisés par NoteDAO
    // -------------------------------------------------------
    public static final String TABLE_NOTES = "notes";
    public static final String COL_ID      = "id";
    public static final String COL_TITRE   = "titre";
    public static final String COL_CONTENU = "contenu";
    public static final String COL_COULEUR = "couleur";
    public static final String COL_FAVORI  = "favori";
    public static final String COL_DATE    = "date";

    // -------------------------------------------------------
    // Requête SQL de création de la table
    // -------------------------------------------------------
    private static final String CREATION_TABLE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TITRE   + " TEXT NOT NULL, "                     +
                    COL_CONTENU + " TEXT, "                              +
                    COL_COULEUR + " TEXT, "                              +
                    COL_FAVORI  + " INTEGER DEFAULT 0, "                 +
                    COL_DATE    + " TEXT"                                +
                    ")";

    // -------------------------------------------------------
    // Constructeur
    // -------------------------------------------------------
    public NoteDatabase(Context context) {
        super(context, NOM_BDD, null, VERSION_BDD);
    }

    // -------------------------------------------------------
    // onCreate : appelé UNE SEULE FOIS à la première installation
    // -------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE);
    }

    // -------------------------------------------------------
    // onUpgrade : appelé si VERSION_BDD augmente
    // -------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}