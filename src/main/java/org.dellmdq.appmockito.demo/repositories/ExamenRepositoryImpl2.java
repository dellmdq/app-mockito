package org.dellmdq.appmockito.demo.repositories;

import org.dellmdq.appmockito.demo.models.Examen;
import org.dellmdq.appmockito.demo.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl2 implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        //simular pausa larga
        try {
            System.out.println("ExamenRepositoryImpl2.findAll");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Datos.EXAMENES;
    }

    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl2.guardar");
        return Datos.EXAMEN;
    }
}
