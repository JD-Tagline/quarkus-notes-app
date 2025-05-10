package com.quarkus.notes.repository;

import com.quarkus.notes.model.Note;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class NoteRepository {

    private Map<UUID, Note> noteMap = new HashMap<>();

    public Note save(Note note) {
        noteMap.put(note.getId(), note);
        return note;
    }

    public List<Note> findAll() {
        return new ArrayList<>(noteMap.values());
    }

    public Optional<Note> findById(UUID id) {
        return Optional.ofNullable(noteMap.get(id));
    }

    public void delete(UUID id) {
        noteMap.remove(id);
    }

    public boolean existsById(UUID id) {
        return noteMap.containsKey(id);
    }
}

