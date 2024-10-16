package bo.sddpi.reactivatic.modulos.reportes;

import java.io.IOException;
import java.util.List;

import bo.sddpi.reactivatic.modulos.entidades.Empresas;

public interface IEmpresasRep {

    byte[] datosXLS(List<Empresas> datos, List<String> columnas) throws IOException;

    byte[] datosPDF(List<Empresas> datos) throws IOException;

    // private Map<Integer, String> formacionMap = new HashMap<>() {{
//     put(1, "INICIAL");
//     put(2, "PRIMARIA");
//     put(3, "SECUNDARIA");
//     put(4, "TECNICO");
//     put(5, "LICENCIATURA");
// }};

// private Map<Integer, String> estadocivilMap = new HashMap<>() {{
//     put(1, "SOLTERO(A)");
//     put(2, "CASADO(A)");
//     put(3, "VIUDO(A)");
//     put(4, "CONVIVIENTE");
//     put(5, "SEPARADO(A)");
// }};

// private Map<Integer, String> hijosMap = new HashMap<>() {{
//     put(1, "1 HIJO");
//     put(2, "2 HIJOS");
//     put(3, "3 HIJOS");
//     put(4, "4 HIJOS");
//     put(5, "5 A MAS HIJOS");
//     put(6, "SIN HIJOS");
// }};

// private Map<Integer, String> capacidadMap = new HashMap<>() {{
//     put(1, "1 A 2");
//     put(2, "3 A 5");
//     put(3, "6 A 10");
//     put(4, "11 A 15");
//     put(5, "16 A 25");
//     put(6, "26 A 50");
//     put(7, "> 50");
// }};

// private Map<Integer, String> motivoMap = new HashMap<>() {{
//     put(1, "POR INFLUENCIA FAMILIAR");
//     put(2, "POR EXPERIENCIA LABORAL");
//     put(3, "POR DESPIDO DE TRABAJO");
//     put(4, "POR LA DEMANDA DEL MERCADO");
//     put(5, "OTRO");
//   }};

// private Map<Integer, String> involucradosMap = new HashMap<>() {{
//     put(1, "PAREJA");
//     put(2, "HIJOS");
//     put(3, "HERMANOS");
//     put(4, "PADRES");
//     put(5, "PRIMOS");
//     put(6, "OTROS");
// }};

// private Map<Integer, String> trabajadoresMap = new HashMap<>() {{
//     put(1, "1 A 2");
//     put(2, "3 A 4");
//     put(3, "7 A 8");
//     put(4, "9 A 10");
//     put(5, "10 A 11");
//     put(6, "12 A  MAS");
// }};

// private Map<Integer, String> feriasMap = new HashMap<>() {{
//     put(1, "LOCAL");
//     put(2, "NACIONAL");
//     put(3, "MUNICIPAL");
//     put(4, "INTERNACIONAL");
//     put(5, "NINGUNO");
// }};

}
