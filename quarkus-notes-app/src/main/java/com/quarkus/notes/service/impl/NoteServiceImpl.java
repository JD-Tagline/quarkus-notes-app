package com.quarkus.notes.service.impl;

import com.quarkus.notes.model.ExternalPost;
import com.quarkus.notes.model.Note;
import com.quarkus.notes.repository.NoteRepository;
import com.quarkus.notes.service.INoteService;
import com.quarkus.notes.service.IJsonPlaceholderClient;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ApplicationScoped
public class NoteServiceImpl implements INoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);
    private static final String TAG_NAME = "NoteServiceImpl";

    private final NoteRepository noteRepository;
    private final IJsonPlaceholderClient iJsonPlaceholderClient;

    @Inject
    public NoteServiceImpl(@RestClient IJsonPlaceholderClient iJsonPlaceholderClient, NoteRepository noteRepository) {
        this.iJsonPlaceholderClient = iJsonPlaceholderClient;
        this.noteRepository = noteRepository;
    }

    @Override
    @CacheInvalidateAll(cacheName = "notes-cache")
    public Response createNote(Note note) {
        LOGGER.info("{} :: inside createNote : Note :: ", TAG_NAME);
        note.setId(UUID.randomUUID());
        noteRepository.save(note);
        return Response.status(Response.Status.OK).entity(note).build();
    }

    @Override
    @CacheInvalidateAll(cacheName = "notes-cache")
    public Response updateNote(UUID id, Note note) {
        LOGGER.info("{} :: inside updateNote : Note :: ", TAG_NAME);
        Optional<Note> optionalNote = findNoteById(id);
        if (optionalNote.isEmpty()) {
            LOGGER.info("{} :: inside updateNote : UUID :: NoteId not found! : ", TAG_NAME);
            return Response.status(Response.Status.NOT_FOUND).entity("NoteId not found!").build();
        }
        Note existingNote = optionalNote.get();
        existingNote.setTitle(note.getTitle());
        existingNote.setBody(note.getBody());
        return Response.status(Response.Status.OK).entity(existingNote).build();
    }

    @Override
    @CacheResult(cacheName = "notes-cache")
    public Response getAllNotes() {
        LOGGER.info("{} :: inside getAllNotes : ", TAG_NAME);
        List<Note> noteList = noteRepository.findAll();
        if (noteList.isEmpty()) {
            LOGGER.info("{} :: inside getAllNotes : UUID :: Notes data not found! : ", TAG_NAME);
            return Response.status(Response.Status.NOT_FOUND).entity("Notes data not found!").build();
        }
        return Response.status(Response.Status.OK).entity(noteList).build();
    }

    @Override
    @CacheResult(cacheName = "notes-cache")
    public Response getNoteById(UUID id) {
        LOGGER.info("{} :: inside getNoteById : UUID :: ", TAG_NAME);
        Optional<Note> optionalNote = findNoteById(id);
        if (optionalNote.isEmpty()) {
            LOGGER.info("{} :: inside getNoteById : UUID :: NoteId not found! : ", TAG_NAME);
            return Response.status(Response.Status.NOT_FOUND).entity("NoteId not found!").build();
        }
        Note note = optionalNote.get();
        return Response.status(Response.Status.OK).entity(note).build();
    }

    private Optional<Note> findNoteById(UUID id) {
        return noteRepository.findById(id);
    }

    @Override
    @CacheInvalidateAll(cacheName = "notes-cache")
    public Response deleteNoteById(UUID id) {
        LOGGER.info("{} :: inside deleteNoteById : UUID :: ", TAG_NAME);
        if (noteRepository.existsById(id)) {
            noteRepository.delete(id);
            return Response.status(Response.Status.OK).entity("Note deleted Successfully!").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("NoteId does not exist!").build();
    }

    @Override
    public Response getNoteWithExternalData(UUID id) {
        LOGGER.info("{} :: inside getNoteWithExternalData : UUID :: ", TAG_NAME);
        Optional<Note> optionalNote = findNoteById(id);
        if (optionalNote.isEmpty()) {
            LOGGER.info("{} :: inside getNoteWithExternalData : UUID :: NoteId not found! : ", TAG_NAME);
            return Response.status(Response.Status.NOT_FOUND).entity("NoteId not found!").build();
        }

        Note note = optionalNote.get();
        ExternalPost externalPost = getExternalPost(id);

        Map<String, Object> result = new HashMap<>();
        result.put("note", note);
        result.put("externalPost", externalPost);
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @CacheResult(cacheName = "external-posts")
    public ExternalPost getExternalPost(UUID uuid) {
        LOGGER.info("{} :: inside getExternalPost : UUID :: ", TAG_NAME);
        int fallbackId = Math.abs(uuid.hashCode() % 100) + 1;
        return iJsonPlaceholderClient.getPostById(fallbackId);
    }
}
