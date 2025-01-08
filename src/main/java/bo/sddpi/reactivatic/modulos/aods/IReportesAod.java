package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Reportes;

@Mapper
public interface IReportesAod {

    @Select("select idempresa as id, empresa as entidad, count(idempresa) as cantidad from solicitudes join empresas using(idempresa) group by idempresa, empresa order by count(idempresa) desc;")
    List<Reportes> empresasmassolicitadas();

    @Select("select idcliente as id, primerapellido as entidad, count(idcliente) as cantidad from solicitudes join clientes using(idcliente) join personas using(idpersona) group by idcliente, primerapellido order by count(idcliente) desc;")
    List<Reportes> clientesconmascompras();

    @Select("select idproducto as id, producto as entidad, count(idproducto) as cantidad from solicitudesproductos join productos using(idproducto) group by idproducto, producto order by count(idproducto) desc;")
    List<Reportes> productosmasvendidos();

    @Select("SELECT EXTRACT(YEAR FROM fechareg) AS entidad, COUNT(*) AS cantidad FROM empresas GROUP BY entidad ORDER BY entidad;")
    List<Reportes> empresasporgestion();

    @Select("SELECT r.rubro AS entidad, COUNT(e.idempresa) AS cantidad " + 
            "FROM empresas e " + 
            "JOIN rubros r ON e.idrubro = r.idrubro " + 
            "GROUP BY entidad " + 
            "ORDER BY entidad;")
    List<Reportes> empresasporrubro();
    
    @Select("SELECT m.municipio as entidad, COUNT(e.idempresa) AS cantidad " + 
            "FROM empresas e " + 
            "JOIN municipios m ON e.idmunicipio = m.idmunicipio " + 
            "GROUP BY entidad " + 
            "ORDER BY entidad;")
    List<Reportes> empresaspormunicipio();

    @Select("SELECT DISTINCT EXTRACT(YEAR FROM fechareg) AS gestion FROM empresas ORDER BY gestion;")
    List<Reportes> gestion();

    @Select("SELECT r.rubro as entidad, " + 
            "COUNT(e.idempresa) AS cantidad, " + 
            "EXTRACT(YEAR FROM e.fechareg) AS gestion " + 
            "FROM empresas e " + 
            "JOIN rubros r ON e.idrubro = r.idrubro " + 
            "WHERE EXTRACT(YEAR FROM e.fechareg) = #{ano} " +
            "GROUP BY entidad, gestion " + 
            "ORDER BY gestion ASC, entidad;")
    List<Reportes> empresasporrubrogestion(Integer ano);

    @Select("SELECT r.rubro AS entidad, COUNT(DISTINCT e.idempresa) AS cantidad " +
            "FROM productos prod " +
            "JOIN empresas e ON prod.idempresa = e.idempresa " +
            "JOIN rubros r ON e.idrubro = r.idrubro " +
            "GROUP BY r.rubro " +
            "ORDER BY r.rubro ASC;")
    List<Reportes> empresasEnTienda();
    
}
