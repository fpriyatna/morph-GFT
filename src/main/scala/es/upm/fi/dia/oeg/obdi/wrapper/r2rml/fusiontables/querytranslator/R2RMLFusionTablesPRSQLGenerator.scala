package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator

import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLPRSQLGenerator
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractQueryTranslator
import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.graph.Triple
import es.upm.fi.dia.oeg.obdi.core.querytranslator.BetaResult
import es.upm.fi.dia.oeg.obdi.core.model.AbstractConceptMapping
import Zql.ZSelectItem
import java.util.Collection
import java.util.Vector

class R2RMLFusionTablesPRSQLGenerator(owner : AbstractQueryTranslator) 
extends R2RMLPRSQLGenerator(owner : AbstractQueryTranslator) {

  override def genPRSQLSubjectMappingId(subject:Node , cmSubject:AbstractConceptMapping ) 
  : Collection[ZSelectItem] = {
    new Vector[ZSelectItem]();
  }
  
  override def genPRSQLObjectMappingId(obj:Node,cmSubject:AbstractConceptMapping 
      ,predicateURI: String ) : Collection[ZSelectItem] = {
    new Vector[ZSelectItem]();
  }
}