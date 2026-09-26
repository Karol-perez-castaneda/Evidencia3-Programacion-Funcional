import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TecnoMovilData {

    // Formato utilizado para las fechas
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // =========================================================
    // CLASE REGISTRO
    // =========================================================

    static class RegistroTransporte {

        private final String idUsuario;
        private final String ruta;
        private final String estacion;
        private final String accion;
        private final LocalDateTime timestamp;

        public RegistroTransporte(
                String idUsuario,
                String ruta,
                String estacion,
                String accion,
                String timestamp) {

            this.idUsuario = idUsuario;
            this.ruta = ruta;
            this.estacion = estacion;
            this.accion = accion;
            this.timestamp = LocalDateTime.parse(timestamp, FORMATO);
        }

        public String getIdUsuario() {
            return idUsuario;
        }

        public String getRuta() {
            return ruta;
        }

        public String getEstacion() {
            return estacion;
        }

        public String getAccion() {
            return accion;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return idUsuario + " | "
                    + ruta + " | "
                    + estacion + " | "
                    + accion + " | "
                    + timestamp;
        }
    }

    // =========================================================
    // 1. AFLUENCIA POR ESTACIÓN
    // =========================================================

    static Map<String, Long> calcularAfluenciaPorEstacion(
            List<RegistroTransporte> registros) {

        return Collections.unmodifiableMap(
                registros.stream()
                        .filter(r -> r.getAccion().equals("entrada"))
                        .collect(Collectors.groupingBy(
                                RegistroTransporte::getEstacion,
                                TreeMap::new,
                                Collectors.counting()
                        ))
        );
    }

    // =========================================================
    // 2. HORAS PICO
    // =========================================================

    static Map<Integer, Long> calcularFlujoPorHora(
            List<RegistroTransporte> registros) {

        return Collections.unmodifiableMap(
                registros.stream()
                        .filter(r -> r.getAccion().equals("entrada"))
                        .collect(Collectors.groupingBy(
                                r -> r.getTimestamp().getHour(),
                                TreeMap::new,
                                Collectors.counting()
                        ))
        );
    }

    static List<Map.Entry<Integer, Long>> obtenerHorasPico(
            List<RegistroTransporte> registros) {

        return Collections.unmodifiableList(
                calcularFlujoPorHora(registros)
                        .entrySet()
                        .stream()
                        .sorted(
                                Map.Entry.<Integer, Long>
                                        comparingByValue()
                                        .reversed()
                        )
                        .collect(Collectors.toList())
        );
    }

    // =========================================================
    // 3. RUTAS MÁS UTILIZADAS
    // =========================================================

    static Map<String, Long> calcularUsoPorRuta(
            List<RegistroTransporte> registros) {

        return Collections.unmodifiableMap(
                registros.stream()
                        .filter(r -> r.getAccion().equals("entrada"))
                        .collect(Collectors.groupingBy(
                                RegistroTransporte::getRuta,
                                TreeMap::new,
                                Collectors.counting()
                        ))
        );
    }

    // =========================================================
    // 4. PATRONES DE VIAJE POR USUARIO
    // =========================================================

    static Map<String, List<String>> calcularPatronesDeViaje(
            List<RegistroTransporte> registros) {

        Map<String, List<String>> resultado =
                registros.stream()
                        .sorted(Comparator.comparing(
                                RegistroTransporte::getTimestamp))
                        .collect(Collectors.groupingBy(
                                RegistroTransporte::getIdUsuario,
                                TreeMap::new,
                                Collectors.mapping(
                                        RegistroTransporte::getEstacion,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                Collections::unmodifiableList
                                        )
                                )
                        ));

        return Collections.unmodifiableMap(resultado);
    }

    // =========================================================
    // 5. TIEMPO PROMEDIO ENTRE ESTACIONES
    // =========================================================

    static double calcularTiempoPromedioEntreEstaciones(
            List<RegistroTransporte> registros) {

        Map<String, List<RegistroTransporte>> porUsuario =
                registros.stream()
                        .collect(Collectors.groupingBy(
                                RegistroTransporte::getIdUsuario));

        List<Long> tiempos = porUsuario.values()
                .stream()
                .map(lista ->
                        lista.stream()
                                .sorted(Comparator.comparing(
                                        RegistroTransporte::getTimestamp))
                                .collect(Collectors.toList())
                )
                .flatMap(lista ->
                        crearDiferencias(lista).stream())
                .collect(Collectors.toList());

        return tiempos.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    static List<Long> crearDiferencias(
            List<RegistroTransporte> registros) {

        List<Long> diferencias = new ArrayList<>();

        for (int i = 0; i < registros.size() - 1; i++) {

            long minutos = Duration.between(
                    registros.get(i).getTimestamp(),
                    registros.get(i + 1).getTimestamp()
            ).toMinutes();

            diferencias.add(minutos);
        }

        return diferencias;
    }

    // =========================================================
    // 6. DETECCIÓN DE RUTAS CRÍTICAS
    // =========================================================

    static Map<String, String> detectarRutasCriticas(
            List<RegistroTransporte> registros,
            long limite) {

        return Collections.unmodifiableMap(
                calcularUsoPorRuta(registros)
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entrada -> entrada.getValue() > limite
                                        ? "CRÍTICA"
                                        : "NORMAL",
                                (a, b) -> a,
                                TreeMap::new
                        ))
        );
    }

    // =========================================================
    // MÉTODO PRINCIPAL
    // =========================================================

    public static void main(String[] args) {

        // -----------------------------------------------------
        // DATOS SIMULADOS
        // -----------------------------------------------------

        List<RegistroTransporte> registros =
                Collections.unmodifiableList(Arrays.asList(

                        new RegistroTransporte(
                                "U01", "R1", "Central",
                                "entrada", "2026-09-05 07:10"),

                        new RegistroTransporte(
                                "U01", "R1", "Norte",
                                "salida", "2026-09-05 07:28"),

                        new RegistroTransporte(
                                "U01", "R2", "Universidad",
                                "entrada", "2026-09-05 07:45"),

                        new RegistroTransporte(
                                "U02", "R1", "Central",
                                "entrada", "2026-09-05 07:15"),

                        new RegistroTransporte(
                                "U02", "R1", "Norte",
                                "salida", "2026-09-05 07:35"),

                        new RegistroTransporte(
                                "U02", "R1", "Centro",
                                "entrada", "2026-09-05 08:05"),

                        new RegistroTransporte(
                                "U03", "R2", "Universidad",
                                "entrada", "2026-09-05 08:10"),

                        new RegistroTransporte(
                                "U03", "R2", "Centro",
                                "salida", "2026-09-05 08:30"),

                        new RegistroTransporte(
                                "U03", "R2", "Sur",
                                "entrada", "2026-09-05 09:00"),

                        new RegistroTransporte(
                                "U04", "R3", "Industrial",
                                "entrada", "2026-09-05 09:10"),

                        new RegistroTransporte(
                                "U04", "R3", "Centro",
                                "salida", "2026-09-05 09:35"),

                        new RegistroTransporte(
                                "U04", "R3", "Aeropuerto",
                                "entrada", "2026-09-05 10:05"),

                        new RegistroTransporte(
                                "U05", "R1", "Central",
                                "entrada", "2026-09-05 10:10"),

                        new RegistroTransporte(
                                "U05", "R1", "Centro",
                                "salida", "2026-09-05 10:25"),

                        new RegistroTransporte(
                                "U05", "R1", "Norte",
                                "entrada", "2026-09-05 11:00"),

                        new RegistroTransporte(
                                "U06", "R2", "Universidad",
                                "entrada", "2026-09-05 12:00"),

                        new RegistroTransporte(
                                "U06", "R2", "Sur",
                                "salida", "2026-09-05 12:25"),

                        new RegistroTransporte(
                                "U06", "R2", "Centro",
                                "entrada", "2026-09-05 13:10"),

                        new RegistroTransporte(
                                "U07", "R3", "Industrial",
                                "entrada", "2026-09-05 17:05"),

                        new RegistroTransporte(
                                "U07", "R3", "Centro",
                                "salida", "2026-09-05 17:30"),

                        new RegistroTransporte(
                                "U07", "R3", "Aeropuerto",
                                "entrada", "2026-09-05 18:00"),

                        new RegistroTransporte(
                                "U08", "R1", "Central",
                                "entrada", "2026-09-05 18:10"),

                        new RegistroTransporte(
                                "U08", "R1", "Norte",
                                "salida", "2026-09-05 18:30"),

                        new RegistroTransporte(
                                "U08", "R1", "Centro",
                                "entrada", "2026-09-05 19:00")
                ));

        // -----------------------------------------------------
        // MOSTRAR RESULTADOS
        // -----------------------------------------------------

        System.out.println("======================================");
        System.out.println("       TECNOMOVIL DATA");
        System.out.println("======================================");

        System.out.println("\n1. AFLUENCIA POR ESTACIÓN");

        calcularAfluenciaPorEstacion(registros)
                .forEach((estacion, cantidad) ->
                        System.out.println(
                                estacion + ": "
                                        + cantidad + " entradas"));

        System.out.println("\n2. HORAS PICO");

        obtenerHorasPico(registros)
                .stream()
                .limit(5)
                .forEach(resultado ->
                        System.out.println(
                                resultado.getKey()
                                        + ":00 -> "
                                        + resultado.getValue()
                                        + " entradas"));

        System.out.println("\n3. RUTAS MÁS UTILIZADAS");

        calcularUsoPorRuta(registros)
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>
                                comparingByValue()
                                .reversed()
                )
                .forEach(resultado ->
                        System.out.println(
                                resultado.getKey()
                                        + ": "
                                        + resultado.getValue()
                                        + " usos"));

        System.out.println("\n4. PATRONES DE VIAJE");

        calcularPatronesDeViaje(registros)
                .forEach((usuario, estaciones) ->
                        System.out.println(
                                usuario + ": "
                                        + String.join(
                                                " -> ",
                                                estaciones)));

        System.out.println("\n5. TIEMPO PROMEDIO");

        double promedio =
                calcularTiempoPromedioEntreEstaciones(
                        registros);

        System.out.printf(
                "Tiempo promedio: %.2f minutos%n",
                promedio);

        System.out.println("\n6. DETECCIÓN DE RUTAS CRÍTICAS");

        detectarRutasCriticas(registros, 5)
                .forEach((ruta, estado) ->
                        System.out.println(
                                ruta + ": " + estado));

        System.out.println("\n======================================");
        System.out.println("Procesamiento finalizado");
        System.out.println("======================================");
    }
}
