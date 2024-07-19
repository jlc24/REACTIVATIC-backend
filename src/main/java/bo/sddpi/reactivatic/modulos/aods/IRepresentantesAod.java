package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Representantes;

@Mapper
public interface IRepresentantesAod {

    @Insert("insert into representantes(idpersona) values (#{idpersona})")
    void adicionar(Representantes dato);

    @Select("select idrepresentante, primerapellido as representante from representantes join personas using(idpersona) order by representante")
    List<Representantes> datosl();

    @Select("SELECT idrepresentante, idpersona, idpersona as ifore1 FROM representantes where idrepresentante=#{id} ")
    @Results({
        @Result(property = "persona", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    Representantes dato(Long id);

    @Delete("delete from representantes where idpersona=#{id}")
    void eliminar(Long id);

}
