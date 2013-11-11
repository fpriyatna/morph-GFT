 package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator
import org.apache.log4j.Logger
 import com.google.api.services.fusiontables.Fusiontables
 import org.apache.log4j.PropertyConfigurator
 import es.upm.fi.dia.oeg.obdi.core.ConfigurationProperties
 import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.engine.R2RMLElementUnfoldVisitor
 import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLQueryTranslator
 import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.model.R2RMLMappingDocument
 import es.upm.fi.dia.oeg.obdi.core.querytranslator.QueryTranslationOptimizer
 import es.upm.fi.dia.oeg.obdi.core.sql.SQLQuery
 import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.engine.R2RMLRunner

object R2RMLFusionQueryTranslatorTest {
	val logger : Logger = Logger.getLogger("R2RMLFusionQueryTranslatorTest");
	val MAPPING_DIRECTORY_WINDOWS = "C:/Users/Freddy/Dropbox/Documents/oeg/odemapster2/mappings/";
	val MAPPING_DIRECTORY_LINUX = "/home/fpriyatna/Dropbox/Documents/oeg/odemapster2/mappings/";
	val MAPPING_DIRECTORY_MAC = "/Users/freddypriyatna/Dropbox/Documents/oeg/odemapster2/mappings/";

	
	val mappingDirectory = this.getMappingDirectoryByOS();
	val configurationDirectory = mappingDirectory + "r2rml-mappings/r2rml-fusiontable/";
	val mappingDocumentFile = configurationDirectory + "worldgdp.ttl";
	PropertyConfigurator.configure("log4j.properties");


	def getMappingDirectoryByOS() : String = {
		val osName = System.getProperty("os.name");
		if(osName.startsWith("Linux")) {
			return MAPPING_DIRECTORY_LINUX;
		} else if(osName.startsWith("Windows")) {
			return MAPPING_DIRECTORY_WINDOWS; 
		} else if (osName.equalsIgnoreCase("Mac OS X")) {
			return MAPPING_DIRECTORY_MAC;
		} else {
			return null;
		}
	}
	
	def main(args: Array[String]) {
		this.testWorldGDP();
		logger.info("Bye");
	}

  	def runFreddy(testName : String)  = {
		logger.info("------ Running " + testName + " Freddy ------");
		val configurationFile = testName + ".r2rml.properties";
		
		try {
			val start = System.currentTimeMillis();
//			val runner = new R2RMLRunner(configurationDirectory + "1pQBGUqR_g-j1WQavu-Fi1wGS7jsdRxomGc0DxMI", "oegmembers.r2rml.properties");
			val runner = new R2RMLRunner();

			runner.readMappingDocumentFile("http://mappingpedia.linkeddata.es/mappings/fusiontables/1pQBGUqR_g-j1WQavu-Fi1wGS7jsdRxomGc0DxMI/oegmembers.ttl");
			runner.readSPARQLFile("http://mappingpedia.linkeddata.es/mappings/fusiontables/1pQBGUqR_g-j1WQavu-Fi1wGS7jsdRxomGc0DxMI/oegmembers.sparql");

//			runner.readMappingDocumentFile("http://mappingpedia.linkeddata.es/mappings/gft/1pQBGUqR_g-j1WQavu-Fi1wGS7jsdRxomGc0DxMI/oegmembers.ttl");
//			runner.readSPARQLFile("https://dl.dropboxusercontent.com/u/531378/gft2rdf/CableGate.sparql");

			
			
			runner.setQueryTranslatorClassName("es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator.R2RMLFusionTablesQueryTranslator");
			runner.setQueryResultWriterClassName("es.upm.fi.dia.oeg.obdi.core.engine.XMLWriter");
//			runner.setQueryResultWriterOutput("output.xml");
			runner.setDataSourceReaderClassName("es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.engine.FusionTablesReader");
			runner.run();
			val end = System.currentTimeMillis();
			logger.info("test execution time was "+(end-start)+" ms.");
			logger.info("------" + testName + " Freddy DONE------");
		} catch {
      		case e : IllegalArgumentException => {
      			e.printStackTrace();
      			logger.error("Error : " + e.getMessage());
      			logger.info("------" + testName + " FAILED------\n\n");
      			null;
      		}
		}
  	}
  	
	def testWorldGDP() = {
		val testName = "worldgdp";
		this.runFreddy(testName);
	}	
}