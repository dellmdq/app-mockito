package org.dellmdq.appmockito.demo.repositories;

import org.dellmdq.appmockito.demo.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository{

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        return Arrays.asList(new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"), new Examen(4L,"Biologia")
                );
    }

    @Override
    public Examen guardar(Examen examen) {
        return null;
    }
}
