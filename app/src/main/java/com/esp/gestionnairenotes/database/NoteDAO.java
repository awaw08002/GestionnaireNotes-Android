
package com.esp.gestionnairenotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esp.gestionnairenotes.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private NoteDatabase noteDatabase;
    private SQLiteDatabase db;

    public NoteDAO(Context context) {
        noteDatabase = new NoteDatabase(context);
    }

    // Ouvrir la connexion
    public void ouvrir() {
        db = noteDatabase.getWritableDatabase();
    }

    // Fermer la connexion
    public void fermer() {
        noteDatabase.close();
    }

    // AJOUTER une note
    public long ajouterNote(Note note) {
        ContentValues valeurs = new ContentValues();
        valeurs.put(NoteDatabase.COL_TITRE,   note.getTitre());
        valeurs.put(NoteDatabase.COL_CONTENU, note.getContenu());
        valeurs.put(NoteDatabase.COL_COULEUR, note.getCouleur());
        valeurs.put(NoteDatabase.COL_FAVORI,  note.isFavori() ? 1 : 0);
        valeurs.put(NoteDatabase.COL_DATE,    note.getDate());
        return db.insert(NoteDatabase.TABLE_NOTES, null, valeurs);
    }

    // MODIFIER une note
    public int modifierNote(Note note) {
        ContentValues valeurs = new ContentValues();
        valeurs.put(NoteDatabase.COL_TITRE,   note.getTitre());
        valeurs.put(NoteDatabase.COL_CONTENU, note.getContenu());
        valeurs.put(NoteDatabase.COL_COULEUR, note.getCouleur());
        valeurs.put(NoteDatabase.COL_FAVORI,  note.isFavori() ? 1 : 0);
        valeurs.put(NoteDatabase.COL_DATE,    note.getDate());
        return db.update(
                NoteDatabase.TABLE_NOTES,
                valeurs,
                NoteDatabase.COL_ID + " = ?",
                new String[]{String.valueOf(note.getId())}
        );
    }

    // SUPPRIMER une note
    public int supprimerNote(int id) {
        return db.delete(
                NoteDatabase.TABLE_NOTES,
                NoteDatabase.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // OBTENIR toutes les notes
    public List<Note> obtenirToutesLesNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(
                NoteDatabase.TABLE_NOTES,
                null, null, null, null, null,
                NoteDatabase.COL_DATE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                notes.add(cursorVersNote(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    // OBTENIR uniquement les favoris
    public List<Note> obtenirFavoris() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(
                NoteDatabase.TABLE_NOTES,
                null,
                NoteDatabase.COL_FAVORI + " = 1",
                null, null, null,
                NoteDatabase.COL_DATE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                notes.add(cursorVersNote(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    // RECHERCHER par titre
    public List<Note> rechercherParTitre(String recherche) {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(
                NoteDatabase.TABLE_NOTES,
                null,
                NoteDatabase.COL_TITRE + " LIKE ?",
                new String[]{"%" + recherche + "%"},
                null, null,
                NoteDatabase.COL_DATE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                notes.add(cursorVersNote(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return notes;
    }

    // Utilitaire privé : Cursor → Note
    private Note cursorVersNote(Cursor cursor) {
        return new Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_TITRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_CONTENU)),
                cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_COULEUR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_FAVORI)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_DATE))
        );
    }
}