package com.awa.gestionnairenotes.models;

public class Note {

    // Attributs
    private int id;
    private String titre;
    private String contenu;
    private String couleur;
    private boolean favori;
    private String date;

    // -------------------------------------------------------
    // Constructeur SANS id → utilisé quand on crée une note
    // (l'id sera généré automatiquement par SQLite)
    // -------------------------------------------------------
    public Note(String titre, String contenu, String couleur, boolean favori, String date) {
        this.titre   = titre;
        this.contenu = contenu;
        this.couleur = couleur;
        this.favori  = favori;
        this.date    = date;
    }

    // -------------------------------------------------------
    // Constructeur AVEC id → utilisé quand on lit depuis SQLite
    // -------------------------------------------------------
    public Note(int id, String titre, String contenu, String couleur, boolean favori, String date) {
        this.id      = id;
        this.titre   = titre;
        this.contenu = contenu;
        this.couleur = couleur;
        this.favori  = favori;
        this.date    = date;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------
    public int     getId()      { return id;      }
    public String  getTitre()   { return titre;   }
    public String  getContenu() { return contenu; }
    public String  getCouleur() { return couleur; }
    public boolean isFavori()   { return favori;  }
    public String  getDate()    { return date;    }

    // -------------------------------------------------------
    // Setters
    // -------------------------------------------------------
    public void setId(int id)           { this.id      = id;      }
    public void setTitre(String titre)  { this.titre   = titre;   }
    public void setContenu(String c)    { this.contenu = c;       }
    public void setCouleur(String c)    { this.couleur = c;       }
    public void setFavori(boolean f)    { this.favori  = f;       }
    public void setDate(String date)    { this.date    = date;    }
}