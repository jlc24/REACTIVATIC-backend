package bo.profesional.cesarnvf.modulos.aods;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.profesional.cesarnvf.modulos.entidades.Clientes;

@Mapper
public interface IClientesAod {

    @Insert("insert into clientes(idpersona) values (#{idpersona})")
    void adicionar(Clientes dato);

    @Select("select idcliente, idpersona, idpersona as idfore1 from clientes where idcliente=#{id}")
    @Results({
        @Result(property = "persona", column = "idfore1", one = @One(select = "bo.profesional.cesarnvf.modulos.aods.IPersonasAod.dato"))
    })
    Clientes dato(Long id);


}
