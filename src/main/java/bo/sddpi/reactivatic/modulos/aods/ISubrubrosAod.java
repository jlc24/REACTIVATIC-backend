package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Subrubros;

@Mapper
public interface ISubrubrosAod {

    @Select("SELECT idsubrubro, idrubro, subrubro FROM subrubros WHERE idrubro=#{id} ORDER BY subrubro")
    List<Subrubros> datos(Long id);

    @Select("SELECT idsubrubro, idrubro, idrubro as ifore1, subrubro FROM subrubros where idsubrubro=#{id} ")
    @Results({
        @Result(property = "rubro", column = "ifore1", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IRubrosAod.dato"))
    })
    Subrubros dato(Long id);

    @Insert("insert into subrubros(idrubro, subrubro) values (#{idrubro}, #{subrubro})")
    void adicionar(Subrubros dato);

    @Update("update subrubros set idrubro=#{idrubro}, subrubro=#{subrubro} where idsubrubro=#{idsubrubro} ")
    void modificar(Subrubros dato);

    @Select("SELECT idsubrubro, idrubro, subrubro FROM subrubros WHERE idrubro=#{id} AND estado=true ORDER BY subrubro")
    List<Subrubros> datosl(Long id);

    @Select("SELECT * FROM subrubros WHERE idrubro=#{idrubro} AND estado=true")
    List<Subrubros> subrubros(long idrubro);

    @Select("SELECT idsubrubro, subrubro FROM subrubros WHERE estado=true")
    List<Subrubros> listaSubrubros();

    @Select("SELECT idsubrubro FROM subrubros WHERE subrubro=#{subrubro}")
    Long verificarsubrubro(String subrubro);
}
