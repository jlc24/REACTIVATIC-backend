package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Negocios;

@Mapper
public interface INegociosAod {

    @Select("SELECT * FROM negocios n " +
            "JOIN benficios b ON b.idbeneficio = n.idbeneficio " +
            "LEFT JOIN benficiosempresas be ON be.idbeneficioempresa = n.idbeneficioempresa " +
            "LEFT JOIN empresas e ON e.idempresa = be.idempresa " +
            "LEFT JOIN rubros r ON e.idrubro = r.idrubro " +
            "LEFT JOIN municipios m ON e.idmunicipio = m.idmunicipio " +
            "LEFT JOIN representantes rep ON e.idrepresentante = rep.idrepresentante " +
            "JOIN personas p ON rep.idpersona = p.idpersona " +
            "WHERE idbeneficio=#{beneficio} AND CONCAT(COALESCE(e.empresa, ''), ' ', COALESCE(r.rubro, ''), ' ', COALESCE(m.municipio, ''), ' ', " +
            "COALESCE(p.primerNombre, ''), ' ', COALESCE(p.primerApellido, ''), ' ', COALESCE(p.segundoApellido, ''), ' ', " +
            "COALESCE(p.dip, '')) " +
            "ILIKE '%' || #{buscar} || '%' " +
            "ORDER BY n.horainicio, n.estadoempresa, n.estadopersona ASC ")   
    @Results({
        @Result(property = "beneficioempresa", column = "idbeneficioempresa", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod.dato"))
    })
    List<Negocios> datos(String buscar, Long beneficio);
}
