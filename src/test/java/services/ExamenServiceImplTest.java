package services;

import models.Examen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repositories.ExamenRepository;
import repositories.ExamenRepositoryImpl;
import repositories.ExamenRepositoryO;
import repositories.PreguntaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {
    ExamenRepository repository;
    ExamenService service;
    PreguntaRepository preguntaRepository;

    @BeforeEach
    void setUp() {
        repository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre(){
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito



        //when(repository.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //USAMOS AHORA UNA VARIABLE CONSTANTE
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");


        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia(){
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");


        assertFalse(examen.isPresent());
    }

    @Test
    void preguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);//anyLong() se puede usar cualquier dato de tipo Long
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));
    }
}