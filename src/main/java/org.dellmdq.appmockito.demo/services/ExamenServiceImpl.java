package org.dellmdq.appmockito.demo.services;

import org.dellmdq.appmockito.demo.models.Examen;
import org.dellmdq.appmockito.demo.repositories.ExamenRepository;
import org.dellmdq.appmockito.demo.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{
    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository){
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }


    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll()//optional contiene una representación del objeto para evitar el null pointer exception.
                .stream()//convierte la lista en un stream
                .filter(examen -> examen.getNombre().contains(nombre))//filtramos por nombre recorriendo la lista
                .findFirst();//solo queremos un resultado

/*        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
        }
        return examen; HACEMOS EL RETURN DIRECTAMENTE DEL OPTIONAL*/
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }
}
