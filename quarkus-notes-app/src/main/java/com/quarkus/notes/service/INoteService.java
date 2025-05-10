package com.quarkus.notes.service;

import com.quarkus.notes.model.Note;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public interface INoteService {

    Response createNote(Note note);

    Response updateNote(UUID id, Note note);

    Response getAllNotes();

    Response getNoteById(UUID id);

    Response deleteNoteById(UUID id);

    Response getNoteWithExternalData(UUID id);
}
