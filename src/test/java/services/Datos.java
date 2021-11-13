package services;

import models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia"), new Examen(4L,"Biologia"));

    public final static List<String> PREGUNTAS = Arrays.asList("Aritmética", "Integrales", "Derivadas", "Trigonometría", "Geometría");
}
