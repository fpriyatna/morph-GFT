package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator

import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractAlphaGenerator
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractQueryTranslator
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AlphaResult
import com.hp.hpl.jena.graph.Triple
import es.upm.fi.dia.oeg.obdi.core.model.AbstractConceptMapping
import es.upm.fi.dia.oeg.obdi.core.model.AbstractPropertyMapping
import com.hp.hpl.jena.graph.Node
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AlphaResultUnion
import scala.collection.JavaConversions._
import org.apache.log4j.Logger
import es.upm.fi.dia.oeg.obdi.core.sql.SQLLogicalTable
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.model.R2RMLTriplesMap
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.engine.R2RMLElementUnfoldVisitor
import es.upm.fi.dia.oeg.obdi.core.sql.SQLQuery;
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLAlphaGenerator

class R2RMLFusionTablesAlphaGenerator() 
extends R2RMLAlphaGenerator() {
  val logger : Logger = Logger.getLogger("R2RMLFusionTablesAlphaGenerator");


	
	override def calculateAlphaSubject(
	    subject : Node, abstractConceptMapping : AbstractConceptMapping) : SQLLogicalTable = {
	  val sqlLogicalTable = super.calculateAlphaSubject(subject, abstractConceptMapping);
	  sqlLogicalTable.setAlias("");
		return sqlLogicalTable;	  
	}
}