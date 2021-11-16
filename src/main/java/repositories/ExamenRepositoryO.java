package repositories;

import models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryO implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        //simular pausa larga
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Examen guardar(Examen examen) {
        return null;
    }
}
