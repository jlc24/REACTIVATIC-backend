PGDMP  (        
            |            reactivaticdb    16.3    16.3    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16398    reactivaticdb    DATABASE     �   CREATE DATABASE reactivaticdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Spain.1252';
    DROP DATABASE reactivaticdb;
                postgres    false                        3079    16399    pgcrypto 	   EXTENSION     <   CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;
    DROP EXTENSION pgcrypto;
                   false            �           0    0    EXTENSION pgcrypto    COMMENT     <   COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';
                        false    2            N           1255    16436 "   procesasolicitud(integer, integer)    FUNCTION     q  CREATE FUNCTION public.procesasolicitud(_idclientecarrito integer, _idusuario integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
    _empresas RECORD;
    _productos RECORD;
    _idsolicitud INTEGER;
    _idcliente INTEGER;
BEGIN
    -- Obtener el ID del cliente
    SELECT c.idcliente 
    INTO _idcliente 
    FROM clientes c
    JOIN personas p ON p.idpersona=c.idpersona 
    JOIN usuarios u ON u.idpersona=c.idpersona 
    WHERE idusuario = _idusuario;
    
    -- Iterar sobre las empresas distintas del carrito del cliente
    FOR _empresas IN
        SELECT DISTINCT idempresa
        FROM carritos 
        JOIN productos USING(idproducto) 
        JOIN empresas USING(idempresa)
        WHERE idcliente = _idclientecarrito
    LOOP
        -- Insertar en la tabla solicitudes y obtener el idsolicitud
        INSERT INTO solicitudes (idcliente, solicitud, idempresa) 
        VALUES (_idcliente, 'Solicitud de compra', _empresas.idempresa)
        RETURNING idsolicitud INTO _idsolicitud;
        
        -- Iterar sobre los productos del carrito para esa empresa
        FOR _productos IN
            SELECT c.idproducto, c.imagen, c.idprecio, c.idcolor, c.idmaterial, c.idtamano, c.cantidad
            FROM carritos c
            JOIN productos p USING(idproducto)
            JOIN empresas e USING(idempresa)
            WHERE c.idcliente = _idclientecarrito 
              AND e.idempresa = _empresas.idempresa
        LOOP
            -- Insertar cada producto en la tabla solicitudesproductos
            INSERT INTO solicitudesproductos 
            (idsolicitud, idproducto, imagen, idprecio, idcolor, idmaterial, idtamano, cantidad) 
            VALUES (_idsolicitud, _productos.idproducto, _productos.imagen, _productos.idprecio, _productos.idcolor, _productos.idmaterial, _productos.idtamano, _productos.cantidad);
        END LOOP;
    END LOOP;

    RETURN 1;
END;$$;
 V   DROP FUNCTION public.procesasolicitud(_idclientecarrito integer, _idusuario integer);
       public          postgres    false            M           1255    16437     procesasolicitud(bigint, bigint)    FUNCTION     o  CREATE FUNCTION public.procesasolicitud(_idclientecarrito bigint, _idusuario bigint) RETURNS integer
    LANGUAGE plpgsql
    AS $$DECLARE
    _empresas RECORD;
    _productos RECORD;
    _idsolicitud INTEGER;
    _idcliente INTEGER;
BEGIN
    -- Obtener el ID del cliente
    SELECT c.idcliente 
    INTO _idcliente 
    FROM clientes c
    JOIN personas p ON p.idpersona=c.idpersona 
    JOIN usuarios u ON u.idpersona=c.idpersona 
    WHERE idusuario = _idusuario;
    
    -- Iterar sobre las empresas distintas del carrito del cliente
    FOR _empresas IN
        SELECT DISTINCT idempresa
        FROM carritos 
        JOIN productos USING(idproducto) 
        JOIN empresas USING(idempresa)
        WHERE idcliente = _idclientecarrito
    LOOP
        -- Insertar en la tabla solicitudes y obtener el idsolicitud
        INSERT INTO solicitudes (idcliente, solicitud, idempresa) 
        VALUES (_idcliente, 'Solicitud de compra', _empresas.idempresa)
        RETURNING idsolicitud INTO _idsolicitud;
        
        -- Iterar sobre los productos del carrito para esa empresa
        FOR _productos IN
            SELECT c.idproducto, c.imagen, c.idprecio, c.idcolor, c.idmaterial, c.idtamano, c.cantidad
            FROM carritos c
            JOIN productos p USING(idproducto)
            JOIN empresas e USING(idempresa)
            WHERE c.idcliente = _idclientecarrito 
              AND e.idempresa = _empresas.idempresa
        LOOP
            -- Insertar cada producto en la tabla solicitudesproductos
            INSERT INTO solicitudesproductos 
            (idsolicitud, idproducto, imagen, idprecio, idcolor, idmaterial, idtamano, cantidad) 
            VALUES (_idsolicitud, _productos.idproducto, _productos.imagen, _productos.idprecio, _productos.idcolor, _productos.idmaterial, _productos.idtamano, _productos.cantidad);
        END LOOP;
    END LOOP;

    RETURN 1;
END;$$;
 T   DROP FUNCTION public.procesasolicitud(_idclientecarrito bigint, _idusuario bigint);
       public          postgres    false            �            1259    16438    aaaa    TABLE     �  CREATE TABLE public.aaaa (
    no double precision,
    nombre character varying(255),
    direccion character varying(255),
    municipio_ character varying(255),
    razonsocial character varying(255),
    subrubro character varying(255),
    actividad character varying(255),
    telefono character varying(255),
    id_rubro double precision,
    id_municipio double precision,
    carnet double precision
);
    DROP TABLE public.aaaa;
       public         heap    postgres    false                       1259    25181    archivos_idarchivo_seq    SEQUENCE        CREATE SEQUENCE public.archivos_idarchivo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.archivos_idarchivo_seq;
       public          postgres    false                       1259    16932    archivos    TABLE     �  CREATE TABLE public.archivos (
    idarchivo integer DEFAULT nextval('public.archivos_idarchivo_seq'::regclass) NOT NULL,
    idusuario integer,
    idpersona integer,
    idempresa integer,
    idproducto integer,
    filename character varying(255),
    data bytea,
    mimetype character varying(50),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.archivos;
       public         heap    postgres    false    282            �            1259    16443    asociaciones    TABLE     �  CREATE TABLE public.asociaciones (
    idasociacion integer NOT NULL,
    asociacion character varying,
    descripcion character varying,
    fechacreacion date,
    representantelegal character varying,
    direccion character varying,
    telefono character varying,
    celular character varying,
    correo character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
     DROP TABLE public.asociaciones;
       public         heap    postgres    false            �            1259    16450    asociaciones_idasociacion_seq    SEQUENCE     �   CREATE SEQUENCE public.asociaciones_idasociacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.asociaciones_idasociacion_seq;
       public          postgres    false    217            �           0    0    asociaciones_idasociacion_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.asociaciones_idasociacion_seq OWNED BY public.asociaciones.idasociacion;
          public          postgres    false    218                       1259    25155    atributos_idatributo_seq    SEQUENCE     �   CREATE SEQUENCE public.atributos_idatributo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.atributos_idatributo_seq;
       public          postgres    false                       1259    16889 	   atributos    TABLE     H  CREATE TABLE public.atributos (
    idatributo integer DEFAULT nextval('public.atributos_idatributo_seq'::regclass) NOT NULL,
    idproducto integer,
    atributo character varying(100),
    detalle character varying(150),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.atributos;
       public         heap    postgres    false    281                       1259    25230    beneficios_idbeneficio_seq    SEQUENCE     �   CREATE SEQUENCE public.beneficios_idbeneficio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.beneficios_idbeneficio_seq;
       public          postgres    false            	           1259    16814 
   beneficios    TABLE     5  CREATE TABLE public.beneficios (
    idbeneficio integer DEFAULT nextval('public.beneficios_idbeneficio_seq'::regclass) NOT NULL,
    beneficio character varying(150),
    descripcion character varying(255),
    idtipobeneficio integer,
    idmunicipio integer,
    direccion character varying(255),
    fechainicio timestamp without time zone,
    fechafin timestamp without time zone,
    idcapacitador integer,
    capacidad integer,
    idusuario integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.beneficios;
       public         heap    postgres    false    285            
           1259    16821    beneficiosempresas    TABLE     �   CREATE TABLE public.beneficiosempresas (
    idbeneficioempresa integer NOT NULL,
    idbeneficio integer,
    idempresa integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 &   DROP TABLE public.beneficiosempresas;
       public         heap    postgres    false            �            1259    16451 	   bitacoras    TABLE     �   CREATE TABLE public.bitacoras (
    idbitacora integer NOT NULL,
    idusuario integer,
    bitacora character varying,
    fechabitacora date DEFAULT now(),
    horabitacora time without time zone DEFAULT now()
);
    DROP TABLE public.bitacoras;
       public         heap    postgres    false            �            1259    16458    bitacoras_idbitacora_seq    SEQUENCE     �   CREATE SEQUENCE public.bitacoras_idbitacora_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.bitacoras_idbitacora_seq;
       public          postgres    false    219            �           0    0    bitacoras_idbitacora_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.bitacoras_idbitacora_seq OWNED BY public.bitacoras.idbitacora;
          public          postgres    false    220                       1259    16960    cargos_idcargo_seq    SEQUENCE     {   CREATE SEQUENCE public.cargos_idcargo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.cargos_idcargo_seq;
       public          postgres    false                       1259    16948    cargos    TABLE       CREATE TABLE public.cargos (
    idcargo integer DEFAULT nextval('public.cargos_idcargo_seq'::regclass) NOT NULL,
    idrol integer,
    cargo character varying(255),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.cargos;
       public         heap    postgres    false    276            �            1259    16459    carritos    TABLE     H  CREATE TABLE public.carritos (
    idcarrito integer NOT NULL,
    idcliente integer,
    idproducto integer,
    imagen character varying(255),
    idprecio integer,
    idcolor integer,
    idmaterial integer,
    idtamano integer,
    cantidad integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.carritos;
       public         heap    postgres    false            �            1259    16463    carritos_idcarrito_seq    SEQUENCE     �   CREATE SEQUENCE public.carritos_idcarrito_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.carritos_idcarrito_seq;
       public          postgres    false    221            �           0    0    carritos_idcarrito_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.carritos_idcarrito_seq OWNED BY public.carritos.idcarrito;
          public          postgres    false    222            �            1259    16464 
   categorias    TABLE     "  CREATE TABLE public.categorias (
    idcategoria integer NOT NULL,
    categoria character varying,
    ruta character varying,
    iconocategoria character varying,
    orden integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.categorias;
       public         heap    postgres    false            �            1259    16471    categorias_idcategoria_seq    SEQUENCE     �   CREATE SEQUENCE public.categorias_idcategoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.categorias_idcategoria_seq;
       public          postgres    false    223            �           0    0    categorias_idcategoria_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.categorias_idcategoria_seq OWNED BY public.categorias.idcategoria;
          public          postgres    false    224            �            1259    16472    clientes    TABLE     X   CREATE TABLE public.clientes (
    idcliente integer NOT NULL,
    idpersona integer
);
    DROP TABLE public.clientes;
       public         heap    postgres    false            �            1259    16475    clientes_idcliente_seq    SEQUENCE     �   CREATE SEQUENCE public.clientes_idcliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.clientes_idcliente_seq;
       public          postgres    false    225            �           0    0    clientes_idcliente_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.clientes_idcliente_seq OWNED BY public.clientes.idcliente;
          public          postgres    false    226                       1259    25148    colores_idcolor_seq    SEQUENCE     |   CREATE SEQUENCE public.colores_idcolor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.colores_idcolor_seq;
       public          postgres    false                       1259    16877    colores    TABLE     9  CREATE TABLE public.colores (
    idcolor integer DEFAULT nextval('public.colores_idcolor_seq'::regclass) NOT NULL,
    idproducto integer,
    color character varying(20),
    codigo character varying(150),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.colores;
       public         heap    postgres    false    278                       1259    16836    comentarios    TABLE       CREATE TABLE public.comentarios (
    idcomentario integer NOT NULL,
    comentario character varying(255),
    idcliente integer,
    valoracion integer,
    idproducto integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.comentarios;
       public         heap    postgres    false            �            1259    16476    empresas    TABLE       CREATE TABLE public.empresas (
    idempresa integer NOT NULL,
    idsubrubro integer,
    idlocalidad integer,
    idrepresentante integer,
    idasociacion integer,
    empresa character varying,
    tipo character varying,
    direccion character varying,
    telefono character varying,
    celular character varying,
    correo character varying,
    facebook character varying,
    twitter character varying,
    instagram character varying,
    paginaweb character varying,
    nform character varying,
    registrosenasag integer,
    latitud double precision,
    longitud double precision,
    descripcion character varying,
    nit character varying(50),
    bancamovil boolean,
    fechaapertura date,
    servicios character varying(100),
    capacidad integer,
    unidadmedida character varying(150),
    motivo integer,
    otromotivo character varying(100),
    familiar boolean,
    involucrados integer,
    otrosinvolucrados character varying(100),
    trabajadores integer,
    participacion integer,
    capacitacion character varying(255),
    zona character varying(100),
    referencia character varying(150),
    transporte character varying(150),
    idusuario integer,
    fechareg date,
    razonsocial character varying(150),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idmunicipio integer,
    idrubro integer
);
    DROP TABLE public.empresas;
       public         heap    postgres    false            �            1259    16483    empresas_idempresa_seq    SEQUENCE     �   CREATE SEQUENCE public.empresas_idempresa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 -   DROP SEQUENCE public.empresas_idempresa_seq;
       public          postgres    false    227            �           0    0    empresas_idempresa_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.empresas_idempresa_seq OWNED BY public.empresas.idempresa;
          public          postgres    false    228            �            1259    16484    enlaces    TABLE     /  CREATE TABLE public.enlaces (
    idenlace integer NOT NULL,
    idcategoria integer,
    enlace character varying,
    ruta character varying,
    iconoenlace character varying,
    orden integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.enlaces;
       public         heap    postgres    false            �            1259    16491    enlaces_idenlace_seq    SEQUENCE     �   CREATE SEQUENCE public.enlaces_idenlace_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.enlaces_idenlace_seq;
       public          postgres    false    229            �           0    0    enlaces_idenlace_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.enlaces_idenlace_seq OWNED BY public.enlaces.idenlace;
          public          postgres    false    230            �            1259    16492    enlacesroles    TABLE     p   CREATE TABLE public.enlacesroles (
    idenlacerol integer NOT NULL,
    idenlace integer,
    idrol integer
);
     DROP TABLE public.enlacesroles;
       public         heap    postgres    false            �            1259    16495    enlacesroles_idenlacerol_seq    SEQUENCE     �   CREATE SEQUENCE public.enlacesroles_idenlacerol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.enlacesroles_idenlacerol_seq;
       public          postgres    false    231            �           0    0    enlacesroles_idenlacerol_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.enlacesroles_idenlacerol_seq OWNED BY public.enlacesroles.idenlacerol;
          public          postgres    false    232            �            1259    16496    localidades    TABLE     �   CREATE TABLE public.localidades (
    idlocalidad integer NOT NULL,
    idmunicipio integer,
    localidad character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.localidades;
       public         heap    postgres    false            �            1259    16503    localidades_idlocalidad_seq    SEQUENCE     �   CREATE SEQUENCE public.localidades_idlocalidad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 2   DROP SEQUENCE public.localidades_idlocalidad_seq;
       public          postgres    false    233            �           0    0    localidades_idlocalidad_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.localidades_idlocalidad_seq OWNED BY public.localidades.idlocalidad;
          public          postgres    false    234                       1259    25150    materiales_idmaterial_seq    SEQUENCE     �   CREATE SEQUENCE public.materiales_idmaterial_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.materiales_idmaterial_seq;
       public          postgres    false                       1259    16853 
   materiales    TABLE     &  CREATE TABLE public.materiales (
    idmaterial integer DEFAULT nextval('public.materiales_idmaterial_seq'::regclass) NOT NULL,
    idproducto integer,
    material character varying(150),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.materiales;
       public         heap    postgres    false    279            �            1259    16504 
   municipios    TABLE     �   CREATE TABLE public.municipios (
    idmunicipio integer NOT NULL,
    municipio character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.municipios;
       public         heap    postgres    false            �            1259    16511    municipios_idmunicipio_seq    SEQUENCE     �   CREATE SEQUENCE public.municipios_idmunicipio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 1   DROP SEQUENCE public.municipios_idmunicipio_seq;
       public          postgres    false    235            �           0    0    municipios_idmunicipio_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.municipios_idmunicipio_seq OWNED BY public.municipios.idmunicipio;
          public          postgres    false    236                       1259    16920    ofertas    TABLE     �   CREATE TABLE public.ofertas (
    idoferta integer NOT NULL,
    idproducto integer,
    oferta numeric(5,2),
    duracion timestamp with time zone,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.ofertas;
       public         heap    postgres    false            �            1259    16512 
   parametros    TABLE     �   CREATE TABLE public.parametros (
    idparametro integer NOT NULL,
    idusuario integer,
    parametro character varying,
    valor character varying
);
    DROP TABLE public.parametros;
       public         heap    postgres    false            �            1259    16517    parametros_idparametro_seq    SEQUENCE     �   CREATE SEQUENCE public.parametros_idparametro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 1   DROP SEQUENCE public.parametros_idparametro_seq;
       public          postgres    false    237            �           0    0    parametros_idparametro_seq    SEQUENCE OWNED BY     Y   ALTER SEQUENCE public.parametros_idparametro_seq OWNED BY public.parametros.idparametro;
          public          postgres    false    238            �            1259    16518    personas    TABLE     �  CREATE TABLE public.personas (
    idpersona integer NOT NULL,
    idtipogenero integer,
    primerapellido character varying,
    segundoapellido character varying,
    primernombre character varying,
    segundonombre character varying,
    fechanacimiento date,
    dip character varying,
    complementario character varying,
    idtipodocumento integer,
    idtipoextension integer,
    direccion character varying,
    telefono character varying,
    celular character varying,
    correo character varying,
    formacion integer,
    estadocivil integer,
    hijos integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.personas;
       public         heap    postgres    false            �            1259    16525    personas_idpersona_seq    SEQUENCE     �   CREATE SEQUENCE public.personas_idpersona_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 -   DROP SEQUENCE public.personas_idpersona_seq;
       public          postgres    false    239            �           0    0    personas_idpersona_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.personas_idpersona_seq OWNED BY public.personas.idpersona;
          public          postgres    false    240                       1259    25144    precios_idprecio_seq    SEQUENCE     }   CREATE SEQUENCE public.precios_idprecio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.precios_idprecio_seq;
       public          postgres    false                       1259    16903    precios    TABLE     L  CREATE TABLE public.precios (
    idprecio integer DEFAULT nextval('public.precios_idprecio_seq'::regclass) NOT NULL,
    idproducto integer,
    precio numeric(10,2),
    idtamano integer,
    cantidad character varying(150),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.precios;
       public         heap    postgres    false    277            �            1259    16526 	   productos    TABLE     Y  CREATE TABLE public.productos (
    idproducto integer NOT NULL,
    idempresa integer,
    producto character varying,
    descripcion character varying,
    preciocompra numeric(15,2),
    precioventa numeric(15,2),
    cantidad integer,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.productos;
       public         heap    postgres    false            �            1259    16533    productos_idproducto_seq    SEQUENCE     �   CREATE SEQUENCE public.productos_idproducto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.productos_idproducto_seq;
       public          postgres    false    241            �           0    0    productos_idproducto_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.productos_idproducto_seq OWNED BY public.productos.idproducto;
          public          postgres    false    242            �            1259    16534    representantes    TABLE     �   CREATE TABLE public.representantes (
    idrepresentante integer NOT NULL,
    idpersona integer,
    representante character varying
);
 "   DROP TABLE public.representantes;
       public         heap    postgres    false            �            1259    16539 "   representantes_idrepresentante_seq    SEQUENCE     �   CREATE SEQUENCE public.representantes_idrepresentante_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 9   DROP SEQUENCE public.representantes_idrepresentante_seq;
       public          postgres    false    243            �           0    0 "   representantes_idrepresentante_seq    SEQUENCE OWNED BY     i   ALTER SEQUENCE public.representantes_idrepresentante_seq OWNED BY public.representantes.idrepresentante;
          public          postgres    false    244            �            1259    16540    roles    TABLE     �   CREATE TABLE public.roles (
    idrol integer NOT NULL,
    rol character varying,
    nombrerol character varying(50),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.roles;
       public         heap    postgres    false            �            1259    16546    roles_idrol_seq    SEQUENCE     �   CREATE SEQUENCE public.roles_idrol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.roles_idrol_seq;
       public          postgres    false    245            �           0    0    roles_idrol_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.roles_idrol_seq OWNED BY public.roles.idrol;
          public          postgres    false    246            �            1259    16547    rubros    TABLE     �   CREATE TABLE public.rubros (
    idrubro integer NOT NULL,
    rubro character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.rubros;
       public         heap    postgres    false            �            1259    16554    rubros_idrubro_seq    SEQUENCE     �   CREATE SEQUENCE public.rubros_idrubro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 )   DROP SEQUENCE public.rubros_idrubro_seq;
       public          postgres    false    247            �           0    0    rubros_idrubro_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.rubros_idrubro_seq OWNED BY public.rubros.idrubro;
          public          postgres    false    248            �            1259    16555    solicitudes    TABLE     �   CREATE TABLE public.solicitudes (
    idsolicitud integer NOT NULL,
    idcliente integer,
    solicitud character varying,
    idempresa integer,
    estado boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.solicitudes;
       public         heap    postgres    false            �            1259    16562    solicitudes_idsolicitud_seq    SEQUENCE     �   CREATE SEQUENCE public.solicitudes_idsolicitud_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.solicitudes_idsolicitud_seq;
       public          postgres    false    249            �           0    0    solicitudes_idsolicitud_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.solicitudes_idsolicitud_seq OWNED BY public.solicitudes.idsolicitud;
          public          postgres    false    250            �            1259    16563    solicitudesproductos    TABLE     �  CREATE TABLE public.solicitudesproductos (
    idsolicitudproducto integer NOT NULL,
    idsolicitud integer,
    idproducto integer,
    imagen character varying(255),
    idprecio integer,
    idcolor integer,
    idmaterial integer,
    idtamano integer,
    cantidad integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT false
);
 (   DROP TABLE public.solicitudesproductos;
       public         heap    postgres    false            �            1259    16566 ,   solicitudesproductos_idsolicitudproducto_seq    SEQUENCE     �   CREATE SEQUENCE public.solicitudesproductos_idsolicitudproducto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 C   DROP SEQUENCE public.solicitudesproductos_idsolicitudproducto_seq;
       public          postgres    false    251            �           0    0 ,   solicitudesproductos_idsolicitudproducto_seq    SEQUENCE OWNED BY     }   ALTER SEQUENCE public.solicitudesproductos_idsolicitudproducto_seq OWNED BY public.solicitudesproductos.idsolicitudproducto;
          public          postgres    false    252            �            1259    16567 	   subrubros    TABLE     �   CREATE TABLE public.subrubros (
    idsubrubro integer NOT NULL,
    idrubro integer,
    subrubro character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.subrubros;
       public         heap    postgres    false            �            1259    16574    subrubros_idsubrubro_seq    SEQUENCE     �   CREATE SEQUENCE public.subrubros_idsubrubro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 /   DROP SEQUENCE public.subrubros_idsubrubro_seq;
       public          postgres    false    253            �           0    0    subrubros_idsubrubro_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.subrubros_idsubrubro_seq OWNED BY public.subrubros.idsubrubro;
          public          postgres    false    254                       1259    25153    tamanos_idtamano_seq    SEQUENCE     }   CREATE SEQUENCE public.tamanos_idtamano_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.tamanos_idtamano_seq;
       public          postgres    false                       1259    16865    tamanos    TABLE       CREATE TABLE public.tamanos (
    idtamano integer DEFAULT nextval('public.tamanos_idtamano_seq'::regclass) NOT NULL,
    idproducto integer,
    tamano character varying(15),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.tamanos;
       public         heap    postgres    false    280                       1259    25228 #   tiposbeneficios_idtipobeneficio_seq    SEQUENCE     �   CREATE SEQUENCE public.tiposbeneficios_idtipobeneficio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tiposbeneficios_idtipobeneficio_seq;
       public          postgres    false                       1259    25215    tiposbeneficios    TABLE     '  CREATE TABLE public.tiposbeneficios (
    idtipobeneficio integer DEFAULT nextval('public.tiposbeneficios_idtipobeneficio_seq'::regclass) NOT NULL,
    tipobeneficio character varying(255),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 #   DROP TABLE public.tiposbeneficios;
       public         heap    postgres    false    284            �            1259    16575    tiposdocumentos    TABLE     �   CREATE TABLE public.tiposdocumentos (
    idtipodocumento integer NOT NULL,
    tipodocumento character varying,
    documento character varying(30),
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 #   DROP TABLE public.tiposdocumentos;
       public         heap    postgres    false                        1259    16582 #   tiposdocumentos_idtipodocumento_seq    SEQUENCE     �   CREATE SEQUENCE public.tiposdocumentos_idtipodocumento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 :   DROP SEQUENCE public.tiposdocumentos_idtipodocumento_seq;
       public          postgres    false    255            �           0    0 #   tiposdocumentos_idtipodocumento_seq    SEQUENCE OWNED BY     k   ALTER SEQUENCE public.tiposdocumentos_idtipodocumento_seq OWNED BY public.tiposdocumentos.idtipodocumento;
          public          postgres    false    256                       1259    16583    tiposextensiones    TABLE     �   CREATE TABLE public.tiposextensiones (
    idtipoextension integer NOT NULL,
    tipoextension character varying,
    sigla character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
 $   DROP TABLE public.tiposextensiones;
       public         heap    postgres    false                       1259    16590 $   tiposextensiones_idtipoextension_seq    SEQUENCE     �   CREATE SEQUENCE public.tiposextensiones_idtipoextension_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ;   DROP SEQUENCE public.tiposextensiones_idtipoextension_seq;
       public          postgres    false    257            �           0    0 $   tiposextensiones_idtipoextension_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.tiposextensiones_idtipoextension_seq OWNED BY public.tiposextensiones.idtipoextension;
          public          postgres    false    258                       1259    16591    tiposgeneros    TABLE     �   CREATE TABLE public.tiposgeneros (
    idtipogenero integer NOT NULL,
    tipogenero character varying,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
     DROP TABLE public.tiposgeneros;
       public         heap    postgres    false                       1259    16598    tiposgeneros_idtipogenero_seq    SEQUENCE     �   CREATE SEQUENCE public.tiposgeneros_idtipogenero_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.tiposgeneros_idtipogenero_seq;
       public          postgres    false    259            �           0    0    tiposgeneros_idtipogenero_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.tiposgeneros_idtipogenero_seq OWNED BY public.tiposgeneros.idtipogenero;
          public          postgres    false    260                       1259    16599    usuarios    TABLE     "  CREATE TABLE public.usuarios (
    idusuario integer NOT NULL,
    idpersona integer,
    usuario character varying NOT NULL,
    clave character varying NOT NULL,
    estado boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    idcargo integer
);
    DROP TABLE public.usuarios;
       public         heap    postgres    false                       1259    16606    usuarios_idusuario_seq    SEQUENCE     �   CREATE SEQUENCE public.usuarios_idusuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 -   DROP SEQUENCE public.usuarios_idusuario_seq;
       public          postgres    false    261            �           0    0    usuarios_idusuario_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.usuarios_idusuario_seq OWNED BY public.usuarios.idusuario;
          public          postgres    false    262                       1259    16607    usuariosroles    TABLE     s   CREATE TABLE public.usuariosroles (
    idusuariorol integer NOT NULL,
    idusuario integer,
    idrol integer
);
 !   DROP TABLE public.usuariosroles;
       public         heap    postgres    false                       1259    16610    usuariosroles_idusuariorol_seq    SEQUENCE     �   CREATE SEQUENCE public.usuariosroles_idusuariorol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 92233720
    CACHE 1;
 5   DROP SEQUENCE public.usuariosroles_idusuariorol_seq;
       public          postgres    false    263            �           0    0    usuariosroles_idusuariorol_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public.usuariosroles_idusuariorol_seq OWNED BY public.usuariosroles.idusuariorol;
          public          postgres    false    264            �           2604    16611    asociaciones idasociacion    DEFAULT     �   ALTER TABLE ONLY public.asociaciones ALTER COLUMN idasociacion SET DEFAULT nextval('public.asociaciones_idasociacion_seq'::regclass);
 H   ALTER TABLE public.asociaciones ALTER COLUMN idasociacion DROP DEFAULT;
       public          postgres    false    218    217            �           2604    16612    bitacoras idbitacora    DEFAULT     |   ALTER TABLE ONLY public.bitacoras ALTER COLUMN idbitacora SET DEFAULT nextval('public.bitacoras_idbitacora_seq'::regclass);
 C   ALTER TABLE public.bitacoras ALTER COLUMN idbitacora DROP DEFAULT;
       public          postgres    false    220    219            �           2604    16613    carritos idcarrito    DEFAULT     x   ALTER TABLE ONLY public.carritos ALTER COLUMN idcarrito SET DEFAULT nextval('public.carritos_idcarrito_seq'::regclass);
 A   ALTER TABLE public.carritos ALTER COLUMN idcarrito DROP DEFAULT;
       public          postgres    false    222    221            �           2604    16614    categorias idcategoria    DEFAULT     �   ALTER TABLE ONLY public.categorias ALTER COLUMN idcategoria SET DEFAULT nextval('public.categorias_idcategoria_seq'::regclass);
 E   ALTER TABLE public.categorias ALTER COLUMN idcategoria DROP DEFAULT;
       public          postgres    false    224    223            �           2604    16615    clientes idcliente    DEFAULT     x   ALTER TABLE ONLY public.clientes ALTER COLUMN idcliente SET DEFAULT nextval('public.clientes_idcliente_seq'::regclass);
 A   ALTER TABLE public.clientes ALTER COLUMN idcliente DROP DEFAULT;
       public          postgres    false    226    225            �           2604    16616    empresas idempresa    DEFAULT     x   ALTER TABLE ONLY public.empresas ALTER COLUMN idempresa SET DEFAULT nextval('public.empresas_idempresa_seq'::regclass);
 A   ALTER TABLE public.empresas ALTER COLUMN idempresa DROP DEFAULT;
       public          postgres    false    228    227                        2604    16617    enlaces idenlace    DEFAULT     t   ALTER TABLE ONLY public.enlaces ALTER COLUMN idenlace SET DEFAULT nextval('public.enlaces_idenlace_seq'::regclass);
 ?   ALTER TABLE public.enlaces ALTER COLUMN idenlace DROP DEFAULT;
       public          postgres    false    230    229                       2604    16618    enlacesroles idenlacerol    DEFAULT     �   ALTER TABLE ONLY public.enlacesroles ALTER COLUMN idenlacerol SET DEFAULT nextval('public.enlacesroles_idenlacerol_seq'::regclass);
 G   ALTER TABLE public.enlacesroles ALTER COLUMN idenlacerol DROP DEFAULT;
       public          postgres    false    232    231                       2604    16619    localidades idlocalidad    DEFAULT     �   ALTER TABLE ONLY public.localidades ALTER COLUMN idlocalidad SET DEFAULT nextval('public.localidades_idlocalidad_seq'::regclass);
 F   ALTER TABLE public.localidades ALTER COLUMN idlocalidad DROP DEFAULT;
       public          postgres    false    234    233                       2604    16620    municipios idmunicipio    DEFAULT     �   ALTER TABLE ONLY public.municipios ALTER COLUMN idmunicipio SET DEFAULT nextval('public.municipios_idmunicipio_seq'::regclass);
 E   ALTER TABLE public.municipios ALTER COLUMN idmunicipio DROP DEFAULT;
       public          postgres    false    236    235            
           2604    16621    parametros idparametro    DEFAULT     �   ALTER TABLE ONLY public.parametros ALTER COLUMN idparametro SET DEFAULT nextval('public.parametros_idparametro_seq'::regclass);
 E   ALTER TABLE public.parametros ALTER COLUMN idparametro DROP DEFAULT;
       public          postgres    false    238    237                       2604    16622    personas idpersona    DEFAULT     x   ALTER TABLE ONLY public.personas ALTER COLUMN idpersona SET DEFAULT nextval('public.personas_idpersona_seq'::regclass);
 A   ALTER TABLE public.personas ALTER COLUMN idpersona DROP DEFAULT;
       public          postgres    false    240    239                       2604    16623    productos idproducto    DEFAULT     |   ALTER TABLE ONLY public.productos ALTER COLUMN idproducto SET DEFAULT nextval('public.productos_idproducto_seq'::regclass);
 C   ALTER TABLE public.productos ALTER COLUMN idproducto DROP DEFAULT;
       public          postgres    false    242    241                       2604    16624    representantes idrepresentante    DEFAULT     �   ALTER TABLE ONLY public.representantes ALTER COLUMN idrepresentante SET DEFAULT nextval('public.representantes_idrepresentante_seq'::regclass);
 M   ALTER TABLE public.representantes ALTER COLUMN idrepresentante DROP DEFAULT;
       public          postgres    false    244    243                       2604    16625    roles idrol    DEFAULT     j   ALTER TABLE ONLY public.roles ALTER COLUMN idrol SET DEFAULT nextval('public.roles_idrol_seq'::regclass);
 :   ALTER TABLE public.roles ALTER COLUMN idrol DROP DEFAULT;
       public          postgres    false    246    245                       2604    16626    rubros idrubro    DEFAULT     p   ALTER TABLE ONLY public.rubros ALTER COLUMN idrubro SET DEFAULT nextval('public.rubros_idrubro_seq'::regclass);
 =   ALTER TABLE public.rubros ALTER COLUMN idrubro DROP DEFAULT;
       public          postgres    false    248    247                       2604    16627    solicitudes idsolicitud    DEFAULT     �   ALTER TABLE ONLY public.solicitudes ALTER COLUMN idsolicitud SET DEFAULT nextval('public.solicitudes_idsolicitud_seq'::regclass);
 F   ALTER TABLE public.solicitudes ALTER COLUMN idsolicitud DROP DEFAULT;
       public          postgres    false    250    249                       2604    16628 (   solicitudesproductos idsolicitudproducto    DEFAULT     �   ALTER TABLE ONLY public.solicitudesproductos ALTER COLUMN idsolicitudproducto SET DEFAULT nextval('public.solicitudesproductos_idsolicitudproducto_seq'::regclass);
 W   ALTER TABLE public.solicitudesproductos ALTER COLUMN idsolicitudproducto DROP DEFAULT;
       public          postgres    false    252    251                       2604    16629    subrubros idsubrubro    DEFAULT     |   ALTER TABLE ONLY public.subrubros ALTER COLUMN idsubrubro SET DEFAULT nextval('public.subrubros_idsubrubro_seq'::regclass);
 C   ALTER TABLE public.subrubros ALTER COLUMN idsubrubro DROP DEFAULT;
       public          postgres    false    254    253                        2604    16630    tiposdocumentos idtipodocumento    DEFAULT     �   ALTER TABLE ONLY public.tiposdocumentos ALTER COLUMN idtipodocumento SET DEFAULT nextval('public.tiposdocumentos_idtipodocumento_seq'::regclass);
 N   ALTER TABLE public.tiposdocumentos ALTER COLUMN idtipodocumento DROP DEFAULT;
       public          postgres    false    256    255            #           2604    16631     tiposextensiones idtipoextension    DEFAULT     �   ALTER TABLE ONLY public.tiposextensiones ALTER COLUMN idtipoextension SET DEFAULT nextval('public.tiposextensiones_idtipoextension_seq'::regclass);
 O   ALTER TABLE public.tiposextensiones ALTER COLUMN idtipoextension DROP DEFAULT;
       public          postgres    false    258    257            &           2604    16632    tiposgeneros idtipogenero    DEFAULT     �   ALTER TABLE ONLY public.tiposgeneros ALTER COLUMN idtipogenero SET DEFAULT nextval('public.tiposgeneros_idtipogenero_seq'::regclass);
 H   ALTER TABLE public.tiposgeneros ALTER COLUMN idtipogenero DROP DEFAULT;
       public          postgres    false    260    259            )           2604    16633    usuarios idusuario    DEFAULT     x   ALTER TABLE ONLY public.usuarios ALTER COLUMN idusuario SET DEFAULT nextval('public.usuarios_idusuario_seq'::regclass);
 A   ALTER TABLE public.usuarios ALTER COLUMN idusuario DROP DEFAULT;
       public          postgres    false    262    261            ,           2604    16634    usuariosroles idusuariorol    DEFAULT     �   ALTER TABLE ONLY public.usuariosroles ALTER COLUMN idusuariorol SET DEFAULT nextval('public.usuariosroles_idusuariorol_seq'::regclass);
 I   ALTER TABLE public.usuariosroles ALTER COLUMN idusuariorol DROP DEFAULT;
       public          postgres    false    264    263            U          0    16438    aaaa 
   TABLE DATA           �   COPY public.aaaa (no, nombre, direccion, municipio_, razonsocial, subrubro, actividad, telefono, id_rubro, id_municipio, carnet) FROM stdin;
    public          postgres    false    216   �~      �          0    16932    archivos 
   TABLE DATA           �   COPY public.archivos (idarchivo, idusuario, idpersona, idempresa, idproducto, filename, data, mimetype, estado, created_at) FROM stdin;
    public          postgres    false    274   '�      V          0    16443    asociaciones 
   TABLE DATA           �   COPY public.asociaciones (idasociacion, asociacion, descripcion, fechacreacion, representantelegal, direccion, telefono, celular, correo, estado, created_at) FROM stdin;
    public          postgres    false    217   D�      �          0    16889 	   atributos 
   TABLE DATA           b   COPY public.atributos (idatributo, idproducto, atributo, detalle, estado, created_at) FROM stdin;
    public          postgres    false    271   �      �          0    16814 
   beneficios 
   TABLE DATA           �   COPY public.beneficios (idbeneficio, beneficio, descripcion, idtipobeneficio, idmunicipio, direccion, fechainicio, fechafin, idcapacitador, capacidad, idusuario, estado, created_at) FROM stdin;
    public          postgres    false    265   e�      �          0    16821    beneficiosempresas 
   TABLE DATA           l   COPY public.beneficiosempresas (idbeneficioempresa, idbeneficio, idempresa, estado, created_at) FROM stdin;
    public          postgres    false    266   5�      X          0    16451 	   bitacoras 
   TABLE DATA           a   COPY public.bitacoras (idbitacora, idusuario, bitacora, fechabitacora, horabitacora) FROM stdin;
    public          postgres    false    219   R�      �          0    16948    cargos 
   TABLE DATA           K   COPY public.cargos (idcargo, idrol, cargo, estado, created_at) FROM stdin;
    public          postgres    false    275   ��      Z          0    16459    carritos 
   TABLE DATA           �   COPY public.carritos (idcarrito, idcliente, idproducto, imagen, idprecio, idcolor, idmaterial, idtamano, cantidad, created_at) FROM stdin;
    public          postgres    false    221   ��      \          0    16464 
   categorias 
   TABLE DATA           m   COPY public.categorias (idcategoria, categoria, ruta, iconocategoria, orden, estado, created_at) FROM stdin;
    public          postgres    false    223   ��      ^          0    16472    clientes 
   TABLE DATA           8   COPY public.clientes (idcliente, idpersona) FROM stdin;
    public          postgres    false    225   z�      �          0    16877    colores 
   TABLE DATA           Y   COPY public.colores (idcolor, idproducto, color, codigo, estado, created_at) FROM stdin;
    public          postgres    false    270   ��      �          0    16836    comentarios 
   TABLE DATA           v   COPY public.comentarios (idcomentario, comentario, idcliente, valoracion, idproducto, estado, created_at) FROM stdin;
    public          postgres    false    267   �      `          0    16476    empresas 
   TABLE DATA             COPY public.empresas (idempresa, idsubrubro, idlocalidad, idrepresentante, idasociacion, empresa, tipo, direccion, telefono, celular, correo, facebook, twitter, instagram, paginaweb, nform, registrosenasag, latitud, longitud, descripcion, nit, bancamovil, fechaapertura, servicios, capacidad, unidadmedida, motivo, otromotivo, familiar, involucrados, otrosinvolucrados, trabajadores, participacion, capacitacion, zona, referencia, transporte, idusuario, fechareg, razonsocial, estado, created_at, idmunicipio, idrubro) FROM stdin;
    public          postgres    false    227   1�      b          0    16484    enlaces 
   TABLE DATA           n   COPY public.enlaces (idenlace, idcategoria, enlace, ruta, iconoenlace, orden, estado, created_at) FROM stdin;
    public          postgres    false    229   d:      d          0    16492    enlacesroles 
   TABLE DATA           D   COPY public.enlacesroles (idenlacerol, idenlace, idrol) FROM stdin;
    public          postgres    false    231   �<      f          0    16496    localidades 
   TABLE DATA           ^   COPY public.localidades (idlocalidad, idmunicipio, localidad, estado, created_at) FROM stdin;
    public          postgres    false    233   #>      �          0    16853 
   materiales 
   TABLE DATA           Z   COPY public.materiales (idmaterial, idproducto, material, estado, created_at) FROM stdin;
    public          postgres    false    268   �A      h          0    16504 
   municipios 
   TABLE DATA           P   COPY public.municipios (idmunicipio, municipio, estado, created_at) FROM stdin;
    public          postgres    false    235   \B      �          0    16920    ofertas 
   TABLE DATA           ]   COPY public.ofertas (idoferta, idproducto, oferta, duracion, estado, created_at) FROM stdin;
    public          postgres    false    273   �D      j          0    16512 
   parametros 
   TABLE DATA           N   COPY public.parametros (idparametro, idusuario, parametro, valor) FROM stdin;
    public          postgres    false    237   �D      l          0    16518    personas 
   TABLE DATA             COPY public.personas (idpersona, idtipogenero, primerapellido, segundoapellido, primernombre, segundonombre, fechanacimiento, dip, complementario, idtipodocumento, idtipoextension, direccion, telefono, celular, correo, formacion, estadocivil, hijos, estado, created_at) FROM stdin;
    public          postgres    false    239   �D      �          0    16903    precios 
   TABLE DATA           g   COPY public.precios (idprecio, idproducto, precio, idtamano, cantidad, estado, created_at) FROM stdin;
    public          postgres    false    272   ��      n          0    16526 	   productos 
   TABLE DATA           �   COPY public.productos (idproducto, idempresa, producto, descripcion, preciocompra, precioventa, cantidad, estado, created_at) FROM stdin;
    public          postgres    false    241   ��      p          0    16534    representantes 
   TABLE DATA           S   COPY public.representantes (idrepresentante, idpersona, representante) FROM stdin;
    public          postgres    false    243   ;�      r          0    16540    roles 
   TABLE DATA           B   COPY public.roles (idrol, rol, nombrerol, created_at) FROM stdin;
    public          postgres    false    245   �      t          0    16547    rubros 
   TABLE DATA           D   COPY public.rubros (idrubro, rubro, estado, created_at) FROM stdin;
    public          postgres    false    247   �      v          0    16555    solicitudes 
   TABLE DATA           g   COPY public.solicitudes (idsolicitud, idcliente, solicitud, idempresa, estado, created_at) FROM stdin;
    public          postgres    false    249   ;      x          0    16563    solicitudesproductos 
   TABLE DATA           �   COPY public.solicitudesproductos (idsolicitudproducto, idsolicitud, idproducto, imagen, idprecio, idcolor, idmaterial, idtamano, cantidad, created_at, estado) FROM stdin;
    public          postgres    false    251   �      z          0    16567 	   subrubros 
   TABLE DATA           V   COPY public.subrubros (idsubrubro, idrubro, subrubro, estado, created_at) FROM stdin;
    public          postgres    false    253   �      �          0    16865    tamanos 
   TABLE DATA           S   COPY public.tamanos (idtamano, idproducto, tamano, estado, created_at) FROM stdin;
    public          postgres    false    269   �      �          0    25215    tiposbeneficios 
   TABLE DATA           ]   COPY public.tiposbeneficios (idtipobeneficio, tipobeneficio, estado, created_at) FROM stdin;
    public          postgres    false    283   �      |          0    16575    tiposdocumentos 
   TABLE DATA           h   COPY public.tiposdocumentos (idtipodocumento, tipodocumento, documento, estado, created_at) FROM stdin;
    public          postgres    false    255   w      ~          0    16583    tiposextensiones 
   TABLE DATA           e   COPY public.tiposextensiones (idtipoextension, tipoextension, sigla, estado, created_at) FROM stdin;
    public          postgres    false    257   �      �          0    16591    tiposgeneros 
   TABLE DATA           T   COPY public.tiposgeneros (idtipogenero, tipogenero, estado, created_at) FROM stdin;
    public          postgres    false    259   �      �          0    16599    usuarios 
   TABLE DATA           e   COPY public.usuarios (idusuario, idpersona, usuario, clave, estado, created_at, idcargo) FROM stdin;
    public          postgres    false    261   �      �          0    16607    usuariosroles 
   TABLE DATA           G   COPY public.usuariosroles (idusuariorol, idusuario, idrol) FROM stdin;
    public          postgres    false    263   ��      �           0    0    archivos_idarchivo_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.archivos_idarchivo_seq', 1, false);
          public          postgres    false    282            �           0    0    asociaciones_idasociacion_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.asociaciones_idasociacion_seq', 2, true);
          public          postgres    false    218            �           0    0    atributos_idatributo_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.atributos_idatributo_seq', 14, true);
          public          postgres    false    281            �           0    0    beneficios_idbeneficio_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.beneficios_idbeneficio_seq', 2, true);
          public          postgres    false    285            �           0    0    bitacoras_idbitacora_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.bitacoras_idbitacora_seq', 1, true);
          public          postgres    false    220            �           0    0    cargos_idcargo_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.cargos_idcargo_seq', 14, true);
          public          postgres    false    276            �           0    0    carritos_idcarrito_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.carritos_idcarrito_seq', 454, true);
          public          postgres    false    222            �           0    0    categorias_idcategoria_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.categorias_idcategoria_seq', 7, true);
          public          postgres    false    224            �           0    0    clientes_idcliente_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.clientes_idcliente_seq', 14, true);
          public          postgres    false    226            �           0    0    colores_idcolor_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.colores_idcolor_seq', 29, true);
          public          postgres    false    278            �           0    0    empresas_idempresa_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.empresas_idempresa_seq', 547, true);
          public          postgres    false    228            �           0    0    enlaces_idenlace_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.enlaces_idenlace_seq', 27, true);
          public          postgres    false    230            �           0    0    enlacesroles_idenlacerol_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.enlacesroles_idenlacerol_seq', 97, true);
          public          postgres    false    232            �           0    0    localidades_idlocalidad_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.localidades_idlocalidad_seq', 535, true);
          public          postgres    false    234            �           0    0    materiales_idmaterial_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.materiales_idmaterial_seq', 8, true);
          public          postgres    false    279            �           0    0    municipios_idmunicipio_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.municipios_idmunicipio_seq', 516, true);
          public          postgres    false    236            �           0    0    parametros_idparametro_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.parametros_idparametro_seq', 1, false);
          public          postgres    false    238            �           0    0    personas_idpersona_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.personas_idpersona_seq', 1229, true);
          public          postgres    false    240            �           0    0    precios_idprecio_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.precios_idprecio_seq', 25, true);
          public          postgres    false    277            �           0    0    productos_idproducto_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.productos_idproducto_seq', 236, true);
          public          postgres    false    242            �           0    0 "   representantes_idrepresentante_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.representantes_idrepresentante_seq', 1008, true);
          public          postgres    false    244            �           0    0    roles_idrol_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.roles_idrol_seq', 8, true);
          public          postgres    false    246            �           0    0    rubros_idrubro_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.rubros_idrubro_seq', 500, false);
          public          postgres    false    248            �           0    0    solicitudes_idsolicitud_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.solicitudes_idsolicitud_seq', 111, true);
          public          postgres    false    250            �           0    0 ,   solicitudesproductos_idsolicitudproducto_seq    SEQUENCE SET     \   SELECT pg_catalog.setval('public.solicitudesproductos_idsolicitudproducto_seq', 175, true);
          public          postgres    false    252            �           0    0    subrubros_idsubrubro_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.subrubros_idsubrubro_seq', 503, true);
          public          postgres    false    254            �           0    0    tamanos_idtamano_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.tamanos_idtamano_seq', 18, true);
          public          postgres    false    280            �           0    0 #   tiposbeneficios_idtipobeneficio_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tiposbeneficios_idtipobeneficio_seq', 5, true);
          public          postgres    false    284            �           0    0 #   tiposdocumentos_idtipodocumento_seq    SEQUENCE SET     Q   SELECT pg_catalog.setval('public.tiposdocumentos_idtipodocumento_seq', 3, true);
          public          postgres    false    256            �           0    0 $   tiposextensiones_idtipoextension_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.tiposextensiones_idtipoextension_seq', 10, true);
          public          postgres    false    258            �           0    0    tiposgeneros_idtipogenero_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('public.tiposgeneros_idtipogenero_seq', 2, true);
          public          postgres    false    260            �           0    0    usuarios_idusuario_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.usuarios_idusuario_seq', 1230, true);
          public          postgres    false    262            �           0    0    usuariosroles_idusuariorol_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.usuariosroles_idusuariorol_seq', 1038, true);
          public          postgres    false    264            �           2606    16938    archivos archivos_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.archivos
    ADD CONSTRAINT archivos_pkey PRIMARY KEY (idarchivo);
 @   ALTER TABLE ONLY public.archivos DROP CONSTRAINT archivos_pkey;
       public            postgres    false    274            O           2606    16636    asociaciones asociaciones_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.asociaciones
    ADD CONSTRAINT asociaciones_pkey PRIMARY KEY (idasociacion);
 H   ALTER TABLE ONLY public.asociaciones DROP CONSTRAINT asociaciones_pkey;
       public            postgres    false    217            �           2606    16895    atributos atributos_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.atributos
    ADD CONSTRAINT atributos_pkey PRIMARY KEY (idatributo);
 B   ALTER TABLE ONLY public.atributos DROP CONSTRAINT atributos_pkey;
       public            postgres    false    271            �           2606    16820    beneficios beneficios_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_pkey PRIMARY KEY (idbeneficio);
 D   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_pkey;
       public            postgres    false    265            �           2606    16825 *   beneficiosempresas beneficiosempresas_pkey 
   CONSTRAINT     x   ALTER TABLE ONLY public.beneficiosempresas
    ADD CONSTRAINT beneficiosempresas_pkey PRIMARY KEY (idbeneficioempresa);
 T   ALTER TABLE ONLY public.beneficiosempresas DROP CONSTRAINT beneficiosempresas_pkey;
       public            postgres    false    266            Q           2606    16638    bitacoras bitacoras_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.bitacoras
    ADD CONSTRAINT bitacoras_pkey PRIMARY KEY (idbitacora);
 B   ALTER TABLE ONLY public.bitacoras DROP CONSTRAINT bitacoras_pkey;
       public            postgres    false    219            �           2606    16954    cargos cargos_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.cargos
    ADD CONSTRAINT cargos_pkey PRIMARY KEY (idcargo);
 <   ALTER TABLE ONLY public.cargos DROP CONSTRAINT cargos_pkey;
       public            postgres    false    275            S           2606    16640    carritos carritos_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.carritos
    ADD CONSTRAINT carritos_pkey PRIMARY KEY (idcarrito);
 @   ALTER TABLE ONLY public.carritos DROP CONSTRAINT carritos_pkey;
       public            postgres    false    221            U           2606    16642    categorias categorias_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (idcategoria);
 D   ALTER TABLE ONLY public.categorias DROP CONSTRAINT categorias_pkey;
       public            postgres    false    223            W           2606    16644    clientes clientes_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (idcliente);
 @   ALTER TABLE ONLY public.clientes DROP CONSTRAINT clientes_pkey;
       public            postgres    false    225            �           2606    16883    colores colores_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.colores
    ADD CONSTRAINT colores_pkey PRIMARY KEY (idcolor);
 >   ALTER TABLE ONLY public.colores DROP CONSTRAINT colores_pkey;
       public            postgres    false    270            �           2606    16842    comentarios comentarios_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.comentarios
    ADD CONSTRAINT comentarios_pkey PRIMARY KEY (idcomentario);
 F   ALTER TABLE ONLY public.comentarios DROP CONSTRAINT comentarios_pkey;
       public            postgres    false    267            Y           2606    16646    empresas empresas_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_pkey PRIMARY KEY (idempresa);
 @   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_pkey;
       public            postgres    false    227            [           2606    16648    enlaces enlaces_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.enlaces
    ADD CONSTRAINT enlaces_pkey PRIMARY KEY (idenlace);
 >   ALTER TABLE ONLY public.enlaces DROP CONSTRAINT enlaces_pkey;
       public            postgres    false    229            ]           2606    16650    enlacesroles enlacesroles_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public.enlacesroles
    ADD CONSTRAINT enlacesroles_pkey PRIMARY KEY (idenlacerol);
 H   ALTER TABLE ONLY public.enlacesroles DROP CONSTRAINT enlacesroles_pkey;
       public            postgres    false    231            _           2606    16652    localidades localidades_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.localidades
    ADD CONSTRAINT localidades_pkey PRIMARY KEY (idlocalidad);
 F   ALTER TABLE ONLY public.localidades DROP CONSTRAINT localidades_pkey;
       public            postgres    false    233            �           2606    16859    materiales materiales_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_pkey PRIMARY KEY (idmaterial);
 D   ALTER TABLE ONLY public.materiales DROP CONSTRAINT materiales_pkey;
       public            postgres    false    268            a           2606    16654    municipios municipios_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.municipios
    ADD CONSTRAINT municipios_pkey PRIMARY KEY (idmunicipio);
 D   ALTER TABLE ONLY public.municipios DROP CONSTRAINT municipios_pkey;
       public            postgres    false    235            �           2606    16926    ofertas ofertas_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.ofertas
    ADD CONSTRAINT ofertas_pkey PRIMARY KEY (idoferta);
 >   ALTER TABLE ONLY public.ofertas DROP CONSTRAINT ofertas_pkey;
       public            postgres    false    273            c           2606    16656    parametros parametros_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public.parametros
    ADD CONSTRAINT parametros_pkey PRIMARY KEY (idparametro);
 D   ALTER TABLE ONLY public.parametros DROP CONSTRAINT parametros_pkey;
       public            postgres    false    237            e           2606    16658    personas personas_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_pkey PRIMARY KEY (idpersona);
 @   ALTER TABLE ONLY public.personas DROP CONSTRAINT personas_pkey;
       public            postgres    false    239            �           2606    16909    precios precios_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.precios
    ADD CONSTRAINT precios_pkey PRIMARY KEY (idprecio);
 >   ALTER TABLE ONLY public.precios DROP CONSTRAINT precios_pkey;
       public            postgres    false    272            g           2606    16660    productos productos_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (idproducto);
 B   ALTER TABLE ONLY public.productos DROP CONSTRAINT productos_pkey;
       public            postgres    false    241            i           2606    16662 "   representantes representantes_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.representantes
    ADD CONSTRAINT representantes_pkey PRIMARY KEY (idrepresentante);
 L   ALTER TABLE ONLY public.representantes DROP CONSTRAINT representantes_pkey;
       public            postgres    false    243            k           2606    16664    roles roles_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (idrol);
 :   ALTER TABLE ONLY public.roles DROP CONSTRAINT roles_pkey;
       public            postgres    false    245            m           2606    16666    rubros rubros_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY public.rubros
    ADD CONSTRAINT rubros_pkey PRIMARY KEY (idrubro);
 <   ALTER TABLE ONLY public.rubros DROP CONSTRAINT rubros_pkey;
       public            postgres    false    247            o           2606    16668    solicitudes solicitudes_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.solicitudes
    ADD CONSTRAINT solicitudes_pkey PRIMARY KEY (idsolicitud);
 F   ALTER TABLE ONLY public.solicitudes DROP CONSTRAINT solicitudes_pkey;
       public            postgres    false    249            q           2606    16670 .   solicitudesproductos solicitudesproductos_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.solicitudesproductos
    ADD CONSTRAINT solicitudesproductos_pkey PRIMARY KEY (idsolicitudproducto);
 X   ALTER TABLE ONLY public.solicitudesproductos DROP CONSTRAINT solicitudesproductos_pkey;
       public            postgres    false    251            s           2606    16672    subrubros subrubros_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.subrubros
    ADD CONSTRAINT subrubros_pkey PRIMARY KEY (idsubrubro);
 B   ALTER TABLE ONLY public.subrubros DROP CONSTRAINT subrubros_pkey;
       public            postgres    false    253            �           2606    16871    tamanos tamanos_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.tamanos
    ADD CONSTRAINT tamanos_pkey PRIMARY KEY (idtamano);
 >   ALTER TABLE ONLY public.tamanos DROP CONSTRAINT tamanos_pkey;
       public            postgres    false    269            �           2606    25219 $   tiposbeneficios tiposbeneficios_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.tiposbeneficios
    ADD CONSTRAINT tiposbeneficios_pkey PRIMARY KEY (idtipobeneficio);
 N   ALTER TABLE ONLY public.tiposbeneficios DROP CONSTRAINT tiposbeneficios_pkey;
       public            postgres    false    283            u           2606    16674 $   tiposdocumentos tiposdocumentos_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.tiposdocumentos
    ADD CONSTRAINT tiposdocumentos_pkey PRIMARY KEY (idtipodocumento);
 N   ALTER TABLE ONLY public.tiposdocumentos DROP CONSTRAINT tiposdocumentos_pkey;
       public            postgres    false    255            w           2606    16676 &   tiposextensiones tiposextensiones_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.tiposextensiones
    ADD CONSTRAINT tiposextensiones_pkey PRIMARY KEY (idtipoextension);
 P   ALTER TABLE ONLY public.tiposextensiones DROP CONSTRAINT tiposextensiones_pkey;
       public            postgres    false    257            y           2606    16678    tiposgeneros tiposgeneros_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.tiposgeneros
    ADD CONSTRAINT tiposgeneros_pkey PRIMARY KEY (idtipogenero);
 H   ALTER TABLE ONLY public.tiposgeneros DROP CONSTRAINT tiposgeneros_pkey;
       public            postgres    false    259            {           2606    16680    usuarios usuarios_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (idusuario);
 @   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_pkey;
       public            postgres    false    261            }           2606    16682    usuarios usuarios_usuario_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_usuario_key UNIQUE (usuario);
 G   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_usuario_key;
       public            postgres    false    261                       2606    16684     usuariosroles usuariosroles_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.usuariosroles
    ADD CONSTRAINT usuariosroles_pkey PRIMARY KEY (idusuariorol);
 J   ALTER TABLE ONLY public.usuariosroles DROP CONSTRAINT usuariosroles_pkey;
       public            postgres    false    263            �           2606    25171     archivos archivos_idempresa_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.archivos
    ADD CONSTRAINT archivos_idempresa_fkey FOREIGN KEY (idempresa) REFERENCES public.empresas(idempresa) NOT VALID;
 J   ALTER TABLE ONLY public.archivos DROP CONSTRAINT archivos_idempresa_fkey;
       public          postgres    false    227    274    4953            �           2606    25166     archivos archivos_idpersona_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.archivos
    ADD CONSTRAINT archivos_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES public.personas(idpersona) NOT VALID;
 J   ALTER TABLE ONLY public.archivos DROP CONSTRAINT archivos_idpersona_fkey;
       public          postgres    false    4965    274    239            �           2606    25176 !   archivos archivos_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.archivos
    ADD CONSTRAINT archivos_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto) NOT VALID;
 K   ALTER TABLE ONLY public.archivos DROP CONSTRAINT archivos_idproducto_fkey;
       public          postgres    false    4967    241    274            �           2606    25161     archivos archivos_idusuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.archivos
    ADD CONSTRAINT archivos_idusuario_fkey FOREIGN KEY (idusuario) REFERENCES public.usuarios(idusuario) NOT VALID;
 J   ALTER TABLE ONLY public.archivos DROP CONSTRAINT archivos_idusuario_fkey;
       public          postgres    false    261    4987    274            �           2606    16896 #   atributos atributos_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.atributos
    ADD CONSTRAINT atributos_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 M   ALTER TABLE ONLY public.atributos DROP CONSTRAINT atributos_idproducto_fkey;
       public          postgres    false    241    4967    271            �           2606    25244 (   beneficios beneficios_idcapacitador_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_idcapacitador_fkey FOREIGN KEY (idcapacitador) REFERENCES public.usuarios(idusuario) NOT VALID;
 R   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_idcapacitador_fkey;
       public          postgres    false    4987    265    261            �           2606    25237 &   beneficios beneficios_idmunicipio_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_idmunicipio_fkey FOREIGN KEY (idmunicipio) REFERENCES public.municipios(idmunicipio) NOT VALID;
 P   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_idmunicipio_fkey;
       public          postgres    false    235    265    4961            �           2606    25232 *   beneficios beneficios_idtipobeneficio_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_idtipobeneficio_fkey FOREIGN KEY (idtipobeneficio) REFERENCES public.tiposbeneficios(idtipobeneficio) NOT VALID;
 T   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_idtipobeneficio_fkey;
       public          postgres    false    5015    283    265            �           2606    25249 $   beneficios beneficios_idusuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficios
    ADD CONSTRAINT beneficios_idusuario_fkey FOREIGN KEY (idusuario) REFERENCES public.usuarios(idusuario) NOT VALID;
 N   ALTER TABLE ONLY public.beneficios DROP CONSTRAINT beneficios_idusuario_fkey;
       public          postgres    false    265    4987    261            �           2606    16826 6   beneficiosempresas beneficiosempresas_idbeneficio_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficiosempresas
    ADD CONSTRAINT beneficiosempresas_idbeneficio_fkey FOREIGN KEY (idbeneficio) REFERENCES public.beneficios(idbeneficio);
 `   ALTER TABLE ONLY public.beneficiosempresas DROP CONSTRAINT beneficiosempresas_idbeneficio_fkey;
       public          postgres    false    266    265    4993            �           2606    16831 5   beneficiosempresas beneficiosempresas_idempresas_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.beneficiosempresas
    ADD CONSTRAINT beneficiosempresas_idempresas_fkey FOREIGN KEY (idempresa) REFERENCES public.empresas(idempresa);
 _   ALTER TABLE ONLY public.beneficiosempresas DROP CONSTRAINT beneficiosempresas_idempresas_fkey;
       public          postgres    false    266    4953    227            �           2606    16685 "   bitacoras bitacoras_idusuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.bitacoras
    ADD CONSTRAINT bitacoras_idusuario_fkey FOREIGN KEY (idusuario) REFERENCES public.usuarios(idusuario);
 L   ALTER TABLE ONLY public.bitacoras DROP CONSTRAINT bitacoras_idusuario_fkey;
       public          postgres    false    261    4987    219            �           2606    16955    cargos cargos_idrol_fkey    FK CONSTRAINT     x   ALTER TABLE ONLY public.cargos
    ADD CONSTRAINT cargos_idrol_fkey FOREIGN KEY (idrol) REFERENCES public.roles(idrol);
 B   ALTER TABLE ONLY public.cargos DROP CONSTRAINT cargos_idrol_fkey;
       public          postgres    false    275    4971    245            �           2606    16690     clientes clientes_idpersona_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES public.personas(idpersona);
 J   ALTER TABLE ONLY public.clientes DROP CONSTRAINT clientes_idpersona_fkey;
       public          postgres    false    225    4965    239            �           2606    16884    colores colores_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.colores
    ADD CONSTRAINT colores_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 I   ALTER TABLE ONLY public.colores DROP CONSTRAINT colores_idproducto_fkey;
       public          postgres    false    4967    241    270            �           2606    16843 &   comentarios comentarios_idcliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.comentarios
    ADD CONSTRAINT comentarios_idcliente_fkey FOREIGN KEY (idcliente) REFERENCES public.clientes(idcliente);
 P   ALTER TABLE ONLY public.comentarios DROP CONSTRAINT comentarios_idcliente_fkey;
       public          postgres    false    225    4951    267            �           2606    16848 '   comentarios comentarios_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.comentarios
    ADD CONSTRAINT comentarios_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 Q   ALTER TABLE ONLY public.comentarios DROP CONSTRAINT comentarios_idproducto_fkey;
       public          postgres    false    267    4967    241            �           2606    16695 #   empresas empresas_idasociacion_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idasociacion_fkey FOREIGN KEY (idasociacion) REFERENCES public.asociaciones(idasociacion);
 M   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idasociacion_fkey;
       public          postgres    false    227    4943    217            �           2606    16700 "   empresas empresas_idlocalidad_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idlocalidad_fkey FOREIGN KEY (idlocalidad) REFERENCES public.localidades(idlocalidad);
 L   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idlocalidad_fkey;
       public          postgres    false    4959    227    233            �           2606    25189 "   empresas empresas_idmunicipio_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idmunicipio_fkey FOREIGN KEY (idmunicipio) REFERENCES public.municipios(idmunicipio) NOT VALID;
 L   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idmunicipio_fkey;
       public          postgres    false    4961    227    235            �           2606    16705 &   empresas empresas_idrepresentante_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idrepresentante_fkey FOREIGN KEY (idrepresentante) REFERENCES public.representantes(idrepresentante);
 P   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idrepresentante_fkey;
       public          postgres    false    4969    227    243            �           2606    25194    empresas empresas_idrubro_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idrubro_fkey FOREIGN KEY (idrubro) REFERENCES public.rubros(idrubro) NOT VALID;
 H   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idrubro_fkey;
       public          postgres    false    227    4973    247            �           2606    16710 !   empresas empresas_idsubrubro_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_idsubrubro_fkey FOREIGN KEY (idsubrubro) REFERENCES public.subrubros(idsubrubro);
 K   ALTER TABLE ONLY public.empresas DROP CONSTRAINT empresas_idsubrubro_fkey;
       public          postgres    false    253    4979    227            �           2606    16715     enlaces enlaces_idcategoria_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.enlaces
    ADD CONSTRAINT enlaces_idcategoria_fkey FOREIGN KEY (idcategoria) REFERENCES public.categorias(idcategoria);
 J   ALTER TABLE ONLY public.enlaces DROP CONSTRAINT enlaces_idcategoria_fkey;
       public          postgres    false    4949    229    223            �           2606    16720 '   enlacesroles enlacesroles_idenlace_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.enlacesroles
    ADD CONSTRAINT enlacesroles_idenlace_fkey FOREIGN KEY (idenlace) REFERENCES public.enlaces(idenlace);
 Q   ALTER TABLE ONLY public.enlacesroles DROP CONSTRAINT enlacesroles_idenlace_fkey;
       public          postgres    false    4955    231    229            �           2606    16725 $   enlacesroles enlacesroles_idrol_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.enlacesroles
    ADD CONSTRAINT enlacesroles_idrol_fkey FOREIGN KEY (idrol) REFERENCES public.roles(idrol);
 N   ALTER TABLE ONLY public.enlacesroles DROP CONSTRAINT enlacesroles_idrol_fkey;
       public          postgres    false    4971    231    245            �           2606    16730 (   localidades localidades_idmunicipio_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.localidades
    ADD CONSTRAINT localidades_idmunicipio_fkey FOREIGN KEY (idmunicipio) REFERENCES public.municipios(idmunicipio);
 R   ALTER TABLE ONLY public.localidades DROP CONSTRAINT localidades_idmunicipio_fkey;
       public          postgres    false    4961    233    235            �           2606    16860 %   materiales materiales_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 O   ALTER TABLE ONLY public.materiales DROP CONSTRAINT materiales_idproducto_fkey;
       public          postgres    false    241    268    4967            �           2606    16927    ofertas ofertas_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.ofertas
    ADD CONSTRAINT ofertas_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 I   ALTER TABLE ONLY public.ofertas DROP CONSTRAINT ofertas_idproducto_fkey;
       public          postgres    false    273    241    4967            �           2606    16735 $   parametros parametros_idusuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.parametros
    ADD CONSTRAINT parametros_idusuario_fkey FOREIGN KEY (idusuario) REFERENCES public.usuarios(idusuario);
 N   ALTER TABLE ONLY public.parametros DROP CONSTRAINT parametros_idusuario_fkey;
       public          postgres    false    261    4987    237            �           2606    16740 &   personas personas_idtipodocumento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_idtipodocumento_fkey FOREIGN KEY (idtipodocumento) REFERENCES public.tiposdocumentos(idtipodocumento);
 P   ALTER TABLE ONLY public.personas DROP CONSTRAINT personas_idtipodocumento_fkey;
       public          postgres    false    255    239    4981            �           2606    16745 &   personas personas_idtipoextension_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_idtipoextension_fkey FOREIGN KEY (idtipoextension) REFERENCES public.tiposextensiones(idtipoextension);
 P   ALTER TABLE ONLY public.personas DROP CONSTRAINT personas_idtipoextension_fkey;
       public          postgres    false    257    239    4983            �           2606    16750 #   personas personas_idtipogenero_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_idtipogenero_fkey FOREIGN KEY (idtipogenero) REFERENCES public.tiposgeneros(idtipogenero);
 M   ALTER TABLE ONLY public.personas DROP CONSTRAINT personas_idtipogenero_fkey;
       public          postgres    false    4985    239    259            �           2606    16910    precios precios_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.precios
    ADD CONSTRAINT precios_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 I   ALTER TABLE ONLY public.precios DROP CONSTRAINT precios_idproducto_fkey;
       public          postgres    false    241    4967    272            �           2606    16915    precios precios_idtamno_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.precios
    ADD CONSTRAINT precios_idtamno_fkey FOREIGN KEY (idtamano) REFERENCES public.tamanos(idtamano);
 F   ALTER TABLE ONLY public.precios DROP CONSTRAINT precios_idtamno_fkey;
       public          postgres    false    269    272    5001            �           2606    16755 "   productos productos_idempresa_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_idempresa_fkey FOREIGN KEY (idempresa) REFERENCES public.empresas(idempresa);
 L   ALTER TABLE ONLY public.productos DROP CONSTRAINT productos_idempresa_fkey;
       public          postgres    false    227    241    4953            �           2606    16760 ,   representantes representantes_idpersona_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.representantes
    ADD CONSTRAINT representantes_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES public.personas(idpersona);
 V   ALTER TABLE ONLY public.representantes DROP CONSTRAINT representantes_idpersona_fkey;
       public          postgres    false    239    243    4965            �           2606    16765 &   solicitudes solicitudes_idcliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.solicitudes
    ADD CONSTRAINT solicitudes_idcliente_fkey FOREIGN KEY (idcliente) REFERENCES public.clientes(idcliente);
 P   ALTER TABLE ONLY public.solicitudes DROP CONSTRAINT solicitudes_idcliente_fkey;
       public          postgres    false    225    249    4951            �           2606    16770 9   solicitudesproductos solicitudesproductos_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.solicitudesproductos
    ADD CONSTRAINT solicitudesproductos_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 c   ALTER TABLE ONLY public.solicitudesproductos DROP CONSTRAINT solicitudesproductos_idproducto_fkey;
       public          postgres    false    241    251    4967            �           2606    16775 :   solicitudesproductos solicitudesproductos_idsolicitud_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.solicitudesproductos
    ADD CONSTRAINT solicitudesproductos_idsolicitud_fkey FOREIGN KEY (idsolicitud) REFERENCES public.solicitudes(idsolicitud);
 d   ALTER TABLE ONLY public.solicitudesproductos DROP CONSTRAINT solicitudesproductos_idsolicitud_fkey;
       public          postgres    false    251    4975    249            �           2606    16780     subrubros subrubros_idrubro_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.subrubros
    ADD CONSTRAINT subrubros_idrubro_fkey FOREIGN KEY (idrubro) REFERENCES public.rubros(idrubro);
 J   ALTER TABLE ONLY public.subrubros DROP CONSTRAINT subrubros_idrubro_fkey;
       public          postgres    false    4973    247    253            �           2606    16872    tamanos tamanos_idproducto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tamanos
    ADD CONSTRAINT tamanos_idproducto_fkey FOREIGN KEY (idproducto) REFERENCES public.productos(idproducto);
 I   ALTER TABLE ONLY public.tamanos DROP CONSTRAINT tamanos_idproducto_fkey;
       public          postgres    false    269    241    4967            �           2606    16962    usuarios usuarios_idcargo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_idcargo_fkey FOREIGN KEY (idcargo) REFERENCES public.cargos(idcargo) NOT VALID;
 H   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_idcargo_fkey;
       public          postgres    false    5013    261    275            �           2606    16785     usuarios usuarios_idpersona_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES public.personas(idpersona);
 J   ALTER TABLE ONLY public.usuarios DROP CONSTRAINT usuarios_idpersona_fkey;
       public          postgres    false    239    261    4965            �           2606    16790 &   usuariosroles usuariosroles_idrol_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuariosroles
    ADD CONSTRAINT usuariosroles_idrol_fkey FOREIGN KEY (idrol) REFERENCES public.roles(idrol);
 P   ALTER TABLE ONLY public.usuariosroles DROP CONSTRAINT usuariosroles_idrol_fkey;
       public          postgres    false    4971    263    245            �           2606    16795 *   usuariosroles usuariosroles_idusuario_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.usuariosroles
    ADD CONSTRAINT usuariosroles_idusuario_fkey FOREIGN KEY (idusuario) REFERENCES public.usuarios(idusuario);
 T   ALTER TABLE ONLY public.usuariosroles DROP CONSTRAINT usuariosroles_idusuario_fkey;
       public          postgres    false    4987    263    261            U      x��}KSY��:�W���]�2��x?�KP
�� RM����!Uf������,zً2����r�'�K��~��ިt�^� ��������sT�&es�n�ͨ�\�;����3���nWͰ�^�bX��c��yS���rZ�
����t������A�6��l��O�pX��r����.
wT�'MqZ|����Wׅ;.ܡ�a�������_x]^�ͼ�,�+w����x�u� ��$H����yA/rv�Y����������h���{X�@���i��r�s��7���7�tCA�sR~�F���k�U��~�����o�jXk�$�M93��e9�W��=-f�K�M�����p�LҰ�ԯ��y=����D�q���m��(΂8r��/\�2O��g�W�Cgz��k�X,�q|9{�{8.�tz}�4Hq%��q5u��k���t��#_�w[н��Ɵ�q�ă����N9�W����Kc�s�4��(�r�A�K��j�3hk*��k��E���zVIq{�NAgT�?��>�s����_���f^:��_XwSe3��|Z��+����"�s����ʹ�����Q��{�ݙ��KQ��f)�e�N5)q��a��:q5�������ci�p���6ζ��VL6�̊�&�7_o��m�T�q}��3�ۦZ�~&�s����E����xQ�F�De����S���W�Tݣ
W*�io�w7L$�[��.?�_}0���M)�|=���~Q��c���ً���}��8�G����I�0!L����{�~�<�G���H`i���A�AS\����v��OGՔXTwTXow����!M$Z�@������y&�n�Y �����}���ݷ-Űv�CJ\gt��R�����Ã�C� I�����q�1� W�au1�o���A�β5Ӓ��/Hɇ��Y{~��{��v	%2'�w�m�#�9?ᕹ��y;��	�<!EW򫯕�T���o`\�����do���^��W���J����l:IC����k�4�����4���z~�$��Z�"� �7��S؝�_ڊďL������_�bE��^�+e����_��[��H�^��\͐�v!t4�9ݒP���Y&Q�{~���p���:�aUp����ݪ!��������7\4��*����x��܂G��[~EF̕Cp(�Y����9�J��X3�����!=*��9ԮP���xT0S�C�i�g�iaEg������=><8:�뺖��ߝ�Z�k�t�݁(̌�{~���jO�R���,zk���^�~��;�H��ZbS������O���ѽ�U٫�6��[�:ҋ�<���񓞟9�ń|��qю��K@,�UC^YM*���.�~!�᧺i��D�`~�c�y-{5s�,p�m�����d�e�6@~��s���\��=d��O!U����b姰����sE�����Vo��f�s��V�x5�J�u���Q�E��]q�žQei�i�{��W�K8���0H�Nє�.�`a:8Ws�v�����zl^9�����Oj(����s�4(�?�2��~/�w��6����
]M~Fo������I�G�YA��b��(ň�b�y�C
�^�JE�B:�έ$��Uj�]��ԥ�/��2WdË��qr\g0`l����\i
�^v�q�2q�ۦ�A.�:Î��T�6�&��E1,`��`_}���/�$�����
:ӏ�8N��d���	{Ad�����`���Zf[FW��>sH�M�a���P%q�P/��.Q/�)�e����zB�n�����)<vDR`$q���ngW+�ĵ�G'JT�a���d��E|_�#|k��K��^�����Ľ &�K�����XȒ���;�3ɔc��W��ނGzY���hFOK�sR�
�q�����989���o����b�U��ߺ�ǯ0�B��?��`�A��T���q�+��lH��]\՟AM=/?��S���W<�W�q�+��6(B���'��x��h ,����V�.[wc�7I� ���(ӡ<�w�Ki/Ȝw0x�s�+��;7������׳n����0=+��pPҀ.c�.Bz*��YM����$X���������n�}�q
�/�V$0�cX��"Y�i��-0a[�����YVw6�k�ȇ���ѓD���dI�BXa)$��'�8�<+ǒ�����c�u��`ow]�S/`�S�����[���pO���V�{���h������N���xE[Mw��R/�9�b!�	�Lt��v�)V��O�˻�4��i�?S<k��q��B�z��4˘Yz�2�y�;�BD����H��/����(�"������L��K��0nG�i@ᚖ��t�ǃ'̉��z|��M�XaT/D�%i�f����.��ŗ��-!�t�⯋>�z7b93����u+b/���\��a�q��^B���k��KM�>s��Ei���Q�b�(o<l�(�3T��K�����Rf�N�����g�%k�K��B�8+�g��ij-�s���b<�-@��9�gh��`oEp��_�/���D:;x�0�����"��1�J�u�([2ΧPI�������k�3x1xe�����^)��,A�-u�g��w}jAy_���	�i	ч�<C�w5QJ������ R�r��E�j�Sh��?�W��(��{R���Ap#r�2]�ҭ� M<�[�^;o��j���b�Tp������(~�S��g�i�@	o�hw�%(����4�7�x^���0�A�@�!W�~�ǰ�Ї;�5��r{9U�V*���>�;8�����T8�xΕ�ˊ��a�q�\f��QS�d�B����H�k�Ĺ�!�vä������%���ռ�I��=�-)�T-qs��(u���{4�x��/�~ �<� �hޱ�G��;�Nέ��3��!Y�=^҅*�!n܃((�k0������u�piڐ�u|�}E?G\�Y��ث��(bHJ5�V���H6�<:�A:9u�&8��T����<�?6� �E��;���e�W+�z��	�,�m?I��4R��"V:�9�ԁ�\�K�]T�5=�┲��ͪH�O��i�5T��`$����6E�c���v��P����ĬV@�Y�z��d
8�!�,�T��{O{�-�0$�s�Ɗ��#�^p��m��'�����߃��C5!�R��n�8q0�-���Q���)z=����v
�uѓ[�1���#�'�����z<�_���B�A�Ξ�[��(�E!�a["#��z�[�i������a+\9���jTR$!4>KEk����H���Юt�3<��^��u{,D	�(����a)�ay�R)D��/82am*򟢊hq�,h�������2�B�Ҳ�QFKgQ�ң^;}��P�6�?\��	�����0���C���iA��z#�F�����=4�8��τF��`���o�M���W��2�7..KY�[%AJ�X��)��ʻzV�B��?�៬��o��y7+%�c�6H\L�.EDa�ҋRg0"��'�Lp �#<N��+��ɧpl��RJ�F�+X���|��??'��!DKӞ��*fhd��8�����2#��Д 8L�uQ���8����ar���%�vh���DY/�9!5+&C=[���m��T&�����w��Ŵ���@��
:T���i���&��2]���z�R�W�G>a?�`�t��ؓ��`L�iT�}�O"�g��(3p�)�����߉�zߓ�$�V���Ö�E���g�[ȼ8Sj4��p��׃6yW\����{��U��XU�}��`k��8Yِ¾��wo8�̭VF�ߋ
�K��PUb=�J���K��[�JL�np��J�/E�E3��6Cǈ�ev�f�pg��X�c�i��8y�+{=�epIV����u�R�p���ӽ�&W�rńk ��B�?����A����{P+]��*���XB\��X���th��G�WT;P�5X��E�����>A�@Έ�!�G�M{�-���@�ҧ�O�w�l�����ܒ�:THQE�?�z�1�];E'�    )2&Y�A3���y_^�6� ;M1*����q�<tM��2����8���yuw�p]C{��5�5��H}ҋS�#�jʈ����y�q}JŬbm��ut)�w}w ���w���R�T��۹��;⋕�u���;�/N{q���o*��R-����]�N�@!Ad�w��@؋٪JP$c�8�Ϭ@�U�c�q�#��<pQ��C���	BR[�3�nR^�Ӹo	m��e���Hg����?��_��ã��ݽ��)��{��Ɇ%]m$�G�f+��q��9�zI�R+�,��� ��5dnB���h�$����1G�\Z����+[�*�7��� ���Έ*uW�@Ȁ�<J\��]/M��O�^��	R8NEz�02I'Dy,*ԪD�*����E4�*{d#�N@d����/��?�C
�*��-�B
?4�Y[\���^>>��ju����?w�[��؇��ًzIH@��%fw^���WU�G��~;�`p�����T��c��7��9�g{D��b�68	$��B�l��v�S�/�l�C��6I��C��O!YC�79qSb�nh����z�����ȁ*J�g�r:�_�L�����j�xDc�ኚeH�����zI켩�%�nY�SUJ�ᴓ(��pz�!Z��Au�5�'
�u����^��~�ME��`6lGeC����(G�*@u�$V�0��<����ǃ��G���顪��.����0�����jRpV���4d�䇞�a�^�:\8@8[�GZ]��ث/`a�Za[-���"���c��h~.:8����6щ�+uIڃ�;���`���TŰ��</I��C�ove���0��4�⡲ ��U$Y/ɝX���P �9ߦ@f����F��w�Jx��J"��)�c�M1�U7�S����Q<���I�y���{��C�#� ¢�Y[����g���������6�`�㴽������<�UJ�^�;ǌ`�o*�
�����6��Mz�-�z������k\��#��A#�&�粹��'��͈�Kg0!$������;��q��I�7�Ī~�R���{����;���ǧ�m�� %�2M�:M�^Je�ٜݳ����U�O�y�Oc��.8F2��t2x8\�p��44Et�'�8V�zזg�!f�zHس ��T�VV)��7�o��Y;'��,-�I�-�}?��#�zi�(Ќ4RGZ٤Ʈ��BU�I��n�Ůk��
f;����"#�r�@�Y�ƽ4q8�����p�e�P��ބͶ��_[~�x�V#�����@&�/���F�H ����@&�jζ᪵*J�^�rɁ@妓l�����}�x��6�7�{�g�l����·;�j���t�/Ç�D�E�*�.5$�v�E��cf7����H�u8F��,'�Z�;0�
��ڧy/��%�&ؔ������%�FŸ�{��;�/"�I���|g���st�7�
FY�zz�p�E'��U{}�f�V$`���Z�ȫ�er�(���e��7i̖���h(�HIg�]L���|��#��e�B��cdA/�(�)kJ����"�I� �t��5����}6����]��kOCS��^9?��͔3��É�mj�uvZR�ͤ����Cyߡo�%��5BJ�5�k���B>k��m�B����Ж�U�������aJ�9��ǅ�7���a�r'�4G�,v�I?$�rږ8���m��2K��D�=�<Ss��TE��G�?���/pZ}B�&��D��Cb5�|����� �>W�uZ"�㤉�{Y�u�[���o�����(<�L�H�&�8P�딪]6H[#��mn~���`�c�,z�L�����0Yh�����~�8�M#|)�Z��R���pg|��.F|�
��5|?.g�J����Ӗ��B��[>!�
�f�h�\���^󳩟{~�ڙ��_ӟ���yM��gY+\E¢��pI�|�v�p������!ݬ�ج��`F��j@�a�w�(^�����8�_���;+�y驏Y�n�M�	%���c�uz/�S�x�yHY/c Ұd�ʅ�B�^E�GM��+��qyюI���TO#�߷�h��0����vfJ>�����c�D�id��{��p��� ��v<>)��YE�i�c
�F�l.�낡6%_��|���/IiR�b��,�^]�{��wR���"ã���pL��cu��~����6�7�q��B���PSͨ4�.�%��!~omRّ%�rʭȔLy<�-1��q��y�z�����tQ1�#����L�%sݯm��!ZN��p��[Ha���D˛���T����塳_��4��q�>��~�d�R�; 7��ԅ�1g�O���~�\o��6���?T�a�")�?x*�$(�<�4�a/�,0�A3��8;Q�D�*X4�1��7�|1�f�O��1/_7]K�8��t<�Dl�U��矋/�v�����LzwC����\�!����Z���X�D=�������RX_�@Y*����ҟ�M�\�,�_�?�Ts�k���p37Nȝ��R����iXMQ��-~*�.��ᬄ�$��#�F�yWMn�:�m0H��r��!�!U� ��
��t�k�2[{os��I��WC��?�I�\S8��ڸ���`�T�U�b-ه�E���+aIqDo*"�]Q2�����RG�������TÀ������볶�/X���t�c?��:J2K'��<�v�I��2�ݛ��$���J��_k8�\���� ^�9����"�L:�{X]�����,(�KI�9mesVι��V���+�^��x��s�S6�v{}�ߤ.�M�$���H؅Y�b�x�ۇݣ�ݭ��h�[p�&T!b��'g�7�O�Q{J���Om�_�a~ԭgC��o���	"0����DWc���(F����l& |��öa�N����uo�������-�ԩ�����R���X��5�:"����Q*�͠�w�)�=��oD�V�r�x�4ig,��-Ӟ�`�������2R�t�����N��r:�g�U�ʦd�zU
J�@7�B�eq���)Lqq��?�F�o��^�B��r,�U�K�p���C�������>u>�a��7���L
j�:�mDrO9#diNpʤ���=��>��Z�nZ���N1αr��Ɨ��I��+^�z
�fV�.c�"���	��Og������wG���@{���E%�<��'��QG̃i���Q�Ll�@�)�p����ã���ޞ��E=pBy��GT�"����9��L����9*��pJdP$�J��1��L̬��p/*Aً�kc>���`�)�E)튘E�d���he�4�:��MEc��A�o�����C��?���[��Z8s���J9�>�T�5�vʭ�>�����݁a���Q�zq��%�Q��ǖ�`w��k>�/ݎ���N?3GOp-��g�7�?�㞱��qc��_Z�������EGIa��M6l��;G��� A����W�76��R���,����Oi
�1!*j�����|KE�\cwڹ�dx�FC�$�44�gh&Y&U��u=%��C��O�?��mo��(f�"�vV���6+�''#s��ҩo���2"9cgi����'Q$��wi��\�OC�����ׅ��@�����@���𪛦PJ�wE\rأ��3蚋z6J�[mX����9���@�#Q�8��x!hM�44�P�~$��#��+sÎV��r���a���q����ѥr�M��_:x%h�Jc��:����!?v#�4�oh>����O��Ef��,��V��j�^���*�4���Ȏ�������W���(��zI��[� ��+����9#�	���������$�4�P�~B�o
�Q�Z=\=]C=�={-G2���Iz���Pթ�����5C�wYڗs.�loH��|Y���1g��rBA�Ʊ�*���â����,r��S�MD�J    H4�����n'�m����`�������H�Y��99�3Ѯ���s3�Y=�5��&L�� �-�g���R����\US�Wc����7;6E���S���_�I�r|!Y�d�Q�E�����r��u �B8�$��<�9����r�C8t|p̕�J�w^�Y̵5!�>ë���ɓ$ʌ��Q	���������:������X���X^_�o�j�`�Z~�.�o�r���K�V�`'�u���%�Tp�8�s%�ČIª�*q�b��=�iBx�>z���Sj�����
N��^Ǯ�]�pXC4N0��nc`h���9�=m�����b�1Or}�A�4���a�ػS�9_ia`?�43���yj�ie�
�uX� ��W�[_�fK%�Uck����� 0���+���%�P�y��$OM)k@P\�Q}J^$�Թ��Ș��m\�76�]W���E�`�Ds/a��+���j��{Q�i`��X6WR~X��'B��x�Z̯�s>
��W�Z���j���xvwr3���h��f_��ߨ�\��q.1/���)Tt+�sV*=�3�r�EA�����9Hi�dsW�ʥ�q����r��bv^M���T1z���������?v�|8�beK�)�Z�"d4�������Z���PX@p	�w٧�6�/{���p�F>a�y,��c{�BX�X�%�c�� �\\+�~B���}O�z�v?��|�����?x��d>Y�A�s����~���$�vVt�Y:��j"�cDc?sA9
�y������]1��7�_��y��~�+�&���{��%�?��u&�4�D[S�<i�����i�% ��j�#��e�F���@��-.J��:SÙ3j����s����q�t�X� fjoWu���!�}E�R"L�VO{f<���_)�,A{i�{�$��m��zR;e�/Kn�#���^���}��Ua*���2,����zե�t𾿵��B��j*��s�)��*ä-O��ze2|�A�����R@U�ZL��e-b�knqV�^}�fR������$pFROc�	4��#=���m�,�V�S�<xrC�	vIC��Q�S�Uj�+��M_H�9o�Q�)%ǥ-x�&�
�T��#�����ZЛ�'L�] ��4�4���a� U�=��qc�9�E`C�x�+	�x]�Ů	,�\b�xk���i�3�� ��lþ}��tL
�Qw��
⡸MR�↙� 
�\۾�/Ly�k�(Z�9���85*�>�� ��td-	�8�����I��5yP��Z�A���=���bK=L����b��T�\��b9S��َC�;�~BK4�������?���X�7�gc���Ci��
/"��w^s�`�K�B;)���¾ K��둠 v��V>4�&��6ͤUo��٦h�Ѿ��a8G4o�hTX��M�q������:۰t+_��܅����#X�(��2/���b<�XF�R��GZ(��X�Mv:S5����bgϽ�'�%�ɹ��N����J<���W�W���'�9��(!��SI�	�������C�����T�"�D�\�6��d%��fW�����}5�L�+3����Kf4eMIO(�R�� Ė�#F;�Mc���æX��$2~_���O�>��jʣiAl�sҔ��X�ƣb��|c}5�&�If%����i���{�����)�QՆ;��8��Hs����[bO3����vc��4'�f�R-�^�5J�/��!���~��+Y����YnHkM*ڇu��rS�B��R��=rA<��b�NE�Q��|�*m��X`i��H�X���7�c)��� ��"�s?#a�7Ce~���zw�Ũ�-x�F������j��{$y�e���Ł�bڔ��Ed�QbO�1A�e(���v����?<������>�Ƭ'�����������T��f�'N�X����$:�R�g>����/����no5�}�S������^?�ly���n��|:�P�9�;UF�^��bKi��ޱ5矦�Ѹ
�	m�"�,^�s9b��`��:�?]]��&�)v�]-��)2{�Y��*�NE
�0���I<g�8���u�)��o@@m6v=�T)/�s�yAj�؝�w��Y9��,���Hk� �9P�����@N��A'Z��PH�{��r���6�Rd�ں"�**��~oT����8	lG:�a�j�1آ��� �����)וe����<Db�����<����`=��&�IzI�B`��Q�	$	r�3S"aC"�FA��*��G��K�N[a���tl���ze|�������:��;I%ޙ"=J��3V�'��G���O�kMTx�i8!'��"8���Ƥ��7��4��DIf�Tw���B"��hBUv�52<���Ӄx�Z���Eyd�y�� ʎ���S�|��QwM���v���E�}�hC ���ʤ��4Ba��Er�ո�m��&��g�����x-)�7dN��9��K����i��N�L��_�(�����q1?����E�
+Tm��ŝ&�\�ܭMY�#$�o�d9�Qy���j���LV:G_� (�T�W��}��B��:7n���*|�~q6*x�V;o�E�<�c~ePʙgU	�V�������7�y2�NEN���$����a+�MtlQ�Ɲ~��j9݈���/6����?�Pf2
������n2�]ξ.2���@���A�������A�g��3��U�>35��H�1�
C��x{�X~�¶łW����?��7E��z\�:�@Ў��O;y�4N������ ʹ"|EA�����=$���ya`d��T?	��s������0#�;�$jM��	�BM�#꣧b���w��T�;��`�NO��a�̑�T(bf�u��;�,1�I�r9⬫d_�x�q?�E�4��s���=���F"N�:H�$��4�t���am��U�f'^X��O�>x�J�Fdr��<p����D~Edp;)���F���y�Rͳ�f����H�)��Qf��G<!�L��7/)C��<�X������3���i�f{Ɠ@N�C�k��;4�0�y���!�V9�_����~�w|ׁ,VF+�4�0�y��_)��5��_�_	ɖ|ú�U{�r���h%��ҌM2mG�io�t<F�c�x��W�RJ�a��nA���s���D�D�Z8N�JN^K	H���4���䄒P=��Wn��>o!xϤ>K�(����G�`#ߋ���#`Bi-�/���s���)%��4���� 8px�7��կ�_R�)A��ΜT�m#�(5ŵ�z����&I�^�r�����w����#��:��ez���e!�q����O�Y�⾧=,*�FH"p��5�D���<Z���BXޜ���@М���
{N�@����9�C�_Զ@�yUřhimy�u���wu�R�/>�7V^@�����2pr�!�^~���g{M٬��9����N�"D|�JQ�s}Y~��q��A�k¡�}.����؟
`g���Y�ߣ���<mD��k�4��7>���;�m�D�f]*�V�_�L}Y��M?p�PD���Y-5������5�H \i,7Ћ��c��d������$���uu�5�y��MAh��8�o�R�8Ń@;���6#e�M�C��F��+MR/���\q Ӎ(�#E�aP�F%��R�}UZ"�Ӏ��0+~l�hy��񰕏JLZ�j8�4�q��iH���?/���@�j!Ѱ���gC�����~����ːU�}Rn����bR6�}�)�{Ǿ�5�`!�";-�������:�hp��z_�0�~���e�qԺjͣ8M�䟟��1��8�x��~��>@�����0zU��ys�<|����P�0C}s_#o����^�\� ��>��j�lƌ�L��_�k~$c�!9���e��V����Nq*�l��i�/ը���,)O_T�	9p��9��)�8Jc������~]��(�Z�^K��xt�ҿ��@nBd    f��>���rN�xB�p�PA1x�mgu�k΄��wi�b�A8�α�;���F{�"Xs����v�Z��4�"�\� q^�pD��dX�Wis��i�G��ַ��k�cM��H1��miq�pDL!}�";9�<�n��WD�[�G��~q� �<ȝw�po�z��=/�Q��f���	��z�-��^�����;	r����l�G-l�@Q�)��;��N�5��Ut��Eb�`��{��&)���rU7�Ǽ�ȑ���K�-y�_�.r�$���}	�H):+xy'��&�:˵4P��e�	Y��!��^�E!A3F�=5���hB-���B�����?�h�>8��3�K"��~@!�j�.A�����k)�j_�HW=�v�(��%C�;ki�N��J�(- �Iś }��6���JKڂ\�+�ꂧ'h�QE�$��}`eO�s���������H���d�A%�9l(�� ,3n�T9O=W����$��wCQnU�!(�XE2��a�ן�T5�S9{]��[D��1�\��KAl�s��A���G�P���S*��u���I�FS#�s�]&}u3����yQoD����`!�B���F�]�9�]N���sA�߾�{X�q��aKkI�}$�	2�R"X���Tc��i�����=8��Xk?�FJ�.�O"�Z"��(p��<��nS_զ���=�'�y��M)YH���iK��1S���h�T;��;�oqu�7�He�(��#�yXR�e[�Wݍ���� �Ws�#-���4�f��ʱe�&E�����b����{c��o]��F�������#��E�Jx��A��9�WYX]��f&��Zƀ��Z�>ԓ��[�:ǡn�ؚ��+����B�߮��b�K�]X����D�P7�� *�jo+�iQ���<�IY�;kĝ�p���|��i��C]�k�m ��^��⾒�g�-dN`�#Z�5��R����d薾��8J�$��p���V�?��h���;E�S�1ܸ�������PY��: O�M�7f��d�{�����[^H �̓N�A�Dƞ�6�X+=�z���9��j�:�e��H+cfw���˩Q0��QCL}��:"�_0v�~�[��rx��V4B�(�|ܒ��Z�M�/c�a�@�MQ�z"�D��n���ee
Ia(Z�q $����[n�5�Nu��~5�t^����r9*�cT���]�M[�2�0X�8�+~�d[���G�������J��!�yALY;�jra���y%�O��`�f>C?b���z<�x�+S�G�۽�����	�a����(D2�!���^�z>�5y�k,1*��Շn��%�SG�t�a}
_���^��,��S�޲ɫ"�hvI������3]�'�O.�2)�De�\��Um?~X�`��E�l��Ź*͑>��_�͛��w_��n�j��6im=��0��x7���P��P[�����q,tG,��ޒ�Q9���4��TDD�!i��?��J|1�rl���C�Ql�k'��%�ga�>l]Aئ)XC������ح��E���ț%I���sSj��9�X��8If��4�"�M0�Yʩ��\�t8jxa�j4"�G���N?6r3�D4��`�.�n�S�gm�H���F}� K��Ux ��<A�
×Ī�K�"��Q-O�{��w��b2"��#Ȇ�K$Q�tqڨػ�}w�3�'�P8�!�_+�וA�8	�<��%<��A��me�T�(
 ���%�DZ���wT���?0�I�'��q��[���&�w����*V�t�e6x�c��FB��AZ
4��.��_���o��e������{?6	���/K��ؒ3��ԓͤ&5��S}���񽂦�?����Y�>)�C��w
+�����c�S���W�vc�O��Q��O�3��ec��ZN��*������v�D�}
ۙl�9�� ��~%v����>Pi��A�T��n��%��¨��z�4B���x*�*�+/dI/u`��R��4Z|ɴ0���	��o�#l���+mfi�|η\���cNa�S�\�������<��,��ʲd8��O��( X�4qo#&�����dI��Q������� ��-ȅ�NS����|���)Tv���>����Y�*�_U�{d�9U��43S���-k�wA�_<�5���Ց�g�W@��0�i�d��.��$X���I+���V���&6��Ť���Nv�wXRl��k*��W�ӟ���&-E�>Eh���uМY;}��RS����ֱ[S���R�V/�i�qgy��8�<�IN~f0�<-��IIS]�]4��'$6�r�i�57G�J���7yw�7:���E#�����&>�U|�&>��4X�j�R�:1w��m�ݟ4f����y���8K�7ݬ̷�c�~�e �#���������I���9����(�:���e�~���п}��K��17�&Z��(=��J�h����n'u�%�p��{aufJ~�M�6�$إ%���k�L]�������o�y��H����a=A)n)2�Q1�:*�/�	�q;`���ߠ�Ւ�v"�$��uAEI������O����TL�$�NVtQ�T��G)�����Z�r�w�r�+HK%��˒%� F�z�(7ƪ_;�q��p��3S���B��-A�f�|DR��0�&��#�t��Cx����3j3��W�5w����,�r�2��O���3�er)��W�2o_�3��+}g��{y�p��\�.�"��*ȓ��bpg�6C�rO�Ѥ���5K9ܠ�wh��U��O����tj��+W$�k*�_��I����� h�c?G"K�?����ˠ:Y�k�Ë�C5bS������}���O�p�y�ܢU���=��?\7b��/F9������l籜���ͤA OX �t]����%>
.#�Ǽ�uC��(
��a��;�f2v 0�{��ɋ1��@ĶS�0�p�ے���,u��/�3���0�9��,iG��� s�<<c(6���*5I�C)jK/�z!���a&ɇ��S=eM	]_L�x��b�V��-?8бbΰ�h���N���]/��_��M��"9G��>Y�_@�(�ٗ�]�@��GR��T�H�A*��#�(N	w�� 4�� ������~�eIŧ㶰 XO�x�F4�'��늲�*1������I�s/Z��}��ɢ��=�[B(Kc�d�a=.8�w%���%i`�z�.��n�T\�����Ǆ�����ZS��D���H���B�fb� 6~?�*C�U'����t�T�e��&��zy�E�K����|$�#u�g&���k�S�i���"�i��l�cP�$1�f^�n���ۊ(���.u�N�3�I�0�d�T��I7wC[E��e�m� t�a*S�1��a�����+Qf��v�p����~*��$��w��V����Ӛ�L����iOQ�"e<Sߙ|���V��E9SEh����մ>�m_�Z������v~ �gn�n�
Dꎏ�)��zz�99��V!H��n�%��	�e�B5����lx�P��-�}�n��\ٓ��^H :��[����f��sܣM�����EIu����)��ڮ�dz�UCz�
D\��
µ���#/��B��[y��c	]5�EDy�f��vҧ�!��<���a�,�	_�&|��kS�C9*ab�'1F��zn����i��~�|���B̡��X��C���1�_jX��`h�hm�#=��5A�	�nM���� 
-�a(���Lc��Z��R�� �H=,��M��xbf�Պ��ʴ���͠�W�=�mG*����Q��Qxh�!ocg�5��I�aQ��|�Ow���T�NF��\?������P��%IJk�5w�-A�;�d`(�J�J�L@v��ѫx���d>��ٛ�s�9f1s�N�%�8/�A�j���F�O�/ɛ�FPe���5A�	w!H�`��B��ښ�+ntT\�k���m�O����5���ť�����`�O#    �XB�ֈ�J�a�a��A�ؼq������Rɫ�p�;�:�� ��Z�Me5�}�#t�~f���(?�����\����̅���b�H����ö���k�oțs�S�n�o�5���Ea�-c��20�%��}<�Q=�d��
&��/�7}e�*���el��D���>t�B�ӥ\<㥨���{=�8wd�O\��ǟű��=�� ���.��.ObڿL5oaܺ�w�(���0�FI�<$�'
pg/'W�� ���p�pCC�v���eg���%}�I��C7e���c���H#��0�u��W��+�y�;.Ϭ�k0��i}��ܿ����OxR�����=��j:\����|?KB�,���xO� ������,wu�~�9o���6b�1�l_�����M�NM���Q��u9=�� ��,�LHE�'>\��j@�ϯ���	�&`�#�yw^_ {��M�a���0�����r�[x��"�;�Xe ��`�#�z��h���$4XC��_�.֧L6��S���g�-	� 0���G��g�Sk��R�Ը��⵾���:�ŋ&E�d�>���ui��4���E�Iy�k��V6�z�z��L"�v�>�b�%H��h�\�B�������"��Z�����~V��v;�T���q�z�.D&���?�Qߐ^��R<�x�װ�����@�Y2��be���;T�t^����tp�tS�Q�,wC;�,u������Go��Um��3��9*A�~A-�Oeoe�W��/X�J��1��Ôp{|(&*I�`����zT���2*V���C$�y<�@�+��7c���U�oB��40�g-��ｬ�!G��>�5�� �#G�׋�٪ؾ��	pGb����3�o���zB�o:���{�[�)?bW���\��r�Y�ɉ(���@���Xv�Y����uy��=�kjq��]�5c�~O?Q`��|���nN���)��~���6�1�_Ia�Ł:uDzh�XfxT>���*#��ʍ�:-t� # ����cxUq��G�~������^���Z�R����#�����DI���̬���׽�>�@��~ʘJ��n�� �Y���\�8�/?⭞���K�'_���O�يžR(����&c3�������}��>W�:r��J�Z�^_�V1e��aX)�5�e���R
D3 O*��]z|	�Ġ��tخ �e�o���R���h0�Ғ��N;!��S�}W�ԕ�E�Ｏ�51*R��~�T�K�*<���F��,j$������ݢ�?v���:Z��*�f���$L�,�,6=����$gf�D�9�;�k��9	�x�A���?��U�_PD�<��JI%0�I��i*9���Y���#v��~^ YY�69�
ǰ��b�emŜ�/�s;��x���@ؖ��N'�Rf���fK��\����N�SX7_<��9�_E;�OYL��d3��Y����;ɜ�Y�ݹ4�~-��G�/�I���,0��J'���9u;XR�fs���>�)��>nv;�\H���iD�Ԛ��;��+D93�/�5�n(7�D�F�Ha�R�ySP;�'#��q�Eaj��"�æ��fB�'���>����85��¦������"�?�P;������`\S���j~����x�@�?�-N2z	���0�il)o�fzBd���J�~qB�oQq�0�ibF�>���D�!d��z�7�Ap�����躙YA[��~^wQ�&��u��)�({�$��r�Zg�]y ����Oh���f'�,��a�2=��- ��_T�=�ڥ�&[���Qy5i�#�������=��r��^�!�G�F�?BܗR�I���Bc�3��,��%�������CN�4���"9�QL)W��"�ޣ�P����X��+O(��=
�d���#3im���vz뉹z��}�~����~�O���K�˝�>m��[��4�tX�L[��qd{��d+�|X펛�EC[3to���	?Ib��d0Y�l�C����p��
�E��/Dv�p�b�|,�pJba/��<e6���G�y\|.z�R�s�H��\.����jf�%;D���+�����*�9~�������V�6&��S�I���Mic����A��p�N�ź����m\�)V$�&�
"A(���Y=�~QŌD��	�nn���#�/�E������3�.���j6N!QE���pi\�������摐nx���N�vK��Wc��S*A/�ٜ���+���ζ&�gg$��(�3�n�:�A�+���74ۑ&�؅<�E��s�xh����-P3�˚���F~�����e���O�K�i�H���8y��'�5�Y#��lY��D�4R�/����k�@���i�.[�ؾ��7ީ��v��[g��W찋�X�d���,���8V!�M1/�������|��)}I��	��+�}�M���3M�N����h�xk��w�$�%����u����esYA���b�7�W�5�r�2Q���^�yy���.�*>��2
^I�?n�y���8 o�S�/i�:��S�����dw:���O�Vv.W����1���ΐyw��֔����ϯ�����	���p�$n~����u}�)����X��b �?��_j����[ɟ��3Z&�yS���R�Y�]��_s��+�6s��g�o`Ĺ9N9R�ax�L�ӆ��(�a��{�iX�B�n ����/yg�!��BȤ��kAg��^����h�m�7��c�wx^^Kfv��IA�{��`C��V=��M,��S��/}O�o���oei.|��aN�BKI��<����4�Iދ<��嬞C�iLpe��7��Bә�)�Ǵ�D��lR���7�aBD��M�6o�;#���^l�R/M�e�~X�,f�(�"O�$0ڣ�W�K|���]t
w��U�L_����T�����wᆻGs��ǘ:�ԅP��(�>�z�N��Iy��M��/��̏~3͍j� v����>bj-~e}��T�� ��~��OCm�m%-~�}t�zI�Cp0�@S�Z�+I&^�G�rv��LɛR���K9����G"=���6��g��o#s�X	�b�=�ZI�.�Ĥ�!I^-P��4O��wc6�z��S9�I�������j?�~�t��G�b�~H�9/�?�M;�/Ok�E�h9%Sp ?΋��5U���)��m6�W4�H	���Xh�8��p��8���a|�'�.��+ɳ(�$� 1Q/u��LUa�;M[�hrH�=��8Q�x��I��Z�t�I�M�q�����ٗZS���lHi
Jhk�mQ��2����:z;�`���
�_ ���|AbƉ�(��3����z��i�(�_٫!���>Ł�*��E����Q1��c�V�il���=,��4E�����G#E�T}�m��������.%���+���{��׌v��ܽ{�伆��j�+Z�F�;4���۠�)?ႇK��V��*I�h�����>}d5`F�j� >��,��w0���A�K�@�=��|J���xQ�Nِ�ں6����&�u:#m��^������aU�]��ҷ�@�������E(��!BW%G�J�A˜0��n��g���V r��$�?��P^��>u��t.g$dD�V���pe��<��t��Z����~�|��31A�b�-�0�Rhm5���^D���/4�_$��uv���ȁ}� �z�<5GH�t,��Ү+���3�z�-=?�� ��7��-�\�_zy�A�%�5w���U+�F�G�l�g^	��c}�6�M��>�F߀i��B�Mq�*v�����`�)_��-��h9 �xb��i}�_?���h?w=)��
l,V�	��]啵����U�K�
�Wnqz2T�p������"(sx�K�`� 3��	4@�ܔ����&������3����������^X���y,�-_�$O�Gj�D�/N6��̲(��Ԯ9�7Y?��b(X���Z���%kFAi�ܴ*묿�Lk��?=�?ķGjv��������jP��    �]\����9΄	x��I[6|�'����}"aVriG|W&��4��ZwP	JCKӀ�+�6��J�,]��H:���
�-�^a$��2�l��:�2�F�-�H������;_5��� �@�+��'�p��#>d]'�$��L���L~}��|��C>������GB��>��od�h~��>��l�{޽Mϡ0���ii=b�wzi���s�2A	[��J4�9�_�CQ�gq뜛'*(
C¸�E0	S�7��ᇾD2.��Y9�;d�n�����(H��M��������
�`x�ڦ�.X�D���\�8�a�x ��50�{���U�C[�����6�,�0�Ȅ�Ga�̚s^�M�4ث��K#'A��]DB����x3j4"(�be�pĭfc:僠r ���y`A����X��@���	�-L���m�O��Y4Ue��+���!��?4�J!H�Ṅ�$(�0�V.;q�$/O�6��L�Û�͗U����)��$=WK2��%b��ps˕+6v��!Š}����$�����o^�����˒�P�y�[����(G-��8'휶?;�*�{�2fZ"�+���Q���~�r4��!�*ǅ�/L�/wl��Ymঊ�Z�&�:��P]u�ϛvT���?ִ_��h��B�n�[��淩��S �g��$C�E�lc�-l/���X���}�M�Z��Ǫ$Rx�qF�Hx�Q�|_p.]�|�\Uzժ��NH�3���T����	��|ӋA&�&������������8pwF��Lg*1g���q�A�G	H�`q�QN���Pd*ӣ�W�~�mJ�ƩX�H6�J�3�2�v�4h�(�]�6�Z4>'i2�qj���R��A�F�Ϟ������iX���w�p���J�!ɹ:./���+岏Z�x�"C����A�������&)`f��Vn�Ԡ�c���?���%(퇂������0�8�؇�g�ީG��5���د�AW������QW�έ��p��g�۶�\���Q�H����|9M4r:G���K�`�˻5YIԳ{���o$�d�r�}d�̪C�;�hy��B3�UH9\fܪE�.���u}�~�Q�ӌ=I�j�yg����p����z����=�-w�:$o�i	8y�&�!.r����DPy���*�����*��8�o:�+/��R��)��~A�<(��z����P�PakyF/~D�ނB�sؿ�N�h��ԿsR\Ai�@����P5N=��:��,?��E��@���� ��+=5;�8�X(�P�C]n%c'�0Q'��̅�=ұ���&�b;,e�P�t������RlA[���V�>��u��F#��2�'���hT�����W]Q��|d�e��/����'�;)��)X̝4��bj���w�oo��u�p��K�͜����$��$P��[T���N;j��ӿB�G�spGql����S�q���bv�s5G#����W2�L��H����)#�Dx<�;%�ׄ���}��Y��s�ψ��ٓ���p��T��D%���P�c%d���-%�n~�La��dqj	"'`�1����������l5�6�b��3LM&�;io��Z�g��9���=p���y���.��:SZ3
k��}��f:�+D֔��׵����~�{�ckZ�5��DY��Ã���>��x���N�H)�p_���K�*N�z�O �|b^؎vx~
j��G��՘���|\���#�`ze����HX<�jT�v�f�H�ʉ���p�&C��OC)Ei�*$�O�`9}<;Q|\^� Uz���z)������@��ĥd1�lX	��A�	)nTa��nR0 n]���,8&����'�Fx<�YA�	*8�����v�	|Y���s�zZ]؎gb�VSP</uc�&/���$��#I�����G�<1�TB����vDȢy='U����&��k�Y��&4X��շ�����u�r�|�݄�3��cIy1!y,'dA�����+�,�L���Xj�m��-�� �"�n�?,Ž�1\���5��}U��9ݧ|������O�e����I��.?�@X+W/֋�$����ܴ��^���RlBxK �륤F1�:)�f?;��8J�t�sԞҧP���n�I)N�s���) �
�>��a;f_�F|��YN����ςC��biO��tA�O�JQݖcSb���C��OQI-�C��L�eՖ	,4S5���30s�>��+���V�?e��bMČ��`T
�q�|��tW�N�IE�<4*)�F�l؈����_��)p3?Fd������J}�5ۢӊ�+8�<�đ��Rv0P�(J�Ocwo���x{* D۲�q-{ÛL�Q!��6��e-���GA�������i�4��+�O�ՔAD�L�'|�8�y�]���d�i��A�O��B'���rfԚ��,�4������0rֲ��ʭ�RN��/�ZHHhZ	�D���z��?^���SS�W���jzF�%#,����@�NTk,@��5SLU�(K���}<�9v(�,vޖ�F�V�������*>���h�c����^*"�Cbn6����n�z� �q���B���뒖 ��u)�S���_���Q���*��_M�8&�i?s�51��3b����\�R���8��?��YA�|-8���{�p����=��uF��'��<i�u��)"9�c��毌ې@�+�G�����H�=�@�Û�0�u�����Iڶ�^�b6qy�Zd�ٽcw����r7��	��s�y\*����i�o��Y<2(�:7�x��4���UJ�ʩ4����)SCΦ)����t9PI�D�*$h�����sWDQ�N*����.���O�e��ռQk2տ��A����R���wz�� �)�j�3�T��_\j��OJ��+z�ڍܝ�Z�߉O<)�Wt��������2��?�@E�źPeD�M俿r��`C9&N9-gM�I8�[��W(k�Q੘��ɒ�ӡb��4��M*'�r�7Uͨ �֘��S�?eQ�a��Z"aaJd���""8�=*�S��p��@���������v�8�Fj�@�"껭7H��i|��G*S�g���t�jm��/�.~��z��)c��o�7ix�h���+��A����ǛG�{�NL�!������(��$�ow�������[|V/���x���4��u%�]�ٛ�����I��uo�۸���C��,̺���Y�ާM^7<�J5cL�P��6it���
��V���yL��p�/�У�̓����*�յ��Px��m7ه�m������hDm�5e�U���=:��h�,�R�h\����L	������M��X��#$$���)9e#}V4園n#
dp��`Z���w)||>Nᦲ���9~�Bc���ڹj �3)�*��;!i*Řa6j�ϰ�z�Ҧ�h�8�%�<��*��<�X��r����R�D��a�!�K8���Y\�����ll�3��w�
�V-���B�~��W���R>K���u���*F�b�����Uy�G�2aF�����L%"a6<�K���܁�R��x1e��.u�ِ�4�!T�@⇤�KY������U��T��6�.�I����Φ��
}�}Ly�iK�DU��tz��ܵ�|���(�}��~@k�n�۠֐q.9�埒��T+�R�ٮ��W����2��Д��@MR�֢r��+-՗4�nH9�Ngۭ�M�I2���Yj|�)����Rc�Ҹ���e�a���Р3��9�VK^N�;j���k�iɓ~�Ya�_�A�����u��=(S���ڄtp��耺�7�����2����������D��uU���0z1�w��0��szN's.�������LRV�PB��;�}G���\"��T�僧`]����U9ܷ��e_F��WƱ�8B+��	�����t��V��j��Y	b�:M�z�$_Uc�3����!������:��x�1��I\�"i���k�v؅>�.4^ô�d�V_]    ��A�?6{���c+k]      �      x������ � �      V   �   x���A
�0D��S�Jc���� c$�Ѝ� �"H{��klݖ?�b���B�w#�Ԫ��N9)�
K�1�7�u;��0��e]඗EH�0f�%��9�A�LШ
=��S������^9�eZ��R����y�5���g~����A���_#�<H
)Ob����<�:GA� N=�      �   H  x�m�Mn� F�p
.0(�?�h�R�	��T��c����.�������V�~n��5�R�3)����1	u��M����O�[�j��Fl	5,=��zZ	�'�H����
k��V)��eD��H�u�ל{i,f�zY>^���	fm�[�Gv��������R�`)���a�1��o5��y%�sढ3����)�Gg�wm����0!�F�\RɃ�y�����r�( ��<�pPGY�^ȗP���k�*
���=a'�m��^Zn�UCP�|l=?��g���Mn��n�i�#�B4�j8�����dh/����6Nj��)��6��u      �   �   x����
�0��ۧ�LIk��]��(hu�6|��?��?6�� $pNHΧ��x���Fo{��gШ�EB���1���8n��nŪ�vހqS�쇞�~h�=s��3�m�����8�a�x}��Z�\רRM�oI�{�1��d�G4��i������?��D�pQ+�ഢ>U#�9�e��,�J)o�P`�      �      x������ � �      X   =   x�3�4���sr�Wp�Q�q�u�4202�54"N#C+C+cs=#3#K#�=... O�"      �   �  x���Mr� ��p
�h~$`��P�@�W�*��M|�\,-;����W�|�^��� 	ӒK�zSm�H!�Iؓ�+�僖�(
���Xr�lJlK�K^r*��+[jɽ�T�C$��a�F9)����>��ֆ�-��Kf�B�#��ְ��Ӈ�����xp�;=����"��5ET�R�=?�s����7�*9Зbo��o˧?���)�0c�K���}�����['�V#���՟d�����o���,~��>���Z)��O�G�S�z�Ꮍ��9m���9�\j[B�7Gu%p��5��Đ-Ŗzhy�؏�+FȚ�7��y�����>h���F��k�z�`;�IKF2�Ǥ���RwU,�Z��	��nA��҂v�:��*���*��%��%ݶQ�q̴%��.�}M=�3��.kj�yCs��~�1Oa�{�ŏ�a��7t\�L9�A�~��XÊ3��0�	,����K����Q��R��O\      Z   �  x���[n\7���Ux9��,�+P�!HR q�6�}M|�H`�!c���$�)M�0]*:(���~��d}3��{��م��t��C�ZsῤƦ�V�"c�L[��f�H��kb&�/+�t#�Z|#�H�K)��#Dxe��6�')�xN���h��h����v[�jO4���B�V�̓,suפ9�4�A��Ҹ�����j��߲����T�h�y����ы����̓4*��'�*�w�@�"3-�Y9)�Α	'*���Ij��������:�*�aFs{QѢ𠑴��1jC�©��p�%������je��4V]c�{�Je�f%��� �v�V��(��~?�E�]��Ъq��pq��~BJi��G����b^�Qĳ�R�>*}T���Qo���!�r����z�#�n7�ǜ�I��̓��Ww�1��%A�	�F��:ҽS2��dx=�:3�|P��0J��z�~��Ճ�f)W�"_��oh�J�]:�j��2���aa��8��=��$�Y%o�-+i�Ъ�Ъ+��h�}�'�4*'ZΠj���7BU�D�ش�b�b��򺚲�B9�6��{`�zB��,�jh���ov�ODc�@�d��p�(ͩ��9�hP�z��)�r5Vu��E:�����&�p�F�Y����UvDG�Aamm;���ʋbT��8��$�.Q�n}����'��f�����҉�����;�:�#Sw��M9x9��ilX��&�v�gM��Պ����:�JU�I�����I�P��dYIp�F�/�7���m_���Fa���WG��j�>�}4�(��}y�t�Т7�79+/�c�lB^V��$�]�~�%�6�'���Z��1P�в�pHG��-�-�	{⧱�kiΆU��Z�/���~����VVy��V7�a�,�Rutf��k���3[��v��_`�X=�=�Y���v�w��(�d�g�x=v}O&��@��������%����E�DY��T�'��D/<���h����>��H2��v٣Շ��|uXdԩ��Ɲ�qSe}]ڵ����g@3d�uV��ʊ�*�>�������?�{|������1�����gQ|O~�����	Q����?=|y��[>?J[�#�}:��L-��2���}/*.�����A����{�W$�+��$|�A�z��7�\컧�:��t^h� ��V�=���[WؖJ$l���Ã���{�-�� O�6�k��?�ߋ��By`ʣ�-�_�[���]E)U�EW@�8֞��L."z%3�$���]Q�����ü�k;EM����.S�M��1w��V��p��:�Q�R[���E��ӎ�Ss+=�Nv�keQ���#�1�@Ծ�1��cP�X~�R3���P�ϵ���Q6��c#kAU����@���A�Ǉ/?�-&���!�eA}�{�,-_���H�F0��I��o��+��|�;pջ����ݿU7      \   �   x���MN�0F��S���M���b��K6�c��6qd[q�)z1¦͊��G�3����r����"�\�;��=��Ø@��A�����Rw��ʪh[m�b
^�L�_��<���l۔����Lo���=`�`VzN�B̋ڞN���'J܆a�����؇���r�W4լ����Ցa�h��7�^1����d),��p��F�Bn�4�V��E�l#�[����s�      ^   D   x�ʹ�@��X(�#�^����0�,(:/e��dZ�l����ŵ��e��H��D�z��;�t�y��m�      �   6  x�u�=O�0�g�WT�\���u�V��	�K��*(%RZ~=�Tj�[�ǯ�����kRMׁ,uQ��[Jt��l�q���F/�2�j �N�Y�Pc������8.�d�Ʋ��4Ɗ������V�l�F��ğ�q���7���0S"+I���T�~<�����k����Uz��r�m��v #
�P
���w%���,9���pG$m7�lFo�\����B<��Y5uV6��r��e�/�?H��AD1P��x����a9X��aw�Ī6��[�O����yqA>ɇ�Z��
�~      �      x������ � �      `      x�սKs#ɱ&�F����ݹ��G�W'	�H�� R��1��YdVHvY��,g�űY͵sZj�fҙ���?�/���	D�[]
�j��L�@~�p����ó�Ӳ[��?�ɬ;���x�.&��x2��vOF��Io�
\�������n��R���<[������o�r,����7�Ǭ�7���m�,Ǉgv��,�0�	���]�sC�-��y��/��-������ٚ��'�a�;O N�#����&Y��l}Ďs�N�͒OI���ÿ��9g��6wC�b�� ������,ϓ���Ϣ��l�_95]NϺ}�8}�y�
8�m�ە`��]Gv^�|܋�J��1O�ϊ-�'���Iɚ]�M��[������0˓�&iC���e�<������g�n . /��	̷w����ΐ�v���|S\���X������B,�
5\�r*�4:O���I|<�V���b�mX���fŝ������oS	0���A��]�[Vd=w�4V_b�UO��׉w:�X�+�gs��3N���wm`O ��%��M�����c5!���"�ߴ(�۞x�=��Ͼbp(�e*�0��T��^gO�5!j�ō��r��l��S�O;����`^$��%Ns���XK��ѯ��{6\l �d+���؞U	��qi��¦0�&���4��b�ޅ6y�I�i��l�-Ś���^�I��F�t���� m	�kD&�t�8Ϯ��� �:K~3d���I��g0b�4N`;���<�l��K�@�N�����i��'�J,����LV�C�,��r��I��7��9
��@�\�;�9�w�4f�g���t�����6+��b^�o3��`�� k��������=�y���,WB�w�4|I�貣�΋�}� �"�)@O'��
��s
��|�T�&�<���7Y�%��'��I	�t.0wspX��$+��N:�#{��AV�i0���o��Y�_È�������;2�S����:a_��d��;��A�~Y��=w۴�%���K�u�<�_T�P�h.z0����G�%���{�	���O/�'��u#��3�R�8h��m�H�л`H]�z�`z�J�G	k��u	�X̳���[9��Sj6���RTƟ�|�1�n�,�A%�F)�]$���ٖ��fg��$r�z�^���A�2P-G�}�u��r`An%��I�G���u��X�{d2�G�/��a��.�]Z^�-���=s�4|I�r~6:���������M䐫��P$��K��2]=�I�%��v,�#ͱC�E�"2�p����x�[�AK~GI��Y���@Ði�'�
�un�=am`���#�(���\ط�t~[,��"�i���/���yc9��/��|�I�I��4]�ؤ��z��-�^"!)��a���g��}�1�dd#;�@m�y��T��q^�g9�ފ��f���6�gzlr1;c�x2����yor��No4$�NaT	;�`W�x2:������.��Eoxo�L������.�����;e�фŏ�>�OF�-n��x4�u'��ͺ罓�?u��X��҇������,*2�{S���_| yՐ��l����ڜ�!�Vφ1z�ky��p�L��qw��������4���t���$�ÎG��?^��7���q?�盎�i܇W'#���m�]	�M���5��ȫ����n��/������Q̓�,�_�崸K�3��B����Z��7��|�@k���H�;Ԫ��l�ȵ��_�����@M�4g0��Il)%���<-��W0�G,��'sx�u��T��g�>��牠��F^�?������M~ }�-��k%c�_�Ѝw����	�R�oD�}�9o+�k!7���~������)У�����i|ޅq9���0�Z�k{��B-���.��h���R�4[�x���k�I?C�|W$�8b�>hhhojػ,Ǘ�G�޲���]��.�fVoX$�LJKr8����~�]t7�-f��{�H���������g����s��O2*u��wS����A��i��s�T�M�X�h�!�0��X��倘}L��­+q��ފ��t�{�Q�+�_X�:\�}r�z�(�l4��Sֽ��{CV���H�!��Z��<�ma���B���oS��}\`$��%�=#!)��u:�I'{&���2N�����M���l������Ǣؤرy��[$r����As'�􀫳V1�i�nA�ԧ(����G�pS�7�C6_�����҅�-�k!������F�"�4���:��#\`�k���n~{�۫���}��>L�E�s(Oa�|9�����}�\q�����m��fN�<���g��aX	��<���K�9�w�/���@1ylEEؠ�9���l@/x�2�ZX�ԩ+������Aj��u�Li��} [���a�|J�*@+�@��ʌ���_�K
@�(����YN��k��%^�\����+�k����]�
W���5i�����eE�mT��������?߰�#6�8���;�x����Y��݀%B��;���}ϋ��q��:��c�e��0>,yy�"�u=�͠U�spt�_\��Q�D-��y<�c��_�h�|���b���t�W	&ݙ��I S��6�>�Lz�Y/�)^C�M.&#&{�3��9��C��Z	}�	[�<�����Y��Ȟ�t�g��*��U�B��ӫ�p(z�O�{S�$����m|OA��
�� M�>b&���x���J�4�5>t��`�u����(s|ن��g�ԏ���������x�s��O�ٻ(�V��Z�[��fs�������W�דּP�k��s���j��݊�+Ұ%�BE�rP��T�fk� ��
��w>wE���>iC��X|W�I�6�Q]I��_ܿ���tz����n�]�ҫ�¬;���b���kH�l��d?�8L�y����CGl6�ѥ��>;���2�/n��@�tI�Y�N�y��=h��&��|��ϒkcG���4g�%� �{W\-��d�c&�����&��������!{6��*��,}��S�:�ƣ7��u^��T���X��۸ȋg�w��Z@w���0,n26β�����s6��H�M�.(c߾N0�.����#���1�0[Jg�B�2]��ׂ��䆾y�B~�K�$�G�� �>��(�� �5l�b�w��#�_ ��X�0�/S�����b0d�D�U������9�윔Q9� �N���GMu% !�[���	��;�	n����LhA�������H7~��P
1˯�A�` g���ߵ�y���#դ��#o�A��8g=��ޓZ�0>E+�ԅ���a*�1D dV���h�C>n/���T�`�"����K�zJ��y�)��t-p�.�O�h�z��㿏�s}��b��8�=P�c���"h��=n��t	��G�;dË.�ۘ)��@>����1����15���������|ww2���ވ�>��C<�{#i��d%�l�As��'��x}ڝ\�:�fO��z�ꢋ���H����dܻ��N����H@r:K%�3����GMR���)}� ��b|�%���{>� )�8s�Lcd��.�e���Q��@,��:�n��պy@�D��q� ��t�S��2���sѝ���e�1z�'=Ֆ���.|X�'?��P5^���\�)����v��]z��iGj�L�À*}8_��.N��@����S��/~������˞���u=ߣg�߰��������������f!gtZQDYB�)�a9����}��?��p~���5�����×���at���RF�PvM{�|E�eK���D�	
��㟀x(��nA��l��������֘\��������o�]��Q9i�Y�Y��??����yocN	5}��xz�lch�L�3|G�S�1�+a����M!��T�?��F��<O�g��?%    ���.��%30HI6�}N�)}
R�f�N��M<�@��7����;�p��'��ӏ`t)�	�b-Y�'_tJ�1��5���	�yP9����Yw:}_�i<9E1bo�#���z�S	�?c���w,;�*�.O�W�o`ο@��©�)�"͸2Q��ŷ$�f����=}��m�mе���%; g��c#L)M	f�ώ����9�ٳ޴w� ~������G`&���#�3ߋ0����@�VH�>��+�,�V�;I��/���=��t5g{<X�90)Vkbz5�I��n5�+|�WE�*��a��K7w$ǻ�8���?�/``��� r�Š����ƻ#���ھם�R�<�̋OGSP.�o{h:*!_/�M`��v������@5�6���o���o�K�����L��Xk��;��\�Z�[��SĊ"��6$7�����ɀ!^�t��j:�#�=��1�z�X|��,�>0N��K��)�m `A�(v��0����3Vhvz�j�Ԗ,�:��t���9yF�d������J�[Z.+}�I͝2e¶���o䔠Y�( �}��T:V�d�۲ԿJ�y�1��ɖ������x�:�
̼;�)j��=	x � �6A[�=����2ƀ�?�!��O=�'@ [�^w;Wb?��4	����퀇��J��B�� M���]�e��ۤn���F�)���~ �4l����� 0>^	���z�]e��_��l?�i���C�ڌ����kr�)Q��ؤ����D���K�zkN\rFW�b�՗���΀��������@�P�{n
��X�%�"�ʿ�"*Ɯ#�]�S���۹��k��{��t�z�#6��f�/m���+�\N;�� �u}��a��OЀ`t^�z�`�jERS�-qə�0�|���p����e
�YPCi�2�<	ľ+�h��;Q�&J�H�Nr�eBI����G�<�VË٤�3�n�.�����Wsd"o<B-x-L�V��$p����.'>u�(��nI��L�ɡ�$/�M��yGl#�W b�*۰bWXD���[��Hޗ5j?
ek�[�~1��R�8����6�sx>��D��{"]Z��q�=�ٶZ/z��B���`���|���0Kp�bުx��@��.�y��a�r����o��fI9WX�^O��,������c��fC><DW�AI;6w��_A��(k�F���W�/�5��&v��b��6θ}������N$�V%F/��<��:�-qց��]_A;.淨j�[Z��F�L�S���k��.[{#���ʩ�e{\����8��o�vE��3~�`{�i�2��.�3��'h���$h�ח�m����v�N���z�N�6�F`�9��P���P�WwO�'�`��-S���l[�7��w����˅j�nm=�:��ǰƄ������:;�|^	F�8_�,2�l����6(q���*��Q�� 1�7�����K{��`z\K
E�-�����πQ?~O[���V��/E��1��シLÓ�xt�~ӝ�d ɧ��8c�v�6pD�Ȓ"��ܢ�n%�F'i]fg�чzQ�4���b���W�����8��U�f���� z�LC���.��\�lJ_e�K�>DWm3c�3����γ��6]�Lc�d�.��v��l۸�ԥKhS �gZ̩��Y/�X�r�~�BϮ�@�}t�_��ű$o�K��H��´b�R�]-ҍXc�(۱ LÐ�.I���+���B
��O/�]j�;� �ב�6s�td�`���HV�s�����Q�WS��x^U	 l;x��F�v�.O�G��=��[%J	r�=���ŤKIt�X���E�z�X�
Ӝ�|r[׳�7W�9��s�4Fi7�R�ڣ�����q:���c����#�j�W-��	���A�qB^	�A��	]���D��wI�T\�{Ʌ�j�ڝ�I��b�1]#(���Y���kz�J���d�9���ԉ�,��*Ъ"�v��!����W�ipr4{/����
8��[�����;6�=�u�l�Kh�4V9��r�X�ŧrZbǆob�U�2xk?��	
,��K�6O�[�`�Z��v4U���3U��nW�i$|�v�T5�����i"�]��hJ0ԖQ4�� <��x:���~w�����S�,n�@Z]���D0�A��m���,Qh�۞}�tsI��g���!�T�������Uj|Ϗ���R�"���\��{�4z�J�����f�R�f�A��>���{)�F())]���N(������}�@<��>�p����`��>�r�,����#�ֻ]4(V�<�U�#���J��*�[	�]~WU!w���2,RaGeTmy��s��_�Q�.Fg\,�S	���$Vty���(���Òd�W� ���~%��jA/��޴;<�NX7�(DD�����/����K��2����L��$�.�NqJV������ '1�vT	�!��!]԰̞XBŇ]�r�&��<��p$�˻�s������s�V������K�����|�>�l�f@yݖ��F����־[�W�%��~��,M>[��g"��z[e1j�t%�*)]����V�<�߂#�������KE�k�q0:+�4I?��yO�b2F�J�(�V�\�ʶqˤm��K�4>I��Y�C�!�28��TW^F��v�~%��'-9]TU�#�=����]W"�A�Ez[l-W�gW�ipҜ�%>t�y�&V�W��쒈�J��:�"�-�E`	f�E���ˤ�)Z��>I�����9������|d~�<�>�pBⲟB��4tty�n���.I�w��I��
�C �C����K�Ji�9�LÐ�)��ȳE���U�]���@\x΂���`�4Rty�N��W�I,���v�b:��"��� �;M� q�j�#�W}�.��2]Y�[
�qU�
~���s�"����I��X�)ԫp�v=K7l��*�(2�PV�)9���`���Ok�R'逵�ϳ�w%�� /h�Ȧ#�ȜǱ��޻���zcTߨ=n�k�0�N�[q�Z��E��+��]_x�,)�v�������;��m�*���x��n��m��h ��ya��Cd�AP�F���x�4x�3D�|�n�z��I���x�n��:*d7�$��"��6��c[9�I(=���y���3�M7��."���k p��fWI�����Qu�XR,��&�&w���J�U��)�*�0�Ȓ{p)%:�?'��[�b]pig�Z��:���z��mq.ոL#�%2[���L�K'wڞ������/q��{�l3�N�r�vm���\�Zn��Z�Ђ���_	���Zh�A��%�Y���
~{�{,�ca��9s2Ѿj%�$�U�譮��� ���P���X@�}�:a5t���]�����v��h�����=��댶4��J��F��g�@�J�t�H��=<�N/vaS9�.�{�K!!E��u��@�	�!V
���i�dhQ-��d�lk`����i��/��Z���C�5�h����%��A砆�3����X�	��i�I���\)w$�_cG�-�n��Tj���#_'��a��i�=j�"��������C��i@�]DQ��V��W��;v���N�wmL�n+G�qh;1�f�g�
{��* �`�mI�aˊvxzA�\Tĩ��Z�וU)��dH�D���z��V��o���"��vF��Ig�kpc��~�����n�]���DR�^����ƛ�QMA1���0�����=u�N+���'z����:�n�և��J0�X՟�zBO�f�%���/j�
�֩X���v��������J0��+�Re��EOz��n�b��~���L�*��G�"#L[��nX	�qz
'�a�y���.ͩ+�*(yw��5�Ԩ��
,���4�����P�s�
 �T��vlQ�S���qN��z�d�=U愿��tLp���R����r-��{�xsE���z��<<�ٌ�_�b����E�    R���H�*�4,[Y/�/��F�����a�9��_�|���W�d��`��J�a�z������TƲo`�nkv&q��V�@�ވ6�,��\1�7�gc�o�]�͢L���0T	9ؗ)���R܌���n���
r��xE�S�p�8����*[E�k���hB��f�Zp_�$�<�gO6#��Rg�'3=�`�2N�����T$������dOF�x��n�V�Y`̶��x ����TV�%sq�����>f*��i�)|����b�sp��=:�����5��(��:o����Q�y�`�2�6���٨3�;������}Gm�8ƚ_TwNH�c	� �p���C�},�99��1�!���@�BIՒx�{�	9	��k�������S��w�sP�B�g�����G��q{�Lr,U�_�?�{�3̼z.񊽶�+Щ��]Ч�Z������J�z�K�1��1���	���g��qY�R�T����Z��B��{��S�o�A��m��0��j	��sK�<�U�o����T��bƏ��c�Gf�C)(�WI���|���/�F ����`�:P�vF�w�9�x�e9���T�?�Pݹ��rl]�k��ي�x��$�����J0��j
*<���Ӌ�c,�6��vw���X�����T���tu��@^�; �J05PP�}w�TK6�
��9V�0E;�{�r�B.o)�Fc�9&����[
����%�=��2ҩ`�Q`@��D@�q�����nϩky�w=}��v[�aT��s",:��S�r=�����i�4W�9�z���UR�Gx�`��oNI�x��q�Q�vbc���eBT9Yɶ���r��Is�Ý�	I�3���>�+��`S�u4�\,�&��'��+<Ԁ�vsf1	�e| BSL�凡�6ϔ6�g��4��#=�dίЩ|J0M�-�;MFQ�0O�R�v���QA%G���� ��	6E��б�Z0E�!78E��L��:�ڨ޴`�"
nQP���)��_żW\�m�����XdK�k�[xHB)�����lx�����k�8��/��߽XI�"� �x�;�IF��鐘�nl���@�T��Wm�9.�]�W�q �`��)�Ƌ\��I�on�+�@<5{���S���B�e�ީ�V�,�Ԅ�&�Nj��f�0LS����Iؕ`��C^�R+*G��_R���%�����J0O�l��e��^��/1�7j�8�#���*��&%탎���CQ$�{	�v������Y���>b�Aw�_�R	̣ �������4��#_{����k|tE�����V0M���J0�Ch��@c�����n*8,�{N%�F�+R�7�9����ߌN��K�T�q,�
�T@��#_s��K�u���q<h�R0�F����C8�[ϫтq4������� ���[aha���Q�o0�z��^�,���n%������P\��jZ0Ex�����O7��Z0�����V^տ�kYQP	Ɵ_�z����hFn
���Bӗ�q���f^/R�br���8p��G�<��;Z0�,P�>h0���`�x<�+�82e������hpK�82E�С�X���U�
�(�4��W����v��>�2�A�q�ϯ,z�`�_��+�4X�W��ʢ���+}~e̓Fk�:�?T6;l�ٯ���e-�+}~����T�M��`
&%^�!�N�,Vt����mڞ���ci�اoeTL��<�Lb�����?|�w"� �X._��D*[��x����)����R�cJ��FZ	�az������2p|�x%��k��OҴ:��+��`�\'�� C2��@��8��`d�AF?]OZ�Q%Y
$Up��z2�,�Ղy��i�T )���U-���P������'�Y�`�&<�?Gx��+�yhU�y���D�$˩@�p-,_�� 5ǉ~*���P,�+�<H�q���� }˩� 5�~*��*�\�9N��qP�oՂy���D?�q\�WDN
�A:��8��Sq��0@m!�g�h�>j�s[|W�k��E�*�E�`�C�l�U?S�eK'p�J0�q��S1��1��t��x:�����*5�Nl�����i��o��PY�NT	�G��5�m<7�B�\)�hР���О�w�_'�8M������"OK�Uo�����a&o�!�+� ΈpF
���/�9���{^�:nT	�Q�C��=6����� ����mr�'r������J0��V8wO|}!Κ�C�܊����s:�ǡ����1��+��A�d��u�Ήg�'�>�`��`�?�Sf0��Xة�C�
��Ф&%�؁���J�8<O��-�򃦣�n��H@���H@i��Ÿ�����j7��q����8��2�A�lk�W�y���?��Tp �kك!�)�b�HҢʈ�@<o'͐�.�W�Q�����E'���
���8�qy �kDa%�����#���[�GXf�t*�q���f3-��no�j��jt9�ir%֛�j���xLZ4��<��`��;Ώ�;���G���gU�ql��8���t&N���=;�d������ y%�����i�fP=�I��`��yVP	Ap(/�f��x���4���`%qN{��`Q�5&@��Q�e� �̅<�É\�L��` 5� �����e�~G��w̖3�Pl�!1R�E��(2��Cp��b_�\�{+�d ��,+�J0�+(i��7�4�x�y%��)M������r屽J0��U(�V���,�[c��˧W6��dׯ
�cW);��@n(s���W	��([�T�kg@�|�$	-G*M%��GʃA<ߍ���PV��x���ǹ���N�pe��JwM�O���ª�:�W	�(��T��Π��mK$j�8e���r��oS:E�s��*�8e#x��8��h�����g��;Ш�`�s�±�2����nC�P�:�+��r|@�<Z�n��\��,����O�>�Oj�ҔkQ���?k5Eԁ.�I j�ͶC+p�J0��M�-=�C������e����γ,���Уg��ymOPu��w�|9-D��@�����`}��mhw))���򗀭��x�ǝ�}�U����7K�4z�R�F���l�d���2 �,\����`
%{��ʀ*b��4U9�8�~�C�p?^`~%�T�!AsT/5X���oj~:�����@4��A�Y
r��� h�8 � 4�('�y�(�NO�:V%G�)$M��$���|ve����U�>�c�����hZ�����a]�;��o��=z�P=t�O�3�,�[<N�4�D)>u�h�K�1݆4��3<�ë@B5�@j��G�~�wEJ!,tO��j�8[�hp��X�숙��'7�� ��X�m6�e̴z~yz��{~%��*8M���,�}��Y
��x
N��+`x���Fo<4�`��������T�������Qf����b��#O���̉�<�_
�a)C�To�c�ڤ�Snz³O�J0L�Η���s6Fh�S��m&���`Z��iS����:�C�ҕ�8���|6�����d�oU	��*�8
eC�
�
���2��^��m���Ä��x�Z�j�!����<��R���t�����[��5Ny����I4`S��Ar>j�R0C���Zy[00�$�|���P\��d�����~�-�2"4�-��hh.�D��XܽW&P��n�<��u=;�̃f��y��F�҇*�tPȥ�'cgɧ�v<k=^��#b6��-2\�û��[�Emg����D�������[���T�3ɛ��mg�Vg�b�͏X!V��M�|,
S
&��lB�h�/9zj���J�#6X��ʘ�`7o��6.ٖ�yp���}����Ҳ��n$g�A��i;�9����Ht\�����R0�Օ����K��:�A<�y鹸-� �2\W7�Vʭ�sۯ��5�M(��^�NP���ExvJ)��    ���/���0�))�FМ�^v��'��0��*U0�m��t�R0$�@X���1�"��L��=:�C1ju��G�D&j 2�#�v<�Â4Z0Ds��)�~H�.��R0D󒨁�l��WC��'�)�cFh�5����̻��&ϳ�r���Z0�G���D��':6ֱ"/�`�6jQ��Ou}�by����(Ӊ�Z0Dӌ��f�Z�crh#5�ݷH����T�RVε^l�^Ƀ;��_l�0d�x���K�<W�x�M��7��T�`�0lZա���N%��A��Ph^�&q�s��S���_�����*�C+rBn3�]�e�w̃4Ȧ��d���$�3�_G?�B���>��i;by��Ur#˞9.G��(�k�ӻ��kx��7���f�h�1�ıܤX
�qP;�a7x�g�����3��������tDN9w.�Uv+6� @]P	��8�L�H�lLpq��g�,˯��+��<PWn/p�r��`s0�GlNa��X��-�WZ>U�Ђ�.U�#\lA.#w��T�Ʌ�&+�K�<O#i �lk���%�A&/F~��`L�����
LG���RQ�̃
5�&�[�#�aNs��[!�4�&뙬6�է��a�r��V%�B5���:t��8��37��֏�`@��F&a��R0�� ̦q����2����^%���j,�Qay%È�n:��u�~O?v���z����UJ[nߏ*�<_�i�K4L���;��L�`2*s��釕`B�!4e	�bE��^	�1�Y�[ncٳ�S��i��Z���ۖ_	&������Bj,~���!L�#�Զ~��\�``h)r�fAp4���)��wT���B��Z0����N ��=�
+�<��4X�p���VX	��N��=�5�\Vs�X_����:��H��)�b~s�V�
SQ��7*>b1�R0�C+ڦ�T�c���S*5ΧS	�ahu�T�j&�X���þ�N"� ��m�P��2��Y�U*oP�r�5z���œy�`��Q4�B��!� �m{a`1�9Ǝ��e���5�E%0�yZVM2#�0���t���M�:y���=h�W���ڼ��6X;Bs7[/j��y4�v��K�M�r�כTZE\�-��q\���^S��'��F�ئ�����Ӫ�Hoq;5"<5�" G�Ӵ�k�%{�	0tǓu�`�;\O����[�#Xt�Ff8فgW�yd��4U�:��5L��d�兹fi�I����aEA%��yNc�.�6���0���^�Ck��T�K=4.(�ȗ�y��4U� �A@;�`�f'M��dw���ZK��k��T�J��+����5sj**�����0�J0D�즪\e�3�b�4��� h��T�k/,����T�y@�B�M9F;NeK��+�<m���
�s����Cж���ث�R��!N��^�Cܵ�(�*�$��P�E�b�g;a�T�y�F=A�]o0]��V�2]ϑ^�(�l��*�8*����׋zB.��̣�5
�����w�<y-�G�h΋Q ���]+J0���(�����v%�G�+q/���-@��`��QxOQ`�wU�5*&_?���0m��sk��@����&9x�I���3��`�6��S���9�B��V%Gj����C���y�(�/6ʯb&���/6�~hc���̣�f8�f8𣈒<�`w�Te<]��s̉],��1D�@�������2u��Ӎ�ڔ5U[R�J`��c��\y0ھ5U\z�{��S)����`���/���F����Ob)���������.6�u�ՄI!<�B����H��rwK ��/���ǿ�1G����'/r�J0	�#8����`8�9>ؙ���!���R��Xo�E��X�z��
��ѷ�Ir��<񎂄+P�a,a�uc�X�iF���CyN��?�U���e^1�(h�hk����A�%�V����
h��p�� p��\)MS�ث��͊|����m�'/��Be��g�e�G�������mϾ�7=���;�ͱ����N g�ϯ�c+�a$��O����h���!6�`|L�
.�~R,�	��wi.�:/�W��sBK����g�T,>���3|�G��VJ$�:���7��j�fb�I�
������i6��?��N��f���g]' �����Oy.nD����@���Xb�Fs�m�M�& F�6�:s���փ����d�C�V����n��Y�U�q��H���Qg4��;�z@�p�MV�w��8?����3e��l�1K��
V��״`�LZ�4�E;hs��[�e�Ǡ���k�ͧ���� X��6h����y� ����ڠ�thN��h�r,����m��`��6t��p���`ՃUƎ߸G��u�)�n�1��|u�Z�o��8N�[.Q�1�5Θ`�;����JO\Ǉ.�����|����u*�8BW!�@��5�r�=�U��4���_5F�f7�}��h�8$� 5�퍰���̳,�7��h�`��q���5i���F��I-�=��;�[UI�i��^��t�t�	�B/\���Q��J0��WH�CH�0��
L�����yۮ����	��`��)�>�]�V��܃��J0�+T��C��0ݮ����*E})�w;��kd�؅0�ӨlE��YT�I�H�H��k���/�_W����ojr	5�T�\N��T�qd��S�3t'��"cӹ�7;j�v�h�)�h��vJҡs�W�E����i��̣CN���z0+z��M��#U��oX:ܔ*���L寡;����������q��9ٶ9�M�#@�g���ToL{k譐��33H�9�[��/aj����A^Q%�CYZ�>h��u��.8cs�PA�!�ro�bq�<[����=���~X���O�J�!���>���`&7���r�g���������Ez�!%7����l!
qo܉߰��V@�*P�[�Y�k�v�)5��Ѿ� �veG����-�<hWeA�A��$W;�~��F�1D
Ct�Y��=�H�lyL�L#��l��kB0��b(�63/qYD��
�{���5N�ȷx%����M�)�=��-�y���9m�w�J0���9�L�~z�?��_���W���sPs�kOa�c�`�`:Ϟ�zZ�9�=�.��\�s��8���]	Ƒ�T�m�R�zj�p'Э�_�q��|���K�8]N���u�c��Е�iJ�<U����}egL�%.,���aJW�I<!��4����B��I���̘���H/pn;�`��A�\�b	��s��"=�H��ۭ�Hp��^�?:�W� &i���!�u��P8!����sy.�k�>�*���S�0x~~�~ϊMA#	\_+t*�������=����K<5VV�Gǡ��p����f��Icۡz^%G�*�|����h2�vy%G�e�����Z޸X�yʒ�w.����t<�M�_�0���wM'Hq��� ރE�-���=�V��`2�'��a2���0V��g?�N�=ZCߞ-�B
��D�A�X�9�����B<���H��c��[��;h&Mg��:0�b�Ƕ�c4���Ar���ؼ̃p4��֑Lc/�+� Α���y�Fq0JX9X��dkJ��JqW�Ph�F�Bw���o��V�U�J�̣�4���"�-�)��h+d�v%���-�wВOŝ�I�p)Y��5�R0E�r�)�G���)°�X �Ԃyڪ{V{���a7�	�����k�q����}�X�[�i/��o��I������Ƃ��ƃ�K�����J���@l0U��� �H�_?'�:�0ۮV7�B�j�Oe�o'F��쉏�L�a�\Q*l5׸t�O����,��b~�10�w�@"/�6�D�პ�mK�x��)�0B[�=n�Me�;��q��?O��Z��~�R�RP ���r ��:-�ZHk8�e��T��jٰ�o�T�I�L��X�;5�n�Ń�m��|����׍v�-`   ���Y\��<�\���c���03ܡ�-��gP;�S���72y�d�)��(h4#�Da����W)z���V���U�֧C*n�5{@��adU�qX\��۰X||ҋ�_�X
=���w�r ��]L��8����r����=��S��m5ʤ���d��k�M�^��NM�����{��V���������W��o��Xf믷���{!�����3᩾ә�mRl�Qe�7,���g���@V>H6XQ8�?�i����\z���]�O���#�!/�4PB�STn���!n�8���F�@NT+���U����a��:ɮ���o����@�+c�2��#/
B0�`����[��w�f�{ʚ���-v�l��n�ƺ-;�Ww��w=��F��@�\E�6C���\9Tyb��=��sk��LJ�9�X��D���˓�u�z��ܕ:Ne,��B��קj���7�j��nQ��\�ڎ&ܴ��6�H��y�h���^,<-��-� ���(���9��R�T0k��p\�(����i�rZR�]<��̣�5����a�%<�ض�Gi�<J��Z<8��D-����0�C�J��̰�J�����&��Hruh�Qa%�^�CEŰ�� >9�N�
�H�%ɧ��j��n'��J0̪\b����.� 0<����t�H��z��g���e3������0�2!�y�%���q��L�Jy�X����E��w�;�y,wq`~V��b��L�ژ�'�A�ߝ������PsF��q���gj��=�S�"h��@zlm��Y�3w7U�=��J���h�i{�l�����X}m$�}�f�M�\�8a�v^,"y ��RΆY�����WP	FA{_9�������:t���`�0Y,$0(yZk��z��!�^E$�<+��A�X��ro�C)1�N<ٱ���4�<ۈ#��zV,��:a`��-���<!�e7'��H7t����]	�$�k�3�Zv�qr|,^����J�K�q�����3Xfyr�-`��K�hr�RW
����܆��$�m�� ��/��}験���܆e��m'�ڡL~�F��>��m���S�%�<苀�k�Y`��R��	�j���vvO��Y�z��Y����'�JC\t&]�@�������������鍆��~u�^�[�$xc���
Z���I|ҝ��h���A�>�E��4�΀H ��٬{�;MY<���{�%�~�!fo'���z;��G k�k��¤F��?��jN�e���}�z�з-�l��f�j	48����k ��W�wb� �:�Z�Uz �Tnd�c���$fqC�b��n�d4i}@>ׇ����)%�G!&�i��!/��"�'�����o�-�2�a�k�[=�����,��XE����5�!�����,�*X9�mpm���0_�'b�>6�~Z&s�U-`"cI�MqMթ�x����N��5�hK�;Mg���c6�8��9�l����1��E��¨��`?��1ߓ.��Sց�ӥ�޳s�����T�5�.�n}<2^Y��]L���;��iy����i$w#�Goc�r^��ŷ	�a���&�p���z�X|�0�:-���&��,ڥX����t�<���.���`^g�/�]���G�uf�^<�k���8N��<[�$�F��z�����BR��	W���O��B��r���V�a��vZ\�F'0�N��n��	}YVT�qtE�ݕ\h��Q<�J0�6_�-�t���d>�9���l���s̄���^�D��0==ƝJ0O�o��'��vm�@�\>�!�q�K�d�J��e�>\�& �&����ȸn/��=s��:Q@�A�����J��"�_�+�a�B�/�]�7�����g�?�FG;�������7F�Mn_w�;��o���Yg�M�5�r�v'q�1�?196��.���H��:�\��[I��I|N���"7ˏj`�I����#��0��������Ϡo��j��O��A	�������|�b�Z=n�D*c7;���^���H���p����/��d`��@��l�ȟt��-9����9�I�{�9�O�ٻ�K����R+�*�l���P�/;��<KM8:(BQs 6S�� ΀3^tz1V��Z�x���e<929�O/���[Goc��1���^�f�W	��u'��9��:��D4�p��BF�`�Qў�d�%��x��x������?��߳�Ė�5�K������8>!i8��r�CǇ���;!��.�����Zp�7�������yY�����n�+��uUc�FB ޲)eC��D�!%Odxo��%��оe���A��~G�J��KF���/��%;�����|k}g7���
y���T�/T�}�&�怔�൜�sRߊź�i�:QP	P)v[��w�`�Zн��!�)(�J0�ߊTqId��z��ĸ�����Z]�'���<���f �
b��R��m��F'�8�����Rp{�N�p]]\��-ڢ��M��X��{RDb��S�'��Gy)�n:���^�m+���vE�1���]�?�e>K!P=4��:�7��/=;�@�ޛ�;��.���Zf���ax������)ķ^��G�K����c5k}+Hoo���Q��$����Q*�F�iX�#�P�*<��L����^����q���nac:���k��!�U�f:�:�qy�RF�����},�/�3�������CA��������e����q��P��$�+���tҎ�zX�D��j+app�?p�O�b��y�s$�Z0�@m ��Rq���fт�%#��W�O�	���<��r� ���P9��Q��}ܹ蜍h��Ӟ-�<�[��UC�d�����b�}���wy�i�S�Wb��E�ܷ�	8�=�?��S���S�_}�^��K��7n���q|G-%z���V������&[�O�V�N�I:�-��Y�_-�b�v' O❆���k�����-�۬<��[�[�0ݬ^�Y���Z�`�Q5{�Q����0,7�u�-K�+�;���m����-)���Z������>�w��N�E�h�p�6�ǿq��e�aX�D�e��G�5��8����0 ���{�nm�+��o�ێc�̡n3�vZ��:z��-iZ����Geڡ�t�|{L���q�8`�'݁��-�#���,����N��G��[h��pc������|�/N����Qwڏ/G-ljf����<zc������o,��9�놤<�{������ �/;      b   �  x����r� ���z8� ��L��ɤ���!q5��.���H�,���rV���;��K�GW�y�/cC>���O����u����ퟖNZԆ���������B�K�V��������oM�K���
����g��4D��:]����O��tD��q�j�"4I�<~�kO�9<��� �\j�����,����)��s<��P��@�!ͱd̒�'x��)|��Ba�'�uh���q@��ӄ����Ņ�r��aپ��w��U7�)B_���ƣ�aY����h���s�k<��1�qs��T�bR�mٴ��3�Z�e���Xf���z�[���w���g�( 2,�VJ*ѽ#XrYA4G��Ѝ��n�W̦�I��1��P�w�.�ܷ9���4�)s�R� &����^�wL�43�j�R��;iw�N���
l�9�C����z����q��^�2���JC���t��M���8PHz��vp��{cl�5�M1�U�
���j��~`a\�kKe<e`$pME:Tӆm��፝���.3a��FJ�"��4�U�]�θ��5��g_�����|�϶W]�6�&]!�~���7Rd>��;��������K�a
����F��f��:�2��ZhC3J�?Yّ�      d     x��ٍ1�o2��P�rq�ql� C��n�슊���Gg1XgL��uǎ�'�=�^#�åoJ@W"�bNcR��u�qɤ�#�Pl�b7��8E��1"�9�z�n������L�ΡX�y �/Ń�7 |"��`Z��"���E+�g�;g� ��� �Q�J�X�ׂ�� �l`� y��������M!�x�7��d	�J��8l�i2.��:?���$�&�7	�w�p~*��<���K���-�W��>v�q���r�{-����-��.�\k      f   �  x��VKr�6]C��4���K�"8��ʮ#ər�����#�誖��F^��Z�e_��K�"�C�J7Z��Z��F�	�k�<�-?�a -���+P+�����<G:��랧}���⚑i��y�"�9���s�V��A_�?���me�����I��}�pF�����8`��}�q'`�n���ҏ������ `���q��}}N��K�쨳9��;]�s?R����d;b�0��>N��vk���?/�3��V(JF���rs�^��
�>����K��+� �f$��������Q�>ȁ���>8e��F��[^���fM�Q�U�%-�r�4ЎG���w��?��ΚZ��Q�����U�����#ִ�Jc����1���
ֶD��o��F��!�P���iC�\l�o���#8��%�`�;9h
A�:���X�V�b�����e�А}�CNF*��
���b{-Hd�cW��$�3*rt	U��:�&�h�����1�֒�1:��`֌ÔW��,�k?���{�<� ��`�ߨ�瓶UI꤬�'�_YjXM�h"��_��,�
7^���g��M�@p��U����x���'ڵ͊x �C|�r�2����]������:�sY�Ry���R;cʅ�����ddl��)�¨�4�Sk�=�D&1�R|��9W����J�@�bI�g�CU ^�`�m��;�䟇u�T�NKMF�o�5��r0
"�9��Pt?$�ӹ�A��"�a_߮��5���y�LL��A��T�-�FK�uP�%Ʈ�'�\���Pc0bcn�?__J���jU�^'K�D�r[@�#�5�Ұ2�߆��VA��01�r��S�,"���`SL��tu��Y������5�!'�����b&����O�x��8���J�ې��?��t�0�,�      �   r   x�m�1�0 g�^�N�lE,���*��/�L����  ��c��i���e��=i��n�Ys4iQЎf��_�D>������w�1۲��"7��]$�5�*�3"~d�	      h      x���Mn�0���)t��~,�;�&4�#[&�1z���+5�&7J /?>���rɉ�`J*�$�'	�h�(�� ЫN��c����D����"����h�K����ǘc�������q�(v�<5��I.��"e¸lG�����ə��T2^
5���k#��J�9�����7J��yjت�q�[�s���H�\��5ǂ�{	��^2�3�VJ����R��s�a�BV;�Z)~�~TVXc�0Y�f���g��q�E����3^X-�{}���VYg	��]��|JZ-�0P��k�t�S��GX�'����y����-�u���� t�s�nȱ��U@��/5�\��Ϝ"�t(�n *.+�5����kX���Ϳ[5�87���K*y�y-2�b�S�뭦:�o�9��V�GZ���3@6�aS.o�����@�9�T���ЦH���w#����笯XO��QzVx�H��A��ܺ�)�g���L�~�Ka��^ʭ�y\�O ��K�]�|�]t]���m�      �      x������ � �      j      x������ � �      l      x�ܽIs#ɕ.���a��ڌI�<�JAI���@�H�f� IF�` J��j���������(3�Ӯ��r�=��x0��:�Vy�����8�aV�u��Vټ�F�*�:�ǵ]����ߎcێӱ;�݃rm���#�3���ZNl=Yݬ��M^e�y�.n��#'�q�.V������}V,��}����>�g��4˱��O���#�,q���1�/?dַ�j�d��lUXi���*Z����-���`��rm϶�զ��G���5�V��|Y<�ً}/�}E`�������&�*nK�d�U�����J�=����Qw��2�N�|���3l3ξ� t��D�mG~(� q >�Ui�㻴γ�sau�J�?֠�5].2B��Zb�-k����>��9���ߋ=E �鳼\� �z{S䛌�W�P�-��v��-�y��5�Ո�=ϫ�a[��>�KphuB۷�6Zp#x�\2k�-�ִ\���l�j�-J�#��n��?s��/�}�����:�sl���Ί\�u���~f >�Ñ�����.���Ȭ��2h��Ҳ��/aV���_���n$���l	/캬��ƞ��^��	<��b���n���?���eI��g��6*:�N��t;��g[�n��E��~b�����$�����!,��|��W�"�i�|�W x�w90m�$��y��>W���>��>�Y�l݉��q�8R�&Ρ>�"��x�4'���~��yFa����o?�D�n�(R|'p� �;Gܒ����v-���/{P��,��R��w#`(1���`��'�D�������r�r�\Z����?�g\K����;Bf⃠Ib�Gp��g���d<<�Ӽ��k"��|,d�Q���%���5��w�0rC׍}E Fv���\��|�!c~*?,7�'�U�v`�dOLb�^�!�;����<�Η���):���8�����S��$��掶Y������M���Ew�c������a6U��ͫS"��?���=�?���:��ui-�Zܼ�O���~��E ��-��Z�%�(j�G����F��B2Kv��'r�u��dM��|I
�E�"��ç�J��L�jչ�P��^~�;� �����W��d\��/Q>�+�hb�['`��OyL��>6X���m@�|]%{B۠�SeA[X-4�s��h���1��p������vje�!˄�⻊0 �⃫b��CƟ.��������	������/p��ں�s�ᣮue���JrY�d��m��S�i
�{�����������u�˭�6_ ��T�T7�)��%�xx�>P\u������|`��@�����^�����PN����9�Ɂq�p���L��u�)���x���T\t����}<s[��(�]E@F��>[���l.",Cb!��, �j�}Ⱦ�����+��E��:Q��m;��>6XC��2�St^�Z�.��>t�}�YFS`�mWŊ�XPW�\?�9V�Έ���eV��<��X�]~�R�ͮ#~����!߃͡�t"0�@��L�q[�b��X��<i�� 9������
3�[8���\䪉A
a 2��@�.��)��L�N�vWX��O`oJ��%��:��������5��%���ӗ����,�Sڜ0����3�^��0@�Ǹ-~phM
x�C<�+͞�B~N��蓻��ǻ9n(�N��P�݋��V�s�x��Ʈ^+yޢ�p��@�Ɠj�A���f�$���� �G؉-s�iR�A?�j�ժb���S6����u��X�h������;� �<! ��}l��Ty��\�*��T�!���ʹ�f�"8z�B�U�,���o��@��ctv����͑eѺ̪[t �+��(�����V���U�\�y^���Xh�nמ�����%���DE!�Vh�}���ܬ��n˝��	ܦ���
T�9X�`PgxD�m$�<vI )&��p���1o� �B��q��i,s�h֟�����@;�/���S�������/
E�I��V�M'�y�:�ҫtUv�ƌ�ɏ~���{�w��s��D���*3ÇǇ�^�P��7�x���U��?\Y餟N��\���=����5ng���.��ǒ>Ҁ�8q�vpI��t٬�����!N�����=V��Ib �s��o�^�����A��?���7��X��lҳ�u>Hg)^�1<Ƃo��_<��g�h��,N���C�{U�|hÖ��堘�MF��xl]�t2�Y�>@�lxS�9�WL��BṂ�����S��C=M��$��{�K��$��V:���D��WJ�!;����KUX�P%N-R�YX'v����.*�œ������H�����pw�ay�c@�=��g�������@��U�Ϸ�ᥫ����t�$����e�E��o_1�� /F7����X'�~o2�]�NT��2V{���t<0��sR;�}d���ӏqw����hte��+p�az5�@�t��7�����0~S�p}ߕ�t8ah�~np1�i�Op)��
A�p5�L0�K�[Aʢ��^������#�`x�*)�䀃	�4L��]Y#`����1���0���I�~�(��sP��5���7(&�h�t���f�	`�B02���;�U��]�Sf|J8$�$D��$�i�]��M��O�+
qRgv���S�W��)����U	:ԁ�,A}��,<Oo���x'��V�7�ۍhO@�]�O��糍�� 6G�������Y�����0�М��i�R;y �m��D�0��2�h�z��S8�K��h3]�$�q 
��()R�c@��U0`W�%b,^��*���1i=C��9A�
���#-|�������У`��Ɗ0��wvS��,�@���1��`/�苦	���j�Ur�è��:��ڪ����a#&њZE`l�.R��\�O�EOJ�MrH�Z\h\A��:�j9�WK��X�0��=^�A>�+V�τ3�)� 3J�ˢ�-0�C��;t��8ϲ�)���D���'���ߥV��^��5(ؽ�L�$�]��<�mE�Gث��2t���s�}Q��V⽇��[��[V�e
���tTJ�}@����r��ui��^O��kK(���3yf��vm񠽮��3��D���>Ig),{��Q�Wڲ-bw�W�2�p���}̡�:�>�&�T��8�`�F6fU�� <���"��|SRZ���J�4o��%�D�e )}�5�0 �@D�\�7�j�J���m�lM�{�s:t%FvL@�b�X�K�����	9���?H��s\餶�(n��`A(=���@	D�&�E�C��������vU��,��´X��6e9n䳎lO�}:��XW�9z	�M5@�PE�νN�_T��m�T�yDS�PHa ��2�#�s4 w�0��0�c)H�+&�m�	a ,��#Ԓ�z�]��`�K��k�
� ��飼he{�G���0�C��bP�RL��X�]����僖
a�OQ���ЅN�p(-��
��/V���ɣpMAyā����dx��B�P����ǻ>��S�M�a6���,���b�ڠ�[V�I↶�#_�M~	��f��֞�n��6�<jڅ*�F��9�Y�)��Kƀ�_�$'8a �U)�'=�d ^~�t�2��?�w߀��bQS���α\x��0@�Ҭw����^���5�+���Qd�#]<�D�k���)�pI�d�.�	�����X�d���� ���݌t|����I/T�f�a�$.��'�V������M�'�@�w�MցEA�(� '

���,N���1;JUZQ�ֺq,��:���U��?@5��4��.�9�l7j����]l2��,�,����#d�A���j��S~(�� �ۏ��A��5VǷ�%�C��?|Q�5�ߎy��2����pNl7U<L
+    (���>�a ���xB�_3�6�skh,5�
�pE(I���jY-2<qO[Ĉ�<��H� #2�P-�$�����
�L�+�A*��Nz���dvOQ�'�?S�U�I�� �G�JeN�����Ȉ@����$�%j*s�,�wbn]�o�IE<3� 'r�o��jE�;��˩��5>��O@�/��-�b�]蠞S��� |E�E�>��A�؎���#\�l�� ��]W#��p�0�5���Z��q 7=q(�G�w�"-��%���S�J0c���V�t��ݻ o!s����c��c�
L����+5��K˧K���`�#]����<[ОkִS�Дf��d�f뉜�v����m/|�kA��D�N2�V}W���ɐ��̾do��S88"�z�S��H���ʖ���3"�9%��_�1�pZΙ��9$�p8-P���r���6��'��(� #M7`pc>rE՟��bkb�����u��khy�Ex�m�g���H-�����G8���-�9*r{k �r9�,@=�>�Z���=7���b�a-���cRh����ea*�`-.mL7<mM�N\��0�Z|�yC�m�i�O�)ֻ�N�k�v'����a��*�rx����P	�ja6��c��?%^M��~{8������ča����K8�0@+3�Z�\B[H�rH��l�m���ݭn�ǀ۞�JV��{��V!	��r����zM�E*���M�orv2��Ps~a ŉ(	aԟ]��y:�^���x0[����{�S�	U��y�Ve������[�R�9�=�V;+�_�c�A����4��n`ʄ��.r���q<�-�&8q�lPC}E͐h���a��.��v�h��7x,�+Ψ��ʺ��A����<Ht��G��I��KV��6Ѷ��D_Q!�$QW�#;���.β�^��:q��VG��C���dnV��uU.y�1�d����u=\ R^��/D iZ)E�xI{k]��}8Ě�Cnd��5�>�����I� Q&�I�C�nOYApΟ��^�?�l�*�t;�Vk��\��@% �!q}������M���oy�>�����9ki��-cv�n�ͮ7_B!aW3�ċ��,���0��G��
�6x;�b��7�!}Ԓ�3Ns��X��\ac�F���Pd���0Y�'�H�5ݖ;Jj� �I�ۺ|��U�'ٺ\l�7{�m�ݺ��a��U�L�+���ȁ��"�v�A�[�2L-k/�P4d�x�^M�^��\Cx�$b;�o��n�o���
��S��u�hT���%#���$�5e�f]l�(��ƪ�P��Ʒb�X�h۬�fw�eD�n��S�>3Y���-�l�k���}�����d�+�ۯ���@sX��P���.2yScmA�(��W�޽B޲,d�$Ej�̷֥-�B@�\�6��]��V�	b|�v���QI�WMɈ�z[m
d���Wк��s�.���yL��N�"��ͯ�3x?���f>�|Zo�C�k� �}.J�E�����&+�(���<g�35�J`����P����p��00I�� ���u�?0,�3z�ܣ�}�̾�xrMy8O�^������2a�4�1�zI+lD��F�Y5�&g�}�sP�����ԲEU,�Y$��D����̱�~h?�^�t�-��R���������7Uq�<S�Kz��M�/�DA�#�C[8���ch�M�\�by��Eg�'+c��=蓀�ƾ@�"L`�*��r�6�Y�D�\�w����Cs/#d���������6�9���Ò0\�(��X��c!D!�C�`s���n0��t�$}l~%�T*�ӂ�ۚ����"����;ԯ��-�Ϧy:�f]ܸQ|�J��Z	��U�	D�� JФ�A�sy)V�A(�.רQÃc�˝�c�c~d+�a�U�S��`�zX��{���w�{�����Q+:,x�0A��M��K��������j�����%m�����������o���ΛrE��ȑ�	`�l��l�9D�"=P�W�����x�;i�b]\F��/1�&�bv�V�|cy:f7�ɟ��d�
)�2V~Q���T���0�(�f�j��J�G((�]b#�/�L�(�VA�tu��m�o�O %[_	PG.2П<E� ���1Ǜ�����l�dMO�L��I�`_�a�0~a���ז�vC%�<�����A*S2�y�X2C�u)�-a���]���B��ѧT:;=�j�����U�>`��>2���4L��aA���o��Օ�����gM�R;�CE�����e�ٮ���9��vNȻ�r���,SŎ�X&8��L���Ӕ@�]�=֠�=b7�a�.��bU�`05R�	<��T�-�1�f�/��g�h��uh#�d�"/8��bV&�(j��P�y�*�ҋ!���V�A����	&Z,S�+K�1�]��T!/%����)3�������oW<]E���I���n�[}�!�&��Id���a��ͤ����%� M�8vE���_}Lr��~ ��Y�P��ؑ��O,-o��b4�mB�^I�Ra�W n����Xz'�ĥ���M�P�X��4�P/�8`�vnd�V�'|�g��:���zaz̩1�{.n,�3_d�a{�ΐjG��|�3|E��a!L��i� k��9b��l�"�X��6af�$L �`9~X�͡�N��"�RJ|E� 	�����0�.��`G��0���I��f���0����=�IJ]rX��ڿ_�����=�e��9���
s#[�%BP���5���Q1���A�q����,Vԝ���[,[9̪��\�n��D���(� �m�fŊ~�
ґ�A�k�J�y��q�-�%a�%�����U<Ǡ�f�d���|>�c��Di�v�8�h�	@��+V�2vn��<��P��\f����#�0|E� E�fʏ_m����P{K�B�k��4ƴ|A� �>N�l��� ��r��d��d#� R7P�	"�H�8�F�7�J�g1h�;aO��K`܃3�f�0�7tz�Q�E�`$E Ӱ�����qd���.'����M.�gP��o��>�������
����)�F>ꏏʱ�{�X�~�*����|�	3��K���`��	�$u�i7ԉ.��W����0=��j�D����`O�t���~�M��\E��կ���^�Z�:<����`�*gyu��v�0
\7�^Z�0A��<Crì���<]MבۑMt�@�w��
��G{��Ď)\!�1=�1�6T��ql��S&�X?�BɆ3�S��%��ph?��x� ��ay�0�$��`v�{{k0��<��p��$L0�W�;Q}&)�mj�)��31�؏<E���:"_�&�t�k=זe���xʆ��	�����0��LpL~z F�3heY�`���v����c�I� �aZL/d~}�X�LT��n�@m~�b���?�a�.R��x)b�x�N��+��$~�(���aY�YS#��s~�����U&�X���"V��-�*��S��i�Ǣ�47TW"��M������H3�40��C%��ڑ��T�y�|}�'
�"���Ok��Png�MC�R¯��r��?).׈4@���6�Aĩ��v�~��á�f���"�~�G�M�Um�N����*�e�Q��G��q�c�����afFq[Oi8_��)s�w(,�#���a�\���bJ�Bt�A�-6i S�U�	4���}��1J�)[�5#pd����s�����&<���	�}?Oz�nl{Ǔt4����uFl�>�cm�u��xI?��Ͷ�K���M ��1o������؅ L^����O"�,q�Zb�x��^�+�	�}��Hj�Ķ��6�{�L�r�Iy�W�f��I����8�Jw�x|+��&�0C=!Y���7��ip����z��,��Yۥ&g�@It�bG6�F@��r5����0�uKb�%J���$L���-*�~���j��
�h�
�
����#�hи����+mE���:lR�:�T�s�a.��{I�C+    a��F�ny^	뿮im�$�>�|�$��	J�_o�m��6�73˰a��>���&֠Z&X���:'��o6O�=5�L���\Lc�a�)��4~R,+]T��_�+��d��r~*�UnX�H���$^W&���0�B�lN�N*����Z3��I4������}F��K���e~�?����q�}vk�Z\*��Fz��s j��^��Fn� ��RiJ�E��g�k�sV�= ^��0�O��`����ݝb�x�n�=&f���k��D&���Ί�mņ7T�s{W��r�8�A��l�Xsi�
�ew�y��rn��L�]Q��f���d�c��ʠ8����s���5ҕ�Cm�3�QLF���:*��<�<��_{�9?�V/?�a3g�Daa�$�1L&ȐA�*���E"��`�	�(��Qo��5���L�w�|�CE�@�n^j�Kн@|��R>W��N5�A�x��(� ���Ѵ=�fB��m�+�we��9�]L��}z��T9+��bX��h��� 8����6v*�����PG�*��	 ��5V�g�r�yQ����*v����Q��!L+�sR � `!XF���tw�L���Y7;�k�J��q�}k�$a��<�>��]h���,T$��1�`�N�4A��mY.�[�f=*kc*���������[���3��û���؂\;�R̠%�H���*.	@���a~@��ʚ��4,y�M�����|N�^���J��T���JAq��s<�d��̝k���N3tȒ��.*�w��;&��Z�i^����0n3P��y�)��x�yC	$Yk{w'���P�&
�2���;%W\��������L�dO�+H4�!-��~)�%�p���z���@��� �ZD6|؊0�,�<�`'���*�ݟ�CD��d��y�����V�(@��E���T�����
�:"����,��%��)�莳�9�F�\UaiV+(rS�M��	����� �ϷpU�'�'[��[||�a��E�x��֎ ��]>מ+�K�M!��� �b��C7�\E@�"��v��\0�e��j���L��Fo}�\�]v�椹TکJ턃�\b�F{j��s�h[B}�%a�y��s &���!��S���U���Y�ﻌy�b�r�(�Kt��b���@�_L��70+�m���X�/T�ޡ����Ac���H���tj���aٷ(@�8}B��>�@��5�	�P9�R��E����Ͽٲ���^sjO�a(��̭s��f+w�Վ;�o�Mxd�	����#�h��'�Z�~X-��*xč�����Kp� L !Ö	�\��o�,c�W
����DA`���Ⱥ5��c����S��6ύE�`q��X��!��,��/�'�cE������:�!�~U>����d�^
b��p��pV� F��aC>3�پ�"�U�Δ}��ϰ���4mť�A6�M��k��w}G����S/�����rY2�9a���媔Cfϲ�^gT��F��Z�>P�VLܧ��|�k+�/r������]YgxZ,qz*�aC�Z=
���@ۋ8 �~��R�)�B�bA�J��լT6}�:�e���N ������Χ J(#
G�)���^"˾��֧���Bb�H�0:+	<Tv(���2(Y|�g�߄�'zj��&���S3G���i�E��R��H�n1�e���w[zo!N���@&H���R5�ʵ;��#�����g���1a�	��ftMx�j˵���}�]nY�ﹶ"L!//����wŃ�l����E��0�H��3#5XY��f��c�/��.8����4��G� Ԅ�?;f�=R���o=;�X���ag�/��r�g��Һ�ȕ�-?���s���(��M�d�e���z�>6Y"Ȯ��ۂ�zl�C��a�i_�=�`���̐e��[�}�,{B'xel���sV���kƕ�4x���z��a*�M��8��k�y
X���N�(��䣬�A�?X��Uk=�+�g��É�V�n)�	����V�u�� Y��<�gm� �$L��̭@���5-r�F���a&�~�n�d���#�\Y���5���`�|�*�f�"A�i˒0���\����r-�����&��l��\b����hmE���|e엁����l�>38�ZwܒW0�s6� ��E��$	�TA��͕���+�/V��p�+��$��K~�WD�,�e���x�iF�0�r�B
�X\��ݼ!Ȗ��e�T�S/a�>p5�������8��9��Y]��em�ށ�>-A�m��/|���d��lD9؁� 3OM6C<�0�|���^׆ʕ���I��U�	����d=��Բ�jU�p���74���V�	R�E7CVR��!��y����Xa��]��y�`Ǭ��{</ң:J���jW��5��1�r`�x�������0���p�u׵<G�!��mtyKeX�U�E���XK,����pt�J����0���_��n�LK�6�C���m�p&�/ыʆSQ�;��nI��%*�̉�ʥ�}τp� �6�0�E��6�ǌMoĤt ks��}�Ā�@cm���z�e��J�ҘR�t@j�r[��}6Q�&h�
L�,��+��Oa9<���백�0AS�K�EZ��8ց1}5,�f��>>�
���;\伳����� X��X��pG�]Yl�<�x>���9?���R.4p\=�8�=���	84B����H�xq�i��Ăl3���E��8��1!VJ�g�%��t�T0ң�%�؉��4�0vX�ߚl���`����hu�$�}E���e(���M�V��D���+b�r�#�Zغ*����9k[y�ͩ=� L��j����4H'���K����������8_~��C�=ֳ���񃝺ğ������J��L�բX��R\�i��8Ѯw���0�B3k��9VL��+�*�����L���	����e\mtLܓ�t���4�#W&`T��ܥ��x�TN�?��AsY�[�m}��yJ=8]aO�
�SM1$����������z�;��I� Uv����|���2�����.0$��*{0�yT�ŚsMDi��콑K0`�v�K�K��&��6�_[ӆ�N��:��9��(e������P�h�֓^�ݩ����Z�
wVԈ�Mp�i�6��������e�'k�����}(�5)J�� �0AD�a�Ԍ+(m��Q=۱���~���g6K�(�a��md�n��g�L{�1#���U~�g4{>�`�3�,���`��`��Ȯj}�tfPl]C�D�X�wH�ٝqS���'�Q����1�Uy�Zj�<Y8�'<��������`�	 ���9��)��&S�s���
L9�-I�u*��ը:��em폼Z�̌`��2g�	���,�p�k��{�|{�����O��g+� �ϝ�}Z����m��R�Oe1���rn��P���ec�6n)+�����&X�(	,.�Kʰ|�|p�&�������g_H�ӣ��֦��b=���0�U�i����҇%U�Ŭ�W�|j�ɂ;�(��Fa�(��f�ϛe��#܅��;�?�ۃ����Y�L3�ғ�u)���R�]���M��.չlU)dICB�����(I�ޟ�v�t���L��ɨ�
q��$`Q)�q�dLZe�nT���[�L��As��2�¹d���A�}w�U��c�|X�j['�p������P&(t&[+�41�}����cǊ0A�wD��A�C�w�r8-��+���O)+�[��yޞ+�ɧ�D&���b��l�6L�1|�1��9q�)���x�+Ghϝ�������~����	MLՓEp��˭X3�vc9�$L�0w����5z� N�nÍ������Ͱ!���=0�y��_���}n��X�'	L�h��\cK��*O��{Va{����U�	��*{�^oUo�    Zz�2CŅ:̆,E�&H�d��gt ��������;*[������6�,�Z
�)�=lT%TD�Ž�0+OЧ�A"�q��TX@9��\L��"��j�0Z�91|ES$��Z ������^�8E� �V^4.��)�>��\55Y)��`k�D9��԰I�8Ku����ӂD�e��Y��f���^}Dѣ�T��@Y��y��*��<d�	,V�R����3�dU�<儎��� b�ɉG& ifЂb�W��k�y?	��{�`���iߥ���ZSZS�5+���K�r�(I"E�`H�,�3*I�V�XPo�{o�K��o���6�O~�g�-��sk��7�D�G��)B�K&@�N�����x�EN� *
�����95@Ԑgd�3�����?�V����^��ql+��z>_�[%m,/���Vƾb����x�	�Z�\�j���ޠ�51��y�l݂={���}�ua��x����&P����b�]݁<�0A����z���h��t�`��.u*�	��S��~��J�{e��3=UV����U��Ϣ�J�,I�D&�hp溠Kl��k����8n��0A�tx���je)kLN�Ƃ�</p(�%n1�+�'���ݓ�0�F�SGyz�6�{��r^V:e]�`�o�=��"L���g���O�`�����䵖�����J&��/N�fp����
`G�q��nќG$=��)T�U�t�qa�4�E��������5�PQ#�Z��vk�a/h�1��9�'�6��8����_�FR��)���VS��F9��sI�����e}��ب�ˣ�/�"�rm4[����KT�@�W5�|��3��e���G�Z�t�.u0>'M�鸬%'L@�N7�l�9��P�)����Ч�0�D�3���K*�%�ğ~�F�$LP1?���a���$f��i�}F��'�EM���������3&�0�G�%)��%G[>/�� ���}�\(@�S�/��ާNp`S��gN��b@��-�l�@.�,�p#'pT�����5�T��-(4t�����ƒ0�BM�r�S�`Ӫ��
4��)��}a�܇�0E9��v��Єf��v	xSF��b��>���"?J(i �f��%X����:È�ņ��]Q8[���0�MUmg9h��d�I��w�1�S!��Iw�d�[��(�#l]6�*z���	>�X��G(V�v��ƾ�oZH ��U�	$�`/��aҀq������'�0�?T9��|�Z�@�̰�n�*O���ί�1�����I���>h,kH#*��,�æ����yBd� #&W�����X.m��%�'�[�O�Z!V\[�|�dU(>�a��*����k�O��5����
��-j�beHO8s}3V	��)r�8F{I&�C�U�D�Β�!�]!*j��Wˍm�֌��'���cx�.�ƏAaP�c���M�A���r󌉇�O�zd�R�m�(���^ύ ��x~�KUe�١�%��ګ�r��&k�����!�qн�f!qd��~�1���`�g:�؉"L��=�W��J��¿�;]�M�l(�Y��� t�y:*�/?���)f�a����H��S�v�>��t�r'�z�۪P~����NF�e��Eu��̫��/c8�"������5t�lX��!{�����W8Q�Q��rX�y�H	�P4sl�������9fMj��{��w`�[9�����{�æ��h����V�MP���=h��/k"�ȓS`�/?���wÝq���乊0����q���w��_~\����f�r��ۚ�j$D�Z`�I��.I&HQ�b[}<',X�ʬ�����5�m�����*�F0 C��&�QӲ��wQ�$L��vTV��ܬ�=>8�O�鏾,�g�(�s��l6KE4"���$�=E�����~�x�^���Cl��568�� vt�|���;@�$	p4�sA�0z�9���C&�cW���ě���c?��y�0I���5&{Ј!�0�5݇)d$�{t`�݀� �(���Zd+��GF�$L@�T<�o�mV�l�@�=R�T�'��U�K�5��O)��n��:�O�|�����	�/�a$�v�g+��[63t�����;(sM���f)�t������u�q�$L�QZP�.,i�4V*�9�;�u��|�����WǕ��5�_!s��[�����+H}diVf͘��UI�L���t����ܦz(�2�����"L�Ѫ���'��l�N`s6��/�y��_�03H�bޮ�:o;v�r;N����dl�Mu�j��h[c���Vl�����(A�9T́��|_�X9�[8�E��$�G-9�$*3�[my�/ˀ{�-����`N��i
�ϊa*��[��B[Z�/�W��뉺vӌ��og�	��9�0����:���H ���l�q�َ����0�0	��F��.����u���}I��4ˑZT�>���TA��Wa���PC+��Q|Y����ꌭ8T�:Ȱ��9l����rW�O����(FW� L��+� w�g.��F�,��l7q�X&HP6�y��|S0���i.�p�1�����<�����x��aF��wr���g�,u�(}Y
;d�L��>|l�E�S�K���:n�ي0Aʗіx��RWz<�s�*���9���)+(�S�W��a�<�4�}�0ļkX��b{��R"�Q�5j���Q�>4A��������V��~aOE��o���$?q.Qd;�0 屳��������)��%j�|�?�^��5�n���B�W�߫C�J�[@I��2�W[Y�m�L�5 #c��]�ͧ-u��`����γ�֬*>�\��<I� %��/�Z�2M0���K� �s�F�)8p�a�J����&���.�ŗu�{;F���
k<ݮ"L�1G�=����&�� �}l�E�K6�}I)Z�=���)�PĘ03��^�F�H�Kj�&�A�.�N��0�ī	�x��v'�,łM�+ܳ��X8a��
HOq�f�Q�2���`d��l���k�]E�`!gڸ���[� [[�x"4�MV�bx�>���6ډ"L ���E��T�E.;i]�[�2,kH�_/��ˉ�@&h��J��|�3A�^rw�� �|E� B��!c�6�j�Z��-Ӡj:�Ϸ�ȵ�5002�
^[��G�_'�#�jj?5A�LA��O�v�yJMv��7c�����H���tK�e+
-U�6B�����{=��g8q�TțXܟU��{j�d���s�{(�q�,��a ��QK!��{-yZ�,B��b^��ۢ���>ѵ\�O�m	jO�"��t\�W�	**�����ᾼՉ4Yq��~���SZ����$L� k���3��ekF��O�>��õ�L+ЏxnvlV�S�)�5͞�U��W_S`�Gŷ|.i���q�P�(�҆���h�	�^[��|()/:�BG&hh��H��.�}j�����V��<��I�5�o�q�����؏�<e���/�hl�5(�a�F���q �Jӕ�����1�:	ɩ�d=��!���Yv:˞��|����c������v��z���kmi5�d��~t�'���v�}d��8�4_& ���}���O�̙{0!`�&(�$5{:Q,(Ļ&�b�T����A�^��ST��<i��������#W tIpq.�c3WpQD�&���q :/��5[���):��]�=����t�X<J��Wvs�X���
#�Q���0YeC�����ڌx_�Ŋp�h{�o��Q�䱶�M 3�p�a�,�o48U��!F3�dD �RHX�$L��$w01sZn���f���,���`�ʦ��M��q�[e��Sac����L@S�E��
8�OL�^��X8�t��C�������@%��a#�$T0�"�t�mf�h63��ɲ���"�/_>o)���g�#'r�P����.�~f���~�3��j-�w��p�������B�$*�=ǆOm�x~Tk�	E#�P��    �Fa�Q�w7�t��k�Y�g��|#A���fY�)S�Y�\!f�z��ޛU�Qjy9��-����,M���7(��7k�hPHCw�$N�~�&h>2���$�d�b�s"u[��-����.	)�U��T�R�0d�mc�������$Lp��8Z_]35�����B��	�ј L0Ed��7�/R�\f��h���y��Ő�9V�3F���C��`����s̉�����&6� ���tq���$a���n���4�RD��X�;ժi�&Wc?�$��N����S�o`V��y�Ʒ�I������٦�FŤ>������p>�g�n�0�8����F"c5-�,�;FdA��#��`��Xi�ۮAc���F�5ذ�v/b]a���x�hY��(\!7΀!�(��粝$Iz[������eA� ��`WS"-F�!����Gq(��<8qv(iڱ#G&QR|K9[��dK<n���|��a�V�Ә"���qh��aV8�'|��¨Z����*�:���VY�+��ӭ�bs�\,�;x�����~η�,��aO��[�D��·4���*�>�NX����D����طW���r���{��϶�9�S�"�҃ŗ�ĽO/�A������8xڛ7v?�J�׾d�(����}T��b�[���i'ˀ�8�<��N���"L���L�����nU��{���4}Ib���_v=?�|��9���q�:�Z�ﳰ�JV��ƾ����r]����\��ȷ�6S�*7U�<@&i�+����Gm
@- An ϡ+����rdC]�ZGXE� ^���(r��MTE�6����j�&0����U|ʁu^����5���աBJ��6\��;�2�$2ٷ؉�{���e?�F��x�!�~sџ��,���a���_�����!�?Wa�O�F���vl��O��Nn0nA�)f���jV��c�#�tGq��9Q��;�F?l���?��&�U�Z�K`�0t�'N��3L'/N�Yoқ��Qo2J�^�dM�n:�f=��$��9�;�S���w�׎ƃ�?_��8u!{�a�=[֕uޛ�g�}A��<r;0E������n~}Wn~&� x A����p���(��O��qO.�c�_�y9�ޖ��BJ>�?���sA���t	Nwҟ����:K�C2_�ɤ=��b�3x�rt3G}����t0�\����a|���� ��������[=Þ�fF�p��a��ȕC��U�|*����8�}�1>��tBg����˟�����^���:rk����&���-y���+�~ȒBQ��`������#4ݗ?�M;��$��n4;���O�z�:x<|[����P�E���*Hxj����p�B�k��t�M�F=8�tԷvxz�!Xv&<�|��x�w>�!~�e:��E�3����;KG����8� R�Ȏ�ޥ�O
�$>a��u���һ<�����v!���M����v@� *�����OmOqG�GG/��q>���/F�����p��_�;����(�¿��x�aq�5���,s����0n~�MgWV��/,l�vzp���\�����x�7O��	 [�c��ַ���?:��G�!þ��D~
ۉlE<T ~���m�^���j����K�����ؕ5�����!l��XE�?�p��tN���t�������.@�Q�v��(��-��,b�pd�)�\ c�����wz��2��NM(4����E�Sf]¾��pfF��6c�$���D�`Aϋ��XߗT�]d?_�=�xg�������!���5鏧�5X��X0a�8���>�^�t֛��-�(J[놠y&�ܤ�T|)����t�[¼&
��C����w�ioLa��E���E�lj�ᶜ��ǁ�O�����{�vO/z��I�d����������K�-�X���A�wgG�G�{���,5�P���bv:�U�7`�7>ã�V�����p�Rd��n �����n�5r��nz	�z`x��=���0���� ��Ǣ��q���/�;��J��-�����9:��k���tԛ��~,�7=�p�\�rex�����̼\`���8���10��&/s�

)��Uڽ@���k	�ca��i�*�֗a#�(	;�9���:����[�l,=O-�w�IA��V�S�xa���dP`_������z��v���(���J�</��&[���jS�:�͹���}����55�|<>�cF�tx`��Xvr����g�q�uj,����x�\�:�J-�����mh�Ӌ.��o@�p����)�e�.o�Y�(��G�N����vC� ��m��x2L;��x@�umMw��~hY�pY�i���
�l�o�p@ߦ�۞�u���Ya��G��*Vo`S�!1��`n�t�z���λ���1��K�sW��˟&c�l�3`w`s�OϬC+b��Ä��p�N�SU`o�-���D��?bBr�/������口cP;WSr�4a�T�ٸ;>G͟��c�F��t��F<dklY����6���@����z��`r����H�Ko�<�5Ft��#4Ñm]q�h2f}�Qq��;L掼7�Vg��sd ܬ��;��ف����:V
�sZ�;C����f'b���Y{=�.�|����zo|t97��7��3�t�̊� ��'`R�~z2F��,��~l�fZ�1R��r<� �H�O:�����izT������.&M�!�R	"[΋E�vݛ7��Ct��x%��]_�:�໣c�]��G1�����3���>��.�;�ɇq��]�DN���G�t����4�ļ�>?f�܏��pHc��֟����zg��y
�b��mL��� 6ԭI���%��(y�����H��x���ɄI��b��`@�''=��(��y�a��Nw��v���i��V(�7`��;Jљ6���:Co:�ǣu��'x�������0�c[���6#�����9o�oPgؕ�
`�����|;�T?k�:�CӺ�c��|{�oV�,X1H/��&Jδ�s��8<�y�A&ƚv��=EA�{�R�oE~(�vWqzx$-��t7~�+�1�򗟂-x�X{���ITv�HE��[����N:�,����\;@��+�lj�Ή�(�����2y�j���d��#�'Z�M<�����y9�Q¿��4/W����_?ewey��*���uoI׽%]��h����_�;5D?ǝ����8ؑXƸg���`����F8kh.���u�Ỏ�����ϼ8ć#>fR'�&�s��n9�}��n�y��{���Ѯ�I����!��3a�����x��j5�Z�?�Z���`�s��^I�U��?f-�?�Z�դko��Ej���51[�+\�o�F.��k��(f�DaY�cij��JBL����C۫眤��<P��� ��L��Y�aF ���3x���<������R���b%J)���* (�v��e=��cp��1���#�T�M��<�=3��'�7��,�װ��)����E�H������`�E p���1:�`��5�n*Y�T�� ��Ȍ��>,]r:3��^�tmi��$��c�@f̽��ƮPm�O�B0�Bç�}�[�t�Lc�Z�������Y�x�G�0�/1h,�(� ��7}���z���1j�������Z�)B����t��}7�8�@���:�����Y�7��x��'�I���$�	�p���Ӗ+�ΔGթ&�uBz�g)&/�܌C�֜n�1I���p{��`�����$-����qI/^-�.�0X�����n��=ݷ���t��垝���L�m�e'�x�2��e�Z��j�UK��W���������ŤG׈�+�������f�ʵ�+O.ޣ�ŝK�g��c!E6l��n�(��G�J@����h�� ���u.��Vg���c9�pq�>�$ѥ{X��("[,��pYd��S�e�Qw�9���鮳�/Zg    ���[��lQ������I�'�,y�yĀ��\�(��:�c0+�un�~�S6��{�>�Y3Pg�A��	w�q��Ȟ3�p������t"u֟�;�>����>��1e��À'�N�:;Q���m
La�/כb��K_fҁ&7���l<� �e�w��(��¦�)n������M1Ǳ���M	
���O���o��q�=�D�c8�x̸��)R�(���<
�h�ŊX��{����yK�z��B#�:��7�A�h��K��	��b���Xd���&��Zl��盨�W�T~9�7�Ҟ��B���M7=��`��2�`ۅٴ� �%�t.&$���:��y�I~�"[�8Ij���JHO�1�
����{�EMǼ���lƜ��rgq���$���X‛�*�T)�&o��L��b���~�Z ���S���ͬG82Q���ܘ����U�X��ͦ{<����7JуۻėK�;z��ӵD�L�FQ(��3��X�7`
jJ�$@ɧ�q'Ei;n��v�����*�k�qBNsbY��-��s��fj���g2K����)��c<��#���	fB6���!FI̳j����oȄ`�Aև�>����d�;� �<��wq��"��6wO�������^�c��9�*����d�I���9���k�S�74�7������i� ��*v�|CՔW/P���a����Yʗ�e��/�KŬ���R���B�e�p����/����ٌXI���k��G
/+��56]��s'��rXA�`����D�=�&u;2� �����p�ݐ��8�Q��&P$P��o����~�����j\ZWHŴ�Y��e����br�l3�v���E�[q8�����D$�0qk�d�b��.��F���k&��KK<�i����_|����$�O@��Ѳ�0��x��{x���V��LC����K<�,?:�ʣT��m)��f;�a��e�a[��Ě��G�P��W-O����٨�����J��&}���(���y,ڋ�������	�h�_� �l�,6�2�WKz����p7,� '�����Xnh�yf"?b^���s�>�&��,W��8xVb$��~1=�(�u��'�zX�ѵ�D��#���g;qcz�T��Ļc>�� ��)�}=�S�2|m�
��^b�������-i��u�_|qB���b��<��Z�t�a�;��a����/�l<��e*EI& D�}zq|e�r����ZA��Ȼ||e�D��}�C�,�����z��Tb�Y�����x4E�:���4�����75zw�0l���tvڛP�U�ڼ�Kl>�)�$1{h����	�&ڧa�,4���Tf�0=�5�����"!�qy�y���++{�쵟�?0J�
lz��u��g�$m�X����������>=I�!��L�9�˩���� ���oh�����g�^/e��U�
s�7��o�"��t����r�`��0d�`�lP�\ET�z���A��}��8(�-�`���4�!κT�Ӹ[�]�G��'�����s�W�����|����w/&@��R�2��Q�utӗ�'�k��F�����t��.@�X��fXݵ�8,�2{�+���WH���86���2�w�Ǳm����bH��sMg��S8�=�'�*Q�֔U`ɹ��.3�&�#�y�dl� v_�;���?;�IfC���������0a��R��mp�g�;�����E�_�s��)��{�zx�nV4�@���
0P�8FF���M�e��z���H�e���;��������� ����:��(f��<�siئ��ܿ��:N�cf�+=y����a��6���ݰ0���'ǽP�-ai�f����E����,�N'��S�m���8��[ \Y�>1'�C wW��B雾L�9�77�''B8�vw�;�5������x/m�Ϟ��v�Y�`
Fմϙ��s�Zg��i��a_�݀�E~�3C[�?����x��/�v1:�F{V�c(C�0���~�4ex�p�`9�x����GFא�}`�dnW@y��e�H܇�UT��z�c������Mã}Sҏy��K�)�Ox�A,Mw%?N1p���a�j����tH��{j,N��o��X�
ެ�y%�;g_M�)��$foqb>��P�
��{A���^�p��8�0y�܍-O끈��#u*d��	,�ݷ�v��1�=;����h'�cxZ^^A��K�*+��߽�;��oy(m�Bx�#T��Ʋ�]�v�����>����z�pƼ�=e11~+(c�N����g�	]Ǭv/d-{��	��z�<kZ��P��� 7q8Kl�
A�tZ�R��T�q���
�Jd�l�+��vզ�BP39�Z�a��˟��h�_��r������#�a�n�b�	��Y���?�p�|�c�_rG���aV���?���Yo�����z�k D��{�We=B���b�2�E��!qh6x't��v���@G�T�с!�t�%߳�֫o-js�$('ךsl��"�~�y�&Ѹ˱#ߌϱ|Zf��s�;N�������{\��}�7���%k�����Ӌ!��1#�,��.�}�:d]�@9>��@�`��)\Qx�{���*�C��0��0�$�\�	r�Z\���;	��5��̻
,�� �����pSw{�gj�q�!X�Ek��@��_�<�,/!d9���a*˰:7B��K��~Ͷ��0 E&vX���o}HO��=�.�&i&K�:J�������I������hˎ��ǯ�!&�0������L��>�u���Q��޲h���:�a1Ӟ���fMB�Nd�LQ%���wޟ��ϱ�c3&ǌ���:�IzD�t����������懳���Z}�!w.U"�I����o>���0�h@zu��Ⱦ��Ռ��N�Q
��5}<��z�g�&��f\��>�^����m�lC��e�r ����R�k��Gq�'V�̓���>l���-c��E�@���5a~��cX����<�7M/�RS���H"0�o�`3�]�ɐ1*֌ww��mǱc�@��aЃ�u��)�������K��,s��cN{�ɕ5�9��U�L��b��d���	�u��Ã'��ʮ�{ql�8'd�8-��;&gB�.4ƿcۆ�j��#�r�Αao�%���cw�}���ǔ�i�M��C,Յ�%��x`�`n�a��wbv�jôς��LR1W4���jz��c�_��v�  �ҮB&]������g�z�=o`����w��KD��s�� ��͓�S]N��3���C�xul�D�z`��p ��w;��I%
����:��������3tq0U���"�� ��ӂ��a��f�6�ǵ��[��'V8����|ȡmh��k�9Wݶ4� 4~�0���O���s��ޢb'6�qB�s��n}ч2=K���,`v�=/���o)��3�j��Ći�!K���ޣ%�E2�3�k�'����X"g���1P��	W�w�}IRoZ��ض=�|�	l0�/��9 �n��[���Yo4��v@xz5F�g�l-�x��dL�+4���zզEG��9�~vVX4�G�G�!���5ѥ8M5A~�?��ż3d��aL��Ҥ-��{�;�%��q��:n�g��V6F��e��,�5J.�հ��z�Mo�y8ƌճpQ�xx7��{rFrE�dv�5�jڛ���b�,�U��&�-ri�3�,�0�y_���a����451LM�[e��v��J����R����y�����e�����T`�;����b��ZO�p������x��1�o�i^�a��xv������M��q����E�.��N-Ù�S��-Q��jo���r|�T�c��G���V�2tM�,���Z6x{�N(s�;Ìɽ���^	�׌��u�[bf$�>�]1\(!���x� #j]l��g������=��� �[��Ֆ/����s��({�ޅ�B&�R:�#���9�    !`�Z/f�Da����3N3��,P�>O���z�V-]�2 ��{��c�l�Ć�Y��	٧��yv�fFZ�b��ӣ�������1�{�"7�¬��qO�O ����
2���z-V>�fR;�yֆ��G�S�u�ٓ8dC��/�/y���@G,c�'P����C�tǒ�IdƂ#�(*�w����l����$2�K�TQU}3�ڛV�b�ucܢ�1��[�cק,�6u��tВn�F����j������wV?��0a�����h�0��O�������o3�����!��������I��P�5m���0`����`i�2'j�rb�c�S�O�������6z-���n�qB�AF�n-n,|���?�W�����@�~�o�>s��ݤ���D0��Z�g_w4�����c�j�M*>�z����x[b/��:%����qk�+0�"�.,�������M��.q��j�d�߮��k�4t��3���d������'?�W����Ӝ�����Y��1f����$K1v1�k��U*�v#lHv1��_N����� ����Q�o��K"_�ˏ���o��c�5.�;��b��V�_+�(��N<��ꥎ^�daN*&�����;��^ <��;B�&���vBK���{���� �W����m~w�7c=KQ��'�+c¡Q�j����0������(q��Io������t
�}ϡ�x�9dռt��wo鑟����"t�f	(�3��.S���Ѩh4ډk!��K�};�%/c��4DPı ��d��8�횵����������)�<�����`�5���"���^F��6P�38�u6F3�?����C/0��p��%N�G)����cc�X��n86)��� �ޟ.�VD$�h���^��f�ܘ�Z�6�g5��x�V~;[�$ql6l�b�Л�Z����1�� �Ѯ6����YZf�l��~��p���A��3�ˌT�bZ���S�1�j�bF�4b�NK�p�f����2nM��}�{�ՒD��8E��	뼷�jN&}�Cx{��)6��8�!'��$�"^����P
gl�6HG�!�k'�����Z'il��{���6�MVu��*�Oa�`��<�"����!s�mk�+��
:#�G�f,%� �谇�^���;��$�m��Y�#)�k�zKiC�!�OPL̖�j)4;x��sQI���]Ms�F�=s~"��a�
U�i�n�	p�ݴ���͝�%y��'fn>��������=.��fVf�@YLZ�d�ݖH&�P�����i�Q�Mf���p2K׀Ǥi�r�md��O� ��"#�����L��T��&���R���Y�M�ͺÒ_!�4~�16�� �F3�b�%V���#'=zl�3%������:k8U~��0� $�������	�(y�P.��V���R�?l��	S��Y�Md�h#;�	����>�@��}�_�b�������I��v�|:`{��)�7�l����]I�N!��Ծ��T,u/�7UNx�Ae��]v��L�j��̤�He�D�~{/��k�ۣ����\����y@�aCY�=(/���n�X�H�U���d�����=,D3��p3��1�%ҽNG�~��&ydh����xp:⎕'c��3�qA{��bW�Q����e�K�)���y̢HJ����D,~ ��c��.� ��ö��gfr� �P�SϨP�GZ�0m�s��r����?���n��r��` �pN�UkjY1�Ğ���;��hQ"� �Ջ�0�iwX���=^����8^C(��\��]L@q�k\Ǒp�P�tU�X��Wد�S��'�E"��P~��y*t�����ֿ����W$y����,ܚ</.��1������L)9Q(bY,]d�T}�bO�o�����$�oP'����۠��� ��o^ݼ^����#ho_����uE������ �4����ww��z����4<���u�,.Տ�F�yT�w�����W߾����櫻��yܾ���� ������7��}p�C�o��{������G6���s찁���S-�eL��4�D�wJ��U~V֍;���싳H��e�>���0�ҝ��wy^���(of���P.}Fx�8PK���}dd#%��D �a=A�hR��lh���G�r��=�H�9�t����O�R�Y_�*{�E���(�E����uਐ�Q����m�b�<�y��i@2�R��P�+�C�{ڷ�e9S���,�6.R��Y���c-MX��_W��[�`��W����zf�1�:��e��uᮉs���U@��#Ә[��~E6�9�`�º�ޞ��9���vf��8���ee�h�;Ⱂ�����$_��+�V	����E>��u�8h�r��2'��+�ؖ6-z�ͭ�j��{�YS!_��:i&�
Q&�0Ef�z��L����Ŧ�F�Ն�͢$v�>��Nbn��3ʄXWO#�p��#���n�C��L�ᖦ�_����zD֗��hls��Aި�)�z��)��P��n}�K��WH����̙��Tf�\�a3�9�a��=���Y,�y�-��d��Z}��܋3-����Hn�E�9���2��j�Y�e�h`ڟaN��K�cdנm��N���'���e���{���.)nB���]d�3.n%S����e>\��$�aZ^M��H���e�$9k�z0�v���ꅗ+#T���)��wp�dvg�P~U.��crO�0�L��q�	����8�z�����}9=nԗˤw3����G���V�lf� ����.��N.�f�/zX�c.P�2$҅��c�U���2���
�FxNu�O�YǊ��g}.Aۏ���Ox��cd�����I��(��{���q��=���4_#�"���8	�_!��	�!��ggj�On�o_ߜ�����0~�D�O�iGtD��%_�<�˿��rj'��N]M�`?�(~�țw��Y���蚲�q���� ��|�_�˺�U��s���qևئm�t�"?5�TȤY�CVP�q�$g�	,W�$:3�$�2���V�k�u�FgE]n��l�j���6����݁5�P�mz�:�JFQ�B�`��U�\A��%�����n6?�u
��T�Ef���YǱ0�@��Δ?`�����?��賮���hrT�@.�̏<�l~�A����Om u�����?�)y�P�ɑ4\�����g�4Z��|�J��	�� !�v��,�5��ѲC<�$�;7s1}�"��P��qt4���!6c�D��,�QևU:�f�2M���n�����DFg�Q�o,B�«�mQBǕæG&*R��E\7HW�����͓`�ȦҔ�R&��d8y�[��h�`���\ �R�.��=p+�9�j�
j��<�v�G�LN4`1k@�r6=�0T¼|H�Ɋ^�c �����$l�T,K4�D$rN�w�[`�A�3��GO'F��kX�dPY��;L��,a��ܡ�H��M�Or����`qK���&��`���U�j����	��RY�~/���l����8e3����?ž�Q�OYM2
k�!���s��.v��f(5�D�\�����l(,<�TH�3�"G����Ws����B�#�9
�\�X]mKU�ug��ز����Ď�/���SA?����zdF0O*ώ���͝����q`�C�'��)FĲ6
V#X��çp��lnB%���[1uQ��{Ԣ�,�_R���ġp_S=�X~�М��X�J�S8�ȥ�J0:�D�� e���G�)i:h�̶{v�2J��FE�ʓ=���ɭ3.�X��w�H��Y��㾟2�I�$��5��<�G*�ϐ�S!����+l��r�Ƥe���t�w_~�!:<��]29�
SZ2���yK�رS�u�*���a�ȅ-����n!s)3c�����'�B魙y�,հ�~>��=<{���1���3��_�ވ-�_�u>m�kc�v�S�R�o!�*�^���@���Pa��gC�͘�an���r���̪`�d��Z�VZ:    ��v�X�!��ɠY��y־q�ڬ�H�&�?X�l	���z0=7i;L��]��X��C�̗xU ��f1��6Y�I��|��GȊ�xx.�ufn���٘���o�Ǽ��h�VI#C:�:��GY�d�8���PR�8^�xvR�1HR�43�M�$�i/�i��j���Y��S�����$�f� ��9-s8�}:��b��H.7�^���lVnC��mC�
G*��1b��M�bU4��h����R�w�<�����R���9-��fV*{
-*d�WM]L'嶜��F6��z*�#-���~�IIQ�D���OJrY�J�*�t�f���g���3�
$��w�t��&`��u�YU��]�eJ��ʇ�L�Om%�� '���Ss9�,�����+~����K�kk��P�Av{:�{�9V`�ɤ��(qm��΋r��c��ٓۆ��݀NC�?�{���	D�w��L��	K!����ċz���܏߻��L�|Ne�91E	�޾��5�������s6�*�q81��)R�"'��t�hf*���h�qi}/���c&�g:>��K-����]�ϙI��j�v"a0
�!n��}�nz�px���+�t��u�%Z���6NC&��ib#N��#�.����ZH
)녉Ơ:킨��5���c&��}�n���άZ�6-��j�p�g�!��%������pyH������-Ņ��]��>n1Vt�;M��|��
�_���*���v�f��R�b���6�P��wm�9����ٔ?�BR�f�8�����i��q��B�a����!�����*%*���|Ju��Y��k���L[aN\QQ�Vq^����0V��Noɇ�N����)�B�t���6�5�;�W�8�Ì>������.��N��PS&����/�5�Ũ�9�gLbd��`�C""��a��I�F����r��N����s����~I��g�{f��0�2�^?��H��տ�ޞ�I¶ͯ1Gw�D�K8��ʼ�������EQ/�U�b�|�s�pj��K�} ��ٺ��R����ams��Y==�9Ki�(��T+�t�����U~�@|��Q�ER!�V��3������9'*M���`���ڑA�W_��W�f��E7pt��(���~��i�`֫eY,:Ǌ��]���c��G����7Þ���b9n�*b�����R���}�������F��3T�0�6�V��ţ5��,�s	� ���	.KG_ͼ�0�B)�Lҵ����2o_�5���s	�Xe�p�:<nTzϏ3�&��|������z�(o���^)f�`��}�q�>��|�-��V|�<&���h�z��T�C��IF#	�t�g�}��HkI|��:��$�� t����g��p�6���ԏ���#����d�e���e��֔<�g�ef��悻���TQ��j���F�_6x�k��P�W?ݘ[A�󼅥�����-��Ϻ�E���2�.��9p�:_գ��3���Z��`� �Φ��p7JB#$紴<��}�<���:g���Ak:^�}T���^��}�,��C����=�?�)+�p�@_"��mFra(�&k]�T��ɲt�,*Sz'�$ݏ�V��ʜw;h�r1)�9'�0�N���cُKRm��q�KطwN�YI��L���ę��QcKY�l���zKG~8�2�dڍ0G^F�m�A廙����D�ǃL�g��z�I��pk�\�����8PB<���Z�_��t\6�ˆ��ܿ�GgBcd�F����d-7A[\����$ө'��ѣZ��o���ÊOX32����*�fC��[x��:�̄��O���Y���f��!�@x�RU��y3O��=2����)vP]��)HTl�k�DF���e��_���[Q�[��V�5����sȉ�>���<!u�[�)�x�at&������4�Z=�W��k��Sh�#�M+C�[ʒp���3xo`l4Le��`~�[1�'�s�r����M��+%��r8�⩎Ps�4�hF�t�j�UG�9sy����:��⸇�W= q�A�!2V��H�z��&2�YQm[X7GO:eo/��n,63��c#��rjI,�%�r�=��⳩��)3�P�%JR<U8��Q���8���g������5:��&�e9�,��f��]�٨�C�����"�S��XA���]�ŰƇ7�s�0��S�zf�Ɛ|������uQm=Id�u��<����(�762]#
�	�YV��^��U�.�R�h�
��):��II��EƝ43�����ʆ#D�m��PH�ƭ_%�P���ż2����*B;1�cg<~��\KݫRm�qB3���d˅b�ٵK8=�'>.��(�4�{���y㔜�1�X/�|���O���\fɰz� �8 ��R:PC!�So�A&Y�(� �*
�z�
�|�r��S�1bO����`	�V��)���7��3���=���U�x>ʪ|�>.��T�B�+��DY�˼���F�M��2���*�Fs��<2����/$����
�Ϡ�����w��BY+����rD9�j�V
E�3h�Q�[�#0�Ҹ_	�{�8i|�o4DԖ��G�H���U!�De�2á�u�J�WU���h���`�C�P��+��.��&2䲡(�i��@nhT�f5��m�Xw�s	�s��9rZ�8�0��7���K�Z�^��A��������3����9Mh��HYEO.
m)��Q�E�렀sّ�5�z�F�+�*Z���?���m/��8��'�e��Ƀ�x�S?��h���R@jx��+� ��6�yS;>*����^����cM���!ͬ0�Ety�3���>f��PY*��V��6]=�>K9������n��9��C��!���s�n�$�\Dҫ����8�e�O&�h�}p_W��Tf��ܷ/�E�,Б��z��GwB*��n�/�m�}��B��|Q\���4n��)1YJ|_gYc6�c�|}���~V�=�x������M�bf��a:�so��c<K,�-�Ū(��Fﳩ�N�n���R�^Y�SsF��V_D`�ʾ郔Q;�"a��j&	��x���5�SN���Z�(��7�oYҼs*�c/S	��X��:~Ġ�,p�r4:*�I�Tv�-������|��^�zd�O1J�ő�B�-�!�f��;����V�#v���
� �l��O�6J�O"'f����*��t��E��AK򇟐R4�,	C�p�Z�2�Pb��ᷬ�K�1<����h�"�Q���!���ʷ��\�]�p3�]��ͯ���dM)��� �yўMXSm���5[���!5~S ��E�%#�Teu�r�`&i�p|jW(�>�R+�d* ��V�*uR(Ჟ��v�R�Z�eBQ�Z�4���,�w���΅z�~"En��@f��M�H��v�2��j������\֯FV��{�Z�/_��+-��z��	��6f���r��Л݁ɭTɚc���m�ep�j��a�C?@Mj�FJ:k	uќ+E����P��1b|"�nα���tl����P�Zv�uZ��2���xQ�&��>���X])�����c�!0��	����N����C�
_��#�xGF��p�A�l�e ��*���M��<��4��	_)���������0��}*��l��s��)��W�4��L���7�1#�034H���Id6���
=������#�\G>G�ZϩM��"���J$���sU�ˆ�J����.�2�p���2�m��Z�8����r��r����Bsn�W�y�h͹���#o�La�
�Ës�%�~2r�B��9
�a�S��M\����S�@b�9Di�3w���d����L��w��q"�w��µ=��d�����dlT�؜�m���žG���!Џ�,��Q�zu<u϶�P/7 X�>��$��LJ&�l��x%�zL��	��>�b������s(��$T���uE/3�/0J�n� �  9ĥ�1��&��L�1;ף�+�D�S�f��W�Ml�u�1'�����W������_��{�柧�
�|O������cS�ʝ&S�C�S�"&X���߄T%$���K���u�5Xx��8c:�2���Y��ܜ��:r/wE��>�AMه��}�"w���;:�M�������%'B���?���٧!��J#L�(��0!x���:��K���"Xa��D�BO�������d]p�bVū24؎y�?�Ȯ�|;sL��0bܯQg�zݧ�X���B��p*=���M�vڬ��j��C�a��w����o�sb�*RHoay�v��EG�Ȭ&7W#�#��ɱ���cd�(Ē�O���z�j$�w��3&8_���\�op┄�fFib+�ևg�W.��FY;� r�8>|<��	�U�P��{���n���.�z�s�qf�rrt�s<U�"Ӿ��tՌy�)��E~yn�q3�̩�3�D�n�>��myUb���� ��jN>���z�:2<�!�q��<�61�+�D�z�l6���Q�y��,]�1Zj�qrp�D(�
��1i�Ud�*�T��v(���&�BaF�s�����k��;i\�,�93v�é�c������3�O�էG+��I7�WE�F|e�zf�i�ʪ�`�x��gm�)�rU;�fx�m�B��D�.���<�{'��}g�AQc0�]{��5>,�Qp��A�Y��uۜ��$1ľ��?���������o^�}������������g,��G{p�����3�߯���yޘ5�T�I� �\�j6��Ĥ
������ݫ��nn�o><��PEͭ��Y�?��كW����d�Q`'N�o�w߼�۟�ﾻ�0>�Q��V��ä���r���֗�	Cv��1v�O0:�pZ���<ɯ��!�xy�)˂?F'��������o���{������77������{�b��x��&i���NU}�9KD���g�t6NP��n�/v�)/aI�xZA\�f�E� �/�셶��feh5��@_���k��*�����Ucb�٥���?�j��hh*�W���SF����U�M�焨�����'6��n��a۬Q��]���������)L����89����	����� ��06��$F'g�iwmC��Zs܅�[R:P��E�N�,�M��b���ƃ�e+��Y��mc�6���Ʉ��Yj�������8�%}�D�ɻՒ��tx��-��	:��/46�>~m'����#;�~����~㾡zI�l������A�|s���7�� �>X�I�"	O�ӑ[ )<�ƴ؝t�Wpd!�	�xK�����Ph�ntj4:�B���=������ޠ�Q�0ҡ�0��s��庺��*���t���ߪ�c\ *~���V��Ѡ�w{	��	i#9!�n�gq���2D��^�x�|s�����_����)v���"eP�F���_���`���˅S��x�@�sF݇��%K��߰��Ⱥ�������������1S��Xg,���:���1̘�-�&z�Ƌ�L���sU�X]*���U��.V�%��2�ۋ��s0�?��2�n`W$�Q��֝��L�.�Q%�����^��($��������>�5s���ɩ�Ne��fA�	8m�/ tT�'��[���?"�r�,$X�u�����/�K�])at�����}����o�ܽ�|���}�����NK��      �   �   x�u��� E�5�"��?�"���1�k�&Q���X�c�H/R�s���Y��ʣ"7�i1����$z6�����?�Môl!��$���r�A��l��I|���h�1�$��؟#b*&�6%HA�{�;�ʾF���,��h�;����1%���퍈^�l>D      n      x��]Ks�Ƶ^ÿ�+U��s9�7�AH�L@��T�EB& � �b�.? �Tv�y�E�����n�(jH;7�KC��;�ϻOw��JG�)�b86'��b#�͍����p�b�;�m�p�_�B�����/�ÿ�ro�U�8���e�m�.]�8|���_�7�ʋ��?Ø��M䲝����ݸF����F7��b�{������g>g��M�c�.�'%xbƔF��?Te�4���F�]Cej��N��N�>P[���U���k2��b{��ڹ�f�5�'� :|�=�=�����;wy�q�����O��"7��W��> q�^��q ����&\�~���B�K�e@6	��b1_|�p�5�5ǜk�2F������@�[������#ׂ��q˽��ÿ|F�m�M� ge��B2&J�ב�A�G�ӄ���O��}͜���+�ݻ9�%�^��4l�@�|{�'��;^g:|��5���2�}�J7�<�:7 �oDHΊ�uEm�4K�D��`�=7�9=Gee#�1�Ř���p�ƌ��6���v�M,��nM�;i[LM�1P��6k��5��cM��������e�AWlkf��3}lӹQgsm2��ll��m�h��1�+S�/3�1�65�����]���52�Ŧ�n�����G0��k6�g�{�91�m��0��z����K%��{ĥ�2���O3	8r�޲�3j�yY�u�����51��q���!FG�nв��YyK4�d�]�߄Q�#oYp�[N�\�r�]�?Ы�p����l>F�$�D�G�p��߯����6%�R�� hF���o?� ,��0"�����
p"l�{��(���}����v�'��;j��r����2��@w 0`z#��۱Y�N��{�g����6��Y�m�(���*DP>L�����1���ۃ��%P�J�k���Z�� {[x�72f�}��&`J��:B�W � ���r�g�1#�i|O�K�Բ-|_Q�C���E�АS`K�#'��V�^�Dx�sP��c�/��ox������O����+D��#�A���ğ.��Qh���w�1D�G`��F��0�@OQ A�C�_�t�\�`Bj"0����%�3#`(���	<o��Aw���J��%�L׋)���)R4%.}��������}�}���t�}���B+C+[G`I$ChL�a�p��cf�����l��F��!c�1�ڂ��ғY���M,̱� <	4��{�b� �+MAr��H
��ЦӘ��F6�9��r p��5�#W�� )�Ť,!�W!�|�ST���	y�L߹q�8��>~�v���	�"仞���G4�0�S��@����un62z+Q9� k37@���q�%ؽUޫ�+�S_�o�2Q�{��4����˂�����i9��c���f���є&�P��;�p�(��8�&��{l����ۍ6."�D�����:s���! �0'%[ �	u����B-Exh 1�K
"�R�/N��q�~�'�'�EH�Y�8���)�-����~�
k����8�*ł�����e��P��?�#!ID��<A#��A)�B�,@&16�`s�a�V"FΞ��P����I�Z�k�5�����^��]ɂ�҂KAd�ڑ��+}*y������YG��l#��h+W$fN��B�y�.Ql�Ɓ��3�9�����哦V��\�mXa�CL��q�B^a ���Twu�ʘ�1c5�$�s�ǚ���<�f|���y��ލwTA�". ܅�����!g�y˔��h;��Zh�ɰ�u!	���2��,-����»�Y���4د��Kb��йX�����>��Y5S�j=�=�s:�N�'~c�m��Sm�<e���Ӟ��/����|>��c�}{�A:��)R�A5m��k�61�G�D;)y��� � �@��][�/�sSV�ސ{y>E��K��3p���^��j��9Z,1���?WXܲ��6Iy&] q�_��ػ���w�v�6$��^�L/�Ŏ�|l�`w���U��V��5�mCө��؄$�aX�rXk��m�M�#8�Q� ��|X(��Ջ
�9[f8.Q�,�D��(Mb���Ou����Pv�$�ܘ�r��u�S���=)�܍���>� �J�5 <C[{o�ss�M-e�!;�v���rz_�<�����_�Tq�w�"�R�X=�@�-�:Lצ��Q�.���(���1�t��y�Z� �nL?-yTc�BR:q��#��(�=\�ӭ�gD��C���X��S����fE*T)�T�o�MA�ƹ��OWK��p*q)��>;Ԫ�c����(<EY�Y�^O� ��t��1��Q���icL[�O�k0$�qnR�U!�?��Oc�@;�*�V�	�S~��Rt�T~"/L��W�b2����£��km� ���˞��������T�m�9r�t�A�wH-���8�R�(1��ݣ�˒?����-�R58�pA
X�(��LWH��m?�lr���^�13��:S����h�C�/s�����c��=*3~���+D�R��B\`�^�C�����M,���_4��Q(��#�P@X��'!a��@Wy6�a�S���J���WJ  }k����Ć���-Gqp�HZ��K@��8�A��&b���0���¤�vґ�5�6�c\�A�	o�Y��sf�/���x���*2���GE=��ދ_{��O���.Ç�mN,T�_��NC��2t���V�}�8vp�����½%f2];��#�� �$�eWi�|,AE4cnضv��Ql�:E"�h b<�p��(�k��֌�`5�g�<�f�e~�K!k��[Ł��py���������b2o3t�Q��ʯ���+h�ʰ/5G�"\(����9��[r^�{1�U�n�����_�	MZ��XsT��|��U�W-R���	튵մ�9��|>|a��V��A�zj�J_1�%�LB��'	��0jس�
b��*<�.��p��swл��ܝԸ:�u����lk��A�>@FI���S#� �����ĬsJP��W���/p]<4	��]J�O!<-�@������-L��mH�43peO&��R�(B���S8Y\�aÌ���x7�'�<�n{������t ^��viO��_�X�6���A�[84لa0xG�0���Hb��u	2ڹ�@����vNNg�ohNsI�ss���x��+JG'Sp[�5Zs����k�<��|B��$i��Ѐff{lN1��h(g��ON���RR^<�⦌����Ur��z��9���WbK����)N&�ʴ��T	�T[рC�!׾��4^�4H���Q[U.j W�^܅�F��-O�e�������N��ʶ�b��9h�#hW�95�c��Mc�M!\�^�{��#�5��a�k��o���{�E���2��Oc�����~�J�O�����^�?�<���Y�����/f��L����cU/�a�?��JI�w�F"k��߻Laf����B�DԮOLf��d�X����T7��
��(=k�3�Ҳ���4�)�#1�3bý%V���<��?`F�o	2)�dB�l���\c�#���t&�Nn��(�_�jE��f�S��*X�ACB�{��CT�P��X8[t�'�ظ|������oh
y�����w-:�o�6�R͖��ɪD��zR�x\��*b��	^g␹�da�q����ʇmQ��
ӓŒ�5�3�����]���G7�b�OO��s����+�V���Y�񪅄RB֧��?�Lݘ�x�>�/czP���`i��\��"��lm��yd�@ά�5��L���	t�k9��Q�!�\;��%�2�_=�� ��bJ�9C�<0�O,�7J��!�4�1m�y����R�b��f���O�q뭩i.mn�hb˷�2���UZϠm�E�L����@r���:Qr����|���[���$U���w��\�ku��on"����O��B    ;��ܥ�A��*�g��)�<f.�>����G<XC6`^0y`[L]���~���0��R���2��?F0;0f`�$�B�k �~+ ��
���ј�5ZLemIB��Д���Gk���l}�!HP�"�)ƀD��������n BBR�?�r���n�V	D*NJ�<���YA�{7���l��eC��Hn�6�͖�1�DSt�77j�����|�<�A ���p3N�'���^��)��U�w�k]��z�v��ʣJ�a��;�O�:��
j��>P9�]�X��x����s
�r�N�+���Jv��4|�˨���������{/�@B#j~H�!�b��=�m�aDɈ*�Um�|-�j�9�ݜ��&$�����Q��	2{ML�V�����v��C�L<�=�iy��%ٛ�T��{hp�V��&��̹о5�h{��]m3�,f�̈́�=����w��$[����Ê鬱��{���`g�u�}�d������-��v��Wm�I�f�5�sr��T��g�� E�w�?���6a�[�hW�c�!7n������-,�I���$�7"��'�o��vUW�]J�z���1�W�bOS�Ƞ�
��!H������L�oޞ�ؔd�0�?���Q�<Y�=ٶ���H�]���� �������3�T��Wq�O6����ܓ+��(t]\���cf�U����+���z�ڜa#��6~���p��੭�>̱�A�]���k��/��k��z�~	_�X3S<O/%�UH�m,���q>�Ǐ�R�^~W����%�����6.�sЊ�����yW��/k�sN��t*�����K�/�)9�,���:9��O�|_��`� ���l?P��+!��TL�V>��<�3�p�������e���4����B�b�!��众f�aՂ�>5�[�ar�z�������i���VA-��B��&:�:~�g��r����F�p0��%��|�ī�t��̙�S�8�z�2�@�v�;r�.���7EZI�:B� ހxk��4������6hQ9�9H��a�''6ˍ/�L��H ��d-fܿ	���Ɨ����L��yz�cO{j��}9�$C�F�j��]��;�@�lBx�%7ξ��h�U��at�q/�b�(�(�ZX<xDVL#y�T�}���,�ai���~s�A���F���2N��$F�T[�*���K�"�=MoB�$��"��%��I
��j�RR(@�؛�7���s�z�-9m_<:y�_�P5^�at�9�A���UYVc�{��;	"mD�z��O����l�5d�)!����֍��+��jr��L��6-���*Y�֗��|0#�aQ;����)�2;���y��V�E9���c��Sэ�[�p���9T���T��U�bT�A�!x��9����p��P�[w}�)�]ie�RUP4ͦ�/Gѓ��X�Lj�i]���eېv���u��G����Qo�����Q�F��śJC�r}��]b��Td�V'V��q������Sqł ���>�L��ѕ��	���f%�\+tq�Z��-����	Z�IΆd�x�%. �2?~"�<���u�ҴR�ZCt��j��-Ge;0�b�6�[��J뱝A�ČX'���r���}k�m�p_���p��M@P�c�U�~Cly{D�=ր��_�t��Ӊ�xD��x~�@+�]z\�Ȃ��S� ����&K��W)�O�m������綟|���r����kS<�v��	es<Vm�AsT�z�r�����v{<��O�����	�ƃ��NR\��+�3�ϰ c)g�x@�z��(�`��n��p�?`L �V
j�Y	lc*G�í�+_gږ~���L��湶	Iyz@.LbDj��,☛���k����Le�G�=��&�.�
3	�Vtb�����k�y�eb�pn�:K�@��s`�9��)b���.�-��wB�D�g��xn��L���0�N�h�0xoˋ|�r���jO�1nb�L��b_I|�`S�� ɞ!6I�#�L�n�ǒ'�5w�f+Z�t�J��k=1xV�4
��|�u#�PJ|z�� ���懿O+s�� 8��B��G`-�S�2 ā�W��xGYF(�y��q/�#1�9�-J���y�>9a�i�<4��ٞ��;N��6I�#lT��H�v�6�,��G|+	6U�PjQs�I��H�̕r�^��F��q���� �x&0�D�1�!�)�=ʩ��S�Q������sP���,������"X8�p���r��?򨕖����CS���Ym�� W����L/	�%�ܙ���z���=;;���:�T/�.�`@�3d"UI����z��t���_0}�>�r ՎVU'&�	� �r :�d�����٧~����;H��LM���o�i$%Nf;Ohz����߬fU�]�N������N�Y�z�:6zYs.r���qe�&!�9VK�	ϫc.���;���2{��a,'ƽ�|��S��U�O𯊨�e�{Q��"�q�,�~%�!9&�[��v��OjDyOb�kOG�r��n.!�о�Lʱ>�Z���<�'L��wn=��*W������׀0]�.-<(��w��5�rM���2 Dq(��hY#--�����%SH#�z�������{ܼ��,c�O;J}�����G�F��MS��y�U*�ZY��&C�s6��3%��=��fd~�
�/."���A�ie�:���6i��2-��<�=5ŉAsby��&��,�}�Vn��@+���&ָ4�^���$��
�t�tB�t��*���PF�`����7zbU/��Q���S�@R�-��C��r���8 ��%4��k�ՙ?�P6�N3R��gc�������ع�^sj�>3���ec�複�A%��1���ޢ(J�q�T�=�*n�*s��؜1[3�C���x�ܛxE�T7�$����,�$��ݗ�	���6u�^���<�P,���~��r��c������]����|p���?>��ā����x��-����y��/�+��w�����,0mz�� J�$큎�T;�;��5+��5��x��k���\�����6���t&�����s��Ukb,�tS�E��%��J����K�������wW�];5���-����[0������	������=2��U6M�b�c3#�ec�>�ҋ=b�H�#!�%X]�V,�(����t��r��}���r�3��p��5��ۊ��qB�湅[�.O]�t��M2�ħ����Qy~�[�
�H�l�2E-�- _.4�].�ŵY4d%&�,�|�1Zݭ�k���̔�}���j�*6��q�Ew�	O�!L�;��Jޗ�܊V<�qDF��$x���D�ry�O���f���h�"�c�������>�[V����׺-<��a=���qt�Ѽ�̶�fGD�
T�e76Ւ�˿��D�"�V�r�|�!/p[�o�'��t&�[�m���Z#Mw6���-y�Xz5ػ��)�?�1�Ɨq5��h3�i�U�q����%y_bzu]�& B�{o��mϓQ�Z&���`OP0�ZԶ���~3]Hg툒#��М�Ы����q<3M���%��I����/gk 3�iX�q��q�k����ӏ��A�(x6�?�r'��� �xk9h=/	�8ia[��!Gɻ���o�{�0T�S�g�4�aۆ28���_��-���c�q�KÖ5-�~^�QЕ����$(ct2�ʼ�TͿ�j�y��1u�G�L�����sY�jA�����faX�J�&ݍ���#G�\�LC5+����-�Қf�R�4�;+����t1W=ʯ X��D�F��X񠎦����E��#�i̠fWK���O���EY,���I[�jgW5�������*��&��W��{�'�?��}�
��v�.��.�ɯ���j/؃eq?�|S��A,w�d+�O�*"���uőG��Nzj��T����p��� 8[��ҁ��0��x�l}1���\�j��RZ�DQt�-(�&�"׳½Ā��r�������.��*� �   >}Z�+А��P��%W�Aj�+d2���%ݘ�����1�]�v�^4q�m��1i�{�.��s
`�b:u��GS�v'��!�w�~M�y��o�u�ai)��힢M�s�^LL'�d@��<c��ej�kxLsP�����������.&      p      x�u��r�H��׈��'�a�$��Z�T�Lՙ�0"�T�u�oS�^Ԣ�v�V/�ǯ;dg��f����p��]i�F�������ݴ�~o�o���u�Ue�/�~�~?���s7uO볩}h�U�ћ���ݮ]_���Y��6�S;������M��~\�����w�����n�*�2����vX_����}����&տ��*��{���ݯ���߻U�ћ����٦������xw�DM����v؎��n�N�n}ݏ��*�#��v��O����~ۮCWΩ�2�$��x��=�C����҈뢣�t����o���,��u�����+za4������'�Z_�۩�)Ul������P�J���s��F0>t��s{x�}'WIq}�����o���X�P&U����? ��f��������6�v\%u���}�]���A�n6�Il"��
���Ÿ07���cG\���~�܃�*M"��~���������v�6�*M#���-���[_�� �_���\�Y�:�x�C���Pe��0uk��kѐ]�}yp�Z�����쫴����^D'��v6�
���5]��.�7�vG�#*1��UZG\��p�^jo~��A�0Y�I�M�u�sM�y��<nF7����A�@�VYq}�(����̒������8��#�|�T�i�������B�l�F@g��1R=��Q���_xe?v[��w ]ey����=�/e^��9��w���-��VF}}�P w�f��2��]Gݟ'��}t ���*�".9ܷ�w?��]_w?F�藍u���������b�5�)��}�pۯ��<������N�̠����C�ʓ��u{��xeF~���N#.y�	�A)7%�"���n�^y��M{��`;����3������\�7�U^D\g�Isz�<�+<JH�Bq}�����î��y�W��VNvw�4�����XZ����p�M�7NY���$Cg�8*b���Nя2XX�D\��mB���o�}?��4�By���ț�wrL�"��.�i�v�S�U��s?�GY�����,����b��b:�؋2���=n�(�@��VEq}���~p��黗B�Gߍ��~U�������$v�վ[��ǩG��ۉn�Tq}������
�w�>��8�r�n��������$���o��MZ���L#.��〭{�Gs����4��".<�t��4�ph�4E�N�wU��W����H�z��UYD\��]���pI�,q⇩��\�Pp��-���@q9E�FB��`���]�#�w[����?�!h������ݢ�Bk��gG\��E���ݱ�)nUI�u3j��^O	?���XUi�u� ?n�}��/u�ʣJ�i̲%������ݶ�*".���y^�î�2�r��fNΛ�134hUA*g���� ha������o0�m���l	�Uq�ټ�Wuq��'֋�x+~�jU'�U�b]��4�����4�zÄ�vNF���$�R�Y�Y�EЯ�t�Կ�\�)�9��:���z�Y9�UpFś8����>7L��f8v?J{T��ˈ+����q�0�i��*�`��e4�s��-�0��3�O̨*����v���%#�����UG\�1STj��29Nc�r�$^ID&1?��B&WMq�������,��x��������ڪ�#�%�~�v�~a�i�QZ��w�.��~�� �����_ê)#�7� qc�Q��Y7WMq]Sϟ�Tϳ���� D�_ǝ�B�7�s60��+��z8A�����\����ĭ�G9���ޭ?�^̩%16v�K�ݻ��	���O;s<�E�
����|�l4I�E���cZ��5�!��u�8�>c��S ��%4��;�!����8)�2<Y�0 x���zTw�&�'�p�0��>rMņ�Έ�<z�Hc��'��;��A��� �%�����&��LD��C��iQ,J(�}x�j.ͺ�<l�� !�[Q׳I����0�v�BL�ψ؝�רD���iw1�K)��'�xݍ��W=�g��Y�! Mf擃�����ہ�K��U'� ���%F�&YH��a2�B;U�:2�uAZ
�#&��_�&B� �2\'���_���g1{�D��%b�M^Z:.&����hFH
$���f&I��%d��|I 륶���4��@B��;��^-c�� ��0u��N���E��oR�ȧo�bz\��3����11Ǎ����r���!���L�}�K" y=N�� ��GܩB�k��.��P��"�f��\����o���G@q�\xd�>+P"k�(~-c#G���;�0x#�I��qy���pC�	�A "�8�.�߉OJ��V�tX��l�Y�I	$���D8�����(�\�{�#��@���ń�@r����N��)-��:e��u2f��L��q��*s�GA�L}!Rn޽��|�ߺa����\r:�4��@r�����|��iBJ ��w�ڤW-7��	�$~?��s� r�>YCp� ����
���P�.1��i`>r~�}�H\l�o�<'!#����ih=��Q.�' y7)Sk#L�k���n���ݼW�UB" �"��<���y��l��=�Fu��B�P����e@o�ʮR ���H~	ާ�@�G�}����і���$,<��}�M����?��6 gXx�#�T���ް"�������V��N'_M �$����7i���|y'���%	�(��ϧ�2�R�s�/v�7J8!�r��һ(�FB� ���*�HUrJ|�Z���H�������E��+�q�j�������K1U�E�WnY�RЩBļjﶭK�Ϗ�����:G�>قd�g����x����f���`�\�Xv�*��;��'@�4�#�򬿃YX'$C��n�s��t\��A��� �+`���s�Y��lK�!C�ڠ�o��V�:I��Ftc.AY�GR�%���JH$˂��%R
dȇ^D��Y�89��E��
堆h�g�#�w�TA��i�숭� rI���HߋO8J��)lz�S�w��cXE1�x
���anq�����(.dq?��8s�F˲�.�[���]i�Ek��fq�����z�T��$���ۧ��P(J#��3����)�"ɵ�(��@����K{�l�jj��L(��K��|���Y�e$q+�K�B1gJ�H褭���:��ZIN��{���F����j�sB��L�;%)H�C�{�+�ߝ[O�i'�k0��{�|��9�: ��ƽ�0d^��~iQ��$%�8�3@Ũz�PR� �2(�"��H)p!� �g$���+-���=ݕ�,N�.��K���n�"�V��ish�3E�6�z?r���e*#�Z�&\��A���S�ǃ���H�����K��_h��4��D=�y�R�$���d� �S-�#�`�L�Y���R���Z��ʲ/�WpB�p�ٲ�j�y��I�>v�R��2��Sȼd�����ai�t^�U^%^��=)���<�o$�[wGr�%�o�0(��,_֭.�5��C��1H�$�E?Yg��N�@�xS?C���T�@,���k�l6�#h��f�&��~�:ʨ�Z�������\l^r�)�/� �Ǵ�.�
���޻[�"��q<q	0{	|�ob�ø`��[���o�v��~���%�㼐��E˙D�"xJç(���ns1���q��">)9��<_"�!��J��R��$�!��q�#��K��O�qK��y��K�:j����2��f�=5�#)�U��>����8��f0|��^C��1C�%o��r���x�NЋ��(!a����-⽤>�����/yo+�F#B޹��s���\xwZ��O��p��}
��k
�Xn~��� ���ӭ|(&s7�D9��K<u�쌟�I�t�C�2eN!�L�b�-L�(:$�n��4_�����Bڽ;��a��z�Y�U�ʟ��� N@��K�S�ge���_���K^t��A�2C�,���    ��|�y�-�Rf�V��[En1kf��ȼ�a��쎠���^��y�/��땸�P���x�&d��"/!��'<%� 	-�۷$���sib
���nѬx3^rs辵�0�p*��<4^ry܍��!�q�X�@SKa�K���t���y�߭g�

s��J���K���X̥ ��8ÉW���Q�A
�!�����"#���d!#�g@
�.Q�ޅz�G��K,٘�W����'}2�/�OS��Dx�Z�; �h-���l���F{�\z�RL1��% �"շ�p��I���{?پ�F���%8�~k��Ǟ�,��?��V������%/2Y�<Eie3S�Y��]��I.Ҷ����$��;b��Hr���i�.��K��?kZ.��Z_	�����I�VY�=������)�#Ss���])Ĺ����|2ظd	E�g1��K�����B�%�C����W=�Π䒏˥��g��"�A�?�I9��K��:nzc�<�� �ϻVI�	A�*�f�1��%ކ�e�0�T[JRMηޱ���Ke���&����es@���!d+���*d����sV9WN�_��U;%�f� 
�_K_��Z.Y������U�m7�/��z6	~�L�{�����ȵ�&KgF[��d'�u��
b9���4Y3�-5Y�ƻd�x�i;M������P��N��m� a�l�6�����e9]�� Ւ����
ll��<J���-5��e��z3(��>�h��$mN�|mк�G�2m<�g��_��A�%7���@�A�%7-&�?��N	� .9����I�b�·�A��20A��m�?�5k�Y��؄��N��B�f���XR���Yv��� ͆BK�NC���$e��\�F�!g��G���׵3� ��[�q��^�,1K_dd�����-��_�5�d���=i�k��,	�n/,�,�^�0e�ϒ]N�����\ҵI��A)�׀
��S�U�A?μ@n	�}E7��ڷV.ˣ/�>��NA^Q:�"���GS:(����i�YDp�`�A�%�>��y��K@վ>��,��v��G��d���s�P�Ȓ����=�($Yb��^#ߵ�e!̒e�Fb���*@�i��H~�־9s�(���,�U~�ջ[_UJUHX/\�pf�Ϻx���YB��pt��6�3��.�A�%!ߝwR.O�8!F�<:a�!�DKN?�|����N�|k�/�}�������Lj�};���k�7�v������H�l����w��Ԣ'��-�����אi���}r�7��r-zig-�|T7��}KP��o�ww��ŃTK����
�Ě�q�o�p�mO�|Zrf��ʭE��PK�|�ʁy��C%������F-���L�}mmNv<���my��n�Ck�h|���翞mz��_n�3�lK���`�a�"8��-���m�
[�r���o�>�=�����kՒG-��5�'k�9$\b�0�9*l��e$��}�mH��٤��qɕ�9�|�k��$�n�R�X�״<(�NI�v��;R���\;i����v���.��%������|���rm�էx��8�I���[���o�Z�T1�&�q��m�~���Z�{hn�/�&'�P���e/	{}���|n�������K�ԇ����wɹ�}�!\{�r��d^gpT*��2��e���
/��xs��Kk9�]r�;���K�v5�a���<7@9����(>��X�'d��]>l���:YZ��fU H��M�pv���>��-e���2 J�����m
F! !7��НPչ��s�E��۹i���-M��k=|l���L���AT�qĀ�=��Vɵ�q�_�z��gŒ�-	��Wi��ը��?�y{,�`�U/�����9�\B�_uX\�v�#o������#93[���@>H����Q�v����B���]�����t�vsz��x�:i�{��lJ+�^�>���Ph���1v���@_ߴ��7oG̵�	|��u�"׶�\�p�>L�Ϻ��� Ɛ��/�=�|��y5�2PA~iO���������d��ol�D�k�ׯ���搗C�%�r{��]�N
���ٞx��� ��S#
���r�ݙ:������PE�Ta-N��(&ğ�0B�o�Cȸ�v��9���l	2.�]��C�J��L������|,�'@�����'���ϫ�\,]r��?5���Y9�=w���Ȗ;]���uX��v����wx�J}�۶P��Ѵi;jc���x �yh���'堈8wA��39�a��n/��9<]�g�{'�`�0��ֵ��K��)t�B@C��{�vQ��m�y��'@��hܢ���&!���� ��;!/~�kә�*l�����
��wA�%��[����*���t\;�.��?t|����%o-��S:���>X��
8!agu�s�>ع��.	�{|�O;|c�a�e���M�w��~w��҃�$pv��n���f��0fغ��������<juN�}9�7GwP/�]y�9r]8G�7�^����Cv�N��Gnn3�M���6㶰ۜ��nn�-�"�5T�&v[s��mí��sEb��\��k�N��k��"�� GEj݁�u�S��-���-���	ZK�	�/2����j��"��:!d�Q}�Ye��"��h��|e��[i�ȭ��T��DFk��P�����FT/rk-��#k����FW��Z�+Ea�ѕ����JQXt�(�	�R�])
����UFW��*�+Ei��:�d]�+E�륵��Kk�ի�Z�u]G�JkB'�*�Wg�*�W'�*�W'�*�Wg�*�ם����Z�륵�ꥯEe��ע�Q�ע���kQ[k����5�Z��}-jk����V�ԕ��/-jk�������eEc�1����A�Xe��h�2FP4�2Zk�댠h�댠�FV�Ԗ5��غ�Joz�����Joz����5FP��#(�Ab�7Hr��[!�*�6:fM�K��5��Ě`�ebM0�2�&b鍗!��xb鍗!�f�biƫ!�f�biƫ!�ia��fv�!�f�biv�!�f�b��vKk���/)ͺ5�2��mi��іf�mi��іf�mi��іf�m��&hͬ[�-ͺ5�Ҭ[�-͎5���X�-͎5���X�-͎5ڲ���Z�륵�:���*c��ٱ�X�k����X��j����X��j�e��	3�R���-Kk�іf�biv\�5J���Q�k�ٱ^�k�ٱ^�k��^��j��^�������T����zk�L'k�N:��
�ҌWp��իC�fǂ�4;�ٱ�(͎Giv\����p�fǥ
����X�Tf�B�2;2�ٱ��̎�Lev,d*��R��̎Rev,�*��2�.���t��̎]ev,�*�cAW��*�MКٱ��̎]���сGk�*o�@Wy;���1�Uގ���vt��c��̎]e�+�*3^AW��
�ʌW�Uf���2�t�����x]��&h͢���̎]ev,�*��
�P��ʢ�P�,J�ʬ[(Vf�B�2���Y�P�̺�be�-+��B��(-+3t�XY���ټP���bU�&h�l��Vf��2���ټ ���he�.��נc��5����4a++��B�2����P�,�
��­P�̎�bev,+�c�Xվ	Z3;���P��x�be�[�**3^Z��
�ʌW�Vf��2��U�h�ڌW��f��63��Y� ��
h���Jn��x�:�5���h¬��k�fo^�6s�u��5o�Cm��k��D����^��v���^홝ګ��z��A^��:���^�s�ڻ#����
���^�u��k���z��Y��x��b���
Μ�^����+8sV{ot�ך`�j���Y��9���j�j�u�Ym�^�5j���Q��k&k�u�dm��kԦ�u�SÍ��pl��f�US]�]h�ku���L��k�f"����f�6kѬ�f-5^�6k���Y�t��}�f�S�5j3��Q��HYj3��Q���x��B�T�6k���YK�ר_/�5�^m��б�LD:V���x��LD�֘�H��o5n���V�@ �  3�Ҙ�H�o��������&}l�Z����}l<$8��C�j6~�8��-m�UHK�C���V
������<
ۘ���6��8�LW쇬\��l�~�:��A� Vl�AZ������ۻ:X[���ro�ҩ��"�T�{�D��+������n��W�����[�:�[��soב��<�t�{kW�b�Rh�]'��I�����t�{kW�b��j�~��-*K���:u�'6%�Nb�PRm�}�j7�u�]sR����楤��[�u~&6�%�����јآ�?��kՍ;�m��Kl~Kf���_��~�k��qG��a5�X�y�ƝC7�ո���w�ܜV�N���j��xn�j��V��ҷ�v�]5�z2��wl��a��<���{kK�c�Y��d�������7�;�nm�Pu\��Ԯ9�F��c��FG�c3�F�c��Fg�c�X�4��_��l�R��n�I�-�7:~��6�;om�Xql>���،��y�ج�����̷�Q���љ��Y����y�F'x��Fgu�ط���f�M���۸t�61w��mb��ѹ��l��1��<Z�#����FGd��FGb��ׯv_��5���lkb~�)�	~�SgW����&f���&f���Ub���_%f���U���ծ�~#���7�W��{#���7�W��{S��ڒ�J�v4�WI��U��W���]���wծ�u#��]7�W���F�*1�m�#��Ub��T���]��F�*�)�x��J��_%��o��W��A����C#�x���J�]�_%�������q_���:/�x���J���_%������U��S�*��)�z}��J-�4�W��1�����ű$���Y���H���B����t�?Ը)�u�"��Ԉ?Ծ��PL����j���]�qz      r   �   x�}�1� Eg8E/�M���4ԩR���%��%�����D~,�M��Tj��ȱ�?/Pc���p��(�΁���x��"r���VZk�M�%q�wq��{W�x�4s��\�CCK[m����8*tƺQ�-�#�5=�����[�\�G��2��E#���ќ���g�H��t��OJ=ȗ�R~ 	�d�      t   c   x���1
�0 �9y�P�؂v5C�*hg_����~p������!	--qÜH��b��t.�_ ���e�V���
ח��dۏ:;D| �)4      v   �  x����JA��u�S�d誮��<��n�JD��;Y�BD�}>:�ӧ�#m���������q}|Z�o����=7�j�^'�Ur�اn)C�X��A7�"��t	]A'�BiƠR8(4
'�NaP����dP(T�U���BZ8JG�9���
�J��(�:�A!MΠ�1�x;L[0H;�hr�&�hr�&��ctϙtZM:��R8�AҰ�XzK�BZ;�֎��q:��,��i�8����o�'�a��	Z;��ZŠ�(�Kr8�49A�49��,��<��<��֤39�t,K�����d�E���*�;E{�&�t�)��TRHw�4:ҥ�@J��t�4?ҏ���:��ibzT�����<�?l�"��n�&�c�/{�����{�÷�ǹ��s;��7뚥�ݶ,��1�      x   �  x���KN\AE�fl�V��v/"+@�$@$ �� ��u�Z
�bv�*���{���\��|������WZ/Վ��<��iM�_T��=K5��5����	_����\q/-2e�4��oU����EgC��t$u��erF� 0CӜ	"C��C^�e6�K@�Ҝ�!GKsHodPIs�1�Aq"��M������於�&������%B�������l*x�|hh>�(/�g��
\Z�ml.�%���pks�������^���Ћy�Qt��C�qe�.��s�Mi̬�W4?���`8<�I�s�I3���Ҙ�0j@�H�G�����&�D����t���v&�E�#��7�)�UA�����H��� �Ir�����҉羠�A��^�:��f[=�yb��K@�����t}Y�3�/�\�����9ź��4�R�s�aid���.��'�@�����-L�~7A�sL9����U&3�z���io2�x�2�2�7`S&3����sw��̟�2�h)2�hQA3˿`*U��F�2ii�n6ӯn�� �C��3@��tP� �����YP���2 ��(�1
�r�	�"�t�W�܌�ɂKL)�X�Ũ|��U���Z����6�@�bTZձA�#�C�c]$<B9T"W�`�c�r�
Д+B9N��N��Ȁ���{aAe�*1T`�`>;z��ħ�>ĩ����_';�C�+X�$<B9���;��G��|�Q,�X�b���]&B9Fgl��C�d���Y��ﶓ&}�+���N]��*�� ���P�Nl��[b�.���u����O#�]شj��Z!vl�Z22
�}Y�Fo�o�N/�_�nN�߇?o��}L�U\�YZ���>�}Zo]_����y��X�����F�g�	��F�������͐�!�p$j8ա�jh:q�~�Я���N7�o�����kg����ٙ��S�عÑ���7��꧍��dׇ���?�)ף      z   �   x��Ͻ�0���)x��4l�6�F(i��A���2�����徽xC&jT5Ҋ�E�U�(�J�m]��� %H8�c�> "PH��L��L��}H�Zһ~֕����h�KpF��)tH6�����O:��'?~q�VT�L�,s�:�s�y��c����[.�5���T_      �   Z   x�]ʻ�0�ڞ"$z���n�ـ1�_$�>	:�"�z��Mf9�0�Lc�H-���/fG<QW�7sL�����"YSKcȆ��s0����      �   q   x�}�M
� @�ur�^�c��]B���]�1�gz����}�,ݵ��n[�L,#�Ѧ�R搉M��|��=!���ɡ�S��w%0���y8��V�����&�%�e���(�      |   x   x�}�=
�0@�Y>�/#�M�dSm��t�1zJ�B��[>A��k�Y�eݻeɐ^�ȷ���8�q
�s$r�49�������U�|NM�Ӓ~�b]*�����:r���+8���%�      ~   �   x���M� ����)z�6�Tkw�S;��M�1�,��;�x2�(����'rt�;��R�^N�T�R��ϣ�����J]�k8���n!Ko{޵��8�ȵm8"p��rj�+2E��:��D[���r �?�M���״���׶���9ն�gB� ��X      �   ?   x�3�ts�u�����,�4202�5��50T04�24�25�33�434�2��uv�!�0F��� �^      �      x���G��X�-:>�W���^��3��� L�	�p�
~�}�G�F��Z�ݑd�\i�>8����Q����/��_+,�r-ݽᖜ�+<%���%�gl���:�)��c��� uF5n����@��!�C����F�D4E������#?;���X��>�;ʒt�+�k�22��_��bz]�#s�q\!]�]I����f����ώ#���&#�4����p�����"Cu��N�h='3�^q�ܓ)����ـ�����s���ü3'�q�aĨ��6��^�v�=��t�N�;���;�������H�
�p���8-b)?el��3�:3��������Zմ�xl���Ƒ��- )���%�{NN��	���	Ӈ? #���m��8�ɛE��	#v`�4��,��ϑ0�н?Ɓu5�J���rRH<���: ��$3
0F��@a��_�������.g3u�=,5�Xش_� ��w*��)b.�=�c��A�$���I�`���qI\Vf V�_�:�ŅP�%�]T��gF�R80F�?;h�H���]���*�� ���r��0�NM�a�=�b#-m/�D�F-�����ȍ-6�L`_o��������s��"���s��\p��)�z3G���=g��c��l Q�$P�?cf�65��VW������|�QJ8�_d�3��yr��+<fBl���(
��F��*'拢���=;�F
��Vȍ��^�x����ː����ߌ�vP(a��k��Ed�!$[u̹� "�I56.i���I0.]I`\uv��t�����ߜKl����F'�_�u�Œ�	g#�ݦ	�X�Y|�lA�}��@u�h���p��f��~v��A$��h���|y�M@�/�Y{�2���'��1��кn���UrF(���5jK\���$�!�[Xz���uZ[�-H���#�nd}Z�ZK��B�E��BP�ώ�%Q�4��7w���4q��We�;H���ޫ���U���%~,
�C�#��c�nj�����T2�D��נ�.��@���8˒�ΰ�GJ���.I^��o�0��������_��ʲ������$�r����%	~�*'�R�b	�ӛ���k.�?;��Ch��AxbX#���2�jxOa {��Kq&����{-�,���^��6�߰��/9�p5g�_Ѐ��Pn%��4��Aآ<����n
�Q�c�<���P���56�[������<D�;+�gTɹ�kZOŋ=�>䖦�aHC`|���cKh���B��԰Z���h�S}d�bk�6��If�"�|+[Cz�4������8� c0��eG]�]޲�1�I��Ye����3��h!/��NcyE�D��u'bc�$B J�R�7��R�=��ʭ	���M���f��o��J�_�+�G3�/(��m��$��
nFs�Q��g��Q��@��P�P��,����Ԙ�����8����A�?;�����Ʉ�5��Rq�c�o�Ƣ�Ȓ���pbk�h�?/�]����i����M���4�~�'�J��4jd�[t�U�L��9}��Ĕ'ŀ��E������w�MA_ªL�e�RHRh�+��v6�ǚ�ˮFzn�i�y��r.���Vs��I���A��Ʀ�lݻY���l�%�!|��5���C��P���g¦5T=s��Hd��a��~CLXO�;��.��(���<��
&�D�,���sA0��Y��o�"��=n،A4���/�J�z�����0�r�Xؚ4�(�g��'�B�1'�{Bā�����'F����Mj`��-S;���\$<̠�t�L�W���snQ
s�yI��ӑ'��#���Z�Z�{S?g��[�m2G���3���k��E�RN��#s>��[��?������5θ����Q����53f�J��lR|dME7a\N�����oe��m�g�X����P�C8���@آ�x1p�T�'c�ە)@�W���1���f���-un@��ѿV����/V�Rz]��EN��ac�����}[YŻ��N�*4�%��W$��F�g�϶'�]?���9s�L�ޞ��d$�R>��X|R԰�K��׍O�+�oUL��ώM�л���l��T-=��EO]���{����dK�'p�h%#_$��o��;�Ͷ�ް}3�u� �����s���@Z`)A�b�4!�$J@z�"�2|�x�5b{��Ĉ_n�-�x�'*n.���O]Q���U��x%��!��M�B�~��݀�oqc�8���/�#�?;�˩������)��ѭ�D���tr�W��)�R�8j�����?6`[>��ɞ�.�js7���㴱5VW���L��D��j2�+�;}���>�Ȇ]}��*9�H�d���o2g���J��2�Om��fN��z3N���K8�hyl����FA�{�K to9��#�_oU+R�#{�ɞo����in�����c��B��5v���B�|�2�:���,�=j���Yڹ��U��:�p��l��x�� 0���W��,+ת��z!�mЛP53�-W����N�Ê�P�{��G�m�i���_c��c�J�3���,3^4�+�����f$�G3F=�\F�=�W�$����A�xX|�,)��ޗ����:5ev_}h#����H�m�>���X��1�ߺq+^~v�0D���[d�O�&~�����:���שs��>=q��%3R�:�T����8@V�F d�Y~����I7����_x���0*����,�!����R�qU�H��!y@���@��F}Q3i�Y��=���F�;( T�/�t��F����Z%ֳ���;��F ;����_f�o|�o��ϗc_�~1 �� ʮ3>��0}��j�Z6/�r����wlF~�^n�e�Id4Z׻݄�ߗt����Z{�5Z���<B{>�Ŷ�ހ�1~��������������z��w3�U��ix��A���[&��Y0�E4�)�I&u�F2��EdD��`E�ոe��f2��Ѩ<yNg�B�T!������$�-��=�1�ւ��ۀ��5�q]'��� 2�I	R�8�ba��j]͚z�X����z`�o��#����|�hʸZ��p�/X5�$�	 7�����s�E�;�qx3��&�xu���F���՗�%��6b|W߾����^n���4�-%d��������=ւ���3��)�����:�EWˠn��g�*R�H��t�u������e�>�$�2����g'[�t�8�J�Y)'J-A�וtT�+��-k}@C8��1,�i��A��42�np��4� '�}"��}|�V�8�r��������;�)^ð	=o��d
��6��p�ϒ��d���&��&�=L'6���$��c��c��+<T�hC�9��y�ND����}���[�:'������i��馩�,����	��2jѸ��uD�������h �G��s�2����`��9��x��{��������������[�Ǔ�6@����n
Xv��|3ro��S��_y0��b�u�\���@E`N��x��K��M�w�Ϝ�}�,�&��b�#��L�P�u*t�Q��` �r9ل���Ҷ� �M�C1R��cn�z��e��O?X��AC��\�2N������R���7� �x�GyÒV�(�>����{=K���z�ճܡ�@}|{(	ηsB��-��{�s�x�U�I�6:ѧ��o%��
�P�}��v/�0�ƭ8�9�����񮔠���O{��{J������A���h��0���	�l��%>�8��Y�|����^��|�l� �����A5�C��|��m����W���
��qS���gB_��M���a0�4F�:|��1H�� G�e��T�}�Q[��}49�9Y�^��]��kN�Y����|�N�Yd1湕{�����o #�~�'�[T����y�Yܧ�����ٱw�p    �׹�bf�8Nﲲ_-�v��6���4D�s�`E�6�����&D�����DQx����\��
���MB�E54(>����^��b� n&�Z� ݕWh����ط+ihN.���]&�c\��1#���>F�+�]L7�宖��Ex�/9`�فo���r��w&��2�r:�ι!`���p��m!��o�.tq4N2%mK���	��A����+o��f�2��}�k�x?
`{��^-�n^R�K�	���*��(hS�(
���d��ᵤ�Ěbr�U=C�fPq�K��Z�`S9��:ɁV��h��1
����}��EA�:-/�7�bZ�(��.%���O~�\K�ޯvs�*AnY(?��-�̾ �g���NG�Y:M��b���֖����[r�)��x��Os(d�YĞ��	���c0$���	U۪^ㇱ�8H�`V	�#o���z�$�����ڬ�K�o�Il�yE����
��$�5+PyQ�I��ֱ �3�9�\o-xʫ�䄨6c���w�v�[��Q���O=<h� /������c��Ox�j�w:~���b �|2<�E��=9��y��/i�_��Yq��}Q����kԢ�쟦j�g�d�Xrhd�}�-���$����H%��\)ѡu��q��q��]C�X�4�嵱|��<h����G_0������(l�����B�-{ې�(���pm2
���j�+����;����h��/9B61�O*sƛ	��936��W�E�H����u��4���;z`�)�0Bl��g��
]Sϙ��]���&.���o	��K��^)<����R/�I�D��k�3�{"T���{�&�����9W�#��:�0�]t,Bx-���E�I��GC�/;27��n2�/����,Fr7���6|O�+ �R(]�^3�N��<2���j�L{��~��O7-�ǦF�jN B}�B6��1�B�&<(M .;�	��j��&�vl��~�9��N\Đ�*�9���F��?O�&�\!���UypB���׸��.�����_2����+�E.r��j�ӝ_���
m3^)K���4� _��&wvl�Ӿ9F��Y˶��V��Bc�č�y3��D�_�G3��{֗����Lv�~����� �G���y��WM�~�M��:��#
A�
�K}[+H��)��ྞB_����(��1F����Ӕ���3�N#1x�H��?�J���l�D�f�ѻ���-�Gf�[I}I����U�b�����.#��W� ��y�F�'�&rr�$ݫM|h�o�`�F�$��W6�$'m�[��&�=^a1^��@灰cz�D	c�1bP�h� ���P��Z�3����m�ɿ����xbHJ�ܳ_l4ۏ ��bM4�5������]�@i���a���-�Y��@_��{Q(4�#k��(1мa8k���Z�[QI��z�En���@��ޥu��%D=�K�.�B��5�t�ɂ\�Z/G�(�
����Ih/+��9I�_ţ]��S!����v�xoπS*%4Xޤtr9LmN�vW����
x�٠�M�ъ]�4�\m¸|� ��Ǯ[�粤G�s�W��&N�%��#޸	�P��m�Fho���� �r������	5w)s� ^Q���x�ٌ��#�6��?[*�o��3��U&��̡LFN�E�F멻���2��@�Ūf{��
D�s�M���W�_�A}c������z���<�R�u�.�0�4���MF"g��G�mA�kbS��j!�#5�R��ihǼ���⥲֚ ��� 	`�6�H�E�<o1��
�7��(5�������� C	c�,O��W�r!�$�e��ӑO���� F�/]�z�� 2#
X�������F�+��4�����"�\�o��iz��_1��O�n=!& '҄�}�a=	��� �Ĵ��-Q�7,�m#� 9"[�!��%�n����b�CP�@y��l�C��F�ʰ��}��Z%��t߈�����o�w��"�=��lY�>�3�=�Գ8��yc�Pj(�'
U~��ߞ��<�@�3��`ٗ�?��8�A��?r�%dD_�S���P+�e@�r�髎�%7�W/�>��	��,�]��sm��/�W7�B�7�8�,_��z#�������=u��<�gЈj✄�k[d#�gt}�b��Z���N��CMfs4a`�F��
	{>BeӏC{�ϥ���#�]�{����KΓSqnH $���,��U�������̨��XIU��sW�d��#���&v���o�R���򢢕kT�I�M�d ��G3A���8Vׁ,k��%��3ȧ��ULQ�ZFC��*5��|��ۯQQ��1���O6=1tz��2�&U��O�`�vgܳ�|�b�k�x��<7l�0���D�
x�o�
�V��v���X��ïZ�i���Lá���� ���o
O�$�	3A��TZ�B&0�J�Z�r<����u��X2�Z�}���g$6!�[�0Ɋ_ �S���OB[����x3/�F��W Et�����z�s��&�w��=�|�ڙ~��9sdy�}]�F7,�>��)b�L�tՖ�_��-Ӕ�����6|;��N �YK���d��������2�bI}U�u�bJ\��y�8������	�(Gh�T�/�3����~|����#S6��չ���Zap
@<�<��0����n��B}�VD��uT���v��d�.��v|�bE�:0N���1����/�3�D5�����ҝ^�8�]ޘfQ�6s�G嗊�98^�T?0�7/߱E1���f�^�^M�w��ʈ��C@dĮ�?!�S��Y�9��D��c�G���7|�R���l/�ۋx�䤇#�^�]����c�1//ZnyWM���l�`�E�cX��Al�lA4�79�~��6]j,1*+��"�s��C��4�^W�|������T��<~����} ��D��/���@��^Ը��� �!��6�Db>�9��������d���D�����;��F�M��ܩ���͑_H�:��÷�C~�3�=q�?����p/ȋi��\�H\�������%%A�_Fn��?bf���+s�V&7#pj�Y�]Rm����[�1�_���BwѠ8��M�?����Ḱ���>���a��X:��@m~��¶z��5�_x�E?z)���`�[<R��5�7�P0,y�1Uĭ��
m�ȓm����ϴ'��l��ݦ[c�W^'� �W)�n�*��\���꒿k��с����-O"��!�"<B~l���-[��:�o �� ]�T�*w[����t:y��X[!�c��1�����f�6��嚼���b��4G�N�������RF���q�q� ƖSvl`��~�K��Z9�E$��7�Asdy��r��6Y*�P�}�\^"��݁�ٖX�O-�;��k<9�8���;x�yPx���RG�����|�8��h1�s�7bp`h���ß�}WX����5����!��U�`aC6�+�
Eb��v�<+(84��,{���]�n����EZ�%hy3.�F@�$,szQ�[&4��r�V�
���s��d�K���9�"IޥP�暾MJ��)ػ`�q&�m�a���L��EW�ژ��7�F|�C�ǁp�%�,N�B�c��.��j �H���õͳ��([W1i(�v�W��p��`�A�J�C��Yί�]q�Ng�N��gtV�v��ע Ó(55xaQ k#v���V������o�[c��}rR����h8��(�75Y���h��0�C���<pVr�Wv�������Mzz�[��r/�l���^������^/�]x`6%�i�o��c4�.;M{ﴻL��TS��"�i��4a�m����:�I--NɴҶFK�b$��h٫��"�o���|��=��㍹"Gxjj%GU�ŮW�~V$���*
��(���w���&��$%9][_e������@Ο[Hَs�V"��E(�eGm��
����    ��Ǔ�R�赬����,�����4s�uDWW�L��t�";0D���O���F
\od~VX�A����T~��b���������b&�m���;��쀰��Ӏ7X��؟�-�`|3@O�N�/ms�R�t��Ƹ(��Z�d����<�~`�����\廚 ��v,���<]�nP�[,���@O;���RG�K���]tc��y b#��:�&�E9DWşZ?���%��$�m��DN��;�p�Rݱr�x!G�
�d?����T�����s��y�>��0O7&<Ѭ�*%���`�IG�@>�
�4�������<��P��s3)��0�Q�g1�1��������\z
����k+�>�7��c��G�Nm��c������@+�g:O�fvԈ��ȘR {F��s�a��@�aߋB&�-טC/�z������X��sU���='O]���.�w�.v�7��C�����7r���s�>��:um5h�aq;U�{�y���#�g��)	�A�A����&�������i�z��J����Ŕc�W��5�d0*�N@FpǍ5��!à�'��i�7ht� ��Tu�Y����w���*8��'����'h��<bl����
o����)?�x��d���SR�a ��ֻ�͓A*�Z?�z��=���R�&�w��O0�ˍO{q�ѥ����	�&�� �c�d1!^� ��K���iĽ�������-�?�t�*��{,d�"	.������� !���n�rnˣ���P��`N{ �lR����J �[�=�F�p!A3�'�5�9��z�'�x����k5��IbXo�6�ߏ��$A}5�TlnZ�=w�;au?�v���~'��ݤ�tN������7����ȓm��� ���O�h��A��A�ґ�`�w����׼�Z�'ftKs2����h���~��~����VL�x1Qi��Kl_~�8����4�9P���|���3a6N�-�p�S"��	aw�<�eV]�a���Uc��PoS~���P����$�B�n���ن���(�7�Ű��Z��Z���k �S��>z��)D�����v ��[����/0��Z�(2���M*�����T>�j;A�>�t	?/�� ����3+=��#�l�/��%�ϺxMN���r���[�1]���CM��bm�O��p�:z�Y=ϯ��<����8#�_�Jo��`�����o[+/���.�i"��u�e�)z��5��)�p�����7�z���e�"3�8
cW��F�N�R�s�G����g���D!����޸�����<@pi+՟¶^ny%6�(O�YҴ�@Wx ������A�8����MT���I��9�lɰ<}��^�Zfӂ�H�	t��xOH-�JF��4R�!קr`׊ؼ��T�8�U�[qv_�$�U�%�-���f�\u��b%@$L3x�^I<���'�������P�~K�a�o5���
*b�bQ��������D���u%����5�\y�}.�7�����fW�I�xD'���||X�Z�)��,��U��5���%RF�AM��s7�?Wl u�wc�Eד�\cr~�����6ݻ���&��]z��}���o���/v���D��
�OY.�x�2˸Re � ��̼� �Y�i��]|�KB8��l�췹c<ϵ��ܐ�L7�O�������DͷP �Pa7�j���f�͏}�7a.����%�z�ڗ���kQ�[Ne�3ֵ�jh����z90�@63��^��;".�bS�	>��[�zi�BVR78�惤cĸ��(4�M��K��L$6����F�$�SG�Wj$�^S�;���;�'�<�|�ܠ�M8�����_=V�iv�"������,d�iz�d�Dn!d��Y��#y�Jz�C��;�n�m�����t�v�3�%�]K���X���b��{K�4�=���	��uzb��� ������O��\K8p}Vq2��ʚ���3[�H̝i�^���C쎽������yr#8��j=�;8��w1������D�N�X,^�;։�����g��f`���aJNN�>A1!=��:+q�)76T
]�N��	�W�&���c旀	��8��Ɩ2�L�&#��9��ٴTu
o�՜�6��+N���[��f~�z=��7�w�Rz9�*��;Ș�I�G���ҫ׫�u�v6�jN$��K�~���_Aƅ�ڐ�{�t>�S�B.��>����t�.q�^i��/�Q�����c�7z�������gq�؃c�٣�������(.Ă��=J��-7r����S�	3W�A�s���\G�z
�Wo�֢��\�k<�U%�%�*�����ĺ���fX�Y��m9]Գ���R�y3�H�P+���=W�]����_���뽍D�_�?"��h�OTЏCg%I��hT�Sl�^ y�.<h=�V���z��&�-��6�&;�v&�:)CJ�3�X�
��-:I�)q�{�7:N�+�z���F����[GwC�|��Ri���d'��,3R޶|Z^�[�
:���3�FB���"��2�e���G�Wـ����i�nm����nz�&��3� OHWk��w+f�"����H���r�5�juO�8����Ɓ��f-�l��d	�-�J���j��}ub7��
��LF4�Ø����N���A9%Ȧ��P��e&ۅ+�g�o��zx�A1�ŕ�Zѓ_��.=gh�1�!�uY��gZ���L�.'јᑊPpzϨ��_f��zq-��!>��r|�8�Q�u �~�@.�l�CK	n|��t�z�M� f��z)�ߧ���_z�E�0#���������%v�ݼ|�Z݅�ǋ�s*� f@�c3���Я�M��4k��Y�fҜ񕕛8�&�r~�문'���y���A����V��}(y�{W`���A���#*��^�ܢC<�.d�r pf�ʋ���������_f�
I������=i�<��U��x�r!�9L�ul�� �p���{+��3|����Rz*G>����<�L��w��F���8륒�?�Q�{����}m�c����Q�i���W|&X�G.�"D�.�yB~yf��BG ���!~�3|{X��4���1�}Ө�+wc��V�F��\���[핆'����o���?ȇ�_fpF�A��q�#i�"f%
�8Hx޾�4�KB�Zz��.���@Q?ȇ��vp�F2B�S�fy�\�����rÃO��R/y�|K��;I<݉���~�܇��)l��r�x���Bm��������������57�t]rP�7{���a�o�0����2e,�k��}|���௒��7�gaǃT\����,�o�C����&I�'�*Ѻa�	"`��W�;_k��|�UΝ�Z).��������a��Ip�''����9U��h��䉆�3Y���:@�d�R��w����0��ܕT��k5n.�⛼��39"�M�� ?�H�"!E��y<��~���c(���xx�5��x�Lm�=��R������[�:�|n��[@���a��!��U��Aё`���aO<���C�Q����:�4	��wM�խ���|��C�L 7��e_�� C�E{�y0�>�0;U���u����̰Ւ��������dɫN���	Ovc!U�k�����u�K"�|��R;���R?�qԿ�H�xi�Wr��(NQ��k��e�ܸ}׷�����VԐ�5���7��3|#�p�;�@x:Eg��%?5 ��Bc@1�kv2�B�`�o8ׇ�w���{�wC��N\_�����S�W�������K\ߡ��1��M�z��<�G��}�����Jm�ǔ�D?F+W83yjZv3�֮�g�ή_)хY+fp]��[n�7�}�Ự_)4�cvL��Y1��,W�~eP���Z�'f��7�,+������}����'i31d����y��}$oC����	��g~�MRF�!��0��>����0�wI�,����v
��    Y<$����r��eYK�X"h�����{�e'��
��3|+c�$��W��x\�˝���� [u�\�&�����t���"~�3`�����j	y��;�x\᎘dH���%�A��(����녜MLw`v��}��;A���S}һ^ͯW��::q읷@w��>����*X#�o���F��a��ɔP���(�w�,���L��Fm6ȐW�xʹU=���ʌ���ߩ��3|����	>v;EW[�f[A����q��z-6}�A�3�1o�-��G�솾G %<�Z��=��*����|tp�_��hW�e��\�����=`��?�𽞠�ƟRؗ�	Q�����nv�C-SY��x�)��qp���ΰ���@>��? A㴱��b�V�3�l$#`�hn ����e�7A��(�S
��O���?̀�2�͇S �����9[ "�Jv
�9���&`�TV-^�U.�p�C7o��f�^^j��Z`3Ђ.�8�Mo�z�t�W��VL�BQ����a}�lb���a��-?�O:��m��}����y����j��Yan��Mn���	��$��]�_f�)�PXh����%�dD�����u1����B�{�癫�����s�����Ow}�<��fY���ħ[{ygÒ�(���n�s)䯰�l�S?���b�
�U'$�n߉pM;nn�	b�{D"\$g~�uA�av��Y�{���c��1{5'��b�6[i��s�q�\#�QO7��:ԥ��=8���ݳ�����z$O|�lU��<7�ѽq]F{p�G��}:�LѠR�ŏ��f�����Ȍ'�z� 2��[ƭH�{!�k��"ѷ;G]8<�H~��#�?ć�g%�1�/�h! ��M�U�m��	)�j��4��2�S���>'Q�с�۾��1���Bdӛr�#��8%"���%ШU��AWO�'9ٌ۱ι�������_f�,�Y�q��:_cֶ�2�q��H-N��"G>Y$�������I��2�t��$�5����v.Tz���m:b?h����b�^ l�4f��]|�����/3$J̏U<`ra՚��m  �c
-�I� �Y�!��#h�	'�Q)?ć���OrL� ��U̪��^�V�2ևH��B����U(��Չ@� �����1��������k������FC��c<�θּ�w�j@��>��QV��=?ć�[m���6�!X�@���y]lù��E�ڻ�Pճ�ŧ8��J��/�[A�6��nm(ߥ滸�\OE��:/�g� {6�+2�"���j�i��Zq�7����?�~��䮑�c�s���%�6B�E�b��k_�&(�	�` *�����]G���C��  Q��y+h�Iٷ�S�a�/���kw>�u��╎������?�'`���C�+[�����0p�� =×�Q�������B�:�ڹ0��w�~�,���__d����q{ꆎ��gw:|n��(�ⳬA�4T\��3���	��}~�����N�@��d*���7��=z�#������d,ʕ4���L����\��,�w�34/��"yK|n;'+8��J�y7��5O^�/*8p��!?��]w��)�LT��+3�# �W0C-k�k����kT� g��c���:��M�?�'`�G� j^��!�ظ�Yאk���>&�~�3�g>�,) �ݡ��>�����o���&�m�K.��7E��jy���rnUw^��Mܹ�w��,������=��¬�W��j����zÍ)9��;	�Q�׬�_��9���k����'`�߀�븑%�"��4�e�ʔ0��'�'�9/��������9�K#�I��
�O�}R�w��h�S�0"n��k�+���b2I_*�qDx��=�{ڀ�!O���{S��a�8ta��@`(�6sJ�GT�F���9�R"T[���t��\�vo
��>�@}�%�a����(�s�Ԗ�"r�7��o ��j5��I"3�G���C?ԇ����T�c�������`rV�O ^'��/�xE��z'(&�uс4A�?ԇ���EG�����jK
�d���Tf�����w�W��Jd�O�{h��	<�C}��{�ƣ�{G�ð|"Kh[�X^�j����l���y��biO�gѳ���.�C}��{w�K[�M<�AQ�B�?��=�������E`DN�o�k���y��WGC?����j�d�zi_B�V�+-� /�cK�)$^v�-i(�L'𚦠�N�4�C��{h^Ȧ�+4�.*��hcl?s`֍��@^� �!���8��B���+���f��Y2Q��Y�������"f��%��ފp�#j"���
{�C3��|����]X�ӵA%��ѓU夎�7���X���D��}^{���})����~�3��{���������XV��� ��7C�M� Q��IJ�$��r�~V�a�ή���;�t=�у\I�o�mץ��c.�5v��.�S�|�E#�(G~+�{���?��'�_2@Z��6����u^Z����
^��󞾩�0`�C��÷�����;"��{2�iIe�~&�8r���"5���P�\�2͓-���JUƈ�Q]K������&����������[Q2ib��j�Z��Guk�曧{�D�\��:��wIS��=��^B��<tW�G@	��Lj�V��F��=�R��!�1~������ڛ���{2O& �;�qt���7t�I�4���g]כU$t����O_m�f�v���d�.���_��"N�*"��Ҽ�fB��VITRjs���!�vq_���B��	��aw|��Μ)r���9|X��-Cc�L<�� Ysk�_��f6��3��	����/��|x��m�J
�pBi(����J���f�T�Y`�9w�f�/{;#�~��:�U�t�ܒ1]�_��Dn��2�!����t���z�jd���@.�/��R���ѳ��v�D����]WA@�|�	��!v�)p�+o�Y)e��	����/����wI+��9��Ԙ:� h�SF��<֟���7�	��fO4���_F�����z*�v3��B*Z��b`6��ĵzg	"��E�G�e��u�g�/%��R�K
��ɧ��t�1<���ipMd\�8�Ǿ�U���*=13�lm+!|=�|W�ۗ�/u��C�Y5a��ȣ��-j�@Oʼ�-H�%��*[�q�/W�r��"�O�>a� G��;������ފ<�K�*� �xyD�J0�c��=~�'�qxg��-|8B�R'��{O���z��� ^�$u[Ң�n`z��IM���p�߻�S��$�I��>�ki��(��q����(�0i�WU���ਾ�y��"� ��8�_G����M-��3�q�!ʫ֓g\6��g�w��<%��n�;ٓlz���T�O�>4!I��@9t�a�V��ip6ɼ�t[|�^_��Ni^!���4 �,��	��N����k�
'��Y�d���סF�^��/�@��:x�������&�E!|�B#3e�᯶Ј�夥��H�B�!#d����}1����#:�퉹I����c
�7�z�h!&7h��+'G9�Ʌs|��XW+^����Bv��D�m�'
wU���C����C�r�^��SR�%/q���_��Н�����'�b�e����
ۊ��ãG8ͥ:�E+����Ԇ�q�b��`�XL���m]�gv��A�=-Y�J��R�Ր׍�}�;��	�o���-�h}Ch�v�D�&ͧ����
w[�0����܅�'�l�h����0�\@�����)ӂ9�ͷ�KTU�O���,�u���?r"�q��Ibk�0|�&��K�^�)*���J���I@^ЙK�����C��MO���,��h�g�� ��ꢃ��P�lI���|W�>;{Z��{���`��",�R�y��}"���u�[qރ�>���VE�i��1�cxWA�v3��pc�^�A@���:��1xI���{U�b?�[ӳs�    ��[�'we����MfDd�*�aO� F��y�U^����+��,�;������_[��1ܝ!|8C�\x�V'�u!ݏ���0Acv+�DI�y3[�qE�)Tx4�g{b�'�S��4�ix㴷���[�uܴN�E��~4�I�����Z�?�ǈ������s��5�kXD�vڡ1���e��-�L����qU�w��&Uf���*�L?Q1����<N9T[e	��˻K*�-5��8���#9_��aډ�Au�-#��D�����7�o谐~3��@#<	����{����P�(�]�O�u�"=�i��'�O��>�a�|�{�V	���8z�����Xm<�l*6���J�
���[mҞ�'n�7��!|�C ��6��uЧ��s�$���q���<�: S�RU�;���B�'wu�}q��JeMAo��Or2�=у�z�ӬJn��1p�-�WO��?s��C�p�٧����I�fiY_3� �٠����k�[R2�e���t����.�?�v���G���g����z�o�+c$�>��/gqux�Jtm�`�OL{����{Z����������IIu8��H�jg�Т�i��I���N�S�Ֆ�>�}hU��C�c�t��g� �����;�]%Ap�q�9�e�w���=��!|�Cq�y�A��%���z�u����P�}v�����y`��?O,��>������>�!������ݒ��,�)#s��{!_m�[�1{f�ۅ6O���?�"|D��l��7Q[o+��_3�fX��Ds�G
��mH��	T��i�g.�%Ů�C!�X���ɗX��:��R� d;+��'q+���YK�q�u��{�y��A�"|8D2� P�!�[���+�ɓ�x��U#0� �s�*�2=<f^��$.O�D�S��D��ȗ_�5��}�Q�x�a��c��e�>)�Cm�3��������د�A�{Zq���m�����v�=��CȪ�0#G���L��Yl��<��%Ů�C#��7J9A�8�3�p܂i�U0���K�"�Q	AK�u�X�5O<e�,����#҆����kϵB{8�0���X�j�r��[G��y�a�c��3�ڒb��!�W�<��;����_Tyn$���B������c��Nl"?����?�H��v����Ȅ��\�X��;���[�@���^�z"� $J���'w�*�c�ڣ��u��ܴ�=LUI��/C��Jjm��j=U<����S�rb��L����%�B �(?f['����[=�}�"XU��m�R��.�@։��w�2�:jZ�_�XH|��g��,��T�<�(<f�f��:}���\MI���s��M���@"ftq�%��M���l3AI�mBn��C�6���e<>ωw�Du����N��H���j1Vh�jp�{��l�o�YX�U�`�(�,?A���J'����/�O�q�>[5��{+��ڭ�Pb�9��9ͅ$��ؑq{'�a�:q���](PT��n�p��[����h���r����_\/����,~���g&�Dȳ������RlFK dpc��\�Ag��"~ӿR� ��D��߰�������`���\Q�u�7ȧu2@m�#�(�_��' 뙪wlP==󮶤؝"|8Ū)�6I+̡���*���*#�!�4 ��ǽ{K�l�6(�~�=��O�?��K�?M�Bl��DdגF��@T�_��U�_�z�)_�M%p���:�����jAZ^�t*a�b��b�C�[�r5$b*6��.R>P[�DƢ��Pmq����=�'w�Zq�C,c4�n�91Y�/n�fGDm�z����C����s�i��-����W���p�[��o�2+Lh��q��������L$�]_��~bt�ӊ�X���E�E{7ǒ�/�cۉ#O�6:ߤ�@4S�ff��!y��y��,Y�l�\2�-���+��6�����Y P���� ���%+�C�y��9���Z����c��#�Od;0r�~1�[�|�W�i����L�eW���3�}K��-[�A|��شŵ����DS.�U
���{Y ���]��ďt?���E������\k����@�q��
�f@㖈����A�����"�s��]�ӑ�S�-���˕[L����� �3@d|ED��>���B̉2�'w�zQ��q�mFl���	�jH8�b�ŝZ3�/x��?���	W�\jK��/_���2��o�0��q���~�Sd|���^e<�/��N��<��~b����]0`ʾf*?a�S��m����>N�LQ�Jhes-��ih�j�L�iAnIA�iq��m�h���C_޺���
`��\?�h�Ɗ:����=O]�H>Q�����%�K�u��N�G�pqH�3��sv��!%S��x��W�.��@�{��a�L��(ҧD����t���!�q�������8n���X��s�O��҉)H��K�d>�dT�)
������&/I�Xc
;/�+�[ jl�jh�}lm9�m��8�$Lg5�缁��h�T�"T[L�Wn%L����U� 2+�� ��ɝM��\ު���:(}�g|��u%�Q�#�L����	S�uVZ����*��`���N�z�۬�,�m�J�P�j�mZz��%���v�A=����O��lr���A'�o̬�7<`Vԣ�)v3�E�*���{&ХL/mL�6��U8��?8��I����M��@�{T�ȱ);�V�����3<)-��ͷ������pe�_>=q���ɝO���ZA�c�K��3�53�b@
`$-|;1�y���^x�# 4��Pf��'w@	��&q�cK�7�r9�Z��wp����jm�߅�	�o\���Y�X�?>�J� ����E��%-�Sk9�рS�7�0D"�e�l�\L5���;�]mI�#J�@�"�o��Q|�G�i�����M�kJ�t4aSq�wW�AZ��8�nI�3J�`�4�>(�4|,��w��w<���|��Z�AG��n%�ƻ�j��1���D~�r����f�������7-��?��(,2k� 3�_ca[�#��oW�oNd��Q�>(��U2ޚ�i��]pN�q՛:H�o�\8��Rx��9���N�|�ܒbǔ��)=½@��4?W.�P�uG`ݡ�t�s˿��c�g�&N4]�d<�G���9%|pJ�f I���4�,U-�рD�PƦ�vTY��t���نբ�D0�0�*�T�4V=U�>�s�5�8�=&)�/��5��֡�yb*�����N�?N��J�O�ƴu���X&F����Wm%ԗ�SP�i���9�`�ڟh��@�*�U�i���P\֞,լ\T����X��׃$^������n���?ѿ��ʝU��\)��M)�l�8��k�$[�O�*�����l)��4Z��~N��?T��J���݅^(!��5����8�B�Fw�Bzn[�~K�gC��/�,D��|W[R�>he5�i�u��S
���M�ӆ�\�ر	K�'��k�P�{t�����Ӊ�~�rǕ��+���Sg�:�u.<�6� ��/�&�oSӧBC}	��Ze�̻�u+�����3�(���aYh��9��3�w�~]��p�6 ��v�'��>�~m���8�%��t����V�rJw����o��.�JMs7,����&��X�N4��~��~i��2�?�U�cH �x6���
䊽��fe| �G6{�h[2����s��Cv`��3*��[z꽯�Bo�nd\$�s�JѦ��o����8�)<��B~�c����"�n��N�!A@�/�[� ��郂����RE��'�f�B@�����f���9�%�ˈ4^n%אH;W�o�D�Ƈ�~d��C�zC�^�4�w��E'��!�ف%r K�.A���JpC��G�@kA��7�t
L�X�@*�^��W�s�Nd ���X"�d(ڦ/�ؐiXʐ����ԫ�UN;�@>i=�V.��e��=���,�X���    i��t���y9 ̽k>k0�m`�����W�F˹�ԉ6�D�Cv`����)�V'��Km��Y0Fr+"�[mOU�dw���Q�����^N� A �������҂`��_��u��A4(��h0H5�cʒ�%˲Wƭ��{+��KN�5S��K�/-T��fX�E�@6����IIL"�o�y�r4g���9���wD�][ ���D˙IEA^Kc��}�'_� h�H*|��+�?����5�^�@?�׉���k����a9c�OiR.���,=X�tf����ĶEDa��b��ܿ���'V ���n9��r�'xį��,�RĨ!�`���*��5�@�@}Pa+-�5 ?�r"��--v���	�9�)%�MW��m���frq_@[u�J_Cv�4:�������ۉ�oi�[N䰜a'pz"���n!�&�3��$ic��·�hiy{�߽���-xK��r"��M���JE��E�QzJ���j*�M_m��[/@���w��V8�gV����D�I�<:�&��W5��d���Y\��K�.�����Z��2�* �vfYli�[N䰜z�a��##�c^B�C����3����i�������'�q!���D�)�7��;Rr�[����E��N@�����[�`�sI�ɖ*�x�D�>xK��r"��D��úwc���U}!3�ѽ_��m�V޷s�"��/�R�����b�5��-'rX���[�,2��NB�'���u��%K�L�w{5*R�wB��3����k�[N����v�Z�Zs��HgK~��ubr�V�R�<�uYdLm��˳z��۽Q���,1j�򢀹�.NU0�I%�;��i�Ft��鋖$,�e�.����>_!�V���D�	?������""䍶OU��%�LO�G�@�����:϶.މ���,r���a9ŴAgY1imH+���Mb�	��� �)�r�E������"�v���D�i���<�W����&�]��%�7R�c$�%P��������V�:q�B~#wˉ��m��t�h�����.ʉ �$+%�l�� ���{ T�}���P%~���b���a9�o�5����Ҡ8��Q|y��{o��QN�*ä[������_�y"nM#wˉ���54���%��<d�`T����D"v %|���[a�gm�y�(c~m#w��g��ˑ���:]_P-�݋��89�A�7�L�<k��S��h�@���L�JtK��r"��d���_Ekz�D�iw}�/�e���{��Ǟ�������Dm�ni�[N䰜rL��W)<�ǭ�AE5���=A��H���5e�vP��O�--vˉ�����.�מ1�4
ss^=W�MK�)���Q�Rc�}��t�7�W�د�_c�=-�ٕ��V��g�\�|*2���~�QǪVۍP|�GR��9ʡ[Z�9,'E$�$Xw�E::��ﭕ�$���+�o�	��+XS�D����0>���A���-'rXN8�SX-�^%����N
@����h���b�[���%�Q�>s�--vˉ�SҀ{py��7w���Ƽ\^��B�o`��s'f��)�3+pK��r"����N"��p��*�+xM�ƨɷ����[v$�`��7?s�A���-矹w������.w�����~��5I�Ԑ0.��|�,�e@��`��'�٢[Z�9,�Jo%�/0[�I���D�����x�������ۄS��&�ؖ��D��x��-�=��5�k�+�m���{jb�7�d�AfN:A;lK��r"��q�^�^�5,o��G ��y��2������l��2��ݸ7�'�[lK��r"��i���9����Hzh)���9k�+_s��X� j�R�� ����--vˉ��o�Q\��ڂ1�A�[ر]�ڶK�DH�Q�K��<�\ybY`[Z�9,�e��ý�����Zn5�����@�t�rt@�yG�;")Nl�ؖ��D˙������k9ϭ����5������יw�/>�@_�A�V9qc[Z�9,�W�|���K�\�����#�zc�`}4�������ц<>
,�3���b���a9+[p�u%-ԇ�A��Æ��Ij�iN7d����9=t���!ؖ��D�i�g�\�������E�"�Xt���f�8I�]^dsʞz�~�g�Ŗ��D�9���^+6�2l?��ͬ1��(<MQ3'�y���v�4׵"]\��bǷ��-'rXΫ�]�����+n�ۧ�e;)â�K��ߑ�iM\z��\,����--vˉ�S�D��=530�ѽ�f�I�I��	Bє<��Ӑ���i�[.�yW[Z�9,'ƽ�����O�5�{��D�\��%jh��ʞ�x}���_'� ��[Z�9,g�����޸��C)���
 �Y�;;N�Yq�F������y^X�L3�--vˉ��7u6hQ�&Uy���2>�T���M�jPD��yXY���-;����iqXN4V9��� ��kWmXF|��"{<�V�p��҄)��� Y����--vˉ�Sc9��5wl{}a�5��g3N���[� �bm4�gn�ݸ7?��s��P��b���a9?�c�>���GZuq����j�E$�+�N8�LZ�܉���b���a9���to0}��ﭦ�j����qk2��.,�&��*�y"-�--vˉ���$09��B-24�7���X����X�
|�V#�q�@p��ҝ����n9�?�3h�1�HL�ܚ׆mMvߩ5���P��y/J�<u<E��u��+bK��r"��ݻ8�`*�Pc��������B����]��ڬ�]�v~���E?s�--vˉ���S�Q���} f�4�N0��G��I����I�۔�I&M��=��bK��r"��t��u6�p����l����G�;��ZI�꺁�"*HZ9LĖ��D�مpi��v�De��
��	͘�9��D� u�l���d�H�$ٝX�Ė��D�XN
Sڤ��m�a_��9#�r�j^��7�L���:[�����D�]�iqXNϧ��U�D����˃Ϡ��h��S�Y���:�Z��~Eli�[N䰜���q+.�\�0O:���J���S�/h��|p%U\�9�"��N��B�--vˉ�3��oh&�k9LN� yR��E�%LI;�ߴQ��5�o4��	Ǆ[Z�9,��s�R��̘4��|%�>U��(k��%Ĩ����7il��>Qr�[Z�9,'��L{��U�-�T#��YJI�œy�W��*�Rt��̥���-'rXN��1S�@�P��X#R?f��	��vB�����^7ZF��ďt���D��W�mz�;�ǵU��l��a����Z���}n�3����zQg��[Z�9,g_�6),����e�� %T�|I���C�����;���l��':l"���D�)!
˷���3�<Zy
�$FKo�4�-} 3�,�U�m���|W[Z�9,g&}��Gu9���+q���^^Z�i�����J��-�ܕ��憞8��[Z�9,g�/����lj�q��1�Y����k�j��< en���R��3�g�����iqXN�~��ڣ��<}^�GQ'\��j?8����)��l8�3���T�--vˉ��oj-�B��)�PzQ8ΠI��r�Q�G6\U���C��aO$;���n9��r��Ǜ昼�g��%�Q����~��ݿ̅j�#?a���k��D�U���b���a9�hr*��}�-|�˘����N�؇�[�iH���Q�������--vˉ��	��\�5ѡ}�]�UכwB4�S�rÃ�A'Q�黄p�w�e��>󮶴�-'rXN�Θ��D6^��&��{��@*�Id�
h��8�6�dE뙿�P[Z�9,'���1L�/��S}�G�۪���nNn��v�����R�4�X�:Q[P[Z�9,g������~yr���|� Ll�E���cSjP������i�M=3jK��r"��|)M.=�ޒo��^R��{x������5�~##
e��%�d�3���b���    a9�m��l� I�GK�W��u�7�T�4�}�_��r`y�/�b[!w�R[Z�9,�2�\�Z�>1�`�*�o���{�<J�ӤR.���*q�h9���2�����Ⱌߒʓu~���uh��rI?����$�B�U����5�{��D�--vˉ�Sz◸2�Aތ������20��jJ+��5�9���H�k*������-'rXND�B���8 F8?���]3oz�^�w${�#Yw�L˽F������n9��rj`~���eu��ޭ��8�<ni�`�����kNA� ���E��n�--vˉ�S��Y@�5xτ��[����?.3 (�+����q����D�gj��Z��?t��"�o�����j ��w��L_#Y��g����b���a9�Ｐ��\9���&�mЬP�M��c�6��7�ELU�'6|zK��r"�弹Jk�Zĭ��1���/�;F2��5�����8�~!��R'���b��ȟ6���k����t)&��'�}���*�K��-�4 K����{�?����-'rX�|T�dE9�'��p��}vϔ$s��$�I���%�H��)���n9��r�IɷN�����Ɉ�gN by'���պ�<����n����F��@�7xoO��r�	RqDW��̬�z`­�<<y��܂�.�z+غ�+Th��F���=,g�Y~�]W)�`²��v̹��G�
<H0�(V���r$��� ��[N����	<E��ĘB��M�`��˹�c;������cX�(�RqY��Yu��-'zX�\v;ϝ��M� ��r�#<�C镍HC@����v`y�"�6ŉ���������4jo[G�A����n�Q7��/ ��|[@��G��W������
���n9��r�S��b^��曨���V��J4Yp���G�*�r��<
����r���>������=	��B2;�I��	�[i�ʘQeF���4�߻0
���r����@�IG�h�����7p-J�E��E5uG�0����˅���n8(D�Cwˉ��FCrp^�*%g��7��t�^J`EU���6쩖X��	�>�ة�n9�?s��WM�+�Z�x�j:��P�5¦*_���-MWs�y83<����=,'�\LB����E*T��?�Qu�����̒�^a�d��.�Ư.�ԁ�[Z�=,g��M�ƿ�а�$B�p��FQT�A�Oc��o�^�iA�����'�B�ߨ�=-��*_׿���w��ε0D�ZB4X���6�~�j}]��N�S�PxK��r���|%_�mҚ�~�&�2�`&B\dnݜ"�x5�m����=K��_9QxK��r���DrMq���ϩ
be��C�ݔ��F�_��d�.���2���ioi�[N����uv�1�`e`)�*Ruޣ��gU�j�/�>��KU>�1(��-�--vˉ�3s���A�MYߪ}���;�l����Nŕ������`�PG��Y[Z�=,g)���W�UyZ;i4j�pR���u��}í"[=��:����b��b���a9��S��t2]�*�m&�U�X4�M��RM��s%���≸����-'zXN&���B�+կ�zM�#�j��?]sU����t༞`O�6A�g>�--vˉ���vwyՐ^� �P��վ����M��9�ǅ�|˺��t���f�X�Ȗ��D��M^g@�sB~��g������`�� ��RW_t��7�c7�=1���b���a9��-������EÍ�yYxe�' �G�!b`E�Aϋ��w��F��iqXNI'?s��I6��wL�������YQ ��@XlLq�s��'z~�Ȗ��D˹�ǒt\�ӝ��|���ڸ���<I��0��!^+o�)��&$�|W[Z��3�1K���M"

J�#�F�1�P�@�)s8#�LeYq���3�jK��r����Bأ�Ai��^�D�Cf\����K�jmkv5�OΉ�QdK��r��嬵o���(UWoEn�y�b1�p�%,���&�0XY��R9����b���a9������hk^#��Ƀ��L���4�����,x���B�&s&-�--vˉ���
����i!2)t�:؀������Q:���0��bӴ_�+��(���n9��r6��!�H�p��%�5�z��ڐ|��F�|�1��h9���08Q�����=,�����;]oʳ�zE�T��1�mjL?PA'�멓�E|m`��w��[N���˺\���9�EE��SZ���B^Kr.�;��s�@���n�83�{K��r���絋�+]�s��;���!��20
�z��z/QD�l%�����ub�����8,������*ź��/��@��CeB�v#�VS~ؘO�׾~96މ�7|���a9� �WdTc�|�I�>I��0�)[�[�[U_�����hU9�̻��b���a9',i^X1}�O��H�F�����b#�Hsf�-�n;�.��M�gV����D�|=1M��̙y�`��uZ�̏o뱭8/8|R�h&.��z�����-'zXNbi�hD�����!5ܷ���#�ə�xXrǺoCZ"D�DZ����=,���?X�c BB5�I\����"ׯ��K2FX���S�o|
0��7|���a9i� �/`���m�����u8�" ���K�@!Av�y�<�'>@lK��r���\���`��Z�bA4�.\�8��Ѿ(��$�4$�/��2�qℏmi�[N�����z�4��/�Y�u�P��?/9��&��ѼA{m��s�PlK��r���tP�5���F�,$)#\���������n�cM����=P��Oz��Ĳ����-'zX���I�~JT�ѹU��w��/�8OzG'Wτ����7����i�mi�[N����=�؍ج@S0�I�Jr�0}gL� s�@��)���#~!�[��0���n9��r*\�/X��5.�Brpߟ*���uh����%kD(^F�C�HvlK��r���x�q�G�F2<���`į�?���hsyL�\\��B#����Ԗ��D��b�P`�}�� ]���{2�Q����0vw�Ň9$S���w-p��ƶ��-'zX�K�Pp��W	z"e�;�Ē@��1 4��+�ifZ�6�p�MIѦ��
��b���a9?v ]:�>+=\�9��2M�b��G��H?��)of����@nЍ�N��oi�[N�����#]
ilCi;�Ȭ�Ԩ��`y�w�����SB<^�L���~�oi�[N���J.�U���7�B_B�nFm)2#���i�"� V�X��^��Ĳ����-'zXΗ�0�P7>�q^� ��Lr4X�ly~Kް��4-���X]�����[Z�=,'rO���&�{�<}���m%��.a�~u��|/���D󎖄'�����D˩��c[�@��+55�D�Qk�Cڒ4ߛ�'�k�*���f�q"��--vˉ�״m�֯i��9�:��h`�XH~D��XF�z���<퀘�g�Ŗ��D�	�\�*���_�_�w��*�ԕo���*׾���nỉmP|K��r���Իϗ��
�ǋ?ޟc�(���v6Մ*�*��b0���
-����--vˉ��~pM]cX��0`ziH��@�C��pa��wk�J_�w8j��Dm�oi�[N��� {/���dѱ�����=vL�O@�B[�����1����Ol"Ė��Dˉa��h߄š?n6)��dP�PM���4p[�>�[lv��HO�Hli�[N����*?a���S���GG4��ś�6R�z�'�~��P`\��bK��r����m�ii��ո� �
Q�Y�����N� |e��@�J��'�+bK��r�戛j{ ��o�+"�I���͠q�F��� 	����|�[Z�=,g\�H�� �t�kٕP�iP�2{}��m���%7e�pݟ�3?~[Z�=,�B"�㇝���
8r+��c;S*c�6*�a�Z���    �3�bK��r���t!]�O���ׂ���9���deȜ����y���գ3W���=Q�[Z�=,'.k/�-�`���uA����N��p��E��e�V܎�|<LĖ��D�)p�r�="����	i�̉�Q�^pM>
+_�%c������D[I���b���a9�nI�#����*��;G�t�(��d7��p��τoI���˂��b���a9�������21>��#�����,��D ��6NtED�--vˉ����5�e�񯧞^��Q��ݻ|�;��6�=ʁ��u��c�b~�$���-'zXΙ�8_�}2�pԫ$���!2���V��)T� '!e��b�X���D�9 E�N��B�b]D�!}O*�.�����Ufn�Yٌ_�)����--vˉ�3�/Ӏ'��i'�`0�e��RAQ�&/��L��_�|��z"��--vˉ�S���&Qo-L�R!��=���m?S�� ��.I�<�/�܉�(���n9��r���"�܇��qY���T�1C���l*g�?���N=��t�R[Z�=,' A���/��U7���nI�/�i�'Z �r0���#2��!(���n9��r��^_��[ ��I�M� ���ᮀ`7�� jrX
�`�3�Ֆ��D˹m���=�z)h
�^���cd�l$���zP(X5������mЦ���b����-'�g��f������:���HRg,l��	�@�N����}_%�:��Ԗ��D�� �(��r��.
d�*>�A4�A#�����/"��fs�NL@���r���|M���@�3ʽk��M�zﾧ>(ۜ����e��	��G�N�@jK��r���$��e�>��ok롾ߌ� SS�0�9I���;V�p)�ܡ�D�Dmi�[N����Q��^��iYY�`�Οh�&��� �ҕ��h@g���n9��rƙeI�{8��;�eG��]��f��"�72�fx��{�?�wb�g�r���ػ�|3��*<0�'����ħ-�S�\'�����'>��@uKoj�c9��$b�w�����+DC2S�%�K`ƃg�ݱ�缰�5:p<Noj�c9��<��Jx�;�|���4J�N?��Iԝ�.�2+t~�b�#"��>�D�M-~,'���C8K���ԭ�4?���Y��B?O����.�}K�Y��)H\��ӛZ�XN|g9a���u�^9T���$�Ku:0yxV��>�M�����I�L�3�Z�XN|g9��F[�b����Ԡ�k�i�x�Rw��=��+���(���@��lj�c9����}�TC�>���u�uꁲϜ�l�C�!���cd�r�ٙM-~,'���Y�0�)�C�����Ŭ1XDL����v�[�Nߏ�L�V��w} �0�Z�XN|g9Xq�j�b�G����Ra�$��	�AJ-����y�.#]M�eg6������r��<X�Wޭ~� >�k�@Ug�����P��?�j�8$�Ƥ�ܑ=0E�lj�c9��d\���5�B+`�o_CNk���.';=TU�N�.̠
*4�G�0��ŏ��w���Ȅ!F6�J&ô���#����:TwBBʤ�
65�j��g̦?��YN�L�k0��PFM4Vlb%:��X�}�3;Jٛ0�A�N����d6������r���瑡���g5���5���F�;����U�>m�%�&k�z�-6������rreE�A�T��_�c؂qI������t�)auy+�0�:�?��wO߿���XN���o��޹Gv��o�{��ML#Z��S<_)�d�����kknn������`g9) ���/1[8�[�)"(9������%�ͬ+�jMR�w��XN����x����)�ۿ�X�JO�^o���uo������sD���Vy`��V ����	v�S�U�u�n�4eִm���F�Le<N���7$�'���b	�@���4@����w�S�r>��������X�.�����n�z��=PN�������o����?��r���d�v`�!őBB�s�h&� �%vo�@j�%LL�1����;�B�?��,看����( V�Ԋ1jK�Uƛ�X����r>����){��X ��XN�����s�T��E�tG����-3uq��K�u�Ɲ��)U{��u8���@�����;���<��$���3�����vѻ��+1��8��x�pK�m郛X�����XN���1`�1����f���ٺ�'<������פ<�fM�]��8�G^ ��ŏ�;��x?B�t���]J*��d(������G%�&�3���G/	x*tS��	v��G��j'��~���fŬ|9�h��7"��#��{�ӌ�O�D$�����r���L�",kXY{g�(v��ZnI�
'WW3Y��t/�4:�]�l��?��,g������Q��1��)&���nV-�}�TΗoU|^��8T�#/pS��	v�SΐP���&�i���ڰ����%��=(m�!�1q<�YxZ^��ׁ ����r���|�b9X��O��z���dQ�9�j�(�W�hd/�wK�޿kv�nj�c9��r�0�W���^X�Ak���|�'U�+�T�(��K���h���λ�?��,�V�T���6ت.�r�[�	)8:ݘz��CH+}�k���#�.� �?��,'0��^��U@<��Z6ܥd
2{Ą��]�.�KVF�2�: L�?��,g�x���w��[:Y�!���vn�Y��0oދ�ܢ�s�l[f��� lS��	v��3��ʑ_����w�R�R��7�f�����B#�k�g��fئ?��,gU��!NjC\^�7۶�-L��5i�LZ������~m{kzZO�ϳlS��	v���]��LhP���wr�]}�Ps.d�g��+w&�;�;p~ �6����`g9�'���Ѡ�dѢU�,��::2]f��N�ec���[� ��ŏ����ӆ�g$��^���ϙU%�ף ��d�{[%��n���@lS��	v�����RY�KH(�f�hȯT9/����5�3��Zb���S���M-~,'�Y� f���*g��.���iv*��|�����̦5x�P���ɋ8�8��Z�XN���e��=5-����X
�e8�C����x�όA�����r�K82Ԧ?��,'ԿT��?Z,�3Rz���X�ŸY��a�S�FybY֋��� ����r����pQ�뵍?�(Us��Yf��5��$��/���"�s��=�C����[� K�����ҭ���F0�ɹ�X�<�w�=�.�����g�n��?pj����`,��|#����S]�!�.�iVLj���kx_�<
ǀ�V�����;`Y�JZmo��:}���φ�ڪ�b^�.P0v"�����.7��B�X���Y+��{�R�s/�y�qX��C��Y��X�9Q6��@]9�{B�[� K��&k������f�ct��:��ubN�v�tan�8��l��Cm!�,�X��%jٙ^�'r�����O?���M��êWW�5Ջ^<?hZ���@�o!�,�X���J��sھ׳:�&WLN�sW���N`�h�nd?�:����ȷ���G=��z,�8]l=�8*إZ�S�qt?��V�QxNd6�k�j�a3)�g[�Q��/걾�Ut�Y������,��>���M�����rz<m��X�p����z;���T�����;Uůߺ����z���~X����)���`����Q�`�{�?E�ʧ$�p7e���']ΎD�f���(4wD~ &�F����z;��YYX��ҋgΜW�>��*�<�:�Z��[s<	�_�<�-}�n!���N=�E�$��\J�N5��	�B--S�W�|� /t&\o���Ƒ����"�E�(��yi@�z��fg��A�b�dW�E�Uv#Y��	*��`/�@�����`G�*v3&�D�c�Vq�    �n�����J���f;��d7s����Z"�o`K�?�("��LrLf��3{���d���]��e���~��J��8w�,e�gBl!�C��"����v�fx�rAuB��4��x[Y������ɍ��z��h��-�("�QDqz#�_�0�> �	�����M��!�kOj4'7=:�{�[�PD�����y%��4n��������8HZ�Ʈ��� /�W]{F>�4���?�(�h��|6<)L.#S����P��k�o��ゕ�����)�^U@l!�C��"N���֜��/�9���:R�Ek�.��nޏ&�g�v7��&b��v�Of�`~��Oh�!<#����5&���#bs�����y�[���`G�-��q���b����k�F��Ę˘csM�aPD�Ց-#��P�Z�PD���փ���d���e�3���!f�z��,�7+yr �6�Y����"�E܄�j�g�i��(��3�ü>�ƽ�mJ�'%��w��LW����Z�PD���nj�
y���������uN9X��G����d
7#c'��GBxS��v�Μ��^"��,z�����o)uƢ��)��ƺ�l[��`v�K�I��yrS��vѦ'UO�V�G<�(t�i�������HйE�
:�zQ3f������E;�X��D�&2k�ڄF�5ֻYo����O<������x������ ���E;�(|���m�R�M������g�wO֘Qs��N^J)lQy��Z�PD���H��"s�C�L;�
����3�W��.��E�ށ�/ˋ�mX~!7����`G{�uFϱsW������� !��i��+珂���=��&ׯ��&7����`G��|]>�i|�[$�o_�Y5�I��/���dHf^J���wLܵ&�H��?�("�����E�'W���7���Z2���t�D�� Oݤ�[��$�y�nj�C��"Bo˹xK��K��H\�n�+�B�@�����W��;k��d����M-~("�Q�uO�0y�s.X���:�j�ݒ�n�_�M��!4A�$��(n`�?�(�^1<X��kR�X��ة����].��GE�������˻q�S$Ԧ?�(bRyq�\}hLI^� �[�qS�:xm�}�\J$����ՊkjS��v�	�ڌzO��ϵ��6Ձ�X&�.����q��7���IQ��/����"�E�<ˡQ�Rl���z��׿˗	�4e;�	ep�-��
w�b�6����`Gӧ�QGx޽�����������_�M��)��0�y�@\Q�Z�PD���<w�J�x�I��=���Y����IR2o���*(x�vr;R�R�Z�PD������t�Cs^Z��:�&[|��w.����:	��Q��j��ؖhP�M-~("�Qľ�{���u��}#��������7S�~o�"�\�W�����6����`G�Z�^�
t��a#��{XYi�ĭK��2���J�Hn�������E;��ؗ�v91��
s����DP>�\�r����X6w�́n�g��	����"�Et���j�*�Fn�j�2BNkf̉�F�D�ü�qSX�IM��@uKoj�C��"v�	�p
'pi������'I;���Y�ʫq:]:�k���&bz=PқZ�PD���>����%Q�R�k�5BV��/���dx�i>����f�<�\�w`�e�~("�QD7) 섮F��z�OX6�i���,�/��C"� ihlu�'��]���E;����F7H�Pޑ�&C��c��o��X!�� -�2�T�i�9x8p�/�7����`G��J�����*i���ܴ��Z�Q/����Ry�G�Iu��&jS��v1>{�u��u����Ćՙ,i?���ٙ�P��+ƌ>�~$������"�E��'�Hy9]߽�&<����	�?ىq�M�{/U���lk!@�ꡧ���"�E4�z���Y���)[�3��d��p{H�[zT#s��
��ʅ�$p`�M-~("�QD����g��
�]���T�3�h��dp��A{���w�O��|�=�����A*��+q�&�`�9��S%'.�p)������o:���b%���(��k�=*��AÄ"�I��������ϧ�Gt��L���z	�����a��=���R�#�mm��A,'0}�}�,
�'1-�"�+?����8?���?_�2��@���r�w��b���B����:9��R~cл�1�g�K����|�H��� �'+�<�yT�M��k4[<Zj��z�\L_*��Z^;q��B�aG��k�=�0u����BM;�|]ҽ4�h1�����5]�����9g�>v����??��G�η>��2��&�ڀ�Q�+ '�ǋzg�������o(��f�������b3
ߞo_����J�j+�N��@P�`�--� B�5X_��-VR�Z�@*�ߚN ���(����S���زb5ׅ�sA/'��X��nB���1�8�j� �8xn�Xr��q��|m�K_9.�nˇ�ubq荞T|]y��F\�e��3���V�V?��������";�[1#k�%����ц��L��6 ��YѽHø�W����y�f�(���ajgC̜
M���v�S+�)��LE�E������)�2���AS���f�{d�}^?uu���Ъ���$)�4�z���y��~��v�X��GV�N����Hn��ݶI`8�3F2e0`$�ڼ�[��%�I�*�\�D�ؙL�D֧���R�z_�������p���P�\5�L$r����S�?Wm�иܫ��U���AU%S'N���;��?_�0�B�3xDO}��-������Ό�&Z�\T3ꗮ���V2�*ǵ<dk��g��-�s��gf�����R���wYA.�s�9�o�U�g��6O��}C1x�	�]�[��5�D�_=�=��q��n�W���A���-�E���ڴ��<7N�VOs&
�w�El��׶@�^��,��E�l����SCփ� �P�*ع9w֪<{}]l��!��i>�Oe8WrI�6N8S_ś�Ϻ���&�w~�����G����4}������>l��kB���eO0W@���2-���r���~�g�X3?VH�ws|z�7Gcv�A�N�kFQ̾.���-g�u�f-����uu���8���
 Z�-����j�;g[Q�3
P��(2�:��5O�4�7�]6���f&ѵ~�˺�r�Oj-����=�J0�\mF�E�m2��2�*�uw���*ᰞ���0W�vPg!�]0����翿�9��(�F�@�dN#e�4$z��V3�7>����o+X�zP��ﻋ�Ǩ���|�$��k��m��/���I�|��4r�YnI5��gF^r�sV��br��44�$�����
鿂�%���`u������u��گ����V'dk7W������w=��򆤰?_� ��;�	ݴ&���mЧ�Ė�T;:��0�S�d�yҠ�zEd/^�ۿ��t�k п�7B+mQ�M�E��wb�e�gU�y�����)(���=�^Z7����䦋_� �����xJ>�s��;��A��%O�4����=]Ǻ7'��"���>O��+r�N_ۼqS��ĈO6�ʱ�Y��4Oϒ����/>����*�\k���@���i�($r���m���e7��(�s:�H�����G7��B��FK��u�� MWo�=�&�~�#�_;�΃1t��Vi
$�7���ξę&��paR���� �ڛi>�ZH}K�߁������kĿ|.�x�x1y5���n����K�>�7���x]	aM!��}cԷ���C!��<��7��_5���6��&���I�R:~n%u�1`H.� �<��d�'�|mK1����A��2ʹ����q�0 JH��r��S�Yl�V�'�n<�:e��֡��i��)��\�B�r)���H�-��CxFa�h��J]��KG���B9տ]�    "�#uI�{>����K�Q����1��[�)�޽:/�=/�rs����]Tm�^���-ƾ�D�=�{�&@~;3�T���(@��� ����,���J�J�u�?R;G���3�{��~���Cp� �\�X��-�I�?7"���9�� .j:���'�gj�˾Il<��u:~/2�uL��I<%�Z���o2؉�(�������N�!qui�K��*��pjk���e[Rc*��� �J�6?f�Z����iyQv��˾�lܛol2��+��U���}�v�G?�!%D[z�.]خ�K��as>�������.o7��e:� �.~l�����Yw+A.����?
�����F��g�<A|`��ie�_��x�{��
ԯ�������7�J\���!�Cյ�ֆV��� ���ftjK�F�<�V-�������$�u+�(5�������+���Е/@��a/��hLSf_��ћ8~��oE�_���{*J��/�"h��-��'�2�Ġ뉑��B��/�)�%-	V%�ܷB}���T�wue��Ȇ���fx����s����y:�� B��Yq.+�V͵�1����ٞ�k���#;�����V�M��<h��i��%wP�E���|x9��6#�ٓ�|�{u�V��k����A3��SOW'	�Ю <'��ʯlbҮ{���>�< !�&���B�}��\��W7��W�{aʪi�E�W���;�l+�$/���{é�Ol���k��H���P�=?�-�2+����e�Ӱ5fUy��r�R�^�T��'��N�3�w�JmU�϶֓�i^���k#�V�g�T�t���5��a֣��d�l螁">� ���_�6�����01�����`L"��r�ʫ��:W��У5v�=���#{H�ہ�`K�_�(���T���:��z���Lv�׬
ǝ��)��kڢc�L�%A��޸�o_#6uDv�]�*ўE�ؚ/����| <�}�$F
/d[{O�!C��&�{ {2[~a�{\I�+�Q�d8C�$��D6���RY^�����>�cQ~��'�Z�g
x����=�����xM4�VR^���*�7�	�4�������~�e�/���t�1���-��������]�-M�7 ��S^�c1���詧�	��ce�|W�RXҁ�قz�M?Pd�!s+z,#��P�9�xC���Go��w)��M�2.����1�{��b���F��^?�=�ǻ=C	�lF��2���b�F��H	A=�=����z�a_�|�&v�1L���fWwWxL�N���ֹ@�W���qm�ˊ��!=B���S������� �k������U5��Չ�{�R��q�B��M>K�I2 ךF�2�md��f�SI�pL��T 9�'W�|�OgZ����G�X6���]�9�nڔ�����+�4����&�4���!�� �VKbӛ��WՖ9�pո�	\�,��#�GhTr����F�?4�8��&E�,�&�<��<Q,R����dK�%T7,�?�1�\��	煽�@Fo���p���:Q�֘ ���"�\��E��� �5p�s���{���+��<5?�ځ�idS���6$C�Aݠ,2�����l�ƛ�*:��`�}_o�]Ռ�4E�&�|:�K�F�?_#��k{rkd�J���U���F�S����8S�]?�k�,��ks(:w�(�U�2��&W���e#Z%<�f�r��"P5+5*���8QeX��q~Kّf�U�$�9��������ˠ��I�D���?>���)�j&}Y)�<>�0>Ѧp�͗�t��ϧU�~��6��捛Q8�2;m�S�b/2uf� /�j|@��K������1�̅��;���AM��7nF��P�,��r��'��9G`�L�yOt\�*h���g��<̔W�g��#�|���ƿV���)q�����.�aNY�B�%]&���wq"aw5z=�V�Y�Kf���������ϖ��`�ˣES��nƨ\+>�s!u�'0Z��TZݍČx�0�ư?_C1Ǳ}Zn*�N�UQ}���E�bx+R��(�� Xܭ��ۋ��	&FP��r4��G�{�0��u�4�\{���(x4}�ߩ�&��bAӸuERZ�5��g	������ֵ�[��ց�kYN�|`�9oڊ�x����8��{�J���=��I<[��#�5�����n�³�2�h�Y�cs���:Z���/��/�>-��`�� �I���X�[��5����5Սy�{
9��&��p�d%�(_iCB��8�W�V��VّV�tw�"��~۪d?,��]�>�Ǘ3R��!���C@"x�%ˍ^�d6��v)H��|�B���oA��w1毫�#��`)��4�e/����n���q���Cvn&OM������82�h������F����d*�R/�\N'KUg��U�N&Qc���5�o���Ϻl����w��~l`Ռ�޽�`�����S6�
2�$�8��v�K6���c�$|3�y�f[�J�;�������"�>\¡1ԜJ�������hRH��>�Xl[V~dѓ�7��72(���ӃK�D�2���,-|\�ì�rK�o}���W�Ҵ����:{����M��w}!�}6�M.�h_p�x+#��ȃ��B]U`�v��^���k0W��M��^�7ՠ�������_f��ς{q��yr&��+���'4PBr��&��K�CO�i#���}��1�;�LO��|'[����ؤ��%��2�gN���j�z��Q܃7�&�_#��.�_#:QL;��O�/���SU���!M"3�ô�\e�
��rN��G��l���°��~Q�"���H�����!\�����E������1�aCt<���'6�'~e(�g�"����:3FFn�eǎ����u�ˑ��OL,�C�� \�90�B��ߵ��k���\
�>tL�Ϻ�ؕ �r_�Hlb�x[�c�ba\�Ǚ\�݁R��\����f(z����P�Y�4�Ձlq�l_̢r4u�m���m�Ӑ�4����������@�啮����Ҽ���ը�>�OM���b=Ӆ�����A��ā��T3
E��ִJ�s��-��ֵk���=(��>Z�y�u"�Ab+�&�����F�_Ds��"�O7����J� ���Yҧч���>DOE��V�K��lT�wO��N_�2eľ>R���6c6N��L̔�9��>�J�]��H2)ɽR4�7κ�Ɓ�h�m������	R�#,�3tRٵq[��U��� �R_Ȣ��!#9���m�Kzd�-���[�FvlBv��
��+��Xf�P�#��Y{�."?��ˤsTMŝ��@��<�k�5&b?���g�ݼ=��l�N])�U�4�G�E�O��g/)�n�re���_d�|����zZa>zs���K��֐����DAo,I6��>�R�"�[�>�~'6�됀�״�Q}����u����^���g���k1$�Ȗ��I+�r:�$��A��$�C�1������Ŝ�B-ٔO���É����	��`��`�Bn
�]�ܚ\f�$��t�~���zqV���P����k�G�Gio��(Upg�S�������o0/�S�H%˳�� +�=K��{j��DI�\�t�^��q�>� ����+=�������n_�v`WH�i�jg�%�>�~�r)�&|����t<�t1[���� ��<�d�pz��	_�c�c#mS���&������?���0Tv/�Ϥ~��F�����/F��ƛ+�fQ��^�I4���N|B�L��'VD�7~���o)�}߳�����w�g���6�!�Z�"9�bM��~f��_���#޸)�#(tO1�ru/Q�$n���.wRQB�Мl?��G��YC��/�>D����_��c���Ϙ.��
�B����+j��!�/����z&�
DQ�x�6�޾�׶����*�ݭn"l�Q�k�q����a�#�S�5q�F������C�m�����k�;��n��7[�{��    "��������GKΐ´�94�g��D���a�%7��ȓm
�=�Eɿ
 X�h�g�����;Y����
��Q���l �J2q�Mn_������~C���J���:����2�S��D��V;��b�߈|��.Q�V ʂ���ڷ"f�of|�f�I�4*
v�y��p3��S�3�nӪ�%ջՈ�ʭ�CmA�=O����f��l���P�7�֢�C��;�Ὴ�,��[�Sꜛ�}y\���l}�׈�"#����y�칷��Hs����r!���%ê����?Rf����F@�ځRn{u_�I
���м�r��`�R�d%h����q*�.�U�.�v�MIƋ���<�b�@�����>�sW�y�߭)��}@�ix<H�̊D�{���65T�*��39���.�ނ�{�>�P�㗥C�Gm�}�
�kҘ�y^m��Y��})����!�}���?��l����3���e&�cM�&���n�J$CR*H�ͯ����&�2��ȧ�\�~��3z��-�#���V�8
b-�<����-��f�셟bҜ�<(rWe�7nB����ނ��M'!��B�wR/��P5K����?t)x�l�g��B���0�!]3��-�f�R�k8�l}�r�޵!bqDf$�'Sp0H�5Qb}Jtb|B���G�_Ɨ�`��zizS��r?�=	�����������oi�R�	��4Uc�3D`� �5���TC��������k�����<�s�9w[)���.{ �E�o���ԋ_��>�ʛ�r�%���2���>���7�{���iy��%z�@�lmD�ovM�=��n2b"j
;P]m��׶"��v�(��wc���:� ��h혗Op��4�@��C�3<}d�����
���3<##��E�����7�dz��-�S��y�]Vz��Yќ�@)�lq���� �~�Og�`�\]4E>�[m?w�M��"Y#�{�$���*"��� M��2�����}^t?�'V��P�[���ﯩ٪~���+7	�p�D
5�	�����������u�=�^�t\��o]H�uK���"�F�6�Z��:cT���%S�Gds}�{U�=Ũ��Y��Ѻ��u6O�X��0p}�D8Xs?��LC���U��)=� ̖<�ۋS�y����ltqs��� e�����=�t�J����p��?lK�_� ��ː�"��*>�3:Ғ�51+.Ç�`SX��+�;����t�$~YIN�V}A�0�c���r�!AK�-Ny*�L�%:��x0�'��ɐ��<F\��x�*-�w� 蟯�_���X8�l���<\����z$WkY�����jNZk�Q&�x썯�s=�h؟�Q8	�:�+Q�4j�'�[�W�ϛ�z� �� ���~ �҃f�C��y�d���5���_՟��j�h�ߋ85�5��`,��O^�'��xQ���!�l�k���}ba�r �}�Ir%�Eq�X����b���f��z{�4������M�(fߣ�@}�(�E�N��
[�j7	�z�'=���7�7����d��ȓ����[��!n�))���s�?��{����mE����X��ˋ�+��c�*Ə8��kAl����a�����Hr+�ȸCR���<�������R��S7�&�N�B������^�#��C�����nƝ�,���'�rC�����fw��Ѥ�H�1�FR���4�U?�ܣR���WU�GH{Wʩ5y����3��̉�u�a�g�����5
$���(O���,QC !��cꜙ>�iA�p�h�Um���/'j��i��ُ٪��}�93�P֧ˉX�o�`T�$�n)gdj�"�q3��l����T��wC����5�q@�G/�A�xoZ�-_�rҘ�U􇈄@H�x}vf�vE%��L+��t��6A����uu���"mׇ�YMf���q?����xJVܤh����ԁ�F�������g���y��+��(-.u?�e���zr�6��e;+X��V����e��@d�����.���2��Ո�9-j��Ð�%�9\V�{�^�#�?�`N��c����		�ے@�R��s������=7�J�����e�o[Rx��-�򗺜�׫磊�A7%��.�a����qI�VT�v��XUo�N�>=���I!���{�Z�3�w��&�_#(�Dv�L�A�F���y�}�����JK_D��אvKE�:���s��*s	;��Mm�񻃊��/���xa��:s���IUT@u���"<tn��N�p%�0ו�����8_�A�]M�N�4�}fؠ��;�R�,c��/�T��g*�]5Δ1��������@�˺��+��,��I������}��$F�鄥U�s�t��tنd%2��3s�Pf�*�_s����4%o��[�h������KC��bNt��l�t|j#��p��,�5����~��UkO)��vl��X�<������@ɜAU�w+]��t�QY45��7�\�{���L�8O=4�:'�JZDB��i��H'3���D>Y�������k�8���M6۟�|�ڪ��Ηʹx���k��G���
A�Ҵ��3-A�a�"k珙�;M(��t@��^�k_��)�ϰ�^S�0��=��s�>���H�B`h��{��h	^p��݂2[��5�@bC8�;�O����������IS:�4�SR��0������O������&���߲g���)���;J�cO�in$5����9_"J^`sI\���ӣ�#m>��f����)��<�UO��y��̳�o�U-����^g�P�a��f��XP�9���d���m�Dt�����?�6�ٝ����л����~�N�ݣ�`�,S��c��@��l�싛�(��k���h?d�E����%������PPW��~qGů��R����d����"��������p���+�\=�d�Zː<�
xkZnSm��G���� ����Q�:zϚ7���TYI|��6��$�Q�;��H٣|t��s*5�D�D����큾��!�#��"�5�
Ebr��hwfh!�$Ǟ��kЁ�;���Kx��M-¿�������y$��X�꤉��Y����ō�4��/�12}��.kI�6$�Ne��r���o#}!�]^F����4�{������c?�'��R��%����n�<.ȉ�>$Ņ|�o7�F����� 0��+ޮ�3oEoK�AJ0�+L@ex��Op	>}&�B�V5�ru�w��[�Q�wI�=�廭�d-��$,�{�&O�A�,�)�ޣ�����M#�Z��[`�A�����wx���� 4�Oҩ�Q��D>ù��s�a��D��ٕJq�A���`놗�	8�k��+����"̜�$k���,p8�pAt2,�=9�ߧ��"r�]F��A[z��=��u�[���Y/�KiC2�F?���&�K�-�/�$]�xF !��qrf4q�؁bd�{f��}�ՐM�ܚV�9so����K��YL��>��,�[�QJ0��F�z������u�g��E��$ϙ���M`e��hx~Ȟw*����9�$�GR����eW�$�񫯨h�7o&<H����	N8a���t��Nu�������b�ۙ���uq�IwӴ���|O����z�Sѝ�UI��i�\��},*~��ɻ��Bf(�4�T���r���x��ܑ�>c�A|���[�s��7�u���A�����c��WH�-�I��������@��v����v�'��oAZ��	����u�;��I�̑X�D�>eLcu�*i�B���wa�ʧM���}G��dM7�}\�cS��^Ԧ(�kӓ^��ZĐ}B-a�)Ӂ��M�nu;�H�W��ꨜh��8yY���j� �ްB��p2i�����,ΗK;���a�}О���N�Ba�/Q�A�����.�el�V<�\�O���bL�'���-��-w���* ��(#t�r�,� 5&�n�oW8�g�~Oo!�J#H�	M�L�#7�� �����M��ӳ=yL�    K��8���b����������(���G�χj���酾?f��m�K��e��:a����.��&�R�q����B-R�@eӴ;xϋ�O��e*�ό�[�@�W�|��L#:p����jd��֞��
�xOރ��k�M��T6(��e�._a&h`�������%L���T �z�z]��L��,��=&�;��4�N����m�u9��v�Q���'�=Gs�K�y�_Q�4Pm�|`eCN����a�������(����1������n�2�Tw[Ei��|GeMt)����7)x �����m�����(��&>����'�v�b4Σ��Ҭ��Ŕ�bV.�u�D��yW��/��L뭾9���{_���&�{�R�Ё_/�@G��75���Y��N�a���}�`Т��F?�I���k���![i {��R�T��(�֭V�G��u�&�ٰ_�T�I�k��kF��k��M����N�0o~!r%��'ѼQ�R�t�C�>�{� ��WzY�~�j%ދ£.�u���.��vY~~軠���.]�>�3�J���B��[�ۏ��SeYl@������v@�`��lM�!t�$���wG`�h�5�]��M?���`�HL*1N'e!���~��X)$�j���(j��Ư;��GN�C���F�ex��s�ʳz����x�P��JW(DQ^͈�r,��w�5������C��c�wf1�p�,
~;�8�Өg�fo�,�/W�VN��;fr.I�������i�o?�73��B��g*.�͏U�_Yu�Ry�EX�6"����2s�)΅����Z'�_���U�7�r>�Ef�]B+D���Ks�
9��J���p�n����눙�?d-0�eʹ�������9y��ǘ�3��/��^C�����	_�#-�ġo�C���_����ڇ��n>�"�8��"��z��~R�U\CZY�3U1w¡��#���Ӿta�� l`�B�l������x��W��,����W��F%\�wIҩU1�`�ڑ�?p�{5	�Bɠ�7Y��y��vh���ek��g|�!!8�oi&,�jb�DYU�C^����������e�1	��ZcMޠ����	�/y�&���1e��G���h���Ik)�۰e��<���WHssQ�B�\J�f�$=c׷̻�@vMsv�u����}x����DI�ͅ�ԣ&ȼ,�yX�e��&Xxհ:R�<�/w�Q�h�ؑ<��ń�i�vU~����s�
y�� �̰�AG�j/h�z�0P����J��W��\>~H�v,[bO����4_ۦ���x�'��s�⽓)���[��{Z�[�a�ˬ������g`�#v�.��n��BRb('��-&���D�r��c��W�_��}�����q;GЇ,	��v��/�	HD|�!{��u�:����\
�9��n���D��6�Cy�0i8�R�Ȃ�����X�攫��`��#��[�>�]o�<A����Tm��cڑW�i����~M{��^�f�
�߰�L,�ĳع��6�͒�Su ^�]��!�C��	�D�?�T ��a���௷S��I_PU�D��ǧ��|�Fz+S����C��������M'��;����L� ^ǧ����#p�*�S�C���9B߂�%�3�(mb�6ƾm��Փu��h�}C%�@��i�"m_8�t��#�u8Ê�[�����4�f��  �|�i ���:l�3'2�pf�5!%�d�\+)Sl8p) �-�*��C���ugT�)#�v�D�㞞_�TB K�Q���Bͅ{�����0�H�PqVj'�Hx��)�?���^���;�O 9���{��<��`�7�zl���z�f`E'�v�>,G�B�����vt��Z�8�'��SV!@�0Q��g�puU"�,��8}���M�n�;,R�^_���T���fdt1�`�#vjp�=��S�J�Lժ���v���������mx~E���I��d���
9�� vҼ��3K�{"�0�&=�I��K�a<��`�����dɹ54P0�
�,�|�v�e��r��Ib{_ ��SP
E�@1)�����i7��b�̔,��R��LYs�Q�@&ٌ��
*�P�� ��4�U� ���i7sb_C��?.z�inw@c�
��������w:Ы+������Dp�2��.���a?ڱg!�m�>�hɄ)�cؓ�3��dh�3�чb���4�n��$y\����}�v�C���4��fW���i�2�5�@z%~VK��y���͏��Ҏ?8N}q���&��^�v���Ǎ0&5�����8�D��((wY�
�Hj��'�F��V^��EL�AˋQluOA\)@�2�\h��١A ��s�b��t�����V�Sm3�}�x���;&ģ�ڜj��?zRU��K����)����H-��=�ğK�|����Ll����9'�xahǱ0`���A]�ۂ��6��[pӬ|	���c�>�~�y��t��ƺI�9�7��g9C�#���<$��­����)�D�w� �{ػ��L�ϐe�֯U�D�$���Kμ�h��K�C'��r8�r�_&��#�ÇFh��
�G�/u�+���9�70òl:s�;Vgbe6�<z7?��7��Q.Qy �?TB��6���3ܐ��	���S6�W�T����ތޯG�2����vJ킜G2�G���N胸��F��`B[�;��Ѩ1�F�hp�.b=8 /m����Z�E\,�G9���&pp��O0��A����/z��'�~�g��(���Y�3�ⴇ=Q�idT=�*����`��
w��)�ŹR�1 ��;v9 ��9O��L_J~���b&��+;��u�}���I�3�}�	�u��R���H��\��:˓k�/"�v>)��{L��3>�=��s0Ȗ�|ե^A�����ycHhzJ�Rv��ߌ�%�_m9����F�#.���|{��Z�:z���@H�ze�x�k%�%�I�Y��i&��6�#D���N��9�ٱ�𯋮S��
k�������/�x=R_�P��n��]�l:���O�C���T��i�M���7��>�j�8eWs%�||9��]0�z}�ES���L�P��f�L����@�0���7��E�|>իE�dp-�4�9�A�Yi����Y��ӡ߫B>����eV�tL����c��#��f��J+[`�c�8�Lq$U�v���P8�mpq$P�Z4Μ{Y�n�M�Ԍ^��Y����c��cb�)�Y��CWe��4"�����z/I�2nm�,*صW�WΗ+Q
����.���f,)ͩ=�n�v��?%B�U�̘�;��C=�A;F2�d[��X�SZ�:	���9��8P�@�,��A~#��؜�r����
c@'pˌ�� }�NS�\���!\62X;�
~,�<��]�Xa/���p��=?��Ov��B�����E10���	h聈�#�}LP[�ee{�h`1�P6���؀�\[�J�}j���@)ieN���~yO�������G�Mz����nέ?�����������^Wŗ)����)g�@E��_��E�ߵe�_��v���o��+?h�r��3+�#�Bk��x�pO|��OR�D��>��#�$��%˪�������, z� ��M�xuUU⸗$�4e���ǺZ���'�v�cl'{zg.��Lދ/5�	���q��F�0����諧[�DtQ�*�_�?g�jp��U��T%�A	r��)NJ>����ݔP�԰8:�4���8�<�A����ڒ�� �5�)mJF1"QԪsy8�臋��x�;��ܻ_m��,{*6r���sM��q%��F�ܷ���W#����Ș�\y���~Ai��:\Ɛ�E����k>2>�M������黠2�6\�04	�������kB�n��̃St��0�p9b�`�Y�@H�ۓ��Tz�fϒ�f�����&[Me	i&��E`k3n�� �  ˄�<�|;h�;hϢ1�.�4��[�`>�E1��m��^֣s����Z͏�E�IZ�M@�9�@j��p|��wK�u�NRܚك��9zhZm���"PG ���[E��3�طV%�ڡs7����ߗ��S�B���ĭ�􆶹%v}�2ܥnz�F�!�ʍi����rR#h���>sT�~kgѠ�����RX�4j5/��piE�*�v��V�F�@æ)�#~�>ش|KU�_08���{Eď%�s�:�$Σ%�����*��t�jd־z���'�vA�$�~�c���|��^_dE�6p;�݈Nf\	-<� aj�z^Mv?�c�M�؟9�o&��^ g�h�[�l/��JF�dD�{���`sՌ�+ņ-1R���|���o�j�\ɝ�Y_�SN1�r^��.\�r^D��N�����c����ե}����=�A$��ZT		�i�-�N���||���͐�߅!�]O^*�����b��@�d�.v 컖EITZ ��\}Ӽ�z%'� �BGq^�[)!�%��,�:�-�@��C�v���ϓ������ӧ�����Tʇ��	YB��Y��q��%���W��V�J�Q�]�^�����{�w��ʵ��n�KJ�I�y/�跌{a��	J�D'JC�����x�5�w<�?ի=7g��%�x�b���:��7�PŢ��"@�ל$刺}�h���ǔ�n��-β"��M��dЁ�J^ɔ3��[��9�~�q���f�A�%�_y`�F�*97��:�1}@��ϠыX_�:C��x�E���K���*�nȇ����B��h��n�SS��Q���>�N�%(C��Ȼn3����zdn���-����	4�9j�ɳ�Cu����($�����D���/-��r��:Pp�+`������]!E�Ҿ��J�o&�v�i����]ɝ��j�Q�PT�kr�C���#.z���ԯ���$}�}����\�ǘR�(� ���Ac�ș��δ�<��@�0�.Pp_	�nV��p�>Oτ�Q�����3��g��"~��NoV&�+�Gv$Wهqv����(d����Qs	0"k���̯�< m�5p�:O-_�;��c��>�􁥕M����_@�l�X��)������ߕ���[�h�pa�B��kCž�1wC@w�� �M��:��wⲗ�����^'q�țOW}����� �=S9�N͑x�Ϗ��a�>�&&��k��Dl�}�i�t�f��XI�Z�%�Be�~{z9�-8�;l���Ϟ̯���[��/�ː�X.�)��Hz��5ck�N_���[�whW��G�/46!�{A6Lk�̳���>R�a�UǼ�~�)�jc7���������� @�;Zߗ�T 8u��	LM�*�ޚƆ�%���rr�7�v�ܪ��	�>���Bw�Ï؛���A�Y�@-�x�G����`��d2�v�6Ź �Qq2��m�>@i�i�}�ǉB�Uw�U@-x���p�y�ủ{ઞ/F�ƔoI�n��C��ˌU��
�}a(�e!�E��T�Y���U1v�`�G��c���eܱ�A���5	qx��5r���^����2J����*��0tW{�ֹ��w'�k?�`Jɧq"�0�� &��i�*?38����D�	Z
x+�]��Q��A��a�=�6 y,���=��}��W�د.=t��7yE"�zt.^�����9�{RY'^� bK `��:"��HZ�� @�>G��ɉ����e�z��Vkm���La��O_zЎa��`Th��eJ����ᇶ¾^E���c'#��'F� ���]�*�Q|��,�H{���(�f����<���~Ğ���N��N��r���ـ�V�4�fOp ϕ����^��Y�����cw �ϧ�ޝ���[��}�0�~�晱�¡J�DX"�����˵�݅����J=��խ��޹���G �-+7z�o�J��-�a
h�+������`A=�uR(�� -�i���؂ϖ�}����ZG�k ��c�/�Dh���?� �=��tc�j:���A{�n��47?�ou�ݪ�~U����pm�P�i�YX�)�U㤸0l��2c[~�#�SPy��j�׌������]�RcAG�	�����n�=>���S�o�%B9���n��������d���`��[EM��`�h\A �Ĝ8�5~Gi&�V>H�S?�>ͭ}�{o^|�U.�v2�	��N����C���3�$��:�I��8�Ct�Y<⢩�gnb�LM{�^��4���O�p�P����M��K�� �_Oz�BE�C�C��/�i�~����0��UTA���D�M�!;���V�\�v(9�o�5L�U�E<��� �
C_pd�T��߼E~��5�y���;�|� txΧ\s7^Z%�?p[ཕ�[���p ��N��f	�vF�+gic~�<t%/%ߠ3^�g�'��<����H!�?F�� �x0\�Z醧5||y���x"��r�p�΍��9�L}qe�O/��\�ߢ��p�
���2���e���d�&��蕤Y��� �ӵrm8[�闸ru�@ ���a��8�����"�r��~����g�>�$P(�Ԑ=��Ü{�,-��A�)��k��GٟXnm�9M ]a�fz>'-�<l��a�"����Q�F�"�!���H;ޟ��S�AT�md��h�:w���<���|��rC'���?gH�ilx���¼@�}:�'D����`�5;��[
&�z<��ˡo�~%8�uz�ߢ�LC�� ��»�.����I�^�����Y�^��W&�	n
��a@���֮��!�M��6��U�S�l�=�
=JǣXjy��p1���"B �qT��%�ۏ��?!�o��H�x{?�/�v�hS�ȗ�7<�������>BqVp?���dZ[S.�. bT���"��VT����������A�o�-I'����Ѯ���:���Z[�l<���n��7"LR����j�M�aw�K�*A~)�����/
�ގ���ܑ�{�/)F���w" ,:!�NS|�Ip��fFװ���)�Gk�6�w��oo���ɠ����vG! Ð�����O!=��|�w�-�2�u5h�R:�>�D⃏Q:��U=|*���L�Sry@�d�GT������@ #@�����a|��v{H�_���M� i0,�q�d�������3!�ZXƭ��ͻ��_��F)`�R�c�_�Q04����^�N@s�H���|Q�KG@��|h��s&\-���4��\F��b��0�$�c����~�Q��6�E!�˧�����D6��>O����u��N#�k�z9u�7v7��� /���f���	�����׿.�l�5���'O�Ku|ۉ�n �� Ev��X L;%�+�U3bΗu8�+Уk��N�!��Զb 
B0�o����·�e�9��>w�J#nce�]�Nh�]�;t�d��^�>�s���ʒ�p�r����n6�]lK�����S���$�n�� 7�q�(	��ܵ��VIS��=��/��HW�MϭSV/�q��忼�f0�8�l�ߏ�O$�j7Lb�{
�&\0R�HPp�.��
������3\�� _�{	�Xu��K!�#��"���������?����|      �   o  x�-�m��*s���^f��x�rK�L��1�.H0Ѣ��h�}�ͼ������i'��ݼ������?��������x�l�K���n�S�x�m�[��}i�ߗf�}i�ߗf�}i�ߗf�}i�ߗf�}i�ߗf���~#��o|m�7�����~c����m�7N���~㵁��m�7�M���&~s����7W����&~�߼m�7_����~+��o}m�F[��ٖ�B����~봅ߺm�^[���6~;��om�G����6~{���?,~���߾m�_����~'���|��wF;������c��v����'/G��.>y����ⓗK��r����//��\s_^.~yy���ᗗ�_^~yy���ᗗ�_^~yyf�m��D7�;m*�5�ɜ��s^���y�nJ�5�I���u^� ��H�(H��0��!�P���z
�)` ���!�A�Wf��@����A���C��!B�"t�:	��aB�(�3�@�C��!T�+t:D�B���C��1kR�b�AC���C��!n�8t�:�b��CǪ�+c:��D��C�!��Et#:��&Ȍ!��Jt�%:���D�x��#u�*:����!��Yt-:����E��C|�!��atܚ�3��C��!��ft�3:"��F�X�C���jI�­.��ҭ�n�t��[-�j�VK���̠{-\��\-�j9W˹Z��r��s����\]�#��<��-��k�D�"�it-��Z*ѵX�k�D˹Z��r����s����\-�j9W˹Z��r��s���g-���\-�j9W˹Z��r��s����\-��U���\-�j9W˹Z��r��s����\-��].��\-�j9W˹Z��r��s����\-��S�G��\-�j9W˹Z��r��s����\-��[a��\-�j9W˹Z��r��s����\-��W�z��=�=�=�=�=�=�=�=�=�=z=Pf9G9G9G9G9G9G9G9G9G9G��[3�W�C��C��C��C��C��C��C�ѣ�S�z4N=�ᘧ���qƨd����gfc��\��<7׃2O���̳������������Ǭ���!��!��!��!��!��!��!��!��!��!���Q?c�9z�9z�9z�9z�9z�9z�9z�9z�9z�9z�z��r�r�r�r�r�r�r�r�r�r��^[2������������������������������ǭ���!��!��!��!��!��!��!��!��!��!����,c�9z�9z�9z�9z�9z�9z�9z�9z�9z�9z�z��r��r��r��r��r��r��r��r��r��r��_�Nf��^(3�������������������������(�1�e5cH7���_�t�)��eJ7�����
�������kp���g�z�]�^�y[��`ޗ�E�7f�}g֙�fI��)��=W9g�U/�C��{J2wOI��)��=%���$s��d��S��{J2w�]��C��{J2wOI��)��=%���$s��d��S��{J2w�SC��{J2wOI��)��=%���$s��d��S��{J2w�[�C�WgG�-�df��Jf��df��JfV�o��T�������F�%3�%�%+�Y_m�d����1��FƐ���,	�a��8,��e���{�����|,s��-s��-s��Q.��
���e���c��x-�}%k�&�3�/]�َ�r�u���.��e��j(]Wm��m�t]���Q[@�= ��u��O��U.c�K�0�q]f6���^�0��R�f6�����ً�&�x��T~�_�0����bb�S�T�,&�2����bb.����,&�2��fG���2�i�2�i�2�iǺ��1�bڱ\{h�r���K�x�1^��o�K�c��R��8�c��Ў]Td;�+�خ8�c)َ]�$M�H�up��ж�C�vѓ��.z�����V�b&[�]gh�v����u�V�(��:C+�����)�.�ۂ��-e�rK��RF+�����:C+����-o;���F{����=j둽Fcd{����C{�4��-��wK#���H{�4��-��w����2H{���-��w�r��\2�,��!o�w��ݫ6J3�����B+�+	�ܵњ�ܵ՚�ܿM�t�m��sm�f{wm�f{wm�f{wm�f{wm�&���c'�[�h��7Z�卖�S[��,o�|�-���-o;��RFo���dzK}ط���O��͖(z�]���	��l)�7[���~�m����-��ly���~��1�~my�_[��ב7�u�~y�_G�v�{�y��G���鵭=�1��#o���Z��#y��H�>Q�C����7z}����)޲קx�^��-{}����)޲קx�^��-{}�^y��G����7z}�ژ�RF�����#e��H�>��3����#e��H�>Rv�������F�����#o����?�y������#���� �?2H��������1d���<�\���Y�1\�N�{䒑8r�H�d$ήbEƐKF��%#q䒑8r�Hi���
���+u�s9������S����2����W0�n�d�o�L2����#o����I���1G��#e�đ2F�H#q^9g�W嘌!e'�>RƘ)cL��1&W��+Q'����q�S}�+3�PɱU��u�bĮ̜d��cw��$�W�[ٞL����;�9cTfS-�|�^T�lŨJK�2��Qe,u��X*G��Ԏ*c�U������d�Vv�����[9�c|+'�)]r�oeg����1���9Ʒ�3������5;o2}w�2�Oi��5O�k��N�˂X2}�ӛL_�_�~�\3�_�~�\׏�L�S嶌a>��\W~�kfߠʦs2}]?���)�a��d�V/��U��_��Jy��y���r^�ʷ
z�����G-�ռ���89p�qr�*fs�l�f6�p��L_3�&��̾��3�ɕ�˙��Ι+�l'W^��㗟li��s��I�s� k;�i�秌��������O���?2�I�����#�;d�c��Lz����I��?r����G���}��F<��Ոg6��M=�
�ٖ�̆W��̆W���j�����f6��̆�r��W��_�3u�^�Ja�ר+�6��P���6ԝ2H���dG~�k��z�ר��PQ �QDs����zT��hQ`��ʒ9�Q_�D�Y��ɏ�R(ꮴ�JQw�!��Ѩu[R)?ͪ��ӳ,<��L4'�W�a�V�]h��a��gm���YeF�YfD�eF4���
�,�*?�K�����Y����3��A���*�m�/�V����Q*��^�Zqw�z���ףZܝ�����>�ކ�`�w���$�(wg�G͸;�=�����Q5�N_��qw�zԍ�أpܝ������e�*��I�Q<�B�i�Q>��c��qw"{��4=*�]�%�.O�r�u��h�a�*r�G��
A4�G!���*ɽ�&PJ�u<�Zr��	���p��|��^G�gʗhuP��rUx���£�\�E�:��*�q�GY��+<��u`�QX���rYx����۞YЗ�r�UxT���T��G}�)<
�uJ�Qa��	LdQ�sIOx�%u>�1���w="a{�K�`�^��潨�	����&<�:��tu��0��c.��̍Q����0;F8x�#�����0UF�%`��:L�du���2��c.����wP�I4~��E�w0�i4~G:���p@���&�(�̵�+�w�_پ{���������s'U��<��|��I滧O�� q���=��~��|��?���~�d<}�; ����O���(gP��sr����9��������Xu��{���0CG����
�N�Q%og樢w~&Vo�L+��g^��w~��6���B��{n}�������m��     