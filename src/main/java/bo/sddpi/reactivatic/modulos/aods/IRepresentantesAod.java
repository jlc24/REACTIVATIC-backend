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

    @Select("SELECT r.idrepresentante, r.idpersona, p.primerapellido, p.segundoapellido, p.primernombre " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%' LIMIT #{cantidad} OFFSET #{pagina}")
        @Results({
            @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        })
    List<Representantes> datos(String buscar, Integer pagina, Integer cantidad);

    @Select("SELECT count(r.idrepresentante) " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip) ILIKE '%'||#{buscar}||'%'")
    Integer cantidad(String buscar);

    @Select("SELECT r.idrepresentante, r.idpersona, p.primerapellido, p.segundoapellido, p.primernombre " +
        "FROM representantes r " +
        "JOIN personas p ON r.idpersona = p.idpersona " +
        "WHERE p.estado=true AND CONCAT(p.primernombre, ' ', p.primerapellido, ' ', p.segundoapellido, ' ', p.dip, ' ') ILIKE '%' || #{buscar} || '%'")
        @Results({
            @Result(property ="persona", column ="idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato")),
        })
    List<Representantes> buscar(String buscar);

    @Select("select idrepresentante, primerapellido as representante from representantes join personas using(idpersona) order by representante")
    List<Representantes> datosl();

    @Select("SELECT * FROM representantes WHERE idrepresentante=#{id} ")
    @Results({
        @Result(property = "persona", column = "idpersona", one = @One(select = "bo.sddpi.reactivatic.modulos.aods.IPersonasAod.dato"))
    })
    Representantes dato(Long id);

    @Delete("delete from representantes where idpersona=#{id}")
    void eliminar(Long id);

}
