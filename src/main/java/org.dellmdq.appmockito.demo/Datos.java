package org.dellmdq.appmockito.demo;

import org.dellmdq.appmockito.demo.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia"), new Examen(4L,"Biologia"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matemáticas"), new Examen(null, "Lenguaje"),
            new Examen(null, "Historia"), new Examen(null,"Biologia"));

    public final static List<Examen> EXAMENES_ID_NEGATIVE = Arrays.asList(new Examen(-5L, "Matemáticas"), new Examen(-6L, "Lenguaje"),
            new Examen(null, "Historia"), new Examen(-4L,"Biologia"));

    public final static List<String> PREGUNTAS = Arrays.asList("Aritmética", "Integrales", "Derivadas", "Trigonometría", "Geometría");

    public final static Examen EXAMEN = new Examen(null, "Física");
}
