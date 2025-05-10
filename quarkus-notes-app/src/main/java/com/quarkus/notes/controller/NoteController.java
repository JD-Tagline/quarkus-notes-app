package com.quarkus.notes.controller;

import com.quarkus.notes.model.Note;
import com.quarkus.notes.service.INoteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RequiredArgsConstructor
@Path("/notes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);
    private static final String TAG_NAME = "NoteController";

    final INoteService iNoteService;

    @RolesAllowed("user")
    @POST
    public Response createNote(Note note) {
        LOGGER.info("{} :: inside createNote : Note :: ", TAG_NAME);
        try {
            return iNoteService.createNote(note);
        } catch (Exception e) {
            LOGGER.info("{} :: inside createNote : Note :: Create Note request details invalid! ", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Create Note request details invalid! : " + e.getMessage()).build();
        }
    }

    @RolesAllowed("user")
    @PUT
    @Path("/{id}")
    public Response updateNote(@PathParam("id") UUID id, Note note) {
        LOGGER.info("{} :: inside updateNote : Note :: ", TAG_NAME);
        try {
            return iNoteService.updateNote(id, note);
        } catch (Exception e) {
            LOGGER.info("{} :: inside updateNote : Note :: Update Note request details invalid!", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Update Note request details invalid! : " + e.getMessage()).build();
        }
    }

    @RolesAllowed("user")
    @GET
    public Response getAllNotes() {
        LOGGER.info("{} :: inside getAllNotes : ", TAG_NAME);
        try {
            return iNoteService.getAllNotes();
        } catch (Exception e) {
            LOGGER.info("{} :: inside getAllNotes : Note details not found! ", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Note details not found! : " + e.getMessage()).build();
        }
    }

    @RolesAllowed("user")
    @GET
    @Path("/{id}")
    public Response getNoteById(@PathParam("id") UUID id) {
        LOGGER.info("{} :: inside getNoteById : UUID :: ", TAG_NAME);
        try {
            return iNoteService.getNoteById(id);
        } catch (Exception e) {
            LOGGER.info("{} :: inside getNoteById : UUID :: NoteId is invalid! ", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("NoteId is invalid! : " + e.getMessage()).build();
        }
    }

    @RolesAllowed("user")
    @DELETE
    @Path("/{id}")
    public Response deleteNoteById(@PathParam("id") UUID id) {
        LOGGER.info("{} :: inside deleteNoteById : UUID :: ", TAG_NAME);
        try {
            return iNoteService.deleteNoteById(id);
        } catch (Exception e) {
            LOGGER.info("{} :: inside deleteNoteById : UUID :: NoteId is invalid! ", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("NoteId is invalid! : " + e.getMessage()).build();
        }
    }

    @RolesAllowed("user")
    @GET
    @Path("/with-external/{id}")
    public Response getNoteWithExternalData(@PathParam("id") UUID id) {
        LOGGER.info("{} :: inside getNoteWithExternalData : UUID :: ", TAG_NAME);
        try {
            return iNoteService.getNoteWithExternalData(id);
        } catch (Exception e) {
            LOGGER.info("{} :: inside getNoteWithExternalData : UUID :: NoteId is invalid! ", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("NoteId is invalid! : " + e.getMessage()).build();
        }
    }
}
