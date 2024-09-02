package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import bo.sddpi.reactivatic.modulos.entidades.Rubros;

@Mapper
public interface IRubrosAod {

    @Select("SELECT * FROM rubros WHERE rubro ilike '%'||#{buscar}||'%' ORDER BY idrubro LIMIT #{cantidad} OFFSET #{pagina} ")
    List<Rubros> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(idrubro) FROM rubros where rubro ilike '%'||#{buscar}||'%' ")
    Integer cantidad(String buscar);

    @Select("SELECT idrubro, rubro FROM rubros where idrubro=#{id} ")
    Rubros dato(Long id);

    @Insert("insert into rubros(rubro) values (#{rubro})")
    void adicionar(Rubros dato);

    @Update("update rubros set rubro=#{rubro} where idrubro=#{idrubro} ")
    void modificar(Rubros dato);

    @Select("SELECT idrubro, rubro FROM rubros WHERE estado=true ORDER BY rubro")
    List<Rubros> datosl();

    @Select("SELECT idrubro, rubro FROM rubros WHERE estado=true AND rubro=#{rubro} ORDER BY rubro")
    List<Rubros> datoslrubro(String rubro);

    @Select("select rubro, count(idproducto) as cantidad from rubros join subrubros using(idrubro) join empresas using(idsubrubro) left join productos using(idempresa) where cantidad>0 group by rubro order by rubro")
    List<Rubros> cantidadporrubro();

    @Update("UPDATE rubros SET estado=#{estado} WHERE idrubro=#{idrubro}")
    void cambiarestado(Rubros rubro);

    @Select("SELECT idrubro FROM rubros WHERE rubro=#{rubro}")
    Long verificarrubro(String rubro);
}
