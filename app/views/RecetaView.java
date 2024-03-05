package views;

import models.Ingredientes;
import models.Receta;

import java.util.ArrayList;
import java.util.List;

public class RecetaView {
    public Long id;
    public String nombre;

    public String tipoComida;
    public String imagen;

    public List<Ingredientes> ingredientes;

    public RecetaView() {}
    public RecetaView(String nombre, String tipoComida, String imagen) {
        this.nombre = nombre;
        this.tipoComida = tipoComida;
        this.imagen = imagen;
    }

    public RecetaView(String nombre, String tipoComida, String imagen, List<Ingredientes> ingredientesReceta) {
        this.nombre = nombre;
        this.tipoComida = tipoComida;
        this.imagen = imagen;
        this.ingredientes =  ingredientesReceta;
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


    public List<Receta> toModel(List<RecetaView> recetasView) {
        List<Receta> recetas = new ArrayList<>();

        for (RecetaView recetaView : recetasView) {
            Receta receta = new Receta();
            receta.setNombre(recetaView.nombre);
            receta.setTipoComida(recetaView.tipoComida);
            receta.setImagen(recetaView.imagen);

            List<Ingredientes> ingredientesReceta = new ArrayList<>();

            if (recetaView.ingredientes != null && !recetaView.ingredientes.isEmpty()) {
                for (Ingredientes ingredienteData : recetaView.ingredientes) {
                    Ingredientes ingrediente = new Ingredientes();
                    ingrediente.setNombreIg(ingredienteData.getNombreIg());
                    ingrediente.setDescripcion(ingredienteData.getDescripcion());
                    ingrediente.setCocinero(ingredienteData.getCocinero());

                    ingrediente.setReceta(receta);

                    ingredientesReceta.add(ingrediente);
                    // Establecemos la relación 1-1 entre la receta y sus ingredientes
                    receta.setIngredientesDescrip(ingrediente );

                }
            }
             // Establecemos la relación 1-n entre la receta y sus ingredientes
            receta.setIngredientesId(ingredientesReceta);
            // Establecemos la relación n-m entre la receta y sus ingredientes
            receta.setIngredientes(ingredientesReceta);
            recetas.add(receta);
        }

        return recetas;
    }

   public List<Receta> toDuplicadp(List<RecetaView> rv) {
       List<Receta> recetasDuplicadas = new ArrayList<>();

       for (RecetaView recetaView : rv) {
           String nombre = recetaView.getNombre();
           String tipoComida = recetaView.getTipoComida();

           List<Receta> recetasNombre = Receta.findByListaNombre(nombre);
           List<Receta> recetasTipoComida = Receta.findCocinero(tipoComida);

           if (!recetasNombre.isEmpty() || !recetasTipoComida.isEmpty()) {
               recetasDuplicadas.addAll(recetasNombre);
               recetasDuplicadas.addAll(recetasTipoComida);
           }
       }
       return recetasDuplicadas;
   }

}