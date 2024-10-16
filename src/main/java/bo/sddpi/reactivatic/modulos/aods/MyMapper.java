package bo.sddpi.reactivatic.modulos.aods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyMapper {
    @SuppressWarnings("unchecked")
    public String construirConsultaDinamica(Map<String, Object> parametros) {
        List<String> columnsemp = (List<String>) parametros.get("columnsemp");
        List<String> columnsrub = (List<String>) parametros.get("columnsrub");
        List<String> columnsmun = (List<String>) parametros.get("columnsmun");
        List<String> columnsrep = (List<String>) parametros.get("columnsrep");
        List<String> columnsper = (List<String>) parametros.get("columnsper");
        String municipio = (String) parametros.get("municipio");
        String rubro = (String) parametros.get("rubro");
        String fecharegistro = (String) parametros.get("fecharegistro");
        String ordenCampo = (String) parametros.get("ordenCampo");
        String ordenDireccion = (String) parametros.get("ordenDireccion");

        Long municipioId = null;
        Long rubroId = null;

        StringBuilder query = new StringBuilder("SELECT ");

        List<String> columnasTotales = new ArrayList<>();

        if (columnsemp != null && !columnsemp.isEmpty()) {
            columnsemp.forEach(col -> columnasTotales.add("e." + col));
        }

        if (columnsrub != null && !columnsrub.isEmpty()) {
            columnsrub.forEach(col -> columnasTotales.add("r." + col));
        }

        if (columnsmun != null && !columnsmun.isEmpty()) {
            columnsmun.forEach(col -> columnasTotales.add("m." + col));
        }

        if (columnsrep != null && !columnsrep.isEmpty()) {
            columnsrep.forEach(col -> columnasTotales.add("rep." + col));
        }

        if (columnsper != null && !columnsper.isEmpty()) {
            columnsper.forEach(col -> columnasTotales.add("p." + col));
        }

        if (!columnasTotales.isEmpty()) {
            query.append(String.join(", ", columnasTotales));
        } else {
            query.append("*");  
        }

        query.append(" FROM empresas e ");

        if (columnsrub != null && !columnsrub.isEmpty()) {
            query.append(" JOIN rubros r ON e.idrubro = r.idrubro ");
        }
        if (columnsmun != null && !columnsmun.isEmpty()) {
            query.append(" LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio ");
        }
        if ((columnsrep != null && !columnsrep.isEmpty()) || (columnsper != null && !columnsper.isEmpty())) {
            query.append(" JOIN representantes rep ON e.idrepresentante = rep.idrepresentante ");
        }
        if (columnsper != null && !columnsper.isEmpty()) {
            query.append(" JOIN personas p ON rep.idpersona = p.idpersona ");
        }

        query.append(" WHERE 1=1 "); 

        if (!municipio.equals("allM")) {
            try {
                municipioId = Long.parseLong(municipio);
                query.append(" AND e.idmunicipio = ").append(municipioId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Municipio debe ser un número válido", e);
            }
        }

        if (!rubro.equals("allR")) {
            try {
                rubroId = Long.parseLong(rubro);  
                query.append(" AND e.idrubro = ").append(rubroId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Rubro debe ser un número válido", e);
            }
        }

        if (!fecharegistro.equals("allReg")) {
            query.append(" AND EXTRACT(YEAR FROM e.fechareg) = ").append(fecharegistro);
        }

        if (ordenCampo != null && !ordenCampo.isEmpty()) {
            if (!ordenDireccion.equalsIgnoreCase("ASC") && !ordenDireccion.equalsIgnoreCase("DESC")) {
                throw new IllegalArgumentException("Dirección de ordenamiento debe ser ASC o DESC");
            }
            query.append(" ORDER BY ").append(ordenCampo).append(" ").append(ordenDireccion);
        }

        //System.out.println("Consulta generada: " + query.toString());

        return query.toString();
    }
}
