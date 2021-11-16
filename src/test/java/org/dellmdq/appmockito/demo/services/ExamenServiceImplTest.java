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
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl2 examenRepositoryImpl2;

    @Mock
    PreguntaRepositoryImpl preguntaRepositoryImpl;

    @InjectMocks
    ExamenServiceImpl examenServiceImpl;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);//forma para habilitar las anotaciones.
//        examenRepository = mock(ExamenRepository.class);
//        preguntaRepository = mock(PreguntaRepository.class);
//        examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito


        //when(repository.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //USAMOS AHORA UNA VARIABLE CONSTANTE
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = examenServiceImpl.findExamenPorNombre("Matemáticas");


        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        //ExamenRepository repository = new ExamenRepositoryImpl();//vamos a reemplazar esto con mockito

        List<Examen> datos = Collections.emptyList();

        when(examenRepositoryImpl2.findAll()).thenReturn(datos);//implementación del mockito indicando lo que queremos que retorne.
        //no se llama al metodo real cuando hacemos repository.findAll();
        Optional<Examen> examen = examenServiceImpl.findExamenPorNombre("Matemáticas");


        assertFalse(examen.isPresent());
    }

    @Test
    void preguntasExamen() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);//anyLong() se puede usar cualquier dato de tipo Long
        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));
        verify(examenRepositoryImpl2).findAll();
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(5L);
    }

    @Test
    void noExisteExamenVerify() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);//anyLong() se puede usar cualquier dato de tipo Long
        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");//no llamara a findPreguntasPorExamenId porque la coleccion de examenes viene vacia
        assertNull(examen);
        verify(examenRepositoryImpl2).findAll();
        //verify(preguntaRepository).findPreguntasPorExamenId(5L);//no entra en el if de findExamenPorNombreConPreguntas. Por eso falla. La lista de examenes esta vacia.
    }

    @Test
    void guardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepositoryImpl2.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long id = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(id++);
                return examen;
            }
        });
        //when
        Examen examen = examenServiceImpl.guardar(Datos.EXAMEN);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepositoryImpl2).guardar(any(Examen.class));//verificamos que se llame guardar con cualquier examen como parametro
        verify(preguntaRepositoryImpl).guardarVarias(anyList());//falla si no se invoca. si no hay pregunta no entra el if en el metodo.

    }

    @Test
    void exceptionHandler() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        /*Comprobamos el lanzamiento de la exception al recibir el null.*/
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            //tiene que ser el metodo con preguntas porque necesitamos que invoque a findPreguntasPorExamen. ^
            examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepositoryImpl2).findAll();
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(isNull());
    }

    @Test
    void argumentMatchers() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");

        verify(examenRepositoryImpl2).findAll();

        /*Distintas formas de verificar con ArgumentMatchers*/
//        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));
//        verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L));
    }


    /*Implementación usando clase personalizada*/
    @Test
    void argumentMatchers2() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");

        verify(examenRepositoryImpl2).findAll();
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(argThat(new MyArgsMatchers()));//This way we can implement the methods in the whole class.
    }

    /*Utilizando una expresión lambda.*/
    @Test
    void argumentMatchers3() {
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVE);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");

        verify(examenRepositoryImpl2).findAll();
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(argThat( (argument) -> argument != null && argument > 0 ));//We can only implment one method at a time.
    }

    /*Creación de una clase con todos los matchers que vamos a utilizar
    * También un toString para mostrar los argumentos.*/
    public static class MyArgsMatchers implements ArgumentMatcher<Long>{

        private Long argument;

        @Override
        public boolean matches(Long argument){
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString(){
            return "Custom error message. " + argument + " must be a positive integer.";
        }
    }

    @Test
    void argumentCaptor(){
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);//instance that will be used to capture the id. COMMENTED CAUSE IT IS INSTANCIATED AS A CLASS ATRIBUTE
        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(captor.capture());//here we capture the id

        //validation of capture
        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow(){
        //when(preguntaRepository.guardarVarias()).thenThrow(IllegalArgumentException.class);//falla porque guardarVarias es void
        //when(preguntaRepository.findPreguntasPorExamenId(5L)).thenThrow(IllegalArgumentException.class);//es si funciona es como venimos haciendolo

        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntaRepositoryImpl).guardarVarias(anyList());
        assertThrows(IllegalArgumentException.class, () -> {
            examenServiceImpl.guardar(examen);
        });


    }

    @Test
    void testDoAnswer(){
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(org.dellmdq.appmockito.demo.Datos.PREGUNTAS);

        /*Otra manera de obtener el las preguntas a través de un mock.*/
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS: null;
        }).when(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());

        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Geometría"));
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());

        verify(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());

    }

    @Test
    void guardarExamenDoAnswer() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

/*      CON DOANSWER TENEMOS OTRA MANERA DE HACER LO MISMO, PERO EN ORDEN INVERSO
      when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long id = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(id++);
                return examen;
            }
        });*/
        doAnswer(new Answer<Examen>() {
            Long id = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(id++);
                return examen;
            }
        }).when(examenRepositoryImpl2).guardar(any(Examen.class));

        //when
        Examen examen = examenServiceImpl.guardar(Datos.EXAMEN);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepositoryImpl2).guardar(any(Examen.class));//verificamos que se llame guardar con cualquier examen como parametro
        verify(preguntaRepositoryImpl).guardarVarias(anyList());//falla si no se invoca. si no hay pregunta no entra el if en el metodo.
    }

    @Test
    void doCallRealMethodTest(){
        when(examenRepositoryImpl2.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepositoryImpl.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //vamos a probar invocando con el método con el método real
        doCallRealMethod().when(preguntaRepositoryImpl).findPreguntasPorExamenId(anyLong());//falla inicialmente porque estamos llamando un método de una interfaz



        Examen examen = examenServiceImpl.findExamenPorNombreConPreguntas("Matemáticas");

        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void testSpy() {
        ExamenRepository examenRepositoryImpl2 = spy(ExamenRepositoryImpl2.class);
        PreguntaRepository preguntaRepositoryImpl = spy(PreguntaRepositoryImpl.class);
        ExamenService examenServiceImpl = new ExamenServiceImpl(examenRepositoryImpl2, preguntaRepositoryImpl);

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