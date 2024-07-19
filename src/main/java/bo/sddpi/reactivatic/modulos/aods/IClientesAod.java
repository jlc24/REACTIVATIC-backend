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

    @Select("SELECT * FROM personas INNER JOIN clientes ON idcliente=idpersona")
    List<Clientes> listar();


}
