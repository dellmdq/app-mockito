package services;

import models.Examen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.ExamenRepository;
import repositories.PreguntaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks
    ExamenServiceImpl examenServiceImpl;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);//forma para habilitar las anotaciones.
//        examenRepository = mock(ExamenRepository.class);
//        preguntaRepository = mock(PreguntaRepository.class);
//        examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre(){
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito



        //when(repository.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //USAMOS AHORA UNA VARIABLE CONSTANTE
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = examenServiceImpl.findExamenPorNombre("Matemáticas");


        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia(){
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito

        List<Examen> datos = Collections.emptyList();

        when(examenRepository.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = examenServiceImpl.findExamenPorNombre("Matemáticas");


        assertFalse(examen.isPresent());
    }

    @Test
    void preguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);//anyLong() se puede usar cualquier dato de tipo Long
        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void noExisteExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);//anyLong() se puede usar cualquier dato de tipo Long
        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");//no llamara a findPreguntasPorExamenId porque la coleccion de examenes viene vacia
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);//no entra en el if de findExamenPorNombreConPreguntas. Por eso falla. La lista de examenes esta vacia.
    }

    @Test
    void guardarExamen() {
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
        Examen examen = examenServiceImpl.guardar(Datos.EXAMEN);

        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));//verificamos que se llame guardar con cualquier examen como parametro
        verify(preguntaRepository).guardarVarias(anyList());//falla si no se invoca. si no hay pregunta no entra el if en el metodo.

    }
}