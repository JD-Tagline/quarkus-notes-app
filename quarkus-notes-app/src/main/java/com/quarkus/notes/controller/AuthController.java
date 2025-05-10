package com.quarkus.notes.controller;

import com.quarkus.notes.model.User;
import com.quarkus.notes.service.IUserService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private static final String TAG_NAME = "AuthController";

    final IUserService iUserService;

    @POST
    public Response login(User user) {
        LOGGER.info("{} :: inside login : User :: ", TAG_NAME);
        try {
            String token = iUserService.authenticate(user.getUsername(), user.getPassword());
            if (token != null) {
                LOGGER.info("{} :: inside login : User :: response : ok :: ", TAG_NAME);
                return Response.ok().entity("{\"token\":\"" + token + "\"}").build();
            } else {
                LOGGER.info("{} :: inside login : User :: response : unauthorized :: ", TAG_NAME);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            LOGGER.info("{} :: inside login : User :: User login request is invalid!", TAG_NAME, e);
            return Response.status(Response.Status.BAD_REQUEST).entity("User login request is invalid! : " + e.getMessage()).build();
        }
    }
}
