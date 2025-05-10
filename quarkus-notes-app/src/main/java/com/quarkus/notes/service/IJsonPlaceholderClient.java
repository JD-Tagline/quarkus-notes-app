package com.quarkus.notes.service;

import com.quarkus.notes.model.ExternalPost;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/posts")
@RegisterRestClient(baseUri = "https://jsonplaceholder.typicode.com")
public interface IJsonPlaceholderClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    ExternalPost getPostById(@PathParam("id") int id);
}
