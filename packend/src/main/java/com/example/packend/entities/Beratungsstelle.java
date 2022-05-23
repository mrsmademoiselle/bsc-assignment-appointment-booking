package com.example.packend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Data
@Embeddable
@NoArgsConstructor
public class Beratungsstelle {
    @Id
    String id;
}
