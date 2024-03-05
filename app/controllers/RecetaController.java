package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Ingredientes;
import models.Receta;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.data.FormFactory;
import play.mvc.Results;

import views.RecetaView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class RecetaController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public RecetaController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result crearReceta(Http.Request request) {
        Form<RecetaView> form = formFactory.form(RecetaView.class).bindFromRequest(request);
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            RecetaView recetaView = form.get();
            List<RecetaView> rv = obtenerRecetasDesdeRequest(request);
            List<Receta> rc= recetaView.toDuplicadp(rv);

            if (!rc.isEmpty() ) {
                return badRequest("Ya existe ese nombre o tipo de comida");
            }
            else {
                List<Receta> recetas = recetaView.toModel(rv);

                for (Receta receta : recetas) {
                    receta.save();
                }
                return ok("Recetas creadas correctamente");
            }
        }
    }

    private List<RecetaView> obtenerRecetasDesdeRequest(Http.Request request) {
        List<RecetaView> recetasView = new ArrayList<>();

        JsonNode data = request.body().asJson();

        if (data.isArray()) {
            for (JsonNode recetaJson : data) {
                String nombre = recetaJson.get("nombre").asText();
                String tipoComida = recetaJson.get("tipoComida").asText();
                String imagen = recetaJson.get("imagen").asText();

                List<Ingredientes> ingredientesReceta = new ArrayList<>();

                if (recetaJson.has("ingredientes")) {
                    JsonNode ingredientesJson = recetaJson.get("ingredientes");
                    for (JsonNode ingredienteJson : ingredientesJson) {
                        String nombreIngrediente = ingredienteJson.get("nombreIg").asText();
                        String descripcionIngrediente = ingredienteJson.get("descripcion").asText();
                        String cocineroIngrediente = ingredienteJson.get("cocinero").asText();

                        Ingredientes ingrediente = new Ingredientes(nombreIngrediente, descripcionIngrediente, cocineroIngrediente);
                        ingredientesReceta.add(ingrediente);
                    }
                }

                RecetaView recetaView = new RecetaView(nombre, tipoComida, imagen, ingredientesReceta);
                recetasView.add(recetaView);
            }
        }

        return recetasView;
    }

    public Result getRecetas(Http.Request req) {
        List<Receta> recetas = Receta.find.all();

        if (req.accepts("application/json")) {
            JsonNode json = convertirRecetasAJson(recetas);
            return ok(json);
        }
        else if (req.accepts("application/xml")) {
            return ok(views.xml.AllRecetas.render(recetas));
        } else {
            return Results.notAcceptable();
        }

    }
    private JsonNode convertirRecetasAJson(List<Receta> recetas) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode recetasJson = mapper.createArrayNode();

        for (Receta receta : recetas) {
            ObjectNode recetaJson = mapper.createObjectNode();
            recetaJson.put("id", receta.getId());
            recetaJson.put("nombre", receta.getNombre());
            recetaJson.put("tipoComida", receta.getTipoComida());
            recetaJson.put("imagen", receta.getImagen());

            ArrayNode ingredientesJson = mapper.createArrayNode();
            for (Ingredientes ingrediente : receta.getIngredientes()) {
                ObjectNode ingredienteJson = mapper.createObjectNode();
                ingredienteJson.put("id", ingrediente.getId());
                ingredienteJson.put("nombre", ingrediente.getNombreIg());
                ingredienteJson.put("descripcion", ingrediente.getDescripcion());
                ingredienteJson.put("cocinero", ingrediente.getCocinero());
                ingredientesJson.add(ingredienteJson);
            }

            recetaJson.set("ingredientes", ingredientesJson);
            recetasJson.add(recetaJson);
        }

        return recetasJson;
    }

    public Result obtenerRecetaPorNombre(String nombre, Http.Request req) {
        Receta receta = Receta.findByNombre(nombre);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        if (req.accepts("application/json")) {
            JsonNode json = receta.toJson();
            return ok(json);

        }
        else if (req.accepts("application/xml")) {
            return ok(views.xml.recetasId.render(receta));
        } else {
            return Results.notAcceptable();
        }
    }
    public Result obtenerRecetaPorId(Long idReceta, Http.Request req) {
        Receta receta = Receta.findById(idReceta);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        if (req.accepts("application/json")) {
            JsonNode json = receta.toJson();
            return ok(json);
        }
        else if (req.accepts("application/xml")) {
                return ok(views.xml.recetasId.render(receta));
        } else {
            return Results.notAcceptable();
        }
    }

    public Result buscarTipoComida(String tipoComida,Http.Request req ){
        Receta receta = Receta.findByTipocomida(tipoComida);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        Ingredientes descripcionIngredientes = receta.getIngredientesDescrip();
        if (descripcionIngredientes == null) {
            return notFound("Cocinero no encontrada para la receta: " + tipoComida);
        }

        if (req.accepts("application/json")) {
            JsonNode json = descripcionIngredientes.toJson();
            return ok(json);
        }
        else if (req.accepts("application/xml")) {
            return ok(views.xml.recetas.render(descripcionIngredientes));
        }

       else {
            return Results.notAcceptable();
        }
    }

    public Result buscarPorIngrediente(String nombreIngrediente,Http.Request req ){
        List<Receta> receta = Receta.findByIngredientes(nombreIngrediente);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        if (req.accepts("application/json")) {
            JsonNode json = convertirRecetasAJson(receta);
            return ok(json);
        }
        else if (req.accepts("application/xml")) {
            return ok(views.xml.AllRecetas.render(receta));
        }
        else {
            return Results.notAcceptable();
        }
    }

    public Result modificarReceta(Long idReceta, Http.Request request) {
        JsonNode data = request.body().asJson();

        Receta receta = Receta.findById(idReceta);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        if (data.has("nombre")) {
            receta.setNombre(data.get("nombre").asText());
        }
        if (data.has("imagen")) {
            receta.setImagen(data.get("imagen").asText());
        }

        receta.update();

        return ok("Receta modificada correctamente");
    }
    public Result modificarIngrediente(Long idIngrediente, Http.Request request) {
        JsonNode data = request.body().asJson();

        Ingredientes ingrediente = Ingredientes.findById(idIngrediente);
        if (ingrediente == null) {
            return notFound("Ingrediente no encontrado");
        }

        if (data.has("nombreIg")) {
            ingrediente.setNombreIg(data.get("nombreIg").asText());
        }

        if (data.has("descripcion")) {
            ingrediente.setDescripcion(data.get("descripcion").asText());
        }

        ingrediente.update();

        return ok("Ingrediente modificado correctamente");
    }

   /* public Result eliminarReceta(Long idReceta) {
        System.out.println("hola");
        Receta receta = Receta.findById(idReceta);
        if (receta == null) {
            return notFound("Receta no encontrada");
        }

        // Eliminar la receta y aplicar la cascada para eliminar los ingredientes relacionados
        receta.delete();

        return ok("Receta eliminada correctamente");
    }*/

}
