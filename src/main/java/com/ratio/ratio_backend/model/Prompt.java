package com.ratio.ratio_backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Prompt")
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String content;
    private String model;
}