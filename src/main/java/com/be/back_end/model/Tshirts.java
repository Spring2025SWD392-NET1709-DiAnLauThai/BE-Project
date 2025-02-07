package com.be.back_end.model;

import com.be.back_end.model.Enum.TshirtStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
public class Tshirts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    

}
