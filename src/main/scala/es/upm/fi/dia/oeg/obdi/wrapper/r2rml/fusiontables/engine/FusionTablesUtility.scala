package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.engine

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.util.DateTime
import com.google.api.services.fusiontables.FusiontablesScopes
import com.google.api.services.fusiontables.Fusiontables
import com.google.api.services.fusiontables.model.Table
import com.google.api.services.fusiontables.model.TableList
import com.google.api.services.fusiontables.model.Column
import java.io.File
import java.util.Collections
import java.util.UUID
import java.util.Arrays
import java.util.Date
import org.apache.log4j.Logger
import scala.collection.JavaConversions._
import com.google.api.services.fusiontables.model.Sqlresponse
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator.R2RMLFusionTablesQueryTranslator

class FusionTablesUtility {
  val logger : Logger = Logger.getLogger("FusionTableUtility");

    /** Global instance of the HTTP transport. */
  val HTTP_TRANSPORT : HttpTransport  = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  val JSON_FACTORY : JsonFactory  = new JacksonFactory();
  
  var fusiontables : Fusiontables = null;
  
  val view = new View();
  
  def authorize() : Credential = {
    logger.info("Authorizing Google API....");
    val userDir = System.getProperty("user.dir");
    
    
    // load client secrets
    logger.debug("loading client secret JSON file...");
    val clientSecretInputStream = classOf[FusionTablesUtility].getResourceAsStream(
            "/client_secrets.json");
    logger.debug("clientSecretInputStream  = " + clientSecretInputStream);
    
    logger.debug("Loading Google Client Secret...");
    val clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, clientSecretInputStream);
    logger.debug("Client Secrets loaded.");
    
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out.println(
          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
          + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }
    // set up file credential store
    val credentialStore = new FileCredentialStore(
        new File(System.getProperty("user.home"), ".credentials/fusiontables.json"), JSON_FACTORY);
    // set up authorization code flow
    val flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
        Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setCredentialStore(credentialStore)
        .build();
    // authorize
    return new AuthorizationCodeInstalledApp(flow, 
        new LocalServerReceiver()).authorize("user");
  }

    /** List tables for the authenticated user. */
  def listTables() = {
    view.header("Listing My Tables");

    // Fetch the table list
    val listTables = fusiontables.table().list();
    val tablelist = listTables.execute();

    if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
    	System.out.println("No tables found!");
    } else {
	    val tableListItems = tablelist.getItems();
	    for (table <- tablelist.getItems()) {
	      view.show(table);
	      view.separator();
	    }      
    }


  }

    /** Create a table for the authenticated user. */
  def createTable() : String = {
    view.header("Create Sample Table");

    // Create a new table
    val table = new Table();
    table.setName(UUID.randomUUID().toString());
    table.setIsExportable(false);
    table.setDescription("Sample Table");

    // Set columns for new table
    table.setColumns(Arrays.asList(new Column().setName("Text").setType("STRING"),
        new Column().setName("Number").setType("NUMBER"),
        new Column().setName("Location").setType("LOCATION"),
        new Column().setName("Date").setType("DATETIME")));

    // Adds a new column to the table.
    val t = fusiontables.table().insert(table);
    val r = t.execute();

    view.show(r);

    return r.getTableId();
  }
  
  /** Inserts a row in the newly created table for the authenticated user. */
  def insertData(tableId : String) {
    val sql = fusiontables.query().sql("INSERT INTO " + tableId + " (Text,Number,Location,Date) "
        + "VALUES (" + "'Google Inc', " + "1, " + "'1600 Amphitheatre Parkway Mountain View, "
        + "CA 94043, USA','" + new DateTime(new Date()) + "')");

    try {
      sql.execute();
    } catch {
      case e : IllegalArgumentException => e.printStackTrace();
      // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
      // been thrown.
      // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
      // http://code.google.com/p/google-api-java-client/issues/detail?id=545
    }
  }
  
  /**
   * @param tableId
   * @throws IOException
   */
  def showRows(tableId : String) {
    view.header("Showing Rows From Table");

    val sql = fusiontables.query().sql("SELECT * FROM " + tableId + " LIMIT 100");

    try {
      val response = sql.execute();
      println("response = " + response);
    } catch {
      case e : IllegalArgumentException => e.printStackTrace();
      // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
      // been thrown.
      // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
      // http://code.google.com/p/google-api-java-client/issues/detail?id=545
    }
  }

  /** Deletes a table for the authenticated user. */
  def deleteTable(tableId : String) {
    view.header("Delete Sample Table");
    // Deletes a table
    val delete = fusiontables.table().delete(tableId);
    delete.execute();
  }

  /**
   * @param tableId
   * @throws IOException
   */
  def showRows(tableId : String, columnName : String) {
    view.header("Showing Rows From Table");

    val sql = fusiontables.query().sql("SELECT " + columnName + " FROM " + tableId);

    try {
      val response = sql.execute();
      System.out.println("response = " + response.toPrettyString());
    } catch {
      case e : IllegalArgumentException => e.printStackTrace();      // For google-api-services-fusiontables-v1-rev1-1.7.2-beta this exception will always
      // been thrown.
      // Please see issue 545: JSON response could not be deserialized to Sqlresponse.class
      // http://code.google.com/p/google-api-java-client/issues/detail?id=545
    }
  }

  def executeSQL(sqlString : String) : Sqlresponse = {
    //logger.info("Executing query : \n" + sqlString);
    val sql = fusiontables.query().sql(sqlString);
    val response = sql.execute();
    response;
  }
  
  /**
   * @param tableId
   * @throws IOException
   */
  def describeTable(tableId : String) : Sqlresponse = {
    view.header("Describing Table " + tableId);
    val sql = fusiontables.query().sql("DESCRIBE " + tableId);
      val response = sql.execute();
      System.out.println("response = " + response.toPrettyString());
      response;
  }
}

	object FusionTablesUtility {
	    val logger : Logger = Logger.getLogger("FusionTableUtility");
	  
		def main(args: Array[String]): Unit = {
				val fusionTablesUtility = new FusionTablesUtility();
				val credential = fusionTablesUtility.authorize();
				logger.info("credential = " + credential);
    
				fusionTablesUtility.fusiontables = new Fusiontables.Builder(
						fusionTablesUtility.HTTP_TRANSPORT, fusionTablesUtility.JSON_FACTORY, credential)
						.setApplicationName("Google-FusionTablesSample/1.0").build();
				logger.info("queryTranslator.fusiontables = " + fusionTablesUtility.fusiontables);

				val tableId = "1pQBGUqR_g-j1WQavu-Fi1wGS7jsdRxomGc0DxMI";
				fusionTablesUtility.describeTable(tableId);
				fusionTablesUtility.showRows(tableId);
//    
//	val tableId = fusionTablesUtility.createTable();
//    fusionTablesUtility.insertData(tableId);
//    fusionTablesUtility.showRows(tableId);
//	fusionTablesUtility.deleteTable(tableId);
//        // success!
//
//	fusionTablesUtility.describeTable(tableId2);
//	fusionTablesUtility.showRows2(tableId2, "Country%20Code");
    
//    val tableId2 = "1YDlqQzZHVMPxSadct1u5cBBiN341xOCMcO6kSkU";
//	val sqlSelect = "SELECT 'Country code' AS countrycode\n";
//	val sqlFrom = " FROM " + tableId2 + "\n";
//	val sqlWhere = " WHERE 'Country code' NOT EQUAL TO '' AND 'Region' NOT EQUAL TO ''" + "\n";
//	val sqlString = sqlSelect + sqlFrom + sqlWhere;
	
//	val sqlString = this.testFusionTable().toString();
	
//	val sqlResponse = fusionTablesUtility.executeSQL(sqlString);
//	logger.info("sqlResponse = " + sqlResponse);
    
		}    
  }