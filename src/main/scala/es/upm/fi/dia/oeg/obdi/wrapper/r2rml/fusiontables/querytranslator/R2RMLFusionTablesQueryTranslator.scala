package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator

import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLQueryTranslator;
import es.upm.fi.dia.oeg.obdi.core.sql.SQLQuery;
import es.upm.fi.dia.oeg.obdi.core.model.AbstractMappingDocument;
import es.upm.fi.dia.oeg.obdi.core.engine.AbstractUnfolder;
import es.upm.fi.dia.oeg.obdi.core.model.AbstractConceptMapping
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLBetaGenerator;
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLCondSQLGenerator;
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLPRSQLGenerator;
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.engine.R2RMLElementUnfoldVisitor;
import es.upm.fi.dia.oeg.obdi.core.querytranslator.QueryTranslatorUtility;
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AlphaResult;
import es.upm.fi.dia.oeg.obdi.core.querytranslator.BetaResult;

import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.graph.Triple
import com.hp.hpl.jena.vocabulary.RDF;

import Zql.ZExp;

import org.apache.log4j.Logger;


import scala.collection.JavaConversions._


class R2RMLFusionTablesQueryTranslator  extends R2RMLQueryTranslator {
	val logger : Logger = Logger.getLogger("R2RMLFusionQueryTranslator");
	super.setUnfolder(new R2RMLElementUnfoldVisitor());
		
  override protected def buildAlphaGenerator(): Unit = {  
    super.setAlphaGenerator(new R2RMLFusionTablesAlphaGenerator());
  }

  override protected def buildBetaGenerator(): Unit = {  
	super.setBetaGenerator(new R2RMLFusionTablesBetaGenerator(this));
  }

  override protected def buildCondSQLGenerator(): Unit = {  
	super.setCondSQLGenerator(new R2RMLFusionTablesCondSQLGenerator(this));
  }

  override protected def buildPRSQLGenerator(): Unit = {  
    super.setPrSQLGenerator(new R2RMLFusionTablesPRSQLGenerator(this));
  }


}



object R2RMLFusionTablesQueryTranslator {
  
}