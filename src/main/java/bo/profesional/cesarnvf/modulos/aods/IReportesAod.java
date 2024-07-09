package bo.profesional.cesarnvf.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Reportes;

@Mapper
public interface IReportesAod {

    @Select("select idempresa as id, empresa as entidad, count(idempresa) as cantidad from solicitudes join empresas using(idempresa) group by idempresa, empresa order by count(idempresa) desc;")
    List<Reportes> empresasmassolicitadas();

    @Select("select idcliente as id, primerapellido as entidad, count(idcliente) as cantidad from solicitudes join clientes using(idcliente) join personas using(idpersona) group by idcliente, primerapellido order by count(idcliente) desc;")
    List<Reportes> clientesconmascompras();

    @Select("select idproducto as id, producto as entidad, count(idproducto) as cantidad from solicitudesproductos join productos using(idproducto) group by idproducto, producto order by count(idproducto) desc;")
    List<Reportes> productosmasvendidos();
}
