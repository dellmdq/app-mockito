package repositories;

import models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
