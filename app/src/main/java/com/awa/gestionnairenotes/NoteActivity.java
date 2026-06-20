package com.awa.gestionnairenotes;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awa.gestionnairenotes.database.NoteDAO;
import com.awa.gestionnairenotes.models.Note;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText etTitre;
    private EditText etContenu;
    private LinearLayout scrollView;
    private Button btnAction;

    private NoteDAO noteDAO;
    private boolean modeModification = false;
    private int noteId;
    private boolean noteFavori;
    private String noteDate;
    private String couleurChoisie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteDAO = new NoteDAO(this);
        initViews();
        recupererDonneeIntent();
    }

    private void initViews() {
        etTitre    = findViewById(R.id.et_titre);
        etContenu  = findViewById(R.id.et_contenu);
        scrollView = findViewById(R.id.scroll_view);
        btnAction  = findViewById(R.id.btn_action);
    }

    private void recupererDonneeIntent() {
        couleurChoisie = getIntent().getStringExtra(MainActivity.EXTRA_COLOR);
        if (couleurChoisie == null) couleurChoisie = "#219653";

        noteId = getIntent().getIntExtra(MainActivity.EXTRA_NOTE_ID, -1);

        if (noteId != -1) {
            modeModification = true;
            String titre   = getIntent().getStringExtra(MainActivity.EXTRA_NOTE_TITRE);
            String contenu = getIntent().getStringExtra(MainActivity.EXTRA_NOTE_CONTENU);
            noteFavori = getIntent().getBooleanExtra(MainActivity.EXTRA_NOTE_FAVORI, false);
            noteDate   = getIntent().getStringExtra(MainActivity.EXTRA_NOTE_DATE);

            etTitre.setText(titre);
            etContenu.setText(contenu);
            btnAction.setText("Modifier");
        } else {
            btnAction.setText("Créer");
        }

        appliquerCouleur(couleurChoisie);
        btnAction.setOnClickListener(v -> sauvegarder());
    }

    private void appliquerCouleur(String couleur) {
        try {
            int color = Color.parseColor(couleur);
            scrollView.setBackgroundColor(color);
            btnAction.setBackgroundColor(darken(color));
        } catch (Exception e) {
            scrollView.setBackgroundColor(Color.parseColor("#219653"));
        }
    }

    private int darken(int color) {
        float factor = 0.7f;
        int r = (int) (Color.red(color) * factor);
        int g = (int) (Color.green(color) * factor);
        int b = (int) (Color.blue(color) * factor);
        return Color.rgb(r, g, b);
    }

    private void sauvegarder() {
        String titre   = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty() && contenu.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un titre ou un contenu.", Toast.LENGTH_SHORT).show();
            return;
        }

        noteDAO.ouvrir();

        if (!modeModification) {
            String dateDuJour = DateFormat.format("dd MMM yyyy", new Date()).toString();
            Note nouvelleNote = new Note(titre, contenu, couleurChoisie, false, dateDuJour);
            noteDAO.ajouterNote(nouvelleNote);
            Toast.makeText(this, "Note créée !", Toast.LENGTH_SHORT).show();
        } else {
            Note noteModifiee = new Note(noteId, titre, contenu, couleurChoisie, noteFavori, noteDate);
            noteDAO.modifierNote(noteModifiee);
            Toast.makeText(this, "Note modifiée !", Toast.LENGTH_SHORT).show();
        }

        noteDAO.fermer();

        setResult(RESULT_OK);
        finish();
    }
}