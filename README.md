# EA3 - Programación Funcional en Java
## Caso de estudio: Procesamiento funcional de datos para un sistema de gestión de transporte urbano

---

## 1. Información del proyecto

**Actividad:** EA3 - Creación de repositorio  

**Asignatura:** PROGRAMACIÓN II: ORIENTADA A OBJETOS AVANZADA - PREICA2602B010093

**Docente:** BORIS ALBERTO SALLEG

**Grupo:** 7

**Fecha:** 26/09/2026

---


## Integrantes

1. EMILSE CAVADIA TORDECILLA
2. CRISTIAN CAMILO DEOSSA BOLIVAR
3. JOSE WILBER MURILLO MURILLO
4. KAROL LICETH PEREZ CASTAÑEDA
5. NICOLE VALENTINA QUEVEDO FRANCO

---

## 2. Descripción del proyecto

El presente proyecto corresponde a la Actividad Evaluativa #3 de Programación Funcional.

El caso de estudio plantea una situación relacionada con el sistema de transporte urbano de la ciudad de **TecnoValle**, donde se generan grandes cantidades de información sobre los pasajeros, las rutas, las estaciones y los horarios.

El objetivo es desarrollar un programa en Java que permita procesar un conjunto de datos simulados utilizando conceptos de **programación funcional**.

Para realizar el proyecto se utilizan:

- Funciones.
- Funciones puras.
- Inmutabilidad.
- Expresiones lambda.
- Streams.
- Funciones de orden superior.
- Colecciones.
- Procesamiento declarativo de datos.

El programa permite obtener información útil sobre el comportamiento de los pasajeros y el uso del sistema de transporte.

---

## 3. Problema planteado

El sistema actual de transporte utiliza un procesamiento principalmente imperativo, con estructuras de datos mutables y código que puede ser difícil de mantener.

Esta situación puede generar problemas cuando se necesita procesar grandes cantidades de información diariamente.

Entre los principales problemas planteados en el caso se encuentran:

- Demoras en el procesamiento de la información.
- Dificultad para analizar grandes cantidades de datos.
- Problemas para encontrar patrones de viaje.
- Dificultad para generar estadísticas.
- Código extenso y difícil de mantener.
- Posibles errores cuando varias operaciones modifican los mismos datos.

Por esta razón se propone utilizar programación funcional para organizar mejor el procesamiento de los datos.

---

## 4. Objetivo general

Aplicar conceptos de programación funcional en Java para procesar datos simulados de un sistema de transporte urbano y obtener información sobre pasajeros, estaciones, horarios y rutas.

---

## 5. Objetivos específicos

- Representar los registros de transporte mediante objetos inmutables.
- Aplicar funciones puras en el procesamiento de los datos.
- Utilizar expresiones lambda.
- Utilizar Streams para filtrar, agrupar y transformar información.
- Aplicar funciones de orden superior.
- Calcular la afluencia de pasajeros por estación.
- Identificar las horas de mayor flujo.
- Determinar las rutas más utilizadas.
- Identificar patrones de viaje de los usuarios.
- Calcular un tiempo promedio entre estaciones.
- Detectar rutas que superan un umbral de ocupación simulado.
- Practicar el trabajo colaborativo utilizando Git y GitHub.

---

## 6. Datos utilizados

Cada registro del sistema contiene la siguiente información:

| Campo | Descripción |
|---|---|
| `idUsuario` | Identificación del usuario |
| `ruta` | Ruta utilizada |
| `estacion` | Estación donde se registra el movimiento |
| `accion` | Entrada o salida |
| `timestamp` | Fecha y hora del registro |

Los datos utilizados en el proyecto son **simulados**, ya que el objetivo es demostrar el funcionamiento de la programación funcional.

No se afirma que el programa de demostración esté procesando los 6 a 12 millones de registros diarios mencionados en el caso real.

---

## 7. Procesos realizados

El programa realiza los siguientes procesos:

### 7.1 Afluencia por estación

Se cuentan los registros de entrada de pasajeros y se agrupan según la estación.

Esto permite conocer qué cantidad de pasajeros ingresa por cada estación.

---

### 7.2 Identificación de horas pico

Se agrupan los registros de entrada según la hora.

Después se identifica la hora o las horas que presentan el mayor número de entradas.

---

### 7.3 Rutas más utilizadas

Se cuentan los registros de entrada asociados a cada ruta.

Esto permite conocer el volumen de utilización de cada ruta dentro de los datos simulados.

---

### 7.4 Patrones de viaje

