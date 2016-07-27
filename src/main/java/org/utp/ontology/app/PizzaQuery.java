package org.utp.ontology.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

/**
 * 
 * Class to query the pizza ontology
 * 
 * @author juanprq
 *
 */
public class PizzaQuery {
    
    /**
     * 
     * Query the pizza ontology
     * 
     * @param model ontology model
     * @param query the input query of the user
     * @return the result of the query ontology
     * @throws IOException if cannot read the query template
     */
    public static List<QuerySolution> queryOntology(OntModel model, String query) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile("templates/baseQuery");
        
        Map<String, Object> params = new HashMap<>();
        params.put("rdfUri", RDFS.getURI());
        params.put("owlUri", OWL.getURI());
        params.put("query", query);
        
        String baseQuery = template.apply(params);
        
      Query queryObject = QueryFactory.create(baseQuery);
      QueryExecution queryExecution = QueryExecutionFactory.create(queryObject, model);
      ResultSet resultSet = queryExecution.execSelect();
    
      List<QuerySolution> results = ResultSetFormatter.toList(resultSet);
      queryExecution.close();
      
      return results;
    }

}
