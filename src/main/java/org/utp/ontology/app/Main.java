package org.utp.ontology.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

/**
 * 
 * Main class.
 * 
 * @author juanprq
 *
 */
public class Main {

    /**
     * Main thread to open the ontology.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // logger
        Logger logger = LoggerFactory.getLogger(Main.class);
        
        // Load the ontology.
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        FileManager.get().readModel(model, "ontology/transporte.owl");
        
        // Configuration for the front web
        Spark.staticFiles.location("/css");
        Spark.get("/", (request, response) -> {
            String query = request.queryParams("q");
            
            Map<String, Object> map = new HashMap<>();
            map.put("query", query);
            
            List<String> results = new ArrayList<>();
            if(query != null && query.length() > 0) {
                try {
                    List<QuerySolution> result = PizzaQuery.queryOntology(model, query);
                    for (QuerySolution solution : result) {
                        results.add(solution.toString());
                    }
                    map.put("results", results);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.debug(e.getMessage());
                    map.put("error", "Ha ocurrido un error con la consulta SPARQL");
                }
            }
            
            return new ModelAndView(map, "index.hbs");
        }, new HandlebarsTemplateEngine());
        
    }
}

