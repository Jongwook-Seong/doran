package com.sjw.doran.itemservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ARTWORK")
public class Artwork extends Item {

    private String artist;
    private String explanation;
    private String workSize;
    private int productionYear;
}
