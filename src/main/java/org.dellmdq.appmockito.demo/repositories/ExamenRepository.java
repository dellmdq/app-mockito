package org.dellmdq.appmockito.demo.repositories;

import org.dellmdq.appmockito.demo.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();

    Examen guardar(Examen examen);
}