Los registros se ordenan por fecha y hora y posteriormente se agrupan por usuario.

De esta forma se puede observar el orden de las estaciones visitadas por cada usuario.

Ejemplo:

```text
U01 → Central → Norte → Universidad
```

--- 

### 7.5 Tiempo promedio entre estaciones

Se revisan los registros de cada usuario en orden cronológico y se calcula una estimación del tiempo transcurrido entre cambios de estación.

El resultado se presenta en minutos.

---

### 7.6 Detección de rutas críticas

Se utiliza un umbral simulado para identificar rutas que presentan un volumen superior al límite establecido.

En este proyecto se utiliza:

```text
Umbral = 5 entradas
```

Una ruta que tenga más de 5 entradas se marca como:

```text
CRITICA
```

---

## 8. Programación funcional aplicada

### 8.1 Funciones puras

Las funciones reciben información y generan un resultado sin modificar directamente la lista original de registros.

Esto facilita la comprensión y el mantenimiento del programa.

---

### 8.2 Inmutabilidad

La clase `RegistroTransporte` utiliza atributos `final.`

Ejemplo:

```java
private final String idUsuario;
private final String ruta;
private final String estacion;
private final String accion;
private final LocalDateTime timestamp;
```

Esto significa que los valores no se pueden cambiar después de crear el objeto.

También se utilizan colecciones protegidas para evitar modificaciones accidentales.

---

### 8.3 Expresiones Lambda

Las expresiones lambda permiten escribir funciones pequeñas de una manera más sencilla.

Ejemplo:

```java
r -> "entrada".equalsIgnoreCase(r.getAccion())
```

Esta expresión permite filtrar los registros cuya acción sea una entrada.

---

### 8.4 Streams

Los Streams permiten trabajar con los datos de manera declarativa.

En el proyecto se utilizan operaciones como:

```java
filter()

collect()

groupingBy()

map()

sorted()

counting()
```
---

### 8.5 Funciones de orden superior

El método `contarPor` recibe funciones como parámetros.

Se utilizan:

```java 
Predicate
```

y

```java 
Function
```

Ejemplo:

```java
public static Map<String,Long> contarPor(
        List<RegistroTransporte> registros,
        Predicate<RegistroTransporte> filtro,
        Function<RegistroTransporte,String> clasificador)
```

Esto permite reutilizar una misma función para realizar diferentes tipos de conteos.

---

## 9. Resultados esperados

Con los datos simulados incluidos en el proyecto se obtienen los siguientes resultados principales:

### Afluencia por estación
```text
Central: 4
Universidad: 3
Centro: 3
Industrial: 2
Aeropuerto: 2
Sur: 1
Norte: 1
```

### Uso de rutas
```text
R1: 7
R2: 5
R3: 4
```

### Rutas críticas

Se utiliza un umbral de:

```text 
5 entradas
```

Por lo tanto, una ruta que supere este valor se marca como:

```text 
CRITICA
```

Con los datos simulados:

```text
R1: CRITICA
R2: NORMAL
R3: NORMAL
```

Los demás resultados, como las horas pico, los patrones de viaje y el tiempo promedio, son calculados directamente por el programa.

---

## 10. Estructura del proyecto

La estructura propuesta para el repositorio es:

```text
EA3-Creacion-Repositorio-Grupo7/
│
├── README.md
│
├── src/
│   └── TecnoMovilData.java
│
├── documento/
│   └── EA3_Creacion_repositorio_Grupo7.pdf
│
├── evidencias/
│   ├── ejecucion_programa.png
│   ├── resultados.png
│   ├── github_ramas.png
│   ├── github_commits.png
│   └── pull_request.png
│
└── video/
    └── enlace_video.txt
```

---

### 11. Conclusiones

La realización de este proyecto permitió aplicar conceptos básicos de programación funcional utilizando Java.

El uso de Streams permitió organizar diferentes operaciones sobre los datos, como filtrar, agrupar, ordenar y contar información.

Las expresiones lambda ayudaron a escribir algunas operaciones de una manera más sencilla y las funciones de orden superior permitieron reutilizar parte de la lógica del programa.

La inmutabilidad también fue importante porque ayuda a evitar cambios accidentales en los datos originales.

Finalmente, el uso de GitHub permite organizar el trabajo de los integrantes mediante ramas, commits, push, pull y Pull Requests.

Los datos utilizados en este proyecto son simulados y sirven para demostrar el funcionamiento de la solución planteada para el caso TecnoMovil.

