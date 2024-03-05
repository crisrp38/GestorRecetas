package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.Finder;
import io.ebean.Model;

import jakarta.persistence.*;
import play.data.validation.Constraints;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ingredientes extends Model {

    @Id
    private Long id;
    @Constraints.Required(message = "error.required.ingredient.name")
    @NotNull
    @Size(min = 1, max = 255)
    private String nombreIg;
    @Constraints.Required(message = "error.required.description")
    @NotNull
    @Size(min = 1, max = 1000)
    private String descripcion;
    @Constraints.Required(message = "error.required.cook")
    @NotNull
    @Size(min = 1, max = 255)
    private String cocinero;

    @ManyToMany(mappedBy = "ingredientes")
    public List<Receta> recetas = new ArrayList<>();

    @ManyToOne
    public Receta receta;

    @OneToOne(mappedBy = "ingredientesDescrip")
    public Receta nom ;



    public Ingredientes( String nombreIg, String descripcion,String cocinero) {
        this.nombreIg = nombreIg;
        this.descripcion = descripcion;
        this.cocinero = cocinero;

    }
    public Ingredientes(){}

    public static Finder<Long, Ingredientes> find = new Finder<>(Ingredientes.class);
    public static Ingredientes findById(Long idIngrediente) {
        return find.query().where().eq("id", idIngrediente).findOne();
    }

    public String getCocinero() {
        return cocinero;
    }

    public void setCocinero(String cocinero) {
        this.cocinero = cocinero;
    }

    public Receta getNom() {
        return nom;
    }

    public void setNom(Receta nom) {
        this.nom = nom;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreIg() {
        return nombreIg;
    }

    public void setNombreIg(String nombreIg) {
        this.nombreIg = nombreIg;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static Finder<Long, Ingredientes> getFind() {
        return find;
    }

    public static void setFind(Finder<Long, Ingredientes> find) {
        Ingredientes.find = find;
    }


    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("cocinero", this.cocinero);
        return jsonNode;
    }
}
