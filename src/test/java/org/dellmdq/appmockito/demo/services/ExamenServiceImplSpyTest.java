package org.dellmdq.appmockito.demo.services;

import org.dellmdq.appmockito.demo.Datos;
import org.dellmdq.appmockito.demo.models.Examen;
import org.dellmdq.appmockito.demo.repositories.ExamenRepository;
import org.dellmdq.appmockito.demo.repositories.ExamenRepositoryImpl2;
import org.dellmdq.appmockito.demo.repositories.PreguntaRepository;
import org.dellmdq.appmockito.demo.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl2 examenRepositoryImpl2;

    @Spy
    PreguntaRepositoryImpl preguntaRepositoryImpl;

    @InjectMocks
    ExamenServiceImpl examenServiceImpl;


    /**Vamos a configurar clase y su método solo en base a las anotaciones Spy*/
    @Test
    void testSpy() {

        //1 así configurado el when terminará llamando al método real
        List<String> preguntas = Arrays.asList("Aritmética");
//        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        //vamos a usar un do para evitar que se llamé al método real en el when.
        doReturn(preguntas).when(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());
        //

        //2 acá llama al método ya mockeado
        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");
        //no estamos simulando nada, se llamará el método real (si no estuviera el when declarado)

        assertEquals(5, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));

        verify(examenRepositoryImpl2).findAll();//llamada real
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());//llamada al mock

    }
}