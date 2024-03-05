package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.Finder;
import io.ebean.Model;

import jakarta.persistence.*;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receta extends Model{

    @Id
    private Long id;
    @Constraints.Required(message = "error.required.name")
    @NotNull
    @Size(min = 1, max = 255)
    private String nombre;
    @Constraints.Required(message = "error.required.type")
    @NotNull
    @Size(min = 1, max = 255)
    private String tipoComida;
    @Column(length=999999)
    private String imagen;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Ingredientes> ingredientes ;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL)
    public List<Ingredientes> ingredientesId ;

    @OneToOne(cascade = CascadeType.ALL)
    public Ingredientes ingredientesDescrip ;

    public Receta() {
        this.ingredientes = new ArrayList<>();
        this.ingredientesId = new ArrayList<>();
        this.ingredientesDescrip = new Ingredientes();
    }

    //Finders
    public static Finder<Long, Receta> find = new Finder<>(Receta.class);
    public static Receta findById(Long id)
    {
        return find.byId(id);
    }
    public static Receta findByTipocomida(String tipoComida)
    {
        return find.query().where().eq("tipoComida", tipoComida).findOne();
    }
    public static Receta findByNombre(String nombreReceta) {

        return find.query().where().eq("nombre", nombreReceta).findOne();
    }
    public static List<Receta> findByIngredientes(String nombreIngrediente) {
        return find.query().where().eq("ingredientes.nombreIg", nombreIngrediente).findList();
    }
    public static List<Receta> findCocinero(String tipoComida) {
        return find.query().where().eq("tipoComida", tipoComida).findList();
    }
    public static List<Receta> findByListaNombre(String nombreReceta) {

        return find.query().where().eq("nombre", nombreReceta).findList();
    }

    //Getter y Setter
    public List<Ingredientes> getIngredientesId() {
        return ingredientesId;
    }

    public void setIngredientesId(List<Ingredientes> ingredientesId) {
        this.ingredientesId = ingredientesId;
    }

    public Ingredientes getIngredientesDescrip() {
        return ingredientesDescrip;
    }

    public void setIngredientesDescrip(Ingredientes ingredientesDescrip) {
        this.ingredientesDescrip = ingredientesDescrip;
    }

    public static Finder<Long, Receta> getFind() {
        return find;
    }

    public static void setFind(Finder<Long, Receta> find) {
        Receta.find = find;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Ingredientes> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingredientes> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode recetaJson = mapper.createObjectNode();
        recetaJson.put("id", this.id);
        recetaJson.put("nombre", this.nombre);
        recetaJson.put("tipoComida", this.tipoComida);
        recetaJson.put("imagen", this.imagen);

        ArrayNode ingredientesJson = mapper.createArrayNode();
        for (Ingredientes ingrediente : this.ingredientes) {
            ObjectNode ingredienteJson = mapper.createObjectNode();
            ingredienteJson.put("id", ingrediente.getId());
            ingredienteJson.put("nombre", ingrediente.getNombreIg());
            ingredienteJson.put("descripcion", ingrediente.getDescripcion());
            ingredienteJson.put("cocinero", ingrediente.getCocinero());
            ingredientesJson.add(ingredienteJson);
        }

        recetaJson.set("ingredientes", ingredientesJson);

        return recetaJson;

    }
}




