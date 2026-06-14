package com.awa.gestionnairenotes;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esp.gestionnairenotes.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteFavoriteToggled(Note note);
    }

    private List<Note> notes;
    private List<Note> allNotes;
    private final Context context;
    private final OnNoteClickListener listener;

    public NoteAdapter(Context context, OnNoteClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.notes = new ArrayList<>();
        this.allNotes = new ArrayList<>();
    }

    public void setNotes(List<Note> notes) {
        this.notes = new ArrayList<>(notes);
        this.allNotes = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            notes = new ArrayList<>(allNotes);
        } else {
            String q = query.toLowerCase().trim();
            List<Note> filtered = new ArrayList<>();
            for (Note n : allNotes) {
                if (n.getTitre() != null && n.getTitre().toLowerCase().contains(q)) {
                    filtered.add(n);
                }
            }
            notes = filtered;
        }
        notifyDataSetChanged();
    }

    public void filterFavoris(boolean favoriOnly) {
        if (!favoriOnly) {
            notes = new ArrayList<>(allNotes);
        } else {
            List<Note> filtered = new ArrayList<>();
            for (Note n : allNotes) {
                if (n.isFavori()) {
                    filtered.add(n);
                }
            }
            notes = filtered;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.tvTitre.setText(note.getTitre());
        holder.tvDate.setText(note.getDate());

        try {
            holder.itemView.setBackgroundColor(Color.parseColor(note.getCouleur()));
        } catch (Exception e) {
            holder.itemView.setBackgroundColor(Color.parseColor("#219653"));
        }

        if (note.isFavori()) {
            holder.ivFavori.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavori.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));

        GestureDetector gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        listener.onNoteFavoriteToggled(note);
                        return true;
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        listener.onNoteClick(note);
                        return true;
                    }
                });

        holder.itemView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre;
        TextView tvDate;
        ImageView ivFavori;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tv_titre);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivFavori = itemView.findViewById(R.id.iv_favori);
        }
    }
}
