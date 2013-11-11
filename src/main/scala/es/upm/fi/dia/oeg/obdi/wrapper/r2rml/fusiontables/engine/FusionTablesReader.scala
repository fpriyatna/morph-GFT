package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.engine

import scala.collection.JavaConversions._
import es.upm.fi.dia.oeg.obdi.core.engine.AbstractResultSet
import org.apache.log4j.Logger
import com.google.api.services.fusiontables.Fusiontables
import es.upm.fi.dia.oeg.obdi.core.engine.AbstractDataSourceReader


class FusionTablesReader extends AbstractDataSourceReader{
	val logger : Logger = Logger.getLogger("FusionTablesQueryEvaluator");
	val fusionTablesUtility = new FusionTablesUtility();
    val credential = fusionTablesUtility.authorize();
    logger.debug("credential = " + credential);
    fusionTablesUtility.fusiontables = new Fusiontables.Builder(
        fusionTablesUtility.HTTP_TRANSPORT, fusionTablesUtility.JSON_FACTORY, credential)
    	.setApplicationName("Google-FusionTablesSample/1.0").build();
    logger.debug("queryTranslator.fusiontables = " + fusionTablesUtility.fusiontables);
    
	
  def evaluateQuery(query: String): AbstractResultSet = { 
		val sqlResponse = this.fusionTablesUtility.executeSQL(query);
		val rows = sqlResponse.getRows().toList;
		val iResultSet = new FusionTablesResultSet(rows); 
		return iResultSet; 
   }

}