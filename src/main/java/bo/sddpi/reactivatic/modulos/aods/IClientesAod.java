package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.*;

import bo.sddpi.reactivatic.modulos.entidades.Clientes;

@Mapper
public interface IClientesAod {

    @Insert("insert into clientes(idpersona) values (#{idpersona})")
    void adicionar(Clientes dato);

    @Select("select idcliente, idpersona, idpersona as idfore1 from clientes where idcliente=#{id}")
    @Results({
        @Result(property = "persona", column = "idfore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    Clientes dato(Long id);

    @Select("SELECT c.idcliente, p.idpersona, p.primerapellido, p.segundoapellidos, p.primernombre, p.telefono, p.celular" + 
            "FROM personas AS p" +
            "INNER JOIN clientes AS c ON c.idpersona=p.idpersona")
    List<Clientes> listar();

    @Select("SELECT c.idcliente, p.idpersona, p.primerapellido, p.segundoapellido, p.primernombre, p.telefono, p.celular " + 
            "FROM clientes c " +
            "JOIN personas AS p ON c.idpersona=p.idpersona " + 
            "WHERE p.primerapellido ilike '%'||#{buscar}||'%' OR p.segundoapellido ilike '%'||#{buscar}||'%' OR p.primernombre ilike '%'||#{buscar}||'%' " + 
            "ORDER BY p.created_at DESC LIMIT #{cantidad} OFFSET #{pagina} ")
    @Results({
        @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
    })
    List<Clientes> buscar(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(c.idcliente) " + 
            "FROM clientes c " +
            "JOIN personas AS p ON c.idpersona=p.idpersona ")
    Integer cantidad(String buscar);

    @Delete("DELETE FROM clientes WHERE idpersona=#{idpersona}")
    void eliminar(Long idpersona);

}
