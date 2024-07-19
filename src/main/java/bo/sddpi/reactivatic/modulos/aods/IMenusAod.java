package bo.sddpi.reactivatic.modulos.aods;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import bo.sddpi.reactivatic.modulos.entidades.Menus;

@Mapper
public interface IMenusAod {

    @Select("select idcategoria as idmenu, categoria as nombre, categorias.ruta as ruta, iconocategoria as icono, idusuario from usuarios join usuariosroles using(idusuario) join roles using(idrol) join enlacesroles using(idrol) join enlaces using(idenlace) join categorias using(idcategoria) where idusuario=#{idusuario} group by idcategoria, categoria, categorias.ruta, iconocategoria, categorias.orden, idusuario order by categorias.orden; ")
    @Results(value = {
        @Result(property = "submenu", column = "{idusuario=idusuario,idcategoria=idmenu}", many = @Many(select = "bo.sddpi.reactivatic.modulos.aods.ISubmenuAod.datos"))
    })
    List<Menus> datos(Long idusuario);

}