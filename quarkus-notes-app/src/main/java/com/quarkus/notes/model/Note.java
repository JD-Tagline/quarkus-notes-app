package com.quarkus.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {

    private UUID id;
    private String title;
    private String body;
}

