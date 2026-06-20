package com.awa.gestionnairenotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awa.gestionnairenotes.database.NoteDAO;
import com.awa.gestionnairenotes.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    public static final String EXTRA_COLOR = "extra_color";
    public static final String EXTRA_NOTE_ID = "extra_note_id";
    public static final String EXTRA_NOTE_TITRE = "extra_note_titre";
    public static final String EXTRA_NOTE_CONTENU = "extra_note_contenu";
    public static final String EXTRA_NOTE_FAVORI = "extra_note_favori";
    public static final String EXTRA_NOTE_DATE = "extra_note_date";
    public static final int REQUEST_NOTE = 100;

    private static final String[] COULEURS = {
            "#219653", "#EB5757", "#2F80ED", "#F2C94C", "#F2994A", "#828282"
    };

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private TextView tvAucuneNotes;
    private TextView tvCompteur;
    private EditText etRecherche;
    private TextView btnFavoris;
    private FloatingActionButton fab;

    private View btnVert, btnRouge, btnBleu, btnJaune, btnOrange, btnGris;
    private View layoutPalette;
    private boolean paletteOuverte = false;

    private NoteDAO noteDAO;
    private boolean afficherFavoris = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteDAO = new NoteDAO(this);
        initViews();
        setupRecyclerView();
        setupRecherche();
        setupFab();
        setupBoutonFavoris();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerNotes();
    }

    private void initViews() {
        recyclerView    = findViewById(R.id.recycler_notes);
        tvAucuneNotes   = findViewById(R.id.tv_aucune_notes);
        tvCompteur      = findViewById(R.id.tv_compteur);
        etRecherche     = findViewById(R.id.et_recherche);
        btnFavoris      = findViewById(R.id.btn_favoris);
        btnFavoris.bringToFront();
        fab             = findViewById(R.id.fab_ajouter);
        layoutPalette = findViewById(R.id.layout_palette);
        btnVert   = findViewById(R.id.btn_couleur_vert);
        btnRouge  = findViewById(R.id.btn_couleur_rouge);
        btnBleu   = findViewById(R.id.btn_couleur_bleu);
        btnJaune  = findViewById(R.id.btn_couleur_jaune);
        btnOrange = findViewById(R.id.btn_couleur_orange);
        btnGris   = findViewById(R.id.btn_couleur_gris);
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRecherche() {
        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
                mettreAJourEtatVide();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBoutonFavoris() {
        android.util.Log.d("DEBUG_FAVORIS", "setupBoutonFavoris() appelée, btnFavoris = " + btnFavoris);
        btnFavoris.setOnClickListener(v -> {
            android.util.Log.d("DEBUG_FAVORIS", "CLIC DETECTE !");
            afficherFavoris = !afficherFavoris;
            btnFavoris.setText(afficherFavoris ? "Tous" : "Favoris");
            adapter.filterFavoris(afficherFavoris);
            mettreAJourEtatVide();
        });
    }

    private void setupFab() {
        masquerPalette();

        fab.setOnClickListener(v -> {
            if (paletteOuverte) {
                fermerPalette();
            } else {
                ouvrirPalette();
            }
        });

        btnVert.setOnClickListener(v   -> ouvrirCreation(COULEURS[0]));
        btnRouge.setOnClickListener(v  -> ouvrirCreation(COULEURS[1]));
        btnBleu.setOnClickListener(v   -> ouvrirCreation(COULEURS[2]));
        btnJaune.setOnClickListener(v  -> ouvrirCreation(COULEURS[3]));
        btnOrange.setOnClickListener(v -> ouvrirCreation(COULEURS[4]));
        btnGris.setOnClickListener(v   -> ouvrirCreation(COULEURS[5]));
    }

    private void ouvrirPalette() {
        paletteOuverte = true;
        layoutPalette.setVisibility(View.VISIBLE);

        animerApparition(btnVert);
        animerApparition(btnRouge);
        animerApparition(btnBleu);
        animerApparition(btnJaune);
        animerApparition(btnOrange);
        animerApparition(btnGris);
    }

    private void fermerPalette() {
        paletteOuverte = false;
        masquerPalette();
    }

    private void masquerPalette() {
        layoutPalette.setVisibility(View.GONE);
    }

    private void animerApparition(View vue) {
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        anim.setDuration(200);
        vue.startAnimation(anim);
    }

    private void ouvrirCreation(String couleur) {
        fermerPalette();
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(EXTRA_COLOR, couleur);
        startActivityForResult(intent, REQUEST_NOTE);
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, note.getId());
        intent.putExtra(EXTRA_NOTE_TITRE, note.getTitre());
        intent.putExtra(EXTRA_NOTE_CONTENU, note.getContenu());
        intent.putExtra(EXTRA_COLOR, note.getCouleur());
        intent.putExtra(EXTRA_NOTE_FAVORI, note.isFavori());
        intent.putExtra(EXTRA_NOTE_DATE, note.getDate());
        startActivityForResult(intent, REQUEST_NOTE);
    }

    @Override
    public void onNoteFavoriteToggled(Note note) {
        note.setFavori(!note.isFavori());
        noteDAO.ouvrir();
        noteDAO.modifierNote(note);
        noteDAO.fermer();
        chargerNotes();
        String msg = note.isFavori() ? "Ajouté aux favoris" : "Retiré des favoris";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteDelete(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Supprimer la note")
                .setMessage("Voulez-vous vraiment supprimer cette note ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    noteDAO.ouvrir();
                    noteDAO.supprimerNote(note.getId());
                    noteDAO.fermer();
                    chargerNotes();
                    Toast.makeText(this, "Note supprimée", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void chargerNotes() {
        noteDAO.ouvrir();
        List<Note> notes = noteDAO.obtenirToutesLesNotes();
        noteDAO.fermer();

        adapter.setNotes(notes);
        tvCompteur.setText(notes.size() + " note(s)");

        if (afficherFavoris) {
            adapter.filterFavoris(true);
        }

        mettreAJourEtatVide();
    }

    private void mettreAJourEtatVide() {
        if (adapter.getItemCount() == 0) {
            tvAucuneNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvAucuneNotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NOTE && resultCode == RESULT_OK) {
            chargerNotes();
        }
    }
}